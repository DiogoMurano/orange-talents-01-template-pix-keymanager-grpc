package br.com.zup.pix.endpoint

import br.com.zup.pix.*
import br.com.zup.pix.endpoint.dto.PixKeyResponse
import br.com.zup.pix.endpoint.mapper.toFindByKeyModel
import br.com.zup.pix.endpoint.mapper.toFindByPixIdModel
import br.com.zup.pix.endpoint.mapper.toFindPixKeyResponse
import br.com.zup.pix.endpoint.mapper.toModel
import br.com.zup.pix.exception.ErrorHandler
import br.com.zup.pix.exception.types.InternalException
import br.com.zup.pix.exception.types.ValidationException
import br.com.zup.pix.service.KeyService
import io.grpc.stub.StreamObserver
import javax.inject.Inject
import javax.inject.Singleton

@ErrorHandler
@Singleton
class PixKeyEndpoint(
    @Inject private val service: KeyService
) : KeyManagerServiceGrpc.KeyManagerServiceImplBase() {

    override fun create(request: CreatePixKeyRequest, responseObserver: StreamObserver<CreatePixKeyResponse>) {

        val createKey = request.toModel()
        val pixKey = service.persistKey(createKey)

        responseObserver.onNext(
            CreatePixKeyResponse.newBuilder()
                .setClientId(pixKey.bankAccount.owner.clientId.toString())
                .setPixId(pixKey.id.toString())
                .build()
        )
        responseObserver.onCompleted()
    }

    override fun remove(request: RemovePixKeyMessage, responseObserver: StreamObserver<RemovePixKeyMessage>) {

        val removeKey = request.toModel()
        service.removeKey(removeKey)

        responseObserver.onNext(
            RemovePixKeyMessage.newBuilder()
                .setClientId(removeKey.clientId.toString())
                .setPixId(removeKey.pixId.toString())
                .build()
        )
        responseObserver.onCompleted()
    }

    override fun find(request: FindPixKeyRequest, responseObserver: StreamObserver<FindPixKeyResponse>) {

        fun buildResponse(key: PixKeyResponse) {
            responseObserver.onNext(key.toFindPixKeyResponse())
            responseObserver.onCompleted()
        }

        when (request.filterCase) {
            FindPixKeyRequest.FilterCase.KEY -> buildResponse(service.findByKey(request.toFindByKeyModel()))
            FindPixKeyRequest.FilterCase.PIXID -> buildResponse(service.findByPixId(request.toFindByPixIdModel()))

            FindPixKeyRequest.FilterCase.FILTER_NOT_SET ->
                throw ValidationException("The data entered in the request is insufficient")

            else -> throw InternalException("An internal error has occurred")
        }

        super.find(request, responseObserver)
    }

}