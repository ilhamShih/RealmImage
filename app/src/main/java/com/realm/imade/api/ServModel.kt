package com.realm.imade.api

import com.google.gson.annotations.SerializedName

class ServModel {
    @SerializedName("photos")
    val mPhotos: Photos = Photos()

    class Photos {
        @SerializedName("photo")
        var mPhoto: ArrayList<Photo> = arrayListOf()
    }

    class Photo {
        @SerializedName("id")
        val id: String = ""

        @SerializedName("farm")
        val farm: String = ""

        @SerializedName("server")
        val server: String = ""

        @SerializedName("secret")
        val secret: String = ""

    }

}

