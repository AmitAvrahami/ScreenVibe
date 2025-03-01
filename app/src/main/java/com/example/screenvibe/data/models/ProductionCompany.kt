import com.google.gson.annotations.SerializedName

data class ProductionCompany(
    val id: Int,
    val name: String,
    @SerializedName("logo_path") val logoPath: String?,
    @SerializedName("origin_country") val originCountry: String
) {
    val logoFullPath: String
        get() = logoPath?.let { "https://image.tmdb.org/t/p/w500$it" } ?: ""
}