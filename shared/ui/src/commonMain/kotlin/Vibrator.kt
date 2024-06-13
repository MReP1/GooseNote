import androidx.compose.runtime.Composable
import org.koin.compose.koinInject
import org.koin.core.module.Module
import org.koin.dsl.module

sealed class Vibration {

    data class OneShot(val millis: Long, val amplitude: Int) : Vibration()

    data object ClickShot : Vibration()

}

expect class Vibrator {

    fun vibrate(effect: Vibration)

}

val vibratorModule = module { vibrator() }

expect fun Module.vibrator()

@Composable
fun rememberVibrator(): Vibrator = koinInject()