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

@Entity(tableName = "ambilkelas")
data class AmbilKelasEntity(
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    val idKelas:Int,
    val usernameMurid: String,
    var statusSelesai: Boolean,
){
}
