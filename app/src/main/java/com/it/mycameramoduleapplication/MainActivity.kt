package com.it.mycameramoduleapplication

import android.Manifest
import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.it.imagePickerLibrary.withCompress.ImagePickerActivityWithCompressor
import com.it.imagePickerLibrary.withoutCompress.ImagePickerActivity
import com.it.permissionLibrary.PermissionsActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.ref.WeakReference

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        clickOnCamera_1()
        clickOnCamera_2()
    }

    private fun clickOnCamera_1() {
        circleImageView_1.setOnClickListener {

            var permissionsRequired = arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            
            var myWeakRefActivity : WeakReference<Activity> = WeakReference(this)

            myWeakRefActivity?.let {
                PermissionsActivity.startActivityForResult(it, 0,
                    object : PermissionsActivity.MyAllPermissionsGranted{
                        override fun permissionsNotGranted() {
                            Toast.makeText(this@MainActivity,"Camera Permission Required",Toast.LENGTH_LONG).show()
                        }
                        override fun allPermissionsGranted() {

                            myWeakRefActivity?.let {
                                /***************************************************************/
                                ImagePickerActivity.startActivityForResult(it,
                                    object : ImagePickerActivity.ImagePickerCallback{
                                        override fun getImageURL(url: String) {
                                            if(url!=null) {
                                                myWeakRefActivity?.let { it1 -> circleImageView_1.loadImg(url, it1?.get()!!) }
                                            }
                                        }
                                    })
                                /***************************************************************/

                            }
                        }
                    }
                    ,*permissionsRequired)
            }
        }
    }


    private fun clickOnCamera_2() {
        circleImageView_2.setOnClickListener {

            var permissionsRequired = arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)

            var myWeakRefActivity : WeakReference<Activity> = WeakReference(this)

            myWeakRefActivity?.let {
                PermissionsActivity.startActivityForResult(it, 0,
                    object : PermissionsActivity.MyAllPermissionsGranted{
                        override fun permissionsNotGranted() {
                            Toast.makeText(this@MainActivity,"Camera Permission Required",Toast.LENGTH_LONG).show()
                        }
                        override fun allPermissionsGranted() {

                            myWeakRefActivity?.let {
                                /***************************************************************/
                                ImagePickerActivityWithCompressor.startActivityForResult(it,
                                    object : ImagePickerActivityWithCompressor.ImagePickerCallback{
                                        override fun getImageURL(url: String) {
                                            if(url!=null) {
                                                Log.e("~~~!~!~!~!","Mainactivity :: "+url)
                                                myWeakRefActivity?.let { it1 -> circleImageView_2.loadImg(url, it1?.get()!!) }
                                            }
                                        }
                                    })
                                /***************************************************************/

                            }
                        }
                    }
                    ,*permissionsRequired)
            }
        }
    }
    
    
}
