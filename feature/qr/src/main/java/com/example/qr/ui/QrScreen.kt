package com.example.qr.ui

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Rect
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.OptIn
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import java.util.concurrent.Executors

private const val VALID_QR_MESSAGE = "Стиральная машина открыта"

@Composable
fun QrScreen(
    onBackClick: () -> Unit,
    onMachineOpened: () -> Unit
) {
    val context = LocalContext.current

    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasCameraPermission = granted

        if (!granted) {
            Toast.makeText(
                context,
                "Для сканирования нужен доступ к камере",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    LaunchedEffect(Unit) {
        if (!hasCameraPermission) {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    if (hasCameraPermission) {
        QrScannerContent(
            onBackClick = onBackClick,
            onMachineOpened = onMachineOpened
        )
    } else {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Разрешите доступ к камере, чтобы сканировать QR-код",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        permissionLauncher.launch(Manifest.permission.CAMERA)
                    }
                ) {
                    Text("Разрешить")
                }
            }
        }
    }
}

@Composable
private fun QrScannerContent(
    onBackClick: () -> Unit,
    onMachineOpened: () -> Unit
) {
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
    val cameraExecutor = remember { Executors.newSingleThreadExecutor() }

    val density = LocalDensity.current
    val scanFrameSizePx = with(density) { 260.dp.toPx() }

    var previewSize by remember { mutableStateOf(IntSize.Zero) }
    var showSuccessDialog by remember { mutableStateOf(false) }
    var scanLocked by remember { mutableStateOf(false) }
    var lastWrongQrToastAt by remember { mutableLongStateOf(0L) }

    DisposableEffect(Unit) {
        onDispose {
            cameraExecutor.shutdown()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .onSizeChanged { previewSize = it }
    ) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { viewContext ->
                val previewView = PreviewView(viewContext).apply {
                    scaleType = PreviewView.ScaleType.FILL_CENTER
                }

                val cameraProviderFuture = ProcessCameraProvider.getInstance(viewContext)

                cameraProviderFuture.addListener(
                    {
                        val cameraProvider = cameraProviderFuture.get()

                        val preview = Preview.Builder()
                            .build()
                            .also {
                                it.setSurfaceProvider(previewView.surfaceProvider)
                            }

                        val analyzer = ImageAnalysis.Builder()
                            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                            .build()
                            .also {
                                it.setAnalyzer(cameraExecutor) { imageProxy ->
                                    processQrImage(
                                        context = viewContext,
                                        imageProxy = imageProxy,
                                        isScanLocked = scanLocked,
                                        previewSize = previewSize,
                                        scanFrameSizePx = scanFrameSizePx,
                                        onWrongQr = {
                                            val now = System.currentTimeMillis()

                                            if (now - lastWrongQrToastAt > 1500) {
                                                lastWrongQrToastAt = now

                                                Toast.makeText(
                                                    viewContext,
                                                    "Ой, похоже это не наш Qr-код.",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        },
                                        onValidQr = {
                                            scanLocked = true
                                            showSuccessDialog = true
                                        }
                                    )
                                }
                            }

                        cameraProvider.unbindAll()

                        cameraProvider.bindToLifecycle(
                            lifecycleOwner,
                            CameraSelector.DEFAULT_BACK_CAMERA,
                            preview,
                            analyzer
                        )
                    },
                    ContextCompat.getMainExecutor(viewContext)
                )

                previewView
            }
        )

        ScannerOverlay(
            modifier = Modifier.fillMaxSize(),
            frameSize = 260.dp
        )

        Text(
            text = "Назад",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 24.dp, top = 32.dp)
                .clickable { onBackClick() }
        )

        Text(
            text = "Поместите QR-код в рамку",
            color = Color.White,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(top = 330.dp)
        )
    }

    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = {},
            text = {
                Text(VALID_QR_MESSAGE)
            },
            confirmButton = {
                TextButton(
                    onClick = onMachineOpened
                ) {
                    Text("Ок")
                }
            }
        )
    }
}

