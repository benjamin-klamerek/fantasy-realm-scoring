package com.klamerek.fantasyrealms.screen

import android.content.Intent
import android.graphics.BitmapFactory
import android.media.Image
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.ComponentActivity
import androidx.camera.core.*
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.common.InputImage
import com.klamerek.fantasyrealms.databinding.ActivityScanBinding
import com.klamerek.fantasyrealms.ocr.CardTitleRecognizer
import com.klamerek.fantasyrealms.util.Camera.TAG
import com.klamerek.fantasyrealms.util.CameraUseCase
import com.klamerek.fantasyrealms.util.Constants
import com.klamerek.fantasyrealms.util.Constants.CARD_LIST_SOURCE_SCAN
import com.klamerek.fantasyrealms.util.manageCameraPermission
import com.klamerek.fantasyrealms.util.onRequestPermissionsResultDelegator
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import java.nio.ByteBuffer


/**
 * Activity using camera and text recognition (ML kit) to detect cards from titles<br>
 * After scanning, activity is closed and result given back to the caller.
 *
 */
class ScanActivity : CustomActivity(), CameraUseCase {

    private lateinit var imageCapture: ImageCapture
    private lateinit var recognizer: CardTitleRecognizer
    private lateinit var binding: ActivityScanBinding

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EventBus.getDefault().register(this)
        binding = ActivityScanBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        manageCameraPermission(this)
        imageCapture = ImageCapture.Builder().build()

        recognizer = CardTitleRecognizer(baseContext)
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
        answer.source = CARD_LIST_SOURCE_SCAN
        answer.cardsSelected.addAll(cardDetected.indexes)
        closingIntent.putExtra(Constants.CARD_SELECTION_DATA_EXCHANGE_SESSION_ID, answer)
        setResult(Constants.RESULT_OK, closingIntent)
        finishAfterTransition()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermissionsResultDelegator(requestCode, this)
    }

    private fun scan() {
        imageCapture.takePicture(
            ContextCompat.getMainExecutor(this),
            MyImageCapturedCallback(recognizer)
        )
    }

    override fun getActivity(): ComponentActivity = this

    override fun getCameraPreview(): Preview.SurfaceProvider = binding.cameraPreview.surfaceProvider

    override fun getMainCameraUseCase(): UseCase = imageCapture

    class MyImageCapturedCallback(private val recognizer: CardTitleRecognizer) :
        ImageCapture.OnImageCapturedCallback() {

        @ExperimentalGetImage
        override fun onCaptureSuccess(imageProxy: ImageProxy) {
            val mediaImage = imageProxy.image
            if (mediaImage != null) {
                val image = convertImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
                imageProxy.close()
                recognizer.process(image).addOnSuccessListener {
                    EventBus.getDefault().post(CardDetectedEvent(it))
                }
            }
        }

        /**
         * Convert image to InputImage<br>
         * This method as a workaround so solve this issue : https://github.com/benjamin-klamerek/fantasy-realm-scoring/issues/12<br>
         * I cannot guarantee that the problem is solved. But at least, app crash is avoided<br>
         */
        private fun convertImage(mediaImage: Image, rotationDegrees: Int): InputImage {
            return try {
                InputImage.fromMediaImage(mediaImage, rotationDegrees)
            } catch (e: IllegalArgumentException) {
                Log.e(TAG, "Could not parse image from media image, try workaround", e)
                val buffer: ByteBuffer = mediaImage.planes[0].buffer
                val bytes = ByteArray(buffer.capacity())
                buffer.get(bytes)
                val bitmapImage = BitmapFactory.decodeByteArray(bytes, 0, bytes.size, null)
                InputImage.fromBitmap(bitmapImage, rotationDegrees)
            }
        }

        override fun onError(exception: ImageCaptureException) {
            Log.e(TAG, "Use case binding failed", exception)
        }
    }


}
