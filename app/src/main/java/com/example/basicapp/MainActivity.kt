package com.example.basicapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import coil.compose.AsyncImage
import com.example.basicapp.api.RetroFitInstance
import com.example.basicapp.factory.UserServiceFactory
import com.example.basicapp.ui.theme.BasicAppTheme
import com.example.basicapp.screen.*;
import com.example.basicapp.viewmodels.ListScreenViewModel

class MainActivity : ComponentActivity() {
    private val userServiceFactory = UserServiceFactory(this)
    private val listScreenViewModel: ListScreenViewModel by viewModels<ListScreenViewModel>(){
        userServiceFactory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BasicAppTheme {
                ListScreen(listScreenViewModel)
            }
        }
    }
}
@Preview
@Composable
fun Test(){
    Column (
        modifier = Modifier.fillMaxSize()
    ){
        AsyncImage(
            model = "https://randomuser.me/api/portraits/women/6.jpg",
            placeholder = painterResource(R.drawable.error_icon),
            contentDescription = "Quote Image",
            modifier = Modifier.size(25.dp).clip(CircleShape)

        )
        Text(text = stringResource(R.string.full_name))
        Text(text = stringResource(R.string.gender))
        Text(text = stringResource(R.string.age))
        Text(text = stringResource(R.string.email))
        Text(text = stringResource(R.string.phone_number))



    }
}
