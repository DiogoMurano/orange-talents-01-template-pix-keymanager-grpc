package br.com.zup.pix.exception

import java.lang.RuntimeException

open class CustomException(message: String): RuntimeException(message)