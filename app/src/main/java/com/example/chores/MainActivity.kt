package com.example.chores

import android.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.util.Log
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.chores.data.DataBaseHandler
import com.example.chores.data.RecyclerViewAdapter
import com.example.chores.model.ChoreModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.alert_dialog.*
import kotlinx.android.synthetic.main.alert_dialog.view.*

class MainActivity : AppCompatActivity() {


    private var adapter:RecyclerViewAdapter? = null
    private var list:MutableList<ChoreModel> = mutableListOf()
    private var dbHandler:DataBaseHandler? = null
    private var layoutManager: RecyclerView.LayoutManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHandler = DataBaseHandler(this)
        layoutManager = LinearLayoutManager(this)
        readFromDataBase()
        adapter = RecyclerViewAdapter(this,list!!)
        mainListView.layoutManager = layoutManager
        mainListView.adapter = adapter


    }

    fun readFromDataBase(){
         list.addAll(dbHandler!!.readChores())
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.tob_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(item!!.itemId == R.id.Menu){
           createAlertDialog()
        }
        return super.onOptionsItemSelected(item)
    }

    fun createAlertDialog(){
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.alert_dialog,null)
        val mBuilder = AlertDialog.Builder(this).setView(mDialogView)
        val mAlertDialog = mBuilder.show()
        mDialogView.dialogOk.setOnClickListener{

            if (!TextUtils.isEmpty(mDialogView.nameField.text)&&
                !TextUtils.isEmpty(mDialogView.assignByField.text)&&
                !TextUtils.isEmpty(mDialogView.assignToField.text)) {

                val choreName: String = mDialogView.nameField.text.toString()
                val assignedTo: String = mDialogView.assignByField.text.toString()
                val assignedBy: String = mDialogView.assignToField.text.toString()
                var choreModel: ChoreModel = ChoreModel(choreName, assignedBy, assignedTo, System.currentTimeMillis())
                dbHandler!!.createChore(choreModel)
                adapter!!.list = dbHandler!!.readChores()
                Log.d("Recycler", "Now list size is ${adapter!!.list!!.size}")
                adapter!!.notifyDataSetChanged()
                mAlertDialog.dismiss()
            }
            else{
                Toast.makeText(applicationContext,"Enter Fields",Toast.LENGTH_LONG).show()
            }
        }
        mDialogView.dialogCancel.setOnClickListener {
            mAlertDialog.dismiss()
        }
    }


}
