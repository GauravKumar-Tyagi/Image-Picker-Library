package com.it.permissionLibrary

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import java.lang.ref.WeakReference

class PermissionsChecker(private val context: WeakReference<Context>) {

    fun lacksPermissions(vararg permissions: String): Boolean {
        for (permission in permissions) {
            if (lacksPermission(permission)) {
                return true
            }
        }
        return false
    }

    private fun lacksPermission(permission: String): Boolean {
        if(context?.get() != null)
            return ContextCompat.checkSelfPermission(context?.get() as Context, permission) == PackageManager.PERMISSION_DENIED
        else
            return false
    }

}