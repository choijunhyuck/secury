package com.codelog.secury.setting

import android.app.AlertDialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.codelog.secury.R
import com.codelog.secury.encrypt.EncryptLoad
import kotlinx.android.synthetic.main.activity_asetting.*

class ASettingActivity : AppCompatActivity() {

    var ALLOW_WEB_CACHE = false
    var ALLOW_APP_CACHE = false
    var ALLOW_APP_COOKIES = false
    var USE_WIDE_WEB = false
    var USE_SAFE_BROWSING = false
    var USE_JAVASCRIPT = false
    var USE_DOMSTORAGE = false
    var USE_WIFI_ONLY = false

    override fun onStart() {
        super.onStart()

        // get user option
        ALLOW_WEB_CACHE = EncryptLoad.get(this).getBoolean("ALLOW_WEB_CACHE",
            false)
        ALLOW_APP_CACHE = EncryptLoad.get(this).getBoolean("ALLOW_APP_CACHE",
            false)
        ALLOW_APP_COOKIES = EncryptLoad.get(this).getBoolean("ALLOW_APP_COOKIES",
            false)
        USE_WIDE_WEB = EncryptLoad.get(this).getBoolean("USE_WIDE_WEB",
            false)
        USE_SAFE_BROWSING = EncryptLoad.get(this).getBoolean("USE_SAFE_BROWSING",
            false)
        USE_JAVASCRIPT = EncryptLoad.get(this).getBoolean("USE_JAVASCRIPT",
            false)
        USE_DOMSTORAGE = EncryptLoad.get(this).getBoolean("USE_DOMSTORAGE",
            false)
        USE_WIFI_ONLY = EncryptLoad.get(this).getBoolean("USE_WIFI_ONLY",
            false)

        buttonSwitch()
        setWidgets()

    }

    fun setWidgets(){

        //fonts
        asettingTitle.typeface = ResourcesCompat.getFont(this, R.font.helvetica)
        asettingText1.typeface = ResourcesCompat.getFont(this, R.font.helvetica)
        asettingText2.typeface = ResourcesCompat.getFont(this, R.font.helvetica)
        asettingText3.typeface = ResourcesCompat.getFont(this, R.font.helvetica)
        asettingText4.typeface = ResourcesCompat.getFont(this, R.font.helvetica)
        asettingText5.typeface = ResourcesCompat.getFont(this, R.font.helvetica)
        asettingText6.typeface = ResourcesCompat.getFont(this, R.font.helvetica)
        asettingText7.typeface = ResourcesCompat.getFont(this, R.font.helvetica)
        asettingText8.typeface = ResourcesCompat.getFont(this, R.font.helvetica)
        asettingText9.typeface = ResourcesCompat.getFont(this, R.font.helvetica)
        asettingText10.typeface = ResourcesCompat.getFont(this, R.font.helvetica)
        asettingText11.typeface = ResourcesCompat.getFont(this, R.font.helvetica)
        asettingText12.typeface = ResourcesCompat.getFont(this, R.font.helvetica)
        asettingText13.typeface = ResourcesCompat.getFont(this, R.font.helvetica)
        asettingText14.typeface = ResourcesCompat.getFont(this, R.font.helvetica)

        //buttons-onclick
        asettingButton.setOnClickListener {
            ALLOW_WEB_CACHE = !ALLOW_WEB_CACHE
            EncryptLoad.get(this).edit()
                .putBoolean("ALLOW_WEB_CACHE", ALLOW_WEB_CACHE)
                .apply()
            buttonSwitch()
        }

        asettingButton2.setOnClickListener {
            ALLOW_APP_CACHE = !ALLOW_APP_CACHE
            EncryptLoad.get(this).edit()
                .putBoolean("ALLOW_APP_CACHE", ALLOW_APP_CACHE)
                .apply()
            buttonSwitch()
        }

        asettingButton3.setOnClickListener {

            if(ALLOW_APP_COOKIES){

                ALLOW_APP_COOKIES = !ALLOW_APP_COOKIES
                EncryptLoad.get(this).edit()
                    .putBoolean("ALLOW_APP_COOKIES", ALLOW_APP_COOKIES)
                    .apply()
                buttonSwitch()

            }else{

                val builder = AlertDialog.Builder(this)
                builder.setTitle(R.string.asettingToast1)
                builder.setMessage(R.string.asettingToast2)
                //builder.setPositiveButton("OK", DialogInterface.OnClickListener(function = x))

                builder.setPositiveButton(android.R.string.yes) { dialog, which ->

                    ALLOW_APP_COOKIES = !ALLOW_APP_COOKIES
                    EncryptLoad.get(this).edit()
                        .putBoolean("ALLOW_APP_COOKIES", ALLOW_APP_COOKIES)
                        .apply()
                    buttonSwitch()

                }

                builder.setNegativeButton(android.R.string.no) { dialog, which ->
                    //cancel
                }
                builder.show()
            }

        }

        asettingButton4.setOnClickListener {
            USE_WIDE_WEB = !USE_WIDE_WEB
            EncryptLoad.get(this).edit()
                .putBoolean("USE_WIDE_WEB", USE_WIDE_WEB)
                .apply()
            buttonSwitch()
        }

        asettingButton5.setOnClickListener {

            if(USE_SAFE_BROWSING){

                val builder = AlertDialog.Builder(this)
                builder.setTitle(R.string.asettingToast3)
                builder.setMessage(R.string.asettingToast4)
                //builder.setPositiveButton("OK", DialogInterface.OnClickListener(function = x))

                builder.setPositiveButton(android.R.string.yes) { dialog, which ->

                    USE_SAFE_BROWSING = !USE_SAFE_BROWSING
                    EncryptLoad.get(this).edit()
                        .putBoolean("USE_SAFE_BROWSING", USE_SAFE_BROWSING)
                        .apply()
                    buttonSwitch()

                }

                builder.setNegativeButton(android.R.string.no) { dialog, which ->
                    //cancel
                }
                builder.show()

            }else{

                USE_SAFE_BROWSING = !USE_SAFE_BROWSING
                EncryptLoad.get(this).edit()
                    .putBoolean("USE_SAFE_BROWSING", USE_SAFE_BROWSING)
                    .apply()
                buttonSwitch()

            }
        }

        asettingButton6.setOnClickListener {
            // javascript, domstorage

            if(USE_JAVASCRIPT && USE_DOMSTORAGE){

                // start option
                val builder = AlertDialog.Builder(this)
                builder.setTitle(R.string.asettingToast5)
                builder.setMessage(R.string.asettingToast6)
                //builder.setPositiveButton("OK", DialogInterface.OnClickListener(function = x))

                builder.setPositiveButton(android.R.string.yes) { dialog, which ->

                    USE_JAVASCRIPT = !USE_JAVASCRIPT
                    USE_DOMSTORAGE = !USE_DOMSTORAGE
                    EncryptLoad.get(this).edit()
                        .putBoolean("USE_JAVASCRIPT", USE_JAVASCRIPT)
                        .putBoolean("USE_DOMSTORAGE", USE_DOMSTORAGE)
                        .apply()
                    buttonSwitch()

                }

                builder.setNegativeButton(android.R.string.no) { dialog, which ->
                    //cancel
                }
                builder.show()

            }else{

                // end option
                USE_JAVASCRIPT = !USE_JAVASCRIPT
                USE_DOMSTORAGE = !USE_DOMSTORAGE
                EncryptLoad.get(this).edit()
                    .putBoolean("USE_JAVASCRIPT", USE_JAVASCRIPT)
                    .putBoolean("USE_DOMSTORAGE", USE_DOMSTORAGE)
                    .apply()
                buttonSwitch()

            }

        }

        asettingButton7.setOnClickListener {

            // use wifi only
            USE_WIFI_ONLY = !USE_WIFI_ONLY
            EncryptLoad.get(this).edit()
                .putBoolean("USE_WIFI_ONLY", USE_WIFI_ONLY)
                .apply()
            buttonSwitch()

        }

        asettingBackButton.setOnClickListener {
            onBackPressed()
        }

    }

