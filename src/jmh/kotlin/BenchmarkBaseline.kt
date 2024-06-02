package org.example

import core.Delivery.Companion.DeliveryId
import core.DeliveryType
import org.openjdk.jmh.annotations.*
import org.openjdk.jmh.infra.Blackhole
import standard.DeliveryException
import java.util.UUID
import java.util.concurrent.TimeUnit


@Fork(1)
@Warmup(iterations = 3)
@Measurement(iterations = 10)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
open class BenchmarkBaseline {
    @State(Scope.Benchmark)
    open class BenchmarkState {
        val fixture = BenchmarkFixture

        val deliveryRepository = core.DeliveryRepository(fixture.deliveries)
        val functionalDeliveryService = functional.DeliveryService(deliveryRepository)
        val standardDeliveryService = standard.DeliveryService(deliveryRepository)
    }


    @Benchmark
    fun functionalWithNotFoundErrors(blackhole: Blackhole, state: BenchmarkState) {
        val deliveryId = DeliveryId(UUID.randomUUID())
        for (i in 1..100) {
            val result = state.functionalDeliveryService.getDelivery(deliveryId, DeliveryType.DROP_OFF)
            blackhole.consume(result)
        }
    }

    @Benchmark
    fun standardWithNotFoundErrors(blackhole: Blackhole, state: BenchmarkState) {
        val deliveryId = DeliveryId(UUID.randomUUID())
        for (delivery in state.fixture.deliveries) {
            try {
                state.standardDeliveryService.getDelivery(deliveryId, DeliveryType.DROP_OFF)
            } catch (e: DeliveryException) {
                blackhole.consume(e)
            }
        }
    }

    @Benchmark
    fun functionalWithDropOffErrors(blackhole: Blackhole, state: BenchmarkState) {
        for (delivery in state.fixture.deliveriesNotEligibleForDropOff) {
            val result = state.functionalDeliveryService.getDelivery(delivery.id, DeliveryType.DROP_OFF)
            blackhole.consume(result)
        }
    }

    @Benchmark
    fun standardWithDropOffErrors(blackhole: Blackhole, state: BenchmarkState) {
        for (delivery in state.fixture.deliveriesNotEligibleForDropOff) {
            try {
                state.standardDeliveryService.getDelivery(delivery.id, DeliveryType.DROP_OFF)
            } catch (e: DeliveryException) {
                blackhole.consume(e)
            }
        }
    }

    @Benchmark
    fun functionalWithPickupErrors(blackhole: Blackhole, state: BenchmarkState) {
        for (delivery in state.fixture.deliveriesNotEligibleForPickup) {
            val result = state.functionalDeliveryService.getDelivery(delivery.id, DeliveryType.PICK_UP)
            blackhole.consume(result)
        }
    }

    @Benchmark
    fun standardWithPickupErrors(blackhole: Blackhole, state: BenchmarkState) {
        for (delivery in state.fixture.deliveriesNotEligibleForPickup) {
            try {
                state.standardDeliveryService.getDelivery(delivery.id, DeliveryType.PICK_UP)
            } catch (e: DeliveryException) {
                blackhole.consume(e)
            }
        }
    }

    @Benchmark
    fun functionalWithPastDueDateErrors(blackhole: Blackhole, state: BenchmarkState) {
        for (delivery in state.fixture.deliveriesPastDueDate) {
            val result = state.functionalDeliveryService.getDelivery(delivery.id, DeliveryType.PICK_UP)
            blackhole.consume(result)
        }
    }

    @Benchmark
    fun standardWithPastDueDateErrors(blackhole: Blackhole, state: BenchmarkState) {
        for (delivery in state.fixture.deliveriesPastDueDate) {
            try {
                state.standardDeliveryService.getDelivery(delivery.id, DeliveryType.PICK_UP)
            } catch (e: DeliveryException) {
                blackhole.consume(e)
            }
        }
    }
}