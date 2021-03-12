package br.com.zup.pix.service

import br.com.zup.pix.client.ItauERPClient
import br.com.zup.pix.endpoint.CreateKey
import br.com.zup.pix.exception.AlreadyExistsException
import br.com.zup.pix.exception.NotFoundException
import br.com.zup.pix.model.PixKey
import br.com.zup.pix.repository.KeyRepository
import io.micronaut.validation.Validated
import org.slf4j.LoggerFactory
import javax.inject.Inject
import javax.inject.Singleton
import javax.transaction.Transactional
import javax.validation.Valid

@Validated
@Singleton
class CreateKeyService(
    @Inject private val repository: KeyRepository,
    @Inject private val erpClient: ItauERPClient
) {

    private val logger = LoggerFactory.getLogger(CreateKeyService::class.java)

    @Transactional
    fun persistKey(@Valid createKey: CreateKey): PixKey =
        with(createKey) {
            if (repository.existsByKeyValue(value))
                throw AlreadyExistsException("Key $value is already registered.")

            if (erpClient.getAccount(clientId, accountType.name).body() == null)
                throw NotFoundException("Account id $clientId and type ${accountType.name} was not found")

            toModel()
        }.apply {
            repository.save(this)
            logger.info("Pix key registered successfully")
        }
}