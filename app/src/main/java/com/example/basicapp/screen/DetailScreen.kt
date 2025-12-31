package com.example.basicapp.screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.basicapp.R
import com.example.basicapp.api.NetworkResponse
import com.example.basicapp.components.CustomScaffoldComponent
import com.example.basicapp.db.userdetail.UserDetailEntity

import com.example.basicapp.viewmodels.DetailScreenViewModel

private val TAG: String = "DetailScreen"
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    detailScreenViewModel: DetailScreenViewModel,
    id: Int
){
    val uiData = detailScreenViewModel.uiState.collectAsState().value
    LaunchedEffect(Unit) {
        detailScreenViewModel.getUserDetail(id)
    }
    CustomScaffoldComponent("Detail Screen") { paddingValues ->
        when(uiData){
            is NetworkResponse.Success -> {
                Details(uiData.data, paddingValues)
            }
            is NetworkResponse.Loading -> {
                Column (
                    modifier = Modifier.padding(paddingValues).fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    CircularProgressIndicator()
                }
            }
            is NetworkResponse.Error -> {
                Log.d(TAG, uiData.message)
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Details(uiData: UserDetailEntity, paddingValues: PaddingValues) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White).padding(paddingValues),
        ) {
            Spacer(modifier = Modifier.height(50.dp))
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = uiData.profilePic,
                    placeholder = painterResource(R.drawable.error_icon),
                    contentDescription = "Quote Image",
                    modifier = Modifier
                        .size(250.dp)
                        .clip(CircleShape)

                )
            }
            Column (
                modifier = Modifier.fillMaxWidth().padding(20.dp)
            ){
                Text(text = stringResource(R.string.full_name), modifier = Modifier.padding(bottom = 5.dp), fontSize = 15.sp)
                Text(text = uiData.fullName, fontSize = 20.sp, color =  colorResource(R.color.grey),)
                Spacer(modifier = Modifier.height(20.dp))

                Text(text = stringResource(R.string.gender), fontSize = 15.sp)
                Text(text = uiData.gender, fontSize = 20.sp, color = colorResource(R.color.grey),)
                Spacer(modifier = Modifier.height(20.dp))

                Text(text = stringResource(R.string.age), fontSize = 15.sp)
                Text(text = uiData.age, fontSize = 20.sp, color = colorResource(R.color.grey),)
                Spacer(modifier = Modifier.height(10.dp))

                Text(text = stringResource(R.string.email), fontSize = 15.sp)
                Text(text = uiData.email, fontSize = 20.sp, color = colorResource(R.color.grey),)
                Spacer(modifier = Modifier.height(10.dp))

                Text(text = stringResource(R.string.phone_number), fontSize = 15.sp)
                Text(uiData.ph, fontSize = 20.sp,color = colorResource(R.color.grey),)
            }
        }

}
