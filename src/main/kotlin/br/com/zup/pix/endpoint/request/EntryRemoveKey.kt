package br.com.zup.pix.endpoint.request

import br.com.zup.pix.RemovePixKeyRequest
import br.com.zup.pix.validator.ValidUniqueId
import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.NotBlank

@Introspected
class EntryRemoveKey(

    @field:ValidUniqueId
    @NotBlank
    val pixId: String?,

    @field:NotBlank
    val clientId: String?
)

fun RemovePixKeyRequest.toEntryRemoveKey(): EntryRemoveKey = EntryRemoveKey(
    clientId = clientId,
    pixId = pixId
)