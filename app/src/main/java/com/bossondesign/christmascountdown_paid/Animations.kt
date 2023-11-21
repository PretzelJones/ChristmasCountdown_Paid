import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator

object Animations {

    fun squashAnimation(view: View): AnimatorSet {
        val scaleX = ObjectAnimator.ofFloat(view, "scaleY", 1f, 1.2f, 1f)
        val scaleY = ObjectAnimator.ofFloat(view, "scaleX", 1f, 0.8f, 1f)

        scaleX.interpolator = AccelerateDecelerateInterpolator()
        scaleY.interpolator = AccelerateDecelerateInterpolator()

        scaleX.duration = 300
        scaleY.duration = 300

        val set = AnimatorSet()
        set.playTogether(scaleX, scaleY)
        return set
    }

    fun bellAnimation(view: View): ObjectAnimator {
        val rotate = ObjectAnimator.ofFloat(view, "rotation", 0f, 20f, 0f)
        rotate.duration = 300
        rotate.interpolator = AccelerateDecelerateInterpolator()
        return rotate
    }

    fun flyAwayAnimation(view: View, containerWidth: Int): AnimatorSet {
        val targetScale = 0.1f  // Target scale for the shrink animation

        // Calculate the end positions (top right corner of the container)
        val endX = containerWidth - view.width * targetScale
        val endY = 0f

        // Create a scale down animation
        val scaleX = ObjectAnimator.ofFloat(view, "scaleX", 1f, targetScale)
        val scaleY = ObjectAnimator.ofFloat(view, "scaleY", 1f, targetScale)

        // Create a move animation
        val moveX = ObjectAnimator.ofFloat(view, "translationX", 0f, endX - view.x)
        val moveY = ObjectAnimator.ofFloat(view, "translationY", 0f, endY - view.y)

        // Set interpolators and duration
        val interpolator = AccelerateDecelerateInterpolator()
        scaleX.interpolator = interpolator
        scaleY.interpolator = interpolator
        moveX.interpolator = interpolator
        moveY.interpolator = interpolator

        val duration = 1000L // duration in milliseconds
        scaleX.duration = duration
        scaleY.duration = duration
        moveX.duration = duration
        moveY.duration = duration

        // Combine animations
        val set = AnimatorSet()
        set.playTogether(scaleX, scaleY, moveX, moveY)

        // Add a listener to reset the view's properties after the animation ends
        set.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                view.scaleX = 1f
                view.scaleY = 1f
                view.translationX = 0f
                view.translationY = 0f
            }
        })

        return set
    }

    // You can add more animations here in the future
}
