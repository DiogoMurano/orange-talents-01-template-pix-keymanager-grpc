package br.com.zup.pix.service

import br.com.zup.pix.client.BCBClient
import br.com.zup.pix.client.ItauERPClient
import br.com.zup.pix.client.bcb.BCBCreatePixKeyRequest
import br.com.zup.pix.client.bcb.BCBDeletePixKeyRequest
import br.com.zup.pix.endpoint.dto.*
import br.com.zup.pix.endpoint.mapper.toPixKeyResponse
import br.com.zup.pix.exception.types.AlreadyExistsException
import br.com.zup.pix.exception.types.InternalException
import br.com.zup.pix.exception.types.NotFoundException
import br.com.zup.pix.exception.types.PermissionDeniedException
import br.com.zup.pix.model.PixKey
import br.com.zup.pix.model.enums.KeyType
import br.com.zup.pix.repository.KeyRepository
import io.micronaut.http.HttpStatus
import io.micronaut.validation.Validated
import org.slf4j.LoggerFactory
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import javax.transaction.Transactional
import javax.validation.Valid
import javax.validation.ValidationException
import br.com.zup.pix.KeyType as ReceiverKeyType

@Validated
@Singleton
class KeyService(
    @Inject private val keyRepository: KeyRepository,
    @Inject private val erpClient: ItauERPClient,
    @Inject private val bcbClient: BCBClient
) {

    private val logger = LoggerFactory.getLogger(KeyService::class.java)

    @Transactional
    fun persistKey(@Valid createKeyRequest: CreateKeyRequest): PixKey {
        val keyValue = createKeyRequest.value!!
        val clientId = createKeyRequest.clientId!!
        val accountType = createKeyRequest.accountType!!

        if (keyRepository.existsByKeyValue(keyValue))
            throw AlreadyExistsException("Key $keyValue is already registered.")

        val body = erpClient.getAccount(clientId, accountType.name).body()
            ?: throw NotFoundException("Account id $clientId and type ${accountType.name} was not found")

        if (createKeyRequest.type!! == ReceiverKeyType.CPF && body.owner.cpf != keyValue)
            throw ValidationException("This CPF does not belong to the informed user.")

        val bankAccount = body.toModel()
        val pixKey = createKeyRequest.toModel(bankAccount)

        keyRepository.save(pixKey)

        val bcbResponse = bcbClient.createKey(BCBCreatePixKeyRequest.byPixKey(pixKey))
        if (bcbResponse.status != HttpStatus.CREATED) {
            throw InternalException("We had a problem registering your key")
        }

        logger.info("Pix key registered successfully.")

        if (pixKey.keyType == KeyType.RANDOM) {
            pixKey.updateKey(bcbResponse.body()!!.key)
        }

        return pixKey
    }

    @Transactional
    fun removeKey(@Valid removeKeyRequest: RemoveKeyRequest) {

        val pixId = UUID.fromString(removeKeyRequest.pixId!!)
        val clientId = UUID.fromString(removeKeyRequest.clientId!!)

        val pixKey =
            keyRepository.findById(pixId)
                .orElseThrow { NotFoundException("The informed pix key was not found.") }

        if (pixKey.bankAccount.owner.clientId != clientId) {
            throw PermissionDeniedException("You don't have permission to remove this pix key.")
        }

        val bcbResponse = bcbClient.deleteKey(BCBDeletePixKeyRequest(pixKey.keyValue), pixKey.keyValue)
        if (bcbResponse.status() != HttpStatus.OK) {
            throw InternalException("We had a problem deleting your key")
        }

        keyRepository.deleteById(pixId).also {
            logger.info("Key successfully deleted")
        }
    }

    @Transactional
    fun findByKey(@Valid findRequest: FindByKeyRequest): PixKeyResponse {
        val key: String = findRequest.key!!

        val optional = keyRepository.findByKeyValue(key)
        if (optional.isEmpty) {
            val bcbResponse = bcbClient.findKey(key)
            if (bcbResponse.status != HttpStatus.OK) {
                return bcbResponse.body()!!.toPixKey()
            }

            throw NotFoundException("Key not found.")
        }

        return optional.get().toPixKeyResponse()
    }

    fun findByPixId(findRequest: FindByPixIdRequest): PixKeyResponse {
        val key = keyRepository.findById(UUID.fromString(findRequest.pixId))
            .orElseThrow { throw NotFoundException("Key not found.") }

        if (key.bankAccount.owner.clientId.toString() != findRequest.clientId)
            throw PermissionDeniedException("You do not have permission to interact with this key.")

        return key.toPixKeyResponse()
    }
}