package io.microcms.android.example

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import io.microcms.android.Client

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val client = Client(
                serviceDomain = BuildConfig.MICROCMS_SERVICE_DOMAIN,
                apiKey = BuildConfig.MICROCMS_API_KEY,
                globalDraftKey = BuildConfig.MICROCMS_GLOBAL_DRAFT_KEY
        )

        //リスト取得
        client.getList(
                "blog",
                mapOf("limit" to 5, "filters" to "createdAt[greater_than]2021")
        ) { result ->
           result.onSuccess { Log.d("microcms_example", "Success!! ${it}") }
                   .onFailure { Log.d("microcms_example", "Failure!! ${it}") }
        }

        //個別に取得
        client.get(
                "blog",
                "what-is-nocoderowcode",
                mapOf("fields" to "id")
        ) { result ->
            result.onSuccess { Log.d("microcms_example", "Success!! ${it}") }
                    .onFailure { Log.d("microcms_example", "Failure!! ${it}") }
        }
    }
}