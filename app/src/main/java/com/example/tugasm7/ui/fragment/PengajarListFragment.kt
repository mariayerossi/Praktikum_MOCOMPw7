package com.example.tugasm7.ui.fragment

import android.R
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tugasm7.database.AppDatabase
import com.example.tugasm7.database.DBDao
import com.example.tugasm7.database.entity.AmbilKelasEntity
import com.example.tugasm7.database.entity.KelasEntity
import com.example.tugasm7.database.entity.UserEntity
import com.example.tugasm7.databinding.FragmentPengajarListBinding
import com.example.tugasm7.ui.RecyclerviewAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class PengajarListFragment(val usernamelogin:String,val namalogin:String) : Fragment(),RecyclerviewAdapter.AdapterCallback{

    private val coroutine = CoroutineScope(Dispatchers.IO)
    private lateinit var db: AppDatabase
    private lateinit var dao: DBDao

    private var listUser= arrayListOf<UserEntity>()
    private var listKelas= arrayListOf<KelasEntity>()
    private var listAmbilKelas= arrayListOf<AmbilKelasEntity>()

    private lateinit var adapters:RecyclerviewAdapter

    private lateinit var binding:FragmentPengajarListBinding

    var onToEditListener:(( data: KelasEntity)-> Unit)? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentPengajarListBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        db=AppDatabase.build(this.context)
        dao=db.dbDao
        adapters=RecyclerviewAdapter(this,usernamelogin)

        binding.rvListKelasPengajar.apply {
            adapter=adapters
            layoutManager=LinearLayoutManager(this.context)
        }
        registerForContextMenu(binding.rvListKelasPengajar)

       refreshAllData()

    }

    override fun onListPengajarLongClicked(tipe:String,kelas: KelasEntity) {
       if(tipe=="delete"){
           coroutine.launch {
               val jumlahmurid=dao.getCountKelas(kelas.id)
               if(jumlahmurid>0){
                   activity?.runOnUiThread {
                       Toast.makeText(this@PengajarListFragment.context, "Gagal Delete ! Ada murid yang terdaftar pada kelas ini", Toast.LENGTH_SHORT).show()
                   }
               }
               else{
                   dao.delete(kelas)
                   adapters.refreshTipe(RecyclerviewAdapter.TYPE_LIST_PENGAJAR)
                   refreshAllData()
                   activity?.runOnUiThread {
                       Toast.makeText(this@PengajarListFragment.context, "Berhasil delete kelas", Toast.LENGTH_SHORT).show()
                   }
               }


           }
       }
       else if(tipe=="edit"){
           onToEditListener?.invoke(kelas)
       }
    }

    override fun onUserPilihLongClicked(kelas: KelasEntity) {
        //nothing
    }

    override fun onUserListLongClicked(tipe: String, kelas: KelasEntity, ambilKelasEntity: AmbilKelasEntity) {
    }

    override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenu.ContextMenuInfo?)
    {
        super.onCreateContextMenu(menu, v, menuInfo)
        menu.add(0, v.id, 0, "Delete")
        menu.add(0, v.id, 0, "Edit")
    }

    fun refreshAllData(){
        coroutine.launch {
            listKelas= dao.getKelas() as ArrayList<KelasEntity>
            listAmbilKelas= dao.getAllAmbilKelas() as ArrayList<AmbilKelasEntity>
            adapters.refreshTipe(RecyclerviewAdapter.TYPE_LIST_PENGAJAR)
            activity?.runOnUiThread {
                olahData()
            }
        }
    }

    fun olahData(){
        var temp=listKelas
        var tempKirim= arrayListOf<KelasEntity>()
        for (i in temp){
            Log.e("comapare username","${i.usernamepengajar} - $usernamelogin}")
            if(i.usernamepengajar==usernamelogin)tempKirim.add(i)
        }
        adapters.refreshDataListKelas(tempKirim)
        adapters.refreshDataListAmbilKelas(listAmbilKelas)
    }

}