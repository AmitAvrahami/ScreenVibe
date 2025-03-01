import com.google.gson.annotations.SerializedName

data class CollectionInfo(
    val id: Int,
    val name: String,
    @SerializedName("poster_path") val posterPath: String?,
    @SerializedName("backdrop_path") val backdropPath: String?
) {
    val posterFullPath: String
        get() = posterPath?.let { "https://image.tmdb.org/t/p/w500$it" } ?: ""

    val backdropFullPath: String
        get() = backdropPath?.let { "https://image.tmdb.org/t/p/w780$it" } ?: ""
}