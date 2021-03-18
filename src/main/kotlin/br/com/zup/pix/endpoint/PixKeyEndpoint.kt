package br.com.zup.pix.endpoint

import br.com.zup.pix.*
import br.com.zup.pix.endpoint.reply.PixKeyResponse
import br.com.zup.pix.endpoint.reply.toFindPixKeyReply
import br.com.zup.pix.endpoint.request.*
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

    override fun create(request: CreatePixKeyRequest, responseObserver: StreamObserver<CreatePixKeyReply>) {

        val createKey = request.toEntryCreateKey()
        val pixKey = service.persistKey(createKey)

        responseObserver.onNext(
            CreatePixKeyReply.newBuilder()
                .setClientId(pixKey.bankAccount.owner.clientId.toString())
                .setPixId(pixKey.id.toString())
                .build()
        )
        responseObserver.onCompleted()
    }

    override fun remove(request: RemovePixKeyRequest, responseObserver: StreamObserver<RemovePixKeyReply>) {

        val removeKey = request.toEntryRemoveKey()
        service.removeKey(removeKey)

        responseObserver.onNext(
            RemovePixKeyReply.newBuilder()
                .setClientId(removeKey.clientId.toString())
                .setPixId(removeKey.pixId.toString())
                .build()
        )
        responseObserver.onCompleted()
    }

    override fun find(request: FindPixKeyRequest, responseObserver: StreamObserver<FindPixKeyReply>) {

        fun buildResponse(key: PixKeyResponse) {
            responseObserver.onNext(key.toFindPixKeyReply())
            responseObserver.onCompleted()
        }

        when (request.filterCase) {
            FindPixKeyRequest.FilterCase.KEY -> buildResponse(service.findByKey(request.toEntryFindByKey()))
            FindPixKeyRequest.FilterCase.PIXID -> buildResponse(service.findByPixId(request.toEntryFindByPixId()))

            FindPixKeyRequest.FilterCase.FILTER_NOT_SET ->
                throw ValidationException("The data entered in the request is insufficient")

            else -> throw InternalException("An internal error has occurred")
        }
    }

}