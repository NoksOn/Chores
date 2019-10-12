package com.example.chores.data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.chores.model.*


class DataBaseHandler(mContext: Context):SQLiteOpenHelper(mContext,DATABASE_NAME,null,DATABASE_VERSION),IDataBaseHandler{

    override fun onCreate(db: SQLiteDatabase?) {
        var CREATE_CHORE_TABLE = "CREATE TABLE $TABLE_NAME ($KEY_ID INTEGER PRIMARY KEY," +
                "$KEY_CHORE_NAME TEXT," +
                "$KEY_CHORE_ASSIGNED_BY TEXT," +
                "$KEY_CHORE_ASSIGNED_TO TEXT," +
                "$KEY_CHORE_ASSIGNED_TIME LONG);"
        db?.execSQL(CREATE_CHORE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    override fun createChore(choreModel: ChoreModel) {
        var db:SQLiteDatabase = writableDatabase
        var values:ContentValues = ContentValues()

        values.put(KEY_CHORE_NAME,choreModel.choreName)
        values.put(KEY_CHORE_ASSIGNED_BY,choreModel.assignedBy)
        values.put(KEY_CHORE_ASSIGNED_TO,choreModel.assignedTo)
        values.put(KEY_CHORE_ASSIGNED_TIME,System.currentTimeMillis())

        db.insert(TABLE_NAME,null,values)
        Log.d("DataBase","Create a new Chore ${values.toString()}")
        db.close()
    }

    override fun deleteChore(id: Int) {

        var db:SQLiteDatabase = writableDatabase
        db.delete(TABLE_NAME,"$KEY_ID =?", arrayOf(id.toString()))
        Log.d("DataBase","You delete a chore $id")
        db.close()

    }

    override fun readChores(): MutableList<ChoreModel> {

        var resultList = mutableListOf<ChoreModel>()
        var db:SQLiteDatabase = readableDatabase
        var cursor:Cursor = db.rawQuery("SELECT * FROM $TABLE_NAME",null)

        if(cursor.moveToFirst()){
            do{
                var choreModel:ChoreModel = ChoreModel()
                choreModel.id = cursor.getInt(cursor.getColumnIndex(KEY_ID))
                choreModel.choreName = cursor.getString(cursor.getColumnIndex(KEY_CHORE_NAME))
                choreModel.assignedBy = cursor.getString(cursor.getColumnIndex(KEY_CHORE_ASSIGNED_BY))
                choreModel.assignedTo = cursor.getString(cursor.getColumnIndex(KEY_CHORE_ASSIGNED_TO))
                choreModel.timeAssigned = cursor.getLong(cursor.getColumnIndex(KEY_CHORE_ASSIGNED_TIME))

                resultList.add(choreModel)
            }while(cursor.moveToNext())
        }
        Log.d("DataBase","Read From DataBase ${resultList.size}")
        return resultList
    }

    override fun updateChore(choreModel: ChoreModel):Int {
        var db: SQLiteDatabase = writableDatabase

        var values: ContentValues = ContentValues()
        values.put(KEY_CHORE_NAME, choreModel.choreName)
        values.put(KEY_CHORE_ASSIGNED_BY, choreModel.assignedBy)
        values.put(KEY_CHORE_ASSIGNED_TO, choreModel.assignedTo)
        values.put(KEY_CHORE_ASSIGNED_TIME, System.currentTimeMillis())
        Log.d("Database","Update row $values")

        //update a row
        return db.update(TABLE_NAME, values, KEY_ID + "=?", arrayOf(choreModel.id.toString()))
    }



}