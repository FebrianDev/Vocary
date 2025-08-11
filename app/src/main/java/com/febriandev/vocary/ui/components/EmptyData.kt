package com.febriandev.vocary.ui.components


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.febriandev.vocary.R

@Composable
fun EmptyData() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(1f))
        Image(
            modifier = Modifier.size(256.dp),
            painter = painterResource(id = R.drawable.no_data),
            contentDescription = "No Data",
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
        )
        Spacer(modifier = Modifier.weight(1f))
    }

}