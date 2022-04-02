package `in`.slit.wnnext.skin.lgux9

import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        private const val WNN_PACKAGE_NAME = "jp.co.omronsoft.wnnlab"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()

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

        if (isWnnInstalled()) {
            layoutIntroduction.visibility = View.GONE
            layoutTips.visibility = View.VISIBLE

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

        } else {
            layoutIntroduction.visibility = View.VISIBLE
            layoutTips.visibility = View.GONE

            buttonOpenWnnStore.setOnClickListener {
                try {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$WNN_PACKAGE_NAME")))
                } catch (e: ActivityNotFoundException) {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$WNN_PACKAGE_NAME")))
                }
            }
        }
    }

    private fun isWnnInstalled(): Boolean {
        try {
            packageManager.getPackageInfo(WNN_PACKAGE_NAME, PackageManager.GET_ACTIVITIES)
            return true
        } catch (e: PackageManager.NameNotFoundException) {
            // do nothing.
        }
        return false
    }
}
