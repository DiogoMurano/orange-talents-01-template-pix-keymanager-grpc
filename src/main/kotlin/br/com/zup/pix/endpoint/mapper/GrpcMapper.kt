package br.com.zup.pix.endpoint.mapper

import br.com.zup.pix.CreatePixKeyRequest

fun CreatePixKeyRequest.toModel(): CreateKey = CreateKey(
    clientId = clientId,
    type = keyType,
    value = keyValue,
    accountType = accountType
)