package com.realm.imade.db

import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.query
import io.realm.kotlin.query.RealmQuery
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class ItemBase : RealmObject {
    @PrimaryKey
    var key: String = ""
    var imageUrl: String = ""
}

class ItemFavorites : RealmObject {
    var imageUrlFavorit: String = ""
}

class ItemFavoritesKey : RealmObject {
    @PrimaryKey
    var key = ""
    var items: ItemFavorites? = null
}

object DataBase {
    val config =
        RealmConfiguration.Builder(
            schema = setOf(
                ItemBase::class,
                ItemFavorites::class,
                ItemFavoritesKey::class
            )
        ).build()
    val realm = Realm.open(config)

    suspend fun delete(vararg primaryKey: Any?) {
        if (primaryKey[0] != null) {
            realm.write {
                val query: RealmQuery<ItemFavoritesKey> =
                    this.query("key = $0", primaryKey[0] as String)
                delete(query)
            }
        } else {
            realm.write {
                query<ItemBase>().find().also { delete(it) }
            }
        }
    }

    suspend fun addEntry(vararg it: Any) {
        realm.write {
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
}