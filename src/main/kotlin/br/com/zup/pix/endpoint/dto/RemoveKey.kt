package br.com.zup.pix.endpoint.dto

import br.com.zup.pix.validator.ValidUniqueId
import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.NotBlank

@Introspected
class RemoveKey(

    @field:ValidUniqueId
    @field:NotBlank
    val pixId: String?,

    @field:NotBlank
    val clientId: String?
)