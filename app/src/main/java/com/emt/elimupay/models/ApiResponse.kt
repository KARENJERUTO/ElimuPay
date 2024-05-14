import java.io.Serializable

data class LoginRequest(val email: String,val password: String): Serializable
data class APiResponse(
    val message: String,
    val status: Long,
    val entity: AppUser,
): Serializable

data class AppUser(
    val id: Long,
    val password: String,
   // @JsonProperty("last_login")
    val lastLogin: String,
    //@JsonProperty("is_superuser")
    val isSuperuser: Boolean,

    //@JsonProperty("first_name")
    val firstName: String,
    //@JsonProperty("last_name")
    val lastName: String,
    val email: String,
    //@JsonProperty("is_staff")
    val isStaff: Boolean,
   // @JsonProperty("is_active")
    val isActive: Boolean,
    //@JsonProperty("date_joined")
    val dateJoined: String,
    //@JsonProperty("middle_name")
    val middleName: String,
    val gender: String,
    //@JsonProperty("date_of_birth")
    val dateOfBirth: String,
    val address: String,
    val nationality: String,
    val groups: List<Any?>,
    //@JsonProperty("user_permissions")
    val userPermissions: List<Any?>,
    val roles: List<Any?>,
): Serializable
