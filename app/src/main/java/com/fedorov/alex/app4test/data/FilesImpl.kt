package com.fedorov.alex.app4test.data

import java.io.File

object FilesImpl {
    fun saveFile(fileName:String, cachePath: String, storagePath:String){
            val fileSource = File(cachePath + fileName)
            val fileDest = File(storagePath + fileName)

            fileSource.copyTo(fileDest, true)
            deleteFile(fileSource)
        }

    fun deleteFile(fileName:String, cachePath: String){
        deleteFile(File(cachePath + fileName))
    }

    private fun deleteFile(f:File){
        f.delete()
    }
}