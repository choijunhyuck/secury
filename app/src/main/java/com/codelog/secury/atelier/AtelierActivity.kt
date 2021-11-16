package com.codelog.secury.atelier

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.res.ResourcesCompat
import com.codelog.secury.R
import kotlinx.android.synthetic.main.activity_atelier.*

class AtelierActivity : AppCompatActivity() {

    override fun onStart() {
        super.onStart()
        setWidgets()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_atelier)
    }

    fun setWidgets(){

        // fonts
        atelierBackButton.typeface = ResourcesCompat.getFont(this, R.font.helvetica)

        // buttons-onclick
        atelierBackButton.setOnClickListener {
            finish()
        }

    }

}
