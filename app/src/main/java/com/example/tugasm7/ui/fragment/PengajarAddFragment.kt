package com.example.tugasm7.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.tugasm7.R
import com.example.tugasm7.database.AppDatabase
import com.example.tugasm7.database.DBDao
import com.example.tugasm7.database.entity.AmbilKelasEntity
import com.example.tugasm7.database.entity.KelasEntity
import com.example.tugasm7.database.entity.UserEntity
import com.example.tugasm7.databinding.FragmentPengajarAddBinding
import com.example.tugasm7.databinding.FragmentPengajarListBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PengajarAddFragment(val usernamelogin:String,val namalogin:String,val tipe:String,val dataKelas:KelasEntity?) : Fragment() {

    private val coroutine = CoroutineScope(Dispatchers.IO)
    private lateinit var db: AppDatabase
    private lateinit var dao: DBDao


    private lateinit var binding:FragmentPengajarAddBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentPengajarAddBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        db=AppDatabase.build(this.context)
        dao=db.dbDao

        if(tipe=="add"){
            binding.btnAddPengajar.text="Tambah"
        }
        else if(tipe=="edit"){
            binding.etHarga.setText(dataKelas?.harga.toString())
            binding.etNamaKelas.setText(dataKelas?.name.toString())
            binding.etKeteranganKelas.setText(dataKelas?.keterangan.toString())
            binding.btnAddPengajar.text="Simpan"
        }


        binding.btnAddPengajar.setOnClickListener {
            if(binding.etNamaKelas.text.isNullOrEmpty()||binding.etHarga.text.isNullOrEmpty()||binding.etKeteranganKelas.text.isNullOrEmpty()){
                Toast.makeText(this.context,"Field tidak boleh kosong!", Toast.LENGTH_SHORT).show()
            }
            else{
                var keteranganKelas=""
                if(binding.etKeteranganKelas.text.isNotEmpty()){
                    keteranganKelas=binding.etKeteranganKelas.text.toString()
                }
                if(tipe=="add"){
                    coroutine.launch {
                        val kelasEntity=KelasEntity(0,usernamelogin,namalogin,binding.etHarga.text.toString().toInt(),binding.etNamaKelas.text.toString(),keteranganKelas)
                        dao.insert(kelasEntity)
                        activity?.runOnUiThread {
                            binding.etHarga.text.clear()
                            binding.etNamaKelas.text.clear()
                            binding.etKeteranganKelas.text.clear()
                            Toast.makeText(this@PengajarAddFragment.context,"Berhasil add Kelas", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                else if(tipe=="edit"){
                    coroutine.launch {
                        dataKelas?.name=binding.etNamaKelas.text.toString()
                        dataKelas?.harga=binding.etHarga.text.toString().toInt()
                        dataKelas?.keterangan=keteranganKelas
                        dao.update(dataKelas!!)
                        activity?.runOnUiThread {
                            Toast.makeText(this@PengajarAddFragment.context,"Berhasil Edit Kelas", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

            }
        }


    }

}