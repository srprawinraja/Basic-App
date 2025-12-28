package com.example.basicapp.screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.basicapp.api.NetworkResponse
import com.example.basicapp.data.Users
import com.example.basicapp.ui.theme.customViolet
import com.example.basicapp.viewmodels.ListScreenViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import com.example.basicapp.R

private val TAG: String = "ListingScreen"
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(listScreenViewModel: ListScreenViewModel){



    val gridState = rememberLazyGridState()

    val uiData = listScreenViewModel.uiState.collectAsState().value
    LaunchedEffect(Unit) {
        if(uiData is NetworkResponse.Loading) listScreenViewModel.getAllUserDetails(25);
    }
    when(uiData){
        is NetworkResponse.Success ->{
            ListOfProfile(uiData.data, gridState, listScreenViewModel)
        }
        is NetworkResponse.Loading -> {
            CircularProgressIndicator()
        }
        is NetworkResponse.Error -> {
            Log.e(TAG, uiData.message)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListOfProfile(users: Users, gridState: LazyGridState, listScreenViewModel: ListScreenViewModel){
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = customViolet,
                    titleContentColor = customViolet,
                ),
                title = {
                    Text("Listing Screen", color = Color.White)
                }
            )
        },
    ) { paddingValues ->
        LazyVerticalGrid(
            modifier = Modifier.padding(paddingValues).fillMaxSize().background(Color.White),
            columns = GridCells.Fixed(2),
            state = gridState
        ) {
            items (
                users.results.size
            ){ index->
                AsyncImage(
                    model = users.results[index].picture.large,
                    placeholder = painterResource(R.drawable.error_icon),
                    contentDescription = "Quote Image",
                    modifier = Modifier.height(200.dp).width(200.dp)
                )
            }
        }
    }
    LaunchedEffect(gridState) {
        snapshotFlow { gridState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .collect { lastVisibleIndex ->
                if (lastVisibleIndex != null && lastVisibleIndex >= users.results.size - 1) {
                    listScreenViewModel.getAllUserDetails(25)
                }
            }
    }
}
