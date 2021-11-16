package com.codelog.secury.code

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.codelog.secury.R
import com.codelog.secury.encrypt.EncryptLoad
import com.codelog.secury.function.GetRandom
import kotlinx.android.synthetic.main.activity_new_code.*


class NewCodeActivity : AppCompatActivity() {

    // set widgets
    override fun onStart() {
        super.onStart()
        setWidgets()
    }

    fun setWidgets(){

        // font
        newText.typeface = ResourcesCompat.getFont(this, R.font.helvetica)
        newCodeText.typeface = ResourcesCompat.getFont(this, R.font.helvetica)
        newDoneButton.typeface = ResourcesCompat.getFont(this, R.font.helvetica)

        //buttons-onclick
        newDoneButton.setOnClickListener {
            onBackPressed()
        }

        // code action
        codeAction()

    }

    fun codeAction(){

        var ACCESS_CODE = GetRandom.getAccessCode(6)
        EncryptLoad.get(this).edit()
            .putBoolean("ACCESS_CODE", true)
            .putString("ACCESS_CODE_STRING", ACCESS_CODE)
            .putBoolean("IS_LOCKED", true).apply()
        newCodeText.setText(ACCESS_CODE)

    }

    // main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_code)

    }

    // rest function

}
