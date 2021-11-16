package com.codelog.secury

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.core.content.res.ResourcesCompat
import com.codelog.secury.database.DatabaseHelper
import com.codelog.secury.encrypt.EncryptLoad
import com.codelog.secury.function.GetRandom
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.activity_splash.*
import net.sqlcipher.database.SQLiteDatabase

class SplashActivity : AppCompatActivity() {

    var IS_FIRST:Boolean = true
    var SPLASH_TIME_OUT:Long = 3000
    var SPLASH_ESCAPE:Long = 2000
    var SPLASH_AES_OUT:Long = 6000

    var IS_END = false

    lateinit var dialog:AlertDialog

    // set widgets
    override fun onStart() {
        super.onStart()

        if(EncryptLoad.get(this).getBoolean("DEACTIVATE", false)) {
            // deactivate , delete all data
            finishAffinity();
            System.exit(0);
        }else{
            setWidgets()
        }

    }

    fun setWidgets(){

        //fonts
        splashText1.typeface = ResourcesCompat.getFont(this, R.font.bebas_regular)
        splashText2.typeface = ResourcesCompat.getFont(this, R.font.bebas_regular)
        splashText3.typeface = ResourcesCompat.getFont(this, R.font.bebas_regular)

        //dialog
        dialog = SpotsDialog.Builder()
            .setContext(this)
            .setMessage(R.string.splashText1)
            .setCancelable(false)
            .build()

    }

    // main
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        IS_FIRST = EncryptLoad.get(this)
            .getBoolean("IS_FIRST", true)

        if(IS_FIRST){

            Handler().postDelayed({

                showMessage()
                setUserInformation()

            }, SPLASH_TIME_OUT)

        }else{

            startWeb()

        }

    }

    // rest functions
    fun setUserInformation(){

        EncryptLoad.get(this).edit()
            .putBoolean("IS_FIRST", false)
            .putString("DEFAULT_URL", "https://www.google.com") // setting start
            .putBoolean("LOAD_BACK_PAGE", true)
            .putString("DYNAMITE_TAB", "0")
            .putBoolean("ACCESS_CODE", false)
            .putBoolean("IS_LOCKED", false)
            .putBoolean("SECURY_BOX", false)
            .putBoolean("SECURY_ATELIER", false) //setting end
            .putBoolean("ALLOW_WEB_CACHE", false) // web start
            .putBoolean("ALLOW_APP_CACHE", false)
            .putBoolean("ALLOW_APP_COOKIES", false)
            .putBoolean("USE_WIDE_WEB", false)
            .putBoolean("USE_SAFE_BROWSING", true) // web end
            .putBoolean("USE_JAVASCRIPT", true)
            .putBoolean("USE_DOMSTORAGE", true)
            .putBoolean("USE_WIFI_ONLY", false)
            .putString("DB_PASSWORD", GetRandom.getDBPassword())
            .putBoolean("DEACTIVATE", false)
            .putBoolean("WEB_GUIDE", true)
            .putBoolean("NEED_CODE_GUIDE", true)
            .apply()

        makeDatabase()

    }

    fun makeDatabase(){

        val CREATE_PRODUCTS_TABLE = ("CREATE TABLE " +
                DatabaseHelper.TABLE_NAME + "("
                + DatabaseHelper.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"  +
                DatabaseHelper.COLUMN_TITLE + " TEXT,"+
                DatabaseHelper.COLUMN_URL + " TEXT"+ ")")

        SQLiteDatabase.loadLibs(this)
        val databaseFile = getDatabasePath("secury.db")
        databaseFile.parentFile.mkdirs()

        val database = SQLiteDatabase.openOrCreateDatabase(databaseFile, EncryptLoad.get(this).
            getString("DB_PASSWORD",""), null)

        database.execSQL(CREATE_PRODUCTS_TABLE)
        database.close()

        Handler().postDelayed({

            killMessage()
            startWeb()

        }, SPLASH_AES_OUT)

    }

    fun showMessage(){ dialog.show() }

    fun killMessage(){ dialog.dismiss() }

    fun startWeb(){
        startActivity(Intent(this, WebActivity::class.java))
    }

    override fun onRestart() {
        super.onRestart()

        Handler().postDelayed({

            if(IS_END) {

                finishAffinity()
                System.exit(0)

            }else{
                startWeb()
            }

        }, SPLASH_ESCAPE)

    }

    override fun onBackPressed() {
        super.onBackPressed()
        IS_END = true
        onRestart()

    }

}
