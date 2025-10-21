package com.example.approomiematchu.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

// ----------------------------------------------------
// 🔹 Picker simple (galería)
// ----------------------------------------------------
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

// ----------------------------------------------------
// 🔹 Picker avanzado (galería + cámara)
// ----------------------------------------------------
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
    onTakePhoto: (Uri?) -> Unit
): AdvancedImagePickerLauncher {
    val context = LocalContext.current

    // Crear archivo temporal
    val photoFile = remember { createTempImageFile(context) }
    val photoUri = remember {
        FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            photoFile
        )
    }

    // Galería
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri -> onImageSelected(uri) }
    )

    // Cámara (usa URI segura con FileProvider)
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            if (success) onTakePhoto(photoUri)
        }
    )

    return remember {
        AdvancedImagePickerLauncher(
            launchGalleryAction = { galleryLauncher.launch("image/*") },
            launchCameraAction = { cameraLauncher.launch(photoUri) }
        )
    }
}

// ----------------------------------------------------
// 🔹 Utilidades de archivos
// ----------------------------------------------------
fun createTempImageFile(context: Context): File {
    val timeStamp = System.currentTimeMillis()
    val storageDir = context.externalCacheDir ?: context.cacheDir
    return File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir)
}

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
