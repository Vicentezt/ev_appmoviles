package com.example.dsy1105_004v2026

import android.Manifest
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.dsy1105_004v2026.utils.CameraPermissionHelper
import com.example.dsy1105_004v2026.navigation.AppNav
import com.example.dsy1105_004v2026.ui.theme.DSY1105004V2026Theme

class MainActivity : ComponentActivity() {

    private var hasCameraPermission by mutableStateOf(false)

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasCameraPermission = isGranted
        if (isGranted) {
            Toast.makeText(this, "Permiso de cámara concedido", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Se necesita permiso de cámara para tomar la foto del respaldo", Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hasCameraPermission = CameraPermissionHelper.hasCameraPermission(this)

        setContent {
            DSY1105004V2026Theme {
                AppNav(
                    hasCameraPermission = hasCameraPermission,
                    onRequestCameraPermission = {
                        requestPermissionLauncher.launch(Manifest.permission.CAMERA)
                    }
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        hasCameraPermission = CameraPermissionHelper.hasCameraPermission(this)
    }
}
