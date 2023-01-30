package com.companyname.practicesm.profile

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.companyname.practicesm.routes.Routes
import com.companyname.practicesm.viewmodels.AddPostViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@Preview(showBackground = true)
@Composable
fun PreviewAddPostScreen() {
    AddPostScreen(pageChange = {})
}

@Composable
fun AddPostScreen(

    modifier: Modifier = Modifier,
    addPostViewModel: AddPostViewModel = viewModel(),
    pageChange: (String) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        val scope = rememberCoroutineScope()
        val context = LocalContext.current
        val title by remember {
            addPostViewModel.title
        }
        val description by remember {
            addPostViewModel.description
        }
        LaunchedEffect(key1 = addPostViewModel.errorMessage){
            addPostViewModel.errorMessage.collectLatest {
                if(it.isNotEmpty()){
                    Toast.makeText(context,it,Toast.LENGTH_SHORT).show()
                }
                addPostViewModel.resetErrorMessage()
            }
        }
        TextField(value = title, onValueChange = {addPostViewModel.title.value = it.trim()}, label = {Text(text = "Title")})
        TextField(value = description, onValueChange = {addPostViewModel.description.value = it.trim()}, label = {Text(text = "Description")})
        Button(onClick = {
            scope.launch {
                val result = addPostViewModel.addUserPost(title,description)
                if(result){
                    Toast.makeText(context,"Post not added",Toast.LENGTH_SHORT).show()
                }
                pageChange(Routes.Profile.route)
            }
        }) {
            Text(text = "Add", textAlign = TextAlign.Center)
        }

        Button(onClick = {
                pageChange(Routes.Profile.route)
        }) {
            Text(text = "Go Back", textAlign = TextAlign.Center)
        }
    }
}