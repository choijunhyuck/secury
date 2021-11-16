package com.codelog.secury.encrypt

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import com.codelog.secury.encrypt.EncryptKey

class EncryptLoad{

    companion object {

        fun get(context:Context): SharedPreferences {
            
            val mySharedPreferences = EncryptedSharedPreferences.create(
                "secury_file",
                EncryptKey.MasterKeysAlias(),
                context,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV, //for encrypting Keys
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM ////for encrypting Values
            )
            
            return mySharedPreferences
        }
    }

}