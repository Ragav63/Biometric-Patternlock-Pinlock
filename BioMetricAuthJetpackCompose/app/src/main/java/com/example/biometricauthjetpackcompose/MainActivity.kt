package com.example.biometricauthjetpackcompose

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import com.andrognito.patternlockview.PatternLockView
import com.andrognito.patternlockview.listener.PatternLockViewListener

class MainActivity : AppCompatActivity() {

    private val promptManager by lazy {
        BiometricPromptManager(this)
    }

    private lateinit var navController: NavController // Declare NavController here

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val pinStorage = PinStorageManager(this)



        setContent {
            val navController = rememberNavController()

            NavHost(
                navController = navController,
                startDestination = Routes.BIOMETRIC
            ) {
                composable(Routes.BIOMETRIC) {
                    BiometricScreen(promptManager, pinStorage, navController)
                }
                composable(Routes.SET_PIN) {
                    SetPinScreen(pinStorage, navController) {
                        navController.navigate(Routes.HOME) {
                            popUpTo(Routes.BIOMETRIC) { inclusive = true }
                        }
                    }
                }
                composable(Routes.SET_PATTERN) {
                    SetPatternScreen(pinStorage, navController) {
                        navController.navigate(Routes.HOME) {
                            popUpTo(Routes.BIOMETRIC) { inclusive = true }
                        }
                    }
                }
                composable(Routes.ENTER_PIN) {
                    PinEntryScreen(pinStorage, navController) {
                        navController.navigate(Routes.HOME) {
                            popUpTo(Routes.BIOMETRIC) { inclusive = true }
                        }
                    }
                }

                composable(Routes.ENTER_PATTERN) {
                    EnterPatternScreen(pinStorage, navController) {
                        navController.navigate(Routes.HOME) {
                            popUpTo(Routes.BIOMETRIC) { inclusive = true }
                        }
                    }
                }
                composable(Routes.HOME) {
                    HomeScreen(pinStorage, navController)
                }
            }
        }
    }

}

@Composable
fun DefaultBackHandler(navController: NavController) {
    BackHandler {
        navController.navigateUp()
    }
}


@Composable
fun BiometricScreen(
    promptManager: BiometricPromptManager,
    pinStorage: PinStorageManager,
    navController: NavController
) {

    DefaultBackHandler(navController)


    val biometricResult = promptManager.promptResults.collectAsState(initial = null).value
    var showBiometric by remember { mutableStateOf(false) }

    LaunchedEffect(biometricResult) {
        if (showBiometric) {
            when (biometricResult) {
                is BiometricPromptManager.BiometricResult.AuthenticationSuccess -> {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.BIOMETRIC) { inclusive = true }
                    }
                }
                is BiometricPromptManager.BiometricResult.AuthenticationNotSet -> {
                    navController.navigate(Routes.SET_PIN)
                }
                else -> {}
            }
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Button(onClick = {
                showBiometric = true
                promptManager.showBiometricPrompt("Biometric Login", "Use your fingerprint or device lock")
            }) {
                Text("Authenticate with Biometrics")
            }

            val context = LocalContext.current

            Button(onClick = {
                if (pinStorage.getPin().isNullOrEmpty()) {
                    navController.navigate(Routes.SET_PIN)
                } else {
                    navController.navigate(Routes.ENTER_PIN)
                }
            }) {
                Text("Use PIN Instead")
            }

            Button(onClick = {
                if (pinStorage.getPattern().isNullOrEmpty()) {
                    navController.navigate(Routes.SET_PATTERN)
                } else {
                    navController.navigate(Routes.ENTER_PATTERN)
                }
            }) {
                Text("Use Pattern Instead")
            }

            biometricResult?.let {
                Text(text = when (it) {
                    is BiometricPromptManager.BiometricResult.AuthenticationError -> it.error
                    BiometricPromptManager.BiometricResult.AuthenticationFailed -> "Authentication failed"
                    BiometricPromptManager.BiometricResult.AuthenticationNotSet -> "No biometric setup"
                    BiometricPromptManager.BiometricResult.FeatureUnavailable -> "Feature unavailable"
                    BiometricPromptManager.BiometricResult.HardwareUnavailable -> "Hardware unavailable"
                    BiometricPromptManager.BiometricResult.AuthenticationSuccess -> "Success"
                })
            }
        }
    }
}




@Composable
fun PinEntryScreen(
    pinStorage: PinStorageManager,
    navController: NavController,
    onSuccess: () -> Unit
) {

    DefaultBackHandler(navController)

    val pinState = remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            OutlinedTextField(
                value = pinState.value,
                onValueChange = { pinState.value = it },
                label = { Text("Enter PIN") }
            )
            Button(onClick = {
                if (pinStorage.getPin() == pinState.value) {
                    onSuccess()
                } else {
                    error = "Incorrect PIN"
                }
            }) {
                Text("Verify PIN")
            }
            if (error.isNotEmpty()) {
                Text(text = error, color = Color.Red)
            }
        }
    }
}



@Composable
fun SetPinScreen(
    pinStorage: PinStorageManager,
    navController: NavHostController,
    onPinSet: () -> Unit
) {

    DefaultBackHandler(navController)


    val pinState = remember { mutableStateOf("") }
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            OutlinedTextField(
                value = pinState.value,
                onValueChange = { pinState.value = it },
                label = { Text("Set PIN") }
            )
            Button(onClick = {
                pinStorage.savePin(pinState.value)
                onPinSet()
            }) {
                Text("Save PIN")
            }
        }
    }
}

