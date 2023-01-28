package com.companyname.practicesm.firestoreutil

import android.util.Log
import com.companyname.practicesm.auth.getUser
import com.companyname.practicesm.models.UserDataModel
import com.companyname.practicesm.models.UserPostModel
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import java.lang.reflect.Type
import kotlin.coroutines.Continuation
import kotlin.coroutines.resumeWithException
import kotlin.reflect.typeOf

private var db = Firebase.firestore
val postList:MutableMap<String,UserPostModel> = mutableMapOf()
val searchedPost:MutableMap<String,UserPostModel?> = mutableMapOf()

fun getCountOfTotalPost(): Int {
    var count:Int = 0
     db.collection("UserPosts").document("PostCount")
         .get()
        .addOnSuccessListener {
            count = it.data?.get("Count")?.toString()?.toInt() ?: 0
        }
         .addOnFailureListener{
             count = 0
         }
    return count
}


var totalPostCount:Int = getCountOfTotalPost()



suspend fun addUserData(uid:String,name:String,username:String,email:String):Boolean{
    val user = UserDataModel(uid,name,username,email,0)
    Log.v("Gson",Gson().toJson(user))
    return suspendCancellableCoroutine {
        continuation ->
        db.collection("UserData")
            .document(uid)
            .set(user)
            .addOnSuccessListener {
                Log.v("AddUser","User added")
                updateUserCount()
                continuation.resume(true, onCancellation = {})
            }
            .addOnFailureListener{
                continuation.resumeWithException(it)
            }
    }
}

fun updateUserCount(){
    db.collection("UserData")
        .document("UserCount")
        .update("UserCount",FieldValue.increment(1))
}

suspend fun getUserData():UserDataModel?{
    return suspendCancellableCoroutine {
        continuation ->
        db.collection("UserData")
            .document(getUser()!!.uid)
            .get()
            .addOnSuccessListener {
                val userData = Gson().fromJson(it.data.toString(),UserDataModel::class.java)
//                Log.v("UserData",userData.toString())
                continuation.resume(userData, onCancellation = {})
            }
            .addOnFailureListener{
                continuation.resumeWithException(it)
            }
    }
}


suspend fun addPost(title:String,description:String):Boolean{
    Log.v("PostCount", totalPostCount.toString())
    return false

//    val userPost = UserPostModel(++totalPostCount,getUser()!!.uid,title,description)
//    return suspendCancellableCoroutine {
//        continuation ->
//        db.collection("UserPosts")
//            .add(userPost)
//            .addOnSuccessListener {
//                Log.v("AddUserPost","Post added successfully")
//                updateUserPostCount()
//                updateTotalPostCount()
//                continuation.resume(true, onCancellation = {})
//            }
//            .addOnFailureListener{
//                Log.w("AddUserPost",it)
//                continuation.resumeWithException(it)
//            }
//    }
}

fun updateTotalPostCount() {
    db.collection("UserPosts").document("PostCount").update("Count",FieldValue.increment(1))
}

fun updateUserPostCount() {
    db.collection("UserData").document(getUser()!!.uid).update("postCount",FieldValue.increment(1))
}


suspend fun getUserPosts(): Flow<MutableMap<String,UserPostModel>> {

    return callbackFlow {
        val uid = getUser()?.uid
        var data = db.collection("UserPosts")
            .whereEqualTo("uid", uid.toString())
            .addSnapshotListener{
                snapshot, e ->
                if (e != null) {
                    Log.v("GetUserPosts","Exception Occured")
                    return@addSnapshotListener
                }

                if(snapshot!=null) {
                    if (postList.isEmpty()) {
                        for (post in snapshot) {
                            postList[uid.toString()]=(Gson().fromJson(post.data.toString(),UserPostModel::class.java))
                        }
                        Log.v("GetUserPost", postList.size.toString())
                    }
                    else{
                        for(dc in snapshot.documentChanges){
                            when(dc.type){
                                DocumentChange.Type.ADDED -> {
                                    postList[uid.toString()] = Gson().fromJson(dc.document.data.toString(),UserPostModel::class.java)
                                }
                                DocumentChange.Type.REMOVED -> {
                                    postList.remove(uid)
                                }
                                DocumentChange.Type.MODIFIED -> {
                                    postList[uid.toString()] = Gson().fromJson(dc.document.data.toString(),UserPostModel::class.java)
                                }
                            }
                        }
                    }

                    trySend(postList)
                }
                else{
                    Log.v("GetUserPosts","Null snapshot")
                }
            }
        awaitClose{
            data.remove()
        }
    }
}


suspend fun getMatchedPost(searchText:String):Flow<MutableMap<String,UserPostModel?>>{
    return callbackFlow<MutableMap<String,UserPostModel?>> {
        val uid = getUser()!!.uid
        val data = db.collection("UserPosts")
            .whereEqualTo("description",Regex(searchText).pattern)
            .addSnapshotListener{
                snapshot, e ->
                if(e!=null){
                    Log.v("GetMatchedPost",e.toString())
                    return@addSnapshotListener
                }
                if(snapshot!=null){
                    for(post in snapshot){
                        searchedPost[uid] = Gson().fromJson(post.data.toString(),UserPostModel::class.java)
                    }
                }
                else{
                    searchedPost["0"] = null
                }
                trySend(searchedPost)
            }

        awaitClose{
            data.remove()
        }
    }
}