    fun buttonSwitch(){

        if(ALLOW_WEB_CACHE){
            asettingButton.setBackgroundResource(R.drawable.setting_asset_pluged)
        }else{
            asettingButton.setBackgroundResource(R.drawable.setting_asset_unpluged)
        }
        if(ALLOW_APP_CACHE){
            asettingButton2.setBackgroundResource(R.drawable.setting_asset_pluged)
        }else{
            asettingButton2.setBackgroundResource(R.drawable.setting_asset_unpluged)
        }
        if(ALLOW_APP_COOKIES){
            asettingButton3.setBackgroundResource(R.drawable.setting_asset_pluged)
        }else{
            asettingButton3.setBackgroundResource(R.drawable.setting_asset_unpluged)
        }
        if(USE_WIDE_WEB){
            asettingButton4.setBackgroundResource(R.drawable.setting_asset_pluged)
        }else{
            asettingButton4.setBackgroundResource(R.drawable.setting_asset_unpluged)
        }
        if(USE_SAFE_BROWSING){
            asettingButton5.setBackgroundResource(R.drawable.setting_asset_pluged)
        }else{
            asettingButton5.setBackgroundResource(R.drawable.setting_asset_unpluged)
        }

        if(USE_JAVASCRIPT && USE_DOMSTORAGE){
            asettingButton6.setBackgroundResource(R.drawable.setting_asset_unpluged)
        }else{
            asettingButton6.setBackgroundResource(R.drawable.setting_asset_pluged)
        }
        if(USE_WIFI_ONLY){
            asettingButton7.setBackgroundResource(R.drawable.setting_asset_pluged)
        }else{
            asettingButton7.setBackgroundResource(R.drawable.setting_asset_unpluged)
        }

    }

    // main
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_asetting)
    }

    // rest function

    override fun onResume() {
        super.onResume()
        buttonSwitch()
    }

}
