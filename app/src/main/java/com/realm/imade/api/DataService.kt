package com.realm.imade.api

import com.realm.imade.config.Config.SERV
import retrofit2.Call
import retrofit2.http.GET


interface DataService {
    @GET(SERV)
    fun getDataService(): Call<ServModel>
}