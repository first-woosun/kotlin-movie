package movie.view.input

import movie.domain.discountpolicy.PayMethod

object InputView {
    fun readYesOrNo(): Boolean {
        val value = inputTrim()?.uppercase() ?: ""
        return when (value) {
            "Y" -> true
            "N" -> false
            else -> throw IllegalArgumentException(LABEL.YES_OR_NO_ERROR)
        }
    }

    fun readStartReserve(): Boolean {
        println(LABEL.START_RESERVE_LABEL)
        return readYesOrNo()
    }

    fun readMovieTitle(): String {
        println(LABEL.INPUT_TITLE_LABEL)
        val value = inputTrim() ?: ""
        require(value.isNotBlank()) { LABEL.BLANK_ERROR }
        return value
    }

    fun readDate(): String {
        println(LABEL.INPUT_DATE_LABEL)
        val value = inputTrim() ?: ""
        require(value.isNotBlank()) { LABEL.BLANK_ERROR }
        return value
    }

    fun readScreeningNumber(movieCount: Int): Int {
        println(LABEL.INPUT_SCREENING_NUMBER_LABEL)
        val value = inputTrim()?.toIntOrNull() ?: 0
        require(value != 0) { LABEL.BLANK_ERROR }
        require(value in 1..movieCount) { LABEL.INVALID_INPUT_SCREENING_NUMBER }
        return value
    }

    fun readSeatNumber(): String {
        println(LABEL.INPUT_SEAT_LABEL)
        val value = inputTrim() ?: ""
        require(value.isNotBlank()) { LABEL.BLANK_ERROR }

        return value
    }

    fun readContinue(): Boolean {
        println(LABEL.CONTINUE_LABEL)
        return readYesOrNo()
    }

    fun readUsePoint(): Int {
        println(LABEL.USE_POINT_LABEL)
        val point = inputTrim()?.toIntOrNull()
        require(point != null) { LABEL.BLANK_ERROR }
        return point
    }

    fun readPayMethod(): PayMethod {
        println(LABEL.CHOSE_PAY_METHOD_LABEL)
        val value = inputTrim()?.toIntOrNull()
        require(value != null) { LABEL.BLANK_ERROR }
        return when (value) {
            1 -> PayMethod.CARD
            2 -> PayMethod.CASH
            else -> throw IllegalArgumentException(LABEL.INVALID_PAY_METHOD_NUMBER_ERROR)
        }
    }

    fun readPayAgreement(): Boolean {
        println(LABEL.PAY_AGREEMENT_LABEL)
        return readYesOrNo()
    }

    fun inputTrim() = readlnOrNull()?.trim()

    object LABEL {
        const val START_RESERVE_LABEL = "영화 예매를 시작합니다. 새 예매를 생성하시겠습니까? (Y/N)"
        const val YES_OR_NO_ERROR = "Y 또는 N을 입력해 주세요."
        const val INPUT_TITLE_LABEL = "예매할 영화 제목을 입력하세요:"
        const val BLANK_ERROR = "공백은 입력할 수 없습니다."
        const val INPUT_DATE_LABEL = "날짜를 입력하세요 (YYYY-MM-DD):"
        const val INVALID_DATE_FORMAT_ERROR = "올바른 날짜 형식(YYYY-MM-DD)이 아닙니다."
        const val INVALID_TYPE_ERROR = "날자에는 숫자만 입력할 수 있습니다."
        const val INPUT_SCREENING_NUMBER_LABEL = "상영 번호를 선택하세요:"
        const val INVALID_INPUT_SCREENING_NUMBER = "표시된 상영번호 중 선택해주세요"
        const val INPUT_SEAT_LABEL = "예약할 좌석을 입력하세요 (A1, B2):"
        const val INVALID_SEAT_NUMBER_FORMAT_ERROR = "좌석 번호는 '대문자+숫자' 형태여야 합니다."
        const val CONTINUE_LABEL = "다른 영화를 추가하시겠습니까? (Y/N)"
        const val USE_POINT_LABEL = "사용할 포인트를 입력하세요 (없으면 0):"
        const val CHOSE_PAY_METHOD_LABEL =
            "결제 수단을 선택하세요:\n" +
                "1) 신용카드(5% 할인)\n" +
                "2) 현금(2% 할인)"
        const val INVALID_PAY_METHOD_NUMBER_ERROR = "결제 수단은 1번과 2번 중 선택해주세요"
        const val PAY_AGREEMENT_LABEL = "위 금액으로 결제하시겠습니까? (Y/N)"
    }
}
