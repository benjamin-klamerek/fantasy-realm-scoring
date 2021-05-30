package com.klamerek.fantasyrealms.screen

import android.animation.AnimatorInflater
import android.animation.ObjectAnimator
import android.content.Intent
import android.graphics.drawable.Animatable2
import android.graphics.drawable.AnimatedVectorDrawable
import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.provider.Settings
import com.klamerek.fantasyrealms.R
import com.klamerek.fantasyrealms.databinding.ActivityMainBinding
import com.klamerek.fantasyrealms.util.Constants


class MainActivity : CustomActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.startButton.setOnClickListener {
            val playerSelectionIntent = Intent(this, PlayerSelectionActivity::class.java)
            startActivity(playerSelectionIntent)
        }

        binding.settingsButton.setOnClickListener {
            val settingIntent = Intent(this, SettingsActivity::class.java)
            startActivityForResult(settingIntent, Constants.SELECT_SETTINGS)
        }

        val animationActivated = Settings.Global.getFloat(
            baseContext.contentResolver,
            Settings.Global.WINDOW_ANIMATION_SCALE, 1f
        ) == 1f

        if (animationActivated) {

            val rotationAnimation = AnimatorInflater.loadAnimator(baseContext, R.animator.rotate_picture) as ObjectAnimator
            rotationAnimation.target = binding.bookIcon
            rotationAnimation.start()

            val backgroundAnimation = binding.mainActConstraintLayout.background as AnimationDrawable
            backgroundAnimation.setEnterFadeDuration(Constants.ANIMATION_ENTER_FADE_DURATION)
            backgroundAnimation.setExitFadeDuration(Constants.ANIMATION_EXIT_FADE_DURATION)
            backgroundAnimation.start()

            val bookColorAnimation = (binding.bookIcon.drawable as? AnimatedVectorDrawable)
            bookColorAnimation?.registerAnimationCallback(
                object : Animatable2.AnimationCallback() {
                    override fun onAnimationEnd(drawable: Drawable?) {
                        binding.bookIcon.post { bookColorAnimation.start() }
                    }
                }
            )
            bookColorAnimation?.start()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        recreate()
    }

}
