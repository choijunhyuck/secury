package com.codelog.secury

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView.OnItemClickListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.codelog.secury.code.NeedCodeActivity
import com.codelog.secury.data.LinkListAdapter
import com.codelog.secury.data.LinkModel
import com.codelog.secury.database.DatabaseHelper
import com.codelog.secury.encrypt.EncryptLoad
import kotlinx.android.synthetic.main.activity_sbox.*


class SBoxActivity : AppCompatActivity() {

    var linkModel = ArrayList<LinkModel>()
    val adapter = LinkListAdapter(this, linkModel)

    // set widgets
    override fun onStart() {
        super.onStart()

        if(EncryptLoad.get(this).getBoolean("ACCESS_CODE", false) &&
            EncryptLoad.get(this).getBoolean("IS_LOCKED", false)){

            sboxList.visibility = View.GONE
            startActivity(Intent(this, NeedCodeActivity::class.java))

        } else {

            sboxList.visibility = View.VISIBLE

            if (EncryptLoad.get(this).getBoolean("ACCESS_CODE", false)) {
                EncryptLoad.get(this).edit().putBoolean("IS_LOCKED", true).apply()
            }

            setWidgets()
            postLinks()

        }

    }

    fun setWidgets(){

        //buttons-onclick
        sboxBackButton.setOnClickListener {
            onBackPressed()
        }

        sboxSecurityButton.setOnClickListener {

            val builder = AlertDialog.Builder(this)
            builder.setTitle(R.string.sboxToast1)
            builder.setMessage(R.string.sboxToast2)
            //builder.setPositiveButton("OK", DialogInterface.OnClickListener(function = x))

            builder.setPositiveButton(android.R.string.yes) { dialog, which ->

            }
            builder.show()

        }

    }

    fun postLinks(){

        sboxList.adapter = adapter

        var database = DatabaseHelper(this, null)

        linkModel.clear()

        val cursor = database.getAllLinks(this,
            EncryptLoad.get(this).getString("DB_PASSWORD", "")!!)

        cursor!!.moveToLast()
        if(cursor.moveToLast()) {

            val item = LinkModel(
                cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_TITLE)),
                cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_URL))
            )
            linkModel.add(item)

        }

        while (cursor.moveToPrevious()) {

            val item = LinkModel(
                cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_TITLE)),
                cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_URL))
            )
            linkModel.add(item)

        }

        adapter.notifyDataSetChanged()

        if(linkModel.isEmpty()){
            //warning_no_contents.visibility = View.VISIBLE
        }else{
            //warning_no_contents.visibility = View.GONE
        }

    }

    // main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sbox)

    }

    // rest functions

}
