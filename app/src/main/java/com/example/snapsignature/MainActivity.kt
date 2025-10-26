package com.example.snapsignature

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.layout.onSizeChanged
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.snapsignature.model.DrawStroke
import com.example.snapsignature.ui.theme.CanvasJetpackComposeTheme
import com.example.snapsignature.viewmodel.DrawingViewModel
import com.example.snapsignature.viewmodel.SaveState
import kotlinx.coroutines.flow.collectLatest

data class ColorCategory(
    val name: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val colors: List<Color>
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        installSplashScreen()
        setContent {
            CanvasJetpackComposeTheme {
                Scaffold {
                    // Padding values from Scaffold
                    val paddingValues = it
                    Box(modifier = Modifier.padding(paddingValues)) {
                        DrawingScreen()
                    }
                }

            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrawingScreen(
    modifier: Modifier = Modifier,
    viewModel: DrawingViewModel = viewModel()
) {
    val context = LocalContext.current
    val density = LocalDensity.current

    // Observe ViewModel state
    val strokes = viewModel.strokes
    val currentStroke by viewModel.currentStroke
    val selectedColor by viewModel.selectedColor
    val strokeWidthDp by viewModel.strokeWidthDp
    val isEraser by viewModel.isEraser
    val showExpandedColors by viewModel.showExpandedColors

    // Permission state
    var hasStoragePermission by remember {
        mutableStateOf(checkStoragePermission(context))
    }

    // Permission launcher
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        hasStoragePermission = permissions.values.all { it }
        if (hasStoragePermission) {
            Toast.makeText(context, "Permission granted! You can now save drawings.", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Permission denied. Cannot save to gallery.", Toast.LENGTH_LONG).show()
        }
    }

    // Handle save state
    LaunchedEffect(Unit) {
        viewModel.saveState.collectLatest { state ->
            when (state) {
                is SaveState.Success -> {
                    Toast.makeText(context, "✅ ${state.message}", Toast.LENGTH_LONG).show()
                }
                is SaveState.Error -> {
                    Toast.makeText(context, "❌ ${state.message}", Toast.LENGTH_LONG).show()
                }
                is SaveState.Loading -> {
                    Toast.makeText(context, "💾 Saving your drawing...", Toast.LENGTH_SHORT).show()
                }
                SaveState.Idle -> {}
            }
        }
    }

    // Color palette data
    val colorCategories = remember {
        listOf(
            ColorCategory(
                name = "Basic",
                icon = Icons.Default.Palette,
                colors = listOf(
                    Color.Black, Color.White, Color(0xFF424242), Color(0xFF757575),
                    Color(0xFFBDBDBD), Color(0xFFEEEEEE)
                )
            ),
            ColorCategory(
                name = "Reds",
                icon = Icons.Default.Favorite,
                colors = listOf(
                    Color(0xFFFFEBEE), Color(0xFFFFCDD2), Color(0xFFEF9A9A), Color(0xFFE57373),
                    Color(0xFFEF5350), Color(0xFFF44336), Color(0xFFE53935), Color(0xFFD32F2F),
                    Color(0xFFC62828), Color(0xFFB71C1C), Color(0xFFFF8A80), Color(0xFFFF5252),
                    Color(0xFFFF1744), Color(0xFFD50000)
                )
            ),
            ColorCategory(
                name = "Pinks",
                icon = Icons.Default.FavoriteBorder,
                colors = listOf(
                    Color(0xFFFCE4EC), Color(0xFFF8BBD9), Color(0xFFF48FB1), Color(0xFFF06292),
                    Color(0xFFEC407A), Color(0xFFE91E63), Color(0xFFD81B60), Color(0xFFC2185B),
                    Color(0xFFAD1457), Color(0xFF880E4F), Color(0xFFFF80AB), Color(0xFFFF4081),
                    Color(0xFFF50057), Color(0xFFC51162)
                )
            ),
            ColorCategory(
                name = "Purples",
                icon = Icons.Default.Star,
                colors = listOf(
                    Color(0xFFF3E5F5), Color(0xFFE1BEE7), Color(0xFFCE93D8), Color(0xFFBA68C8),
                    Color(0xFFAB47BC), Color(0xFF9C27B0), Color(0xFF8E24AA), Color(0xFF7B1FA2),
                    Color(0xFF6A1B9A), Color(0xFF4A148C), Color(0xFFEA80FC), Color(0xFFE040FB),
                    Color(0xFFD500F9), Color(0xFFAA00FF)
                )
            ),
            ColorCategory(
                name = "Blues",
                icon = Icons.Default.WaterDrop,
                colors = listOf(
                    Color(0xFFE3F2FD), Color(0xFFBBDEFB), Color(0xFF90CAF9), Color(0xFF64B5F6),
                    Color(0xFF42A5F5), Color(0xFF2196F3), Color(0xFF1E88E5), Color(0xFF1976D2),
                    Color(0xFF1565C0), Color(0xFF0D47A1), Color(0xFF82B1FF), Color(0xFF448AFF),
                    Color(0xFF2979FF), Color(0xFF2962FF)
                )
            ),
            ColorCategory(
                name = "Greens",
                icon = Icons.Default.Eco,
                colors = listOf(
                    Color(0xFFE8F5E9), Color(0xFFC8E6C9), Color(0xFFA5D6A7), Color(0xFF81C784),
                    Color(0xFF66BB6A), Color(0xFF4CAF50), Color(0xFF43A047), Color(0xFF388E3C),
                    Color(0xFF2E7D32), Color(0xFF1B5E20), Color(0xFFB9F6CA), Color(0xFF69F0AE),
                    Color(0xFF00E676), Color(0xFF00C853)
                )
            ),
            ColorCategory(
                name = "Oranges",
                icon = Icons.Default.WbSunny,
                colors = listOf(
                    Color(0xFFFFF3E0), Color(0xFFFFE0B2), Color(0xFFFFCC80), Color(0xFFFFB74D),
                    Color(0xFFFFA726), Color(0xFFFF9800), Color(0xFFFB8C00), Color(0xFFF57C00),
                    Color(0xFFEF6C00), Color(0xFFE65100), Color(0xFFFFD180), Color(0xFFFFAB40),
                    Color(0xFFFF9100), Color(0xFFFF6D00)
                )
            ),
            ColorCategory(
                name = "Yellows",
                icon = Icons.Default.LightMode,
                colors = listOf(
                    Color(0xFFFFFDE7), Color(0xFFFFF9C4), Color(0xFFFFF59D), Color(0xFFFFF176),
                    Color(0xFFFFEE58), Color(0xFFFFEB3B), Color(0xFFFDD835), Color(0xFFFBC02D),
                    Color(0xFFF9A825), Color(0xFFF57F17), Color(0xFFFFFF8D), Color(0xFFFFFF00),
                    Color(0xFFFFEA00), Color(0xFFFFD600)
                )
            )
        )
    }

    val quickColors = remember {
        listOf(
            Color.Black, Color(0xFF424242), Color.White, Color(0xFFF44336),
            Color(0xFFE91E63), Color(0xFF9C27B0), Color(0xFF673AB7), Color(0xFF3F51B5),
            Color(0xFF2196F3), Color(0xFF03A9F4), Color(0xFF4CAF50), Color(0xFF8BC34A),
            Color(0xFFCDDC39), Color(0xFFFFEB3B), Color(0xFFFFC107), Color(0xFFFF9800),
            Color(0xFFFF5722), Color(0xFF795548), Color(0xFF9E9E9E), Color(0xFF607D8B)
        )
    }

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.background,
                            MaterialTheme.colorScheme.surface.copy(alpha = 0.8f)
                        )
                    )
                )
        ) {
            // Top App Bar
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color.Transparent,
                shadowElevation = 4.dp
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                                    MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f)
                                )
                            )
                        )
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Draw Signature",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        IconButton(
                            onClick = { viewModel.clearCanvas() },
                            enabled = viewModel.hasStrokes()
                        ) {
                            Icon(
                                Icons.Default.DeleteSweep,
                                contentDescription = "Clear Canvas",
                                tint = if (viewModel.hasStrokes()) MaterialTheme.colorScheme.error
                                       else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                            )
                        }
                    }
                }
            }

            // Drawing Canvas
            Card(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(4.dp)
                        .onSizeChanged { viewModel.updateCanvasSize(it) }
                ) {
                    androidx.compose.foundation.Canvas(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(16.dp))
                            .pointerInput(Unit) {
                                detectDragGestures(
                                    onDragStart = { offset ->
                                        viewModel.startStroke(offset)
                                    },
                                    onDrag = { change, _ ->
                                        change.consume()
                                        viewModel.addPointToStroke(change.position)
                                    },
                                    onDragEnd = {
                                        viewModel.endStroke()
                                    },
                                    onDragCancel = {
                                        viewModel.cancelStroke()
                                    }
                                )
                            }
                    ) {
                        drawCanvas(strokes, currentStroke, density)
                    }
                }
            }

            // Controls Panel
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    // Brush Size Control
                    EnhancedBrushSizeControl(
                        strokeWidth = strokeWidthDp,
                        onStrokeWidthChange = { viewModel.setStrokeWidth(it) },
                        isEraser = isEraser,
                        selectedColor = selectedColor
                    )

                    // Color Palette
                    EnhancedColorPaletteSection(
                        quickColors = quickColors,
                        colorCategories = colorCategories,
                        selectedColor = selectedColor,
                        isEraser = isEraser,
                        showExpandedColors = showExpandedColors,
                        onColorSelected = { viewModel.selectColor(it) },
                        onEraserSelected = { viewModel.setEraserMode(true) },
                        onToggleExpandedColors = { viewModel.toggleExpandedColors() }
                    )

                    // Action Buttons
                    EnhancedActionButtonsRow(
                        onUndo = { viewModel.undoLastStroke() },
                        onSave = {
                            if (hasStoragePermission) {
                                viewModel.saveDrawing(context.resources.displayMetrics.density)
                            } else {
                                requestStoragePermission(permissionLauncher)
                            }
                        },
                        hasStrokes = viewModel.hasStrokes()
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun EnhancedBrushSizeControl(
    strokeWidth: Float,
    onStrokeWidthChange: (Float) -> Unit,
    isEraser: Boolean,
    selectedColor: Color
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    if (isEraser) Icons.Default.AutoFixOff else Icons.Default.Brush,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (isEraser) "Eraser Size" else "Brush Size",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .padding(4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size((strokeWidth / 4f).coerceAtMost(20f).dp)
                            .clip(CircleShape)
                            .background(if (isEraser) Color.Gray else selectedColor)
                    )
                }

                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Text(
                        text = "${strokeWidth.toInt()}dp",
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Slider(
            value = strokeWidth,
            onValueChange = onStrokeWidthChange,
            valueRange = 1f..100f,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun EnhancedColorPaletteSection(
    quickColors: List<Color>,
    colorCategories: List<ColorCategory>,
    selectedColor: Color,
    isEraser: Boolean,
    showExpandedColors: Boolean,
    onColorSelected: (Color) -> Unit,
    onEraserSelected: () -> Unit,
    onToggleExpandedColors: () -> Unit
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.Palette,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Colors",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilledTonalIconButton(
                    onClick = onToggleExpandedColors,
                    colors = IconButtonDefaults.filledTonalIconButtonColors(
                        containerColor = if (showExpandedColors) MaterialTheme.colorScheme.primary
                                        else MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    val rotationAngle by animateFloatAsState(
                        targetValue = if (showExpandedColors) 180f else 0f,
                        label = "rotation"
                    )
                    Icon(
                        Icons.Default.ExpandMore,
                        contentDescription = "More Colors",
                        modifier = Modifier.rotate(rotationAngle),
                        tint = if (showExpandedColors) MaterialTheme.colorScheme.onPrimary
                               else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                EnhancedEraserButton(
                    isSelected = isEraser,
                    onClick = onEraserSelected
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(10),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.height(80.dp)
        ) {
            items(quickColors) { color ->
                EnhancedColorCircle(
                    color = color,
                    isSelected = !isEraser && color == selectedColor,
                    onClick = { onColorSelected(color) }
                )
            }
        }

        AnimatedVisibility(
            visible = showExpandedColors,
            enter = expandVertically(animationSpec = tween(300)),
            exit = shrinkVertically(animationSpec = tween(300))
        ) {
            Column(
                modifier = Modifier.padding(top = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                colorCategories.forEach { category ->
                    ColorCategorySection(
                        category = category,
                        selectedColor = selectedColor,
                        isEraser = isEraser,
                        onColorSelected = onColorSelected
                    )
                }
            }
        }
    }
}

@Composable
private fun ColorCategorySection(
    category: ColorCategory,
    selectedColor: Color,
    isEraser: Boolean,
    onColorSelected: (Color) -> Unit
) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            Icon(
                category.icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = category.name,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Medium
            )
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.height(80.dp)
        ) {
            items(category.colors) { color ->
                EnhancedColorCircle(
                    color = color,
                    isSelected = !isEraser && color == selectedColor,
                    onClick = { onColorSelected(color) },
                    size = 32.dp
                )
            }
        }
    }
}

@Composable
private fun EnhancedColorCircle(
    color: Color,
    isSelected: Boolean,
    onClick: () -> Unit,
    size: androidx.compose.ui.unit.Dp = 36.dp
) {
    val animatedScale by animateFloatAsState(
        targetValue = if (isSelected) 1.1f else 1f,
        label = "scale"
    )

    val borderColor by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.primary
                      else Color.Gray.copy(alpha = 0.3f),
        label = "border"
    )

    Box(
        modifier = Modifier
            .size(size)
            .scale(animatedScale)
            .clip(CircleShape)
            .background(color)
            .border(
                width = if (isSelected) 3.dp else 1.dp,
                color = borderColor,
                shape = CircleShape
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        if (isSelected) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(
                        if (color == Color.White || color.luminance() > 0.8f)
                            MaterialTheme.colorScheme.primary
                        else Color.White
                    )
            )
        }
    }
}

