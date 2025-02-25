package com.example.canvasjetpackcompose

import android.graphics.Path
import android.os.Bundle
import android.provider.CalendarContract.Colors
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.canvasjetpackcompose.Screens.AndroidBot
import com.example.canvasjetpackcompose.Screens.Circle1
import com.example.canvasjetpackcompose.Screens.DrawLine
import com.example.canvasjetpackcompose.Screens.DrawLine2
import com.example.canvasjetpackcompose.Screens.DrawLine3
import com.example.canvasjetpackcompose.ui.theme.CanvasJetpackComposeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CanvasJetpackComposeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)){
                     // DrawLine()
                      //  DrawLine3()
                       // Circle1()
                        DrawingScreen()
                    }

                }
            }
        }
    }
}

//@Composable
//fun SimplePainterApp() {
//    val paths = remember { mutableStateListOf<Pair<androidx.compose.ui.graphics.Path, Color>>() }
//    var currentPath by remember { mutableStateOf(Path()) }
//    var currentColor by remember { mutableStateOf(Color.Black) }
//    var drawTrigger by remember { mutableStateOf(0f) } // Forces recomposition
//
//    Canvas(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(Color.White)
//            .pointerInput(Unit) {
//                detectDragGestures(
//                    onDragStart = { offset ->
//                        currentPath = Path().apply {
//                            moveTo(offset.x, offset.y)
//                        }
//                        paths.add(currentPath to currentColor)
//                    },
//                    onDrag = { change, _ ->
//                        change.consume()
//                        currentPath.lineTo(change.position.x, change.position.y)
//                        drawTrigger += 0.0001f // Force recomposition
//                    }
//                )
//            }
//    ) {
//        paths.forEach { (path, color) ->
//            drawPath(path, color = color, style = Stroke(width = 5f))
//        }
//    }
//}
data class Line(
    val start :Offset,
    val end:Offset,
    val color:Color = Color.Red,
    val strokewidth:Dp = 10.dp
)

@Composable
fun DrawingScreen(modifier: Modifier = Modifier) {
    val lines = remember { mutableStateListOf<Line>() }
     Canvas(modifier = Modifier.fillMaxSize().pointerInput(key1 = true){
         detectDragGestures { change, dragAmount ->
             change.consume()
             val line= Line(
                 start = change.position - dragAmount,
                 end = change.position
             )
             lines.add(line)
         }

     }) {
       lines.forEach { line->
           drawLine(
               start = line.start,
               end = line.end,
               color = line.color,
               strokeWidth = line.strokewidth.toPx()
           )

       }


     }

}