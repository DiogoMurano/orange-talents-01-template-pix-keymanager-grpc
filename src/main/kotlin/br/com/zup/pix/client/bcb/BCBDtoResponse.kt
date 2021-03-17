package br.com.zup.pix.client.bcb

import br.com.zup.pix.endpoint.dto.AccountOwnerResponse
import br.com.zup.pix.endpoint.dto.BankAccountResponse
import br.com.zup.pix.endpoint.dto.PixKeyResponse
import java.time.LocalDateTime

data class CreatePixKeyResponse(
    val keyType: BCBKeyType,
    val key: String,
    val createdAt: LocalDateTime
)

data class DeletePixKeyResponse(
    val key: String,
    val participant: String,
    val deletedAt: LocalDateTime
)

data class BCBPixKeyDetailsResponse(
    val keyType: BCBKeyType,
    val key: String,
    val bankAccount: BCBBankAccountResponse,
    val owner: BCBOwnerResponse,
    val createdAt: LocalDateTime
) {
    fun toPixKey(): PixKeyResponse = PixKeyResponse(
        keyType = keyType.modelKeyType!!,
        keyValue = key,
        bankAccount = bankAccount.toBankAccount(owner.toAccountOwner()),
        createdAt = createdAt,
        id = null
    )
}

data class BCBOwnerResponse(
    val type: OwnerType,
    val name: String,
    val taxIdNumber: String
) {
    fun toAccountOwner(): AccountOwnerResponse = AccountOwnerResponse(
        name = name,
        cpf = taxIdNumber
    )
}

data class BCBBankAccountResponse(
    val participant: String,
    val branch: String,
    val accountNumber: String,
    val accountType: BCPAccountType
) {
    fun toBankAccount(accountOwner: AccountOwnerResponse): BankAccountResponse = BankAccountResponse(
        type = accountType.modelAccountType,
        agency = branch,
        number = accountNumber,
        owner = accountOwner
    )
}