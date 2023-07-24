package dev.joyid.plugins.fido2

import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.fido.Fido
import com.google.android.gms.fido.fido2.Fido2ApiClient
import com.google.android.gms.fido.fido2.api.common.AuthenticatorErrorResponse
import com.google.android.gms.fido.fido2.api.common.PublicKeyCredential
import com.google.android.gms.tasks.Tasks
import com.google.gson.Gson
import kotlin.Exception


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
        this.fido2ApiClient?.let { client ->
            try {
                val options = parsePublicKeyCredentialCreationOptions(params!!)
                val task = client.getRegisterPendingIntent(options)
                val intent = Tasks.await(task)
                createCredentialIntentLauncher.launch(
                    IntentSenderRequest.Builder(intent).build()
                )
            } catch (e: Exception) {
                Log.e("FIDO", "fido exception", e)
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
                if (credential.response is AuthenticatorErrorResponse) {
                    Toast.makeText(activity, (credential.response as AuthenticatorErrorResponse).errorMessage!!, Toast.LENGTH_LONG).show()
                } else {
                    credentialRet = gson.toJson(credential)
                    Toast.makeText(activity, credentialRet!!, Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}