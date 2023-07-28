package com.example.tugasm7.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.tugasm7.database.AppDatabase
import com.example.tugasm7.database.DBDao
import com.example.tugasm7.database.entity.UserEntity
import com.example.tugasm7.databinding.ActivityRegisterBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {

    private val coroutine = CoroutineScope(Dispatchers.IO)
    lateinit var binding : ActivityRegisterBinding
    private lateinit var db: AppDatabase
    private lateinit var dao: DBDao
    private var role=""
    private var listUser= arrayListOf<UserEntity>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db=AppDatabase.build(this)
        dao=db.dbDao
        coroutine.launch {
            listUser= dao.getAllUser() as ArrayList<UserEntity>
        }

        binding.btnToLoginRegister.setOnClickListener {
            val i= Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(i)
            finish()
        }
        binding.btnRegisterRegister.setOnClickListener {
            if(binding.rbMurid.isChecked)role="murid" else role="pengajar"
            if(binding.etUsernameRegister.text.isNullOrEmpty()||binding.etNameRegister.text.isNullOrEmpty()||binding.etPasswordRegister.text.isNullOrEmpty()||binding.etConPassRegister.text.isNullOrEmpty()){
                Toast.makeText(this,"Field tidak boleh kosong!",Toast.LENGTH_SHORT).show()
            }
            else{
                Log.e("size",listUser.size.toString())
                if(listUser.isEmpty()){
                    doInsert()
                }
                else{
                    var cekuser=true
                    for(i in listUser){

                        if(i.username==binding.etUsernameRegister.text.toString())cekuser=false
                    }

                    if(cekuser){
                        if(binding.etPasswordRegister.text.toString()!=binding.etConPassRegister.text.toString()){
                            Toast.makeText(this,"Password dan Konfirmasi Password Tidak Sesuai",Toast.LENGTH_SHORT).show()
                        }
                        else{
                            if(binding.rbMurid.isChecked)role="murid" else role="pengajar"
                            doInsert()
                        }
                    }
                    else{
                        Toast.makeText(this,"Username Sudah Digunakan",Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }


    fun doInsert(){
        coroutine.launch {
            val user=UserEntity(binding.etUsernameRegister.text.toString(),binding.etPasswordRegister.text.toString(),binding.etNameRegister.text.toString(),role)
            dao.insert(user)
            listUser.add(user)
        }
        Toast.makeText(this,"Berhasil Register",Toast.LENGTH_SHORT).show()
        binding.etUsernameRegister.text.clear()
        binding.etConPassRegister.text.clear()
        binding.etPasswordRegister.text.clear()
        binding.etNameRegister.text.clear()
        binding.rbPengajar.isChecked = true
    }
}