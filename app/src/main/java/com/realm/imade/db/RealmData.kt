package com.realm.imade.db


interface RealmData {

    fun getAllEntry(): ArrayList<BaseList>

    fun getAllEFavoritsEntry(): ArrayList<BaseList>

    fun getNetImage()

    suspend fun addEntry(vararg it: Any)

    suspend fun delete(vararg primaryKey: Any?)

}