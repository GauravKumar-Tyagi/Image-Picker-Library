package com.it.imagePickerLibrary.common

import android.app.Activity
import android.content.Context
import android.os.Environment
import android.util.Log
import com.it.imagePickerLibrary.withoutCompress.ImagePickerDialog
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

object FileCreationUnderAppDirectory {


    @JvmStatic
    @Throws(IOException::class)
    public fun createImageFile(context : Context?): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val imageFileName = "IMG_" + timeStamp + "_"

        var dd : String = getCurrentDate()

        val storageDir: File? =
            context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)

        val subStorageDir: File = File("${storageDir?.absolutePath}${File.separator}${dd}")

        if (!subStorageDir.exists()) {
            subStorageDir.mkdirs()
        }
        Log.e("~~~~~!!!!~!@@#",subStorageDir.absolutePath)

        // delete all old files
        if(storageDir?.listFiles() != null){
            for (file in storageDir?.listFiles()){
                if(file.name.equals(dd)){

                }else{
                    deleteDir(file)
                }
            }
        }
        // delete all old files

        val image = File.createTempFile(imageFileName, ".jpg", subStorageDir)
        return image


    }


    fun deleteDir(dir: File): Boolean {
        if (dir.isDirectory) {
            val children = dir.list()
            for (i in children.indices) {
                val success = deleteDir(File(dir, children[i]))
                if (!success) {
                    return false
                }
            }
        }
        return dir.delete()
    }


    fun getCurrentDate(): String {
        val cal = Calendar.getInstance()
        cal.timeZone = TimeZone.getTimeZone("UTC")
        val dd = cal[Calendar.DAY_OF_MONTH]
        return dd.toString()
    }


}
