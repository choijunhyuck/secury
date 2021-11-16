package com.codelog.secury.encrypt

import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.security.crypto.MasterKeys

class EncryptKey{

    companion object {

        fun MasterKeysAlias(): String {

            // make or get AES256 key
            val AdvancedKeyGenParameter = KeyGenParameterSpec.Builder(
                "secury_advanced_key",
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
            ).apply {
                setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                setKeySize(256)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {//Android P or higher
                    setUnlockedDeviceRequired(true)
                }
            }.build()

            //val keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC
            var advkey = MasterKeys.getOrCreate(AdvancedKeyGenParameter)

            return advkey
        }
    }

}