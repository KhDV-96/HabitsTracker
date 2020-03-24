package com.khdv.habitstracker

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.khdv.habitstracker.model.Habit

class MainActivity : AppCompatActivity() {

    companion object {
        private const val HABIT_LIST_KEY = "HABIT_LIST"
    }

    lateinit var habits: ArrayList<Habit>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navController = findNavController(R.id.nav_host_fragment)
        NavigationUI.setupActionBarWithNavController(this, navController)

        habits = savedInstanceState?.getParcelableArrayList(HABIT_LIST_KEY) ?: ArrayList()
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(HABIT_LIST_KEY, habits)
    }
}
