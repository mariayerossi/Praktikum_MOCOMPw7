package com.example.tugasm7.database

import androidx.room.*
import com.example.tugasm7.database.entity.AmbilKelasEntity
import com.example.tugasm7.database.entity.KelasEntity
import com.example.tugasm7.database.entity.UserEntity

/*
DAO ini interface yang jadi perantara kotlin dengan database.
Isinya method2 yang digunakan untuk melakukan query ke database
Untuk command basic kyk insert, update, dan delete bisa pake annotation
@Insert, @Update, dan @Delete dengan objek yang mau diinsert/update/delete yang dipassing sebagai
parameternya.

Kalo query2 yang bersifat lebih spesifik, bisa pakai @Query dengan isi querynya sebagai
parameter functionnya
 */
@Dao
interface DBDao {
    @Insert
    suspend fun insert(user: UserEntity)

    @Update
    suspend fun update(user:UserEntity)

    @Delete
    suspend fun delete(user:UserEntity)

    @Query("DELETE FROM users where username = :username")
    suspend fun deleteUser(username: String)

    @Query("SELECT * FROM users")
    suspend fun getAllUser():List<UserEntity>

    @Query("SELECT * FROM users where username = :username")
    suspend fun get(username:String):UserEntity?



    @Insert
    suspend fun insert(user: AmbilKelasEntity)

    @Update
    suspend fun update(user:AmbilKelasEntity)

    @Delete
    suspend fun delete(user:AmbilKelasEntity)

    @Query("DELETE FROM ambilkelas where id = :id")
    suspend fun deleteAmbilKelas(id: String)

    @Query("SELECT * FROM ambilkelas")
    suspend fun getAllAmbilKelas():List<AmbilKelasEntity>

    @Query("SELECT * FROM ambilkelas where id = :id")
    suspend fun getAmbilKelasByID(id:String):AmbilKelasEntity?

    @Query("SELECT count(*) FROM ambilkelas where idKelas = :id")
    suspend fun getCountKelas(id:Int):Int



    @Insert
    suspend fun insert(user: KelasEntity)

    @Update
    suspend fun update(user:KelasEntity)

    @Delete
    suspend fun delete(user:KelasEntity)

    @Query("DELETE FROM kelas where id = :id")
    suspend fun deleteKelas(id: String)


    @Query("SELECT * FROM kelas")
    suspend fun getKelas():List<KelasEntity>




}