package com.archive.test

import net.sf.sevenzipjbinding.SevenZip
import net.sf.sevenzipjbinding.impl.RandomAccessFileInStream
import java.io.RandomAccessFile

class RarArchive(srcFile: RandomAccessFile) {
    private val mArchive = SevenZip.openInArchive(null, RandomAccessFileInStream(srcFile))
    val fileHeaders = mArchive.simpleInterface.archiveItems.map {
        FileHeader(it.path, it.isFolder, it.size ?: 0, it.lastWriteTime, it.isEncrypted)
    }

    constructor(srcPath: String) : this(RandomAccessFile(srcPath, "r"))
}