package by.bstu.vs.stpms.courier_application.ui.util

import by.bstu.vs.stpms.courier_application.model.database.entity.Ordered
import java.sql.Timestamp
import java.time.Instant

object OrderValidator {
    private const val PHONE_PATTERN = "^(17|25|29|33|44)[0-9]{7}\$"

    fun isNameValid(name: String) = name.trim().length in 1..50
    fun isAddressValid(address: String) = address.length in 1..255
    fun isPhoneValid(phone: String) = phone.matches(Regex(PHONE_PATTERN))
    fun isProductValid(name: String) = isNameValid(name)

    fun isDestinationValid(name: String, phone: String, address: String) =
        isNameValid(name) && isPhoneValid(phone) && isAddressValid(address)

    fun isPickupRangeStartValid(prs: Timestamp?, pre: Timestamp?, drs: Timestamp?, dre: Timestamp?): Boolean {
        val now = Timestamp(Timestamp.from(Instant.now()).time)
        return prs?.let {
            it.after(now) && (pre == null || it.before(pre)) && (drs == null || it.before(drs)) && (dre == null || it.before(dre))
        } ?: false
    }

    fun isPickupRangeEndValid(prs: Timestamp?, pre: Timestamp?, drs: Timestamp?, dre: Timestamp?): Boolean {
        val now = Timestamp(Timestamp.from(Instant.now()).time)
        return pre?.let {
            it.after(now) && (prs == null || it.after(prs)) && (drs == null || it.before(drs)) && (dre == null || it.before(dre))
        } ?: false
    }

    fun isDeliveryRangeStartValid(prs: Timestamp?, pre: Timestamp?, drs: Timestamp?, dre: Timestamp?): Boolean {
        val now = Timestamp(Timestamp.from(Instant.now()).time)
        return drs?.let {
            it.after(now) && (prs == null || it.after(prs)) && (pre == null || it.after(pre)) && (dre == null || it.before(dre))
        } ?: false
    }

    fun isDeliveryRangeEndValid(prs: Timestamp?, pre: Timestamp?, drs: Timestamp?, dre: Timestamp?): Boolean {
        val now = Timestamp(Timestamp.from(Instant.now()).time)
        return dre?.let {
            it.after(now) && (prs == null || it.after(prs)) && (pre == null || it.after(pre)) && (drs == null || it.after(drs))
        } ?: false
    }

    fun isTimeValid(prs: Timestamp?, pre: Timestamp?, drs: Timestamp?, dre: Timestamp?): Boolean {
        return isPickupRangeStartValid(prs, pre, drs, dre)
                && isPickupRangeEndValid(prs, pre, drs, dre)
                && isDeliveryRangeStartValid(prs, pre, drs, dre)
                && isDeliveryRangeEndValid(prs, pre, drs, dre)
    }

    fun isFormValid(
        senderName: String,
        senderPhone: String,
        senderAddress: String,
        recipientName: String,
        recipientPhone: String,
        recipientAddress: String,
        products: List<Ordered>,
        prs: Timestamp?,
        pre: Timestamp?,
        drs: Timestamp?,
        dre: Timestamp?
    ) = isDestinationValid(senderName, senderPhone, senderAddress)
            && isDestinationValid(recipientName, recipientPhone, recipientAddress)
            && products.isNotEmpty() && products.all { isNameValid(it.product.name) }
            && isTimeValid(prs, pre, drs, dre)
}