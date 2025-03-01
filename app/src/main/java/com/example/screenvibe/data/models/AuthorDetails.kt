import com.google.gson.annotations.SerializedName

data class AuthorDetails(
    val name: String?,
    val username: String?,
    @SerializedName("avatar_path") val avatarPath: String?,
    @SerializedName("rating") val rating: Float?
) {
    val avatarFullPath: String
        get() = avatarPath?.let { "https://image.tmdb.org/t/p/w500$it" } ?: ""
}