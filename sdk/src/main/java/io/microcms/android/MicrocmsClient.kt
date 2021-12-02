package io.microcms.android

import android.os.Looper
import android.util.Log
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

    private fun doPostRequest(
            endpoint: String,
            params: Map<String, Any>
    ): JSONObject {
        Log.d("himara2", "### doPostRequest -----------")
        val bodyData = JSONObject(params).toString().toByteArray()

        val url =
                URL("https://${serviceDomain}.${baseDomain}/api/${apiVersion}/${endpoint}")
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "POST"
        connection.doOutput = true
        connection.setChunkedStreamingMode(0)

        connection.setRequestProperty("X-MICROCMS-API-KEY", apiKey)
        connection.setRequestProperty("Content-type", "application/json; charset=utf-8")

        val outputStream = connection.outputStream
        outputStream.write(bodyData)
        outputStream.flush()
        outputStream.close()

        val statusCode = connection.responseCode
        Log.d("himara2", "### statusCode is $statusCode -----------")

        val inputAsString = connection.inputStream.bufferedReader().use { it.readText() }
        return JSONObject(inputAsString)
    }

    private fun doPutRequest(
            endpoint: String,
            contentId: String,
            params: Map<String, Any>
    ): JSONObject {
        Log.d("himara2", "### doPutRequest -----------")
        val bodyData = JSONObject(params).toString().toByteArray()

        val url =
                URL("https://${serviceDomain}.${baseDomain}/api/${apiVersion}/${endpoint}/${contentId}")
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "PUT"
        connection.doOutput = true
        connection.setChunkedStreamingMode(0)

        connection.setRequestProperty("X-MICROCMS-API-KEY", apiKey)
        connection.setRequestProperty("Content-type", "application/json; charset=utf-8")

        val outputStream = connection.outputStream
        outputStream.write(bodyData)
        outputStream.flush()
        outputStream.close()

        val statusCode = connection.responseCode
        Log.d("himara2", "### statusCode is $statusCode -----------")

        val inputAsString = connection.inputStream.bufferedReader().use { it.readText() }
        return JSONObject(inputAsString)
    }

    private fun doPatchRequest(
            endpoint: String,
            contentId: String,
            params: Map<String, Any>
    ): JSONObject {
        Log.d("himara2", "### doPatchRequest -----------")
        val bodyData = JSONObject(params).toString().toByteArray()

        val url =
                URL("https://${serviceDomain}.${baseDomain}/api/${apiVersion}/${endpoint}/${contentId}")
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "PATCH"
        connection.doOutput = true
        connection.setChunkedStreamingMode(0)

        connection.setRequestProperty("X-MICROCMS-API-KEY", apiKey)
        connection.setRequestProperty("Content-type", "application/json; charset=utf-8")

        val outputStream = connection.outputStream
        outputStream.write(bodyData)
        outputStream.flush()
        outputStream.close()

        val statusCode = connection.responseCode
        Log.d("himara2", "### statusCode is $statusCode -----------")

        val inputAsString = connection.inputStream.bufferedReader().use { it.readText() }
        return JSONObject(inputAsString)
    }

    private fun doDeleteRequest(
            endpoint: String,
            contentId: String,
    ): JSONObject {
        Log.d("himara2", "### doDeleteRequest -----------")

        val url =
                URL("https://${serviceDomain}.${baseDomain}/api/${apiVersion}/${endpoint}/${contentId}")
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "DELETE"

        connection.setRequestProperty("X-MICROCMS-API-KEY", apiKey)
        connection.setRequestProperty("Content-type", "application/json; charset=utf-8")

        connection.connect()

        val statusCode = connection.responseCode
        Log.d("himara2", "### statusCode is $statusCode -----------")

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

    fun post(
        endpoint: String,
        params: Map<String, Any> = mapOf(),
        callback: (Result<JSONObject>) -> Unit
    ) {
        thread {
            val result =
                    runCatching { doPostRequest(endpoint, params) }
            mainThreadHandler.post { callback.invoke(result) }
        }
    }

    fun put(
            endpoint: String,
            contentId: String,
            params: Map<String, Any> = mapOf(),
            callback: (Result<JSONObject>) -> Unit
    ) {
        thread {
            val result =
                    runCatching { doPutRequest(endpoint, contentId, params) }
            mainThreadHandler.post { callback.invoke(result) }
        }
    }
}