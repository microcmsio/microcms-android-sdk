package io.microcms.android

import android.os.Looper
import android.util.Log
import androidx.core.os.HandlerCompat
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import kotlin.concurrent.thread

class MicrocmsClient(val serviceDomain: String, val apiKey: String, val globalDraftKey: String? = null) {

    companion object {
        var baseDomain = "microcms.io"
        var apiVersion = "v1"
        private val mainThreadHandler = HandlerCompat.createAsync(Looper.getMainLooper())
    }

    private fun doRequest(endpoint: String, params: Map<String, Any> = mapOf()): JSONObject {
        val queries = params.map { (key, value) -> "${key}=${URLEncoder.encode(value.toString(), "UTF-8")}" }.joinToString("&")
        val url = URL("https://${serviceDomain}.${baseDomain}/api/${apiVersion}/${endpoint}?${queries}")
        val connection = url.openConnection() as HttpURLConnection
        connection.setRequestProperty("X-API-KEY", apiKey)
        globalDraftKey?.let {
            connection.setRequestProperty("X-GLOBAL-DRAFT-KEY", it)
        }
        connection.connect()

        val inputAsString = connection.inputStream.bufferedReader().use { it.readText() }
        return JSONObject(inputAsString)
    }

    fun get(endpoint: String, params: Map<String, Any> = mapOf(), callback: (Result<JSONObject>) -> Unit) {
        this.get(endpoint, null, params, callback)
    }

    fun get(endpoint: String, contentId: String? = null, params: Map<String, Any> = mapOf(), callback: (Result<JSONObject>) -> Unit) {
        thread {
            val result = runCatching { doRequest("${endpoint}${contentId?.let { "/$it" } ?: "" }", params) }
            mainThreadHandler.post { callback.invoke(result) }
        }
    }
}