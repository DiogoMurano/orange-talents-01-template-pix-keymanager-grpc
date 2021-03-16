package br.com.zup.pix.client.bcb

import br.com.zup.pix.model.AccountOwner
import br.com.zup.pix.model.BankAccount
import br.com.zup.pix.model.PixKey
import br.com.zup.pix.model.enums.AccountType
import br.com.zup.pix.model.enums.KeyType
import java.time.LocalDateTime

private val itauISPB: String = "60701190"

data class CreatePixKeyRequest(
    val keyType: BCBKeyType,
    val key: String,
    val bankAccount: BankAccountRequest,
    val owner: OwnerRequest
) {
    companion object {
        fun byPixKey(pixKey: PixKey): CreatePixKeyRequest =
            CreatePixKeyRequest(
                keyType = BCBKeyType.findByKeyType(pixKey.keyType),
                key = pixKey.keyValue,
                bankAccount = BankAccountRequest.byBankAccount(pixKey.bankAccount),
                owner = OwnerRequest.byOwner(pixKey.bankAccount.owner)
            )
    }
}

data class CreatePixKeyResponse(
    val keyType: BCBKeyType,
    val key: String,
    val createdAt: LocalDateTime
)

data class BankAccountRequest(
    val participant: String,
    val branch: String,
    val accountNumber: String,
    val accountType: BCPAccountType
) {
    companion object {
        fun byBankAccount(account: BankAccount): BankAccountRequest =
            BankAccountRequest(
                participant = itauISPB,
                branch = account.agency,
                accountNumber = account.number,
                accountType = BCPAccountType.findByAccountType(account.type)
            )
    }
}

data class OwnerRequest(
    val type: OwnerType = OwnerType.NATURAL_PERSON,
    val name: String,
    val taxIdNumber: String
) {
    companion object {
        fun byOwner(owner: AccountOwner): OwnerRequest =
            OwnerRequest(
                name = owner.name,
                taxIdNumber = owner.cpf
            )
    }
}

data class DeletePixKeyRequest(
    val key: String,
    val participant: String = itauISPB
)

data class DeletePixKeyResponse(
    val key: String,
    val participant: String,
    val deletedAt: LocalDateTime
)

enum class BCBKeyType(val keyType: KeyType?) {
    CPF(KeyType.CPF),
    CNPJ(null),
    PHONE(KeyType.TELL_NUMBER),
    EMAIL(KeyType.EMAIL),
    RANDOM(KeyType.RANDOM);

    companion object {
        fun findByKeyType(type: KeyType): BCBKeyType {
            for (value in values()) {
                if (value.keyType != null && value.keyType == type) {
                    return value
                }
            }
            return RANDOM
        }
    }
}

enum class BCPAccountType(val accountType: AccountType) {
    CACC(AccountType.CONTA_CORRENTE),
    SVGS(AccountType.CONTA_POUPANCA);

    companion object {
        fun findByAccountType(type: AccountType): BCPAccountType {
            for (value in values()) {
                if (value.accountType == type) {
                    return value
                }
            }

            return CACC
        }
    }
}

enum class OwnerType {
    NATURAL_PERSON,
    LEGAL_PERSON
}