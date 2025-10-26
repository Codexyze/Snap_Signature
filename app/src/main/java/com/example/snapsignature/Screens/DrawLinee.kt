package com.example.snapsignature.Screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color

@Composable
fun DrawLine(modifier: Modifier = Modifier) {
    //Drawing line composable
    Canvas(modifier = Modifier.fillMaxSize()) {
        val canvuswidth= size.width
        val canvusheight= size.height
        drawLine(
            color = Color.Red,
            //Starting (0,0)
            //took x to 0 no change y to height/2 ) First pt goes down
            start = Offset(x=0f , y=canvusheight/2 ),
            //took x to canvusw to get itto b point at end (width end)
            end = Offset(x=canvuswidth , y= canvusheight/2),
            strokeWidth = 10f
        )
    }
}

@Composable
fun DrawLine2(modifier: Modifier = Modifier) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val height = size.height
        val width = size.width
        drawLine(
            color = Color.Green,
            start = Offset(x= 0f,y= 0f) ,
            end = Offset(x=width , y= height) ,
            strokeWidth = 10f
        )
    }

}

@Composable
fun DrawLine3(modifier: Modifier = Modifier) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val height = size.height
        val width = size.width
        drawLine(
            color = Color.Green,
            start = Offset(x= width/2,y= 0f) ,
            end = Offset(x= width/2 , y= height) ,
            strokeWidth = 10f
        )
    }

}