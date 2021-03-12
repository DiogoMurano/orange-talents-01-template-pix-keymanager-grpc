package br.com.zup.pix.validator

import java.util.*
import javax.inject.Singleton
import javax.validation.Constraint
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext
import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.annotation.AnnotationTarget.FIELD

@Target(FIELD)
@Retention(RUNTIME)
@Constraint(validatedBy = [UniqueIdValidator::class])
annotation class ValidUniqueId(
    val message: String = "The UUID value has an invalid format"
)

@Singleton
class UniqueIdValidator : ConstraintValidator<ValidUniqueId, String> {

    override fun isValid(value: String?, context: ConstraintValidatorContext?): Boolean =
        with(value) {

            if (this == null) {
                return true
            }

            false
        }
}