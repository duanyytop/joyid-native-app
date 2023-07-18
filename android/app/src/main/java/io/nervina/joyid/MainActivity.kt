package io.nervina.joyid

import android.os.Bundle
import com.getcapacitor.BridgeActivity
import io.nervina.joyid.plugins.screenOrientation.ScreenOrientationPlugin

class MainActivity : BridgeActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        registerPlugin(ScreenOrientationPlugin::class.java)
        super.onCreate(savedInstanceState)
    }
}