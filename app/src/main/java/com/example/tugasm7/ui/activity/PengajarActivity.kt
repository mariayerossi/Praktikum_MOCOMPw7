package com.example.tugasm7.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.FrameLayout
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.tugasm7.R
import com.example.tugasm7.databinding.ActivityPengajarBinding
import com.example.tugasm7.ui.fragment.MuridListFragment
import com.example.tugasm7.ui.fragment.MuridPilihFragment
import com.example.tugasm7.ui.fragment.PengajarAddFragment
import com.example.tugasm7.ui.fragment.PengajarListFragment

class PengajarActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPengajarBinding

    var usernamelogin:String=""
    var namalogin:String=""

    lateinit var pengajarAddFragment: PengajarAddFragment
    lateinit var pengajarListFragment: PengajarListFragment

    lateinit var fragmentManager: FragmentTransaction

    var bundle = Bundle()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPengajarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(intent.hasExtra("usernamelogin")){
            usernamelogin=intent.getStringExtra("usernamelogin").toString()
            namalogin=intent.getStringExtra("namalogin").toString()
        }


        pengajarAddFragment=PengajarAddFragment(usernamelogin,namalogin,"add",null)
        pengajarListFragment=PengajarListFragment(usernamelogin,namalogin)

        binding.txtWelcomePengajar.text="Selamat Datang $namalogin !"
        val navView: BottomNavigationView = binding.navView
        fragmentManager=supportFragmentManager.beginTransaction()
        pengajarAddFragment.arguments = bundle
        fragmentManager.replace(R.id.layoutContainerPengajar,pengajarAddFragment)
        fragmentManager.commit()

        pengajarListFragment.onToEditListener={
            pengajarAddFragment=PengajarAddFragment(usernamelogin,namalogin,"edit",it)
            fragmentManager = supportFragmentManager.beginTransaction()
            pengajarListFragment.arguments = bundle
            fragmentManager.replace(R.id.layoutContainerPengajar,pengajarAddFragment)
            fragmentManager.commit()
            true
        }


        navView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.item_kelas_pengajar ->{
                    fragmentManager = supportFragmentManager.beginTransaction()
                    pengajarListFragment.arguments = bundle
                    fragmentManager.replace(R.id.layoutContainerPengajar,pengajarListFragment)
                    fragmentManager.commit()
                    true
                }
                R.id.item_tambah_pengajar ->{
                    fragmentManager = supportFragmentManager.beginTransaction()
                    pengajarAddFragment.arguments = bundle
                    fragmentManager.replace(R.id.layoutContainerPengajar,pengajarAddFragment)
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