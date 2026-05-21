package com.loom.feature.home.impl

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.loom.core.designsystem.R.drawable
import com.loom.core.designsystem.theme.LoomTheme

@Composable
fun HomeTopBar(modifier: Modifier = Modifier) {
    Surface(
        modifier =  modifier
            .fillMaxWidth()
            .height(48.dp)
        ,
        color = Color.Transparent
    ) {
        Row(
            modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                painter = painterResource(id = drawable.r_o_o_m_2),
                contentDescription = "Logo",
                modifier = Modifier.size(40.dp).padding(2.dp),
                contentScale = ContentScale.Fit,
            )
        }
    }
}

@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun HomeTopBarPreview() {
    LoomTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            HomeTopBar()
        }
    }
}
