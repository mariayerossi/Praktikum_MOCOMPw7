package com.example.tugasm7.ui

import android.util.Log
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.example.tugasm7.R
import com.example.tugasm7.database.entity.AmbilKelasEntity
import com.example.tugasm7.database.entity.KelasEntity
import com.example.tugasm7.databinding.ItemListRecyclerviewBinding
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList


class RecyclerviewAdapter(val callback:AdapterCallback, val usernameLogin:String): RecyclerView.Adapter<RecyclerviewAdapter.ViewHolder>(),View.OnCreateContextMenuListener {

    private var listKelas = ArrayList<KelasEntity>()
    private var listAmbilKelas = ArrayList<AmbilKelasEntity>()
    private var tipe=0

    fun refreshDataListKelas(listKelasKirim:ArrayList<KelasEntity>){

        listKelas.clear()
        listKelas.addAll(listKelasKirim)
        notifyDataSetChanged()
    }
    fun refreshDataListAmbilKelas(listAmbilKelasKirim:ArrayList<AmbilKelasEntity>){
        listAmbilKelas.clear()
        listAmbilKelas.addAll(listAmbilKelasKirim)
        notifyDataSetChanged()
    }

    fun refreshTipe(tipeKirim:Int){
        tipe=tipeKirim
    }


    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)  {
        private val binding = ItemListRecyclerviewBinding.bind(view)
        fun bind(dataKelas:KelasEntity){
            if(tipe==TYPE_PILIH_MURID){
                val number = dataKelas.harga
                val formattedNumber: String = String.format(Locale.US,"%,d", number).replace(",",".")
                binding.txtJumlahMurid.visibility=View.GONE
                binding.txtStatus.visibility=View.GONE
                binding.txtHarga.text="Rp. $formattedNumber,00"
                binding.txtPengajar.text="Oleh : ${dataKelas.namapengajar}"
                binding.txtNamaKelas.text=dataKelas.name
                view.setOnLongClickListener {
                    callback.onUserPilihLongClicked(dataKelas)
                    true
                }
            }
            else if(tipe== TYPE_LIST_MURID){
                binding.txtJumlahMurid.visibility=View.GONE
                binding.txtHarga.visibility=View.GONE
                var dataAmbil:AmbilKelasEntity?=null
                for(i in listAmbilKelas){
                    if(i.idKelas==dataKelas.id&& i.usernameMurid==usernameLogin){
                        dataAmbil=i
                    }
                }
                if(dataAmbil?.statusSelesai == true){
                    binding.txtStatus.text="Status : Selesai"
                }
                else{
                    binding.txtStatus.text="Status : Belum Selesai"
                }
                binding.txtPengajar.text="Oleh : ${dataKelas.namapengajar}"
                binding.txtNamaKelas.text=dataKelas.name
                view.setOnClickListener {
                    val popup = PopupMenu(it.context, binding.txtStatus)
                    popup.inflate(R.menu.menu_murid)
                    popup.setOnMenuItemClickListener {
                        when (it.getItemId()) {
                            R.id.item_murid_delete ->
                                callback.onUserListLongClicked("delete", dataKelas,dataAmbil!!)
                            R.id.item_murid_edit ->                         //handle menu2 click
                                callback.onUserListLongClicked("edit", dataKelas,dataAmbil!!)
                            else -> false
                        }
                        true
                    }
                    popup.show()
                    true
                }

            }
            else if( tipe==TYPE_LIST_PENGAJAR){
                val number = dataKelas.harga
                val formattedNumber: String = String.format(Locale.US,"%,d", number).replace(",",".")
                binding.txtPengajar.visibility=View.GONE
                binding.txtStatus.visibility=View.GONE
                binding.txtHarga.text="Rp. $formattedNumber,00"
                var countjml=0
                for (i in listAmbilKelas){
                    if (i.idKelas==dataKelas.id)countjml++
                }

                binding.txtJumlahMurid.text="Jumlah Murid : $countjml"
                binding.txtNamaKelas.text=dataKelas.name
                view.setOnClickListener {
                    Log.e("cek click pengajar atas", "masuk long click tipe - $tipe")
                    val popup = PopupMenu(it.context, binding.txtHarga)
                    popup.inflate(R.menu.menu_pengajar)
                    //adding click listener
                    popup.setOnMenuItemClickListener {
                        when (it.getItemId()) {
                            R.id.item_pengajar_delete ->
                                //handle menu1 click
                                callback.onListPengajarLongClicked("delete", dataKelas)
                            R.id.item_pengajar_edit ->                         //handle menu2 click
                                callback.onListPengajarLongClicked("edit", dataKelas)
                            else -> false
                        }
                        true
                    }

                    popup.show()
                }
            }
        }
    }


    companion object {
        const val TYPE_PILIH_MURID = 0
        const val TYPE_LIST_MURID = 1
        const val TYPE_LIST_PENGAJAR= 2

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_list_recyclerview, parent, false)
        )


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listKelas[position])
    }

    override fun getItemCount(): Int {
        return listKelas.size
    }

    interface AdapterCallback {
        fun onListPengajarLongClicked(tipe:String,kelas:KelasEntity)
        fun onUserPilihLongClicked(kelas: KelasEntity)
//        fun onUserListLongClicked(tipe:String,ambilKelasEntity: AmbilKelasEntity)
        fun onUserListLongClicked(tipe:String,kelas: KelasEntity,ambilKelasEntity: AmbilKelasEntity)

    }

    override fun onCreateContextMenu(
        menu: ContextMenu,
        v: View,
        menuInfo: ContextMenu.ContextMenuInfo
    ) {
        menu.add(0, v.id, 0, "Delete")
        menu.add(0, v.id, 0, "Edit")
    }
}

