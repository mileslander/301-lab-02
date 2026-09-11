package com.example.listcity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.listcity.ui.theme.ListCityTheme
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.mutableStateOf
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val cityRepository = CityRepository()

        setContent {
            ListCityTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    CityListScreen(
                        cities = cityRepository.cities,
                        onAddCity = { cityRepository.addCity(it) },
                        onRemoveCity = { cityRepository.removeCityInt(cityIndex = it) },
                        modifier = Modifier.padding(innerPadding),
                    )
                }
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview() {
        ListCityTheme {
            Greeting("Android")
        }
    }

    @Composable
    fun CityListScreen(
        cities: List<String>,
        onAddCity: (String) -> Unit,
        onRemoveCity: (Int) -> Unit,
        modifier: Modifier = Modifier
    ) {
        var newCityName by remember {mutableStateOf("") }

        var selectedIndex by remember { mutableIntStateOf(-1) }

        var isRowVisible by remember { mutableStateOf(false) }

        Column(modifier = modifier.fillMaxSize()) {
            Column(modifier = modifier.fillMaxWidth()) {
                Row(modifier = modifier.padding(all = 16.dp)) {

                    Button(
                        onClick = {
                            isRowVisible = !isRowVisible
                        }
                    ) {
                        Text("Add City")
                    }

                    Spacer(modifier = Modifier.width(8.dp))   

                    Button(
                        onClick = {
                            if (cities.count() - 1 >= selectedIndex )  {
                                 onRemoveCity(selectedIndex)
                            }
                        }
                    ) {
                        Text("Remove City")
                    }
                }
            }

            LazyColumn(modifier = Modifier.weight(1f)) {
                itemsIndexed(cities) { index, city ->
                    CityRow(city = city, modifier =
                        Modifier.fillMaxWidth().selectable(selected = selectedIndex == index,
                        onClick  = { selectedIndex = index
                         }).background(
                            if (selectedIndex == index){
                                Color.Gray
                            }
                            else {
                                Color.Transparent
                            }
                        )
                    )
                }
            }

            if (isRowVisible) {
                Row(modifier = modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = newCityName,
                        onValueChange = { newCityName = it },
                        label = { Text("City Name") },
                        modifier = Modifier.weight(1f)
                    )

                    Button(
                        onClick ={
                            if (newCityName.isNotBlank()) {
                                onAddCity(newCityName)
                                newCityName = ""
                                isRowVisible = !isRowVisible
                            }                               
                        }
                    ){
                        Text("Confirm")
                    }
                }
            }
        }
    }

    @Composable
    fun CityRow(city: String, modifier: Modifier = Modifier) {
        Text(
            text = city,
            fontSize = 28.sp,
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 14.dp)
        )
    }


    @Composable
    fun Greeting(name: String, modifier: Modifier = Modifier) {
        Text(
            text = "Hello $name!",
            modifier = modifier
        )
    }

    class CityRepository {
        private val _cities = mutableStateListOf(
            "Edmonton", "Vancouver", "Moscow",
            "Sydney", "Berlin", "Vienna",
            "Tokyo", "Beijing", "Osaka",
            "New Delhi"
        )

        val cities: List<String>
            get() = _cities

        fun addCity(city: String) {
            _cities.add(city)
        }

        fun removeCityInt(cityIndex: Int){
            _cities.removeAt(index = cityIndex)
        }

        fun removeCityStr(city: String){
            if (_cities.contains(city)){
                _cities.remove(city)
            }
        }
    }
}