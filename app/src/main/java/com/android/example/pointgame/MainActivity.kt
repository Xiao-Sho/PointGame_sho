package com.android.example.pointgame

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.menu.MenuView
import androidx.core.view.get
import com.android.example.pointgame.databinding.ActivityMainBinding
import com.android.example.pointgame.databinding.NavHeaderMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // バインディング
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // アクションバー
        setSupportActionBar(binding.appBarMain.toolbar)

        // ドロワー
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home_pager, R.id.nav_list_coupon, R.id.nav_get_coupon
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        // ユーザー情報取得
        val name = intent.getStringExtra("NAME")
        val email = intent.getStringExtra("EMAIL")

        // navView is NavigationView
        val viewHeader = binding.navView.getHeaderView(0)
        // nav_header_main.xml is headerLayout
        val navViewHeaderBinding: NavHeaderMainBinding = NavHeaderMainBinding.bind(viewHeader)
        // name and email are children of nav_header_main
        navViewHeaderBinding.navHeaderName.text = name
        navViewHeaderBinding.navHeaderEmail.text = email
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.action_logout -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}
