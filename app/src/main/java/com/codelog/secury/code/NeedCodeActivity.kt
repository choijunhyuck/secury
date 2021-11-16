package com.codelog.secury.code

import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.codelog.secury.R
import com.codelog.secury.database.DatabaseHelper
import com.codelog.secury.encrypt.EncryptLoad
import kotlinx.android.synthetic.main.activity_need_code.*


class NeedCodeActivity : AppCompatActivity() {

    var SPLASH_TIME_OUT:Long = 1500
    var END_LIMIT:Long = 400
    var FAILED_COUNT = 0

    override fun onStart() {
        super.onStart()

        if(EncryptLoad.get(this).getInt("FAILED_COUNT", 0) != 0){
            FAILED_COUNT = EncryptLoad.get(this).getInt("FAILED_COUNT", 0)
            indicatorAction()
        }

        setWidgets()
    }

    fun setWidgets(){

        // font
        needText.typeface = ResourcesCompat.getFont(this, R.font.helvetica)
        needCodeText.typeface = ResourcesCompat.getFont(this, R.font.helvetica)

        // text
        needCodeText.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

                if(s.length == 6){

                    if(EncryptLoad.get(this@NeedCodeActivity).getString("ACCESS_CODE_STRING","") == s.toString()) {

                        if(EncryptLoad.get(this@NeedCodeActivity).getBoolean("ACCESS_CODE_ACTION", false)){

                            endPasswordAction()


                        } else {

                            normalAction()

                        }

                    } else {

                        FAILED_COUNT += 1
                        EncryptLoad.get(this@NeedCodeActivity).edit().putInt("FAILED_COUNT", FAILED_COUNT).apply()

                        indicatorAction()

                        if(FAILED_COUNT == 5){

                            EncryptLoad.get(this@NeedCodeActivity).edit().putBoolean("DEACTIVATE", true).apply()
                            Toast.makeText(this@NeedCodeActivity, "DEACTIVATED", Toast.LENGTH_LONG).show()

                            needCodeView.visibility = View.GONE

                            Handler().postDelayed({

                                finishAffinity()
                                System.exit(0);

                            }, END_LIMIT)


                        }else{

                            // shake action
                            val animShake =
                                AnimationUtils.loadAnimation(this@NeedCodeActivity, R.anim.shake)
                            needCodeText.startAnimation(animShake)
                            needCodeText.setText("")

                        }

                    }

                }
                // TODO Auto-generated method stub
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

                // TODO Auto-generated method stub
            }

            override fun afterTextChanged(s: Editable) {

                // TODO Auto-generated method stub
            }

        })

        needButton.setOnClickListener {

            val database = DatabaseHelper(this,null)
            database.deleteAllLinks(this,EncryptLoad.get(this).getString("DB_PASSWORD","")!!)
            Log.d("TAG", "deleted all contents")

        }

    }

    fun indicatorAction(){

        when(FAILED_COUNT){

            1->needCodeIndicator1.setBackgroundResource(R.drawable.rounded_rectangle_colorred)
            2->{
                needCodeIndicator1.setBackgroundResource(R.drawable.rounded_rectangle_colorred)
                needCodeIndicator2.setBackgroundResource(R.drawable.rounded_rectangle_colorred)
            }
            3->{
                needCodeIndicator1.setBackgroundResource(R.drawable.rounded_rectangle_colorred)
                needCodeIndicator2.setBackgroundResource(R.drawable.rounded_rectangle_colorred)
                needCodeIndicator3.setBackgroundResource(R.drawable.rounded_rectangle_colorred)
            }
            4->{
                needCodeIndicator1.setBackgroundResource(R.drawable.rounded_rectangle_colorred)
                needCodeIndicator2.setBackgroundResource(R.drawable.rounded_rectangle_colorred)
                needCodeIndicator3.setBackgroundResource(R.drawable.rounded_rectangle_colorred)
                needCodeIndicator4.setBackgroundResource(R.drawable.rounded_rectangle_colorred)
            }
            5->{
                needCodeIndicator1.setBackgroundResource(R.drawable.rounded_rectangle_colorred)
                needCodeIndicator2.setBackgroundResource(R.drawable.rounded_rectangle_colorred)
                needCodeIndicator3.setBackgroundResource(R.drawable.rounded_rectangle_colorred)
                needCodeIndicator4.setBackgroundResource(R.drawable.rounded_rectangle_colorred)
                needCodeIndicator5.setBackgroundResource(R.drawable.rounded_rectangle_colorred)
            }

        }

    }

    fun endPasswordAction(){

        EncryptLoad.get(this).edit().putInt("FAILED_COUNT", 0).apply()

        EncryptLoad.get(this@NeedCodeActivity).edit()
            .putBoolean("ACCESS_CODE_ACTION", false)
            .putBoolean("ACCESS_CODE", false)
            .remove("ACCESS_CODE_STRING")
            .putBoolean("IS_LOCKED", false).apply()
        finish()

    }

    fun normalAction(){

        EncryptLoad.get(this).edit().putInt("FAILED_COUNT", 0).apply()

        EncryptLoad.get(this@NeedCodeActivity).edit().putBoolean("IS_LOCKED", false).apply()
        finish()

    }

    // main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_need_code)
    }

    // rest function

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
        System.exit(0);
    }


}
