package com.laru.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.laru.app.ui.theme.VKMessengerTheme
import com.laru.auth.composable.OneTapButton
import com.laru.common.preview.ThemePreviews
import com.laru.ui.model.Paddings
import com.laru.ui.model.Sizes
import com.laru.ui.model.Spacers
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable


@Composable
fun AuthScreen(
//    onAuthSuccess: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
//        snackbarHost = {
//            SnackbarHost(hostState = snackbarHostState)
//        }, // TODO: for errors?
        modifier = Modifier.fillMaxSize()
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
            LottieAnimation(Modifier.height(300.dp).padding(bottom = Paddings.large))

            Text(
                text = stringResource(R.string.sign_in_title), // uiState
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(horizontal = Paddings.large)
            )
            Spacer(modifier = Modifier.height(Spacers.small))
            Text(
                text = stringResource(R.string.sign_in_subtitle), // uiState
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = Paddings.large)
            )
            Spacer(modifier = Modifier.height(Spacers.extraLarge))

            OneTapButton(
                onClick = viewModel::proceedAuthorization,
//                {
//                    scope.launch { snackbarHostState.showSnackbar(message = "Snackbar") }
//                },
                modifier = Modifier
                    .padding(horizontal = Paddings.large)
                    .fillMaxWidth()
                    .height(Sizes.buttonHeightMedium)
            )
        }
    }
}

@Composable
private fun LottieAnimation(modifier: Modifier = Modifier) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.lottie_login))
    var isPlaying by remember { mutableStateOf(true) }
    val progress by animateLottieCompositionAsState(
        composition = composition,
        isPlaying = isPlaying,
        iterations = LottieConstants.IterateForever,
        speed = 0.8f
    )

    LottieAnimation(
        composition = composition,
        progress = { progress },
        modifier = modifier
    )
}

//@Composable
//private fun SignInForm(
//    textFieldValue: TextFieldValue,
//    onValueChange: (TextFieldValue) -> Unit,
//    onClear: () -> Unit,
//    modifier: Modifier = Modifier
//) {
//    OutlinedTextField(
//        value = textFieldValue,
//        onValueChange = onValueChange,
//        shape = MaterialTheme.shapes.large,
//        label = { Text(text = "Phone or Email") },  // uiState
//        trailingIcon = textFieldValue.text.ifNotEmptyOrNull {
//            Icon(
//                imageVector = Icons.Default.Clear,
//                contentDescription = stringResource(com.laru.ui.R.string.clear),
//                modifier = Modifier
//                    .size(Sizes.icon)
//                    .clickable(onClick = onClear)
//            )
//        },
//        singleLine = true,
//        modifier = modifier
//            .padding(horizontal = Paddings.large)
//            .fillMaxWidth()
//    )
//    Spacer(modifier = Modifier.height(Spacers.large))
//
//    FilledTonalButton(
//        onClick = { /*TODO*/ },
//        modifier = Modifier
//            .width(160.dp)
//            .height(Sizes.buttonHeightMedium),
//        colors = ButtonDefaults.filledTonalButtonColors(
//            containerColor = MaterialTheme.colorScheme.primaryContainer,
//            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
//        ),
//        shape = MaterialTheme.shapes.extraLarge,
//        content = { Text(text = "Sign in", style = MaterialTheme.typography.bodyLarge) }
//    )
//}


fun String.ifNotEmptyOrNull(content: @Composable () -> Unit): @Composable (() -> Unit)? =
    if (isEmpty()) null else content


@ThemePreviews
@Composable
private fun AuthScreenPreview() {
    VKMessengerTheme {
        AuthScreen()
    }
}
