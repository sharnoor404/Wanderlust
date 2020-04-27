package com.example.wanderlust

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var namesArray=ArrayList<String>()
    //array to store names of location
    var locationArray=ArrayList<LatLng>()
    //array to store location

//step 1-menu added(to add new places so as to save them to our database)
    //prior to this generate a google maps API from the link provided in the Maps Activity

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater=getMenuInflater()
        menuInflater.inflate(R.menu.add_place,menu)



        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        //prior to this generate a google maps API from the link provided in the Maps Activity and check for fine location permission within manifest file
        //step 2-when Add Place option of menu is clicked the user is navigated to a new intent(Maps Activity)
        //!! sign represents that item value surely has some value
        
        if(item!!.itemId==R.id.add_place){
            val intent= Intent(applicationContext,MapsActivity::class.java)
            //the below line has been added to mark a difference between the activities that show up when a user adds a new location
            //and when the user opens up an old location from the listView
            intent.putExtra("info","new")
            startActivity(intent)

        }

        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //the below open up the existing database 'Places' and adds them to the list view arrays

        try{
            val database=openOrCreateDatabase("Places", Context.MODE_PRIVATE,null)
            val cursor=database.rawQuery("SELECT * FROM places",null)

            val nameIndex=cursor.getColumnIndex("name")
            val latitudeIndex=cursor.getColumnIndex("latitude")
            val longitudeIndex=cursor.getColumnIndex("longitude")

           cursor.moveToFirst()

            namesArray.clear()
            locationArray.clear()

            while(cursor!=null){
                val nameFromDatabase=cursor.getString(nameIndex)
                val latitudeFromDatabase=cursor.getString(latitudeIndex)
                val longitudeFromDatabase=cursor.getString(longitudeIndex)

                namesArray.add(nameFromDatabase)

                val latitudeCoordinate=latitudeFromDatabase.toDouble()
                val longitudeCoordinate=longitudeFromDatabase.toDouble()

                val location=LatLng(latitudeCoordinate,longitudeCoordinate)

                locationArray.add(location)
                cursor.moveToNext()

            }
        }catch(e:Exception){
            e.printStackTrace()
        }

        //connecting array to adapter and adapter to listView makes the saved locations visible to the user.
        val arrayAdapter=ArrayAdapter(this,android.R.layout.simple_list_item_1,namesArray)
        listView.adapter=arrayAdapter

            //code for actions to occur when clicking on list Item

        listView.setOnItemClickListener { parent, view, position, id ->
            val intent=Intent(applicationContext,MapsActivity::class.java)
            intent.putExtra("info","old")//since it has been taken up from database
            intent.putExtra("name",namesArray[position])
            intent.putExtra("latitude",locationArray[position].latitude)//since it has been taken up from database
            intent.putExtra("longitude",locationArray[position].longitude)
            startActivity(intent)
        }



    }
}
