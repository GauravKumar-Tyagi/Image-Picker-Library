package com.it.imagePickerLibrary.withCompress

import android.content.Intent
import android.graphics.Point
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.*
import androidx.core.content.FileProvider
import com.it.imagePickerLibrary.common.ImagePickerConstants
import com.it.imagePickerLibrary.R
import com.it.imagePickerLibrary.common.FileCreationUnderAppDirectory
import com.it.imagePickerLibrary.withoutCompress.ImagePickerDialog
import kotlinx.android.synthetic.main.dialog_image_picker.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by android on 7/4/18.
 */
class ImagePickerDialogWithCompressor : androidx.fragment.app.DialogFragment(), View.OnClickListener {


    companion object {
        var photoFile: File? = null
        var imageFilePath = ""
        fun newInstance(): ImagePickerDialogWithCompressor {
            return ImagePickerDialogWithCompressor()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.DialogStyle)

    }


    override fun onActivityCreated(arg0: Bundle?) {
        super.onActivityCreated(arg0)
        dialog?.window?.attributes?.windowAnimations =
            R.style.DialogZoomAnim

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        isCancelable = false
        return inflater.inflate(R.layout.dialog_image_picker, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setListener()
    }

    fun setListener() {
        txt_dialog_take_photo.setOnClickListener(this)
        txt_dialog_choose_photo.setOnClickListener(this)
        txt_dialog_close.setOnClickListener {
            dismiss()
            activity?.finish()
        }
    }

    override fun onResume() {
        super.onResume()
        val window = dialog?.window
        val size = Point()
        val display = window?.windowManager?.defaultDisplay
        display?.getSize(size)
        val width = size.x
        window?.setLayout((width * 0.85).toInt(), WindowManager.LayoutParams.WRAP_CONTENT)
        window?.setGravity(Gravity.CENTER)
    }


    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.txt_dialog_take_photo -> {

                dispatchCameraIntent()

                dismiss()

            }
            R.id.txt_dialog_choose_photo -> {
                dispatchGalleryIntent()

                dismiss()
            }
        }
    }

    /**
     * Select image fro gallery
     */
    private fun dispatchGalleryIntent() {
        val galleryIntent = Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        activity?.startActivityForResult(galleryIntent,
            ImagePickerConstants.REQUEST_GALLERY
        )
    }

    /***********/

    private  fun dispatchCameraIntent(): Unit {
        val pictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (pictureIntent.resolveActivity(activity?.getPackageManager()!!) != null) {

            try {
                photoFile = FileCreationUnderAppDirectory.createImageFile(activity)
                imageFilePath = photoFile?.absolutePath?:""  // Most Imp Line
            } catch (e: IOException) {
                e.printStackTrace()
                return
            }
            val photoUri : Uri = FileProvider.getUriForFile(
                activity!!,
                activity?.getPackageName().toString() + ".provider",
                photoFile!!
            )
            pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
            activity?.startActivityForResult(pictureIntent,
                ImagePickerConstants.REQUEST_CAMERA
            )
        }
    }





    /************/


}