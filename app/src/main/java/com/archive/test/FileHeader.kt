package com.archive.test

import java.util.*

data class FileHeader(val fileName: String, val isDirectory: Boolean, val size: Long, val lastModifiedDate: Date, val isEncrypted: Boolean)