
package com.example.pictosay.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.pictosay.R

@Composable
fun Mascot(
    gender: String,
    modifier: Modifier = Modifier,
    size: Dp = 100.dp
) {
    val image = if (gender.equals("GIRL", ignoreCase = true)) {
        R.drawable.mascot_pink
    } else {
        R.drawable.mascot_blue
    }

    Image(
        painter = painterResource(id = image),
        contentDescription = "Mascota de PicToSay",
        modifier = modifier.size(size),
        contentScale = ContentScale.Fit
    )
}
