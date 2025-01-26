package app.lenth.domain.history

import app.lenth.data.GeoCodingRepository
import app.lenth.domain.models.PlaceDomain
import co.touchlab.kermit.Logger

class GetStaticImageUseCase(private val geoCodingRepository: GeoCodingRepository) {
    suspend operator fun invoke(placeDomain: List<PlaceDomain>): ByteArray? {
        Logger.i("Call GetStaticImageUseCase")
        val ok = geoCodingRepository.getStaticMapUrlImage(places = placeDomain)
        Logger.i("GetStaticImageUseCase: $ok")
        return ok
    }

}