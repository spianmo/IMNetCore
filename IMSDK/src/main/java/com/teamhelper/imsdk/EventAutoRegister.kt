package com.teamhelper.imsdk

import android.content.Context
import android.content.pm.PackageManager
import com.teamhelper.imsdk.base.EventRegistry
import com.teamhelper.imsdk.base.EventSubscriber
import de.robv.android.xposed.DexposedBridge
import de.robv.android.xposed.XC_MethodHook
import org.luckypray.dexkit.DexKitBridge


class EventAutoRegister(context: Context) {

    private var hostClassLoader: ClassLoader

    init {
        this.hostClassLoader = context.classLoader
        val apkPath = context.packageCodePath
        val packageInfo = context.packageManager.getPackageInfo(
            context.packageName, PackageManager.GET_ACTIVITIES
        )
        val activities = packageInfo.activities.map { it.packageName }.distinct()
        findSubscribeClazz(apkPath, activities)
    }

    private fun findSubscribeClazz(apkPath: String, activities: Collection<String>) {
        DexKitBridge.create(apkPath)?.use { bridge ->
            bridge.findClass {
                searchPackages(activities)
                matcher {
                    annotations {
                        add {
                            type = EventSubscriber::class.java.name
                        }
                    }
                }
            }
        }?.stream()?.distinct()?.forEach {
            val clazz = it.getInstance(hostClassLoader)
            DexposedBridge.hookAllMethods(
                exploreClassHierarchy(clazz, "onCreate"), "onCreate",
                object : XC_MethodHook() {
                    @Throws(Throwable::class)
                    override fun afterHookedMethod(param: MethodHookParam) {
                        super.afterHookedMethod(param)
                        register(param.thisObject)
                    }
                })

            DexposedBridge.hookAllMethods(
                exploreClassHierarchy(clazz, "onDestroy"), "onDestroy",
                object : XC_MethodHook() {
                    @Throws(Throwable::class)
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        super.beforeHookedMethod(param)
                        unregister(param.thisObject)
                    }
                })
        }
    }

    private fun exploreClassHierarchy(startClass: Class<*>?, methodName: String): Class<*>? {
        if (startClass == null) {
            return null
        }
        val methods = startClass.getDeclaredMethods()
        for (method in methods) {
            if (method.name == methodName) {
                return startClass
            }
        }
        val superClass = startClass.superclass
        return exploreClassHierarchy(superClass, methodName)
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
