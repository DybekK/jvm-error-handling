package functional

import core.Delivery
import core.Delivery.Companion.DeliveryId
import core.DeliveryRepository
import core.DeliveryType
import dev.forkhandles.result4k.Failure
import dev.forkhandles.result4k.Result
import dev.forkhandles.result4k.Success
import dev.forkhandles.result4k.flatMap
import java.time.LocalDateTime

class DeliveryService(private val deliveryRepository: DeliveryRepository) {
    fun getDelivery(id: DeliveryId, type: DeliveryType): Result<Delivery, DeliveryError> =
        getDelivery(id)
            .flatMap { it.validateDeliveryAgainstType(type) }
            .flatMap { it.validateDueDate() }

    private fun getDelivery(id: DeliveryId): Result<Delivery, DeliveryError> =
        deliveryRepository.findById(id)?.let { Success(it) } ?: Failure(DeliveryError.NotFound)

    private fun Delivery.validateDeliveryAgainstType(type: DeliveryType): Result<Delivery, DeliveryError> =
        when {
            type == DeliveryType.DROP_OFF && this.isEligibleForDropOff -> Success(this)
            type == DeliveryType.PICK_UP && this.isEligibleForPickup -> Success(this)
            else -> Failure(DeliveryError.WrongType)
        }

    private fun Delivery.validateDueDate(): Result<Delivery, DeliveryError> =
        if (due.isAfter(LocalDateTime.now())) Success(this) else Failure(DeliveryError.PastDueDate)
}