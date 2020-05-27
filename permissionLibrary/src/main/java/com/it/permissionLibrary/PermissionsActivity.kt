package com.it.permissionLibrary

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import java.lang.ref.WeakReference

class PermissionsActivity : AppCompatActivity() {
    val PERMISSIONS_GRANTED = 0
    val PERMISSIONS_DENIED = 1
    private val PERMISSION_REQUEST_CODE = 0
    private val PACKAGE_URL_SCHEME = "package:"

    private var checker: PermissionsChecker? = null
    private var requiresCheck: Boolean = false

    public interface AllPermissionGrantedCallBack
    {
        fun allPermissionsGranted()
        fun permissionsNotGranted()
    }

    companion object {
        lateinit var mAllPermissionGrantedCallBack : AllPermissionGrantedCallBack
        lateinit var reqPermissions : Array<String>

        fun startActivityForResult(activity : WeakReference<Activity>,
                                   requestCode : Int,
                                   permissionsGrantedCallBack : AllPermissionGrantedCallBack,
                                   vararg permissions : String)
        {
            var myActivity : Activity? = activity?.get()

            mAllPermissionGrantedCallBack = permissionsGrantedCallBack
            reqPermissions = permissions as Array<String>

            if(myActivity != null){
                val intent = Intent(myActivity, PermissionsActivity::class.java)
                ActivityCompat.startActivityForResult(myActivity, intent, requestCode, null)
            }else{
                // Something goes wrong...
            }


        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //setContentView(R.layout.activity_permissions);

        val context: WeakReference<Context> = WeakReference(this)
        checker = PermissionsChecker(context)
        requiresCheck = true
    }

    override fun onResume() {
        super.onResume()
        if (requiresCheck) {
            val permissions = getPermissions()

            if (checker!!.lacksPermissions(*permissions)) {
                requestPermissions(*permissions)
            } else {
                allPermissionsGranted()
            }
        } else {
            requiresCheck = true
        }
    }

    private fun getPermissions(): Array<String> {
        return reqPermissions
    }

    private fun requestPermissions(vararg permissions: String) {
        ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQUEST_CODE)
    }

    private fun allPermissionsGranted() {
        setResult(PERMISSIONS_GRANTED)
        mAllPermissionGrantedCallBack.allPermissionsGranted()
        finish()
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == PERMISSION_REQUEST_CODE && hasAllPermissionsGranted(grantResults)) {
            requiresCheck = true
            allPermissionsGranted()
        } else {
            requiresCheck = false
            showMissingPermissionDialog()
        }
    }

    private fun hasAllPermissionsGranted(grantResults: IntArray): Boolean {
        for (grantResult in grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                return false
            }
        }
        return true
    }

    /**

     */
    private fun showMissingPermissionDialog() {
        val dialogBuilder: AlertDialog.Builder
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialogBuilder = AlertDialog.Builder(this, R.style.DialogTheme)
        } else {
            dialogBuilder = AlertDialog.Builder(this)
        }

        dialogBuilder.setTitle(R.string.permission_help)
        dialogBuilder.setMessage(R.string.permission_string_help_text)
        dialogBuilder.setCancelable(false)
        dialogBuilder.setNegativeButton(R.string.permission_quit, DialogInterface.OnClickListener { dialog, which ->
            setResult(PERMISSIONS_DENIED)
            mAllPermissionGrantedCallBack.permissionsNotGranted()
            finish()
        })
        dialogBuilder.setPositiveButton(R.string.permission_settings, DialogInterface.OnClickListener { dialog, which -> startAppSettings() })

        val alertDialog : AlertDialog = dialogBuilder.create()
        alertDialog.getWindow()?.getAttributes()?.windowAnimations =  R.style.DialogZoomAnim
        /*
        For more Animation
        http://www.devexchanges.info/2015/10/showing-dialog-with-animation-in-android.html
        */

        alertDialog.show()
    }

    private fun startAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.data = Uri.parse(PACKAGE_URL_SCHEME + packageName)
        startActivity(intent)
    }


}