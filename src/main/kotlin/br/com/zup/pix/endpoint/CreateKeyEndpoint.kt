package br.com.zup.pix.endpoint

import br.com.zup.pix.CreatePixKeyRequest
import br.com.zup.pix.CreatePixKeyResponse
import br.com.zup.pix.KeyManagerServiceGrpc
import br.com.zup.pix.RemovePixKeyMessage
import br.com.zup.pix.endpoint.mapper.toModel
import br.com.zup.pix.exception.ErrorHandler
import br.com.zup.pix.service.CreateKeyService
import io.grpc.stub.StreamObserver
import javax.inject.Inject
import javax.inject.Singleton

@ErrorHandler
@Singleton
class CreateKeyEndpoint(
    @Inject private val service: CreateKeyService
) : KeyManagerServiceGrpc.KeyManagerServiceImplBase() {

    override fun create(request: CreatePixKeyRequest, responseObserver: StreamObserver<CreatePixKeyResponse>) {

        val createKey = request.toModel()
        val pixKey = service.persistKey(createKey)

        responseObserver.onNext(
            CreatePixKeyResponse.newBuilder()
                .setClientId(pixKey.clientId.toString())
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

}