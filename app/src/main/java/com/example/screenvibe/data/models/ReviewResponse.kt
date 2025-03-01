import com.google.gson.annotations.SerializedName

data class ReviewResponse(
    val id: Int = 0,
    val page: Int = 0,
    @SerializedName("results") val reviews: List<Review> = emptyList(),
    @SerializedName("total_pages") val totalPages: Int = 0,
    @SerializedName("total_results") val totalResults: Int = 0
)