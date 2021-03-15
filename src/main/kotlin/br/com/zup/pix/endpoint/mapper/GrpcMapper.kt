package br.com.zup.pix.endpoint.mapper

import br.com.zup.pix.CreatePixKeyRequest
import br.com.zup.pix.RemovePixKeyMessage
import br.com.zup.pix.endpoint.dto.CreateKey
import br.com.zup.pix.endpoint.dto.RemoveKey

fun CreatePixKeyRequest.toModel(): CreateKey = CreateKey(
    clientId = clientId,
    type = keyType,
    value = keyValue,
    accountType = accountType
)

fun RemovePixKeyMessage.toModel(): RemoveKey = RemoveKey(
    clientId = clientId,
    pixId = pixId
)