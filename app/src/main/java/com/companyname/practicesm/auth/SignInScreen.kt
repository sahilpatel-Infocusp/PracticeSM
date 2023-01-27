package com.companyname.practicesm.auth

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.companyname.practicesm.routes.Routes
import com.companyname.practicesm.viewmodels.SignInViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Preview(showBackground = true)
@Composable
fun showSignInScreen(){
    SignInScreen(pageChange = {})
}
@Composable
fun SignInScreen(
    modifier: Modifier = Modifier,
    signInViewModel: SignInViewModel = viewModel(),
    pageChange: (String) -> Unit
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val email = rememberSaveable {
        mutableStateOf("")
    }
    val password = rememberSaveable {
        mutableStateOf("")
    }
    val isLoading = rememberSaveable {
        signInViewModel.isLoading.value
    }

    LaunchedEffect(key1 = signInViewModel.errorMessage){
        signInViewModel.errorMessage.collectLatest {
            if(it.isNotEmpty()){
                Toast.makeText(context,it,Toast.LENGTH_SHORT).show()
            }
        }
        signInViewModel.resetErrorMessage()
    }
    if(isLoading){
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = modifier.fillMaxSize()
        ) {
            CircularProgressIndicator()
        }
    }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .padding(8.dp)
            .fillMaxSize()
    ) {
        TextField(
            value = email.value,
            onValueChange = { email.value = it.trim() },
            label = { Text(text = "Email", textAlign = TextAlign.Center) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
            )
        )
        TextField(
            value = password.value,
            onValueChange = { password.value = it.trim() },
            label = { Text(text = "Password", textAlign = TextAlign.Center) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            visualTransformation = PasswordVisualTransformation()
        )
        Button(onClick = {
            scope.launch {
                val user = signInViewModel.signInUser(email.value, password.value)
                if(user!=null){
                    pageChange(Routes.Feed.route)
                }
            }
        }) {
            Text(text = "Sign In", textAlign = TextAlign.Center)
        }
        Spacer(modifier = modifier.size(10.dp))
        Row() {
            Text(text = "New User? Click here to ")
            Text(text = "Sign Up", color = Color.Blue, modifier = modifier.clickable {
                pageChange(Routes.SignUp.route)
            })
        }

    }
}









