package com.example.basicapp.screen

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberSearchBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.basicapp.api.NetworkResponse
import com.example.basicapp.viewmodels.ListScreenViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavHostController
import com.example.basicapp.R
import com.example.basicapp.components.CustomScaffoldComponent
import com.example.basicapp.db.userdetail.UserDetailEntity
import com.google.android.gms.location.LocationServices
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.example.basicapp.components.AlertBoxComponent
import com.google.android.gms.location.Priority

private val TAG: String = "ListingScreen"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(navController: NavHostController, listScreenViewModel: ListScreenViewModel) {

    val gridState = rememberLazyStaggeredGridState()
    val context = LocalContext.current
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    val userUiData = listScreenViewModel.userUiState.collectAsState().value
    val weatherUiData = listScreenViewModel.weatherUiState.collectAsState().value
    val showLocationPermissionAlertUi = remember { mutableStateOf(false) }
    val showLocationAlertUi = remember { mutableStateOf(false) }
    val lifecycle = LocalLifecycleOwner.current.lifecycle


    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            if (granted) {
                fusedLocationClient.getCurrentLocation(
                    Priority.PRIORITY_HIGH_ACCURACY,
                    null
                ).addOnSuccessListener { location ->
                        location?.let {
                            listScreenViewModel.getWeatherDetail(
                                location.latitude,
                                location.longitude
                            )
                        }
                    }
            } else {
                showLocationPermissionAlertUi.value = true
            }
        }
    )
    LaunchedEffect(lifecycle) {
        lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                Log.i(TAG, "Permission available")
                fusedLocationClient.getCurrentLocation(
                    Priority.PRIORITY_HIGH_ACCURACY,
                    null
                ).addOnSuccessListener { location ->
                        if (location == null) {
                            if (!listScreenViewModel.isLocationEnabled(context)) {
                                Log.i(TAG, "location ain't turned on")
                                showLocationAlertUi.value = true
                            }
                        } else {
                            listScreenViewModel.getWeatherDetail(
                                location.latitude,
                                location.longitude
                            )
                            Log.d(
                                TAG,
                                "already given " + location.longitude.toString() + " " + location.latitude.toString() + " " + location.time
                            )
                        }
                    }
            } else {
                if(!showLocationPermissionAlertUi.value)
                    permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
    }
    LaunchedEffect(false) {
        listScreenViewModel.getAllUserDetails()
    }


    CustomScaffoldComponent(
        "Listing Screen",
        weatherData = weatherUiData
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .background(Color.White)
                .fillMaxSize()
                .padding(paddingValues = paddingValues)
                .padding(25.dp)
        ) {

            when (userUiData) {
                is NetworkResponse.Success -> {
                    ListOfProfile(
                        navController,
                        userUiData.data,
                        gridState,
                        listScreenViewModel,
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(10.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                        }
                    }


                }

                is NetworkResponse.Loading -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is NetworkResponse.Error -> {
                    Log.e(TAG, userUiData.message)
                }

                NetworkResponse.Empty -> {

                }
            }
            if (showLocationPermissionAlertUi.value) {
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(10.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AlertBoxComponent(message = "Provide location permission so we can provide location-based features.") {
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                            val uri: Uri = Uri.fromParts("package", context.packageName, null)
                            data = uri
                        }
                        context.startActivity(intent)
                        showLocationPermissionAlertUi.value = false
                    }
                }
            } else if (showLocationAlertUi.value) {
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(10.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AlertBoxComponent(message = "Turn on  location so we can provide location-based features.") {
                        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                        context.startActivity(intent)
                        showLocationAlertUi.value = false
                    }
                }
            }
        }
    }


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListOfProfile(
    navController: NavHostController,
    users: List<UserDetailEntity>,
    gridState: LazyStaggeredGridState,
    listScreenViewModel: ListScreenViewModel,
) {


    val textFieldState = rememberTextFieldState()
    Column (
        modifier = Modifier.fillMaxSize()
    ){
        Spacer(modifier = Modifier.height(20.dp))

        SimpleSearchBar(
            textFieldState,
            listScreenViewModel
        )
        Spacer(modifier = Modifier.height(20.dp))


        LazyVerticalStaggeredGrid(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            state = gridState,
            columns = StaggeredGridCells.Fixed(2),
            verticalItemSpacing = 8.dp,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(
               count =  users.size
            ) { index ->



                val row = index / 2
                val isSquare =
                    (row % 2 == 0 && index % 2 == 0) ||
                            (row % 2 == 1 && index % 2 == 0)
                Column {
                    AsyncImage(
                        model = users[index].profilePic,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(
                                if (isSquare) 1f else 4f / 2f
                            )
                            .clickable {
                                navController.navigate("detail/${users[index].id}")
                            }
                    )
                    Text(
                        users[index].fullName,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.Black),
                        textAlign = TextAlign.Center,
                        color = Color.White
                    )
                }

            }
        }
    }
    LaunchedEffect(gridState, users) {
        snapshotFlow { gridState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .collect { lastVisibleIndex ->
                if (textFieldState.text.isEmpty() && lastVisibleIndex != null && lastVisibleIndex >= users.size - 5) {
                    listScreenViewModel.getAllUserDetails()
                }
            }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleSearchBar(
    textFieldState: TextFieldState,
    listScreenViewModel: ListScreenViewModel
) {

    TextField(
        value = textFieldState.text.toString(),
        onValueChange = {
            textFieldState.edit { replace(0, length, it) }
            listScreenViewModel.searchByName(textFieldState.text.toString())
        },
        modifier = Modifier
            .fillMaxWidth()
            .background(
                colorResource(R.color.light_grey), shape = RoundedCornerShape(16.dp),
            ),
        shape = RoundedCornerShape(16.dp),

        placeholder = { Text("Search...", color = colorResource(R.color.violet)) },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = colorResource(R.color.light_grey),
            unfocusedContainerColor = colorResource(R.color.light_grey),
            cursorColor = colorResource(R.color.violet),
            focusedTextColor = colorResource(R.color.violet),
            unfocusedTextColor = colorResource(R.color.violet),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        singleLine = true,
        leadingIcon = {
            Icon(
                painter = painterResource(R.drawable.search_icon),
                contentDescription = "search icon",
                tint = colorResource(R.color.violet),
                modifier = Modifier.size(30.dp)
            )
        }
    )

}
