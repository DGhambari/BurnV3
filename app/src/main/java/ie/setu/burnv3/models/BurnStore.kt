package ie.setu.burnv3.models

import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.userProfileChangeRequest
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

fun getUserId(): String {
    lateinit var userId: String
    Firebase.auth.currentUser?.let { user ->
        userId = user.uid
    }
    return userId
}
fun getUserRoutes(userId: String, onRoutesReceived: (List<Route>) -> Unit) {
    val db = Firebase.firestore

    db.collection("routes")
        .whereEqualTo("userId", userId)
        .get()
        .addOnSuccessListener { result ->
            val routes = mutableListOf<Route>()
            for (document in result) {
                val route = document.toObject(Route::class.java)
                routes.add(route)
            }
            onRoutesReceived(routes)
        }
        .addOnFailureListener { exception ->
            Log.e("Firestore", "Error getting routes", exception)
        }
}

//fun getCurrentUser() {
//    val user = Firebase.auth.currentUser
//    if (user != null) {
//        // User is signed in
//    } else {
//        // No user is signed in
//    }
//}

//fun getUserProfile(user: UserModel){
//    val user = Firebase.auth.currentUser
//    user?.let {
//        val user.firstName = it.displayName
//        val email = it.email
//        val photoUrl = it.photoUrl
//
//        // Check if user's email is verified
//        val emailVerified = it.isEmailVerified
//
//        // The user's ID, unique to the Firebase project. Do NOT use this value to
//        // authenticate with your backend server, if you have one. Use
//        // FirebaseUser.getIdToken() instead.
//        val uid = it.uid
//    }
//}

fun sendPasswordReset() {

    val emailAddress = "user@example.com"

    Firebase.auth.sendPasswordResetEmail(emailAddress)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d(TAG, "Email sent.")
            }
        }
}

//fun signOut() {
//    AuthUI.getInstance()
//        .signOut(this)
//        .addOnCompleteListener {
//        }
//}

fun updateUserProfile(){
    val user = Firebase.auth.currentUser

    val profileUpdates = userProfileChangeRequest {
        displayName = "Jane Q. User"
        //photoUri = Uri.parse("https://example.com/jane-q-user/profile.jpg")
    }

    user!!.updateProfile(profileUpdates)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d(TAG, "User profile updated.")
            }
        }
}

