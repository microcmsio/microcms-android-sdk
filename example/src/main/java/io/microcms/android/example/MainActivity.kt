package io.microcms.android.example

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import io.microcms.android.*
import io.microcms.android.example.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val client = MicrocmsClient(
            serviceDomain = BuildConfig.MICROCMS_SERVICE_DOMAIN,
            apiKey = BuildConfig.MICROCMS_API_KEY,
        )

        //リスト取得もしくはオブジェクト形式の取得
        client.get(
            "news_android",
            listOf(Limit(2), Filters("createdAt[greater_than]2021"))
        ) { result ->
            result.onSuccess { binding.listJson.text = it.toString(2) }
                .onFailure { binding.listJson.text = it.toString() }
        }

        //個別に取得
        client.get(
            "news_android",
            "96lj7-j_np",
            listOf(Fields(listOf("id")))
        ) { result ->
            result.onSuccess { binding.detailJson.text = it.toString((2)) }
                .onFailure { binding.detailJson.text = it.toString() }
        }
    }
}