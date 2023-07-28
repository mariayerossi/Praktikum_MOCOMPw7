package com.example.tugasm7.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/*
Entity ini ibarat sebuah tabel dalam database. Sebuah entity wajib ada annotation @Entity nya
disertai dengan nama table

Primary key sebuah tabel ditentukan dengan menaruh sebuah @PrimaryKey di ATAS sebuah variabel
Pada kasus file ini, username menjadi sebuah primary keynya.

Variabel lain akan dianggap sebuah column dalam database dengan nama variabel itu sendiri sebagai
nama column
 */

@Entity(tableName = "kelas")
data class KelasEntity(
    @PrimaryKey (autoGenerate = true)
    val id:Int,
    val usernamepengajar: String,
    val namapengajar: String,
    var harga: Int,
    var name:  String,
    var keterangan: String
){
}
