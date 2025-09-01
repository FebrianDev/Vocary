package com.febriandev.vocary.ui.content

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Topic
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.composables.icons.lucide.GraduationCap
import com.composables.icons.lucide.Lucide
import com.febriandev.vocary.ui.components.CardButton
import com.febriandev.vocary.ui.components.CustomAnimatedModalSheet
import com.febriandev.vocary.ui.favorite.FavoriteActivity
import com.febriandev.vocary.ui.history.HistoryActivity
import com.febriandev.vocary.ui.minigame.MiniGameActivity
import com.febriandev.vocary.ui.search.MyOwnWordActivity
import com.febriandev.vocary.ui.search.SearchVocabularyActivity

@Composable
fun ContentScreen(
    isPremium: Boolean,
    showContent: Boolean,
    context: Context,
    onDismiss: () -> Unit
) {
    CustomAnimatedModalSheet(
        show = showContent,
        onDismiss = onDismiss
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = "Explore Content",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                CardButton(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.Favorite,
                    text = "Favorite",
                    onClick = {
                        val intent =
                            Intent(context.applicationContext, FavoriteActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        context.startActivity(intent)
                    }
                )
                Spacer(modifier = Modifier.width(16.dp))
                CardButton(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.History,
                    text = "History",
                    onClick = {
                        val intent =
                            Intent(context.applicationContext, HistoryActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        context.startActivity(intent)
                    }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            CardButton(
                modifier = Modifier.fillMaxWidth(),
                icon = Icons.Default.Search,
                text = "Search Vocabulary",
                isPremium = isPremium,
                onClick = {
                    val intent =
                        Intent(context.applicationContext, SearchVocabularyActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    context.startActivity(intent)
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            CardButton(
                modifier = Modifier.fillMaxWidth(),
                icon = Icons.Default.EditNote,
                text = "My Own Word",
                isPremium = isPremium,
                onClick = {
                    val intent = Intent(context.applicationContext, MyOwnWordActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    context.startActivity(intent)
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            CardButton(
                modifier = Modifier.fillMaxWidth(),
                icon = Lucide.GraduationCap,
                text = "Practice",
                isPremium = isPremium,
                onClick = {
                    val intent = Intent(context.applicationContext, MiniGameActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    context.startActivity(intent)
                }
            )

            Spacer(Modifier.height(24.dp))

            Text(
                text = "Explore Topic",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(Modifier.height(16.dp))

            CardButton(
                modifier = Modifier.fillMaxWidth(),
                icon = Icons.Default.ArrowUpward,
                text = "Change Level",
                isPremium = isPremium,
                onClick = {
                    val intent = Intent(context.applicationContext, ChangeLevelActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    context.startActivity(intent)
                }
            )

            Spacer(Modifier.height(12.dp))

            CardButton(
                modifier = Modifier.fillMaxWidth(),
                icon = Icons.Default.Topic,
                text = "Change Topic",
                isPremium = isPremium,
                onClick = {
                    val intent = Intent(context.applicationContext, ChangeTopicActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    context.startActivity(intent)
                }
            )
        }
    }
}