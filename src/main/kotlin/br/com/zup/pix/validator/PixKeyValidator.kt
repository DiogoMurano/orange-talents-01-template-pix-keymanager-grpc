package br.com.zup.pix.validator

import br.com.zup.pix.KeyType
import br.com.zup.pix.endpoint.CreateKey
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

    override fun isValid(value: CreateKey?, context: ConstraintValidatorContext?): Boolean =
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
        return value.matches("([0-9]{3}[.]?[0-9]{3}[.]?[0-9]{3}-[0-9]{2})|([0-9]{11})".toRegex())
    }

    private fun validateEmail(value: String): Boolean {
        return value.matches("^[a-zA-Z0-9_!#\$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+\$".toRegex(RegexOption.IGNORE_CASE))
    }

    private fun validateTellNumber(value: String): Boolean {
        return value.matches("(\\(?\\d{2}\\)?\\s)?(\\d{4,5}-\\d{4})".toRegex())
    }
}