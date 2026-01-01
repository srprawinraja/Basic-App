package com.example.basicapp.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.basicapp.R
import com.example.basicapp.api.NetworkResponse
import com.example.basicapp.data.weather.Weather
import com.example.basicapp.screen.ListOfProfile
import kotlin.math.min

private val TAG: String = "CustomScaffoldComponent"
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomScaffoldComponent(title: String, weatherData: NetworkResponse<Weather>, contentBody: @Composable (PaddingValues) -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorResource(R.color.violet),
                    titleContentColor =  colorResource(R.color.violet)
                ),
                title = {
                    Row (
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Text(title, color = Color.White)
                        when (weatherData) {
                            is NetworkResponse.Success -> {
                                Row (
                                    modifier = Modifier.fillMaxHeight(),
                                    verticalAlignment = Alignment.CenterVertically
                                ){
                                    Row (
                                        modifier = Modifier.fillMaxHeight(),
                                        verticalAlignment = Alignment.CenterVertically
                                    ){
                                        Text(weatherData.data.main.temp.toString(), color = Color.White, fontSize = 17.sp)
                                        Spacer(modifier = Modifier.width(3.dp))
                                        Text(
                                            "o",
                                            color = Color.White,
                                            fontSize = 10.sp,
                                            modifier = Modifier
                                                .alignByBaseline()
                                                .wrapContentSize().padding(top = 5.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column (
                                        modifier = Modifier
                                            .fillMaxHeight()
                                            .padding(top = 13.dp),
                                        verticalArrangement = Arrangement.Center
                                    ){
                                        var name = "unknown location"
                                        weatherData.data.name.isEmpty().let {
                                            if(!it){
                                                name = weatherData.data.name
                                            }
                                        }
                                        Text(name.substring(0, min(name.length, 16)), color = Color.White, fontSize = 17.sp, modifier = Modifier.wrapContentSize())
                                        Text(weatherData.data.weather[0].description, color = Color.White, fontSize = 10.sp,  modifier = Modifier
                                            .wrapContentSize()
                                            .offset(y = -10.dp))
                                    }
                                    AsyncImage(
                                        model = "http://openweathermap.org/img/wn/${weatherData.data.weather[0].icon}@2x.png",
                                        placeholder = painterResource(R.drawable.error_icon),
                                        modifier = Modifier.wrapContentSize(),
                                        contentDescription = "Weather Icon",
                                        onError = { errorState ->
                                            Log.e(TAG, "Image loading failed", errorState.result.throwable)
                                        }
                                    )
                                    Spacer(modifier = Modifier.padding(10.dp))
                                }
                            }
                            is NetworkResponse.Loading -> {
                                Column(
                                    modifier = Modifier.fillMaxSize(),
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    CircularProgressIndicator()
                                }
                            }

                            is NetworkResponse.Error -> {
                                Log.e(TAG, weatherData.message)
                            }

                            NetworkResponse.Empty -> {
                            }
                        }
                    }
                }
            )
        },
    ) { paddingValues ->
        contentBody(paddingValues)
    }
}