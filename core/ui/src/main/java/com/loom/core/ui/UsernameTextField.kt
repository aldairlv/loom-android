package com.loom.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp

@Composable
fun UsernameTextField(
    value: String,
    onValueChange: (String) -> Unit,
    hint: String,
    modifier: Modifier = Modifier,
    error: String? = null,
    onDone: () -> Unit = {}
) {

    Column {

        BasicTextField(
            value = value,
            onValueChange = {

                val filtered = it.replace(" ", "")

                onValueChange(filtered)
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = { onDone() }
            ),
            textStyle = TextStyle(color = Color.White),
            cursorBrush = SolidColor(Color.White),
            modifier = modifier
                .fillMaxWidth()
                .height(56.dp)
                .background(
                    Color(0xFF1E293B),
                    RoundedCornerShape(12.dp)
                )
                .border(
                    width = 1.dp,
                    color = if(error != null)
                        Color.Red
                    else
                        Color.Transparent,
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(horizontal = 16.dp),
            decorationBox = { innerTextField ->

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = "@",
                        color = Color.Gray,
                        style = MaterialTheme.typography.bodyLarge
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Box {

                        if(value.isEmpty()) {
                            Text(
                                text = hint,
                                color = Color.Gray
                            )
                        }

                        innerTextField()
                    }
                }
            }
        )

        if(error != null) {
            Text(
                text = error,
                color = Color(0xFFFF6B6B),
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}