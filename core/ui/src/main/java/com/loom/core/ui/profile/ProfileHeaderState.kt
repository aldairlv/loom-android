package com.loom.core.ui.profile

import android.util.Log
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource

/*
import androidx.compose.runtime.*

@Stable
class ProfileTabScrollState {
    // Scroll local de cada pager
    var scrollY by mutableFloatStateOf(0f)
        private set

    fun updateScroll(newScrollY: Float) {
        scrollY = newScrollY
    }
}

@Stable
class ProfileScrollState {
    // Estado global del tab (Quick Return compartido)
    var tabOffsetPx by mutableFloatStateOf(0f)
        private set

    var hiddenOffsetPx by mutableFloatStateOf(0f)

    // Un estado por pager
    private val _pagerStates = mutableMapOf<Int, ProfileTabScrollState>()

    fun getOrCreatePagerState(page: Int): ProfileTabScrollState {
        return _pagerStates.getOrPut(page) { ProfileTabScrollState() }
    }

    fun updateScroll(page: Int, newScrollY: Float) {
        val pagerState = getOrCreatePagerState(page)
        val delta = pagerState.scrollY - newScrollY

        // Quick Return global — se mueve con cualquier pager activo
        tabOffsetPx = (tabOffsetPx + delta).coerceIn(-hiddenOffsetPx, 0f)

        pagerState.updateScroll(newScrollY)
    }

    fun scrollYForPage(page: Int): Float {
        return getOrCreatePagerState(page).scrollY
    }
}

@Composable
fun rememberProfileScrollState(): ProfileScrollState {
    return remember { ProfileScrollState() }
}
*/


/*
import androidx.compose.runtime.*

@Stable
class ProfileTabScrollState {
    var scrollY by mutableFloatStateOf(0f)
        private set

    fun updateScroll(newScrollY: Float) {
        scrollY = newScrollY
    }
}

@Stable
class ProfileScrollState {
    var tabOffsetPx by mutableFloatStateOf(0f)
        private set

    var hiddenOffsetPx by mutableFloatStateOf(0f)

    private val _pagerStates = mutableMapOf<Int, ProfileTabScrollState>()

    fun getOrCreatePagerState(page: Int): ProfileTabScrollState {
        return _pagerStates.getOrPut(page) { ProfileTabScrollState() }
    }

    fun updateScroll(page: Int, newScrollY: Float) {
        val pagerState = getOrCreatePagerState(page)
        val delta = pagerState.scrollY - newScrollY

        // Quick Return global afectando a todas las pestañas por igual
        tabOffsetPx = (tabOffsetPx + delta).coerceIn(-hiddenOffsetPx, 0f)

        pagerState.updateScroll(newScrollY)
    }

    fun scrollYForPage(page: Int): Float {
        return getOrCreatePagerState(page).scrollY
    }
}


@Composable
fun rememberProfileScrollState(): ProfileScrollState {
    return remember { ProfileScrollState() }
}
*/
/*
@Stable
class ProfilePageScrollState {

    var scrollY by mutableFloatStateOf(0f)
        private set

    fun updateScroll(value: Float) {
        scrollY = value
    }
}

@Stable
class ProfileScrollState {

    private val pages = mutableMapOf<Int, ProfilePageScrollState>()

    private var maxTabCollapsePx = 0f

    var tabOffsetPx by mutableFloatStateOf(0f)
        private set

    fun setTabHeight(heightPx: Float) {
        maxTabCollapsePx = heightPx
    }

    fun pageState(page: Int): ProfilePageScrollState {
        return pages.getOrPut(page) {
            ProfilePageScrollState()
        }
    }

    fun scrollYForPage(page: Int): Float {
        return pageState(page).scrollY
    }

    fun updateScroll(page: Int, newScrollY: Float) {

        val state = pageState(page)

        val delta = state.scrollY - newScrollY

        tabOffsetPx =
            (tabOffsetPx + delta)
                .coerceIn(-maxTabCollapsePx, 0f)

        state.updateScroll(newScrollY)
    }
}

@Composable
fun rememberProfileScrollState(): ProfileScrollState {
    return remember {
        ProfileScrollState()
    }
}

*/


