package com.realm.imade.ui.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.realm.imade.config.Config.REALM_ADD
import com.realm.imade.config.Config.REALM_GET_ALL
import com.realm.imade.config.Config.REALM_GET_ALL_FAVORITS
import com.realm.imade.config.Config.REALM_GET_ALL_FAVORITS_NOT
import com.realm.imade.db.BaseList
import com.realm.imade.db.RealmData
import com.realm.imade.db.RealmRepository
import kotlinx.coroutines.*

class PageViewModel : ViewModel() {


    private val baseListMutable = MutableLiveData<ArrayList<BaseList>>()
    private val realmData: RealmData = RealmRepository()
    private val favoritesListMutable = MutableLiveData<ArrayList<BaseList>>()
    private var baseInit = MutableLiveData<Boolean>()
    private var favoriteInit = MutableLiveData<Boolean>()


    val _baseListMutable: LiveData<ArrayList<BaseList>> = baseListMutable
    val _favoritesListMutable: LiveData<ArrayList<BaseList>> = favoritesListMutable
    val _baseInit: LiveData<Boolean> = baseInit
    val _favoriteInit: LiveData<Boolean> = favoriteInit


    fun add(boolean: Boolean, keyUrl: String, imageUri: String) {
        CoroutineScope(Dispatchers.Default).launch {
            realmData.addEntry(boolean, keyUrl, imageUri)
        }
    }

    fun subscribeBase() {
        val jobs = Job()
        realmData.getNetImage()
        viewModelScope.launch(jobs) {
            while (isActive) {
                delay(1_000)
                if (REALM_ADD) {
                    baseListMutable.value = realmData.getAllEntry()
                    if (REALM_GET_ALL) {
                        REALM_ADD = false
                        REALM_GET_ALL = false
                        baseInit.postValue(true)
                        jobs.cancel()
                    }
                }
            }
        }
    }


    fun subscribeFavorites() {
        val jobs = Job()
        realmData.getNetImage()
        viewModelScope.launch(jobs) {
            while (isActive) {
                delay(1_000)
                favoritesListMutable.value = realmData.getAllEFavoritsEntry()
                if (REALM_GET_ALL_FAVORITS) {
                    REALM_GET_ALL_FAVORITS = false
                    favoriteInit.postValue(true)
                    jobs.cancel()
                }
                if (REALM_GET_ALL_FAVORITS_NOT) {
                    REALM_GET_ALL_FAVORITS_NOT = false
                    favoriteInit.postValue(false)
                    jobs.cancel()
                }

            }
        }
    }

    fun deliteEntry(vararg primarKey: String?) {
        viewModelScope.launch {
            if (primarKey.isNotEmpty()) {
                if (primarKey[0] != null) realmData.delete(primarKey[0]) else realmData.delete()
            }
        }
    }

}