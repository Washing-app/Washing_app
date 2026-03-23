package com.example.machines.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.domain.model.WashProgram
import com.example.ui.R

@Composable
fun MachineCard(
    program: WashProgram,
    onClick: (WashProgram) -> Unit
) {
    Card(
        border = BorderStroke(
            width = 1.dp,
            color = colorResource(R.color.card_stroke)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(program) },
        colors = CardDefaults.cardColors(
            containerColor = colorResource(R.color.card_background),
        ),
        shape = RoundedCornerShape(16.dp)
    ) {

        Box(modifier = Modifier
            .fillMaxSize()) {

            AsyncImage(
                model = program.imageLink,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .matchParentSize()
            )

            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(Color.Black.copy(alpha = 0.3f))
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier
                        .background(
                            color = Color.Gray.copy(alpha = 0.5f),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 4.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = program.name,
                        color = Color.White,
                        style = MaterialTheme.typography.titleMedium
                    )
                }

                Spacer(Modifier.height(8.dp))

                Column(
                    modifier = Modifier
                        .background(
                            color = Color.Gray.copy(alpha = 0.5f),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(8.dp)
                ) {
                    Text("${program.temperature}°C", color = Color.White)
                    Text("${program.durationMinutes} мин", color = Color.White)
                    Text("${program.price} ₽", color = Color.White)
                }
        }
    }
}
}