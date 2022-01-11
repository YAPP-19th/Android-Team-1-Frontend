package yapp.android1.delibuddy.ui.myparty.adapter


/**
 * 주문 완료된 파티는 맨 아래로 내려야 할듯,
 * isCompleted ordering 해서 처리해주기!
 */
object MyPartyMockGenerator {

    fun getMyPartyList(): List<MyPartyMockEntity> {
        return listOf<MyPartyMockEntity>(
            MyPartyMockEntity(
                id = "1",
                location = "강남역 1번 출구",
                content = "나랑 엽떡 먹을 사람",
                date = "3월 13일 오전 10시 3분",
                isCompleted = false,
            ),
            MyPartyMockEntity(
                id = "2",
                location = "신논현역 1번 출구",
                content = "나랑 라면 먹을 사람",
                date = "3월 13일 오전 10시 3분",
                isCompleted = true,
            ),
            MyPartyMockEntity(
                id = "3",
                location = "강남역 1번 출구",
                content = "나랑 밥 먹을 사람",
                date = "3월 13일 오전 10시 3분",
                isCompleted = false,
            ),
            MyPartyMockEntity(
                id = "4",
                location = "영등포역 1번 출구",
                content = "나랑 김찌 먹을 사람",
                date = "3월 13일 오전 10시 3분",
                isCompleted = false,
            ),
            MyPartyMockEntity(
                id = "5",
                location = "신도림역 1번 출구",
                content = "나랑 된찌 먹을 사람",
                date = "3월 13일 오전 10시 3분",
                isCompleted = false,
            ),
            MyPartyMockEntity(
                id = "6",
                location = "서울대 입구역 3번 출구",
                content = "나랑 순찌 먹을 사람",
                date = "3월 13일 오전 10시 3분",
                isCompleted = true,
            ),
        )
    }
}

data class MyPartyMockEntity(
    val id: String,
    val location: String,
    val content: String,
    val date: String,
    val isCompleted: Boolean,
)
