package com.realm.imade.config

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.realm.imade.api.ServModel
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket

object Config {
    const val SERV =
        "services/rest/?method=flickr.galleries.getPhotos&api_key=50ca4105215dfb1767eb492cff622520&gallery_id=66911286-72157647277042064&format=json&nojsoncallback=1"
    const val baseUrl = "https://www.flickr.com"

    const val PROTOCOL = "https://"
    const val SUB_DOMAIN = "farm"
    const val DOMAIN = ".staticflickr.com"

    const val BASE_LIST = true
    const val FAVORITE_LIST = false
    const val ON_CHANNEL = 4

    var REALM_ADD = false
    var REALM_GET_ALL = false
    var REALM_GET_ALL_FAVORITS = false
    var REALM_GET_ALL_FAVORITS_NOT = false


    var Context.mainSplash
        get() = getSharedPreferences(packageName, MODE_PRIVATE).getBoolean("mainSplash", false)
        set(value) {
            getSharedPreferences(packageName, MODE_PRIVATE).edit().putBoolean("mainSplash", value)
                .apply()
        }

    fun isOnline(): Boolean {
        return try {
            val timeoutMs = 1000
            val sock = Socket()
            sock.connect(InetSocketAddress("8.8.8.8", 53), timeoutMs)
            sock.close()
            true
        } catch (e: IOException) {
            false
        }
    }

    fun getSizeRetrofit(element: ServModel): Int {
        return element.mPhotos.mPhoto.size
    }
}