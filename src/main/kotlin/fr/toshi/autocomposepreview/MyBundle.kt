<<<<<<< HEAD:src/main/kotlin/fr/toshi/autocomposepreview/MyBundle.kt
package fr.toshi.autocomposepreview
=======
package com.github.toshibane.composeautopreview
>>>>>>> e84af9f2afe13c6be72ebebdeff0ea83400ad246:src/main/kotlin/com/github/toshibane/composeautopreview/MyBundle.kt

import com.intellij.DynamicBundle
import org.jetbrains.annotations.NonNls
import org.jetbrains.annotations.PropertyKey

@NonNls
private const val BUNDLE = "messages.MyBundle"

object MyBundle : DynamicBundle(BUNDLE) {

    @Suppress("SpreadOperator")
    @JvmStatic
    fun message(@PropertyKey(resourceBundle = BUNDLE) key: String, vararg params: Any) =
            getMessage(key, *params)

    @Suppress("SpreadOperator", "unused")
    @JvmStatic
    fun messagePointer(@PropertyKey(resourceBundle = BUNDLE) key: String, vararg params: Any) =
            getLazyMessage(key, *params)
}