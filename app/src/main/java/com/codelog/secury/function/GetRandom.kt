package com.codelog.secury.function

class GetRandom{

    companion object{

        fun getDBPassword() : String {
            val allowedChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
            return (1..20)
                .map { allowedChars.random() }
                .joinToString("")
        }

        fun getAccessCode(length: Int) : String {
            val allowedChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
            return (1..length)
                .map { allowedChars.random() }
                .joinToString("")
        }

    }

}