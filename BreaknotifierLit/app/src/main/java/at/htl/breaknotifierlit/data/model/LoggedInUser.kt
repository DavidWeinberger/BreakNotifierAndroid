package at.htl.breaknotifierlit.data.model

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
data class LoggedInUser(
    val userId: String,
    val displayName: String,
    val isLoggedIn: Boolean = false
)
