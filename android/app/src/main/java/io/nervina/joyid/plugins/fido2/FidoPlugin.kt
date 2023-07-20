package io.nervina.joyid.plugins.fido2

import android.content.res.Configuration
import androidx.lifecycle.lifecycleScope
import com.getcapacitor.JSObject
import com.getcapacitor.Plugin
import com.getcapacitor.PluginCall
import com.getcapacitor.PluginMethod
import com.getcapacitor.annotation.CapacitorPlugin
import com.google.android.gms.fido.Fido
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@CapacitorPlugin(name = "Fido")
class FidoPlugin : Plugin() {

    private var fido: FidoInstance? = null
    override fun load() {
        fido = FidoInstance(activity)
        fido?.setFido2ApiClient(Fido.getFido2ApiClient(bridge.activity))
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