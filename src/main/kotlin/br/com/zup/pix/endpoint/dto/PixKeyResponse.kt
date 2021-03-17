package br.com.zup.pix.endpoint.dto

import br.com.zup.pix.model.enums.AccountType
import br.com.zup.pix.model.enums.KeyType
import java.time.LocalDateTime
import java.util.*

data class PixKeyResponse(
    val keyType: KeyType,
    val keyValue: String,
    val bankAccount: BankAccountResponse,
    val id: String?,
    val createdAt: LocalDateTime
)

data class BankAccountResponse(
    val type: AccountType,
    val agency: String,
    val number: String,
    val owner: AccountOwnerResponse
)

data class AccountOwnerResponse(
    val clientId: String? = null,
    val name: String,
    val cpf: String
)