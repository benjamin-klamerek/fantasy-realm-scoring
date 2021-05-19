package com.klamerek.fantasyrealms

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import com.klamerek.fantasyrealms.databinding.ActivityMainBinding
import com.klamerek.fantasyrealms.util.Constants
import com.klamerek.fantasyrealms.screen.CustomActivity
import com.klamerek.fantasyrealms.screen.PlayerSelectionActivity
import com.klamerek.fantasyrealms.screen.SettingsActivity

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

        val animationDrawable = binding.mainActConstraintLayout.background as AnimationDrawable
        animationDrawable.setEnterFadeDuration(Constants.ANIMATION_ENTER_FADE_DURATION)
        animationDrawable.setExitFadeDuration(Constants.ANIMATION_EXIT_FADE_DURATION)
        animationDrawable.start()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        recreate()
    }

}
