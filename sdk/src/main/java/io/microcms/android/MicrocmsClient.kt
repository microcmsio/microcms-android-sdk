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

    private fun doPostRequest(
            endpoint: String,
            params: Map<String, Any>,
            isDraft: Boolean
    ): JSONObject {
        val bodyData = JSONObject(params).toString().toByteArray()

        val url = if (isDraft) {
            URL("https://${serviceDomain}.${baseDomain}/api/${apiVersion}/${endpoint}?status=draft")
        } else {
            URL("https://${serviceDomain}.${baseDomain}/api/${apiVersion}/${endpoint}")
        }

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

        val inputAsString = connection.inputStream.bufferedReader().use { it.readText() }
        return JSONObject(inputAsString)
    }

    private fun doPutRequest(
            endpoint: String,
            contentId: String,
            params: Map<String, Any>,
            isDraft: Boolean
    ): JSONObject {
        val bodyData = JSONObject(params).toString().toByteArray()

        var urlString = "https://${serviceDomain}.${baseDomain}/api/${apiVersion}/${endpoint}${contentId?.let { "/$it" } ?: ""}"
        if (isDraft) {
            urlString += "?status=draft"
        }
        val url: URL = URL(urlString)

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

        val inputAsString = connection.inputStream.bufferedReader().use { it.readText() }
        return JSONObject(inputAsString)
    }

    private fun doPatchRequest(
            endpoint: String,
            contentId: String?,
            params: Map<String, Any>,
            isDraft: Boolean
    ): JSONObject {
        val bodyData = JSONObject(params).toString().toByteArray()

        var urlString = "https://${serviceDomain}.${baseDomain}/api/${apiVersion}/${endpoint}${contentId?.let { "/$it" } ?: ""}"
        if (isDraft) {
            urlString += "?status=draft"
        }
        val url = URL(urlString)

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

        val inputAsString = connection.inputStream.bufferedReader().use { it.readText() }
        return JSONObject(inputAsString)
    }

    private fun doDeleteRequest(
            endpoint: String,
            contentId: String,
    ): JSONObject {
        val url =
                URL("https://${serviceDomain}.${baseDomain}/api/${apiVersion}/${endpoint}/${contentId}")
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "DELETE"

        connection.setRequestProperty("X-MICROCMS-API-KEY", apiKey)
        connection.setRequestProperty("Content-type", "application/json; charset=utf-8")

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

    fun create(
        endpoint: String,
        contentId: String?,
        params: Map<String, Any> = mapOf(),
        isDraft: Boolean = false,
        callback: (Result<JSONObject>) -> Unit
    ) {
        contentId?.also {
            thread {
                val result =
                    runCatching { doPutRequest(endpoint, contentId, params, isDraft) }
                mainThreadHandler.post { callback.invoke(result) }
            }
        } ?: run {
            thread {
                val result =
                    runCatching { doPostRequest(endpoint, params, isDraft) }
                mainThreadHandler.post { callback.invoke(result) }
            }
        }
    }

    fun update(
        endpoint: String,
        contentId: String?,
        params: Map<String, Any> = mapOf(),
        isDraft: Boolean = false,
        callback: (Result<JSONObject>) -> Unit
    ) {
        thread {
            val result =
                runCatching { doPatchRequest(endpoint, contentId, params, isDraft) }
            mainThreadHandler.post { callback.invoke(result) }
        }
    }

    fun delete(
        endpoint: String,
        contentId: String,
        callback: (Result<JSONObject>) -> Unit
    ) {
        thread {
            val result =
                runCatching { doDeleteRequest(endpoint, contentId) }
            mainThreadHandler.post { callback.invoke(result) }
        }
    }
}