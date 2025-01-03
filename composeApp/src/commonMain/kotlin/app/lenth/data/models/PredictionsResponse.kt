package app.lenth.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PredictionsResponse(
    @SerialName("predictions")
    val predictions: List<Prediction>,

    @SerialName("status")
    val status: String
)

@Serializable
data class Prediction(
    @SerialName("description")
    val description: String,

    @SerialName("matched_substrings")
    val matchedSubstrings: List<MatchedSubstring>,

    @SerialName("place_id")
    val placeId: String,

    @SerialName("reference")
    val reference: String,

    @SerialName("structured_formatting")
    val structuredFormatting: StructuredFormatting,

    @SerialName("terms")
    val terms: List<Term>,

    @SerialName("types")
    val types: List<String>
)

@Serializable
data class MatchedSubstring(
    @SerialName("length")
    val length: Int,

    @SerialName("offset")
    val offset: Int
)

@Serializable
data class StructuredFormatting(
    @SerialName("main_text")
    val mainText: String,

    @SerialName("main_text_matched_substrings")
    val mainTextMatchedSubstrings: List<MatchedSubstring>,

    @SerialName("secondary_text")
    val secondaryText: String
)

@Serializable
data class Term(
    @SerialName("offset")
    val offset: Int,

    @SerialName("value")
    val value: String
)
