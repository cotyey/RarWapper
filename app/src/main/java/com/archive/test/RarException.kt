package com.archive.test

import java.io.IOException

class RarException(message: String?, cause: Throwable) : IOException(message, cause)