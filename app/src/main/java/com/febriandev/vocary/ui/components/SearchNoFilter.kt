package com.febriandev.vocary.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.febriandev.vocary.R


@Composable
fun SearchNoFilter(
    searchQuery: String,
    onSearchChange: (String) -> Unit,
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchChange,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(12.dp),
            textStyle = TextStyle(
                fontSize = 13.sp,
                color = Color.Gray
            ),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = stringResource(R.string.search_icon),
                    modifier = Modifier
                        .padding(start = 4.dp)
                        .size(24.dp),
                    tint = Color(0xFFBDBEBF)
                )
            },
            placeholder = {
                Text(
                    text = stringResource(R.string.search_icon),
                    fontSize = 13.sp,
                    //     fontFamily = FontFamily(Font(R.font.publicsans_regular)),
                    style = TextStyle(
                        platformStyle = PlatformTextStyle(includeFontPadding = false)
                    ),
                    color = Color.Gray
                )
            },
            colors = TextFieldDefaults.colors(
                focusedLabelColor = MaterialTheme.colorScheme.onSurface,
                cursorColor = MaterialTheme.colorScheme.onSurface,
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                focusedIndicatorColor = MaterialTheme.colorScheme.onSurface,
               // unfocusedIndicatorColor = MaterialTheme.colorScheme.onSurface
            ),
            singleLine = true,
        )
    }
}