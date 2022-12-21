package by.bstu.vs.stpms.courier_application.model.repository

import android.util.Log
import by.bstu.vs.stpms.courier_application.model.database.CourierDatabase
import by.bstu.vs.stpms.courier_application.model.database.entity.Order
import by.bstu.vs.stpms.courier_application.model.exception.CourierNetworkException
import by.bstu.vs.stpms.courier_application.model.network.NetworkService
import by.bstu.vs.stpms.courier_application.model.network.NetworkService.context
import by.bstu.vs.stpms.courier_application.model.network.NetworkService.isOnline
import by.bstu.vs.stpms.courier_application.model.repository.mapper.OrderMapper
import by.bstu.vs.stpms.courier_application.model.util.event.Result
import com.google.gson.Gson
import com.google.gson.JsonObject
import org.mapstruct.factory.Mappers
import kotlin.Exception

object OrderRepository {
    private final val TAG = "OrderService"
    private val orderApi = NetworkService.orderApi.value
    private val db: CourierDatabase = CourierDatabase.getDatabase(context)

    //todo pagination?
    //Получение списка доступных заказов (только онлайн)
    suspend fun getAvailableOrders(): Result<List<Order>> = try {
        val response = orderApi.getAvailableOrders()
        when (response.code()) {
            //Успешное получение данных
            200 -> {
                val mapper = Mappers.getMapper(OrderMapper::class.java)
                val orders = mapper.dtosToEntities(response.body())
                Result.success(orders)
            }
            //Возвращается сервером, если пользователь не имеет роли курьера
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

    suspend fun getDeliveredOrders(): Result<List<Order>> = try {
        val response = orderApi.getDeliveredOrders()
        when (response.code()) {
            //Успешное получение данных
            200 -> {
                val mapper = Mappers.getMapper(OrderMapper::class.java)
                val orders = mapper.dtosToEntities(response.body())
                Result.success(orders)
            }
            //Возвращается сервером, если пользователь не имеет роли курьера
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

    //Получение списка активных заказов (доступно без подключения к интернету)
    suspend fun getActiveOrders(): Result<List<Order>> = if (isOnline(context)) {
        try {
            val response = orderApi.getActiveOrders()
            when (response.code()) {
                //Успешное получение данных
                200 -> {
                    val mapper = Mappers.getMapper(OrderMapper::class.java)
                    val orders = mapper.dtosToEntities(response.body())

                    val orderIdList = db.orderDao.getAll().map { order -> order.id }

                    //Обновление локальной базы данных
                    //Очистка записей связанных с заказами
                    db.orderedDao.truncate()
                    db.orderDao.truncate()

                    //Помечаем записи об удаленных заказах, как актуальные
                    orderIdList.forEach {
                        db.changeDao.setUpToDate("orders", it)
                    }

                    //Вставка актуальной информации о активных заказах в локальную бд
                    for (order in orders) {
                        insertOrderWithReplaceToLocalDb(order)
                    }
                    //Получение списка активных заказов из локальной бд
                    Result.success(getActiveOrdersFromLocalDb())
                }
                //Возвращается сервером, если пользователь не имеет роли курьера
                403 -> {
                    db.orderDao.getAll().forEach {
                        fillOrderFromDb(it)
                        deleteOrderAndOrderedFromLocalDb(it)
                    }
                    throw CourierNetworkException("Your account is not verified")
                }
                else -> {
                    throw CourierNetworkException("Network Troubles")
                }
            }
        } catch (e: Exception) {
            Result.error(e)
        }
    } else {
        Result.success(getActiveOrdersFromLocalDb())
    }

    suspend fun getOrderById(id: Long): Result<Order> = if (isOnline(context)) {
        try {
            val response = orderApi.getById(id)
            when (response.code()) {
                200 -> {
                    val mapper = Mappers.getMapper(OrderMapper::class.java)
                    val order = mapper.dtoToEntity(response.body())
                    Result.success(order)
                    insertOrderWithReplaceToLocalDb(order)
                    getOrderFromLocalDb(id)
                }
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
    } else {
        getOrderFromLocalDb(id)
    }

    suspend fun getCreatedOrders(): Result<List<Order>> = try {
        val response = orderApi.getCreatedOrders()
        when (response.code()) {
            //Успешное получение данных
            200 -> {
                val mapper = Mappers.getMapper(OrderMapper::class.java)
                val orders = mapper.dtosToEntities(response.body())
                Result.success(orders)
            }
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

    suspend fun getHistory(): Result<List<Order>> = try {
        val response = orderApi.getHistory()
        when (response.code()) {
            //Успешное получение данных
            200 -> {
                val mapper = Mappers.getMapper(OrderMapper::class.java)
                val orders = mapper.dtosToEntities(response.body())
                Result.success(orders)
            }
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

    //Принять заказ (только онлайн)
    suspend fun accept(order: Order): Result<Unit> = try {
        val result = orderApi.acceptOrder(order.id)
        if (result.isSuccessful) {
            insertOrderWithReplaceToLocalDb(order)
            Result.success(Unit)
        } else {
            val gson = Gson()
            val jsonObject: JsonObject =
                gson.fromJson(result.errorBody()?.string(), JsonObject::class.java)
            Result.error(CourierNetworkException(jsonObject["message"].asString))
        }
    } catch (e: Exception) {
        Result.error(e)
    }


    //Отмена заказа (доступно без подключения к интернету)
    suspend fun decline(order: Order): Result<Unit> = try {
        //Удаляем запись о заказе из локальной бд
        //При удалении в таблице changes появится соответствующая запись, при возобновлении соединения,
        //по этой записи можно будет отменить заказ на сервере для синхронизации
        deleteOrderAndOrderedFromLocalDb(order)
        //Если есть интернет, то выполняем отмену заказа и на сервере
        if (isOnline(context)) {
            val result = orderApi.declineOrder(order.id)
            if (!result.isSuccessful) {
                val gson = Gson()
                val jsonObject: JsonObject =
                    gson.fromJson(result.errorBody()?.string(), JsonObject::class.java)
                throw CourierNetworkException(jsonObject["message"].asString)
            }
        }
        Result.success(Unit)

    } catch (e: Exception) {
        Result.error(e)
    }

    suspend fun updateState(order: Order): Result<Order> = try {
        val nextState = order.state.next

        if (isOnline(context)) {
            val result = orderApi.updateState(order.id, nextState.name)
            if (!result.isSuccessful) {
                val gson = Gson()
                val jsonObject: JsonObject =
                    gson.fromJson(result.errorBody()?.string(), JsonObject::class.java)
                throw CourierNetworkException(jsonObject["message"].asString)
            }
        }

        db.orderDao.updateState(order.id, nextState.name)
        val newOrderState = db.orderDao.findById(order.id)!!.state
        order.state = newOrderState
        Result.success(order)

    } catch (e: Exception) {
        Result.error(e)
    }

    //Метод вызывается при появлении соединения для отправки информации о заказах, от которых курьер отказался
    suspend fun sendDecline() {
        //Получаем список ID заказов, которые были удалены из таблицы заказов во время оффлайн использования
        val declinedOrdersIdList = db.changeDao.findAllByTableAndOperation("orders", "delete")
            .filter { change -> !change.isUpToDate }.map { it.itemId }
        Log.d(TAG, "sendDecline: declined in offline - $declinedOrdersIdList")
        for (id in declinedOrdersIdList) {
            //Отправляем соответствующий отмене заказа запрос на сервер
            try {
                val result = orderApi.declineOrder(id)
                //Удаляем запись в таблице изменений для предотвращения последующего повторения
                db.changeDao.deleteByTableAndItemId("orders", id)
                Log.d(TAG, "sendDecline status: " + result.code())
            } catch (e: Exception) {
                Log.d(TAG, "sendDecline error: ${e.message}")
            }
        }
    }

    //Метод вызывается при появлении соединения для отправки информации о заказах, состояние которых было обновлено курьером
    suspend fun sendUpdateState() {
        //Получаем список заказов, состояние которых было обновлено во время оффлайн использования
        val updateStateOrdersIdList =
            db.changeDao.findAllByTableAndOperation("orders", "update").map { it.itemId }
        Log.d(TAG, "sendUpdateState: updated in offline - $updateStateOrdersIdList")
        for (id in updateStateOrdersIdList) {
            //Получаем состояние заказа из БД по id
            val state = db.orderDao.getStateById(id)
            //Отправляем соответствующий обновлению состояния заказа запрос на сервер
            try {
                val result = orderApi.updateState(id, state)
                //Удаляем запись в таблице изменений для предотвращения последующего повторения
                db.changeDao.deleteByTableAndItemId("orders", id)
                Log.d(TAG, "sendUpdate status: ${result.code()}")
            } catch (e: Exception) {
                Log.d(TAG, "sendUpdate error: ${e.message}")
            }
        }
    }

    suspend fun save(order: Order): Result<Unit> = try {
        val mapper = Mappers.getMapper(OrderMapper::class.java)
        val response = orderApi.save(mapper.entityToDto(order))
        if (response.isSuccessful)
            Result.success(Unit)
        else {
            val gson = Gson()
            val jsonObject: JsonObject =
                gson.fromJson(response.errorBody()?.string(), JsonObject::class.java)
            throw CourierNetworkException(jsonObject["message"].asString)
        }
    } catch (e: Exception) {
        Result.error(e)
    }


    private suspend fun insertOrderWithReplaceToLocalDb(order: Order) {
        db.userDao.insertWithReplace(order.customer)
        if (order.courier != null) {
            db.userDao.insertWithReplace(order.courier)
        }
        db.destinationDao.insertWithReplace(order.sender)
        db.destinationDao.insertWithReplace(order.recipient)
        Log.d("OrderService", "customer")
        db.productDao.insertAll(order.ordered.map { it.product })
        Log.d("OrderService", "products $order")
        db.orderDao.insertWithReplace(order)
        Log.d("OrderService", "order")
        db.orderedDao.insertAll(order.ordered.toList())
        Log.d("OrderService", "ordered")
        db.changeDao.setUpToDate("orders", order.id)
        Log.d("OrderService", "change")
    }

    private suspend fun deleteOrderAndOrderedFromLocalDb(order: Order) {
        db.orderedDao.deleteAll(order.ordered)
        db.productDao.deleteAll(order.ordered.map { it.product })
        db.orderDao.delete(order)
    }

    private suspend fun fillOrderFromDb(order: Order) {
        order.customer = db.userDao.findById(order.customerId)
        if (order.courierId != null) {
            order.courier = db.userDao.findById(order.courierId)
        }
        order.sender = db.destinationDao.findById(order.senderId)
        order.recipient = db.destinationDao.findById(order.recipientId)
        val orderedList = db.orderedDao.findByOrderId(order.id)
        for (ordered in orderedList) {
            val product = db.productDao.findById(ordered.productId)
            ordered.product = product
        }
        order.ordered = orderedList

    }

    private suspend fun getActiveOrdersFromLocalDb(): List<Order> {
        //Получаем активные заказы из локальной бд
        val orders = db.orderDao.getAll()
        for (order in orders) {
            fillOrderFromDb(order)
        }
        return orders
    }

    private suspend fun getOrderFromLocalDb(id: Long): Result<Order> {
        val order = db.orderDao.findById(id)
        return if (order != null) {
            fillOrderFromDb(order)
            Result.success(order)
        } else {
            Result.error(CourierNetworkException("No order with id $id in local db"))
        }

    }

}