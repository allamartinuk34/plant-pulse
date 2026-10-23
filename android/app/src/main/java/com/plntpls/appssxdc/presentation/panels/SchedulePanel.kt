package com.plntpls.appssxdc.presentation.panels

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.plntpls.appssxdc.R
import com.plntpls.appssxdc.core.theme.BorderHair
import com.plntpls.appssxdc.core.theme.Primary
import com.plntpls.appssxdc.core.theme.Secondary
import com.plntpls.appssxdc.core.theme.SurfaceTint
import com.plntpls.appssxdc.core.theme.TextMuted
import com.plntpls.appssxdc.core.theme.TextPrimary
import com.plntpls.appssxdc.core.theme.TextSecondary
import com.plntpls.appssxdc.core.ui.PanelScaffold
import com.plntpls.appssxdc.core.ui.PaperCard
import com.plntpls.appssxdc.core.ui.careKindRes
import com.plntpls.appssxdc.core.ui.weekdayName
import com.plntpls.appssxdc.domain.model.WeekStart
import com.plntpls.appssxdc.domain.usecase.ScheduleDay

@Composable
fun SchedulePanel(
    visible: Boolean,
    days: List<ScheduleDay>,
    weekStart: WeekStart,
    onDismiss: () -> Unit
) {
    val ordered = if (weekStart == WeekStart.SUN && days.isNotEmpty()) {
        days.sortedBy { ((it.weekdayIndex + 1) % 7) }
    } else {
        days
    }
    val upcoming = days.flatMap { day -> day.slots.map { slot -> day to slot } }.take(7)

    PanelScaffold(
        visible = visible,
        title = stringResource(R.string.schedule_title),
        onDismiss = onDismiss,
        fillFraction = 0.70f
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            ordered.forEach { day ->
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = weekdayName(day.weekdayIndex),
                        style = MaterialTheme.typography.labelSmall,
                        color = if (day.offset == 0) Primary else TextSecondary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp)
                            .background(color = SurfaceTint, shape = RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        if (day.slots.isEmpty()) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .background(color = BorderHair, shape = RoundedCornerShape(3.dp))
                            )
                        } else {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                day.slots.take(2).forEach { slot ->
                                    Image(
                                        painter = painterResource(careKindRes(slot.kind)),
                                        contentDescription = null,
                                        contentScale = ContentScale.Fit,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(2.dp)
                            .background(
                                color = if (day.offset == 0) Secondary else Color.Transparent
                            )
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(18.dp))
        Text(
            text = stringResource(R.string.schedule_next),
            style = MaterialTheme.typography.labelSmall,
            color = TextSecondary
        )
        Spacer(modifier = Modifier.height(8.dp))
        if (upcoming.isEmpty()) {
            PaperCard(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(18.dp)
            ) {
                Text(
                    text = stringResource(R.string.schedule_empty),
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
                items(items = upcoming, key = { it.first.dayStamp.toString() + it.second.plantId + it.second.kind.name }) { pair ->
                    PaperCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        corner = 14.dp,
                        contentPadding = PaddingValues(horizontal = 12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(careKindRes(pair.second.kind)),
                                contentDescription = null,
                                contentScale = ContentScale.Fit,
                                modifier = Modifier.size(24.dp)
                            )
                            Text(
                                text = pair.second.plantName,
                                style = MaterialTheme.typography.titleSmall,
                                color = TextPrimary,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(start = 12.dp)
                            )
                            Text(
                                text = if (pair.first.offset == 0) {
                                    stringResource(R.string.schedule_today)
                                } else {
                                    stringResource(R.string.schedule_in_days, pair.first.offset)
                                },
                                style = MaterialTheme.typography.labelSmall,
                                color = TextMuted
                            )
                        }
                    }
                }
            }
        }
    }
}
