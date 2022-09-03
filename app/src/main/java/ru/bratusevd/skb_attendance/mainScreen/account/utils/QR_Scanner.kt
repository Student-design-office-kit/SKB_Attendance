package ru.bratusevd.skb_attendance.mainScreen.account.utils


import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.SparseArray
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import ru.bratusevd.skb_attendance.R
import java.io.IOException


class QR_Scanner : AppCompatActivity() {

    var cameraPreview: SurfaceView? = null
    var barcodeDetector: BarcodeDetector? = null
    var cameraSource: CameraSource? = null
    val RequestCameraPermissionId = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qr_scanner)
        cameraPreview = findViewById<View>(R.id.cameraPreview) as SurfaceView
        barcodeDetector = BarcodeDetector.Builder(this)
            .setBarcodeFormats(Barcode.QR_CODE)
            .build()
        cameraSource = CameraSource.Builder(this, barcodeDetector)
            .setRequestedPreviewSize(1920, 1080)
            .setFacing(CameraSource.CAMERA_FACING_BACK)
            .setRequestedFps(30.0f)
            .setAutoFocusEnabled(true)
            .build()
        cameraPreview!!.holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(holder: SurfaceHolder) {
                if (ActivityCompat.checkSelfPermission(
                        applicationContext,
                        Manifest.permission.CAMERA
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(
                        this@QR_Scanner,
                        arrayOf(Manifest.permission.CAMERA),
                        RequestCameraPermissionId
                    )
                    return
                }
                try {
                    cameraSource?.start(cameraPreview!!.holder)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }

            override fun surfaceChanged(
                holder: SurfaceHolder,
                format: Int,
                width: Int,
                height: Int
            ) {
            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {
                cameraSource?.stop()
            }
        })
        barcodeDetector?.setProcessor(object : Detector.Processor<Barcode?> {
            override fun release() {}
            @SuppressLint("ResourceType")
            override fun receiveDetections(detections: Detector.Detections<Barcode?>) {
                val qrcodes: SparseArray<Barcode?> = detections.detectedItems
                val intent = Intent()
                if (qrcodes.size() != 0) {
                    link = qrcodes.valueAt(0)?.rawValue

                    val qrMess: List<String> = link!!.split(":")
                    if (qrMess[0] == "Attendance") {
                        intent.putExtra("link", qrMess[1])
                        setResult(3, intent)
                        finish()
                    } else
                        runOnUiThread {
                            val message: TextView? = findViewById(R.id.mess)
                            message?.textSize = 22f
                            message?.text = resources.getString(R.string.scanner_error)
                        }
                }
            }
        })
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            RequestCameraPermissionId -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(
                            this,
                            Manifest.permission.CAMERA
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        return
                    }
                    try {
                        cameraSource?.start(cameraPreview!!.holder)
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent()
        setResult(3, intent)
        finish()
    }

    companion object {
        var link: String? = null
    }
}