package com.plntpls.appssxdc.presentation.panels

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.plntpls.appssxdc.R
import com.plntpls.appssxdc.core.config.GameConfig
import com.plntpls.appssxdc.core.theme.BorderHair
import com.plntpls.appssxdc.core.theme.Primary
import com.plntpls.appssxdc.core.theme.SurfaceTint
import com.plntpls.appssxdc.core.theme.TextMuted
import com.plntpls.appssxdc.core.theme.TextPrimary
import com.plntpls.appssxdc.core.theme.TextSecondary
import com.plntpls.appssxdc.core.ui.PanelScaffold
import com.plntpls.appssxdc.core.ui.PaperCard
import com.plntpls.appssxdc.core.ui.PrimaryButton
import com.plntpls.appssxdc.core.ui.careKindRes
import com.plntpls.appssxdc.domain.model.CareEntry
import com.plntpls.appssxdc.domain.model.CareKind

@Composable
fun CareLogPanel(
    visible: Boolean,
    entries: List<CareEntry>,
    today: Int,
    composeMode: Boolean,
    draft: String,
    plantName: String?,
    onDraftChange: (String) -> Unit,
    onSaveNote: () -> Unit,
    onDismiss: () -> Unit
) {
    val title = if (composeMode) {
        stringResource(R.string.log_compose_title)
    } else {
        stringResource(R.string.log_title)
    }
    PanelScaffold(visible = visible, title = title, onDismiss = onDismiss) {
        if (composeMode) {
            Text(
                text = plantName.orEmpty(),
                style = MaterialTheme.typography.titleMedium,
                color = Primary
            )
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                value = draft,
                onValueChange = onDraftChange,
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                label = {
                    Text(
                        text = stringResource(R.string.log_note_label),
                        style = MaterialTheme.typography.labelSmall
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary,
                    focusedContainerColor = SurfaceTint,
                    unfocusedContainerColor = SurfaceTint,
                    focusedBorderColor = Primary,
                    unfocusedBorderColor = BorderHair,
                    cursorColor = Primary,
                    focusedLabelColor = TextSecondary,
                    unfocusedLabelColor = TextSecondary
                ),
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = stringResource(R.string.log_counter, draft.length, GameConfig.MAX_NOTE_CHARS),
                style = MaterialTheme.typography.labelSmall,
                color = TextMuted,
                textAlign = TextAlign.End,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 6.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TagChip(
                    text = stringResource(R.string.tag_new_leaf),
                    onClick = { onDraftChange("NEW LEAF") },
                    modifier = Modifier.weight(1f)
                )
                TagChip(
                    text = stringResource(R.string.tag_dry_tips),
                    onClick = { onDraftChange("DRY TIPS") },
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TagChip(
                    text = stringResource(R.string.tag_repotted),
                    onClick = { onDraftChange("REPOTTED") },
                    modifier = Modifier.weight(1f)
                )
                TagChip(
                    text = stringResource(R.string.tag_fed),
                    onClick = { onDraftChange("FED") },
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.height(18.dp))
            PrimaryButton(
                text = stringResource(R.string.cta_save_note),
                onClick = onSaveNote,
                enabled = draft.isNotBlank(),
                height = 56.dp
            )
        } else if (entries.isEmpty()) {
            PaperCard(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(18.dp)
            ) {
                Text(
                    text = stringResource(R.string.log_empty),
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextMuted
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(items = entries, key = { it.id }) { entry ->
                    LogRow(entry = entry, today = today)
                }
            }
        }
    }
}

@Composable
private fun LogRow(entry: CareEntry, today: Int) {
    val ago = today - entry.dayStamp
    val secondary = if (entry.note.isEmpty()) kindLabel(entry) else entry.note
    PaperCard(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp),
        corner = 16.dp,
        contentPadding = PaddingValues(horizontal = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(careKindRes(entry.kind)),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.size(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = entry.plantName,
                    style = MaterialTheme.typography.titleSmall,
                    color = TextPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = secondary,
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Text(
                text = if (ago <= 0) {
                    stringResource(R.string.log_today)
                } else {
                    stringResource(R.string.days_ago_short, ago)
                },
                style = MaterialTheme.typography.labelSmall,
                color = TextMuted
            )
        }
    }
}

@Composable
private fun kindLabel(entry: CareEntry): String = when (entry.kind) {
    CareKind.WATER -> stringResource(R.string.kind_water)
    CareKind.MIST -> stringResource(R.string.kind_mist)
    CareKind.FEED -> stringResource(R.string.kind_feed)
}

@Composable
private fun TagChip(text: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .height(48.dp)
            .background(color = SurfaceTint, shape = RoundedCornerShape(14.dp))
            .border(width = 1.dp, color = BorderHair, shape = RoundedCornerShape(14.dp))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = TextSecondary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}