@Composable
private fun EnhancedEraserButton(
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val animatedColor by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.error
                      else MaterialTheme.colorScheme.surfaceVariant,
        label = "eraser color"
    )

    FilledTonalButton(
        onClick = onClick,
        colors = ButtonDefaults.filledTonalButtonColors(
            containerColor = animatedColor,
            contentColor = if (isSelected) MaterialTheme.colorScheme.onError
                          else MaterialTheme.colorScheme.onSurfaceVariant
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Icon(
            Icons.Default.AutoFixOff,
            contentDescription = "Eraser",
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = "Eraser",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun EnhancedActionButtonsRow(
    onUndo: () -> Unit,
    onSave: () -> Unit,
    hasStrokes: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedButton(
            onClick = onUndo,
            enabled = hasStrokes,
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(16.dp)
        ) {
            Icon(
                Icons.Default.Undo,
                contentDescription = "Undo",
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Undo", fontWeight = FontWeight.SemiBold)
        }

        Button(
            onClick = onSave,
            enabled = hasStrokes,
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(16.dp),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 4.dp,
                pressedElevation = 8.dp
            )
        ) {
            Icon(
                Icons.Default.Save,
                contentDescription = "Save",
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Save", fontWeight = FontWeight.SemiBold)
        }
    }
}

// Drawing function with real-time rendering
private fun DrawScope.drawCanvas(
    strokes: List<DrawStroke>,
    currentStroke: DrawStroke?,
    density: androidx.compose.ui.unit.Density
) {
    fun dpToPx(dp: Float): Float = with(density) { dp.dp.toPx() }

    // Draw all completed strokes
    strokes.forEach { stroke ->
        drawStroke(stroke, ::dpToPx)
    }

    // Draw current stroke in real-time
    currentStroke?.let { stroke ->
        drawStroke(stroke, ::dpToPx)
    }
}

private fun DrawScope.drawStroke(
    stroke: DrawStroke,
    dpToPx: (Float) -> Float
) {
    val strokeWidth = dpToPx(stroke.strokeWidthDp)
    val points = stroke.points

    if (points.isEmpty()) return

    if (points.size == 1) {
        drawCircle(
            color = stroke.color,
            radius = strokeWidth / 2f,
            center = points.first()
        )
    } else if (points.size > 1) {
        for (i in 1 until points.size) {
            drawLine(
                start = points[i - 1],
                end = points[i],
                color = stroke.color,
                strokeWidth = strokeWidth,
                cap = Stroke.DefaultCap
            )
        }

        for (point in points) {
            drawCircle(
                color = stroke.color,
                radius = strokeWidth / 2f,
                center = point
            )
        }
    }
}

// Permission helpers
private fun checkStoragePermission(context: android.content.Context): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.READ_MEDIA_IMAGES
        ) == PackageManager.PERMISSION_GRANTED
    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        true // Scoped storage, no permission needed
    } else {
        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }
}

private fun requestStoragePermission(
    launcher: androidx.activity.compose.ManagedActivityResultLauncher<Array<String>, Map<String, Boolean>>
) {
    val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        arrayOf(Manifest.permission.READ_MEDIA_IMAGES)
    } else {
        arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    }
    launcher.launch(permissions)
}
