package com.loom.core.designsystem.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.R
import com.loom.core.designsystem.R.drawable
/**
 * Loom Top App Bar con título centrado.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoomTopAppBar(
    titleRes: Int,
    modifier: Modifier = Modifier,
) {
    CenterAlignedTopAppBar(
        navigationIcon = {
            Image(
                painter = painterResource(id = drawable.r_o_o_m_2),
                contentDescription = "Logo de la app",
                // Aquí controlas el tamaño. Ajusta los dp a tu gusto
                modifier = Modifier
                    .size(40.dp),
                // Importante: Esto asegura que la imagen se ajuste bien sin deformarse
                contentScale = ContentScale.Fit
            )
        },
        title = { Text(text = stringResource(id = titleRes)) },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,
            scrolledContainerColor = Color.Unspecified,
            navigationIconContentColor = Color.Unspecified,
            titleContentColor = Color.Unspecified,
            actionIconContentColor = Color.Unspecified
        ),
        modifier = modifier
    )
}