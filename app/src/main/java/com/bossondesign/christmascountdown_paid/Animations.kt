import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator


object Animations {

    fun growAndSpinAnimation(view: View): AnimatorSet {
        val totalDuration = 1000L // Total duration of the animation sequence in milliseconds

        // Scale the star larger, making the animation twice as fast
        val scaleUpX = ObjectAnimator.ofFloat(view, "scaleX", 1f, 1.5f).setDuration(totalDuration / 4)
        val scaleUpY = ObjectAnimator.ofFloat(view, "scaleY", 1f, 1.5f).setDuration(totalDuration / 4)

        // Rotate the star quickly
        val rotate = ObjectAnimator.ofFloat(view, "rotation", 0f, 360f).setDuration(totalDuration / 2)

        // Scale the star back to its original size
        val scaleDownX = ObjectAnimator.ofFloat(view, "scaleX", 1.5f, 1f).setDuration(totalDuration / 4)
        val scaleDownY = ObjectAnimator.ofFloat(view, "scaleY", 1.5f, 1f).setDuration(totalDuration / 4)

        // AnimatorSet to manage the timing of each animation
        val animatorSet = AnimatorSet().apply {
            // Play scale-up animations
            play(scaleUpX).with(scaleUpY)
            // Start rotation halfway through the scale-up animations
            play(rotate).after(scaleUpX.duration / 2)
            // Start scale-down animations immediately after the rotation
            play(scaleDownX).with(scaleDownY).after(rotate)
        }

        return animatorSet
    }

    fun swayAnimation(view: View): AnimatorSet {
        // Define the rotation for the sway to the left
        val rotateToLeft = ObjectAnimator.ofFloat(view, "rotation", 0f, -10f)
        val resetRotation = ObjectAnimator.ofFloat(view, "rotation", -10f, 0f)

        // Set a shorter duration for a faster animation
        val duration = 250L // duration in milliseconds

        // Set interpolator for natural motion
        val interpolator = AccelerateDecelerateInterpolator()
        rotateToLeft.duration = duration
        resetRotation.duration = duration
        rotateToLeft.interpolator = interpolator
        resetRotation.interpolator = interpolator

        // Combine the rotations into an AnimatorSet for sequential play
        val animatorSet = AnimatorSet()
        animatorSet.playSequentially(rotateToLeft, resetRotation)

        return animatorSet
    }

fun sleighAnimation(view: View): AnimatorSet {
    // Define the vertical movement (slight lift and drop)
    val moveUp = ObjectAnimator.ofFloat(view, "translationY", 0f, -30f)
    val moveDown = ObjectAnimator.ofFloat(view, "translationY", -30f, 0f)

    // Set duration for the up and down movements
    moveUp.duration = 500 // 0.5 seconds to move up
    moveDown.duration = 100 // 0.5 seconds to move down

    // Set interpolators for natural motion
    val interpolator = AccelerateDecelerateInterpolator()
    moveUp.interpolator = interpolator
    moveDown.interpolator = interpolator

    // Combine the animations into an AnimatorSet for sequential play
    val animatorSet = AnimatorSet()
    animatorSet.playSequentially(moveUp, moveDown)

    return animatorSet
}

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


    fun mugAnimation(view: View): AnimatorSet {
        val scaleX = ObjectAnimator.ofFloat(view, "scaleX", 1f, 1.05f, 1f)
        val scaleY = ObjectAnimator.ofFloat(view, "scaleY", 1f, 1.05f, 1f)
        val rotate = ObjectAnimator.ofFloat(view, "rotation", 0f, 5f, -5f, 0f)

        scaleX.duration = 500
        scaleY.duration = 500
        rotate.duration = 500

        val animatorSet = AnimatorSet()
        animatorSet.playTogether(scaleX, scaleY, rotate)
        animatorSet.interpolator = AccelerateDecelerateInterpolator()
        return animatorSet
    }

    fun treeShakeAnimation2(view: View): ObjectAnimator {
        val animator = ObjectAnimator.ofFloat(view, "translationX", 0f, 25f, -25f, 25f, -25f, 15f, -15f, 6f, -6f, 0f)

        animator.duration = 1000
        return animator
    }

    fun treeShakeAnimation(view: View): AnimatorSet {
            val originalPosition = view.translationX

            // Create the movement animations
            val moveRight = ObjectAnimator.ofFloat(view, "translationX", originalPosition, originalPosition + 20f)
            val moveLeft = ObjectAnimator.ofFloat(view, "translationX", originalPosition + 20f, originalPosition - 20f)
            val moveBackRight = ObjectAnimator.ofFloat(view, "translationX", originalPosition - 20f, originalPosition + 20f)
            val resetPosition = ObjectAnimator.ofFloat(view, "translationX", originalPosition + 20f, originalPosition)

            // Set the duration for each part of the animation
            val duration = 100L
            moveRight.duration = duration
            moveLeft.duration = duration
            moveBackRight.duration = duration
            resetPosition.duration = duration

            // Create an AnimatorSet and sequence the animations
            val animatorSet = AnimatorSet()
            animatorSet.playSequentially(moveRight, moveLeft, moveBackRight, resetPosition)
            animatorSet.interpolator = AccelerateDecelerateInterpolator()

            // Add a listener to reset the view's position after the animation and cancel any ongoing animation
            animatorSet.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    view.translationX = originalPosition
                }

                override fun onAnimationCancel(animation: Animator) {
                    super.onAnimationCancel(animation)
                    view.translationX = originalPosition
                }
            })

            return animatorSet
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

}
