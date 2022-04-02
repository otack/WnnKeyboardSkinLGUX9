package `in`.slit.wnnext.skin.lgux9

import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.Spanned
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_sub.*

class SubActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sub)

        checkHideLauncherIcon.setOnCheckedChangeListener { _, checked ->
            val component = ComponentName(this, MainActivity::class.java)
            val state = if (checked) {
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED
            } else {
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED
            }
            packageManager.setComponentEnabledSetting(component, state, PackageManager.DONT_KILL_APP)
        }
        textPrivacy.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.privacy_url)))
            startActivity(intent)
        }
        val text = getString(R.string.tips_message)
        val spannable = SpannableStringBuilder(text).apply {
            val size = (16 * resources.displayMetrics.scaledDensity).toInt()
            val icon = getDrawable(R.drawable.menu_hide_btn)!!.apply {
                setBounds(0, 0, size, size)
            }
            val span = CustomImageSpan(icon)
            val start = text.indexOf("$")
            val end = start + 1
            setSpan(span, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        textTipsMessage.text = spannable
    }
}