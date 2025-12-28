package com.example.basicapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import com.example.basicapp.api.RetroFitInstance
import com.example.basicapp.ui.theme.BasicAppTheme
import com.example.basicapp.screen.*;
import com.example.basicapp.viewmodels.ListScreenViewModel

class MainActivity : ComponentActivity() {
    private val listScreenViewModel: ListScreenViewModel by viewModels<ListScreenViewModel>()
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
    //ListScreen()
}
