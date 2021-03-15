package br.com.zup.pix.service

import br.com.zup.pix.client.ItauERPClient
import br.com.zup.pix.endpoint.mapper.CreateKey
import br.com.zup.pix.exception.types.AlreadyExistsException
import br.com.zup.pix.exception.types.NotFoundException
import br.com.zup.pix.model.PixKey
import br.com.zup.pix.model.enums.KeyType
import br.com.zup.pix.repository.KeyRepository
import io.micronaut.validation.Validated
import org.slf4j.LoggerFactory
import javax.inject.Inject
import javax.inject.Singleton
import javax.transaction.Transactional
import javax.validation.Valid
import javax.validation.ValidationException

@Validated
@Singleton
class CreateKeyService(
    @Inject private val repository: KeyRepository,
    @Inject private val erpClient: ItauERPClient
) {

    private val logger = LoggerFactory.getLogger(CreateKeyService::class.java)

    @Transactional
    fun persistKey(@Valid createKey: CreateKey): PixKey {
        val pixKey = createKey.toModel()

        if (repository.existsByKeyValue(pixKey.keyValue))
            throw AlreadyExistsException("Key ${pixKey.keyValue} is already registered.")

        val body = erpClient.getAccount(pixKey.clientId.toString(), pixKey.accountType.name).body()
            ?: throw NotFoundException("Account id ${pixKey.clientId} and type ${pixKey.accountType.name} was not found")

        if (pixKey.keyType == KeyType.CPF && body.owner.cpf != pixKey.keyValue)
            throw ValidationException("This CPF does not belong to the informed user.")

        repository.save(pixKey)
        logger.info("Pix key registered successfully")

        return pixKey
    }
}