package com.example.snapsignature.viewmodel

import android.app.Application
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntSize
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.snapsignature.model.DrawStroke
import com.example.snapsignature.repository.DrawingRepository
import com.example.snapsignature.repository.SaveResult
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class DrawingViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = DrawingRepository(application.applicationContext)

    // Drawing state - using mutableStateListOf for reactive updates
    val strokes = mutableStateListOf<DrawStroke>()
    private val _currentStrokePoints = mutableStateListOf<Offset>()
    val currentStroke = mutableStateOf<DrawStroke?>(null)
    val canvasSize = mutableStateOf(IntSize(1, 1))

    // UI state
    val selectedColor = mutableStateOf(Color.Black)
    val strokeWidthDp = mutableFloatStateOf(10f)
    val isEraser = mutableStateOf(false)
    val showExpandedColors = mutableStateOf(false)

    // Save state
    private val _saveState = MutableSharedFlow<SaveState>(replay = 0)
    val saveState: SharedFlow<SaveState> = _saveState.asSharedFlow()

    /**
     * Start a new stroke
     */
    fun startStroke(offset: Offset) {
        _currentStrokePoints.clear()
        _currentStrokePoints.add(offset)

        val stroke = DrawStroke(
            points = _currentStrokePoints,
            color = if (isEraser.value) Color.White else selectedColor.value,
            strokeWidthDp = strokeWidthDp.floatValue,
            isEraser = isEraser.value
        )
        currentStroke.value = stroke
    }

    /**
     * Add point to current stroke (real-time drawing)
     */
    fun addPointToStroke(offset: Offset) {
        currentStroke.value?.let {
            _currentStrokePoints.add(offset)
            // Force recomposition by creating new stroke reference with updated points
            currentStroke.value = it.copy(points = _currentStrokePoints)
        }
    }

    /**
     * End current stroke
     */
    fun endStroke() {
        currentStroke.value?.let { stroke ->
            if (stroke.points.isNotEmpty()) {
                // Create a copy with immutable list for storage
                val finalStroke = stroke.copy(points = stroke.points.toMutableList())
                strokes.add(finalStroke)
            }
        }
        currentStroke.value = null
        _currentStrokePoints.clear()
    }

    /**
     * Cancel current stroke
     */
    fun cancelStroke() {
        currentStroke.value = null
        _currentStrokePoints.clear()
    }

    /**
     * Undo last stroke
     */
    fun undoLastStroke() {
        if (strokes.isNotEmpty()) {
            strokes.removeAt(strokes.lastIndex)
        }
    }

    /**
     * Clear all strokes
     */
    fun clearCanvas() {
        strokes.clear()
        currentStroke.value = null
        _currentStrokePoints.clear()
    }

    /**
     * Select color
     */
    fun selectColor(color: Color) {
        selectedColor.value = color
        isEraser.value = false
    }

    /**
     * Toggle eraser
     */
    fun toggleEraser() {
        isEraser.value = !isEraser.value
    }

    /**
     * Set eraser mode
     */
    fun setEraserMode(enabled: Boolean) {
        isEraser.value = enabled
    }

    /**
     * Set stroke width
     */
    fun setStrokeWidth(width: Float) {
        strokeWidthDp.floatValue = width
    }

    /**
     * Toggle expanded color palette
     */
    fun toggleExpandedColors() {
        showExpandedColors.value = !showExpandedColors.value
    }

    /**
     * Update canvas size
     */
    fun updateCanvasSize(size: IntSize) {
        canvasSize.value = size
    }

    /**
     * Save drawing to gallery with proper error handling
     */
    fun saveDrawing(densityScale: Float) {
        viewModelScope.launch {
            try {
                // Emit loading state
                _saveState.emit(SaveState.Loading)

                // Validate before saving
                if (strokes.isEmpty()) {
                    _saveState.emit(SaveState.Error("Nothing to save. Please draw something first."))
                    return@launch
                }

                if (canvasSize.value.width <= 1 || canvasSize.value.height <= 1) {
                    _saveState.emit(SaveState.Error("Canvas not ready. Please try again."))
                    return@launch
                }

                // Save using repository
                val result = repository.saveDrawingToGallery(
                    strokes = strokes.toList(),
                    width = canvasSize.value.width,
                    height = canvasSize.value.height,
                    backgroundColor = Color.White,
                    densityScale = densityScale
                )

                // Emit result
                when (result) {
                    is SaveResult.Success -> _saveState.emit(SaveState.Success(result.message))
                    is SaveResult.Error -> _saveState.emit(SaveState.Error(result.message))
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _saveState.emit(SaveState.Error("Unexpected error: ${e.message}"))
            }
        }
    }

    /**
     * Check if there are strokes to save or undo
     */
    fun hasStrokes(): Boolean = strokes.isNotEmpty()
}

/**
 * States for save operations
 */
sealed class SaveState {
    data object Idle : SaveState()
    data object Loading : SaveState()
    data class Success(val message: String) : SaveState()
    data class Error(val message: String) : SaveState()
}
