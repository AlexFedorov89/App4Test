package com.fedorov.alex.app4test.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fedorov.alex.app4test.data.model.Record
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.UUID

object Repository {
    private val TAG = Repository::class.java.simpleName

    private val recordsLiveData = MutableLiveData<List<Record>>()
    var records = mutableListOf<Record>()
    private var currentRecord: Record? = null
    private const val fileNameTemplate = "record_"
    private const val dateFormatPattern = "yyyy_MM_dd_HH_mm_ss"

    init {
        recordsLiveData.value = records
    }

    fun getRecords(): LiveData<List<Record>> {
        return recordsLiveData
    }

    fun saveRecord(cachePath: String, storagePath: String) {
        currentRecord?.run {
            try {
                FilesImpl.saveFile(this.filename, cachePath, storagePath)
                records.add(this)
                recordsLiveData.value = records

            } catch (e: IOException) {
                Log.e(TAG, "Failed save file")
            }
        }
    }

    fun deleteRecord(cachePath: String) {
        currentRecord?.run {
            FilesImpl.deleteFile(this.filename, cachePath)
        }
    }

    fun newRecord(path: String): String {
        val fileName = "$fileNameTemplate${SimpleDateFormat(dateFormatPattern).format(
            Date()
        )}"
        currentRecord = Record(
            id = UUID.randomUUID().toString(),
            path = path,
            filename = fileName
        )

        return fileName
    }
}