
package com.example.pictosay.ui.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pictosay.R

@Composable
fun SplashScreen() {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F9FC))
    ) {

        // CONTENIDO PRINCIPAL: PICTOSAY
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Image(
                painter = painterResource(
                    id = R.drawable.pictosay_logo
                ),
                contentDescription = "Logo PicToSay",
                modifier = Modifier
                    .fillMaxWidth(0.82f)
                    .height(250.dp),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(35.dp))

            CircularProgressIndicator(
                color = Color(0xFF3978D5),
                strokeWidth = 4.dp
            )
        }

        // FOOTER INSTITUCIONAL: M × GAUSS
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(
                    horizontal = 24.dp,
                    vertical = 35.dp
                ),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {

            // LOGO M
            Image(
                painter = painterResource(
                    id = R.drawable.logo_m
                ),
                contentDescription = "Logotipo M",
                modifier = Modifier
                    .width(75.dp)
                    .height(48.dp),
                contentScale = ContentScale.Fit
            )

            // SEPARADOR
            Text(
                text = "×",
                fontSize = 25.sp,
                fontWeight = FontWeight.Light,
                color = Color(0xFF092B70),
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            // LOGO GAUSS
            Image(
                painter = painterResource(
                    id = R.drawable.logo_gauss
                ),
                contentDescription = "Centro Pedagógico Gauss",
                modifier = Modifier
                    .width(78.dp)
                    .height(65.dp),
                contentScale = ContentScale.Fit
            )
        }
    }
}
