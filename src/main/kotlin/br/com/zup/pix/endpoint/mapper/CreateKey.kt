package br.com.zup.pix.endpoint.mapper

import br.com.zup.pix.AccountType as ReceiverAccountType
import br.com.zup.pix.KeyType as ReceiverKeyType
import br.com.zup.pix.model.PixKey
import br.com.zup.pix.model.enums.AccountType
import br.com.zup.pix.model.enums.KeyType
import br.com.zup.pix.validator.ValidPixKey
import br.com.zup.pix.validator.ValidUniqueId
import io.micronaut.core.annotation.Introspected
import java.util.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Introspected
@ValidPixKey
data class CreateKey(

    @field:ValidUniqueId
    val clientId: String,
    @field:NotNull
    val type: ReceiverKeyType,
    @field:NotBlank
    val value: String,
    @field:NotNull
    val accountType: ReceiverAccountType
) {

    fun toModel(): PixKey = PixKey(
        clientId = UUID.fromString(clientId),
        keyType = KeyType.valueOf(type.name),
        keyValue = value,
        accountType = AccountType.valueOf(accountType.name)
    )
}