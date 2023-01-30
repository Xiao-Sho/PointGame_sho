package com.android.example.pointgame

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.android.example.pointgame.databinding.ActivityMainBinding
import com.android.example.pointgame.databinding.NavHeaderMainBinding
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    var SUM= 0
    var pos= 0

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //sum,posの変数にファイルの数字を入れる
        //  DataLoad()
//      binding.textView2.text = pos.toString()


//        //変数の中身をレイアウトに反映
//        LayoutReflesh()

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

//        fun LayoutReflesh(){
//            findViewById<>(R.id.textView)=pos.toString()
//            findViewById<TextView>(R.id.textView).text=sum.toString()
//        }

//        fun DataSave(){
//            openFileOutput("sum",   MODE_PRIVATE).bufferedWriter().use{
//                it.write(SUM.toString())
//            }
//            openFileOutput("position",   MODE_PRIVATE).bufferedWriter().use{
//                it.write(pos.toString())
//            }
//        }
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

//                DataSave()

//                openFileOutput("memo", MODE_PRIVATE).bufferedWriter().use{
//                    it.write("enson")
//                }
          finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    }


//    fun DataSave(){
//
//        openFileOutput("SUM", MODE_PRIVATE).bufferedWriter().use{
//            it.write("mieno")
//        }
//        openFileOutput("position", MODE_PRIVATE).bufferedWriter().use{
//            it.write("xiao")
//        }
//    }

//    fun DataLoad(){
//
//        var data=""
//        openFileInput("sum").bufferedReader().forEachLine {
//            data+=it
//        }
//        SUM=data.toInt()
//
//        data=""
//        openFileInput("position").bufferedReader().forEachLine {
//            data+=it
//        }
//        pos=data.toInt()
//
//    }



    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}
