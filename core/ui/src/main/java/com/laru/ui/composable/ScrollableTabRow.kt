package com.laru.ui.composable

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabPosition
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import com.laru.data.model.ChatCategory
import com.laru.ui.model.Paddings
import com.laru.ui.model.Sizes
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue


// NOTE: check transition with more than 4 tabItems
@Composable
fun ScrollableTabRow(
    tabItems: List<ChatCategory>,
    pagerState: PagerState,
    modifier: Modifier = Modifier,
) {
    Column {
        val animationScope = rememberCoroutineScope()

        ScrollableTabRow(
            selectedTabIndex = pagerState.currentPage,
            modifier = modifier,
            edgePadding = Paddings.default,
            divider = { },
            indicator =
            { tabPositions ->
                Box(
                    Modifier
                        .tabIndicatorValuesComputation(tabPositions, pagerState)
                        .height(3.dp)
                        .background(
                            color = MaterialTheme.colorScheme.secondary,
                            shape = RoundedCornerShape(topStart = 3.dp, topEnd = 3.dp)
                        )
                )
            }
        ) {
            tabItems.forEachIndexed { index, item ->
                Tab(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(Sizes.tabHeightDefault),
                    selected = index == pagerState.currentPage,
                    onClick = { animationScope.launch {
                        pagerState.animateScrollToPage(page = index, animationSpec = tween())
                    } },
                    text = { Text(text = item.name) },
                    selectedContentColor = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 1f - pagerState.currentPageOffsetFraction.absoluteValue),
                    unselectedContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                )
            }
        }

        HorizontalDivider(Modifier.fillMaxWidth(), color = MaterialTheme.colorScheme.secondary)
    }
}

private fun Modifier.tabIndicatorValuesComputation(
    tabPositions: List<TabPosition>,
    pagerState: PagerState,
): Modifier = composed {
    val targetValues = getIndicatorTargetValues(tabPositions, pagerState)

    val indicatorWidth by animateDpAsState(
        targetValue = targetValues.first,
        animationSpec = tween(durationMillis = 250, easing = LinearEasing),
        label = "indicatorWidthDpAnimation"
    )
    val indicatorOffset by animateDpAsState(
        targetValue = targetValues.second,
        animationSpec = tween(durationMillis = 250, easing = LinearEasing),
        label = "indicatorOffsetDpAnimation"
    )

    wrapContentSize(align = Alignment.BottomStart)
        .offset { IntOffset(indicatorOffset.roundToPx(), 0) }
        .width(indicatorWidth)
}

@Composable
private fun getIndicatorTargetValues(
    tabPositions: List<TabPosition>,
    pagerState: PagerState,
): Pair<Dp, Dp> {
    val isDragging = pagerState.interactionSource.collectIsDraggedAsState()
    val currentTab = tabPositions[pagerState.currentPage]
    val currentTabOffset = currentTab.left + (currentTab.width - currentTab.contentWidth - 8.dp) / 2

    if (!isDragging.value)
        return Pair(currentTab.contentWidth + 8.dp, currentTabOffset)

    val settledPage = pagerState.settledPage
    val settledTab = tabPositions[settledPage]

    val currentPageOffset = pagerState.offsetForPage()
    val nextTab = tabPositions.getOrNull(settledPage + if (currentPageOffset > 0) 1 else -1)

    return nextTab?.let {
        Pair(
            lerp(
                settledTab.contentWidth + 8.dp,
                nextTab.contentWidth + 8.dp,
                currentPageOffset.absoluteValue
            ),
            lerp(
                settledTab.left + (settledTab.width - settledTab.contentWidth - 8.dp) / 2,
                nextTab.left + (nextTab.width - nextTab.contentWidth - 8.dp) / 2,
                currentPageOffset.absoluteValue
            )
        )
    } ?: Pair(
        currentTab.contentWidth + 8.dp,
        currentTab.left + (currentTab.width - currentTab.contentWidth - 8.dp) / 2
    )
}

private fun PagerState.offsetForPage(): Float =
    (currentPage - settledPage + currentPageOffsetFraction).coerceIn(-1f, 1f)
