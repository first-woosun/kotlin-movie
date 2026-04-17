package movie.controller

import movie.controller.dto.ReservationRequest
import movie.controller.dto.ReservationResponse
import movie.service.ReservationService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/reservations")
class ReservationController(
    private val reservationService: ReservationService,
) {
    @PostMapping
    fun createReservation(
        @RequestBody request: ReservationRequest,
    ): ResponseEntity<ReservationResponse> {
        val response = reservationService.reserveAll(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }
}
