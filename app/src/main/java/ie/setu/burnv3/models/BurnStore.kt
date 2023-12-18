package ie.setu.burnv3.models

import android.content.ContentValues.TAG
import android.util.Log
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseUser
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
                val route = document.toObject(Route::class.java).apply {
                    id = document.id
                }
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

fun getUserProfile(): FirebaseUser? {
    val user = Firebase.auth.currentUser
    user?.let {
        val name = it.displayName
        val email = it.email
        val photoUrl = it.photoUrl
    }
    return user
}


fun signOut(navController: NavController) {
    val user = Firebase.auth.currentUser

    try {
        //FirebaseAuth.getInstance().signOut()
        Firebase.auth.signOut()
    } catch (e: Exception) {
        Log.e("SignOut", "Error signing out: ${e.message}")
    }

    if (user != null) {
        Log.e("SignOut", "User has not been successfully logged out. User ID: $user")

    } else {
        navController.navigate("login") {
            popUpTo("home") { inclusive = true }
        }
    }
}

//fun updateUserProfile() {
//    val user = Firebase.auth.currentUser
//
//    val profileUpdates = userProfileChangeRequest {
//        displayName = "Jane Q. User"
//        //photoUri = Uri.parse("https://example.com/jane-q-user/profile.jpg")
//    }
//
//    user!!.updateProfile(profileUpdates)
//        .addOnCompleteListener { task ->
//            if (task.isSuccessful) {
//                Log.d(TAG, "User profile updated.")
//            }
//        }
//}

