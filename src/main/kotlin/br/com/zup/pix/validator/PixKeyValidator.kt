package br.com.zup.pix.validator

import br.com.zup.pix.KeyType
import br.com.zup.pix.endpoint.mapper.CreateKey
import io.micronaut.core.annotation.AnnotationValue
import io.micronaut.validation.validator.constraints.ConstraintValidator
import io.micronaut.validation.validator.constraints.ConstraintValidatorContext
import io.micronaut.validation.validator.constraints.EmailValidator
import javax.inject.Singleton
import javax.validation.Constraint
import javax.validation.Payload
import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.annotation.AnnotationTarget.CLASS
import kotlin.annotation.AnnotationTarget.TYPE
import kotlin.reflect.KClass

@MustBeDocumented
@Target(CLASS, TYPE)
@Retention(RUNTIME)
@Constraint(validatedBy = [PixKeyValidator::class])
annotation class ValidPixKey(
    val message: String = "The pix key doesn't have a valid value.",
    val groups: Array<KClass<Any>> = [],
    val payload: Array<KClass<Payload>> = []
)

@Singleton
class PixKeyValidator : ConstraintValidator<ValidPixKey, CreateKey> {

    override fun isValid(
        value: CreateKey?,
        annotationMetadata: AnnotationValue<ValidPixKey>,
        context: ConstraintValidatorContext
    ): Boolean =
        with(value) {
            if (this == null) {
                return true
            }

            if (type == KeyType.CPF) {
                return validateCpf(this.value)
            }

            if (type == KeyType.EMAIL) {
                return validateEmail(this.value)
            }

            if (type == KeyType.TELL_NUMBER) {
                return validateTellNumber(this.value)
            }
            false
        }

    private fun validateCpf(value: String): Boolean {
        return value.matches("^[0-9]{11}\$".toRegex())
    }

    private fun validateEmail(value: String): Boolean {
        return EmailValidator().run {
            initialize(null)
            isValid(value, null)
        }
    }

    private fun validateTellNumber(value: String): Boolean {
        return value.matches("^\\+[1-9][0-9]\\d{1,14}\$".toRegex())
    }
}