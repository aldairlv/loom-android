package com.loom.feature.auth.impl

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.loom.core.designsystem.icon.LoomIcons
import com.loom.core.ui.BirthDateDropdown
import com.loom.core.ui.NextActionButton

@Composable
fun CompleteRegisterBirthDateScreen(
    modifier: Modifier = Modifier,
    viewModel: AuthViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    onNextClickRegister: (
        email: String,
        password: String,
        confirmPassword: String,
        birthMonth: String,
        birthDay: String,
        birthYear: String
    ) -> Unit,
    email: String,
    password: String,
    confirmPassword: String
) {

    val month by viewModel.birthMonth.collectAsStateWithLifecycle()
    val day by viewModel.birthDay.collectAsStateWithLifecycle()
    val year by viewModel.birthYear.collectAsStateWithLifecycle()

    CompleteRegisterBirthDateScreen(
        modifier = modifier,
        month = month,
        day = day,
        year = year,
        onMonthSelected = viewModel::onBirthMonthChanged,
        onDaySelected = viewModel::onBirthDayChanged,
        onYearSelected = viewModel::onBirthYearChanged,
        onBackClick = onBackClick,
        onNextClick = {
            onNextClickRegister(
                email,
                password,
                confirmPassword,
                month,
                day,
                year
            )
        }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CompleteRegisterBirthDateScreen(
    modifier: Modifier = Modifier,
    month: String = "",
    day: String = "",
    year: String = "",
    onMonthSelected: (String) -> Unit = {},
    onDaySelected: (String) -> Unit = {},
    onYearSelected: (String) -> Unit = {},
    onBackClick: () -> Unit = {},
    onNextClick: () -> Unit = {}
) {

    val months = listOf(
        "01" to "Mes",
        "02" to "Feb",
        "03" to "Mar",
        "04" to "Abr",
        "05" to "May",
        "06" to "Jun",
        "07" to "Jul",
        "08" to "Ago",
        "09" to "Sep",
        "10" to "Oct",
        "11" to "Nov",
        "12" to "Dic"
    )

    val days = (1..31).map {
        it.toString().padStart(2, '0')
    }

    val years = (2026 downTo 1900).map {
        it.toString()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF020817))
    ) {

        Column {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "room",
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = LoomIcons.ArrowBack,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Transparent
                )
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .safeDrawingPadding()
                    .imePadding()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Spacer(modifier = Modifier.height(48.dp))

                Text(
                    text = "¿Cuándo es tu cumpleaños?",
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "No compartiremos este dato con la comunidad: solo queremos asegurarnos de que tienes la edad suficiente para usar Room.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF94A3B8),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(32.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    BirthDateDropdown(
                        modifier = Modifier.weight(1.2f),
                        placeholder = "Mes",
                        selectedValue = month,
                        items = months.map { it.second },
                        onItemSelected = { selectedLabel ->

                            val selectedMonth =
                                months.firstOrNull { it.second == selectedLabel }?.first
                                    ?: ""

                            onMonthSelected(selectedMonth)
                        }
                    )

                    BirthDateDropdown(
                        modifier = Modifier.weight(0.8f),
                        placeholder = "Día",
                        selectedValue = day,
                        items = days,
                        onItemSelected = onDaySelected
                    )

                    BirthDateDropdown(
                        modifier = Modifier.weight(1f),
                        placeholder = "Año",
                        selectedValue = year,
                        items = years,
                        onItemSelected = onYearSelected
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = "Al pulsar el botón y seguir adelante:",
                    color = Color(0xFF94A3B8),
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    verticalAlignment = Alignment.Top
                ) {

                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier
                            .padding(top = 2.dp)
                            .size(20.dp)
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Text(
                        text = buildAnnotatedString {

                            append("Aceptas las ")

                            pushStringAnnotation(
                                tag = "terms",
                                annotation = "terms"
                            )

                            withStyle(
                                style = SpanStyle(
                                    color = Color.White,
                                    textDecoration = TextDecoration.Underline,
                                    fontWeight = FontWeight.SemiBold
                                )
                            ) {
                                append("Condiciones del servicio")
                            }

                            pop()

                            append(" y confirmas que has leído nuestra ")

                            pushStringAnnotation(
                                tag = "privacy",
                                annotation = "privacy"
                            )

                            withStyle(
                                style = SpanStyle(
                                    color = Color.White,
                                    textDecoration = TextDecoration.Underline,
                                    fontWeight = FontWeight.SemiBold
                                )
                            ) {
                                append("Política de privacidad")
                            }

                            pop()

                            append(".")
                        },
                        color = Color(0xFF94A3B8),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                NextActionButton(
                    text = "Siguiente",
                    enabled = month.isNotBlank()
                            && day.isNotBlank()
                            && year.isNotBlank(),
                    onClick = onNextClick
                )
            }
        }
    }
}