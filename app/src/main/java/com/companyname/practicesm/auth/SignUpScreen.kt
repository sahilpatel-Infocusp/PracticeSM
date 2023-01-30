package com.companyname.practicesm.auth

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.companyname.practicesm.routes.Routes
import com.companyname.practicesm.viewmodels.SignUpViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
@Preview(showBackground = true)
fun SignUpScreenPreview(){
    SignUpScreen(pageChange = {})
}

@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier,
    signUpViewModel: SignUpViewModel = viewModel(),
    pageChange: (String) -> Unit
) {

    val scope = rememberCoroutineScope()
    val context = LocalContext.current



    val isLoading by remember {
        signUpViewModel.isLoading
    }
    val name by remember {
        signUpViewModel.name
    }
    val username by remember {
        signUpViewModel.username
    }
    val email by remember {
        signUpViewModel.email
    }
    val password by remember {
        signUpViewModel.password
    }

    LaunchedEffect(key1 = signUpViewModel.errorMessage) {
        signUpViewModel.errorMessage.collectLatest {
            if (it.isNotEmpty()) {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }
            signUpViewModel.resetErrorMessage()

        }
    }
    if (isLoading) {
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
            value = name,
            onValueChange = { signUpViewModel.name.value = it.trim() },
            label = { Text(text = "Name", textAlign = TextAlign.Center) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
            )
        )
        TextField(
            value = username,
            onValueChange = { signUpViewModel.username.value = it.trim() },
            label = { Text(text = "Username", textAlign = TextAlign.Center) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
            )
        )
        TextField(
            value = email,
            onValueChange = { signUpViewModel.email.value = it.trim() },
            label = { Text(text = "Email", textAlign = TextAlign.Center) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
            )
        )
        TextField(
            value = password,
            onValueChange = { signUpViewModel.password.value = it.trim() },
            label = { Text(text = "Password", textAlign = TextAlign.Center) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
//            visualTransformation = PasswordVisualTransformation()
        )
        Button(onClick = {
            scope.launch {
                val result = signUpViewModel.signUpUser(name,username,email, password)
                if (result) {
                    Toast.makeText(context, "Sign UP Successful", Toast.LENGTH_SHORT).show()
                    pageChange(Routes.SignIn.route)
                }
            }
        }) {
            Text(text = "Sign Up", textAlign = TextAlign.Center)
        }
    }
}