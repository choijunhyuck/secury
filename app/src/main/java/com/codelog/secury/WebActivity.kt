package com.codelog.secury

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.webkit.*
import android.widget.TextView.OnEditorActionListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentActivity
import com.codelog.secury.code.NeedCodeActivity
import com.codelog.secury.database.DatabaseHelper
import com.codelog.secury.encrypt.EncryptLoad
import com.codelog.secury.function.GetDomain
import com.codelog.secury.function.ObservableWebView
import com.codelog.secury.setting.SettingActivity
import kotlinx.android.synthetic.main.activity_web.*
import net.sqlcipher.database.SQLiteDatabase


class WebActivity : AppCompatActivity() {

    var POPUP_LIMIT:Long = 1000
    var END_LIMIT:Long = 400

    var NOW_TITLE = "default"
    var NOW_URL = "default"

    // set widgets
    override fun onStart() {
        super.onStart()

        webView.visibility = View.GONE

        if(EncryptLoad.get(this).getBoolean("DEACTIVATE", false)) {
            // deactivate , delete all data
            deleteDatabase()
            removeAllData()
            finishAffinity();
            System.exit(0);
        }

        if(EncryptLoad.get(this).getBoolean("ACCESS_CODE", false) &&
                EncryptLoad.get(this).getBoolean("IS_LOCKED", false)){

            startActivity(Intent(this, NeedCodeActivity::class.java))

        } else {

            if(EncryptLoad.get(this).getBoolean("ACCESS_CODE", false)){
                EncryptLoad.get(this).edit().putBoolean("IS_LOCKED", true).apply()
            }

            if(EncryptLoad.get(this).getString("PASSED_URL", "None") != "None"){
                webView.loadUrl(EncryptLoad.get(this).getString("PASSED_URL", "None"))
                EncryptLoad.get(this).edit().remove("PASSED_URL").apply()

                webView.visibility = View.VISIBLE
            } else {

                if (EncryptLoad.get(this).getBoolean("USE_WIFI_ONLY", false)) {
                    // wificheck

                    val connManager =
                        getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                    val mWifi = connManager.getNetworkCapabilities(connManager.activeNetwork)

                    if (mWifi.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                        //using wifi
                        Log.d("TAG", "condition success")
                        webBarExpanded.isEnabled = true
                        webView.visibility = View.VISIBLE

                        webView.loadUrl(
                            EncryptLoad.get(this).getString(
                                "DEFAULT_URL",
                                "https://www.google.com"
                            )!!
                        )

                        startJoin()

                    } else if (mWifi.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                        //using data,
                        Log.d("TAG", "condition failed")
                        webBarExpanded.isEnabled = false
                        webView.visibility = View.GONE

                        showFailedContionBox()

                    }

                } else {

                    webView.visibility = View.VISIBLE

                    webView.loadUrl(
                        EncryptLoad.get(this).getString(
                            "DEFAULT_URL",
                            "https://www.google.com"
                        )!!
                    )

                    startJoin()

                }
            }

        }

    }

    fun startJoin(){

        // start, button-enabled

        if(EncryptLoad.get(this).getBoolean("SECURY_BOX",false) &&
        EncryptLoad.get(this).getBoolean("WEB_GUIDE", true)){

            webGuide.visibility = View.VISIBLE
            EncryptLoad.get(this).edit().putBoolean("WEB_GUIDE", false).apply()

        }else{
            webGuide.visibility = View.GONE
        }

        webSettingButton.isEnabled = true

        setWidgets()

    }

