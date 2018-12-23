package com.zeihanaulia.smack.Services

import android.content.Context
import android.content.Intent
import android.support.v4.content.LocalBroadcastManager
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.zeihanaulia.smack.Controllers.App
import com.zeihanaulia.smack.Utilities.*
import org.json.JSONException
import org.json.JSONObject

object AuthService {

    fun registerUser(context: Context, email: String, password: String, complete: (Boolean) -> Unit){
        val url = URL_REGISTER

        val jsonBody = JSONObject()
        jsonBody.put("email", email)
        jsonBody.put("password", password)
        val requestBody = jsonBody.toString()

        val registerRequest = object : StringRequest(Request.Method.POST, url,
            Response.Listener { response ->
                println(response)
                complete(true)
            },
            Response.ErrorListener { error ->
                println(error.message)
                complete(true)
            }){

            override fun getBodyContentType(): String{
                return "application/json; charset=utf-8"
            }
            override  fun getBody(): ByteArray {
                return requestBody.toByteArray()
            }
        }

        App.perfs.requestQueue.add(registerRequest)
    }

    fun loginUser(context: Context, email: String, password: String, complete: (Boolean) -> Unit){

        val jsonBody = JSONObject()
        jsonBody.put("email", email)
        jsonBody.put("password", password)
        val requestBody = jsonBody.toString()

        val loginRequest = object: JsonObjectRequest(Method.POST, URL_LOGIN, null,
            Response.Listener { response ->
                try {
                    App.perfs.userEmail = response.getString("user")
                    App.perfs.authToken = response.getString("token")
                    App.perfs.isLoggedIn = true
                    complete(true)
                }catch (e: JSONException){
                    Log.d("JSON", "EXC:" + e.localizedMessage)
                    complete(false)
                }
            },
            Response.ErrorListener { error ->
                println(error.localizedMessage)
                val jsonError = String(error.networkResponse.data)
                Log.d("ERROR", "Could not register user: $jsonError")
                complete(false)
            }){

            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getBody(): ByteArray {
                return requestBody.toByteArray()
            }
        }

        App.perfs.requestQueue.add(loginRequest)
    }

    fun createUser(
        context: Context, name: String, email: String, avatarName: String,
        avatarColor: String, complete: (Boolean) -> Unit
    ){
        val jsonBody = JSONObject()
        jsonBody.put("email", email)
        jsonBody.put("name", name)
        jsonBody.put("avatarName", avatarName)
        jsonBody.put("avatarColor", avatarColor)
        val requestBody = jsonBody.toString()

        val createUserRequest = object : JsonObjectRequest(Method.POST, URL_CREATE_USER, null,
            Response.Listener { response ->
                try {
                    UserDataService.name = response.getString("name")
                    UserDataService.email = response.getString("email")
                    UserDataService.avatarName = response.getString("avatarName")
                    UserDataService.avatarColor = response.getString("avatarColor")
                    UserDataService.id = response.getString("_id")
                    complete(true)
                }catch (e: JSONException) {
                    Log.d("JSON", "EXC:" + e.localizedMessage)
                    complete(false)
                }
            },
            Response.ErrorListener {error ->
                val jsonError = String(error.networkResponse.data)
                Log.d("ERROR", "Could not register user: $jsonError")
                complete(false)
            }){


            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getBody(): ByteArray {
                return requestBody.toByteArray()
            }

            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers.put("Authorization", "Bearer ${App.perfs.authToken}")
                return  headers
            }
        }

        App.perfs.requestQueue.add(createUserRequest)
    }

    fun findUserByEmail(context: Context, complete: (Boolean) -> Unit) {
        val findUserRequest = object: JsonObjectRequest(Method.GET, "$URL_GET_USER${App.perfs.userEmail}", null,
            Response.Listener {response ->
                try {
                    UserDataService.name = response.getString("name")
                    UserDataService.email = response.getString("email")
                    UserDataService.avatarName = response.getString("avatarName")
                    UserDataService.avatarColor = response.getString("avatarColor")
                    UserDataService.id = response.getString("_id")

                    val userDataChange =  Intent(BROADCAST_USER_DATA_CHANGE)
                    LocalBroadcastManager.getInstance(context).sendBroadcast(userDataChange)
                    complete(true)
                }catch (e: JSONException){
                    Log.d("JSON", "EXC:" + e.localizedMessage)
                    complete(false)
                }
            },
            Response.ErrorListener {error ->
                val jsonError = String(error.networkResponse.data)
                Log.d("ERROR", "Could not find user: $jsonError")
                complete(false)
            }){

            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers.put("Authorization", "Bearer ${App.perfs.authToken}")
                return headers
            }
        }
        App.perfs.requestQueue.add(findUserRequest)
    }

}