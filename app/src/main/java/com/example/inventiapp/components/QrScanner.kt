package com.example.inventiapp.components

import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning

@Composable
fun QrScanner(onQrScanned: (String) -> Unit) {
    val context = LocalContext.current

    val options = GmsBarcodeScannerOptions.Builder()
        .setBarcodeFormats(
            Barcode.FORMAT_QR_CODE,
            Barcode.FORMAT_CODE_128,
            Barcode.FORMAT_EAN_13
        )
        .build()

    val scanner = GmsBarcodeScanning.getClient(context, options)

    Button(
        onClick = {
            scanner.startScan()
                .addOnSuccessListener { barcode ->
                    val code = barcode.rawValue
                    if (code != null) {
                        onQrScanned(code)
                    }
                }
                .addOnCanceledListener {
                    Toast.makeText(context, "Escaneo cancelado", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(context, "Error al escanear: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        },
        modifier = Modifier.fillMaxWidth()
    ) {
        Text("Escanear Código QR / SKU")
    }
}