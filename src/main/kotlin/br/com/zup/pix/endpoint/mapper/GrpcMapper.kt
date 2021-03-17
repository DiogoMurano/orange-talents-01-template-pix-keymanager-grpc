package br.com.zup.pix.endpoint.mapper

import br.com.zup.pix.*
import br.com.zup.pix.FindPixKeyResponse.PixKey.BankAccount
import br.com.zup.pix.FindPixKeyResponse.PixKey.BankAccount.AccountOwner
import br.com.zup.pix.endpoint.dto.*
import br.com.zup.pix.model.PixKey
import com.google.protobuf.Timestamp
import java.time.ZoneId

fun CreatePixKeyRequest.toModel(): CreateKeyRequest = CreateKeyRequest(
    clientId = clientId,
    type = keyType,
    value = keyValue,
    accountType = accountType
)

fun RemovePixKeyMessage.toModel(): RemoveKeyRequest = RemoveKeyRequest(
    clientId = clientId,
    pixId = pixId
)

fun FindPixKeyRequest.toFindByKeyModel(): FindByKeyRequest =
    FindByKeyRequest(
        key = key
    )

fun FindPixKeyRequest.toFindByPixIdModel(): FindByPixIdRequest =
    FindByPixIdRequest(
        pixId = pixId.pixId,
        clientId = pixId.clientId
    )

fun PixKey.toPixKeyResponse(): PixKeyResponse {
    val owner = bankAccount.owner
    return PixKeyResponse(
        keyType,
        keyValue,
        BankAccountResponse(
            bankAccount.type,
            bankAccount.agency,
            bankAccount.number,
            AccountOwnerResponse(
                owner.clientId.toString(),
                owner.name,
                owner.cpf
            )
        ),
        id.toString(),
        createdAt
    )
}

fun PixKeyResponse.toFindPixKeyResponse(): FindPixKeyResponse {
    val owner = bankAccount.owner
    return FindPixKeyResponse.newBuilder()
        .setClientId(owner.clientId ?: "")
        .setPixId(id ?: "")
        .setKey(
            FindPixKeyResponse.PixKey.newBuilder()
                .setKeyType(KeyType.valueOf(keyType.name))
                .setKeyValue(keyValue)
                .setCreatedAt(createdAt.let {
                    val createdAt = it.atZone(ZoneId.of("UTC")).toInstant()
                    Timestamp.newBuilder()
                        .setSeconds(createdAt.epochSecond)
                        .setNanos(createdAt.nano)
                })
                .setAccount(
                    BankAccount.newBuilder()
                        .setAccountType(AccountType.valueOf(bankAccount.type.name))
                        .setInstitution("ITAÚ UNIBANCO S.A.")
                        .setAgency(bankAccount.agency)
                        .setNumber(bankAccount.number)
                        .setOwner(
                            AccountOwner.newBuilder()
                                .setName(owner.name)
                                .setCpf(owner.cpf)
                                .build()
                        )
                        .build()
                )
                .build()
        )
        .build()
}