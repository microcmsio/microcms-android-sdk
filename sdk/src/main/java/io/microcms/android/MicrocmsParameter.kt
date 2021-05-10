package io.microcms.android

import java.net.URLEncoder

sealed class MicrocmsParameter {
    abstract fun toUrlParameter(): String
}

data class Fields(val values: List<String>) : MicrocmsParameter() {
    override fun toUrlParameter() = "fields=${values.joinToString(",")}"
}

data class Depth(val value: Int) : MicrocmsParameter() {
    override fun toUrlParameter() = "depth=$value"
}

data class Limit(val value: Int) : MicrocmsParameter() {
    override fun toUrlParameter() = "limit=$value"
}

data class Offset(val value: Int) : MicrocmsParameter() {
    override fun toUrlParameter() = "offset=$value"
}

data class Orders(val values: List<String>) : MicrocmsParameter() {
    override fun toUrlParameter() = "orders=${values.joinToString(",")}"
}

data class Q(val value: String) : MicrocmsParameter() {
    override fun toUrlParameter() = "q=${URLEncoder.encode(value, "UTF-8")}"
}

data class Ids(val values: List<String>) : MicrocmsParameter() {
    override fun toUrlParameter() = "ids=${values.joinToString(",")}"
}

data class Filters(val value: String) : MicrocmsParameter() {
    override fun toUrlParameter() = "filters=$value"
}