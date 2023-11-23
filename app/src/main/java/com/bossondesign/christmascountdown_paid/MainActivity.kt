package com.bossondesign.christmascountdown_paid

import android.graphics.Color
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.View
import android.view.ViewTreeObserver
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import com.google.android.play.core.review.ReviewManagerFactory

class MainActivity : AppCompatActivity(), ChristmasCountdownManager.CountdownListener {

    private var countdownTextView: TextView? = null
    private var daystillTextView: TextView? = null
    private var textView: TextView? = null

    private lateinit var themeMusicManager: ThemeMusicManager
    private lateinit var wallpaperManager: WallpaperManager
    private lateinit var decorationManager: DecorationManager

    private lateinit var countdownManager: ChristmasCountdownManager
    private lateinit var decorationImageView: ImageView
    private lateinit var mainLayout: RelativeLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        decorationImageView = findViewById(R.id.decorationImageView)
        mainLayout = findViewById(R.id.main_layout)

        val vto = mainLayout.viewTreeObserver
        vto.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                mainLayout.viewTreeObserver.removeOnGlobalLayoutListener(this)

                // Initialize DecorationManager with the layout's dimensions
                decorationManager = DecorationManager(
                    this@MainActivity,
                    decorationImageView,
                    mainLayout.width,
                    mainLayout.height
                )

                decorationManager.setDefaultDecoration() // Set default decoration

                decorationImageView.setOnClickListener {
                    decorationManager.playCurrentSound()
                    decorationManager.playCurrentAnimation() // Trigger the correct animation
                }
            }
        })

        decorationImageView.setOnClickListener {
            decorationManager.playCurrentSound()
            decorationManager.playCurrentAnimation() // Trigger the correct animation
        }

        countdownTextView = findViewById(R.id.txtTimerDay)
        daystillTextView = findViewById(R.id.dayTillText)
        textView = findViewById(R.id.textPrivacy)
        //bellImage = findViewById(R.id.tree)

        showFeedbackDialog()

        countdownManager = ChristmasCountdownManager(this)
        countdownManager.startCountdown()

        textView?.movementMethod = LinkMovementMethod.getInstance()

        themeMusicManager = ThemeMusicManager(this)
        themeMusicManager.playMusic()

        // Initialize WallpaperManager with the layout ID
        wallpaperManager = WallpaperManager(this, R.id.main_layout)
        wallpaperManager.loadWallpaperPreference()

        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)

        // Make both the navigation bar and the status bar transparent
        window.statusBarColor = Color.parseColor("#66000000")
        window.navigationBarColor = Color.TRANSPARENT

        val settings = findViewById<ImageView>(R.id.settingsIcon)
        settings.setOnClickListener { view ->
            showMenu(view)
        }
    }

    private fun showMenu(view: View) {
        val popup = PopupMenu(this, view)
        val inflater = popup.menuInflater
        inflater.inflate(R.menu.settings_menu, popup.menu)
        popup.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.music_merry_christmas -> {
                    themeMusicManager.playMusic("merry_christmas")
                    themeMusicManager.saveMusicPreference("merry_christmas")
                    true
                }
                R.id.music_christmas_tree -> {
                    themeMusicManager.playMusic("christmas_tree")
                    themeMusicManager.saveMusicPreference("christmas_tree")
                    true
                }
                R.id.music_deck_halls -> {
                    themeMusicManager.playMusic("deck_halls")
                    themeMusicManager.saveMusicPreference("deck_halls")
                    true
                }
                R.id.music_jingle_bells -> {
                    themeMusicManager.playMusic("jingle_bells")
                    themeMusicManager.saveMusicPreference("jingle_bells")
                    true
                }
                R.id.music_silent_night -> {
                    themeMusicManager.playMusic("silent_night")
                    themeMusicManager.saveMusicPreference("silent_night")
                    true
                }
                R.id.music_holy_night -> {
                    themeMusicManager.playMusic("holy_night")
                    themeMusicManager.saveMusicPreference("holy_night")
                    true
                }
                R.id.music_holly_jolly  -> {
                    themeMusicManager.playMusic("holly_jolly")
                    themeMusicManager.saveMusicPreference("holly_jolly")
                    true
                }
                R.id.music_frosty_snowman -> {
                    themeMusicManager.playMusic("frosty_snowman")
                    themeMusicManager.saveMusicPreference("frosty_snowman")
                    true
                }
                R.id.music_joy_world -> {
                    themeMusicManager.playMusic("joy_world")
                    themeMusicManager.saveMusicPreference("joy_world")
                    true
                }
                R.id.music_none -> {
                    themeMusicManager.stopMusicAndSavePreference()
                    true
                }
                R.id.red_wall -> {
                    wallpaperManager.changeWallpaper("red_wall")
                    true
                }
                R.id.green_wall -> {
                    wallpaperManager.changeWallpaper("green_wall")
                    true
                }
                R.id.blue_wall -> {
                    wallpaperManager.changeWallpaper("blue_wall")
                    true
                }
                R.id.peach_wall -> {
                    wallpaperManager.changeWallpaper("peach_wall")
                    true
                }
                R.id.black_wall -> {
                    wallpaperManager.changeWallpaper("black_wall")
                    true
                }
                R.id.grey_wall -> {
                    wallpaperManager.changeWallpaper("grey_wall")
                    true
                }
                R.id.jingle_bells -> {
                    decorationManager.changeDecoration(Decoration.JINGLE_BELLS)
                    true
                }
                R.id.christmas_tree -> {
                    decorationManager.changeDecoration(Decoration.CHRISTMAS_TREE)
                    true
                }
                R.id.sleigh_bells -> {
                    decorationManager.changeDecoration(Decoration.SLEIGH_BELLS)
                    true
                }
                R.id.cocoa_mug -> {
                    decorationManager.changeDecoration(Decoration.COCOA_MUG)
                    true
                }
                R.id.snowman -> {
                    decorationManager.changeDecoration(Decoration.SNOWMAN)
                    true
                }
                R.id.north_star -> {
                    decorationManager.changeDecoration(Decoration.NORTH_STAR)
                    true
                }
                else -> false
            }
        }
        popup.show()
    }

    override fun onCountdownUpdate(remainingDays: Int) {
        daystillTextView?.text = getString(R.string.countdown)
        countdownTextView?.text = "$remainingDays"
        countdownTextView?.visibility = View.VISIBLE
    }

    override fun onChristmasEve() {
        daystillTextView?.text = getString(R.string.christmasEve)
        countdownTextView?.visibility = View.INVISIBLE
    }

    override fun onChristmasDay() {
        daystillTextView?.text = getString(R.string.christmas)
        countdownTextView?.visibility = View.INVISIBLE
    }

    private fun showFeedbackDialog() {
        val reviewManager = ReviewManagerFactory.create(applicationContext)
        reviewManager.requestReviewFlow().addOnCompleteListener {
            if (it.isSuccessful) {
                reviewManager.launchReviewFlow(this, it.result)
            }
        }
    }

    override fun onPause() {
        super.onPause()
        themeMusicManager.pauseMusic()
    }

    override fun onResume() {
        super.onResume()
        themeMusicManager.resumeMusic()
    }

    override fun onStop() {
        super.onStop()
        // Any onStop logic for themeMusicManager if needed
    }

    override fun onDestroy() {
        super.onDestroy()
        themeMusicManager.releaseMediaPlayer()
        decorationManager.release()
    }
}