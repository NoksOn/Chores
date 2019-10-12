package com.example.chores.data

import com.example.chores.model.ChoreModel

interface IDataBaseHandler{
    fun createChore(choreModel: ChoreModel)
    fun deleteChore(id:Int)
    fun readChores():MutableList<ChoreModel>
    fun updateChore(choreModel: ChoreModel):Int
}