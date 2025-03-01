import com.example.screenvibe.data.models.Genre
import com.example.screenvibe.data.models.ProductionCountry
import com.google.gson.annotations.SerializedName

data class MovieDetailsResponse(
    val id: Int = 0,
    val title: String,
    @SerializedName("original_title") val originalTitle: String,
    @SerializedName("original_language") val originalLanguage: String,
    @SerializedName("overview") val overview: String,
    @SerializedName("popularity") val popularity: Double = 0.0,
    @SerializedName("poster_path") val posterPath: String?,
    @SerializedName("backdrop_path") val backdropPath: String?,
    @SerializedName("release_date") val releaseDate: String?,
    @SerializedName("runtime") val runtime: Int = 0,
    @SerializedName("revenue") val revenue: Int = 0,
    @SerializedName("budget") val budget: Int = 0,
    @SerializedName("status") val status: String,
    @SerializedName("tagline") val tagline: String?,
    @SerializedName("homepage") val homepage: String?,
    @SerializedName("imdb_id") val imdbId: String?,
    @SerializedName("vote_average") val voteAverage: Double = 0.0,
    @SerializedName("vote_count") val voteCount: Int = 0,
    @SerializedName("video") val hasVideo: Boolean = true,
    @SerializedName("adult") val isAdult: Boolean = true,
    @SerializedName("genres") val genres: List<Genre> = emptyList(),
    @SerializedName("belongs_to_collection") val belongsToCollection: CollectionInfo?,
    @SerializedName("production_companies") val productionCompanies: List<ProductionCompany> = emptyList(),
    @SerializedName("production_countries") val productionCountries: List<ProductionCountry> = emptyList(),
    @SerializedName("spoken_languages") val spokenLanguages: List<SpokenLanguage> = emptyList()
) {
    val posterFullPath: String
        get() = posterPath?.let { "https://image.tmdb.org/t/p/w500$it" } ?: ""

    val backdropFullPath: String
        get() = backdropPath?.let { "https://image.tmdb.org/t/p/w780$it" } ?: ""
}