package com.example.dbfordodo

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.dbfordodo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    //Binding this layout
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val vm : ViewModelProvider.Factory

        //Navigation view getting from layout
        val bottomNavView = binding.bottomNavView

        // Getting my Nav Host and setting to it a Nav Controller
        val navFragment =
            supportFragmentManager.findFragmentById(binding.fragmentsContainer.id) as NavHostFragment
        val navController = navFragment.navController

        Log.d("example_tag", "Step 3")
        //Implementing navigation beetween fragments clicking Bottom NavView
        bottomNavView.setupWithNavController(navController)
        bottomNavView.itemIconTintList = null

    }

}