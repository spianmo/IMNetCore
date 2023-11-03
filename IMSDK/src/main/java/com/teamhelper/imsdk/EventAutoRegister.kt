package com.teamhelper.imsdk

import android.app.Activity
import android.app.Service
import android.content.Context
import android.os.Bundle
import com.highcapable.yukireflection.factory.searchClass
import com.teamhelper.imsdk.base.EventRegistry
import com.teamhelper.imsdk.base.EventSubscriber
import de.robv.android.xposed.DexposedBridge
import de.robv.android.xposed.XC_MethodHook
import java.lang.reflect.Method


class EventAutoRegister(context: Context) {

    private var hostClassLoader: ClassLoader

    init {
        this.hostClassLoader = context.classLoader
        hookActivity()
        hookService()
        hookCommonClazz(context)
    }

    private fun hookService() = hookComponent(
        Service::class.java.getDeclaredMethod(
            "onCreate"
        ), Service::class.java.getDeclaredMethod("onDestroy")
    )

    private fun hookActivity() = hookComponent(
        Activity::class.java.getDeclaredMethod(
            "onCreate",
            Bundle::class.java
        ), Activity::class.java.getDeclaredMethod("onDestroy")
    )

    private fun hookComponent(onCreateMethod: Method, onDestroyMethod: Method) {
        if (!DexposedBridge.isMethodHooked(onCreateMethod)) {
            DexposedBridge.hookMethod(onCreateMethod,
                object : XC_MethodHook() {
                    @Throws(Throwable::class)
                    override fun afterHookedMethod(param: MethodHookParam) {
                        super.afterHookedMethod(param)
                        register(param.thisObject)
                    }
                })
        }

        if (!DexposedBridge.isMethodHooked(onDestroyMethod)) {
            DexposedBridge.hookMethod(onDestroyMethod,
                object : XC_MethodHook() {
                    @Throws(Throwable::class)
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        super.beforeHookedMethod(param)
                        unregister(param.thisObject)
                    }
                })
        }
    }

    private fun hookCommonClazz(context: Context) {
        hostClassLoader.searchClass(context) {
            extends<EventLifecycleSubscriber>()
        }.all().stream().distinct()?.filter {
            it.isAnnotationPresent(EventSubscriber::class.java)
        }?.forEach { clazz ->
            DexposedBridge.hookAllConstructors(clazz,
                object : XC_MethodHook() {
                    @Throws(Throwable::class)
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        super.beforeHookedMethod(param)
                        register(param.thisObject)
                    }
                })
        }
        val releaseMethod =
            EventLifecycleSubscriber::class.java.getDeclaredMethod(EventLifecycleSubscriber::release.name)
        if (!DexposedBridge.isMethodHooked(releaseMethod)) {
            DexposedBridge.hookMethod(releaseMethod,
                object : XC_MethodHook() {
                    @Throws(Throwable::class)
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        super.beforeHookedMethod(param)
                        unregister(param.thisObject)
                    }
                })
        }
    }

    companion object {
        fun register(subscriber: Any) {
            val subscriberClass = subscriber.javaClass
            if (subscriberClass.isAnnotationPresent(EventSubscriber::class.java)) {
                EventRegistry.register(subscriber)
            }
        }

        fun unregister(subscriber: Any) {
            val subscriberClass = subscriber.javaClass
            if (subscriberClass.isAnnotationPresent(EventSubscriber::class.java)) {
                EventRegistry.unregister(subscriber)
            }
        }
    }
}

