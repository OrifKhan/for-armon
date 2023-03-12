    package islom.din.dodo_ilmhona_proskills.db.dao

    import androidx.lifecycle.LiveData
    import androidx.room.Dao
    import androidx.room.Insert
    import androidx.room.OnConflictStrategy
    import androidx.room.Query
    import islom.din.dodo_ilmhona_proskills.db.data.*

    @Dao
    interface PizzaDao {
        @Insert(onConflict = OnConflictStrategy.IGNORE)
        suspend fun insertPizza(pizza: Pizza)

        @Query("SELECT * FROM pizzaAll")
        fun getAllPizza():LiveData<List<Pizza>>

        //cotegory dao
        @Insert(onConflict = OnConflictStrategy.IGNORE)
        suspend fun insertCotegory(category: Category)

        @Query("SELECT * FROM category")
        fun getCotegory(): LiveData<List<Category>>

        //Ingredient
        @Insert(onConflict = OnConflictStrategy.IGNORE)
        suspend fun insertIngredient(ingridients: Ingridients)

        @Query("SELECT * FROM ingredient")
        fun getIngredient(): LiveData<List<Ingridients>>

        //half pizza dao
        @Insert(onConflict = OnConflictStrategy.IGNORE)
        suspend fun insertHalfPizza(halfPizza: HalfPizza)

        @Query("SELECT * FROM half_pizza")
        fun gelAllHalfPizza(): LiveData<List<HalfPizza>>

        // vkus Pizza dao
        @Insert(onConflict = OnConflictStrategy.IGNORE)
        suspend fun insertVkus(vkus: Vkus)

        @Query("SELECT * FROM vkus")
        fun getAllVkus(): LiveData<List<Vkus>>


       /* @Query("SELECT * FROM category WHERE category= :category")
        fun searchCategory(category: String ):LiveData<List<String>>
   */ }