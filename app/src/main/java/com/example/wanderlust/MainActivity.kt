package com.example.wanderlust

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem

class MainActivity : AppCompatActivity() {

//step 1-menu added(to add new places so as to save them to our database)

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater=getMenuInflater()
        menuInflater.inflate(R.menu.add_place,menu)



        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        //step 2-when Add Place option of menu is clicked the user is navigated to a new intent(Maps Activity)
        //!! sign represents that item value surely has some value
        
        if(item!!.itemId==R.id.add_place){
            val intent= Intent(applicationContext,MapsActivity::class.java)
            startActivity(intent)

        }

        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
