package com.zeihanaulia.smack.Services
import android.content.Context
import android.util.Log
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.zeihanaulia.smack.Controllers.App
import com.zeihanaulia.smack.Models.Channel
import com.zeihanaulia.smack.Utilities.URL_GET_CHANNELS
import org.json.JSONException

object MessageService {
    val channels = ArrayList<Channel>()

    fun getChannels(context: Context, complete: (Boolean) -> Unit){
        val channelsRequest = object: JsonArrayRequest(Method.GET, URL_GET_CHANNELS, null,
            Response.Listener{ response ->
                try {
                    for (x in 0 until response.length()){
                        val channel = response.getJSONObject(x)
                        val channelName = channel.getString("name")
                        val channelDesc = channel.getString("description")
                        val channelId = channel.getString("_id")

                        val newChannel = Channel(channelName, channelDesc, channelId)
                        this.channels.add(newChannel)
                    }
                    complete(true)
                }catch (e: JSONException){
                    val jsonError = e.localizedMessage
                    Log.d("ERROR", "Could not register user: $jsonError")
                    complete(false)
                }
            },
            Response.ErrorListener { error ->
                val jsonError = String(error.networkResponse.data)
                Log.d("ERROR", "Could not register user: $jsonError")
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

        Volley.newRequestQueue(context).add(channelsRequest)
    }

}