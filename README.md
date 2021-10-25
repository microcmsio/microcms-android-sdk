# microCMS Android SDK

It helps you to use microCMS from Android(Kotlin) applications.
Check [the official documentation](https://document.microcms.io/tutorial/android/android-top) for more information

## Getting Started

### Installation

The library is located in maven central.

```gradle
buildscript {
    repositories {
        mavenCentral() //need this line
    }
}
```

Install dependency:

```gradle
dependencies {
    implementation 'io.microcms:android-sdk:1.0.1'
}
```

And you need to get `android.permission.INTERNET` permission in the manifest:

```xml
<uses-permission android:name="android.permission.INTERNET" />
```

### Hot to use

First, create a client.

```kotlin
val client = Client(
  serviceDomain = "YOUR_DOMAIN", //YOUR_DOMAIN is the XXXX part of XXXX.microcms.io
  apiKey = "YOUR_API_KEY",
)
```

Next, you can call some api like below.
`result`(`Result<JSONObject>`) is a instance of [kotlin standard class](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-result/).

```kotlin
client.get(
        "API_ENDPOINT",
        listOf(Limit(2), Filters("createdAt[greater_than]2021")) //some params
) { result ->
    result.onSuccess { json -> Log.d("microCMS example", json.getJSONArray("contents").toString(2)) }
}

client.get(
        "API_ENDPOINT",
        "CONTENT_ID",
        listOf(Fields("id")) //some params
) { result ->
    result.onSuccess { json -> Log.d("microCMS example", json.getString("publishedAt")) }
}
```
