package br.com.zup.pix.client

import br.com.zup.pix.client.bcb.CreatePixKeyRequest
import br.com.zup.pix.client.bcb.CreatePixKeyResponse
import br.com.zup.pix.client.bcb.DeletePixKeyRequest
import br.com.zup.pix.client.bcb.DeletePixKeyResponse
import io.micronaut.http.HttpResponse
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Delete
import io.micronaut.http.annotation.Post
import io.micronaut.http.annotation.QueryValue
import io.micronaut.http.client.annotation.Client

@Client("http://localhost:8082")
interface BCBClient {

    @Post(
        value = "/api/v1/pix/keys",
        consumes = [MediaType.APPLICATION_XML],
        produces = [MediaType.APPLICATION_XML]
    )
    fun createKey(@Body request: CreatePixKeyRequest): HttpResponse<CreatePixKeyResponse>

    @Delete(
        value = "/api/v1/pix/keys/{key}",
        consumes = [MediaType.APPLICATION_XML],
        produces = [MediaType.APPLICATION_XML]
    )
    fun deleteKey(@Body request: DeletePixKeyRequest, @QueryValue key: String): HttpResponse<DeletePixKeyResponse>

}