@Composable
fun SetPatternScreen(
    pinStorage: PinStorageManager,
    navController: NavHostController,
    onPatternSet: () -> Unit
) {

    DefaultBackHandler(navController)

    var statusMessage by remember { mutableStateOf("") }
    var statusColor by remember { mutableStateOf(Color.Unspecified) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Set a Pattern", style = MaterialTheme.typography.headlineMedium)

        AndroidView(
            modifier = Modifier.size(300.dp),
            factory = { context ->
                PatternLockView(context).apply {
                    setDotCount(3)
                    setDotNormalSize(20)
                    setDotSelectedSize(30)
                    setPathWidth(6)
                    setAspectRatioEnabled(true)
                    setAspectRatio(PatternLockView.AspectRatio.ASPECT_RATIO_HEIGHT_BIAS)
                    setViewMode(PatternLockView.PatternViewMode.CORRECT)
                    setDotAnimationDuration(150)
                    setPathEndAnimationDuration(100)

                    // ✅ Updated to avoid ContextCompat from android.support.v4
                    setNormalStateColor(Color.Black.toArgb())
                    setCorrectStateColor(Color.Green.toArgb())
                    setWrongStateColor(Color.Red.toArgb())

                    addPatternLockListener(object : PatternLockViewListener {
                        override fun onStarted() {
                            statusMessage = ""
                            statusColor = Color.Unspecified
                        }

                        override fun onProgress(progressPattern: List<PatternLockView.Dot>) {}

                        override fun onComplete(pattern: List<PatternLockView.Dot>) {
                            val patternIds = pattern.map { it.id }
                            Log.d("Pattern", "Pattern entered: $patternIds")

                            if (patternIds.size < 4) {
                                statusMessage = "Pattern too short. Please connect at least 4 dots."
                                statusColor = Color.Red
                                setViewMode(PatternLockView.PatternViewMode.WRONG)
                            } else {
                                pinStorage.savePattern(patternIds)
                                statusMessage = "Pattern saved successfully!"
                                statusColor = Color.Green
                                setViewMode(PatternLockView.PatternViewMode.CORRECT)
                                onPatternSet()
                            }
                        }

                        override fun onCleared() {}
                    })
                }

            }
        )

        if (statusMessage.isNotEmpty()) {
            Text(
                text = statusMessage,
                color = statusColor,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}

@Composable
fun EnterPatternScreen(
    pinStorage: PinStorageManager,
    navController: NavHostController,
    onSuccess: () -> Unit
) {

    DefaultBackHandler(navController)


    var patternState by remember { mutableStateOf<List<PatternLockView.Dot>>(emptyList()) }
    var error by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Enter Pattern", style = MaterialTheme.typography.headlineMedium)

        AndroidView(
            modifier = Modifier.size(300.dp),
            factory = { context ->
                PatternLockView(context).apply {
                    setDotCount(3)
                    setDotNormalSize(20)
                    setDotSelectedSize(30)
                    setPathWidth(6)
                    setAspectRatioEnabled(true)
                    setAspectRatio(PatternLockView.AspectRatio.ASPECT_RATIO_HEIGHT_BIAS)
                    setViewMode(PatternLockView.PatternViewMode.CORRECT)
                    setDotAnimationDuration(150)
                    setPathEndAnimationDuration(100)

                    // ✅ Updated to avoid ContextCompat from android.support.v4
                    setNormalStateColor(Color.Black.toArgb())
                    setCorrectStateColor(Color.Green.toArgb())
                    setWrongStateColor(Color.Red.toArgb())

                    addPatternLockListener(object : PatternLockViewListener {
                        override fun onStarted() {
                            error = ""
                        }

                        override fun onProgress(progressPattern: List<PatternLockView.Dot>) {
                            // You can use this to update the UI with the progress
                        }

                        override fun onComplete(pattern: List<PatternLockView.Dot>) {
                            val patternIds = pattern.map { it.id }
                            if (patternIds == pinStorage.getPattern()) {
                                onSuccess()
                            } else {
                                error = "Incorrect pattern"
                                setViewMode(PatternLockView.PatternViewMode.WRONG)
                            }
                        }

                        override fun onCleared() {}
                    })
                }
            }
        )

        if (error.isNotEmpty()) {
            Text(
                text = error,
                color = Color.Red,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}


@Composable
fun HomeScreen(pinStorage: PinStorageManager, navController: NavController) {

    DefaultBackHandler(navController)

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Welcome to Home Screen 🎉", style = MaterialTheme.typography.headlineMedium)

            Button(
                onClick = {
                    pinStorage.clearPin()
                    navController.navigate(Routes.SET_PIN) {
                        popUpTo(Routes.HOME) { inclusive = true }
                    }
                },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text("Reset PIN")
            }

            Button(
                onClick = {
                    pinStorage.clearPattern()
                    navController.navigate(Routes.SET_PATTERN) {
                        popUpTo(Routes.HOME) { inclusive = true }
                    }
                },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text("Reset Pattern")
            }
        }
    }
}