@Composable
private fun ScannerOverlay(
    modifier: Modifier = Modifier,
    frameSize: androidx.compose.ui.unit.Dp
) {
    Canvas(modifier = modifier) {
        val framePx = frameSize.toPx()
        val left = (size.width - framePx) / 2f
        val top = (size.height - framePx) / 2f
        val right = left + framePx
        val bottom = top + framePx

        val overlayColor = Color.Black.copy(alpha = 0.65f)

        drawRect(
            color = overlayColor,
            topLeft = Offset.Zero,
            size = Size(size.width, top)
        )

        drawRect(
            color = overlayColor,
            topLeft = Offset(0f, bottom),
            size = Size(size.width, size.height - bottom)
        )

        drawRect(
            color = overlayColor,
            topLeft = Offset(0f, top),
            size = Size(left, framePx)
        )

        drawRect(
            color = overlayColor,
            topLeft = Offset(right, top),
            size = Size(size.width - right, framePx)
        )

        val cornerLength = 56.dp.toPx()
        val strokeWidth = 5.dp.toPx()
        val color = Color.White

        drawLine(color, Offset(left, top), Offset(left + cornerLength, top), strokeWidth, StrokeCap.Round)
        drawLine(color, Offset(left, top), Offset(left, top + cornerLength), strokeWidth, StrokeCap.Round)

        drawLine(color, Offset(right, top), Offset(right - cornerLength, top), strokeWidth, StrokeCap.Round)
        drawLine(color, Offset(right, top), Offset(right, top + cornerLength), strokeWidth, StrokeCap.Round)

        drawLine(color, Offset(left, bottom), Offset(left + cornerLength, bottom), strokeWidth, StrokeCap.Round)
        drawLine(color, Offset(left, bottom), Offset(left, bottom - cornerLength), strokeWidth, StrokeCap.Round)

        drawLine(color, Offset(right, bottom), Offset(right - cornerLength, bottom), strokeWidth, StrokeCap.Round)
        drawLine(color, Offset(right, bottom), Offset(right, bottom - cornerLength), strokeWidth, StrokeCap.Round)
    }
}

@OptIn(ExperimentalGetImage::class)
private fun processQrImage(
    context: Context,
    imageProxy: ImageProxy,
    isScanLocked: Boolean,
    previewSize: IntSize,
    scanFrameSizePx: Float,
    onWrongQr: () -> Unit,
    onValidQr: () -> Unit
) {
    if (isScanLocked || previewSize == IntSize.Zero) {
        imageProxy.close()
        return
    }

    val mediaImage = imageProxy.image

    if (mediaImage == null) {
        imageProxy.close()
        return
    }

    val image = InputImage.fromMediaImage(
        mediaImage,
        imageProxy.imageInfo.rotationDegrees
    )

    val options = BarcodeScannerOptions.Builder()
        .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
        .build()

    BarcodeScanning.getClient(options)
        .process(image)
        .addOnSuccessListener(ContextCompat.getMainExecutor(context)) { barcodes ->
            val barcodeInFrame = barcodes.firstOrNull { barcode ->
                barcode.boundingBox?.let { box ->
                    isBarcodeInsideScanFrame(
                        barcodeBox = box,
                        imageWidth = imageProxy.width,
                        imageHeight = imageProxy.height,
                        previewSize = previewSize,
                        scanFrameSizePx = scanFrameSizePx
                    )
                } == true
            }

            barcodeInFrame?.let { barcode ->
                if (barcode.rawValue == VALID_QR_MESSAGE) {
                    onValidQr()
                } else {
                    onWrongQr()
                }
            }
        }
        .addOnCompleteListener {
            imageProxy.close()
        }
}

private fun isBarcodeInsideScanFrame(
    barcodeBox: Rect,
    imageWidth: Int,
    imageHeight: Int,
    previewSize: IntSize,
    scanFrameSizePx: Float
): Boolean {
    val centerX = barcodeBox.exactCenterX()
    val centerY = barcodeBox.exactCenterY()

    val scanFrameFractionX = scanFrameSizePx / previewSize.width
    val scanFrameFractionY = scanFrameSizePx / previewSize.height

    val frameLeft = imageWidth * (0.5f - scanFrameFractionX / 2f)
    val frameRight = imageWidth * (0.5f + scanFrameFractionX / 2f)
    val frameTop = imageHeight * (0.5f - scanFrameFractionY / 2f)
    val frameBottom = imageHeight * (0.5f + scanFrameFractionY / 2f)

    return centerX in frameLeft..frameRight &&
            centerY in frameTop..frameBottom
}