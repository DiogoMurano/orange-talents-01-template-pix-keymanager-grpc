package br.com.zup.pix.validator

import br.com.zup.pix.AccountType
import br.com.zup.pix.KeyType
import br.com.zup.pix.endpoint.CreateKey
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.util.*

@MicronautTest
class CustomValidatorsTest {

    @Test
    fun `tests whether the validator intercepts invalid uuid`() {
        val validator = UniqueIdValidator()
        assertFalse(validator.isValid("test", null))
    }

    @Test
    fun `tests whether the validator doesn't intercepts valid uuid`() {
        val validator = UniqueIdValidator()
        assertFalse(validator.isValid(UUID.randomUUID().toString(), null))
    }

    @Test
    fun `tests whether the validator doesn't intercepts valid key value - tell number`() {
        val createKey = CreateKey(
            clientId = UUID.randomUUID().toString(),
            type = KeyType.TELL_NUMBER,
            value = "(11) 99999-9999",
            accountType = AccountType.CONTA_CORRENTE
        )

        val validator = PixKeyValidator()
        assertTrue(validator.isValid(createKey, null))
    }

    @Test
    fun `tests whether the validator intercepts invalid key value - tell number`() {
        val createKey = CreateKey(
            clientId = UUID.randomUUID().toString(),
            type = KeyType.TELL_NUMBER,
            value = "444.444.444-44",
            accountType = AccountType.CONTA_CORRENTE
        )

        val validator = PixKeyValidator()
        assertFalse(validator.isValid(createKey, null))
    }

    @Test
    fun `tests whether the validator doesn't intercepts valid key value - CPF`() {
        val createKey = CreateKey(
            clientId = UUID.randomUUID().toString(),
            type = KeyType.CPF,
            value = "444.444.444-44",
            accountType = AccountType.CONTA_CORRENTE
        )

        val validator = PixKeyValidator()
        assertTrue(validator.isValid(createKey, null))
    }

    @Test
    fun `tests whether the validator intercepts invalid key value - CPF`() {
        val createKey = CreateKey(
            clientId = UUID.randomUUID().toString(),
            type = KeyType.CPF,
            value = "(11) 99999-9999",
            accountType = AccountType.CONTA_CORRENTE
        )

        val validator = PixKeyValidator()
        assertFalse(validator.isValid(createKey, null))
    }

    @Test
    fun `tests whether the validator doesn't intercepts valid key value - Email`() {
        val createKey = CreateKey(
            clientId = UUID.randomUUID().toString(),
            type = KeyType.EMAIL,
            value = "test@gmail.com",
            accountType = AccountType.CONTA_CORRENTE
        )

        val validator = PixKeyValidator()
        assertTrue(validator.isValid(createKey, null))
    }

    @Test
    fun `tests whether the validator intercepts invalid key value - Email`() {
        val createKey = CreateKey(
            clientId = UUID.randomUUID().toString(),
            type = KeyType.EMAIL,
            value = "(11) 99999-9999",
            accountType = AccountType.CONTA_CORRENTE
        )

        val validator = PixKeyValidator()
        assertFalse(validator.isValid(createKey, null))
    }

}