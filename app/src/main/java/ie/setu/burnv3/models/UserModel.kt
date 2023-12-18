package ie.setu.burnv3.models

data class UserModel(
    var userId: String? = null,
    var email: String,
    var firstName: String,
    var lastName: String,
)