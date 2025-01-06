package app.lenth.domain

import app.lenth.data.GeoCodingRepository

class SearchPlacesByInputQueryUseCase(private val geoCodingRepository: GeoCodingRepository) {
    suspend operator fun invoke(query: String): List<String> {
        return geoCodingRepository.getPlacesList(query)
    }
}