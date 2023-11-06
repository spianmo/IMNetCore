package com.teamhelper.imsdk

import android.app.Activity
import android.app.Service
import android.content.Context
import android.os.Bundle
import com.teamhelper.imsdk.base.EventRegistry
import com.teamhelper.imsdk.base.EventSubscriber
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import org.luckypray.dexkit.DexKitBridge
import java.lang.reflect.Method


class EventAutoRegister(context: Context) {

    private var hostClassLoader: ClassLoader

    init {
        this.hostClassLoader = context.classLoader
        hookActivity()
        hookService()
        hookCommonClazz(context.packageCodePath)
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
        if (!XposedBridge.isHooked(onCreateMethod)) {
            XposedBridge.hookMethod(onCreateMethod,
                object : XC_MethodHook() {
                    @Throws(Throwable::class)
                    override fun afterHookedMethod(param: MethodHookParam) {
                        super.afterHookedMethod(param)
                        register(param.thisObject)
                    }
                })
        }

        if (!XposedBridge.isHooked(onDestroyMethod)) {
            XposedBridge.hookMethod(onDestroyMethod,
                object : XC_MethodHook() {
                    @Throws(Throwable::class)
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        super.beforeHookedMethod(param)
                        unregister(param.thisObject)
                    }
                })
        }
    }

    private fun hookCommonClazz(apkPath: String) {
        DexKitBridge.create(apkPath)?.use { bridge ->
            bridge.findClass {
                matcher {
                    annotations {
                        add {
                            type = EventSubscriber::class.java.name
                        }
                    }
                    superClass {
                        className = EventLifecycleSubscriber::class.java.name
                    }
                }
            }
        }?.stream()?.distinct()?.forEach {
            val clazz = it.getInstance(hostClassLoader)

            XposedBridge.hookAllConstructors(clazz,
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
        if (!XposedBridge.isHooked(releaseMethod)) {
            XposedBridge.hookMethod(releaseMethod,
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
        init {
            System.loadLibrary("dexkit")
        }

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