    fun setWidgets(){

        // fonts
        webSearch.typeface = ResourcesCompat.getFont(this, R.font.helvetica)
        webFUrlDisplay.typeface = ResourcesCompat.getFont(this, R.font.helvetica)
        webUrlDisplay.typeface = ResourcesCompat.getFont(this, R.font.helvetica)
        webGuideText.typeface = ResourcesCompat.getFont(this, R.font.helvetica)

        // webview
        loadWebSettings()

        webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView, progress: Int) {

                webProgressBar.setProgress(progress)
                if (progress == 100) {

                    NOW_TITLE = view.title
                    NOW_URL = view.url.toString()
                    webProgressBar.setVisibility(View.GONE)
                } else {

                    NOW_URL = "default"
                    webProgressBar.setVisibility(View.VISIBLE)
                }
            }

        }

        webView.setWebViewClient(object : WebViewClient() {

            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {
                super.onReceivedError(view, request, error)

                webCLPopup.visibility = View.VISIBLE

                Handler().postDelayed({

                    webCLPopup.visibility = View.GONE

                }, POPUP_LIMIT)

                /*
                if (!CheckInternet.check(this@WebActivity)) {
                }
                */

            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)

                webBarSearch.visibility = View.GONE

                webBarFolded.visibility = View.GONE
                webBarExpanded.visibility = View.VISIBLE

                webSearch.setText(url.toString())
                webFUrlDisplay.setText(GetDomain.getDomainFullName(url.toString()))
                webUrlDisplay.setText(GetDomain.getDomainFullName(url.toString()))

            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)

                webFUrlDisplay.setText(GetDomain.getDomainFullName(url.toString()))
                webUrlDisplay.setText(GetDomain.getDomainFullName(url.toString()))
            }
        })

        webView.setOnScrollChangedCallback(object : ObservableWebView.OnScrollChangedCallback{
            override fun onScroll(l: Int, t: Int, oldl: Int, oldt: Int) {

                webBarSearch.visibility = View.GONE
                hideKeyboard()

                if (t > oldt) {
                    //Swipe down
                    webBarExpanded.visibility = View.GONE
                    webBarFolded.visibility = View.VISIBLE
                } else if (t < oldt) {
                    //Swipe up
                    webBarExpanded.visibility = View.VISIBLE
                    webBarFolded.visibility = View.GONE

                }
            }
        })

        // url bar
        webSearch.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                var url = v.text.toString()

                // url filter
                if(url.length > 3 && url.substring(0,3) == "www"){
                    url = "https://"+url
                } else if(url.length > 5 && url.substring(0,3) != "www" && url.substring(0,5) != "https"){
                    url = "https://www.google.com/search?q="+url
                } else {
                    url = "https://www.google.com/search?q="+url
                }

                // load url
                webView.loadUrl(url)

                // processing rest task
                hideKeyboard()
                webBarSearch.visibility = View.GONE

                webBarExpanded.visibility = View.VISIBLE

                return@OnEditorActionListener true
            }
            false
        })

        webSearch.setOnFocusChangeListener(OnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) { // code to execute when EditText loses focus
                hideKeyboard()
                webBarSearch.visibility = View.GONE

                webBarExpanded.visibility = View.VISIBLE
            }
        })

        // layout
        if(EncryptLoad.get(this).getBoolean("SECURY_BOX", false)){
            webAddButton.visibility = View.VISIBLE
        } else {
            webAddButton.visibility = View.GONE
        }

        if(EncryptLoad.get(this).getString("DYNAMITE_TAB", "0") != "0"){
            webDynamiteButton.visibility = View.VISIBLE
        } else {
            webDynamiteButton.visibility = View.GONE
        }

        // buttons-onclick
        webHomeButton.setOnClickListener{

            webView.loadUrl(
                EncryptLoad.get(this).getString("DEFAULT_URL",
                "https://www.google.com")!!)
            webView.clearHistory()

        }

        webHomeButton.setOnLongClickListener {

            if(EncryptLoad.get(this).getBoolean("SECURY_BOX", false)) {
                startActivity(Intent(this, SBoxActivity::class.java))
            }

            return@setOnLongClickListener true
        }

        webAddButton.setOnClickListener {

            if(NOW_URL != "default") {

                // database satrt ( add to secury box func )
                var database = DatabaseHelper(this, null)
                database.addLink(
                    this, EncryptLoad.get(this).getString("DB_PASSWORD", "")!!,
                    NOW_TITLE, NOW_URL
                )

                // test function
                val cursor = database.getAllLinks(
                    this,
                    EncryptLoad.get(this).getString("DB_PASSWORD", "")!!
                )
                cursor!!.moveToLast()

                Log.d("TAG", "**********")
                Log.d("TAG", cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_TITLE)))
                Log.d("TAG", cursor.count.toString())

                // test function end

                database.close()
                // database complete

                webAddPopup.visibility = View.VISIBLE

                Handler().postDelayed({

                    webAddPopup.visibility = View.GONE

                }, POPUP_LIMIT)

            } else {
                Toast.makeText(this, R.string.webToast1, Toast.LENGTH_LONG).show()
            }

        }

        webUrlDisplay.setOnClickListener {

            webBarExpanded.visibility = View.GONE
            webBarSearch.visibility = View.VISIBLE

            //function keyboard up
            webSearch.requestFocus()
            showKeyboard()
        }

        webReloadButton.setOnClickListener {
            webView.reload()
        }

        webSettingButton.setOnClickListener {
            webSettingButton.isEnabled = false
            startActivity(Intent(this, SettingActivity::class.java))
        }

        webBarFolded.setOnClickListener {

            webBarFolded.visibility = View.GONE
            webBarExpanded.visibility = View.VISIBLE

        }

        webFHomeButton.setOnClickListener {

            webView.loadUrl(
                EncryptLoad.get(this).getString("DEFAULT_URL",
                "https://www.google.com")!!)
            webView.clearHistory()

        }

        webFSettingButton.setOnClickListener {
            webSettingButton.isEnabled = false
            startActivity(Intent(this, SettingActivity::class.java))
        }

        webSearchBackButton.setOnClickListener {
            hideKeyboard()
            webBarSearch.visibility = View.GONE

            webBarExpanded.visibility = View.VISIBLE
        }

        webDynamiteButton.setOnClickListener {

            var dynamiteStrength = EncryptLoad.get(this).getString("DYNAMITE_TAB","0")

            if(dynamiteStrength != "0"){

                EncryptLoad.get(this).edit().putString("DYNAMITE_TAB", "0").apply()

                when(dynamiteStrength){

                    "1"-> {

                        removeAllData()
                        startEndSequence()

                    }
                    "2"->{
                        // with delete box
                        removeAllData()
                        deleteDatabase()
                        startEndSequence()

                    }
                    "3"->{

                        // deactivate , delete all data
                        EncryptLoad.get(this).edit().putBoolean("DEACTIVATE",true).apply()
                        removeAllData()
                        deleteDatabase()
                        startEndSequence()

                    }
                    "4"->{
                        // atelier service
                    }

                }

            }

        }

        webGuide.setOnClickListener {
            if(webGuide.isVisible){
                webGuide.visibility = View.GONE
            }
        }

    }

    fun loadWebSettings(){

        // normal
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true)
        webView.getSettings().setLoadsImagesAutomatically(true)
        webView.getSettings().setSupportZoom(false)
        webView.getSettings().setAllowFileAccess(true)

        // customize
        if(EncryptLoad.get(this).getBoolean("ALLOW_WEB_CACHE", false)) {
            webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT)
        } else {
            webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE)
        }
        if(EncryptLoad.get(this).getBoolean("ALLOW_APP_CACHE", false)) {
            webView.getSettings().setAppCacheEnabled(true)
        } else {
            webView.getSettings().setAppCacheEnabled(false)
        }
        if(EncryptLoad.get(this).getBoolean("ALLOW_APP_CACHE", false)) {
            CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true)
        } else {
            CookieManager.getInstance().setAcceptThirdPartyCookies(webView, false)
        }
        if(EncryptLoad.get(this).getBoolean("USE_WIDE_WEB", false)) {
            webView.getSettings().setUseWideViewPort(true)
        } else {
            webView.getSettings().setUseWideViewPort(false)
        }
        if(EncryptLoad.get(this).getBoolean("USE_SAFE_BROWSING", false)) {
            webView.getSettings().setSafeBrowsingEnabled(true)
        } else {
            webView.getSettings().setSafeBrowsingEnabled(false)
        }

        //allow search history?
        if(EncryptLoad.get(this).getBoolean("USE_JAVASCRIPT", false) &&
            EncryptLoad.get(this).getBoolean("USE_DOMSTORAGE", false)){
            webView.getSettings().setJavaScriptEnabled( true ); //->네이버의 경우 위젯이 제대로
            webView.getSettings().setDomStorageEnabled( true)
        }
        else{
            webView.getSettings().setJavaScriptEnabled( false );
            webView.getSettings().setDomStorageEnabled(false)
        }

    }

    fun showFailedContionBox(){

        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.webToast2)
        builder.setMessage(R.string.webToast3)
        //builder.setPositiveButton("OK", DialogInterface.OnClickListener(function = x))

        builder.setPositiveButton(android.R.string.yes) { dialog, which ->

        }
        builder.show()

    }

    fun deleteDatabase(){

        val database = DatabaseHelper(this,null)
        database.deleteAllLinks(this,EncryptLoad.get(this).getString("DB_PASSWORD","")!!)

    }

    fun startEndSequence(){

        webLayout.visibility = View.GONE

        Handler().postDelayed({

            finishAffinity()
            System.exit(0)

        }, END_LIMIT)

    }

    // main
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)

        SQLiteDatabase.loadLibs(this)

    }

    // rest function

    fun showKeyboard(){

        val imm: InputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)

    }

    fun hideKeyboard(){

        val imm: InputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(webSearch.getWindowToken(),0);

    }

    override fun onBackPressed() {

        if(EncryptLoad.get(this).getBoolean("LOAD_BACK_PAGE", true)){

            if(webView.canGoBack()){
                webView.goBack()
            } else {
                super.onBackPressed()
                removeAllData()
            }

        } else {
            super.onBackPressed()
            removeAllData()
        }

    }

    fun removeAllData(){

        webView.clearCache(true)
        webView.clearFormData()
        webView.clearHistory()

        CookieManager.getInstance()
            .removeAllCookies {
                    value -> Log.d("TAG", "onReceiveValue $value") }

    }

}
