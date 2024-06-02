package core

import core.Delivery.Companion.DeliveryId

class DeliveryRepository(private val deliveries: List<Delivery>) {
    fun findById(id: DeliveryId): Delivery? =
        deliveries.find { it.id == id }
}
