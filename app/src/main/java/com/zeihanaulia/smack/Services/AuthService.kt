package com.zeihanaulia.smack.Services

import android.content.Context
import android.util.Log
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.zeihanaulia.smack.Utilities.URL_LOGIN
import com.zeihanaulia.smack.Utilities.URL_REGISTER
import org.json.JSONException
import org.json.JSONObject

object AuthService {

    var authToken = ""
    var userEmail = ""
    var isLoggedIn = false

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

        //registerRequest.setRetryPolicy(DefaultRetryPolicy(50000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT))
        Volley.newRequestQueue(context).add(registerRequest)
    }

    fun loginUser(context: Context, email: String, password: String, complete: (Boolean) -> Unit){

        val jsonBody = JSONObject()
        jsonBody.put("email", email)
        jsonBody.put("name", password)
        val requestBody = jsonBody.toString()

        val loginRequest = object: JsonObjectRequest(Method.POST, URL_LOGIN, null,
            Response.Listener { response ->
                try {
                    userEmail = response.getString("user")
                    authToken = response.getString("token")
                    isLoggedIn = true
                    complete(true)
                }catch (e: JSONException){
                    Log.d("JSON", "EXC:" + e.localizedMessage)
                    complete(false)
                }
            },
            Response.ErrorListener { error ->
                println(error.localizedMessage)
                val je = String(error.networkResponse.data)
                Log.d("ERROR", "Could not register user: $je")
                complete(false)
            }){

            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getBody(): ByteArray {
                return requestBody.toByteArray()
            }
        }
        print(loginRequest)
        //loginRequest.setRetryPolicy(DefaultRetryPolicy(50000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT))
        Volley.newRequestQueue(context).add(loginRequest)
    }
}