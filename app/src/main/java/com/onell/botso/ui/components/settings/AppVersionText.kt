package com.onell.botso.ui.components.settings

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.pm.PackageInfoCompat

@Composable
fun AppVersionText() {
    val context = LocalContext.current

    val versionInfo = remember(context) {
        try {
            val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            val versionName = packageInfo.versionName ?: "1.0.0"
            val versionCode = PackageInfoCompat.getLongVersionCode(packageInfo)

            "Version $versionName ($versionCode)"
        } catch (e: Exception) {
            "Version Unknown"
        }
    }

    Text(text = versionInfo)

}
