package com.febriandev.vocary.ui.shimmer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun ItemShimmer() {
    Card(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            // Title + phonetic row
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                ShimmerEffect(
                    modifier = Modifier
                        .height(20.dp)
                        .width(120.dp)
                        .clip(RoundedCornerShape(4.dp))
                )

//                Row(
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    ShimmerEffect(
//                        modifier = Modifier
//                            .height(16.dp)
//                            .width(60.dp)
//                            .clip(RoundedCornerShape(4.dp))
//                    )
//                    Spacer(modifier = Modifier.width(8.dp))
//                    Icon(
//                        imageVector = Icons.Default.VolumeUp,
//                        contentDescription = null,
//                        tint = Color.LightGray,
//                        modifier = Modifier.size(20.dp)
//                    )
//                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Definition
            ShimmerEffect(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(14.dp)
                    .clip(RoundedCornerShape(4.dp))
            )

            Spacer(modifier = Modifier.height(4.dp))

            ShimmerEffect(
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(14.dp)
                    .clip(RoundedCornerShape(4.dp))
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Timestamp
            ShimmerEffect(
                modifier = Modifier
                    .height(12.dp)
                    .width(100.dp)
                    .clip(RoundedCornerShape(4.dp))
            )
        }
    }
}
