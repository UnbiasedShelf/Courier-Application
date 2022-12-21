package by.bstu.vs.stpms.courier_application.model.repository

import android.content.Context
import by.bstu.vs.stpms.courier_application.R
import by.bstu.vs.stpms.courier_application.model.database.CourierDatabase
import by.bstu.vs.stpms.courier_application.model.database.entity.Stats
import by.bstu.vs.stpms.courier_application.model.database.entity.User
import by.bstu.vs.stpms.courier_application.model.database.entity.UserRole
import by.bstu.vs.stpms.courier_application.model.database.entity.enums.CourierType
import by.bstu.vs.stpms.courier_application.model.exception.CourierNetworkException
import by.bstu.vs.stpms.courier_application.model.network.NetworkService
import by.bstu.vs.stpms.courier_application.model.network.NetworkService.context
import by.bstu.vs.stpms.courier_application.model.network.NetworkService.isOnline
import by.bstu.vs.stpms.courier_application.model.network.dto.UserDto
import by.bstu.vs.stpms.courier_application.model.repository.mapper.StatsMapper
import by.bstu.vs.stpms.courier_application.model.repository.mapper.UserMapper
import by.bstu.vs.stpms.courier_application.model.util.event.Result
import com.google.gson.Gson
import com.google.gson.JsonObject
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.mapstruct.factory.Mappers
import retrofit2.Response


object UserRepository {
    private val userApi = NetworkService.userApi.value
    private val db: CourierDatabase = CourierDatabase.getDatabase(context)

    //Вызывается при нажатии на кнопку входа в форме логина
    suspend fun login(login: String, password: String): Result<User> = try {
        //Формируем запрос к серверу, ответ - объект UserDto, при помощи CustomUserCallback
        //осуществляем обработку ошибок и отображение в User
        val result = userApi.login(login, password, true)

        userResponseHandler(result) { user ->
            writeUserId(user.id)
            db.clear()
            insertUserToDb(user)
        }
    } catch (e: Exception) {
        Result.error(e)
    }

    //Вызывается при регистрации
    suspend fun signUp(userDto: UserDto): Result<Unit> = try {
        val result = userApi.signUp(userDto)
        when (result.code()) {
            //Успешная регистрация
            201 -> {
                Result.success(Unit)
            }
            //Возвращается сервером при ошибке валидации
            400 -> {
                throw CourierNetworkException("Invalid form")
            }
            //Возвращается сервером, если пользователь уже существует
            403 -> {
                val gson = Gson()
                val jsonObject: JsonObject = gson.fromJson(
                    result.errorBody()?.string(),
                    JsonObject::class.java
                )
                throw CourierNetworkException(jsonObject["message"].asString)
            }
            else -> {
                throw CourierNetworkException("Network Troubles")
            }
        }
    } catch (e: Exception) {
        Result.error(e)
    }

    //Вызывается при запуске приложения, реализует логику попытки автоматического входа
    suspend fun tryAutoLogin(): Result<User> = try {
        val user = db.userDao.findById(getUserId())

        if (isOnline(context)) {
            //Если есть подключение к сети, попытка получить с сервера текущего пользователя
            if (user != null) {
                val result = userApi.currentUser()
                userResponseHandler(result) { userFromServer ->
                    val id = userFromServer.id
                    writeUserId(id)
                    val dbUser = db.userDao.findById(id)
                    if (dbUser == null) {
                        insertUserToDb(userFromServer)
                    }
                }
            } else {
                Result.error(CourierNetworkException("User not found"))
            }
        } else {
            //Если подключения к сети нет, попытка получить текущего пользователя с локальной бд
            if (user != null) {
                user.roles = HashSet(db.userRoleDao.getUserRolesByUserId(user.id))
                Result.success(user)
            } else {
                Result.error(CourierNetworkException("User not found"))
            }
        }
    } catch (e: Exception) {
        Result.error(e)
    }

    suspend fun getCurrentUser(): Result<User> = try {
        if (isOnline(context)) {
            //Если есть подключение к сети, попытка получить с сервера текущего пользователя
            val result = userApi.currentUser()
            userResponseHandler(result) { }
        } else {
            //Если подключения к сети нет, попытка получить текущего пользователя с локальной бд
            val user = db.userDao.findById(getUserId())
            if (user != null) {
                user.roles = HashSet(db.userRoleDao.getUserRolesByUserId(user.id))
                Result.success(user)
            } else {
                throw CourierNetworkException("User not found")
            }
        }
    } catch (e: Exception) {
        Result.error(e)
    }

    //Метод используется для получения статистики курьера
    suspend fun getUserStats(): Result<Stats> = try {
        val result = userApi.getStats()
        when (result.code()) {
            //Успешное получение данных
            200 -> {
                val mapper = Mappers.getMapper(StatsMapper::class.java)
                val stats = mapper.dtoToEntity(result.body())
                Result.success(stats)
            }
            //Возвращается сервером, если пользователь не имеет прав
            403 -> {
                throw CourierNetworkException("Your account is not verified")
            }
            else -> {
                throw CourierNetworkException("Network Troubles")
            }
        }
    } catch (e: Exception) {
        Result.error(e)
    }

    suspend fun logout() {
        //При выходе из учетной записи очищаем локальную базу данных (с информацией текущего пользователя)
        db.clear()
        //Запрос серверу для выхода (удаляет текущую сессию и cookies)
        if (isOnline(context)) {
            userApi.logout()
        }
        context
            .getSharedPreferences(
                context.getString(R.string.shared_prefs_user_info),
                Context.MODE_PRIVATE
            )
            .edit()
            .clear()
            .apply()
    }

    suspend fun changeType(newType: CourierType): Result<Unit> {
        return try {
            val response = userApi.changeType(
                newType.name.toRequestBody(
                    "text/plain".toMediaTypeOrNull()
                )
            )
            when (response.code()) {
                200 -> Result.success(Unit)
                else -> {
                    val gson = Gson()
                    val jsonObject: JsonObject = gson.fromJson(
                        response.errorBody()?.string(),
                        JsonObject::class.java
                    )
                    throw CourierNetworkException(jsonObject["message"].asString)
                }
            }
        } catch (e: Exception) {
            Result.error(e)
        }
    }

    private fun insertUserToDb(user: User) {
        //Вставляем объект User в базу данных
        db.userDao.insert(user)
        //Вставляем роли пользователя в базу данных
        for (role in user.roles) {
            db.userRoleDao.insert(UserRole().apply {
                userId = user.id
                roleId = role.id
            })
        }
        //В таблице изменений помечаем изменение как актуальное
        db.changeDao.setUpToDate("user", user.id)
    }

    private fun getUserId(): Long {
        return context.getSharedPreferences(context.getString(R.string.shared_prefs_user_info), Context.MODE_PRIVATE).getLong("id", 0)
    }

    private fun writeUserId(id: Long) {
        context
            .getSharedPreferences(context.getString(R.string.shared_prefs_user_info), Context.MODE_PRIVATE)
            .edit()
            .putLong("id", id)
            .apply()
    }

    private suspend fun userResponseHandler(
        response: Response<UserDto>,
        onStatusOkCallback: suspend (User) -> Unit
    ): Result<User> = when (response.code()) {
        200 -> {
            val dto: UserDto = response.body()!!
            val mapper = Mappers.getMapper(UserMapper::class.java)
            val user = mapper.dtoToEntity(dto)
            onStatusOkCallback(user)
            Result.success(user)
        }
        401 -> {
            throw CourierNetworkException("Invalid Credentials")
        }
        else -> {
            throw CourierNetworkException("Network Troubles")
        }
    }
}