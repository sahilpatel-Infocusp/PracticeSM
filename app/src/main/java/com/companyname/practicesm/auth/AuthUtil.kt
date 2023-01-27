package com.companyname.practicesm.auth

import android.util.Log
import com.companyname.practicesm.firestoreutil.addUserData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

private var auth: FirebaseAuth = Firebase.auth

fun getUser(): FirebaseUser? {
    return auth.currentUser
}

suspend fun signIn(
    email: String,
    password: String
): FirebaseUser {
    return suspendCancellableCoroutine { continuation ->
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                Log.v("SignIn", "SignInSuccessfull")
                continuation.resume(getUser()!!)
            }
            .addOnFailureListener {
                Log.w("SignIn", it.toString())
                continuation.resumeWithException(it)
            }
    }
}


suspend fun signUp(
    name: String,
    username: String,
    email: String,
    password: String
):Boolean{
    return suspendCancellableCoroutine {
        continuation ->
        auth.createUserWithEmailAndPassword(email,password)
            .addOnSuccessListener {
                Log.v("SignUp","User Created with id ${getUser()!!.uid}")
                continuation.resume(true)
            }
            .addOnFailureListener{
                Log.w("SignUp",it.toString())
                continuation.resumeWithException(it)
            }
    }
}

suspend fun signOut(){
    suspendCancellableCoroutine<Unit> {
        continuation ->
        auth.signOut()
        continuation.resume(Unit)
    }
}

