package com.example.canvasjetpackcompose.Screens

import android.graphics.drawable.shapes.Shape
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ShapeDefaults
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
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


data class Lines(
    val startLine:Offset,
    val endLine:Offset,
    val Color:Color = androidx.compose.ui.graphics.Color.Yellow,
    val width:Dp= 10.dp
)
@Composable
fun PaintScreen(modifier: Modifier = Modifier) {
    val   lines = remember { mutableStateListOf<Lines>() }
    val color1 = remember { mutableStateOf(false) }
    val color2 = remember { mutableStateOf(false) }
    val color3 = remember { mutableStateOf(false) }
    Column(modifier = Modifier.fillMaxSize()) {
      Canvas(modifier = Modifier
          .weight(1f).background(color = Color.White)
          .fillMaxSize()
          .pointerInput(key1 = true) {
              detectDragGestures { change, dragAmount ->
                  change.consume()
                  if (color1.value) {
                      val line = Lines(
                          startLine = change.position - dragAmount,
                          endLine = change.position,
                          Color = Color.Blue
                      )
                      lines.add(line)
                  } else if (color2.value) {
                      val line = Lines(
                          startLine = change.position - dragAmount,
                          endLine = change.position,
                          Color = Color.Green

                      )
                      lines.add(line)
                  } else if(color3.value){
                      val line = Lines(
                          startLine = change.position - dragAmount,
                          endLine = change.position,
                          Color = Color.White,
                          width = 25.dp

                      )
                      lines.add(line)
                  }
                  else {
                      val line = Lines(
                          startLine = change.position - dragAmount,
                          endLine = change.position,


                          )
                      lines.add(line)
                  }


              }

          }) {
        lines.forEach {lines->
            drawLine(
                start = lines.startLine,
                end = lines.endLine,
                strokeWidth = lines.width.toPx(),
                color = lines.Color
            )

        }
      }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            Button(onClick = {
                 color1.value= true
            }, shape = CircleShape, colors = ButtonDefaults.buttonColors(containerColor = Color.Blue)) {
                Text("Blue")
            }
            Button(
                onClick = {
                    color2.value = true
                    color1.value= false
                }, shape = CircleShape, colors = ButtonDefaults.buttonColors(containerColor = Color.Green)
            ) {

            }
            Button(onClick =
            {
                color3.value =true
                color2.value = false
                color1.value= false
            }) {
               Icon(  Icons.Filled.Clear, contentDescription = null)
            }
        }
    }


}

