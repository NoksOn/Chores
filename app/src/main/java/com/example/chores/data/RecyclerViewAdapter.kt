package com.example.chores.data

import android.app.AlertDialog
import android.content.Context
import android.support.v7.view.menu.MenuView
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.chores.R
import com.example.chores.model.ChoreModel
import kotlinx.android.synthetic.main.alert_dialog.*
import kotlinx.android.synthetic.main.alert_dialog.view.*
import kotlinx.android.synthetic.main.item_view.view.*

class RecyclerViewAdapter(private val mContext: Context,list:MutableList<ChoreModel>)
    : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

    var list:MutableList<ChoreModel>? = null
    init {
        this.list = list
        Log.d("Recycler","List size ${list.size}")
    }


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RecyclerViewAdapter.ViewHolder {
          var view:View = LayoutInflater.from(mContext).inflate(R.layout.item_view,p0,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {return list!!.size}

    override fun onBindViewHolder(p0: RecyclerViewAdapter.ViewHolder, p1: Int) {

             p0.choreName?.text = list!!.get(p1).choreName
             p0.assignedBy?.text = list!!.get(p1).assignedBy
             p0.assignedTo?.text = list!!.get(p1).assignedTo
             p0.timeAssigned?.text = list!!.get(p1).showNormalData(list!!.get(p1).timeAssigned!!)
    }

    inner class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView),View.OnClickListener{


        var choreName:TextView? = null
        var assignedBy: TextView? = null
        var assignedTo: TextView? = null
        var timeAssigned: TextView? = null
        var deleteChore: Button? = null
        var editChore: Button? = null

        init {
            choreName = itemView.findViewById(R.id.typeOfChores)
            assignedBy = itemView.findViewById(R.id.assignedByName)
            assignedTo = itemView.findViewById(R.id.assignedToName)
            timeAssigned = itemView.findViewById(R.id.timeOfCreated)
            deleteChore = itemView.findViewById(R.id.deleteItemButton)
            editChore = itemView.findViewById(R.id.editItemButton)
            editChore!!.setOnClickListener(this)
            deleteChore!!.setOnClickListener(this)
        }
        override fun onClick(v: View?) {
          var dbHandler:DataBaseHandler = DataBaseHandler(mContext)
            when(v?.id){
                R.id.editItemButton ->{
                    val mDialogView = LayoutInflater.from(mContext).inflate(R.layout.alert_dialog,null)
                    val mBuilder = AlertDialog.Builder(mContext).setView(mDialogView)
                    val mAlertDialog = mBuilder.show()
                    mDialogView.dialogOk.setOnClickListener {
                        if (!TextUtils.isEmpty(mDialogView.nameField.text.toString()) &&
                            !TextUtils.isEmpty(mDialogView.assignByField.text.toString()) &&
                            !TextUtils.isEmpty(mDialogView.assignToField.text.toString())
                        ) {
                            list!!.get(adapterPosition).choreName = mDialogView.nameField.text.toString()
                            list!!.get(adapterPosition).assignedBy = mDialogView.assignByField.text.toString()
                            list!!.get(adapterPosition).assignedTo = mDialogView.assignToField.text.toString()

                            Log.d("DataBase", "${dbHandler.updateChore(list!!.get(adapterPosition))}")
                            notifyItemChanged(adapterPosition, list!!.get(adapterPosition))
                            mAlertDialog.dismiss()
                        }
                        else{
                            Toast.makeText(mContext,"Enter Fields",Toast.LENGTH_LONG).show()
                        }
                    }

                        mDialogView.dialogCancel.setOnClickListener {
                            mAlertDialog.dismiss()
                        }



                }
                R.id.deleteItemButton ->{
                    dbHandler.deleteChore(list!!.get(adapterPosition).id!!)
                    list!!.removeAt(adapterPosition)
                    Log.d("Recycler"," After Delete Now At List ${list!!.size}")
                    notifyItemRemoved(adapterPosition)

                }
            }

        }
    }

}