package standard

import core.Delivery
import core.Delivery.Companion.DeliveryId
import core.DeliveryRepository
import core.DeliveryType
import java.time.LocalDateTime

class DeliveryService(private val deliveryRepository: DeliveryRepository) {
    fun getDelivery(id: DeliveryId, type: DeliveryType): Delivery =
        getDelivery(id)
            .validateDeliveryAgainstType(type)
            .validateDueDate()

    private fun getDelivery(id: DeliveryId): Delivery =
        deliveryRepository.findById(id) ?: throw DeliveryException.NotFound()

    private fun Delivery.validateDeliveryAgainstType(type: DeliveryType): Delivery =
        when {
            type == DeliveryType.DROP_OFF && this.isEligibleForDropOff -> this
            type == DeliveryType.PICK_UP && this.isEligibleForPickup -> this
            else -> throw DeliveryException.WrongType()
        }

    private fun Delivery.validateDueDate(): Delivery =
        if (due.isAfter(LocalDateTime.now())) this else throw DeliveryException.PastDueDate()
}