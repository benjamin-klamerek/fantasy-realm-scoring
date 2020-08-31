package com.klamerek.fantasyrealms

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.klamerek.fantasyrealms.screen.PlayerSelectionActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startButton.setOnClickListener {
            val playerSelectionIntent = Intent(this, PlayerSelectionActivity::class.java)
            startActivity(playerSelectionIntent)
        }

    }
}