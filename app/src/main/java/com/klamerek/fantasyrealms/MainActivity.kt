package com.klamerek.fantasyrealms

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.klamerek.fantasyrealms.databinding.ActivityMainBinding
import com.klamerek.fantasyrealms.screen.PlayerSelectionActivity

class MainActivity : AppCompatActivity() {

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

        val animationDrawable = binding.mainActConstraintLayout.background as AnimationDrawable
        animationDrawable.setEnterFadeDuration(2000)
        animationDrawable.setExitFadeDuration(4000)
        animationDrawable.start()
    }
}