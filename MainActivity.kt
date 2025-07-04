package com.wahab.myapp

import android.os.Bundle
import android.util.Log
import android.view.ViewTreeObserver
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)

        if (drawerLayout == null) {
            Log.e("MainActivity", "drawerLayout is null!")
        }

        val navView: NavigationView = findViewById(R.id.nav_view)
        //Log statement for navView
        if (navView == null) {
            Log.e("MainActivity", "navView is null!")
        }

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        //Log statement for toolbar
        if (toolbar == null){
            Log.e("MainActivity", "toolbar is null!")
        } else {
            setSupportActionBar(toolbar)
        }

        //Setting up the app bar configuration
        appBarConfiguration = AppBarConfiguration.Builder(
            R.id.dashboard, R.id.profile, R.id.itemFragment
        )
            .setOpenableLayout(drawerLayout)
            .build()


        val mainLayout = findViewById<ConstraintLayout>(R.id.mainLayout)
        mainLayout.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                // Remove the listener to avoid multiple calls
                mainLayout.viewTreeObserver.removeOnGlobalLayoutListener(this)

                val navController = findNavController(R.id.nav_host_fragment)
                //Log statement for navController
                if (navController == null) {
                    Log.e("MainActivity", "navController is null!")
                } else{
                    //Setup action bar with navController
                    setupActionBarWithNavController(navController, appBarConfiguration)
                    //Setup navView with navController
                    navView.setupWithNavController(navController)
                }
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp()
    }
}