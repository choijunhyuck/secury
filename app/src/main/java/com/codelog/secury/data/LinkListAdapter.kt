package com.codelog.secury.data

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import com.codelog.secury.R
import com.codelog.secury.database.DatabaseHelper
import com.codelog.secury.encrypt.EncryptLoad
import com.codelog.secury.function.GetFavicon


class LinkListAdapter(val activity: Activity, val listItem: ArrayList<LinkModel>) : BaseAdapter() {

    private var inflater: LayoutInflater? = null

    override fun getCount(): Int {
        return listItem.size
    }

    override fun getItem(location: Int): Any {
        return listItem[location]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        var convertView = convertView

        if (inflater == null)
            inflater = activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        if (convertView == null)
            convertView = inflater!!.inflate(R.layout.activity_link_item, null)

        val item = listItem[position]

        // font
        convertView?.findViewById<TextView>(R.id.litemTitle)?.typeface = ResourcesCompat.getFont(activity, R.font.helvetica)
        convertView?.findViewById<TextView>(R.id.litemUrl)?.typeface = ResourcesCompat.getFont(activity, R.font.helvetica)


        // layout
        val thread = Thread(Runnable {
            try { //Your code goes here

                convertView?.findViewById<ImageView>(R.id.litemIcon)?.setImageBitmap(
                    GetFavicon.fetchFavicon(Uri.parse(item.url)))

            } catch (e: Exception) {
                e.printStackTrace()
            }
        })

        thread.start()

        convertView?.findViewById<TextView>(R.id.litemTitle)?.text = item.title

        if(item.url.length > 30){
            convertView?.findViewById<TextView>(R.id.litemUrl)?.text = item.url.substring(0,30)+"..."
        }else{
            convertView?.findViewById<TextView>(R.id.litemUrl)?.text = item.url
        }

        //buttons-onclick

        convertView?.findViewById<ConstraintLayout>(R.id.litem)?.setOnClickListener {
            //go webview
            EncryptLoad.get(activity).edit().putString("PASSED_URL",item.url).apply()
            activity.finish()

        }

        convertView?.findViewById<ConstraintLayout>(R.id.litem)?.setOnLongClickListener {

            showEdit(item.id, position)

            return@setOnLongClickListener true
        }

        return convertView!!
    }

    fun showEdit(id:Int, position: Int){

        val builder = AlertDialog.Builder(activity)
        builder.setTitle(R.string.sboxToast3)
        builder.setMessage(R.string.sboxToast4)
        //builder.setPositiveButton("OK", DialogInterface.OnClickListener(function = x))

        builder.setPositiveButton(android.R.string.yes) { dialog, which ->

            // delete
            val database = DatabaseHelper(activity, null)
            database.deleteLink(id, activity, EncryptLoad.get(activity).getString("DB_PASSWORD","")!!)

            listItem.removeAt(position)

            this@LinkListAdapter.notifyDataSetChanged()

            /* run on ui thread
            activity.runOnUiThread(Runnable {
                this@LinkListAdapter.notifyDataSetChanged() })
             */

        }

        builder.setNegativeButton(android.R.string.no) { dialog, which ->
            // cancel
        }
        builder.show()

    }

}