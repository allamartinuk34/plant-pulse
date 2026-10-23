package com.plntpls.appssxdc.core.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

data class DockAction(val text: String, val onClick: () -> Unit, val enabled: Boolean = true)

@Composable
fun BottomActionDock(
    primaryText: String,
    onPrimary: () -> Unit,
    modifier: Modifier = Modifier,
    primaryEnabled: Boolean = true,
    primaryIconRes: Int? = null,
    secondary: List<DockAction> = emptyList()
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 34.dp)
    ) {
        PrimaryButton(
            text = primaryText,
            onClick = onPrimary,
            enabled = primaryEnabled,
            iconRes = primaryIconRes,
            height = 66.dp
        )
        if (secondary.isNotEmpty()) {
            Spacer(modifier = Modifier.height(20.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                secondary.forEach { action ->
                    SecondaryButton(
                        text = action.text,
                        onClick = action.onClick,
                        enabled = action.enabled,
                        height = 56.dp,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}
