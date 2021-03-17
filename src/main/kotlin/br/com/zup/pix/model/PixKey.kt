package br.com.zup.pix.model

import br.com.zup.pix.exception.types.InternalException
import br.com.zup.pix.model.enums.KeyType
import java.time.LocalDateTime
import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@Entity
class PixKey(

    @field:NotNull
    @Enumerated(EnumType.STRING)
    val keyType: KeyType,

    @field:NotBlank
    @Size(max = 77)
    var keyValue: String = UUID.randomUUID().toString(),

    @field:NotNull
    @Embedded
    val bankAccount: BankAccount
) {

    @field:Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: UUID? = null

    @NotNull
    val createdAt: LocalDateTime = LocalDateTime.now()

    fun updateKey(keyValue: String) {
        if(keyType != KeyType.RANDOM) {
            throw InternalException("Internal error")
        }
        this.keyValue = keyValue
    }
}