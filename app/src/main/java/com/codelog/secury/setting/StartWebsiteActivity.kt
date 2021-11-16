package com.codelog.secury.setting

import android.os.Bundle
import android.util.Log
import android.webkit.URLUtil
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.codelog.secury.R
import com.codelog.secury.encrypt.EncryptLoad
import kotlinx.android.synthetic.main.activity_swebsite.*


class StartWebsiteActivity : AppCompatActivity() {

    var DEFAULT_URL:String = ""

    override fun onStart() {
        super.onStart()

        DEFAULT_URL = EncryptLoad.get(this).getString("DEFAULT_URL",
            "https://www.google.com")!!
        setWidgets()
    }

    fun setWidgets(){

        // fonts
        swebsiteTitle.typeface = ResourcesCompat.getFont(this, R.font.helvetica)
        swebsiteUrlText.typeface = ResourcesCompat.getFont(this, R.font.helvetica)
        swebsiteUrlInput.typeface = ResourcesCompat.getFont(this, R.font.helvetica)
        swebsiteUrlApply.typeface = ResourcesCompat.getFont(this, R.font.helvetica)

        // buttons-onclick
        swebG.setOnClickListener {
            EncryptLoad.get(this).edit().putString("DEFAULT_URL",
                "https://www.google.com").apply()
            onBackPressed()
        }

        swebYah.setOnClickListener {
            EncryptLoad.get(this).edit().putString("DEFAULT_URL",
                "https://www.yahoo.com").apply()
            onBackPressed()
        }

        swebM.setOnClickListener {

            EncryptLoad.get(this).edit().putString("DEFAULT_URL",
                "https://www.msn.com").apply()
            onBackPressed()
        }

        swebBi.setOnClickListener {

            EncryptLoad.get(this).edit().putString("DEFAULT_URL",
                "https://www.bing.com").apply()
            onBackPressed()
        }

        swebBa.setOnClickListener {

            EncryptLoad.get(this).edit().putString("DEFAULT_URL",
                "https://www.baidu.com").apply()
            onBackPressed()
        }

        swebN.setOnClickListener {

            EncryptLoad.get(this).edit().putString("DEFAULT_URL",
                "https://www.naver.com").apply()
            onBackPressed()
        }

        swebS.setOnClickListener {

            EncryptLoad.get(this).edit().putString("DEFAULT_URL",
                "https://www.sapo.pt").apply()
            onBackPressed()
        }

        swebYan.setOnClickListener {

            EncryptLoad.get(this).edit().putString("DEFAULT_URL",
                "https://www.yandex.com").apply()
            onBackPressed()
        }

        swebsiteBackButton.setOnClickListener {
            onBackPressed()
        }

        // custom url-onclick

        swebsiteUrlApply.setOnClickListener {

            var url = swebsiteUrlInput.text.toString()

            if(url.length > 0 && URLUtil.isValidUrl(url)){

                saveUrl(url)

            }else if(url.length > 0 && url.substring(0,4) == "www."){

                url = "https://"+url

                if(URLUtil.isValidUrl(url)){
                    saveUrl(url)
                }

            } else {

                Toast.makeText(this,R.string.toast1,Toast.LENGTH_LONG).show()

            }

        }

        // rest
        swebsiteUrlInput.setHint(DEFAULT_URL)

    }

    fun saveUrl(url:String){

        Log.d("TAG", DEFAULT_URL)
        EncryptLoad.get(this).edit().putString("DEFAULT_URL",
            url).apply()
        onBackPressed()
    }

    // main
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_swebsite)
    }


    // rest function

}
