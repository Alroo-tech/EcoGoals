package com.example.tusecogoals.ui.theme
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.tusecogoals.R
import com.example.tusecogoals.ui.theme.MossGreen
import com.example.tusecogoals.ui.theme.WhiteColor

@Composable
fun GradientBackground(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Background image
        Image(
            painter = painterResource(id = R.drawable.tuscampus), // Replace with your image
            contentDescription = "Background Image",
            contentScale = ContentScale.Crop, // Scale to fill the screen
            modifier = Modifier.fillMaxSize()
        )

        // Gradient overlay
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawRect(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.Transparent,  // Fades to transparent
                        MossGreen // SeaFoamGreen
                    ),
                    startY = 0f,
                    endY = size.height / 1.1f // Adjust gradient size
                ),
                size = size
            )
        }

        // Overlay the content on top of the gradient and image
        content()
    }
}
