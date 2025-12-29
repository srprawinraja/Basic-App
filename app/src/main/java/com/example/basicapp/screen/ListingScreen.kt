package com.example.basicapp.screen

import android.util.Log
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavHostController
import com.example.basicapp.R
import com.example.basicapp.db.userdetail.UserDetailEntity

private val TAG: String = "ListingScreen"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(navController: NavHostController, listScreenViewModel: ListScreenViewModel) {

    val gridState = rememberLazyStaggeredGridState()

    val uiData = listScreenViewModel.uiState.collectAsState().value
    LaunchedEffect(Unit) {
        if (uiData is NetworkResponse.Loading) listScreenViewModel.getAllUserDetails();
    }
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorResource(R.color.violet),
                    titleContentColor = colorResource(R.color.violet),
                ),
                title = {
                    Text("Listing Screen", color = Color.White)
                }
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .background(Color.White)
                .fillMaxSize()
                .padding(paddingValues = paddingValues)
                .padding(25.dp)
        ) {
            when (uiData) {
                is NetworkResponse.Success -> {
                    ListOfProfile(
                        navController,
                        uiData.data,
                        gridState,
                        listScreenViewModel,
                    )
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
                    Log.e(TAG, uiData.message)
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

    val searchState = rememberSearchBarState()

    LaunchedEffect(searchState.currentValue) {
        // This runs every time the text changes
        val query = searchState.currentValue
        println("Search query: $query")
    }
    val textFieldState = rememberTextFieldState()
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
            users.size
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
