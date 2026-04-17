package movie.controller.dto

data class ReservationRequest(
    val reservations: List<SingleReservationRequest>,
    val usedPoints: Int,
    val paymentMethod: String,
)

data class SingleReservationRequest(
    val screeningId: Int,
    val seats: List<String>,
)

data class ReservationResponse(
    val reservationId: Int,
    val reservations: List<SingleReservationRequest>,
    val usedPoints: Int,
    val paymentMethod: String,
    val totalPrice: Int,
)
