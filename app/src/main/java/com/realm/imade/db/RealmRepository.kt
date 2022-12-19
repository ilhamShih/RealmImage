package com.realm.imade.db

import com.realm.imade.api.DataService
import com.realm.imade.api.RetrofitResponse.retrofitInstanceServer
import com.realm.imade.api.ServModel
import com.realm.imade.config.Config
import com.realm.imade.config.Config.DOMAIN
import com.realm.imade.config.Config.PROTOCOL
import com.realm.imade.config.Config.REALM_ADD
import com.realm.imade.config.Config.REALM_GET_ALL
import com.realm.imade.config.Config.REALM_GET_ALL_FAVORITS
import com.realm.imade.config.Config.REALM_GET_ALL_FAVORITS_NOT
import com.realm.imade.config.Config.SUB_DOMAIN
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.query
import io.realm.kotlin.query.RealmQuery
import io.realm.kotlin.query.find
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RealmRepository : RealmData {
    private val config = RealmConfiguration.Builder(
        schema = setOf(
            ItemBase::class,
            ItemFavorites::class,
            ItemFavoritesKey::class
        )
    ).build()
    private val realmAllItem = Realm.open(config)

    override fun getAllEntry(): ArrayList<BaseList> {
        val baseList = arrayListOf<BaseList>()
        val realmSize = realmAllItem.query<ItemBase>().find()
        var size = realmSize.size
        if (size > 0) {
            realmAllItem.query<ItemBase>().find {
                it.forEach { itFor ->
                    val list = BaseList(itFor.key, itFor.imageUrl)
                    baseList.add(list)
                    if (size == 1) {
                        REALM_GET_ALL = true
                    }
                    size--
                }
            }
        } else {
            REALM_GET_ALL = true
        }
        return baseList
    }

    override fun getAllEFavoritsEntry(): ArrayList<BaseList> {
        val baseList = arrayListOf<BaseList>()
        val realmSize = realmAllItem.query<ItemFavoritesKey>().find()
        var size = realmSize.size
        if (size > 0) {
            realmAllItem.query<ItemFavoritesKey>().find {
                it.forEach { itFor ->
                    val list = itFor.items?.imageUrlFavorit?.let { it1 -> BaseList(itFor.key, it1) }
                    list?.let { it1 -> baseList.add(it1) }
                    if (size == 1) {
                        REALM_GET_ALL_FAVORITS = true
                    }
                    size--
                }
            }
        } else {
            REALM_GET_ALL_FAVORITS_NOT = true
        }
        return baseList
    }

    override fun getNetImage() {
        retrofitInstanceServer!!.create(DataService::class.java)
            .getDataService().enqueue(object : Callback<ServModel> {
                override fun onResponse(call: Call<ServModel>, response: Response<ServModel>) {
                    CoroutineScope(Dispatchers.Default).launch {
                        if (response.isSuccessful) {
                            var size = response.body()?.let { Config.getSizeRetrofit(it) } as Int
                            response.body()?.let { model ->
                                model.mPhotos.mPhoto.reversed().forEach {
                                    size--
                                    addEntry(
                                        Config.BASE_LIST,
                                        it.id,
                                        "$PROTOCOL$SUB_DOMAIN${it.farm}$DOMAIN/${it.server}/${it.id}_${it.secret}.jpg"
                                    )
                                    if (size == 0) {
                                        REALM_ADD = true
                                    }

                                }
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<ServModel?>, t: Throwable) {
                    REALM_ADD = true
                }
            })
    }

    override suspend fun addEntry(vararg it: Any) {
        realmAllItem.write {
            copyToRealm(if (it[0] as Boolean) {
                ItemBase().apply {
                    key = it[1] as String
                    imageUrl = it[2] as String
                }
            } else {
                ItemFavoritesKey().apply {
                    key = it[1] as String
                    items = ItemFavorites().apply {
                        imageUrlFavorit = it[2] as String
                    }
                }
            }, updatePolicy = UpdatePolicy.ALL)
        }
    }

    override suspend fun delete(vararg primaryKey: Any?) {
        if (primaryKey[0] != null) {
            realmAllItem.write {
                val query: RealmQuery<ItemFavoritesKey> =
                    this.query("key = $0", primaryKey[0] as String)
                delete(query)
            }
        } else {
            realmAllItem.write {
                query<ItemBase>().find().also { delete(it) }
            }
        }
    }
}