package io.nervina.joyid.plugins.screenOrientation

import android.content.res.Configuration
import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;

@CapacitorPlugin(name = "ScreenOrientation")
class ScreenOrientationPlugin : Plugin() {
    private var implementation: ScreenOrientation? = null
    override fun load() {
        implementation = ScreenOrientation(activity)
    }

    @PluginMethod
    fun orientation(call: PluginCall) {
        val ret = JSObject()
        val type = implementation?.getCurrentOrientationType()
        ret.put("type", type)
        call.resolve(ret)
    }

    @PluginMethod
    fun lock(call: PluginCall) {
        val orientationType: String? = call.getString("orientation")
        if (orientationType == null) {
            call.reject("Input option 'orientation' must be provided.")
            return
        }
        implementation!!.lock(orientationType)
        call.resolve()
    }

    @PluginMethod
    fun unlock(call: PluginCall) {
        implementation!!.unlock()
        call.resolve()
    }

    override fun handleOnConfigurationChanged(newConfig: Configuration) {
        super.handleOnConfigurationChanged(newConfig)
        if (implementation!!.hasOrientationChanged(newConfig.orientation)) {
            onOrientationChanged()
        }
    }

    private fun onOrientationChanged() {
        val ret = JSObject()
        val type = implementation?.getCurrentOrientationType()
        ret.put("type", type)
        notifyListeners("screenOrientationChange", ret)
    }
}