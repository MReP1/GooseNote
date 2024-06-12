import org.koin.core.module.Module
import platform.UIKit.UIImpactFeedbackGenerator
import platform.UIKit.UIImpactFeedbackStyle

actual class Vibrator {

    actual fun vibrate(effect: Vibration) {
        when (effect) {
            Vibration.ClickShot,
            is Vibration.OneShot -> {
                UIImpactFeedbackGenerator(UIImpactFeedbackStyle.UIImpactFeedbackStyleMedium)
            }
        }
    }

}

actual fun Module.vibrator() {
    single { Vibrator() }
}