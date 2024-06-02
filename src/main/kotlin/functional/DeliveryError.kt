package functional

sealed interface DeliveryError {
    data object NotFound: DeliveryError
    data object WrongType: DeliveryError
    data object PastDueDate: DeliveryError
}