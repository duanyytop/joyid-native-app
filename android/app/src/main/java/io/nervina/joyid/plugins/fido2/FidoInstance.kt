package io.nervina.joyid.plugins.fido2

import android.app.Activity
import android.app.PendingIntent
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.fido.Fido
import com.google.android.gms.fido.fido2.Fido2ApiClient
import com.google.android.gms.fido.fido2.api.common.AuthenticatorErrorResponse
import com.google.android.gms.fido.fido2.api.common.PublicKeyCredential
import com.google.android.gms.fido.fido2.api.common.PublicKeyCredentialCreationOptions
import com.google.gson.Gson


class FidoInstance(private val activity: AppCompatActivity) {
    private var fido2ApiClient: Fido2ApiClient? = null
    private val gson = Gson()
    private val createCredentialIntentLauncher = activity.registerForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult(),
        ::handleRegisterResult
    )
    private var credentialRet: String? = null
    fun setFido2ApiClient(client: Fido2ApiClient?) {
        this.fido2ApiClient = client
    }

    fun register(params: String?) {
        val options = gson.fromJson(params, PublicKeyCredentialCreationOptions::class.java)
        this.fido2ApiClient?.let { client ->
            val task = client.getRegisterPendingIntent(options)
            task.addOnCompleteListener {
                if (it.isSuccessful) {
                    val intent = it.result
                    Log.i("FIDO", intent.toString())
                    createCredentialIntentLauncher.launch(
                        IntentSenderRequest.Builder(intent).build()
                    )
                } else {
                    Log.e("FIDO", "fido exception", it.exception)
                }
            }
        }
    }

    fun registerResult(): String? {
        return credentialRet
    }

    private fun handleRegisterResult(activityResult: ActivityResult) {
        val bytes = activityResult.data?.getByteArrayExtra(Fido.FIDO2_KEY_CREDENTIAL_EXTRA)
        when {
            activityResult.resultCode != Activity.RESULT_OK || bytes == null -> {
                credentialRet = "error"
            }
            else -> {
                val credential = PublicKeyCredential.deserializeFromBytes(bytes)
                if (credential.response !is AuthenticatorErrorResponse) {
                    credentialRet = gson.toJson(credential)
                }
            }
        }
    }
}