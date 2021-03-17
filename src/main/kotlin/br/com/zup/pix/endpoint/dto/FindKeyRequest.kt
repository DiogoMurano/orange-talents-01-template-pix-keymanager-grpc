package br.com.zup.pix.endpoint.dto

import br.com.zup.pix.validator.ValidUniqueId
import javax.validation.constraints.NotBlank

data class FindByKeyRequest(

    @field:ValidUniqueId
    @NotBlank
    val key: String?
)

data class FindByPixIdRequest(

    @field:ValidUniqueId
    @NotBlank
    val clientId: String?,

    @field:ValidUniqueId
    @NotBlank
    val pixId: String?
)