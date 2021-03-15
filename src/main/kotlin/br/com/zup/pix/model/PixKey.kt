package br.com.zup.pix.model

import br.com.zup.pix.model.enums.AccountType
import br.com.zup.pix.model.enums.KeyType
import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@Entity
class PixKey(

    @field:NotNull
    val clientId: UUID,

    @field:NotNull
    @Enumerated(EnumType.STRING)
    val keyType: KeyType,

    @field:NotBlank
    @Size(max = 77)
    val keyValue: String = UUID.randomUUID().toString(),

    @field:NotNull
    @Enumerated(EnumType.STRING)
    val accountType: AccountType
) {

    @field:Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: UUID? = null

}