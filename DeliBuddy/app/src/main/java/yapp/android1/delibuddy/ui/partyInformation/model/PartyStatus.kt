package yapp.android1.delibuddy.ui.partyInformation.model


enum class PartyStatus(val value: String) {
    RECRUIT("모집중"),
    ORDER("주문중"),
    COMPLETED("주문완료");

    companion object {
        fun of(value: String): PartyStatus {
            return when(value) {
                "모집중"    -> RECRUIT
                "주문중"    -> ORDER
                "주문완료"  -> COMPLETED
                else      -> throw RuntimeException("올바른 Status가 아닙니다")
            }
        }
    }
}