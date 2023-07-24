package dev.joyid.plugins.fido2

import android.util.Base64
import android.util.JsonReader
import com.google.android.gms.fido.fido2.api.common.Attachment
import com.google.android.gms.fido.fido2.api.common.AuthenticatorSelectionCriteria
import com.google.android.gms.fido.fido2.api.common.PublicKeyCredentialCreationOptions
import com.google.android.gms.fido.fido2.api.common.PublicKeyCredentialDescriptor
import com.google.android.gms.fido.fido2.api.common.PublicKeyCredentialParameters
import com.google.android.gms.fido.fido2.api.common.PublicKeyCredentialRpEntity
import com.google.android.gms.fido.fido2.api.common.PublicKeyCredentialType
import com.google.android.gms.fido.fido2.api.common.PublicKeyCredentialUserEntity

fun parsePublicKeyCredentialCreationOptions(
    params: String
): PublicKeyCredentialCreationOptions {
    val builder = PublicKeyCredentialCreationOptions.Builder()
    JsonReader(params.byteInputStream().bufferedReader()).use { reader ->
        reader.beginObject()
        while (reader.hasNext()) {
            when (reader.nextName()) {
                "user" -> builder.setUser(parseUser(reader))
                "challenge" -> builder.setChallenge(reader.nextString().decodeBase64())
                "pubKeyCredParams" -> builder.setParameters(parseParameters(reader))
                "timeout" -> builder.setTimeoutSeconds(reader.nextDouble())
                "attestation" -> reader.skipValue() // Unused
                "excludeCredentials" -> builder.setExcludeList(
                    parseCredentialDescriptors(reader)
                )
                "authenticatorSelection" -> builder.setAuthenticatorSelection(
                    parseSelection(reader)
                )
                "rp" -> builder.setRp(parseRp(reader))
                "extensions" -> reader.skipValue()
            }
        }
        reader.endObject()
    }
    return builder.build()
}

fun parseRp(reader: JsonReader): PublicKeyCredentialRpEntity {
    var id: String? = null
    var name: String? = null
    reader.beginObject()
    while (reader.hasNext()) {
        when (reader.nextName()) {
            "id" -> id = reader.nextString()
            "name" -> name = reader.nextString()
            else -> reader.skipValue()
        }
    }
    reader.endObject()
    return PublicKeyCredentialRpEntity(id!!, name!!, /* icon */ null)
}

fun parseSelection(reader: JsonReader): AuthenticatorSelectionCriteria {
    val builder = AuthenticatorSelectionCriteria.Builder()
    reader.beginObject()
    while (reader.hasNext()) {
        when (reader.nextName()) {
            "authenticatorAttachment" -> builder.setAttachment(
                Attachment.fromString(reader.nextString())
            )
            "userVerification" -> reader.skipValue()
            else -> reader.skipValue()
        }
    }
    reader.endObject()
    return builder.build()
}

fun parseCredentialDescriptors(
    reader: JsonReader
): List<PublicKeyCredentialDescriptor> {
    val list = mutableListOf<PublicKeyCredentialDescriptor>()
    reader.beginArray()
    while (reader.hasNext()) {
        var id: String? = null
        reader.beginObject()
        while (reader.hasNext()) {
            when (reader.nextName()) {
                "id" -> id = reader.nextString()
                "type" -> reader.skipValue()
                "transports" -> reader.skipValue()
                else -> reader.skipValue()
            }
        }
        reader.endObject()
        list.add(
            PublicKeyCredentialDescriptor(
                PublicKeyCredentialType.PUBLIC_KEY.toString(),
                id!!.decodeBase64(),
                /* transports */ null
            )
        )
    }
    reader.endArray()
    return list
}

private fun parseUser(reader: JsonReader): PublicKeyCredentialUserEntity {
    reader.beginObject()
    var id: String? = null
    var name: String? = null
    var displayName = ""
    while (reader.hasNext()) {
        when (reader.nextName()) {
            "id" -> id = reader.nextString()
            "name" -> name = reader.nextString()
            "displayName" -> displayName = reader.nextString()
            else -> reader.skipValue()
        }
    }
    reader.endObject()
    return PublicKeyCredentialUserEntity(
        id!!.decodeBase64(),
        name!!,
        null.toString(), // icon
        displayName
    )
}

private fun parseParameters(reader: JsonReader): List<PublicKeyCredentialParameters> {
    val parameters = mutableListOf<PublicKeyCredentialParameters>()
    reader.beginArray()
    while (reader.hasNext()) {
        reader.beginObject()
        var type: String? = null
        var alg = 0
        while (reader.hasNext()) {
            when (reader.nextName()) {
                "type" -> type = reader.nextString()
                "alg" -> alg = reader.nextInt()
                else -> reader.skipValue()
            }
        }
        reader.endObject()
        parameters.add(PublicKeyCredentialParameters(type!!, alg))
    }
    reader.endArray()
    return parameters
}

private const val BASE64_FLAG = Base64.NO_PADDING or Base64.NO_WRAP or Base64.URL_SAFE

fun ByteArray.toBase64(): String {
    return Base64.encodeToString(this, BASE64_FLAG)
}

fun String.decodeBase64(): ByteArray {
    return Base64.decode(this, BASE64_FLAG)
}
