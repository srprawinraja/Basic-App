package com.example.basicapp

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSearchBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.basicapp.db.userdetail.UserDetailRepository
import com.example.basicapp.factory.UserServiceFactory
import com.example.basicapp.ui.theme.BasicAppTheme
import com.example.basicapp.screen.*;
import com.example.basicapp.viewmodels.DetailScreenViewModel
import com.example.basicapp.viewmodels.ListScreenViewModel

class MainActivity : ComponentActivity() {
    private val userServiceFactory = UserServiceFactory(this)
    private val listScreenViewModel: ListScreenViewModel by viewModels<ListScreenViewModel>() {
        userServiceFactory
    }
    private val detailScreenViewModel: DetailScreenViewModel by viewModels<DetailScreenViewModel>() {
        userServiceFactory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BasicAppTheme {
                AppNavigation(listScreenViewModel, detailScreenViewModel)
            }
        }
    }
}
@Composable
fun AppNavigation(
    listScreenViewModel: ListScreenViewModel,
    detailScreenViewModel: DetailScreenViewModel
){
    val navController = rememberNavController()
    NavHost(navController, startDestination = "list" ){
        composable("list"){
            ListScreen(navController, listScreenViewModel)
        }
        composable(
            route= "detail/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ){
            backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id")
            if(id!=null){
                DetailScreen(detailScreenViewModel, id)
            }
        }
    }
}

@SuppressLint("ViewModelConstructorInComposable")
@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun Test() {
    ListScreen(
        navController = NavHostController(LocalContext.current),     ListScreenViewModel(UserDetailRepository(LocalContext.current))
    )
}
