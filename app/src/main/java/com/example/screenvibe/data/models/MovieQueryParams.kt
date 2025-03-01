package com.example.screenvibe.data.models

sealed class QueryParams { //TODO: handle append_to_response case

    abstract fun toMap(): Map<String, String>

    protected fun defaultParams(): MutableMap<String, String> {
        return mutableMapOf(
            "language" to "en-US",
            "include_adult" to "false",
            "page" to "1"
        )
    }


    data class DiscoverMovie(
        val certification: String? = null,
        val certificationGte: String? = null,
        val certificationLte: String? = null,
        val certificationCountry: String? = null,
        val includeAdult: Boolean = false,
        val includeVideo: Boolean = false,
        val language: String = "en-US",
        val page: Int = 1,
        val primaryReleaseYear: Int? = null,
        val primaryReleaseDateGte: String? = null,
        val primaryReleaseDateLte: String? = null,
        val region: String? = null,
        val releaseDateGte: String? = null,
        val releaseDateLte: String? = null,
        val sortBy: String = "popularity.desc",
        val voteAverageGte: Float? = null,
        val voteAverageLte: Float? = null,
        val voteCountGte: Int? = null,
        val voteCountLte: Int? = null,
        val watchRegion: String? = null,
        val withCast: String? = null,
        val withCompanies: String? = null,
        val withCrew: String? = null,
        val withGenres: String? = null,
        val withKeywords: String? = null,
        val withOriginCountry: String? = null,
        val withOriginalLanguage: String? = null,
        val withPeople: String? = null,
        val withReleaseType: String? = null,
        val withRuntimeGte: Int? = null,
        val withRuntimeLte: Int? = null,
        val withWatchMonetizationTypes: String? = null,
        val withWatchProviders: String? = null,
        val withoutCompanies: String? = null,
        val withoutGenres: String? = null,
        val withoutKeywords: String? = null,
        val withoutWatchProviders: String? = null,
        val year: Int? = null
    ) : QueryParams() {
        override fun toMap(): Map<String, String> {
            return defaultParams().apply {
                put("include_adult", includeAdult.toString())
                put("include_video", includeVideo.toString())
                put("page", page.toString())
                put("sort_by", sortBy)
                put("language", language)

                certification?.let { put("certification", it) }
                certificationGte?.let { put("certification.gte", it) }
                certificationLte?.let { put("certification.lte", it) }
                certificationCountry?.let { put("certification_country", it) }
                primaryReleaseYear?.let { put("primary_release_year", it.toString()) }
                primaryReleaseDateGte?.let { put("primary_release_date.gte", it) }
                primaryReleaseDateLte?.let { put("primary_release_date.lte", it) }
                region?.let { put("region", it) }
                releaseDateGte?.let { put("release_date.gte", it) }
                releaseDateLte?.let { put("release_date.lte", it) }
                voteAverageGte?.let { put("vote_average.gte", it.toString()) }
                voteAverageLte?.let { put("vote_average.lte", it.toString()) }
                voteCountGte?.let { put("vote_count.gte", it.toString()) }
                voteCountLte?.let { put("vote_count.lte", it.toString()) }
                watchRegion?.let { put("watch_region", it) }
                withCast?.let { put("with_cast", it) }
                withCompanies?.let { put("with_companies", it) }
                withCrew?.let { put("with_crew", it) }
                withGenres?.let { put("with_genres", it) }
                withKeywords?.let { put("with_keywords", it) }
                withOriginCountry?.let { put("with_origin_country", it) }
                withOriginalLanguage?.let { put("with_original_language", it) }
                withPeople?.let { put("with_people", it) }
                withReleaseType?.let { put("with_release_type", it) }
                withRuntimeGte?.let { put("with_runtime.gte", it.toString()) }
                withRuntimeLte?.let { put("with_runtime.lte", it.toString()) }
                withWatchMonetizationTypes?.let { put("with_watch_monetization_types", it) }
                withWatchProviders?.let { put("with_watch_providers", it) }
                withoutCompanies?.let { put("without_companies", it) }
                withoutGenres?.let { put("without_genres", it) }
                withoutKeywords?.let { put("without_keywords", it) }
                withoutWatchProviders?.let { put("without_watch_providers", it) }
                year?.let { put("year", it.toString()) }
            }
        }
    }

    data class SearchMovie(
        val query: String,
        val page: Int = 1,
        val region: String? = null,
        val year: Int? = null,
        val primaryReleaseYear: Int? = null,
        val includeAdult: Boolean = false
    ) : QueryParams() {
        override fun toMap(): Map<String, String> {
            return defaultParams().apply {
                put("query", query)
                put("include_adult", includeAdult.toString())
                put("page", page.toString())
                region?.let { put("region", it) }
                year?.let { put("year", it.toString()) }
                primaryReleaseYear?.let { put("primary_release_year", it.toString()) }
            }
        }
    }

    data class PopularMovies(
        val page: Int = 1,
        val region: String? = null,
        val language: String = "en-US",
    ) : QueryParams() {
        override fun toMap(): Map<String, String> {
            return defaultParams().apply {
                put("page", page.toString())
                region?.let { put("region", it) }
                put("language", language)
            }
        }
    }
    data class MovieDetails(
        val language: String = "en-US",
        val appendToResponse: String? = null,
    ) : QueryParams() {
        override fun toMap(): Map<String, String> {
            return defaultParams().apply {
                appendToResponse?.let { put("append_to_response", it) }
            }
        }
    }

    data class MovieReviews(
        val language: String = "en-US",
        val page: Int = 1
    ) : QueryParams() {
        override fun toMap(): Map<String, String> {
            return defaultParams().apply {
                put("page", page.toString())
            }
        }
    }
    data class Genres(
        val language: String = "en-US"
    ) : QueryParams() {
        override fun toMap(): Map<String, String> {
            return defaultParams()
        }
    }

    data object Configuration : QueryParams() {
        override fun toMap(): Map<String, String> = defaultParams()
    }


}