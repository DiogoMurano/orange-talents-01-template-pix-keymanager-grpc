package br.com.zup.pix.validator

import br.com.zup.pix.KeyType
import br.com.zup.pix.endpoint.mapper.CreateKey
import io.micronaut.validation.validator.constraints.EmailValidator
import javax.inject.Singleton
import javax.validation.Constraint
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext
import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.annotation.AnnotationTarget.CLASS

@Target(CLASS)
@Retention(RUNTIME)
@Constraint(validatedBy = [PixKeyValidator::class])
annotation class ValidPixKey(
    val message: String = "The pix key doesn't have a valid value."
)

@Singleton
class PixKeyValidator : ConstraintValidator<ValidPixKey, CreateKey> {

    override fun isValid(value: CreateKey?, context: ConstraintValidatorContext): Boolean =
        with(value) {
            if (this == null) {
                return true
            }

            if (type == KeyType.CPF) {
                return validateCpf(this.value)
            }

            if (type == KeyType.EMAIL) {
                return validateEmail(this.value, context)
            }

            if (type == KeyType.TELL_NUMBER) {
                return validateTellNumber(this.value)
            }
            false
        }

    private fun validateCpf(value: String): Boolean {
        return value.matches("^[0-9]{11}\$".toRegex())
    }

    private fun validateEmail(value: String, context: ConstraintValidatorContext): Boolean {
        val validator = EmailValidator()
        return validator.isValid(value, context)
    }

    private fun validateTellNumber(value: String): Boolean {
        return value.matches("^\\+[1-9][0-9]\\d{1,14}\$".toRegex())
    }
}