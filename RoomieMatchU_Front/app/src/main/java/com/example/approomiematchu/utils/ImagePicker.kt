package com.example.approomiematchu.utils

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

// -------------------------------
// 🔹 Image Picker sencillo (Galería)
// -------------------------------
class ImagePickerLauncher(private val launchAction: () -> Unit) {
    fun launch() = launchAction()
}

@Composable
fun rememberImagePicker(onImageSelected: (Uri?) -> Unit): ImagePickerLauncher {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri -> onImageSelected(uri) }
    )

    return remember {
        ImagePickerLauncher { launcher.launch("image/*") }
    }
}

// -------------------------------
// 🔹 Image Picker avanzado (Galería + Cámara)
// -------------------------------
class AdvancedImagePickerLauncher(
    private val launchGalleryAction: () -> Unit,
    private val launchCameraAction: (Uri) -> Unit
) {
    fun launchGallery() = launchGalleryAction()
    fun launchCamera(outputUri: Uri) = launchCameraAction(outputUri)
}

@Composable
fun rememberAdvancedImagePicker(
    onImageSelected: (Uri?) -> Unit,
    onTakePhoto: ((Uri?) -> Unit)? = null
): AdvancedImagePickerLauncher {
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri -> onImageSelected(uri) }
    )

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            if (success) onTakePhoto?.invoke(null)
        }
    )

    return remember {
        AdvancedImagePickerLauncher(
            launchGalleryAction = { galleryLauncher.launch("image/*") },
            launchCameraAction = { outputUri -> cameraLauncher.launch(outputUri) }
        )
    }
}

// -------------------------------
// 🔹 Utilidades para archivos temporales
// -------------------------------
fun createTempImageFile(context: Context): File {
    val timeStamp = System.currentTimeMillis()
    val storageDir = context.externalCacheDir ?: context.cacheDir
    return File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir)
}

// -------------------------------
// 🔹 Conversión de URI a archivo temporal
// -------------------------------
fun uriToFile(context: Context, uriString: String): File? {
    return try {
        val uri = Uri.parse(uriString)
        val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
        val file = File.createTempFile("profile_image", ".jpg", context.cacheDir)

        inputStream?.use { input ->
            FileOutputStream(file).use { output ->
                input.copyTo(output)
            }
        }
        file
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}
