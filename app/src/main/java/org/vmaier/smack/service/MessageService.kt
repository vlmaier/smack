package org.vmaier.smack.service

import android.util.Log
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import org.json.JSONException
import org.vmaier.smack.controller.App
import org.vmaier.smack.model.Channel
import org.vmaier.smack.model.Message
import org.vmaier.smack.util.URL_GET_CHANNELS
import org.vmaier.smack.util.URL_GET_MESSAGES


object MessageService {

    val channels = ArrayList<Channel>()
    val messages = ArrayList<Message>()

    fun getChannels(complete: (Boolean) -> Unit) {

        val getChannelsRequest = object : JsonArrayRequest(Method.GET, URL_GET_CHANNELS, null, Response.Listener { response ->
            try {
                for (i in 0 until response.length()) {
                    val channel = response.getJSONObject(i)
                    val channelId = channel.getString("_id")
                    val channelName = channel.getString("name")
                    val channelDesc = channel.getString("description")
                    val newChannel = Channel(channelId, channelName, channelDesc)
                    channels.add(newChannel)
                }
                complete(true)
            } catch (e: JSONException) {
                Log.d("JSON", "Error occurred: ${e.localizedMessage}")
                complete(false)
            }
        }, Response.ErrorListener { error ->
            Log.d("ERROR", "Could not find any channels: $error")
            complete(false)
        }) {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers.put("Authorization", "Bearer ${App.prefs.authToken}")
                return headers
            }
        }
        App.prefs.requestQueue.add(getChannelsRequest)
    }

    fun getMessages(channelId: String, complete: (Boolean) -> Unit) {

        val getMessagesRequest = object : JsonArrayRequest(Method.GET, "$URL_GET_MESSAGES$channelId", null, Response.Listener { response ->
            clearMessages()
            try {
                for (i in 0 until response.length()) {
                    val message = response.getJSONObject(i)
                    val messageId = message.getString("_id")
                    val messageBody = message.getString("messageBody")
                    val userName = message.getString("userName")
                    val userAvatar = message.getString("userAvatar")
                    val userAvatarColor = message.getString("userAvatarColor")
                    val timestamp = message.getString("timestamp")
                    val newMessage = Message(
                        messageBody, userName, channelId,
                        userAvatar, userAvatarColor, messageId, timestamp
                    )
                    messages.add(newMessage)
                }
                complete(true)
            } catch (e: JSONException) {
                Log.d("JSON", "Error occurred: ${e.localizedMessage}")
                complete(false)
            }
        }, Response.ErrorListener { error ->
            Log.d("ERROR", "Could not find any messages: $error")
            complete(false)
        }) {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers.put("Authorization", "Bearer ${App.prefs.authToken}")
                return headers
            }
        }
        App.prefs.requestQueue.add(getMessagesRequest)
    }

    fun clearChannels() {
        channels.clear()
    }

    fun clearMessages() {
        messages.clear()
    }
}