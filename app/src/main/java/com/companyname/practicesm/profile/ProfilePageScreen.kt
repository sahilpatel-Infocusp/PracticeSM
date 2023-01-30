package com.companyname.practicesm.profile

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.companyname.practicesm.routes.Routes
import com.companyname.practicesm.viewmodels.ProfilePageViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Preview(showBackground = true)
@Composable
fun PreviewProfilePageScreen() {
    ProfilePageScreen(pageChange = {})
}

@Composable
fun ProfilePageScreen(
    modifier: Modifier = Modifier,
    profilePageViewModel: ProfilePageViewModel = viewModel(),
    pageChange: (String) -> Unit
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val screenLoading by remember {
        profilePageViewModel.screenLoading
    }

    val postsLoading by remember {
        profilePageViewModel.postsLoading
    }
    if (screenLoading) {
        CircularProgressIndicator()
    }
    if (postsLoading) {
        CircularProgressIndicator()
    }
    LaunchedEffect(key1 = profilePageViewModel.errorMessage) {
        profilePageViewModel.errorMessage.collectLatest {
            if (it.isNotEmpty()) {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }
            profilePageViewModel.errorMessage.value = ""
        }
    }
    val userData by remember{
        profilePageViewModel.userData
    }
    val postList = remember {
        profilePageViewModel.postList
    }


    Column(
        verticalArrangement = Arrangement.Top,
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .fillMaxWidth()
                .padding(
                    16.dp
                )
                .weight(0.2f)
        ) {
            Icon(
                Icons.Default.Person,
                contentDescription = "Profile Photo",
                modifier = modifier.weight(0.2f)
            )
            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = modifier
                    .padding(8.dp)
                    .weight(0.8f)
            ) {
                Row {
                    Text(text = "Username: ")
                    Text(text = "${userData?.username}", fontWeight = FontWeight.Bold)
                }
                Row {
                    Text(text = "Name: ")
                    Text(text = "${userData?.name}", fontWeight = FontWeight.Bold)
                }
                Row {
                    Text(text = "Posts: ")
                    Text(text = "${userData?.postCount}", fontWeight = FontWeight.Bold)
                }
            }
        }

        Divider(
            color = Color.Black,
            thickness = 4.dp
        )

        LazyColumn(
            verticalArrangement = Arrangement.Top,
            contentPadding = PaddingValues(8.dp),
            modifier = modifier
                .fillMaxWidth()
                .weight(0.7f)
        ) {
            items(postList.toList()) {
                it.second.apply {
                    PostCard(title = this.title, desc = this.description)
                }
            }
        }
        Divider(
            color = Color.Black,
            thickness = 4.dp
        )
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .weight(0.1f)
                .fillMaxWidth()
                .padding(horizontal = 36.dp)
        ) {
            Button(
                onClick = {
                    scope.launch {
                        profilePageViewModel.signOutUser()
                    }
                    pageChange(Routes.SignIn.route)
                },
                contentPadding = PaddingValues(8.dp)
            ) {
                Text(text = "Sign Out", textAlign = TextAlign.Center)
            }

            Button(
                contentPadding = PaddingValues(8.dp),
                onClick = { pageChange(Routes.AddPost.route) }
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Add Post")
                    Text(text = "Add Post")
                }
            }
        }

    }
}


@Composable
fun PostCard(title: String, desc: String) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(8.dp)
            .background(Color.White)
    ) {
        Text(text = title, color = Color.Black)
        Divider(thickness = 1.dp, color = Color.Black)
        Text(text = desc, color = Color.Black)

    }
}

