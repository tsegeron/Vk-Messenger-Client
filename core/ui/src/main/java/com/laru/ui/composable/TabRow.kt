package com.laru.ui.composable

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.laru.ui.model.Paddings
import com.laru.ui.model.Sizes
import kotlinx.coroutines.launch

enum class TabItem {
    All, Personal, Channels, Bots,
//    Something
}

//data class TabItem(
//    val title: String,
//)

@Composable
fun VKMTabRow(
    tabItems: List<TabItem>,
    pagerState: PagerState,
    modifier: Modifier = Modifier
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val animationScope = rememberCoroutineScope()

    LaunchedEffect(pagerState.currentPage) {
        selectedTabIndex = pagerState.currentPage
    }

    ScrollableTabRow(
        selectedTabIndex = selectedTabIndex,
        modifier = modifier,
        edgePadding = Paddings.default,
        divider = {  },
        indicator = { tabPositions ->
            Box(
                Modifier
                    .customTabIndicatorOffset(tabPositions[selectedTabIndex])
                    .fillMaxWidth()
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
                    .height(Sizes.tabHeight),
                selected = index == selectedTabIndex,
                onClick = {
                    selectedTabIndex = index
                    animationScope.launch { pagerState.animateScrollToPage(index) }
                },
                text = { Text(text = item.name) },
                selectedContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                unselectedContentColor = MaterialTheme.colorScheme.secondaryContainer
            )
        }
    }
    HorizontalDivider()
}

private fun Modifier.customTabIndicatorOffset(currentTabPosition: TabPosition): Modifier =
    composed(
        inspectorInfo =
        debugInspectorInfo {
            name = "tabIndicatorOffset"
            value = currentTabPosition
        }
    ) {
        val currentTabWidth by animateDpAsState(
            targetValue = currentTabPosition.contentWidth + 8.dp,
            animationSpec = tween(durationMillis = 250),
            label = "TabIndicatorAnimation"
        )
        val indicatorOffsetTargetValue = currentTabPosition.left + (currentTabPosition.width - currentTabWidth)/2
        val indicatorOffset by animateDpAsState(
            targetValue = indicatorOffsetTargetValue,
            animationSpec = tween(durationMillis = 250),
            label = "TabIndicatorAnimation"
        )
        fillMaxWidth()
            .wrapContentSize(Alignment.BottomStart)
            .offset { IntOffset(x = indicatorOffset.roundToPx(), y = 0) }
            .width(currentTabWidth)
    }

