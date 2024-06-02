import core.Delivery
import core.Delivery.Companion.DeliveryId
import java.time.LocalDateTime
import java.util.*

object DeliveryFixture {
    val validDeliveries = List(10) {
        Delivery(
            id = DeliveryId(UUID.randomUUID()),
            due = LocalDateTime.now().plusDays(1),
        )
    }

    val deliveriesNotEligibleForDropOff = List(10) {
        Delivery(
            id = DeliveryId(UUID.randomUUID()),
            due = LocalDateTime.now().plusDays(1),
            isEligibleForDropOff = false
        )
    }

    val deliveriesNotEligibleForPickup = List(10) {
        Delivery(
            id = DeliveryId(UUID.randomUUID()),
            due = LocalDateTime.now().plusDays(1),
            isEligibleForPickup = false
        )
    }

    val deliveriesPastDueDate = List(10) {
        Delivery(
            id = DeliveryId(UUID.randomUUID()),
            due = LocalDateTime.now().minusDays(1)
        )
    }

    val deliveries =
        validDeliveries + deliveriesNotEligibleForDropOff + deliveriesNotEligibleForPickup + deliveriesPastDueDate
}