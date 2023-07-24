package dev.joyid.plugins.fido2

import android.content.res.Configuration
import com.getcapacitor.JSObject
import com.getcapacitor.Plugin
import com.getcapacitor.PluginCall
import com.getcapacitor.PluginMethod
import com.getcapacitor.annotation.CapacitorPlugin
import com.getcapacitor.annotation.Permission
import com.google.android.gms.fido.Fido
import android.Manifest;

@CapacitorPlugin(
    name = "Fido",
    permissions = [
        Permission(
            alias = "internet",
            strings = [ Manifest.permission.INTERNET, Manifest.permission.ACCESS_NETWORK_STATE ]
        )
    ]
)
class FidoPlugin : Plugin() {
    private var fido: FidoInstance? = null
    override fun load() {
        fido = FidoInstance(activity)
    }

    override fun handleOnResume() {
        super.handleOnResume()
        fido?.setFido2ApiClient(Fido.getFido2ApiClient(bridge.activity))
    }

    override fun handleOnPause() {
        super.handleOnPause()
        fido?.setFido2ApiClient(null)
    }

    @PluginMethod
    fun register(call: PluginCall) {
        val params = call.getString("params")
        fido?.register(params)
    }

    override fun handleOnConfigurationChanged(newConfig: Configuration?) {
        super.handleOnConfigurationChanged(newConfig)
        val credential = fido?.registerResult()
        if (credential != null) {
            this.onCredentialValid(credential)
        }
    }

    private fun onCredentialValid(credential: String) {
        val ret = JSObject()
        ret.put("credential", credential)
        notifyListeners("credentialValid", ret)
    }
}