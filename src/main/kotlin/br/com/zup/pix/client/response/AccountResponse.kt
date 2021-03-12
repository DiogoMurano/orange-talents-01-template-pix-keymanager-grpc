package br.com.zup.pix.client.response

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

data class AccountResponse(

    @JsonProperty("tipo")
    val type: String,

    @JsonProperty("instituicao")
    val institution: InstitutionResponse,

    @JsonProperty("agencia")
    val agency: String,

    @JsonProperty("numero")
    val number: String,

    @JsonProperty("titular")
    val owner: OwnerResponse
)

data class InstitutionResponse(

    @JsonProperty("nome")
    val name: String,

    @JsonProperty("ispb")
    val ispb: String
)

data class OwnerResponse(

    @JsonProperty("id")
    val id: UUID,

    @JsonProperty("nome")
    val name: String,

    @JsonProperty("cpf")
    val cpf: String
)