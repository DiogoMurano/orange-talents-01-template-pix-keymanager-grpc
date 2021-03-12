package br.com.zup.pix.model

import br.com.zup.pix.model.enums.AccountType
import br.com.zup.pix.model.enums.KeyType
import br.com.zup.pix.validator.ValidPixKey
import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@Entity
@ValidPixKey
class PixKey(

    @NotNull
    val clientId: UUID,

    @NotNull
    @Enumerated(EnumType.STRING)
    val keyType: KeyType,

    @NotBlank
    @Size(max = 77)
    val keyValue: String = UUID.randomUUID().toString(),

    @NotNull
    @Enumerated(EnumType.STRING)
    val accountType: AccountType
) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

}