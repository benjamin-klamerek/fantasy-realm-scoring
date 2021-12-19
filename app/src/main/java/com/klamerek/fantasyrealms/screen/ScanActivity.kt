package com.klamerek.fantasyrealms.screen

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.common.InputImage
import com.klamerek.fantasyrealms.databinding.ActivityScanBinding
import com.klamerek.fantasyrealms.ocr.CardTitleRecognizer
import com.klamerek.fantasyrealms.util.Constants
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * Activity using camera and text recognition (ML kit) to detect cards from titles<br>
 * After scanning, activity is closed and result given back to the caller.
 *
 */
class ScanActivity : CustomActivity() {

    private var imageCapture: ImageCapture? = null
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var recognizer: CardTitleRecognizer
    private lateinit var binding: ActivityScanBinding

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
        cameraExecutor.shutdown()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EventBus.getDefault().register(this)
        binding = ActivityScanBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        manageCameraPermission()

        recognizer = CardTitleRecognizer(baseContext)
        cameraExecutor = Executors.newSingleThreadExecutor()
        binding.cameraCaptureButton.setOnClickListener {
            binding.scanProgressBar.visibility = View.VISIBLE
            binding.scanningLabel.visibility = View.VISIBLE
            binding.cameraPreview.visibility = View.GONE
            scan()
        }
    }

    class CardDetectedEvent(val indexes: List<Int>)

    @Subscribe
    fun closeActivity(cardDetected: CardDetectedEvent) {
        binding.scanProgressBar.visibility = View.INVISIBLE
        binding.scanningLabel.visibility = View.INVISIBLE
        binding.cameraPreview.visibility = View.VISIBLE
        val closingIntent = Intent()
        val answer = CardsSelectionExchange()
        answer.cardsSelected.addAll(cardDetected.indexes)
        closingIntent.putExtra(Constants.CARD_SELECTION_DATA_EXCHANGE_SESSION_ID, answer)
        setResult(Constants.RESULT_OK, closingIntent)
        finish()
    }

    private fun manageCameraPermission() {
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }
    }

    class MyImageCapturedCallback(private val recognizer: CardTitleRecognizer) :
        ImageCapture.OnImageCapturedCallback() {

        @ExperimentalGetImage
        override fun onCaptureSuccess(imageProxy: ImageProxy) {
            val mediaImage = imageProxy.image
            if (mediaImage != null) {
                val image =
                    InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
                imageProxy.close()
                recognizer.process(image).addOnSuccessListener {
                    EventBus.getDefault().post(CardDetectedEvent(it))
                }
            }
        }

        override fun onError(exception: ImageCaptureException) {
            Log.e(TAG, "Use case binding failed", exception)
        }
    }

    private fun scan() {
        val imageCapture = imageCapture ?: return
        imageCapture.takePicture(
            ContextCompat.getMainExecutor(this),
            MyImageCapturedCallback(recognizer)
        )
    }

    @Suppress("TooGenericExceptionCaught")
    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.cameraPreview.surfaceProvider)
                }

            imageCapture = ImageCapture.Builder().build()

            // Select back camera as a default
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)

            } catch (exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(this))
    }

    private fun allPermissionsGranted() =
        REQUIRED_PERMISSIONS.all {
            ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
        }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                Toast.makeText(
                    this,
                    "Permissions not granted by the user.",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    companion object {
        private const val TAG = "CameraXBasic"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }

}
