package br.com.zup.pix.endpoint.request

import br.com.zup.pix.FindPixKeyRequest
import br.com.zup.pix.validator.ValidUniqueId
import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.NotBlank

@Introspected
data class EntryFindByKey(

    @NotBlank
    val key: String?
)

fun FindPixKeyRequest.toEntryFindByKey(): EntryFindByKey = EntryFindByKey(
    key = key
)

@Introspected
data class EntryFindByPixId(

    @field:ValidUniqueId
    @NotBlank
    val clientId: String?,

    @field:ValidUniqueId
    @NotBlank
    val pixId: String?
)

fun FindPixKeyRequest.toEntryFindByPixId(): EntryFindByPixId = EntryFindByPixId(
    pixId = pixId.pixId,
    clientId = pixId.clientId
)