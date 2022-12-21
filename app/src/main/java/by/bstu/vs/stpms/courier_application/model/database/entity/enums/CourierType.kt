package by.bstu.vs.stpms.courier_application.model.database.entity.enums

enum class CourierType(
    val mode: String
) {
    Walk("walking"), Bicycle("bicycling"), Car("driving"), NotCourier("driving") //todo try to do smth with cycling
}