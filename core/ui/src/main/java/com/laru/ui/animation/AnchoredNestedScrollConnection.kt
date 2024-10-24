package com.laru.ui.animation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.unit.Velocity
import com.laru.ui.model.AnchorState


@OptIn(ExperimentalFoundationApi::class)
class AnchoredPagerNestedScrollConnection (
    private val anchoredDraggableState: AnchoredDraggableState<AnchorState>,
    private val isAtTop: List<State<Boolean>>,
    private val pagerState: PagerState,
): NestedScrollConnection {
    private fun Float.toYOffset() = Offset(0f, this)

    override fun onPreScroll(
        available: Offset,
        source: NestedScrollSource,
    ): Offset {
        val delta = available.y
        if (delta > 0 && isAtTop[pagerState.currentPage].value) {
            anchoredDraggableState.dispatchRawDelta(delta)
            return available
        }
        if (delta < 0 && (isAtTop[pagerState.currentPage].value || anchoredDraggableState.currentValue == AnchorState.End)) {
            val consumed = anchoredDraggableState.dispatchRawDelta(delta)
            return consumed.toYOffset()
        }

        return super.onPreScroll(available, source)
    }

    override fun onPostScroll(
        consumed: Offset,
        available: Offset,
        source: NestedScrollSource,
    ): Offset {
        val delta = available.y

        if (delta > 0 && isAtTop[pagerState.currentPage].value) {
            return anchoredDraggableState.dispatchRawDelta(delta).toYOffset()
        }
        return super.onPostScroll(consumed, available, source)
    }

    override suspend fun onPostFling(
        consumed: Velocity,
        available: Velocity,
    ): Velocity {
        anchoredDraggableState.settle(available.y)
        return super.onPostFling(consumed, available)
    }
}


@OptIn(ExperimentalFoundationApi::class)
class AnchoredLazyColumnNestedScrollConnection (
    private val anchoredDraggableState: AnchoredDraggableState<AnchorState>,
    private val isAtTop: State<Boolean>,
): NestedScrollConnection {
    private fun Float.toYOffset() = Offset(0f, this)

    override fun onPreScroll(
        available: Offset,
        source: NestedScrollSource,
    ): Offset {
        val delta = available.y
        if (delta > 0 && isAtTop.value) {
            anchoredDraggableState.dispatchRawDelta(delta)
            return available
        }
        if (delta < 0 && (isAtTop.value || anchoredDraggableState.currentValue == AnchorState.End)) {
            val consumed = anchoredDraggableState.dispatchRawDelta(delta)
            return consumed.toYOffset()
        }

        return super.onPreScroll(available, source)
    }

    override fun onPostScroll(
        consumed: Offset,
        available: Offset,
        source: NestedScrollSource,
    ): Offset {
        val delta = available.y

        if (delta > 0 && isAtTop.value) {
            return anchoredDraggableState.dispatchRawDelta(delta).toYOffset()
        }
        return super.onPostScroll(consumed, available, source)
    }

    override suspend fun onPostFling(
        consumed: Velocity,
        available: Velocity,
    ): Velocity {
        anchoredDraggableState.settle(available.y)
        return super.onPostFling(consumed, available)
    }
}

