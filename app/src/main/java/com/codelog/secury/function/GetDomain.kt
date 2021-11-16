package com.codelog.secury.function

import java.net.URI
import java.net.URISyntaxException

class GetDomain{

    companion object{

        @Throws(URISyntaxException::class)
        fun getDomainName(url: String?): String? {
            val uri = URI(url)
            val domain: String = uri.getHost()
            return if (domain.startsWith("www.")) domain.substring(4,5) else domain
        }

        @Throws(URISyntaxException::class)
        fun getDomainFullName(url: String?): String? {
            val uri = URI(url)
            val domain: String = uri.getHost()
            return if (domain.startsWith("www.")) domain.substring(4) else domain

        }

    }

}