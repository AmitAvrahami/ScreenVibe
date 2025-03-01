import com.google.gson.annotations.SerializedName

data class Review(
    val id: String,
    val author: String,
    @SerializedName("content") val content: String,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String?,
    @SerializedName("url") val url: String?,
    @SerializedName("author_details") val authorDetails: AuthorDetails
)