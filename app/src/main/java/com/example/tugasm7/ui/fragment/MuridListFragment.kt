package com.example.tugasm7.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tugasm7.R
import com.example.tugasm7.database.AppDatabase
import com.example.tugasm7.database.DBDao
import com.example.tugasm7.database.entity.AmbilKelasEntity
import com.example.tugasm7.database.entity.KelasEntity
import com.example.tugasm7.database.entity.UserEntity
import com.example.tugasm7.databinding.FragmentMuridListBinding
import com.example.tugasm7.databinding.FragmentPengajarListBinding
import com.example.tugasm7.ui.RecyclerviewAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MuridListFragment(val usernamelogin:String,val namalogin:String) : Fragment(),RecyclerviewAdapter.AdapterCallback {

    private val coroutine = CoroutineScope(Dispatchers.IO)
    private lateinit var db: AppDatabase
    private lateinit var dao: DBDao

    private var listKelas= arrayListOf<KelasEntity>()
    private var listAmbilKelas= arrayListOf<AmbilKelasEntity>()

    private lateinit var adapters:RecyclerviewAdapter

    private lateinit var binding:FragmentMuridListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentMuridListBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        db=AppDatabase.build(this.context)
        dao=db.dbDao
        adapters=RecyclerviewAdapter(this,usernamelogin)
        binding.rvListKelasMurid.apply {
            adapter=adapters
            layoutManager= LinearLayoutManager(this.context)
        }
        registerForContextMenu(binding.rvListKelasMurid)

        refreshAllData()
    }

    fun refreshAllData(){
        coroutine.launch {
            listKelas= dao.getKelas() as ArrayList<KelasEntity>
            listAmbilKelas= dao.getAllAmbilKelas() as ArrayList<AmbilKelasEntity>
            adapters.refreshTipe(RecyclerviewAdapter.TYPE_LIST_MURID)
            activity?.runOnUiThread {
                olahData()
            }
        }
    }

    fun olahData(){
        var temp=listKelas
        var tempambil=listAmbilKelas
        var tempKirim= arrayListOf<KelasEntity>()
        for(i in temp){
            var cekAmbil=false
            for (j in tempambil){
                if(i.id==j.idKelas && j.usernameMurid==usernamelogin)cekAmbil=true
            }
            if(cekAmbil){
                tempKirim.add(i)
            }
        }

        adapters.refreshDataListKelas(tempKirim)
        adapters.refreshDataListAmbilKelas(listAmbilKelas)
    }

    override fun onListPengajarLongClicked(tipe: String, kelas: KelasEntity) {
        //Nothing
    }

    override fun onUserPilihLongClicked(kelas: KelasEntity) {
        //Nothing
    }

    override fun onUserListLongClicked(tipe: String, kelas: KelasEntity, ambilKelasEntity: AmbilKelasEntity) {
        if(tipe=="delete"){
            coroutine.launch {
                dao.delete(ambilKelasEntity)
                activity?.runOnUiThread{
                    Toast.makeText(this@MuridListFragment.context, "Berhasil Delete Kelas", Toast.LENGTH_SHORT).show()
                }
                val tempAllKelas=listAmbilKelas
                for (i in 0 until listAmbilKelas.size){
                    if(listAmbilKelas[i].id==ambilKelasEntity.id){
                        tempAllKelas.removeAt(i)
                    }
                }
                listAmbilKelas.clear()
                listAmbilKelas.addAll(tempAllKelas)
            }


        }
        else if(tipe=="edit"){
            ambilKelasEntity.statusSelesai=!ambilKelasEntity.statusSelesai
            coroutine.launch {
                dao.update(ambilKelasEntity)
                activity?.runOnUiThread{
                    Toast.makeText(this@MuridListFragment.context, "Berhasil Update Status Kelas", Toast.LENGTH_SHORT).show()
                }
                for (i in listAmbilKelas){
                    if(i.id==ambilKelasEntity.id){
                        i.statusSelesai=ambilKelasEntity.statusSelesai
                    }
                }
            }
        }
        refreshAllData()

    }
}