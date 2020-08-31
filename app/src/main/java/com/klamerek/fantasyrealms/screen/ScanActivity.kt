package com.klamerek.fantasyrealms.screen

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.klamerek.fantasyrealms.R
import com.klamerek.fantasyrealms.game.*
import kotlinx.android.synthetic.main.activity_scan.*
import me.xdrop.fuzzywuzzy.FuzzySearch
import me.xdrop.fuzzywuzzy.model.ExtractedResult
import normalize
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.math.abs

class ScanActivity : AppCompatActivity() {

    private var imageCapture: ImageCapture? = null

    private lateinit var cameraExecutor: ExecutorService
    private lateinit var recognizer: TextRecognizer
    private lateinit var cardWithCleanedName: Map<String, CardDefinition>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan)

        manageCameraPermission()

        cameraExecutor = Executors.newSingleThreadExecutor()
        recognizer = TextRecognition.getClient()
        cardWithCleanedName = allDefinitions.map { cleanText(it.name) to it }.toMap()
        cameraCaptureButton.setOnClickListener {
            scanProgressBar.visibility = View.VISIBLE
            scanningLabel.visibility = View.VISIBLE
            cameraPreview.visibility = View.GONE
            scan()
        }
    }

    private fun closeActivity(cardDetected: Set<CardDefinition>) {
        val closingIntent = Intent()
        val answer = CardsSelectionExchange()
        answer.cardsSelected.addAll(cardDetected.map { definition -> definition.id })
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

    private fun cleanText(input: String): String = input.toLowerCase().normalize().filter { it.isLetter() }

    private fun getBestMatching(input: String): Pair<CardDefinition, ExtractedResult> {
        val extractedResult = FuzzySearch.extractOne(input, cardWithCleanedName.keys)
        return Pair(cardWithCleanedName[extractedResult.string] ?: empty, extractedResult)
    }

    private fun scan() {
        val imageCapture = imageCapture ?: return

        imageCapture.takePicture(ContextCompat.getMainExecutor(this), object : ImageCapture.OnImageCapturedCallback() {

            override fun onCaptureSuccess(imageProxy: ImageProxy) {
                val mediaImage = imageProxy.image
                if (mediaImage != null) {
                    val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

                    recognizer.process(image).addOnSuccessListener { visionText ->
                        val cardDetected = visionText.textBlocks
                            .map { textBlock -> cleanText(textBlock.text) }
                            .filter { test -> test.length > 2 }
                            .map { text -> Pair(text, getBestMatching(text)) }
                            .filter { pair -> abs(pair.first.length - pair.second.second.string.length) < 4 }
                            .map { pair -> pair.second }
                            .filter { matching -> matching.second.score > 75 && matching.first != empty }
                            .map { matching -> matching.first }.toSet()
                        imageProxy.close()

                        scanProgressBar.visibility = View.GONE
                        scanningLabel.visibility = View.GONE
                        cameraPreview.visibility = View.VISIBLE
                        closeActivity(cardDetected)

                    }.addOnFailureListener { e ->
                        Log.e(TAG, "Error to get text", e)
                        imageProxy.close()
                    }
                }
            }

            override fun onError(exception: ImageCaptureException) {
                Log.e(TAG, "Use case binding failed", exception)
            }

        })
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener(Runnable {
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(cameraPreview.createSurfaceProvider())
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
        REQUIRED_PERMISSIONS.all { ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
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