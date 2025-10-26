package com.example.snapsignature.Screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Composable
fun AndroidBot(){
Canvas(modifier = Modifier.fillMaxSize().background(Color.White)) {
    val height= size.height
    val width= size.width
    val recheight=450.dp.toPx()
    val rectWidth=450.dp.toPx()

    drawArc(startAngle = 0f, sweepAngle = -180f, useCenter = true, color = Color.Green
    , size = Size(height/2,width/2)
    )

    drawCircle(center = Offset(height.times(0.3f),width.times(0.9f))
        , color = Color.Red, radius = 200f)

    drawCircle(center = Offset(height.times(0.1f),width.times(0.9f))
        , color = Color.Blue, radius = 200f)

    drawRect(color = Color.Yellow, size = Size(height = recheight, width = rectWidth),
        topLeft = Offset(x = height/2, y = width/2)

    )

}

}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DefaultPreview(){
    AndroidBot()
}