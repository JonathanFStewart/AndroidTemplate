package com.jonathanstewart.androidtemplate.about


data class Dependency (
    val name: String,
    val descripton: String,
    val url: String
)
object dependencyList {
    val list by lazy{arrayListOf(
        Dependency(
            name = "retrofit",
            descripton = "Type-safe HTTP client for Android and Java by Square, Inc",
            url = "https://square.github.io/retrofit/"),
        Dependency(
            name = "moshi",
            descripton = "A modern JSON library for Kotlin and Java.",
            url = "http://square.github.io/moshi/1.x/moshi/"),
        Dependency(
            name = "Kotlin",
            descripton = "Kotlin is a cross-platform, statically typed, general-purpose programming language with type inference. Kotlin is designed to interoperate fully with Java, and the JVM version of its standard library depends on the Java Class Library, but type inference allows its syntax to be more concise.",
            url = "https://kotlinlang.org"),
        Dependency(
            name = "androidX",
            descripton = "AndroidX is the open-source project that the Android team uses to develop, test, package, version and release libraries within Jetpack.",
            url = "https://developer.android.com/jetpack/androidx"),
        Dependency(
            name = "constraint Layout",
            descripton = "ConstraintLayout allows you to create large and complex layouts with a flat view hierarchy (no nested view groups). It's similar to RelativeLayout in that all views are laid out according to relationships between sibling views and the parent layout, but it's more flexible than RelativeLayout and easier to use with Android Studio's Layout Editor.",
            url = "https://developer.android.com/training/constraint-layout"),
        Dependency(
            name = "navigation ui",
            descripton = "Android Jetpack's Navigation component helps you implement navigation, from simple button clicks to more complex patterns, such as app bars and the navigation drawer. The Navigation component also ensures a consistent and predictable user experience by adhering to an established set of principles.",
            url = "https://developer.android.com/guide/navigation"),
        Dependency(
            name = "lifecycle extensions",
            descripton = "Lifecycle-aware components perform actions in response to a change in the lifecycle status of another component, such as activities and fragments. These components help you produce better-organized, and often lighter-weight code, that is easier to maintain. See the reference docs for more information.",
            url = "https://developer.android.com/jetpack/androidx/releases/lifecycle"),
        Dependency(
            name = "espresso",
            descripton = "Use Espresso to write concise, beautiful, and reliable Android UI tests.",
            url = "https://developer.android.com/training/testing/espresso"),
        Dependency(
            name = "Ok http",
            descripton = "An HTTP client for Android, Kotlin, and Java",
            url = "https://square.github.io/okhttp/"),
        Dependency(
            name = "Kotlinx CoRoutines",
            descripton = "kotlinx.coroutines is a rich library for coroutines developed by JetBrains. It contains a number of high-level coroutine-enabled primitives that this guide covers, including launch, async and others.",
            url = "https://kotlinlang.org/docs/reference/coroutines/coroutines-guide.html"),
        Dependency(
            name = "Koin",
            descripton = "A pragmatic lightweight dependency injection framework for Kotlin developers. Written in pure Kotlin using functional resolution only: no proxy, no code generation, no reflection!",
            url = "https://insert-koin.io"),
        Dependency(
            name = "Timber",
            descripton = "A logger with a small, extensible API which provides utility on top of Android's normal Log class.",
            url = "https://github.com/JakeWharton/timber"),
        Dependency(
            name = "Room",
            descripton = "The Room persistence library provides an abstraction layer over SQLite to allow for more robust database access while harnessing the full power of SQLite.",
            url = "https://developer.android.com/topic/libraries/architecture/room"),
        Dependency(
            name = "jUnit",
            descripton = "JUnit is a unit testing framework for the Java programming language. ",
            url = "https://junit.org/junit4/"),
        Dependency(
            name = "glide",
            descripton = "An image loading and caching library for Android focused on smooth scrolling",
            url = "https://github.com/bumptech/glide"),
        Dependency(
            name = "mockk",
            descripton = "mocking library for Kotlin ",
            url = "https://mockk.io")
    )}
}