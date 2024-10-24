package com.laru.ui.animation

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.DecayAnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.animation.splineBasedDecay
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import com.laru.ui.model.AnchorState
import com.laru.ui.model.Sizes


@OptIn(ExperimentalFoundationApi::class)
fun textFieldAnchoredDraggableState(
    density: Density,
    initialValue: AnchorState = AnchorState.Start,
    maxTextFieldHeight: Dp = Sizes.textFieldHeightWithPadding,
    anchors: DraggableAnchors<AnchorState> = DraggableAnchors {
        AnchorState.Start at 0f
        AnchorState.End at with(density) { maxTextFieldHeight.toPx() }
    },
    positionalThreshold: (Float) -> Float = { distance: Float -> distance * 0.4f },
    velocityThreshold: () -> Float = { Int.MAX_VALUE.toFloat() },
    snapAnimationSpec: AnimationSpec<Float> = tween(),
    decayAnimationSpec: DecayAnimationSpec<Float> = splineBasedDecay(density),
): AnchoredDraggableState<AnchorState> = AnchoredDraggableState(
    initialValue = initialValue,
    anchors = anchors,
    positionalThreshold = positionalThreshold,
    velocityThreshold = velocityThreshold,
    snapAnimationSpec = snapAnimationSpec,
    decayAnimationSpec = decayAnimationSpec,
)
