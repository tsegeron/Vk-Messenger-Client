package com.laru.auth.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.laru.app.ui.theme.VKMessengerTheme
import com.laru.auth.R
import com.laru.common.preview.ThemePreviews
import com.laru.ui.model.Paddings
import com.laru.ui.model.Sizes
import com.laru.ui.model.Spacers


@Composable
fun AuthScreen(
//    viewModel: AuthViewModel = hiltViewModel()
) {
//    val uiState by viewModel.authUiState.collectAsState()
    val focusManager = LocalFocusManager.current
    var textFieldValue: TextFieldValue by remember {
        mutableStateOf(TextFieldValue(""))
    }
    
    Scaffold(
//        snackbarHost = // for errors?
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    focusManager.clearFocus()
                })
            },
        floatingActionButton = {
//            if (uiState !is AuthUiState.Start) {
                IconButton(
                    onClick = { /*TODO*/ },
                    modifier = Modifier.size(52.dp),
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.6f),
                        contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.ArrowBack,
                        contentDescription = stringResource(R.string.back)
                    )
                }
//            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = Paddings.small)
                .padding(bottom = Paddings.xxLarge)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            LottieAnimation(
                Modifier
                    .height(300.dp)
                    .padding(bottom = Paddings.large))

            Text(
                text = stringResource(R.string.sign_in_title), // uiState
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(Spacers.small))
            Text(
                text = stringResource(R.string.sign_in_subtitle), // uiState
                style = MaterialTheme.typography.bodyLarge,
            )
            Spacer(modifier = Modifier.height(Spacers.large))

            OutlinedTextField(
                value = textFieldValue,
                onValueChange = { textFieldValue = it },
                shape = MaterialTheme.shapes.large,
                label = { Text(text = "Phone or Email") },  // uiState
                trailingIcon = textFieldValue.text.ifNotEmptyOrNull {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = stringResource(com.laru.ui.R.string.clear),
                        modifier = Modifier
                            .size(Sizes.icon)
                            .clickable {
                                textFieldValue = TextFieldValue()
                            }
                    )
                },
                singleLine = true,
                modifier = Modifier
                    .padding(horizontal = Paddings.large)
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(Spacers.large))

            FilledTonalButton(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .width(160.dp)
                    .height(Sizes.buttonHeightMedium),
                colors = ButtonDefaults.filledTonalButtonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                shape = MaterialTheme.shapes.extraLarge,
                content = { Text(text = "Sign in", style = MaterialTheme.typography.bodyLarge) }
            )
        }
    }
}

// limit iterations and replay on click??
@Composable
fun LottieAnimation(modifier: Modifier = Modifier) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.lottie_login))
    var isPlaying by remember { mutableStateOf(true) }
    val progress by animateLottieCompositionAsState(
        composition = composition,
        isPlaying = isPlaying,
        iterations = LottieConstants.IterateForever,
        speed = 0.8f
//        iterations = 2,
//        cancellationBehavior = LottieCancellationBehavior.OnIterationFinish
    )

    LottieAnimation(
        composition = composition,
        progress = { progress },
        modifier = modifier
    )
}


fun String.ifNotEmptyOrNull(content: @Composable () -> Unit): @Composable (() -> Unit)? =
    if (isEmpty()) null else content

@ThemePreviews
@Composable
fun AuthScreenPreview(modifier: Modifier = Modifier) {
    VKMessengerTheme {
        AuthScreen()
    }
}
