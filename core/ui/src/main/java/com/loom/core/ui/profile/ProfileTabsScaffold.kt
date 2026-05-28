package com.loom.core.ui.profile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.zIndex
import com.loom.core.ui.tabs.TabItem
import android.util.Log
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.IntOffset
import kotlin.math.roundToInt
/*
@Composable
fun ProfileTabsScaffold(
    tabs: List<TabItem>,
    header: @Composable () -> Unit,
    tabsBar: @Composable (PagerState) -> Unit,
    scrollState: ProfileScrollState,
    modifier: Modifier = Modifier,
    pageContent: @Composable (
        Int,
        TabItem,
        Dp,
        Boolean // isActive
    ) -> Unit,
) {

    val pagerState =
        rememberPagerState {
            tabs.size
        }

    var headerHeightPx by remember {
        mutableIntStateOf(0)
    }

    var tabHeightPx by remember {
        mutableIntStateOf(0)
    }

    LaunchedEffect(headerHeightPx, tabHeightPx) {
        Log.d("ProfileScroll", "ProfileTabsScaffold: Heights changed -> header=$headerHeightPx, tab=$tabHeightPx")
        scrollState.setHeights(
            headerHeightPx.toFloat(),
            tabHeightPx.toFloat()
        )
    }

    val activeScrollY =
        scrollState.scrollYForPage(
            pagerState.currentPage
        )

    val headerOffsetPx =
        minOf(-activeScrollY, scrollState.tabOffsetPx)

    val headerBottomPx =
        headerOffsetPx + headerHeightPx

    val naturalTabY =
        headerHeightPx.toFloat() +
                scrollState.tabOffsetPx

    val tabY = if (headerHeightPx == 0 || tabHeightPx == 0) {
        -1000f
    } else {
        maxOf(
            headerBottomPx,
            naturalTabY.coerceIn(-tabHeightPx.toFloat(), 0f)
        )
    }

    val pagerOffsetPx =
        minOf(0f, scrollState.tabOffsetPx + activeScrollY)
    
    LaunchedEffect(pagerState.currentPage, pagerOffsetPx, tabY, headerOffsetPx) {
        Log.v("ProfileScroll", "UI Sync | Page: ${pagerState.currentPage} | headerOffset: $headerOffsetPx | tabY: $tabY | pagerOffset: $pagerOffsetPx")
    }

    val contentTopPadding =
        with(LocalDensity.current) {
            (headerHeightPx + tabHeightPx).toDp()
        }

    Box(
        modifier = modifier.fillMaxSize()
    ) {

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    translationY = pagerOffsetPx
                },
            key = {
                tabs[it].key
            }
        ) { page ->

            pageContent(
                page,
                tabs[page],
                contentTopPadding,
                pagerState.currentPage == page
            )
        }

        Box(
            modifier = Modifier
                .zIndex(2f)
                .onSizeChanged {
                    headerHeightPx = it.height
                }
                .graphicsLayer {
                    translationY = headerOffsetPx
                }
        ) {
            header()
        }

        Box(
            modifier = Modifier
                .zIndex(3f)
                .onSizeChanged {
                    tabHeightPx = it.height
                }
                .graphicsLayer {
                    translationY = tabY
                }
        ) {
            tabsBar(pagerState)
        }
    }
}
*/
/*
@Composable
fun ProfileTabsScaffold(
    modifier: Modifier = Modifier,
    tabs: List<TabItem>,
    tabsBar: @Composable (PagerState, Float) -> Unit,
    pageContent: @Composable (Int, TabItem, Dp) -> Unit,
) {

    val pagerState = rememberPagerState { tabs.size }
    val scrollState = rememberProfileScrollState()

    val nestedScroll = rememberTabNestedScrollConnection(scrollState)

    val density = LocalDensity.current

    var tabHeightPx by remember { mutableIntStateOf(0) }

    LaunchedEffect(tabHeightPx) {
        scrollState.setTabHeight(tabHeightPx.toFloat())
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .nestedScroll(nestedScroll)
    ) {

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->

            pageContent(
                page,
                tabs[page],
                with(density) { tabHeightPx.toDp() }
            )
        }

        // GLOBAL TAB ROW
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .onSizeChanged {
                    tabHeightPx = it.height
                }
                .offset {
                    IntOffset(
                        0,
                        scrollState.tabOffsetPx.roundToInt()
                    )
                }
        ) {
            tabsBar(
                pagerState,
                scrollState.tabOffsetPx
            )
        }
    }
}

*/




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomCollapsingHeader(
    scrollBehavior: TopAppBarScrollBehavior,
    onHeightKnown: (Int) -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val offset = scrollBehavior.state.heightOffset
    val collapsedFraction = scrollBehavior.state.collapsedFraction

    Box(
        modifier = modifier
            .fillMaxWidth()
            // USAMOS .layout EN LUGAR DE .graphicsLayer PARA MOVER EL TABBAR
            .layout { measurable, constraints ->
                val placeable = measurable.measure(constraints)
                // La altura en el layout disminuye conforme hacemos scroll
                val currentHeight = (placeable.height + offset).roundToInt().coerceAtLeast(0)

                layout(placeable.width, currentHeight) {
                    // Dibujamos el contenido desplazado hacia arriba
                    placeable.placeRelative(0, offset.roundToInt())
                }
            }
            .graphicsLayer {
                // El alpha y el recorte se quedan en la capa gráfica por eficiencia
                alpha = 1f - collapsedFraction
                clip = true
                Log.d(
                    "DEBUG_HEADER_MOVE",
                    "Header layout offset: $offset, alpha: ${1f - collapsedFraction}"
                )
            },
    ) {
        // Caja interna para medir el tamaño REAL del contenido sin el recorte del layout
        Box(modifier = Modifier.onSizeChanged { size ->
            Log.d("DEBUG_HEADER", "Content real height: ${size.height}")
            onHeightKnown(size.height)
        }) {
            content()
        }
    }
}
