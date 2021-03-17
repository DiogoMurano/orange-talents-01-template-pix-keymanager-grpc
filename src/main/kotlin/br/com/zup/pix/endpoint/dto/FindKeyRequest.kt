package br.com.zup.pix.endpoint.dto

import br.com.zup.pix.validator.ValidUniqueId
import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.NotBlank

@Introspected
data class FindByKeyRequest(

    @NotBlank
    val key: String?
)

@Introspected
data class FindByPixIdRequest(

    @field:ValidUniqueId
    @NotBlank
    val clientId: String?,

    @field:ValidUniqueId
    @NotBlank
    val pixId: String?
)