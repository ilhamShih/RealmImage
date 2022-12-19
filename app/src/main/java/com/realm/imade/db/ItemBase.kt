package com.realm.imade.db

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
