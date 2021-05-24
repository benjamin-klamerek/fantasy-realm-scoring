package com.klamerek.fantasyrealms.screen

import android.animation.AnimatorInflater
import android.animation.ObjectAnimator
import android.content.Intent
import android.graphics.drawable.Animatable2
import android.graphics.drawable.AnimatedVectorDrawable
import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
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

        val anim = AnimatorInflater.loadAnimator(baseContext, R.animator.rotate_picture) as ObjectAnimator
        anim.target = binding.bookIcon
        anim.start()

        binding.startButton.setOnClickListener {
            val playerSelectionIntent = Intent(this, PlayerSelectionActivity::class.java)
            startActivity(playerSelectionIntent)
        }

        binding.settingsButton.setOnClickListener {
            val settingIntent = Intent(this, SettingsActivity::class.java)
            startActivityForResult(settingIntent, Constants.SELECT_SETTINGS)
        }

        val animationDrawable = binding.mainActConstraintLayout.background as AnimationDrawable
        animationDrawable.setEnterFadeDuration(Constants.ANIMATION_ENTER_FADE_DURATION)
        animationDrawable.setExitFadeDuration(Constants.ANIMATION_EXIT_FADE_DURATION)
        animationDrawable.start()
    }

    override fun onStart() {
        super.onStart()
        val animated = (binding.bookIcon.drawable as? AnimatedVectorDrawable)
        animated?.registerAnimationCallback(
            object : Animatable2.AnimationCallback() {
                override fun onAnimationEnd(drawable: Drawable?) {
                    binding.bookIcon.post{ animated.start() }
                }
            }
        )
        animated?.start()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        recreate()
    }

}
