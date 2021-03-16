package br.com.zup.pix.service

import br.com.zup.pix.KeyType
import br.com.zup.pix.client.ItauERPClient
import br.com.zup.pix.endpoint.dto.CreateKey
import br.com.zup.pix.endpoint.dto.RemoveKey
import br.com.zup.pix.exception.types.AlreadyExistsException
import br.com.zup.pix.exception.types.NotFoundException
import br.com.zup.pix.exception.types.PermissionDeniedException
import br.com.zup.pix.model.PixKey
import br.com.zup.pix.repository.BankAccountRepository
import br.com.zup.pix.repository.KeyRepository
import io.micronaut.validation.Validated
import org.slf4j.LoggerFactory
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import javax.transaction.Transactional
import javax.validation.Valid
import javax.validation.ValidationException

@Validated
@Singleton
class CreateKeyService(
    @Inject private val keyRepository: KeyRepository,
    @Inject private val accountRepository: BankAccountRepository,
    @Inject private val erpClient: ItauERPClient
) {

    private val logger = LoggerFactory.getLogger(CreateKeyService::class.java)

    @Transactional
    fun persistKey(@Valid createKey: CreateKey): PixKey {
        val keyValue = createKey.value!!
        val clientId = createKey.clientId!!
        val accountType = createKey.accountType!!

        if (keyRepository.existsByKeyValue(keyValue))
            throw AlreadyExistsException("Key $keyValue is already registered.")

        val body = erpClient.getAccount(clientId, accountType.name).body()
            ?: throw NotFoundException("Account id $clientId and type ${accountType.name} was not found")

        if (createKey.type!! == KeyType.CPF && body.owner.cpf != keyValue)
            throw ValidationException("This CPF does not belong to the informed user.")

        val bankAccount = body.toModel()
        val pixKey = createKey.toModel(bankAccount)

        accountRepository.save(bankAccount)
        keyRepository.save(pixKey)

        logger.info("Pix key registered successfully")

        return pixKey
    }

    @Transactional
    fun removeKey(@Valid removeKey: RemoveKey) {

        val pixId = UUID.fromString(removeKey.pixId!!)
        val clientId = UUID.fromString(removeKey.clientId!!)

        val pixKey =
            keyRepository.findById(pixId).orElseThrow { NotFoundException("The informed pix key was not found.") }

        if (pixKey.clientId != clientId) {
            throw PermissionDeniedException("You don't have permission to remove this pix key.")
        }

        keyRepository.deleteById(pixId)
    }
}