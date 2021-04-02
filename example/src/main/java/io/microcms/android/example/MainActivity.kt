package io.microcms.android.example

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import io.microcms.android.Client
import io.microcms.android.example.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
           result.onSuccess { binding.listJson.text = it.toString(2) }
                   .onFailure { binding.listJson.text = it.toString() }
        }

        //個別に取得
        client.get(
                "blog",
                "what-is-nocoderowcode",
                mapOf("fields" to "id")
        ) { result ->
            result.onSuccess { binding.detailJson.text = it.toString((2)) }
                    .onFailure { binding.detailJson.text = it.toString() }
        }
    }
}