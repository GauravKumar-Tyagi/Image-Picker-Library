package com.it.imagePickerLibrary.withCompress


import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import androidx.lifecycle.lifecycleScope
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat

import com.it.imagePickerLibrary.common.ImagePickerConstants
import com.it.imagePickerLibrary.R
import com.it.imagePickerLibrary.common.FileCreationUnderAppDirectory
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.*
import kotlinx.coroutines.launch
import java.io.File
import java.lang.ref.WeakReference


class ImagePickerActivityWithCompressor : AppCompatActivity() {

    private var actualImage: File? = null
    var compressedImage: File? = null

    public interface ImagePickerCallback
    {
        //fun getCameraImageURL(url:String)
        //fun getGalleryImageURL(url:String)
        fun getImageURL(url:String)
    }

    companion object {
        var mCallBack : ImagePickerCallback?= null;
        fun startActivityForResult(activity : WeakReference<Activity> , getMyImage : ImagePickerCallback)
        {
            mCallBack = getMyImage

            var myActivity : Activity? = activity?.get()
            if(myActivity!=null){
                val intent = Intent(myActivity, ImagePickerActivityWithCompressor::class.java)
                val bundle = ActivityOptions.makeCustomAnimation(myActivity,
                    R.anim.anim_zoom_in,
                    R.anim.anim_zoom_out
                ).toBundle()
                ActivityCompat.startActivity(myActivity, intent, bundle)

            }else{
                // Something wenk wrong
            }

        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_permissions);
        ImagePickerDialogWithCompressor.newInstance()
            .show(supportFragmentManager, "imagepickker")
    }

    /**
     * To handle profile image for RegistrationDialogFragment
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        if (resultCode === Activity.RESULT_OK) {
            when (requestCode) {
                ImagePickerConstants.REQUEST_CAMERA -> {

                    actualImage = ImagePickerDialogWithCompressor.photoFile  // Okay

                    //compressImage()
                    customCompressImage()

                }

                ImagePickerConstants.REQUEST_GALLERY -> {

                    actualImage = FileUtil.from(this, data?.data)  // Okay

                    //compressImage()
                    customCompressImage()


                }
            }
        }
        else if(resultCode === Activity.RESULT_CANCELED){
            finish()
        }


    }

    private fun compressImage() {
        actualImage?.let { imageFile ->
            lifecycleScope.launch {
                // Default compression
                compressedImage = Compressor.compress(this@ImagePickerActivityWithCompressor, imageFile)
                /***********/
                val temp : String = compressedImage?.absolutePath ?: ""

                Log.e("~~~!~!~!~!","Actual :: "+actualImage?.absolutePath)
                Log.e("~~~!~!~!~!","Compressed :: "+temp)
                Log.e("~~~!~!~!~!","Actual Size :: "+actualImage?.length())
                Log.e("~~~!~!~!~!","Compressed Size :: "+compressedImage?.length())

                temp?.let { mCallBack?.getImageURL(it) }
                finish()
                /***********/
            }
        } ?: showError("Please choose an image!")
    }

    private fun customCompressImage()  {

        actualImage?.let { imageFile ->
            lifecycleScope.launch {
                // Default compression with custom destination file
                compressedImage = Compressor.compress(this@ImagePickerActivityWithCompressor, imageFile) {
                    default()
                    getExternalFilesDir(Environment.DIRECTORY_PICTURES)?.also {
                        //val file = File("${it.absolutePath}${File.separator}my_image.${imageFile.extension}")
                        val file : File = FileCreationUnderAppDirectory.createImageFile(this@ImagePickerActivityWithCompressor)
                        destination(file)
                    }
                }

                // Full custom
                /*compressedImage = Compressor.compress(this@ImagePickerActivityWithCompressor, imageFile) {
                    resolution(1280, 720)
                    quality(80)
                    format(Bitmap.CompressFormat.WEBP)
                    size(2_097_152) // 2 MB
                }*/

                /***********/
                val temp : String = compressedImage?.absolutePath ?: ""

                Log.e("~~~!~!~!~!","Actual :: "+actualImage?.absolutePath)
                Log.e("~~~!~!~!~!","Compressed :: "+temp)
                Log.e("~~~!~!~!~!","Actual Size :: "+actualImage?.length())
                Log.e("~~~!~!~!~!","Compressed Size :: "+compressedImage?.length())

                temp?.let { mCallBack?.getImageURL(it) }
                finish()
                /***********/


            }
        } ?: showError("Please choose an image!")

    }

    private fun showError(errorMessage: String) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
    }



}