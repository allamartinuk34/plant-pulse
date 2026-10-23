package com.plntpls.appssxdc.presentation.dialog

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.plntpls.appssxdc.R
import com.plntpls.appssxdc.core.theme.Alert
import com.plntpls.appssxdc.core.theme.Primary
import com.plntpls.appssxdc.core.theme.SurfacePaper
import com.plntpls.appssxdc.core.theme.TextPrimary
import com.plntpls.appssxdc.core.theme.TextSecondary

@Composable
fun ConfirmResetDialog(
    visible: Boolean,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    if (!visible) return
    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(22.dp),
        containerColor = SurfacePaper,
        titleContentColor = TextPrimary,
        textContentColor = TextSecondary,
        title = {
            Text(
                text = stringResource(R.string.reset_title),
                style = MaterialTheme.typography.headlineSmall
            )
        },
        text = {
            Text(
                text = stringResource(R.string.reset_body),
                style = MaterialTheme.typography.bodyMedium
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(
                    text = stringResource(R.string.cta_reset),
                    style = MaterialTheme.typography.labelLarge,
                    color = Alert
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = stringResource(R.string.cta_cancel),
                    style = MaterialTheme.typography.labelLarge,
                    color = Primary
                )
            }
        }
    )
}
