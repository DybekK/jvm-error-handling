package core

import java.time.LocalDateTime
import java.util.UUID

data class Delivery(
    val id: DeliveryId,
    val due: LocalDateTime,
    val isEligibleForPickup: Boolean = true,
    val isEligibleForDropOff: Boolean = true
) {
    companion object {
        @JvmInline
        value class DeliveryId(val value: UUID)
    }
}