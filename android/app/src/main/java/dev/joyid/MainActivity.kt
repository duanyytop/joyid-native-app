package dev.joyid

import android.os.Bundle
import com.getcapacitor.BridgeActivity
import dev.joyid.plugins.fido2.FidoPlugin

class MainActivity : BridgeActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        registerPlugin(FidoPlugin::class.java)
        super.onCreate(savedInstanceState)
    }
}