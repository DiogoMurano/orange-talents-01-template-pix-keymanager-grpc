package br.com.zup.pix.model

import java.util.*
import javax.persistence.Embeddable

@Embeddable
class AccountOwner(
    val clientId: UUID,
    val name: String,
    val cpf: String
)