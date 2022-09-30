package com.khoshrang.forexquiz

import android.content.Context
import android.content.ContextWrapper
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.khoshrang.forexquiz.data.DataStoreManager
import com.khoshrang.forexquiz.databinding.ActivityMainBinding
import com.khoshrang.forexquiz.data.SharedViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.catch
import java.lang.Runnable
import java.util.*

lateinit var activityResultRegistry1: ActivityResultRegistry

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    private val model: SharedViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityResultRegistry1 = activityResultRegistry

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView
        val testsPics = model.testPictures.shuffled()
        model.testPictures = testsPics
        navController = findNavController(R.id.nav_host_fragment_activity_main)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )

        navController.addOnDestinationChangedListener { controller: NavController, destination: NavDestination, bundle: Bundle? ->
            navView.isVisible = appBarConfiguration.topLevelDestinations.contains(destination.id)
        }

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        val dataStoreManager = DataStoreManager(this)

        lifecycleScope.launch(
            Dispatchers.IO
        ) {
            dataStoreManager.getFromDataStore().catch { e ->
                e.printStackTrace()
            }.collect {
                withContext(Dispatchers.Main) {
                    model.premium.value = it
                }
            }
        }

    }

    // setting the locale to persian RTL or EN LTR
    override fun attachBaseContext(newBase: Context) {
//        super.attachBaseContext(ContextWrapper(newBase.setAppLocale("fa_IR")))
        super.attachBaseContext(ContextWrapper(newBase.setAppLocale("en")))
    }

    private fun Context.setAppLocale(language: String): Context {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val config = resources.configuration
        config.setLocale(locale)
        config.setLayoutDirection(locale)
        return createConfigurationContext(config)
    }

    override fun onSupportNavigateUp(): Boolean {
        val current = navController.currentDestination?.id
        if (current == R.id.testFragment) {
            onBackPressed()
            return false
        }
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    private var doubleBackToExitPressedOnce = false
    override fun onBackPressed() {
        val current = navController.currentDestination?.id
        var islast = false
        // check if the current navigation destination is one of the Main 3
        if (current == R.id.navigation_home || current == R.id.navigation_dashboard || current == R.id.navigation_notifications) {
            islast = true
        }

        if (!islast) {
            super.onBackPressed()
            return
        }

        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }

        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, getString(R.string.doubleTapToExit), Toast.LENGTH_SHORT).show()

        Handler(Looper.getMainLooper()).postDelayed(Runnable {
            doubleBackToExitPressedOnce = false
        }, 2000)
    }
}