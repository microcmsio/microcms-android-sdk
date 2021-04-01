package io.microcms.android.example

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import io.microcms.android.Client
import kotlinx.android.synthetic.main.activity_main.*

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
                mapOf("limit" to 2, "filters" to "createdAt[greater_than]2021")
        ) { result ->
           result.onSuccess { listJson.text = it.toString(2) }
                   .onFailure { listJson.text = it.toString() }
        }

        //個別に取得
        client.get(
                "blog",
                "what-is-nocoderowcode",
                mapOf("fields" to "id")
        ) { result ->
            result.onSuccess { detailJson.text = it.toString((2)) }
                    .onFailure { detailJson.text = it.toString() }
        }
    }
}