package com.archive.test

import net.sf.sevenzipjbinding.ISequentialOutStream
import net.sf.sevenzipjbinding.SevenZip
import net.sf.sevenzipjbinding.SevenZipException
import net.sf.sevenzipjbinding.impl.RandomAccessFileInStream
import java.io.Closeable
import java.io.IOException
import java.io.OutputStream
import java.io.RandomAccessFile
import java.util.function.Consumer

class RarArchive(randomAccessFile: RandomAccessFile) : Closeable {
    private val mArchive = try {
        SevenZip.openInArchive(null, RandomAccessFileInStream(randomAccessFile))
    } catch (e: SevenZipException) {
        throw RarException(e.message, e)
    }

    val fileHeaders = try {
        mArchive.simpleInterface.archiveItems.map {
            it.packedSize
            FileHeader(it.path, it.isFolder, it.size ?: 0L, it.packedSize ?: 0L, it.itemIndex, it.lastWriteTime, it.isEncrypted)
        }
    } catch (e: SevenZipException) {
        throw RarException(e.message, e)
    }

    constructor(srcPath: String) : this(RandomAccessFile(srcPath, "r"))

    fun extractFile(header: FileHeader, outStream: OutputStream, extractedSize: Consumer<Long>) {
        try {
            val index = header.itemIndex
            var total = 0L
            val stream = ISequentialOutStream { data ->
                val size = data.size
                outStream.write(data)
                outStream.flush()
                total += size
                extractedSize.accept(total)
                size
            }
            mArchive.extractSlow(index, stream)
        } catch (e: SevenZipException) {
            throw RarException(e.message, e)
        }
    }

    @Throws(IOException::class)
    override fun close() {
        mArchive.close()
    }
}