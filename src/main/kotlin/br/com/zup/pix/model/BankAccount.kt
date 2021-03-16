package br.com.zup.pix.model

import br.com.zup.pix.model.enums.AccountType
import javax.persistence.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@Entity
class BankAccount(

    @field:NotNull
    @Enumerated(EnumType.STRING)
    val type: AccountType,

    @field:NotBlank
    @Size(max = 4)
    val agency: String,

    @field:NotBlank
    val number: String,

    @NotNull
    @Embedded
    val owner: AccountOwner
) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

}