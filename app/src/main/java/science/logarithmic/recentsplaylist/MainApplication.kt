package science.logarithmic.recentsplaylist

import android.app.Application
import io.multimoon.colorful.Defaults
import io.multimoon.colorful.ThemeColor
import io.multimoon.colorful.initColorful

class MainApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        val defaults: Defaults = Defaults(
                primaryColor = ThemeColor.AMBER,
                accentColor = ThemeColor.AMBER,
                useDarkTheme = true,
                translucent = false)
        initColorful(this, defaults)
    }
}