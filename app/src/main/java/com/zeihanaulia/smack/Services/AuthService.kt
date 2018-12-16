package com.zeihanaulia.smack.Services

import android.content.Context
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.zeihanaulia.smack.Utilities.URL_REGISTER
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
                println(error)
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
}