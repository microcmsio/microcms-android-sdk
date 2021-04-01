# microCMS Android SDK

It helps you to use microCMS from Android(Kotlin) applications.

## Getting Started

### Installation

_under constructions..._

### Hot to use

First, create a client.

```kotlin
val client = Client(
  serviceDomain = "YOUR_DOMAIN",
  apiKey = "YOUR_API_KEY",
  globalDraftKey = "YOUR_GLOBAL_DRAFT_KEY" //if need
)
```

Next, you can call some api like below.
`result` is a instance of [kotlin standard class](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-result/).

```kotlin
client.getList(
        "API_ENDPOINT",
        mapOf("limit" to 2, "filters" to "createdAt[greater_than]2021") //some params
) { result -> /* some actions */ }

client.get(
        "API_ENDPOINT",
        "CONTENT_ID",
        mapOf("fields" to "id") //some params
) { result -> /* some actions */ }
```