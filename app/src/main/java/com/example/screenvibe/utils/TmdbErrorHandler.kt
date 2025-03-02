package com.example.screenvibe.utils

object TmdbErrorHandler {

    fun getErrorMessage(code: Int?): String {
        return when (code) {
            2 -> "Invalid service: this service does not exist."
            3, 14, 30 -> "Authentication failed. Please check your login credentials."
            4 -> "Invalid format: This service doesn't exist in that format."
            5, 18, 20, 22, 23, 27, 28, 29, 47 -> "Invalid parameters. Please check your input and try again."
            6, 34, 37 -> "Invalid ID: The requested resource could not be found."
            7 -> "Invalid API key: You must be granted a valid key."
            8 -> "Duplicate entry: This data already exists."
            9, 46 -> "Service is temporarily offline. Try again later."
            10 -> "Suspended API key: Contact TMDB support."
            11, 15, 44 -> "Internal error: Something went wrong on the server."
            12 -> "Success: The item was updated successfully."
            13 -> "Success: The item was deleted successfully."
            16, 17, 33 -> "Session denied. Please reauthenticate."
            19 -> "Invalid accept header."
            21 -> "Entry not found: The item you are trying to edit cannot be found."
            24 -> "Request timeout: The server took too long to respond."
            25 -> "Rate limit exceeded: Too many requests. Try again later."
            26 -> "You must provide a username and password."
            31 -> "Account disabled. Contact TMDB support if this is an error."
            32 -> "Email not verified. Please verify your email to proceed."
            35, 36, 38, 39 -> "You do not have permission to access this resource."
            40 -> "Nothing to update."
            41 -> "This request token hasn't been approved by the user."
            42 -> "This request method is not supported."
            43 -> "Couldn't connect to the backend server."
            45 -> "This user has been suspended."
            else -> "An unknown error occurred. Please try again."
        }
    }
}