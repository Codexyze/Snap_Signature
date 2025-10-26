package com.example.canvasjetpackcompose.model

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color

/**
 * Represents a single drawing stroke with all its properties
 */
data class DrawStroke(
    val points: MutableList<Offset> = mutableListOf(),
    val color: Color = Color.Black,
    val strokeWidthDp: Float = 10f,
    val isEraser: Boolean = false
)

