package org.example

import core.Delivery
import core.Delivery.Companion.DeliveryId
import java.time.LocalDateTime
import java.util.*

object BenchmarkFixture {
    val deliveriesNotEligibleForDropOff = List(100) {
        Delivery(
            id = DeliveryId(UUID.randomUUID()),
            due = LocalDateTime.now().plusDays(1),
            isEligibleForDropOff = false
        )
    }

    val deliveriesNotEligibleForPickup = List(100) {
        Delivery(
            id = DeliveryId(UUID.randomUUID()),
            due = LocalDateTime.now().plusDays(1),
            isEligibleForPickup = false
        )
    }

    val deliveriesPastDueDate = List(100) {
        Delivery(
            id = DeliveryId(UUID.randomUUID()),
            due = LocalDateTime.now().minusDays(1)
        )
    }

    val deliveries = deliveriesNotEligibleForDropOff + deliveriesNotEligibleForPickup + deliveriesPastDueDate
}