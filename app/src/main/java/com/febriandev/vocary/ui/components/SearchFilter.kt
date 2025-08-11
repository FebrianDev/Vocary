package com.febriandev.vocary.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchFilter(
    searchQuery: String,
    onSearchChange: (String) -> Unit,
    onShowSortClick: () -> Unit,
) {
    // Column {
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
                .fillMaxWidth(0.82f)
                .height(48.dp),
            shape = RoundedCornerShape(12.dp),
            textStyle = TextStyle(
                fontSize = 13.sp,
            ),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search Icon",
                    modifier = Modifier
                        .padding(start = 4.dp)
                        .size(24.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            },
            placeholder = {
                Text(
                    text = "Search Icon",
                    fontSize = 13.sp,
                    //     fontFamily = FontFamily(Font(R.font.publicsans_regular)),
                    style = TextStyle(
                        platformStyle = PlatformTextStyle(includeFontPadding = false)
                    ),
                    color = Color.Gray
                )
            },
            colors = TextFieldDefaults.colors(
                focusedLabelColor = MaterialTheme.colorScheme.primary,
                cursorColor = MaterialTheme.colorScheme.primary,
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                focusedIndicatorColor = MaterialTheme.colorScheme.primary,
               unfocusedIndicatorColor = MaterialTheme.colorScheme.primary
            ),
            singleLine = true,
        )

        Box(
            modifier = Modifier
                .width(54.dp)
                .height(48.dp)
                .border(2.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(12.dp))
                .clickable { onShowSortClick.invoke() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.Sort,
                contentDescription = "Search Filter",
                modifier = Modifier.size(32.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }

//        if (showBottomSheet) {
//            ModalBottomSheet(
//                onDismissRequest = onDismissBottomSheet,
//                containerColor = Color.White,
//                sheetState = sheetState,
//            ) {
    // Filter Section
//                TextMedium(
//                    text = stringResource(R.string.tags),
//                    color = Color(0xFF6F7274),
//                    size = 16.sp,
//                    modifier = Modifier.padding(horizontal = 16.dp),
//                )
//
//                FlowRow(
//                    modifier = Modifier
//                        .padding(horizontal = 16.dp)
//                        .padding(top = 8.dp, bottom = 64.dp)
//                ) {
//                    placeTags.forEachIndexed { index, tag ->
//                        ItemTag(
//                            tag = tag,
//                            isHighlight = index in selectedTagIndices
//                        ) {
//                            onTagSelected(index, index in selectedTagIndices)
//                        }
//                    }
//                }
    //           }
    //       }
    // }
}
