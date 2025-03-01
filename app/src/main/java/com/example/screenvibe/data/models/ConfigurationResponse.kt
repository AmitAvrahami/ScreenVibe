import com.google.gson.annotations.SerializedName

data class ConfigurationResponse(
    @SerializedName("images") val images: ImageConfiguration,
    @SerializedName("change_keys") val changeKeys: List<String>
)