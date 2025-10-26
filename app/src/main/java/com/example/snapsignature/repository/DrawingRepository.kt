package com.example.snapsignature.repository

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas as AndroidCanvas
import android.graphics.Paint as AndroidPaint
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.createBitmap
import com.example.snapsignature.model.DrawStroke
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

class DrawingRepository(private val context: Context) {

    companion object {
        private const val TAG = "DrawingRepository"
    }

    /**
     * Save drawing to gallery - SIMPLIFIED AND WORKING VERSION
     */
    suspend fun saveDrawingToGallery(
        strokes: List<DrawStroke>,
        width: Int,
        height: Int,
        backgroundColor: Color = Color.White,
        densityScale: Float
    ): SaveResult = withContext(Dispatchers.IO) {
        try {
            Log.d(TAG, "=== SAVE STARTED ===")
            Log.d(TAG, "Strokes: ${strokes.size}, Size: ${width}x${height}, Density: $densityScale")

            // Validate
            if (width <= 1 || height <= 1) {
                return@withContext SaveResult.Error("Canvas too small: ${width}x${height}")
            }
            if (strokes.isEmpty()) {
                return@withContext SaveResult.Error("Nothing to save")
            }

            // Create bitmap
            val bitmap = createBitmapFromStrokes(strokes, width, height, backgroundColor, densityScale)
            Log.d(TAG, "Bitmap created: ${bitmap.width}x${bitmap.height}")

            // Save it
            val savedPath = saveBitmapToMediaStore(bitmap)

            bitmap.recycle()

            if (savedPath != null) {
                Log.d(TAG, "=== SAVE SUCCESS: $savedPath ===")
                SaveResult.Success("✅ Saved to Gallery!\n$savedPath")
            } else {
                Log.e(TAG, "=== SAVE FAILED ===")
                SaveResult.Error("Failed to save image")
            }
        } catch (e: Exception) {
            Log.e(TAG, "EXCEPTION: ${e.message}", e)
            e.printStackTrace()
            SaveResult.Error("Error: ${e.message}")
        }
    }

    /**
     * Save bitmap using MediaStore - works on ALL Android versions
     */
    private fun saveBitmapToMediaStore(bitmap: Bitmap): String? {
        val filename = "canvas_${System.currentTimeMillis()}.png"

        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                // Android 10+ - Use MediaStore (no permission needed)
                val resolver = context.contentResolver
                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                    put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/CanvasStudio")
                }

                val imageUri: Uri? = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

                imageUri?.let { uri ->
                    resolver.openOutputStream(uri)?.use { outputStream ->
                        if (bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)) {
                            Log.d(TAG, "Saved via MediaStore Q+: $uri")
                            "Gallery/CanvasStudio/$filename"
                        } else {
                            resolver.delete(uri, null, null)
                            null
                        }
                    }
                }
            } else {
                // Android 9 and below - Use legacy method
                val imagesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                val canvasDir = File(imagesDir, "SignatureStudio")

                if (!canvasDir.exists()) {
                    canvasDir.mkdirs()
                }

                val imageFile = File(canvasDir, filename)

                FileOutputStream(imageFile).use { outputStream ->
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                    outputStream.flush()
                }

                // Notify gallery
                val contentValues = ContentValues().apply {
                    put(MediaStore.Images.Media.DATA, imageFile.absolutePath)
                    put(MediaStore.Images.Media.MIME_TYPE, "image/png")
                }
                context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

                Log.d(TAG, "Saved via legacy: ${imageFile.absolutePath}")
                imageFile.absolutePath
            }
        } catch (e: Exception) {
            Log.e(TAG, "saveBitmapToMediaStore error: ${e.message}", e)
            e.printStackTrace()
            null
        }
    }

    /**
     * Create bitmap from strokes
     */
    private fun createBitmapFromStrokes(
        strokes: List<DrawStroke>,
        width: Int,
        height: Int,
        backgroundColor: Color,
        densityScale: Float
    ): Bitmap {
        val bitmap = createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = AndroidCanvas(bitmap)

        // White background
        canvas.drawColor(backgroundColor.toArgb())

        val paint = AndroidPaint().apply {
            isAntiAlias = true
            isDither = true
            style = AndroidPaint.Style.STROKE
            strokeJoin = AndroidPaint.Join.ROUND
            strokeCap = AndroidPaint.Cap.ROUND
        }

        // Draw each stroke
        strokes.forEachIndexed { index, stroke ->
            paint.color = stroke.color.toArgb()
            paint.strokeWidth = stroke.strokeWidthDp * densityScale

            val points = stroke.points

            when {
                points.isEmpty() -> return@forEachIndexed
                points.size == 1 -> {
                    // Single point - draw as filled circle
                    val point = points[0]
                    paint.style = AndroidPaint.Style.FILL
                    canvas.drawCircle(point.x, point.y, paint.strokeWidth / 2f, paint)
                    paint.style = AndroidPaint.Style.STROKE
                    Log.d(TAG, "Stroke $index: 1 point at (${point.x}, ${point.y})")
                }
                else -> {
                    // Multiple points - draw lines and connection circles
                    for (i in 1 until points.size) {
                        canvas.drawLine(
                            points[i - 1].x, points[i - 1].y,
                            points[i].x, points[i].y,
                            paint
                        )
                    }

                    // Draw circles at each point for smooth connections
                    paint.style = AndroidPaint.Style.FILL
                    points.forEach { point ->
                        canvas.drawCircle(point.x, point.y, paint.strokeWidth / 2f, paint)
                    }
                    paint.style = AndroidPaint.Style.STROKE

                    Log.d(TAG, "Stroke $index: ${points.size} points")
                }
            }
        }

        return bitmap
    }
}

/**
 * Result class for save operations
 */
sealed class SaveResult {
    data class Success(val message: String) : SaveResult()
    data class Error(val message: String) : SaveResult()
}
