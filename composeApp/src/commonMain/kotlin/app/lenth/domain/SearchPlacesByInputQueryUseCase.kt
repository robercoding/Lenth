package app.lenth.domain

import app.lenth.data.GeoCodingRepository
import app.lenth.data.SearchTypeDomain

class SearchPlacesByInputQueryUseCase(private val geoCodingRepository: GeoCodingRepository) {
    suspend operator fun invoke(query: String, searchTypeDomain: SearchTypeDomain): List<String> {
        return geoCodingRepository.getPlacesList(query, searchTypeDomain)
    }
}