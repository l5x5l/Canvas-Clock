package com.strayAlpaca.data.repository_impl

import com.strayAlpaca.domain.models.FaqData
import com.strayAlpaca.domain.models.ShortCut
import com.strayAlpaca.domain.repository.FaqRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FaqRepositoryImpl @Inject constructor() : FaqRepository {
    override suspend fun getFaqList(): List<FaqData> {
        return listOf(
            FaqData(title = "위젯이 멈추는 경우가 발생해요", answer = "설정-애플리케이션-CanvasClock-배터리 에서 '제한 없음' 으로 변경해주세요\n(기종 및 운영체제 종류에 따라 방법이 다를 수 있으며, CanvasClock의 배터리 사용 제한을 해제하시면 됩니다.)", shortCut = ShortCut.NATIVE_SETTING),
            FaqData(title = "위젯이 실제 시간보다 살짝 느리게 반응해요", answer = "CanvasClock에서 제공하는 위젯은 배터리 소모를 최소화하기 위해 1분 내외 주기로 업데이트됩니다. 이로 인해 실제 시간보다 1분 정도의 차이가 존재할 수 있습니다.")
        )
    }
}