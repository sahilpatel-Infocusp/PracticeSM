package com.companyname.practicesm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.companyname.practicesm.auth.SignInScreen
import com.companyname.practicesm.auth.SignUpScreen
import com.companyname.practicesm.auth.getUser
import com.companyname.practicesm.feed.SearchScreen
import com.companyname.practicesm.profile.AddPostScreen
import com.companyname.practicesm.profile.ProfilePageScreen
import com.companyname.practicesm.routes.Routes
import com.companyname.practicesm.ui.theme.PracticeSMTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PracticeSMTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val navController:NavHostController = rememberNavController()
                    NavHost(navController = navController, startDestination = Routes.SignIn.route){
                        composable(Routes.SignIn.route){
                            SignInScreen{
                                navController.navigate(it)
                            }
                        }
                        composable(Routes.SignUp.route){
                            SignUpScreen{
                                navController.navigate(it)
                            }
                        }
                        composable(Routes.Search.route){
                            SearchScreen{
                                navController.navigate(it)
                            }
                        }
                        composable(Routes.Feed.route){

                        }
                        composable(Routes.Profile.route){
                            ProfilePageScreen{
                                navController.navigate(it)
                            }
                        }
                        composable(Routes.AddPost.route){
                            AddPostScreen {
                                navController.navigate(it)
                            }
                        }
                    }

                    PracticeSmAPP(navController)
                }
            }
        }
    }

    private fun PracticeSmAPP(navController: NavHostController) {
        if(getUser()!=null)
            navController.navigate(Routes.Profile.route)
        else
            navController.navigate(Routes.SignIn.route)
    }
}

