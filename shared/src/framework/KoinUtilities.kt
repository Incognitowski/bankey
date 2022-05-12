package framework

import org.koin.core.qualifier.Qualifier
import org.koin.java.KoinJavaComponent

inline fun <reified T> injectFromKoin(qualifier : Qualifier? = null): T {
    val lInstance: T by KoinJavaComponent.inject(T::class.java, qualifier = qualifier)
    return lInstance
}