package standard

import DeliveryFixture
import core.Delivery.Companion.DeliveryId
import core.DeliveryRepository
import core.DeliveryType
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*
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
        assertEquals(delivery, result)
    }

    @Test
    fun `service should throw exception if delivery doesn't exist`() {
        // given
        val deliveryId = DeliveryId(UUID.randomUUID())

        // then
        assertThrows<DeliveryException.NotFound> {
            // when
            deliveryService.getDelivery(deliveryId, DeliveryType.DROP_OFF)
        }
    }

    @Test
    fun `service should return failure if delivery is not eligible for pickup`() {
        // given
        val delivery = DeliveryFixture.deliveriesNotEligibleForPickup.random()

        // then
        assertThrows<DeliveryException.WrongType> {
            // when
            deliveryService.getDelivery(delivery.id, DeliveryType.PICK_UP)
        }
    }

    @Test
    fun `service should return failure if delivery is not eligible for drop off`() {
        // given
        val delivery = DeliveryFixture.deliveriesNotEligibleForDropOff.random()

        // then
        assertThrows<DeliveryException.WrongType> {
            // when
            deliveryService.getDelivery(delivery.id, DeliveryType.DROP_OFF)
        }
    }

    @Test
    fun `service should return failure if delivery is past due date`() {
        // given
        val delivery = DeliveryFixture.deliveriesPastDueDate.random()

        // then
        assertThrows<DeliveryException.PastDueDate> {
            // when
            deliveryService.getDelivery(delivery.id, DeliveryType.PICK_UP)
        }
    }
}