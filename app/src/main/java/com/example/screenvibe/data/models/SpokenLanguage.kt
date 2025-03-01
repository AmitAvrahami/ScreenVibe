import com.google.gson.annotations.SerializedName

data class SpokenLanguage(
    @SerializedName("english_name") val englishName: String,
    @SerializedName("iso_639_1") val languageCode: String,
    val name: String
)