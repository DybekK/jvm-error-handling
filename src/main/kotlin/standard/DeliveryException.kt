package standard

sealed class DeliveryException : Exception() {
    class NotFound : DeliveryException()
    class WrongType : DeliveryException()
    class PastDueDate : DeliveryException()
}