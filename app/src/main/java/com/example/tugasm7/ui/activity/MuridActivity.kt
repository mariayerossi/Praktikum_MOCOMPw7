package com.example.tugasm7.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.example.tugasm7.R
import com.example.tugasm7.databinding.ActivityMuridBinding
import com.example.tugasm7.ui.fragment.MuridListFragment
import com.example.tugasm7.ui.fragment.MuridPilihFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MuridActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMuridBinding
    var usernamelogin:String=""
    var namalogin:String=""

    lateinit var muridListFragment: MuridListFragment
    lateinit var muridPilihFragment: MuridPilihFragment

    lateinit var fragmentManager: FragmentTransaction

    var bundle = Bundle()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMuridBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val bundle = intent.extras
        if(intent.hasExtra("usernamelogin")){
            Log.e("Cek Intent","masuk")
            usernamelogin=intent.getStringExtra("usernamelogin").toString()
            namalogin=intent.getStringExtra("namalogin").toString()
        }

        fragmentManager=supportFragmentManager.beginTransaction()
        muridListFragment= MuridListFragment(usernamelogin,namalogin)
        muridPilihFragment=MuridPilihFragment(usernamelogin,namalogin)


        binding.txtWelcomeMurid.text="Selamat Datang $namalogin !"

        val navView: BottomNavigationView = binding.navView

        muridPilihFragment.arguments = bundle
        fragmentManager.replace(R.id.layoutContainerMurid,muridPilihFragment)
        fragmentManager.commit()
        navView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.item_kelas_murid ->{
                    fragmentManager = supportFragmentManager.beginTransaction()
                    muridListFragment.arguments = bundle
                    fragmentManager.replace(R.id.layoutContainerMurid,muridListFragment)
                    fragmentManager.commit()
                    true
                }
                R.id.item_pilih_murid ->{
                    fragmentManager = supportFragmentManager.beginTransaction()
                    muridPilihFragment.arguments = bundle
                    fragmentManager.replace(R.id.layoutContainerMurid,muridPilihFragment)
                    fragmentManager.commit()
                    true
                }
                else -> {
                    true
                }
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_logout,menu)
        return super.onCreateOptionsMenu(menu)
    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.item_logout ->{
                var customerLogout = Intent(this, LoginActivity::class.java);
                customerLogout.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                Toast.makeText(this, "Berhasil Logout", Toast.LENGTH_SHORT).show()
                finish()
                startActivity(customerLogout);
            }
        }
        return super.onOptionsItemSelected(item)
    }

}