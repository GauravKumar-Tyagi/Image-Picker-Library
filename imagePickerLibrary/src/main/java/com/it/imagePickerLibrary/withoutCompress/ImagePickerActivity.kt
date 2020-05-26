package com.it.imagePickerLibrary.withoutCompress


import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.it.imagePickerLibrary.common.ImagePickerConstants
import com.it.imagePickerLibrary.R
import com.it.imagePickerLibrary.common.RealPathUtil
import java.lang.ref.WeakReference


class ImagePickerActivity : AppCompatActivity() {
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
                val intent = Intent(myActivity, ImagePickerActivity::class.java)
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
        ImagePickerDialog.newInstance()
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
                    /*
                          // don't compare the data (data: Intent?) to null, it will always come as  null
                          //because we are providing a file URI,
                          //so load with the imageFilePath we obtained before opening the cameraIntent
                    */
                    mCallBack?.getImageURL(
                        ImagePickerDialog.imageFilePath
                    )
                    finish()

                    /*
                           But if you want the imagePath to send the image to a server as a MultiPartRequest,
                           you need to save the file in a directory as temporary file. From that file obtain
                           the path and will send to the server.
                           Hence we are using imageFilePath here.
                     */
                }

                ImagePickerConstants.REQUEST_GALLERY -> {
                    val temp = this?.let {
                        RealPathUtil.getRealFilePathFromUri(
                            it,
                            data?.data
                        )
                    }
                    temp?.let { mCallBack?.getImageURL(it) }
                    finish()
                }
            }
        }
        else if(resultCode === Activity.RESULT_CANCELED){
            finish()
        }


    }



}