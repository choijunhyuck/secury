package com.codelog.secury.setting

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.codelog.secury.R
import com.codelog.secury.atelier.AtelierActivity
import com.codelog.secury.code.NeedCodeActivity
import com.codelog.secury.code.NewCodeActivity
import com.codelog.secury.database.DatabaseHelper
import com.codelog.secury.encrypt.EncryptLoad
import com.codelog.secury.function.GetDomain
import kotlinx.android.synthetic.main.activity_setting.*


class SettingActivity : AppCompatActivity() {

    lateinit var DEFAULT_URL:String
    var LOAD_BACK_PAGE = true
    var DYNAMITE_TAB = "0"
    var ACCESS_CODE = false
    var SECURY_BOX = false
    var SECURY_ATELIER = false

    override fun onStart() {
        super.onStart()

        // get user option
        DEFAULT_URL = EncryptLoad.get(this).getString("DEFAULT_URL",
            "https://www.google.com")!!
        LOAD_BACK_PAGE = EncryptLoad.get(this).getBoolean("LOAD_BACK_PAGE",
            false)
        DYNAMITE_TAB = EncryptLoad.get(this).getString("DYNAMITE_TAB",
            "0")!!
        ACCESS_CODE = EncryptLoad.get(this).getBoolean("ACCESS_CODE",
            false)
        SECURY_BOX = EncryptLoad.get(this).getBoolean("SECURY_BOX",
            false)
        SECURY_ATELIER = EncryptLoad.get(this).getBoolean("SECURY_ATELIER",
            false)

        buttonSwitch()
        setWidgets()

    }

    fun setWidgets(){

        //fonts
        settingTitle.typeface = ResourcesCompat.getFont(this, R.font.helvetica)
        settingText1.typeface = ResourcesCompat.getFont(this, R.font.helvetica)
        settingText2.typeface = ResourcesCompat.getFont(this, R.font.helvetica)
        settingText3.typeface = ResourcesCompat.getFont(this, R.font.helvetica)
        settingText4.typeface = ResourcesCompat.getFont(this, R.font.helvetica)
        settingText5.typeface = ResourcesCompat.getFont(this, R.font.helvetica)
        settingText6.typeface = ResourcesCompat.getFont(this, R.font.helvetica)
        settingText7.typeface = ResourcesCompat.getFont(this, R.font.helvetica)
        settingText8.typeface = ResourcesCompat.getFont(this, R.font.helvetica)
        settingText9.typeface = ResourcesCompat.getFont(this, R.font.helvetica)
        settingText10.typeface = ResourcesCompat.getFont(this, R.font.helvetica)
        settingText11.typeface = ResourcesCompat.getFont(this, R.font.helvetica)
        settingText12.typeface = ResourcesCompat.getFont(this, R.font.helvetica)
        settingText13.typeface = ResourcesCompat.getFont(this, R.font.helvetica)

        //buttons-layout
        when(DYNAMITE_TAB.toInt()){

            0 -> settingText9.setText("Dnymite Tab")
            1 -> settingText9.setText("Dnymite Tab 1")
            2 -> settingText9.setText("Dnymite Tab 2")
            3 -> settingText9.setText("Dnymite Tab 3")
            4 -> settingText9.setText("Dnymite Tab 4")

        }

        correctColors()
        settingButton1.setText(GetDomain.getDomainName(DEFAULT_URL))

        //buttons-onclick
        settingButton1.setOnClickListener {
            startActivity(Intent(this, StartWebsiteActivity::class.java))
        }

        settingButton2.setOnClickListener {
            LOAD_BACK_PAGE = !LOAD_BACK_PAGE
            EncryptLoad.get(this).edit().putBoolean("LOAD_BACK_PAGE", LOAD_BACK_PAGE).apply()
            buttonSwitch()
        }

        settingButton3.setOnClickListener {
            startActivity(Intent(this, DynamiteActivity::class.java))
        }

        settingButton4.setOnClickListener {

            if(ACCESS_CODE){

                if(EncryptLoad.get(this).getBoolean("IS_LOCKED", false)){

                    EncryptLoad.get(this).edit().putBoolean("ACCESS_CODE_ACTION", true).apply()
                    startActivity(Intent(this, NeedCodeActivity::class.java))

                } else {

                    ACCESS_CODE = !ACCESS_CODE
                    EncryptLoad.get(this).edit()
                        .putBoolean("ACCESS_CODE", ACCESS_CODE)
                        .remove("ACCESS_CODE_STRING")
                        .apply()
                    buttonSwitch()

                }

            }else{
                startActivity(Intent(this, NewCodeActivity::class.java))
            }

        }

        settingButton5.setOnClickListener {

            if(SECURY_BOX){

                // show "all contents of secury box will deleted permanently "

                val builder = AlertDialog.Builder(this)
                builder.setTitle(R.string.settingToast1)
                builder.setMessage(R.string.settingToast2)
                //builder.setPositiveButton("OK", DialogInterface.OnClickListener(function = x))

                builder.setPositiveButton(android.R.string.yes) { dialog, which ->

                    //must remove all contents
                    val database = DatabaseHelper(this,null)
                    database.deleteAllLinks(this,EncryptLoad.get(this).getString("DB_PASSWORD","")!!)

                    SECURY_BOX = !SECURY_BOX
                    EncryptLoad.get(this).edit().putBoolean("SECURY_BOX", SECURY_BOX).apply()
                    buttonSwitch()

                }

                builder.setNegativeButton(android.R.string.no) { dialog, which ->
                    //cancel
                }
                builder.show()

            } else {
                SECURY_BOX = !SECURY_BOX
                EncryptLoad.get(this).edit().putBoolean("SECURY_BOX", SECURY_BOX).apply()

                buttonSwitch()
            }

        }

        settingButton6.setOnClickListener {
            startActivity(Intent(this, AtelierActivity::class.java))
        }

        settingText13.setOnClickListener {
            startActivity(Intent(this, ASettingActivity::class.java))
        }

        settingMailButton.setOnClickListener {

            val builder = AlertDialog.Builder(this)
            builder.setTitle(R.string.settingToast3)
            builder.setMessage(R.string.settingToast4)
            //builder.setPositiveButton("OK", DialogInterface.OnClickListener(function = x))

            builder.setPositiveButton(android.R.string.yes) { dialog, which ->


            }

            builder.show()

        }

        settingBackButton.setOnClickListener {
            onBackPressed()
        }

    }