/*
import android.util.Log

@Stable
class ProfilePageScrollState {

    var scrollY by mutableFloatStateOf(0f)
        private set

    var lastIndex by mutableIntStateOf(0)
        private set

    var lastOffset by mutableIntStateOf(0)
        private set

    fun update(
        index: Int,
        offset: Int,
        scrollY: Float,
    ) {
        lastIndex = index
        lastOffset = offset
        this.scrollY = scrollY
    }
}

@Stable
class ProfileScrollState {

    private val pages =
        mutableMapOf<Int, ProfilePageScrollState>()

    private var headerHeightPx = 0f
    private var tabHeightPx = 0f

    private val maxScrollOffsetPx get() = headerHeightPx + tabHeightPx

    var tabOffsetPx by mutableFloatStateOf(0f)
        private set

    fun setHeights(headerHeight: Float, tabHeight: Float) {
        if (headerHeight != headerHeightPx || tabHeight != tabHeightPx) {
            Log.d("ProfileScroll", "setHeights: header=$headerHeight, tab=$tabHeight")
            headerHeightPx = headerHeight
            tabHeightPx = tabHeight
        }
    }

    fun pageState(page: Int): ProfilePageScrollState {
        return pages.getOrPut(page) {
            ProfilePageScrollState()
        }
    }

    fun scrollYForPage(page: Int): Float {
        return pageState(page).scrollY
    }

    fun updateScroll(
        page: Int,
        index: Int,
        offset: Int,
        isCurrentPage: Boolean,
    ) {
        val state = pageState(page)

        val delta =
            when {
                index == state.lastIndex -> {
                    (state.lastOffset - offset).toFloat()
                }
                index > state.lastIndex -> {
                    -maxScrollOffsetPx
                }
                else -> {
                    maxScrollOffsetPx
                }
            }

        val oldTabOffset = tabOffsetPx
        
        // Solo la página activa puede "empujar" el estado global
        if (isCurrentPage) {
            tabOffsetPx =
                (tabOffsetPx + delta)
                    .coerceIn(
                        minimumValue = -maxScrollOffsetPx,
                        maximumValue = 0f
                    )
        }

        val absoluteScroll = index * 100_000f + offset
        
        if (isCurrentPage && delta != 0f) {
            Log.v("ProfileScroll", "Active Page $page | index: $index, offset: $offset | delta: $delta | tabOffset: $oldTabOffset -> $tabOffsetPx")
        }

        state.update(
            index = index,
            offset = offset,
            scrollY = absoluteScroll
        )
    }
}

@Composable
fun rememberProfileScrollState(): ProfileScrollState {
    return remember {
        ProfileScrollState()
    }
}*/

/*

@Stable
class ProfileScrollState {

    var tabOffsetPx by mutableFloatStateOf(0f)
        private set

    private var tabHeightPx by mutableFloatStateOf(0f)

    fun setTabHeight(px: Float) {
        tabHeightPx = px
    }

    fun onScrollDelta(delta: Float) {
        tabOffsetPx =
            (tabOffsetPx + delta)
                .coerceIn(-tabHeightPx, 0f)
    }
}

@Composable
fun rememberProfileScrollState(): ProfileScrollState {
    return remember { ProfileScrollState() }
}

@Composable
fun rememberTabNestedScrollConnection(
    state: ProfileScrollState
): NestedScrollConnection {

    return remember {
        object : NestedScrollConnection {

            override fun onPreScroll(
                available: Offset,
                source: NestedScrollSource
            ): Offset {

                state.onScrollDelta(available.y)
                return Offset.Zero
            }
        }
    }
}


*/




// ─────────────────────────────────────────────────────────────────────────────
// Tu perfil de usuario — sustituye este composable por el real
// ─────────────────────────────────────────────────────────────────────────────



/**
 * Combina dos [TopAppBarScrollBehavior] en un único [NestedScrollConnection]
 * que los encadena en orden:
 *
 *  1. [exitBehavior]  → exitUntilCollapsed  (header de perfil, altura libre)
 *  2. [enterBehavior] → enterAlways         (tab row, quick-return)
 *
 * El scroll disponible se reparte en cascada:
 *   - El primer behavior consume lo que necesita.
 *   - El sobrante se pasa al segundo.
 *
 * Esto replica exactamente la lógica interna de Material3 cuando
 * encadenas behaviors en un Scaffold.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Stable
class CombinedScrollConnection(
    private val exitBehavior: TopAppBarScrollBehavior,
    private val enterBehavior: TopAppBarScrollBehavior,
) : NestedScrollConnection {

    override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
        val consumed1 = exitBehavior.nestedScrollConnection.onPreScroll(available, source)
        val consumed2 = enterBehavior.nestedScrollConnection.onPreScroll(available - consumed1, source)
        if (available.y != 0f) {
            Log.d("TRACK_SCROLL", "onPreScroll: avail=${available.y}, exitCons=${consumed1.y}, enterCons=${consumed2.y}")
        }
        return consumed1 + consumed2
    }

    override fun onPostScroll(
        consumed: Offset,
        available: Offset,
        source: NestedScrollSource,
    ): Offset {
        val extra1 = exitBehavior.nestedScrollConnection.onPostScroll(consumed, available, source)
        val extra2 = enterBehavior.nestedScrollConnection.onPostScroll(
            consumed + extra1,
            available - extra1,
            source,
        )
        if (available.y != 0f) {
            Log.d("TRACK_SCROLL", "onPostScroll: avail=${available.y}, exitExtra=${extra1.y}, enterExtra=${extra2.y}")
        }
        return extra1 + extra2
    }

    override suspend fun onPreFling(available: androidx.compose.ui.unit.Velocity): androidx.compose.ui.unit.Velocity {
        val v1 = exitBehavior.nestedScrollConnection.onPreFling(available)
        val v2 = enterBehavior.nestedScrollConnection.onPreFling(available - v1)
        return v1 + v2
    }

    override suspend fun onPostFling(
        consumed: androidx.compose.ui.unit.Velocity,
        available: androidx.compose.ui.unit.Velocity,
    ): androidx.compose.ui.unit.Velocity {
        val v1 = exitBehavior.nestedScrollConnection.onPostFling(consumed, available)
        val v2 = enterBehavior.nestedScrollConnection.onPostFling(consumed + v1, available - v1)
        return v1 + v2
    }
}