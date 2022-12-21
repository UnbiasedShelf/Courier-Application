package by.bstu.vs.stpms.courier_application.model.util.event

import android.util.Log

data class Result<out T>(val status: Status, val data: T?, val t: Throwable?) {

    companion object {
        @Deprecated(message = "loading not needed in updated architecture")
        fun <T> loading(): Result<T> {
            return Result(Status.LOADING, null, null)
        }

        fun <T> success(data: T?): Result<T> {
            return Result(Status.SUCCESS, data, null)
        }

        fun <T> error(t: Throwable): Result<T> {
            Log.d("Error", t.stackTraceToString())
            return Result(Status.ERROR, null, t)
        }
    }

    val isSuccessful = status == Status.SUCCESS
}
