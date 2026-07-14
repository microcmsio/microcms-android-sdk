# microCMS Android SDK

> [!WARNING]
> このSDKの現在の保守レベルは `Deprecated` です。
>
> 新規利用は推奨していません。新規開発では代替手段の利用を検討してください。
>
> 詳細は[SDKの保守方針](https://document.microcms.io/manual/limitations#h8725a1f02a)をご覧ください。

Android（Kotlin）アプリケーションからmicroCMSを簡単に利用できます。

## はじめに

### インストール

このライブラリはMaven Centralで公開されています。

```gradle
buildscript {
    repositories {
        mavenCentral()
    }
}
```

依存関係を追加します。

```gradle
dependencies {
    implementation 'io.microcms:android-sdk:1.0.1'
}
```

また、マニフェストで `android.permission.INTERNET` 権限を追加してください。

```xml
<uses-permission android:name="android.permission.INTERNET" />
```

## 使い方

### クライアントオブジェクトの作成

```kotlin
val client = Client(
    serviceDomain = "YOUR_DOMAIN", // YOUR_DOMAINはXXXX.microcms.ioのXXXXの部分です
    apiKey = "YOUR_API_KEY",
)
```

以下のようにAPIを呼び出せます。

コールバックに渡される `result`（`Result<JSONObject>`）は、[Kotlin標準ライブラリのクラス](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-result/)です。

### コンテンツ一覧の取得

```kotlin
client.get(
    "API_ENDPOINT"
) { result ->
    result.onSuccess { json -> Log.d("microCMS example", json.getJSONArray("contents").toString(2)) }
}
```

### パラメータを指定したコンテンツ一覧の取得

```kotlin
client.get(
    "API_ENDPOINT",
    listOf(Limit(2), Filters("createdAt[greater_than]2021"))
) { result ->
    result.onSuccess { json -> Log.d("microCMS example", json.getJSONArray("contents").toString(2)) }
}
```

### 単一コンテンツの取得

```kotlin
client.get(
    "API_ENDPOINT",
    "CONTENT_ID",
    listOf(Fields("id"))
) { result ->
    result.onSuccess { json -> Log.d("microCMS example", json.getString("publishedAt")) }
}
```

### オブジェクト形式のコンテンツの取得

```kotlin
client.get(
    "API_ENDPOINT",
    listOf(Fields("id"))
) { result ->
    result.onSuccess { json -> Log.d("microCMS example", json.getString("publishedAt")) }
}
```

### コンテンツの作成

```kotlin
client.create(
    "API_ENDPOINT",
    null,
    mapOf("text" to "Hello Android SDK!"),
    false
) { result ->
    result.onSuccess { json -> Log.d("microCMS example", json.getString("publishedAt")) }
}
```

### IDを指定したコンテンツの作成

```kotlin
client.create(
    "API_ENDPOINT",
    "CONTENT_ID",
    mapOf("text" to "Hello Android SDK!"),
    false
) { result ->
    result.onSuccess { json -> Log.d("microCMS example", json.getString("publishedAt")) }
}
```

### 下書きコンテンツの作成

```kotlin
client.create(
    "API_ENDPOINT",
    "CONTENT_ID",
    mapOf("text" to "Hello Android SDK!"),
    true
) { result ->
    result.onSuccess { json -> Log.d("microCMS example", json.getString("publishedAt")) }
}
```

### コンテンツの更新

```kotlin
client.update(
    "API_ENDPOINT",
    "CONTENT_ID",
    mapOf("text" to "Hello Android SDK update method!")
) { result ->
    result.onSuccess { json -> Log.d("microCMS example", json.getString("publishedAt")) }
}
```

### オブジェクト形式のコンテンツの更新

```kotlin
client.update(
    "API_ENDPOINT",
    null,
    mapOf("message" to "Hello Android SDK!")
) { result ->
    result.onSuccess { json -> Log.d("microCMS example", json.getString("publishedAt")) }
}
```

### コンテンツの削除

```kotlin
client.delete(
    "API_ENDPOINT",
    "CONTENT_ID"
) { result ->
    result.onSuccess { json -> Log.d("microCMS example", json.getString("publishedAt")) }
}
```
