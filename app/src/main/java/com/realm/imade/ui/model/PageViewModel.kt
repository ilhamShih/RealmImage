package com.realm.imade.ui.model

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import com.realm.imade.api.DataService
import com.realm.imade.api.RetrofitResponse.retrofitInstanceServer
import com.realm.imade.api.ServModel
import com.realm.imade.config.Config.BASE_LIST
import com.realm.imade.config.Config.DOMAIN
import com.realm.imade.config.Config.PROTOCOL
import com.realm.imade.config.Config.SUB_DOMAIN
import com.realm.imade.db.DataBase.addEntry
import com.realm.imade.db.DataBase.delete
import com.realm.imade.db.DataBase.realm
import com.realm.imade.db.ItemBase
import com.realm.imade.db.ItemFavoritesKey
import io.realm.kotlin.ext.query
import io.realm.kotlin.notifications.InitialResults
import io.realm.kotlin.notifications.ResultsChange
import io.realm.kotlin.notifications.UpdatedResults
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PageViewModel : ViewModel() {

    val errorConnect = MutableLiveData<Boolean>()
    val changesResults = MutableLiveData<ResultsChange<ItemFavoritesKey>>()
    val changesResultsBase = MutableLiveData<ResultsChange<ItemBase>>()
    val changesResults2 = MutableLiveData<Boolean>()
    val changesResults2Base = MutableLiveData<Boolean>()


    fun getImage() {
        retrofitInstanceServer!!.create(DataService::class.java)
            .getDataService().enqueue(object : Callback<ServModel> {
                @RequiresApi(Build.VERSION_CODES.N)
                override fun onResponse(call: Call<ServModel>, response: Response<ServModel>) {
                    if (response.isSuccessful) {
                        response.body()?.let { model ->
                            model.mPhotos.mPhoto.reversed().forEach {
                                add(
                                    BASE_LIST, it.id,
                                    "$PROTOCOL$SUB_DOMAIN${it.farm}$DOMAIN/${it.server}/${it.id}_${it.secret}.jpg"
                                )
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<ServModel?>, t: Throwable) {
                    errorConnect.value = false
                }
            })
    }


    fun add(boolean: Boolean, keyUrl: String, imageUri: String) {
        viewModelScope.launch(Dispatchers.Default) {
            addEntry(boolean, keyUrl, imageUri)
        }
    }


    fun subscribeFavorites() {
        viewModelScope.launch {
            realm.query<ItemFavoritesKey>().find().asFlow().collect {
                when (it) {
                    is InitialResults -> {
                        changesResults.value = it
                        changesResults2.value = true
                    }
                    is UpdatedResults -> {
                        changesResults.value = it
                        changesResults2.value = false
                    }
                }
            }
        }
    }

    fun subscribeBase() {
        viewModelScope.launch(Dispatchers.Main) {
            realm.query<ItemBase>().find().asFlow().collect {
                when (it) {
                    is InitialResults -> {
                        changesResultsBase.value = it
                        changesResults2Base.value = true
                    }
                    is UpdatedResults -> {
                        changesResultsBase.value = it
                        changesResults2Base.value = false

                    }
                }
            }
        }
    }

    fun deliteEntry(vararg primarKey: String?) {
        viewModelScope.launch {
            if (primarKey.isNotEmpty()) {
                if (primarKey[0] != null) delete(primarKey[0]) else delete()
            }
        }
    }

}