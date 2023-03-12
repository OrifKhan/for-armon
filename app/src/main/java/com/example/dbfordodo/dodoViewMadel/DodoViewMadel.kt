package com.example.dbfordodo.dodoViewMadel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dbfordodo.db.dao.dbMain.DodoDataBase
import com.example.dbfordodo.dodoViewMadel.repository.*
import islom.din.dodo_ilmhona_proskills.db.dao.PizzaDao
import islom.din.dodo_ilmhona_proskills.db.data.Category
import islom.din.dodo_ilmhona_proskills.db.data.Ingridients
import islom.din.dodo_ilmhona_proskills.db.data.Pizza
import islom.din.dodo_ilmhona_proskills.db.data.Vkus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class DodoViewMadel(app:Application,pizzaDao: PizzaDao): AndroidViewModel(app) {
    val db = DodoDataBase.getInstance(app).pizzaDao()

    fun insertViewMadel() {
        viewModelScope.launch(Dispatchers.IO) {

            Log.d("hello", "errrro")
             GetVkusList().getList().forEach{
                db.insertVkus(it)
            }
            GetPizzaList().getList().forEach(){
               db.insertPizza(it)
            }
            GetCategoryList().getCategory().forEach(){
                db.insertCotegory(it)
            }

            GetIngridientList().getList().forEach(){
                db.insertIngredient(it)
            }
            GetHalfList().getList().forEach {
                db.insertHalfPizza(it)
            }
        }
    }


    fun getPizza():LiveData< List<Pizza>> {
        return db.getAllPizza()
    }

    fun getVkus(): LiveData<List<Vkus>> {
        return db.getAllVkus()
    }

    fun getIngridient(): LiveData<List<Ingridients>> {
        return db.getIngredient()
    }

    fun getAllCotegory(): LiveData<List<Category>> {
        return db.getCotegory()
    }
}