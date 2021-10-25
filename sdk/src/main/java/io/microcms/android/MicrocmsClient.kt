package io.microcms.android

import android.os.Looper
import androidx.core.os.HandlerCompat
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

class MicrocmsClient(
    val serviceDomain: String,
    val apiKey: String,
) {

    companion object {
        var baseDomain = "microcms.io"
        var apiVersion = "v1"
        private val mainThreadHandler = HandlerCompat.createAsync(Looper.getMainLooper())
    }

    private fun doRequest(
        endpoint: String,
        params: List<MicrocmsParameter> = listOf()
    ): JSONObject {
        val queries = params.map { it.toUrlParameter() }.joinToString("&")
        val url =
            URL("https://${serviceDomain}.${baseDomain}/api/${apiVersion}/${endpoint}?${queries}")
        val connection = url.openConnection() as HttpURLConnection
        connection.setRequestProperty("X-MICROCMS-API-KEY", apiKey)
        connection.connect()

        val inputAsString = connection.inputStream.bufferedReader().use { it.readText() }
        return JSONObject(inputAsString)
    }

    fun get(
        endpoint: String,
        params: List<MicrocmsParameter> = listOf(),
        callback: (Result<JSONObject>) -> Unit
    ) {
        this.get(endpoint, null, params, callback)
    }

    fun get(
        endpoint: String,
        contentId: String? = null,
        params: List<MicrocmsParameter> = listOf(),
        callback: (Result<JSONObject>) -> Unit
    ) {
        thread {
            val result =
                runCatching { doRequest("${endpoint}${contentId?.let { "/$it" } ?: ""}", params) }
            mainThreadHandler.post { callback.invoke(result) }
        }
    }
}