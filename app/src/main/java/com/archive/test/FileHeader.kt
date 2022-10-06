package com.archive.test

import java.util.Date

data class FileHeader(
        var fileName: String?,
        val isDirectory: Boolean,
        val unpSize: Long,
        val packedSize: Long,
        val itemIndex: Int,
        val MTime: Date?,
        val isEncrypted: Boolean
    )