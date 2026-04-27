package com.loom.core.common.network

import javax.inject.Qualifier
import kotlin.annotation.AnnotationRetention.RUNTIME

@Qualifier
@Retention(RUNTIME)
annotation class Dispatcher(val loomDispatcher: LoomDispatchers)

enum class LoomDispatchers {
    Default,
    IO,
}