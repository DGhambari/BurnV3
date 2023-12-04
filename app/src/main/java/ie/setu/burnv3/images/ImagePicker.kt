package ie.setu.burnv3.images

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import coil.compose.rememberAsyncImagePainter

@Composable
fun ImagePicker(
    onImageUploaded: (String) -> Unit,
    onImageUploading: (Boolean) -> Unit
) {
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var isImageUploading by remember { mutableStateOf(false) }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImageUri = uri
        uri?.let {
            isImageUploading = true
            onImageUploading(true)
            uploadImageToFirebaseStorage(
                it,
                onUploadEnd = { uploadedImageUrl ->
                    isImageUploading = false
                    onImageUploading(false)
                    if (uploadedImageUrl != null) {
                        onImageUploaded(uploadedImageUrl)
                    }
                }
            )
        }
    }

    Column (
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    )
        {
        FilledTonalButton(onClick = { imagePickerLauncher.launch("image/*") }) {
            Text("Pick Image")
        }
            Spacer(modifier = Modifier.height(8.dp))
        selectedImageUri?.let { uri ->
            Image(painter = rememberAsyncImagePainter(uri), contentDescription = "Selected Image")
        }
    }
}

fun uploadImageToFirebaseStorage(
    imageUri: Uri,
    onUploadEnd: (String?) -> Unit
) {
    val storageRef = Firebase.storage.reference
    val imageRef = storageRef.child("images/${imageUri.lastPathSegment}")
    val uploadTask = imageRef.putFile(imageUri)

    uploadTask.addOnSuccessListener { taskSnapshot ->
        taskSnapshot.storage.downloadUrl.addOnSuccessListener { downloadUrl ->
            onUploadEnd(downloadUrl.toString())
        }
    }.addOnFailureListener {exception ->
        Log.e("Storage", "Upload failed", exception)
        onUploadEnd(null)
    }
}