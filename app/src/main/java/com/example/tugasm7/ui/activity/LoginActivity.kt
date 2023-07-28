package com.example.tugasm7.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.tugasm7.database.AppDatabase
import com.example.tugasm7.database.DBDao
import com.example.tugasm7.database.entity.KelasEntity
import com.example.tugasm7.database.entity.UserEntity
import com.example.tugasm7.databinding.ActivityLoginBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private var listUser= arrayListOf<UserEntity>()
    private val coroutine = CoroutineScope(Dispatchers.IO)
    private lateinit var db: AppDatabase
    private lateinit var dao: DBDao

    private lateinit var binding:ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


        db=AppDatabase.build(this)
        dao=db.dbDao
        coroutine.launch {
            listUser= dao.getAllUser() as ArrayList<UserEntity>
        }
        binding.btnToRegisterLogin.setOnClickListener {
            val i= Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(i)
            finish()
        }

        binding.btnLoginLogin.setOnClickListener {
            var role=""
            var username=""
            var nama=""
            if(binding.etUsernameLogin.text.toString().isNotEmpty()&&binding.etPasswordLogin.text.toString().isNotEmpty()){
                var ceklogin=false
                for (i in listUser){
                    if(i.username==binding.etUsernameLogin.text.toString()&&binding.etPasswordLogin.text.toString()==i.password){
                        ceklogin=true
                        username=i.username
                        nama=i.name
                        role=i.role
                    }
                }
                if(ceklogin){
                    Log.e("masuk cek","pass - $role")
                    if(role=="murid"){
                        val i=Intent(this, MuridActivity::class.java)
                        i.putExtra("usernamelogin",username)
                        i.putExtra("namalogin",nama)
                        startActivity(i)
                    }
                    else if(role=="pengajar"){
                        val i=Intent(this, PengajarActivity::class.java)
                        i.putExtra("usernamelogin",username)
                        i.putExtra("namalogin",nama)
                        startActivity(i)
                    }
                }
                else{
                    Toast.makeText(this, "Username Tidak ditemukan atau password salah", Toast.LENGTH_SHORT).show()
                }
            }
            else{
                Toast.makeText(this,"Field tidak boleh kosong!",Toast.LENGTH_SHORT).show()
            }

        }
    }
}