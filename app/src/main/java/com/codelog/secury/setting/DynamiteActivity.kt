package com.codelog.secury.setting

import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.codelog.secury.R
import com.codelog.secury.encrypt.EncryptLoad
import kotlinx.android.synthetic.main.activity_dynamite.*

class DynamiteActivity : AppCompatActivity() {

    var STRENGTH = "0"

    override fun onStart() {
        super.onStart()

        STRENGTH = EncryptLoad.get(this).getString("DYNAMITE_TAB", "0")!!
        setWidgets()

    }

    fun setWidgets(){

        // font
        dynamiteTitle.typeface = ResourcesCompat.getFont(this, R.font.helvetica)
        dynamiteText1.typeface = ResourcesCompat.getFont(this, R.font.helvetica)
        dynamiteText2.typeface = ResourcesCompat.getFont(this, R.font.helvetica)
        dynamiteText3.typeface = ResourcesCompat.getFont(this, R.font.helvetica)
        dynamiteText4.typeface = ResourcesCompat.getFont(this, R.font.helvetica)
        dynamiteText5.typeface = ResourcesCompat.getFont(this, R.font.helvetica)
        dynamiteText6.typeface = ResourcesCompat.getFont(this, R.font.helvetica)
        dynamiteText7.typeface = ResourcesCompat.getFont(this, R.font.helvetica)
        dynamiteText8.typeface = ResourcesCompat.getFont(this, R.font.helvetica)

        // initiate guage
        seekbarAction(STRENGTH.toInt())
        dynamiteGauge.progress = STRENGTH.toInt()

        dynamiteGauge.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {

            override fun onStopTrackingTouch(seekBar: SeekBar) { // TODO Auto-generated method stub

            }

            override fun onStartTrackingTouch(seekBar: SeekBar) { // TODO Auto-generated method stub

            }

            override fun onProgressChanged(
                seekBar: SeekBar,
                progress: Int,
                fromUser: Boolean
            ) { // TODO Auto-generated method stub

                selectInitial()
                seekbarAction(progress)

            }
        })

        // buttons-onclick
        dynamiteBackButton.setOnClickListener {

            // apply
            EncryptLoad.get(this).edit().putString("DYNAMITE_TAB",
                STRENGTH).apply()

            onBackPressed()

        }

    }

    fun seekbarAction(progress:Int){

        when(progress) {

            0 -> selectInitial()

            1 -> {dynamiteSelect1.visibility = View.VISIBLE
                dynamiteText1.setTextColor(
                    ContextCompat.getColor(
                        this@DynamiteActivity,
                        R.color.colorPrimary))
                dynamiteText1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)
                STRENGTH = "1"
            }

            2 -> {
                dynamiteSelect2.visibility = View.VISIBLE
                dynamiteText3.setTextColor(
                    ContextCompat.getColor(
                        this@DynamiteActivity,
                        R.color.colorPrimary
                    )
                )
                dynamiteText3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)
                STRENGTH = "2"
            }

            3 -> {
                dynamiteSelect3.visibility = View.VISIBLE
                dynamiteText5.setTextColor(
                    ContextCompat.getColor(
                        this@DynamiteActivity,
                        R.color.colorPrimary
                    )
                )
                dynamiteText5.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)
                STRENGTH = "3"
            }

            /*
            4 -> {
                dynamiteSelect4.visibility = View.VISIBLE
                dynamiteText7.setTextColor(
                    ContextCompat.getColor(
                        this@DynamiteActivity,
                        R.color.colorPrimary
                    )
                )
                dynamiteText7.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)
                STRENGTH = "4"
            }
            */

        }

    }

    fun selectInitial(){

        STRENGTH = "0"

        dynamiteSelect1.visibility = View.GONE
        dynamiteSelect2.visibility = View.GONE
        dynamiteSelect3.visibility = View.GONE
        //dynamiteSelect4.visibility = View.GONE

        dynamiteText1.setTextColor(ContextCompat.getColor(this, R.color.colorGreyDark))
        dynamiteText3.setTextColor(ContextCompat.getColor(this, R.color.colorGreyDark))
        dynamiteText5.setTextColor(ContextCompat.getColor(this, R.color.colorGreyDark))
        //dynamiteText7.setTextColor(ContextCompat.getColor(this, R.color.colorGreyDark))

        dynamiteText1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f);
        dynamiteText3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f);
        dynamiteText5.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f);
        //dynamiteText7.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f);

    }


    // main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dynamite)
    }


}
