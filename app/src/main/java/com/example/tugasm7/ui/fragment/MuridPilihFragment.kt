package com.example.tugasm7.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tugasm7.database.AppDatabase
import com.example.tugasm7.database.DBDao
import com.example.tugasm7.database.entity.AmbilKelasEntity
import com.example.tugasm7.database.entity.KelasEntity
import com.example.tugasm7.database.entity.UserEntity
import com.example.tugasm7.databinding.FragmentMuridPilihBinding
import com.example.tugasm7.ui.RecyclerviewAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MuridPilihFragment(val usernamelogin:String,val namalogin:String) : Fragment(),RecyclerviewAdapter.AdapterCallback {

    private val coroutine = CoroutineScope(Dispatchers.IO)
    private lateinit var db: AppDatabase
    private lateinit var dao: DBDao

    private var listUser= arrayListOf<UserEntity>()
    private var listPengajar= arrayListOf<UserEntity>()
    private var listKelas= arrayListOf<KelasEntity>()
    private var listAmbilKelas= arrayListOf<AmbilKelasEntity>()

    private var spinnerArray:ArrayList<String> =  ArrayList();

    private lateinit var adapterSpinner: ArrayAdapter<String>;

    private lateinit var binding: FragmentMuridPilihBinding

    private lateinit var adapters:RecyclerviewAdapter

    var spinnerPos=0
    var indexPengajar=0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentMuridPilihBinding.inflate(inflater,container,false)
        return binding.root
    }

    @SuppressLint("UseRequireInsteadOfGet")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        db=AppDatabase.build(this.context)
        dao=db.dbDao
        coroutine.launch {
            listUser= dao.getAllUser() as ArrayList<UserEntity>
            spinnerArray.add("Semua")
            for (i in listUser){
                if(i.role=="pengajar"){
                    listPengajar.add(i)
                    spinnerArray.add(i.name)
                }
            }
            adapterSpinner=ArrayAdapter<String>(
                this@MuridPilihFragment.context!!, android.R.layout.simple_spinner_item, spinnerArray
            )
            adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spPilihanOleh.adapter=adapterSpinner
            activity?.runOnUiThread{
                binding.spPilihanOleh.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View,
                        position: Int,
                        id: Long
                    ) {
                        indexPengajar=position
                        refreshAllData(indexPengajar)
                    }
                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                })
            }
            listKelas= dao.getKelas() as ArrayList<KelasEntity>
            listAmbilKelas= dao.getAllAmbilKelas() as ArrayList<AmbilKelasEntity>
        }

        adapters=RecyclerviewAdapter(this,usernamelogin)
        binding.rvPilihKelasMurid.apply {
            adapter=adapters
            layoutManager= LinearLayoutManager(this.context)
        }
        registerForContextMenu(binding.rvPilihKelasMurid)

        refreshAllData(indexPengajar)
    }
    fun refreshAllData(indexPengajar: Int){
        coroutine.launch {
            listKelas= dao.getKelas() as ArrayList<KelasEntity>
            listAmbilKelas= dao.getAllAmbilKelas() as ArrayList<AmbilKelasEntity>
            adapters.refreshTipe(RecyclerviewAdapter.TYPE_PILIH_MURID)
            activity?.runOnUiThread {
                olahData(indexPengajar)
            }
        }
    }
    fun olahData(indexPengajar:Int){
        var temp=listKelas
        var tempambil=listAmbilKelas
        var tempKirim= arrayListOf<KelasEntity>()
        if(indexPengajar==0){
            for(i in temp){
                var cekAmbil=false
                for (j in tempambil){
                    if(i.id==j.idKelas && j.usernameMurid==usernamelogin)cekAmbil=true
                }
                if(!cekAmbil){
                    tempKirim.add(i)
                }
            }
        }
        else{
            val usernamePengajar=listPengajar[indexPengajar-1].username
            for(i in temp){
                if(i.usernamepengajar==usernamePengajar){
                    var cekAmbil=false
                    for (j in tempambil ){
                        if(i.id==j.idKelas && j.usernameMurid==usernamelogin)cekAmbil=true
                    }
                    if(!cekAmbil){
                        tempKirim.add(i)
                    }
                }
            }
        }
        if(tempKirim.size>0){
            binding.rvPilihKelasMurid.visibility=View.VISIBLE
            adapters.refreshDataListKelas(tempKirim)
            adapters.refreshDataListAmbilKelas(listAmbilKelas)
        }
        else{
            binding.rvPilihKelasMurid.visibility=View.INVISIBLE
        }

    }

    override fun onListPengajarLongClicked(tipe: String, kelas: KelasEntity) {

    }

    override fun onUserPilihLongClicked(kelas: KelasEntity) {
        coroutine.launch {
            val ambilKelasEntity= AmbilKelasEntity(0,kelas.id,usernamelogin,false)
            dao.insert(ambilKelasEntity)
            refreshAllData(indexPengajar)
            activity?.runOnUiThread{
                Toast.makeText(this@MuridPilihFragment.context, "Berhasil Daftar Kelas", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onUserListLongClicked(tipe: String, kelas: KelasEntity, ambilKelasEntity: AmbilKelasEntity) {
    }


}