package br.com.zup.pix.repository

import br.com.zup.pix.model.BankAccount
import io.micronaut.data.annotation.Repository
import io.micronaut.data.repository.CrudRepository

@Repository
interface BankAccountRepository: CrudRepository<BankAccount, Long>