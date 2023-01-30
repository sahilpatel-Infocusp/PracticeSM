package com.companyname.practicesm.feed

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.companyname.practicesm.profile.PostCard
import com.companyname.practicesm.viewmodels.SearchViewModel
import kotlinx.coroutines.launch

@Preview(showBackground = true)
@Composable
fun PreviewSearchScreen() {
    SearchScreen(pageChage = {})
}

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    searchViewModel: SearchViewModel = viewModel(),
    pageChage: (String) -> Unit
) {
    val scope = rememberCoroutineScope()
    val searchText = remember {
        searchViewModel.searchText
    }
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(8.dp)
    ) {

        //SearchRow
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            TextField(
                leadingIcon = {
                  Icon(Icons.Default.Search, contentDescription = "Search")
                },
                value = searchText.value,
                onValueChange = { searchViewModel.searchText.value = it},
                label = {
                    Text(text = "Search Post Here")
                },
                singleLine = true
            )
            Button(
                onClick = {
                    scope.launch {
                        searchViewModel.getMatchedUserPost(searchText.value)
                    }
                },
                modifier = modifier
                    .padding(horizontal = 8.dp)
                    .fillMaxWidth()

            ) {
                Text(text = "Search", textAlign = TextAlign.Center, fontSize = 12.sp)
            }

        }

        Divider(
            color = Color.Black,
            thickness = 4.dp
        )

        if(searchViewModel.matchedPost.value.containsKey(0)){
            Text(text = "No Post Found")
        }
        else {
            LazyColumn(
                verticalArrangement = Arrangement.Top,
                contentPadding = PaddingValues(8.dp),
                modifier = modifier.fillMaxWidth()
            ) {

                items(searchViewModel.matchedPost.value.toList()) {
                    it.second.apply {
                        PostCard(title = this!!.title, desc = this.description)
                    }
                }
            }

        }







    }
}





















