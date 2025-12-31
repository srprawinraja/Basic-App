package com.example.basicapp.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import com.example.basicapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomScaffoldComponent(title: String, contentBody: @Composable (PaddingValues) -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorResource(R.color.violet),
                    titleContentColor =  colorResource(R.color.violet)
                ),
                title = {
                    Text(title, color = Color.White)
                }
            )
        },
    ) { paddingValues ->
        contentBody(paddingValues)
    }
}