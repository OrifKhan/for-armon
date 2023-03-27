package com.example.dbfordodo.db.dao.dbMain

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.dbfordodo.db.data.Combo
import islom.din.dodo_ilmhona_proskills.db.dao.PizzaDao
import islom.din.dodo_ilmhona_proskills.db.data.*
import islom.din.dodo_ilmhona_proskills.khq.dbMain.*
import islom.din.dodo_ilmhona_proskills.shodmon.khushbakht.StoryData


@Database(
    entities = [Pizza::class, Category::class, HalfPizza::class, Ingridients::class, Vkus::class,
         IngredientSize::class, Combo::class,StoryData::class, IngredientSizeConnection::class,
        IngredientsProductsConnection::class, Order::class, OrderConnection::class,
        OrderConnectionServer::class],
    version = 1
)

abstract class DodoDataBase: RoomDatabase() {
    abstract fun pizzaDao():PizzaDao

    abstract fun ingredientsDao() : IngredientsDao

    abstract fun ingredientProductsConnectionDao() : IngredientsProductsConnectionDao

    abstract fun productsDao() : ProductsDao

    abstract fun orderDao() : OrderDao

    companion object{
        @Volatile
        private var INSTANCE: DodoDataBase? =null

        fun getInstance(context: Context): DodoDataBase {
            return  INSTANCE ?:synchronized(this){
                val instance=Room.databaseBuilder(context.applicationContext,
                DodoDataBase::class.java,
                "dodo_base")
                    .fallbackToDestructiveMigrationFrom()
                    .build()
                INSTANCE =instance

                instance
            }

        }
    }
}

