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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row


import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.canvasjetpackcompose.Screens.AndroidBot
import com.example.canvasjetpackcompose.Screens.Circle1
import com.example.canvasjetpackcompose.Screens.DrawLine
import com.example.canvasjetpackcompose.Screens.DrawLine2
import com.example.canvasjetpackcompose.Screens.DrawLine3
import com.example.canvasjetpackcompose.Screens.PaintScreen
import com.example.canvasjetpackcompose.Screens.PraticeSet
import com.example.canvasjetpackcompose.ui.theme.CanvasJetpackComposeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        installSplashScreen()
        setContent {
            CanvasJetpackComposeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)){
                     // DrawLine()
                      //  DrawLine3()
                       // Circle1()
                        //DrawingScreen()
                         PaintScreen()
                       // PraticeSet()

                    }

                }
            }
        }
    }
}

data class Line(
    val start :Offset,
    val end:Offset,
    val color:Color = Color.Red,
    val strokewidth:Dp = 10.dp
)
//
//@Composable
//fun DrawingScreen(modifier: Modifier = Modifier) {
//    val lines = remember { mutableStateListOf<Line>() }
//     Canvas(modifier = Modifier.fillMaxSize().pointerInput(key1 = true){
//         detectDragGestures { change, dragAmount ->
//             change.consume()
//             val line= Line(
//                 start = change.position - dragAmount,
//                 end = change.position
//             )
//             lines.add(line)
//         }
//
//     }) {
//         drawRect(color = Color.White, size = size)
//       lines.forEach { line->
//           drawLine(
//               start = line.start,
//               end = line.end,
//               color = line.color,
//               strokeWidth = line.strokewidth.toPx()
//           )
//
//       }
//
//
//
//     }
//
//    Row(modifier = Modifier.fillMaxWidth()) {
//        Text("hello")
//    }
//
//}

@Composable
fun DrawingScreen(modifier: Modifier = Modifier) {
    val lines = remember { mutableStateListOf<Line>() }

    Column(modifier = Modifier.fillMaxSize()) {
        // Canvas takes most of the screen space
        Canvas(
            modifier = Modifier
                .weight(1f)  // This makes Canvas take available space
                .fillMaxWidth()
                .pointerInput(Unit) {
                    detectDragGestures { change, dragAmount ->
                        change.consume()
                        val line = Line(
                            start = change.position - dragAmount,
                            end = change.position
                        )
                        lines.add(line)
                    }
                }
        ) {
            drawRect(color = Color.White, size = size)
            lines.forEach { line ->
                drawLine(
                    start = line.start,
                    end = line.end,
                    color = line.color,
                    strokeWidth = line.strokewidth.toPx()
                )
            }
        }

        // Row containing buttons at the bottom
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = { /* Handle Clear Drawing */ }) {
                Text("Clear")
            }
            Button(onClick = { /* Handle Eraser Mode */ }) {
                Text("Eraser")
            }
            Button(onClick = { /* Handle Change Color */ }) {
                Text("Color")
            }
            Button(onClick = { /* Handle Save */ }) {
                Text("Save")
            }
        }
    }
}
