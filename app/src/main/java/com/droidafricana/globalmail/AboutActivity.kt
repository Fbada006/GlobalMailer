package com.droidafricana.globalmail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.droidafricana.globalmail.utils.CustomTabsUtils
import kotlinx.android.synthetic.main.activity_about.*
import kotlinx.android.synthetic.main.third_party_licenses_layout.*

private const val NEWS_API_URL = "https://newsapi.org/"
private const val PRIVACY_POLICY_URL = "https://global-mail.flycricket.io/privacy.html"
private const val RETROFIT_2_URL = "https://square.github.io/retrofit/"
private const val OKHTTTP3_URL = "https://square.github.io/okhttp/"
private const val PICASSO_URL = "https://square.github.io/picasso/"
private const val FIREBASE_URL = "https://firebase.google.com/terms/"
private const val CHROME_CUSTOM_TABS_URL = "https://developer.chrome.com/multidevice/android/customtabs"
private const val ANDROIDX_URL = "https://developer.android.com/jetpack/androidx"
private const val LOTTIE_URL = "https://github.com/airbnb/lottie-android"
private const val LIKE_BUTTON_URL = "https://github.com/jd-alexander/LikeButton"
private const val CHRIS_GANNON_URL = "https://lottiefiles.com/chrisgannon"
private const val ROGER_TAN_URL = "https://lottiefiles.com/leminhtanvus"
private const val Hoài_Lê_URL = "https://lottiefiles.com/koycatdang"
private const val HENRIQUE_ROSSATTO_URL = "https://lottiefiles.com/henriqrossatto"
private const val FREEPIK_URL = "https://www.freepik.com/"
private const val ROUND_ICONS_URL = "https://www.flaticon.com/authors/roundicons"
private const val PIXEL_BUDDHA_URL = "https://www.flaticon.com/authors/pixel-buddha"
private const val PONG_URL = "https://www.flaticon.com/authors/mynamepong"
private const val ICON_POND_URL = "https://www.flaticon.com/authors/popcorns-arts"
private const val EUCALYP_URL = "https://www.flaticon.com/authors/eucalyp"

class AboutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        setSupportActionBar(toolbar_about)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //TODO: Find a better way to do this
        tv_powered_by.setOnClickListener {
            CustomTabsUtils.launchCustomTabs(this, NEWS_API_URL)
        }
        tv_privacy_policy.setOnClickListener {
            CustomTabsUtils.launchCustomTabs(this, PRIVACY_POLICY_URL)
        }
        tv_retrofit_2.setOnClickListener {
            CustomTabsUtils.launchCustomTabs(this, RETROFIT_2_URL)
        }
        tv_okhttp3.setOnClickListener {
            CustomTabsUtils.launchCustomTabs(this, OKHTTTP3_URL)
        }
        tv_picasso.setOnClickListener {
            CustomTabsUtils.launchCustomTabs(this, PICASSO_URL)
        }
        tv_firebase.setOnClickListener {
            CustomTabsUtils.launchCustomTabs(this, FIREBASE_URL)
        }
        tv_custom_tabs.setOnClickListener {
            CustomTabsUtils.launchCustomTabs(this, CHROME_CUSTOM_TABS_URL)
        }
        tv_android_x.setOnClickListener {
            CustomTabsUtils.launchCustomTabs(this, ANDROIDX_URL)
        }
        tv_lottie.setOnClickListener {
            CustomTabsUtils.launchCustomTabs(this, LOTTIE_URL)
        }
        tv_like_button.setOnClickListener {
            CustomTabsUtils.launchCustomTabs(this, LIKE_BUTTON_URL)
        }
        tv_chris_gannon.setOnClickListener {
            CustomTabsUtils.launchCustomTabs(this, CHRIS_GANNON_URL)
        }
        tv_rogger_tan.setOnClickListener {
            CustomTabsUtils.launchCustomTabs(this, ROGER_TAN_URL)
        }
        tv_Hoài_Lê.setOnClickListener {
            CustomTabsUtils.launchCustomTabs(this, Hoài_Lê_URL)
        }
        tv_henrique_rossatto.setOnClickListener {
            CustomTabsUtils.launchCustomTabs(this, HENRIQUE_ROSSATTO_URL)
        }
        tv_freepik.setOnClickListener {
            CustomTabsUtils.launchCustomTabs(this, FREEPIK_URL)
        }
        tv_round_icons.setOnClickListener {
            CustomTabsUtils.launchCustomTabs(this, ROUND_ICONS_URL)
        }
        tv_pixel_buddha.setOnClickListener {
            CustomTabsUtils.launchCustomTabs(this, PIXEL_BUDDHA_URL)
        }
        tv_pong.setOnClickListener {
            CustomTabsUtils.launchCustomTabs(this, PONG_URL)
        }
        tv_icon_pond.setOnClickListener {
            CustomTabsUtils.launchCustomTabs(this, ICON_POND_URL)
        }
        tv_icon_eucalyp.setOnClickListener {
            CustomTabsUtils.launchCustomTabs(this, EUCALYP_URL)
        }
    }
}