    fun buttonSwitch(){

        if(LOAD_BACK_PAGE){
            settingButton2.setBackgroundResource(R.drawable.setting_asset_pluged)
        }else{
            settingButton2.setBackgroundResource(R.drawable.setting_asset_unpluged)
        }
        if(DYNAMITE_TAB != "0"){
            settingButton3.setBackgroundResource(R.drawable.setting_asset_pluged)
        }else{
            settingButton3.setBackgroundResource(R.drawable.setting_asset_unpluged)
        }
        if(ACCESS_CODE){
            settingButton4.setBackgroundResource(R.drawable.setting_asset_pluged)
        }else{
            settingButton4.setBackgroundResource(R.drawable.setting_asset_unpluged)
        }
        if(SECURY_BOX){
            settingButton5.setBackgroundResource(R.drawable.setting_asset_pluged)
        }else{
            settingButton5.setBackgroundResource(R.drawable.setting_asset_unpluged)
        }
        if(SECURY_ATELIER){
            settingButton6.setBackgroundResource(R.drawable.setting_asset_pluged)
        }else{
            settingButton6.setBackgroundResource(R.drawable.setting_asset_unpluged)
        }

    }

    fun correctColors(){

        when(DEFAULT_URL){
            "https://www.google.com" -> settingButton1.setBackgroundResource(R.drawable.rounded_rectangle_colorgoogle)
            "https://www.yahoo.com" -> settingButton1.setBackgroundResource(R.drawable.rounded_rectangle_coloryahoo)
            "https://www.msn.com" -> settingButton1.setBackgroundResource(R.drawable.rounded_rectangle_colormsn)
            "https://www.bing.com" -> settingButton1.setBackgroundResource(R.drawable.rounded_rectangle_colorbing)
            "https://www.baidu.com" -> settingButton1.setBackgroundResource(R.drawable.rounded_rectangle_colorbaidu)
            "https://www.naver.com" -> settingButton1.setBackgroundResource(R.drawable.rounded_rectangle_colornaver)
            "https://www.sapo.pt" -> settingButton1.setBackgroundResource(R.drawable.rounded_rectangle_colorsapo)
            "https://www.yandex.com" -> settingButton1.setBackgroundResource(R.drawable.rounded_rectangle_coloryandex)
            else -> settingButton1.setBackgroundResource(R.drawable.rounded_rectangle_colormsn)
        }

    }

    // main
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
    }

    // rest function

    override fun onResume() {
        super.onResume()
        buttonSwitch()
    }

}
