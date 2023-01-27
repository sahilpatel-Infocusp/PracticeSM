package com.companyname.practicesm.auth

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.companyname.practicesm.routes.Routes
import com.companyname.practicesm.viewmodels.SignUpViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier,
    signUpViewModel: SignUpViewModel = viewModel(),
    pageChange: (String) -> Unit
) {

    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val email = rememberSaveable {
        mutableStateOf("")
    }
    val name = rememberSaveable {
        mutableStateOf("")
    }
    val username = rememberSaveable {
        mutableStateOf("")
    }
    val password = rememberSaveable {
        mutableStateOf("")
    }
    val isLoading = rememberSaveable {
        signUpViewModel.isLoading.value
    }

    LaunchedEffect(key1 = signUpViewModel.errorMessage) {
        signUpViewModel.errorMessage.collectLatest {
            if (it.isNotEmpty()) {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }
        }
        signUpViewModel.resetErrorMessage()
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
            value = name.value,
            onValueChange = { name.value = it.trim() },
            label = { Text(text = "Name", textAlign = TextAlign.Center) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
            )
        )
        TextField(
            value = username.value,
            onValueChange = { username.value = it.trim() },
            label = { Text(text = "Username", textAlign = TextAlign.Center) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
            )
        )
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
//            visualTransformation = PasswordVisualTransformation()
        )
        Button(onClick = {
            scope.launch {
                val result = signUpViewModel.signUpUser(name.value,username.value,email.value, password.value)
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