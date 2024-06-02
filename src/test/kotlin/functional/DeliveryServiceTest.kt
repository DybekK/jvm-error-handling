package functional

import DeliveryFixture
import core.Delivery.Companion.DeliveryId
import core.DeliveryRepository
import core.DeliveryType
import dev.forkhandles.result4k.Failure
import dev.forkhandles.result4k.Success
import org.junit.jupiter.api.Test
import java.util.UUID
import kotlin.test.assertEquals

class DeliveryServiceTest {
    private val deliveryRepository = DeliveryRepository(DeliveryFixture.deliveries)
    private val deliveryService = DeliveryService(deliveryRepository)

    @Test
    fun `service should return a delivery`() {
        // given
        val delivery = DeliveryFixture.validDeliveries.random()

        // when
        val result = deliveryService.getDelivery(delivery.id, DeliveryType.DROP_OFF)

        // then
        assertEquals(Success(delivery), result)
    }

    @Test
    fun `service should return failure if delivery doesn't exist`() {
        // given
        val deliveryId = DeliveryId(UUID.randomUUID())

        // when
        val result = deliveryService.getDelivery(deliveryId, DeliveryType.DROP_OFF)

        // then
        assertEquals(Failure(DeliveryError.NotFound), result)
    }

    @Test
    fun `service should return failure if delivery is not eligible for pickup`() {
        // given
        val delivery = DeliveryFixture.deliveriesNotEligibleForPickup.random()

        // when
        val result = deliveryService.getDelivery(delivery.id, DeliveryType.PICK_UP)

        // then
        assertEquals(Failure(DeliveryError.WrongType), result)
    }

    @Test
    fun `service should return failure if delivery is not eligible for drop off`() {
        // given
        val delivery = DeliveryFixture.deliveriesNotEligibleForDropOff.random()

        // when
        val result = deliveryService.getDelivery(delivery.id, DeliveryType.DROP_OFF)

        // then
        assertEquals(Failure(DeliveryError.WrongType), result)
    }

    @Test
    fun `service should return failure if delivery is past due date`() {
        // given
        val delivery = DeliveryFixture.deliveriesPastDueDate.random()

        // when
        val result = deliveryService.getDelivery(delivery.id, DeliveryType.DROP_OFF)

        // then
        assertEquals(Failure(DeliveryError.PastDueDate), result)
    }
}