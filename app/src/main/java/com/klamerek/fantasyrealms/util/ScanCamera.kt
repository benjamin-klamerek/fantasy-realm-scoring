package com.klamerek.fantasyrealms.util

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.media.Image
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.core.UseCase
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.common.InputImage
import java.nio.ByteBuffer

/**
 * Constants about camera
 */
object Camera {
    const val TAG = "CameraXBasic"
    const val REQUEST_CODE_PERMISSIONS = 10
    val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
}

/**
 * Common interface for activities using camera
 */
interface CameraUseCase {

    fun getActivity(): ComponentActivity
    fun getCameraPreview(): Preview.SurfaceProvider
    fun getMainCameraUseCase(): UseCase

}

/**
 * Utility method to request camera permission if not given yet
 */
fun manageCameraPermission(cameraUseCase: CameraUseCase) {
    if (allPermissionsGranted(cameraUseCase.getActivity())) {
        startCamera(cameraUseCase)
    } else {
        ActivityCompat.requestPermissions(
            cameraUseCase.getActivity(),
            Camera.REQUIRED_PERMISSIONS,
            Camera.REQUEST_CODE_PERMISSIONS
        )
    }
}

/**
 * Delegates Camera request permission answer
 */
fun onRequestPermissionsResultDelegator(
    requestCode: Int, cameraUseCase: CameraUseCase
) {
    if (requestCode == Camera.REQUEST_CODE_PERMISSIONS) {
        if (allPermissionsGranted(cameraUseCase.getActivity())) {
            startCamera(cameraUseCase)
        } else {
            Toast.makeText(
                cameraUseCase.getActivity(),
                "Permissions not granted by the user.",
                Toast.LENGTH_SHORT
            ).show()
            cameraUseCase.getActivity().finishAfterTransition()
        }
    }
}

@Suppress("TooGenericExceptionCaught")
private fun startCamera(cameraUseCase: CameraUseCase) {
    val cameraProviderFuture = ProcessCameraProvider.getInstance(cameraUseCase.getActivity())

    cameraProviderFuture.addListener({
        // Used to bind the lifecycle of cameras to the lifecycle owner
        val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

        val preview = Preview.Builder()
            .build()
            .also {
                it.setSurfaceProvider(cameraUseCase.getCameraPreview())
            }

        // Select back camera as a default
        val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

        try {
            // Unbind use cases before rebinding
            cameraProvider.unbindAll()

            // Bind use cases to camera
            cameraProvider.bindToLifecycle(
                cameraUseCase.getActivity(),
                cameraSelector,
                preview,
                cameraUseCase.getMainCameraUseCase()
            )

        } catch (exc: Exception) {
            Log.e(Camera.TAG, "Use case binding failed", exc)
        }

    }, ContextCompat.getMainExecutor(cameraUseCase.getActivity()))
}

private fun allPermissionsGranted(context: Context) =
    Camera.REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
    }

/**
 * Convert image to InputImage<br>
 * This method as a workaround so solve this issue : https://github.com/benjamin-klamerek/fantasy-realm-scoring/issues/12<br>
 * I cannot guarantee that the problem is solved. But at least, app crash is avoided<br>
 */
fun convertImage(mediaImage: Image, rotationDegrees: Int): InputImage {
    return try {
        InputImage.fromMediaImage(mediaImage, rotationDegrees)
    } catch (e: IllegalArgumentException) {
        Log.e(Camera.TAG, "Could not parse image from media image, try workaround", e)
        val buffer: ByteBuffer = mediaImage.planes[0].buffer
        val bytes = ByteArray(buffer.capacity())
        buffer.get(bytes)
        val bitmapImage = BitmapFactory.decodeByteArray(bytes, 0, bytes.size, null)
        InputImage.fromBitmap(bitmapImage, rotationDegrees)
    }
}
