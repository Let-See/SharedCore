package io.github.letsee

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform