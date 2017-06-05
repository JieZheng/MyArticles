package au.com.myarticles.news.common.utils

import android.content.res.Resources
import android.support.annotation.IntegerRes
import android.support.v4.view.ViewCompat
import android.support.v4.view.ViewPropertyAnimatorCompat
import android.support.v4.view.ViewPropertyAnimatorListener
import android.view.View
import android.view.animation.AnticipateOvershootInterpolator
import android.animation.ObjectAnimator
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Interpolator
import au.com.myarticles.news.R

fun ViewPropertyAnimatorCompat.setDurationRes(resources: Resources, @IntegerRes durationRes: Int): ViewPropertyAnimatorCompat {
  return setDuration(resources.getInteger(durationRes).toLong())
}

fun ObjectAnimator.setDurationRes(resources: Resources, @IntegerRes durationRes: Int): ObjectAnimator {
  return setDuration(resources.getInteger(durationRes).toLong())
}

/* ScaleInFadeIn */

fun View.clearScaleInFadeIn() {
  ViewCompat.setScaleX(this, 1f)
  ViewCompat.setScaleY(this, 1f)
  ViewCompat.setAlpha(this, 1f)
}

fun View.scaleInFadeInPrepare() {
  ViewCompat.setScaleX(this, 0.5f)
  ViewCompat.setScaleY(this, 0.5f)
  ViewCompat.setAlpha(this, 0.0f)
}

fun View.scaleInFadeIn(listener: ViewPropertyAnimatorListener?, startDelay: Long = 0) {
  ViewCompat
      .animate(this)
      .alpha(1f)
      .scaleX(1f)
      .scaleY(1f)
      .setDurationRes(resources, R.integer.card_appearance_animation_duration)
      .setStartDelay(startDelay)
      .setInterpolator(AnticipateOvershootInterpolator())
      .setListener(listener)
      .start()
}

fun View.scaleOutFadeOutPrepare() {
  ViewCompat.setScaleX(this, 1f)
  ViewCompat.setScaleY(this, 1f)
  ViewCompat.setAlpha(this, 1f)
}

fun View.scaleOutFadeOut(listener: ViewPropertyAnimatorListener?, startDelay: Long = 0) {
  ViewCompat
      .animate(this)
      .alpha(0f)
      .scaleX(0.5f)
      .scaleY(0.5f)
      .setDurationRes(resources, R.integer.card_disappearance_animation_duration)
      .setStartDelay(startDelay)
      .setInterpolator(AnticipateOvershootInterpolator())
      .setListener(listener)
      .start()
}

/* FadeIn */

fun View.clearFadeIn() {
  ViewCompat.setAlpha(this, 1f)
}

fun View.fadeInPrepare() {
  ViewCompat.setAlpha(this, 0.0f)
}

fun View.fadeIn(listener: ViewPropertyAnimatorListener? = null, startDelay: Long = 0) {
  ViewCompat
    .animate(this)
    .alpha(1f)
    .setDurationRes(resources, R.integer.card_appearance_animation_duration)
    .setStartDelay(startDelay)
    .setInterpolator(AccelerateDecelerateInterpolator())
    .setListener(listener)
    .start()
}

fun View.fadeOutPrepare() {
  ViewCompat.setAlpha(this, 1f)
}

fun View.fadeOut(listener: ViewPropertyAnimatorListener? = null, startDelay: Long = 0) {
  ViewCompat
    .animate(this)
    .alpha(0f)
    .setDurationRes(resources, R.integer.card_disappearance_animation_duration)
    .setStartDelay(startDelay)
    .setInterpolator(AccelerateDecelerateInterpolator())
    .setListener(listener)
    .start()
}

/* FadeInTranslateUp */

fun View.clearFadeInTranslateUp() {
  ViewCompat.setAlpha(this, 1f)
  ViewCompat.setTranslationY(this, 0f)
}

fun View.fadeInTranslateUpPrepare() {
  ViewCompat.setAlpha(this, 0.0f)
  ViewCompat.setTranslationY(this, this.translationY + context.resources.getInteger(R.integer.slide_up_translation).toFloat())
}

fun View.fadeInTranslateUp(listener: ViewPropertyAnimatorListener? = null, startDelay: Long = 0, interpolator: Interpolator? = null) {
  ViewCompat
      .animate(this)
      .alpha(1f)
      .translationY(0f)
      .setDurationRes(resources, R.integer.card_appearance_animation_duration)
      .setStartDelay(startDelay)
      .setInterpolator(interpolator?: AnticipateOvershootInterpolator())
      .setListener(listener)
      .start()
}

fun View.fadeOutTranslateDownPrepare() {
  ViewCompat.setAlpha(this, 1f)
  ViewCompat.setTranslationY(this, 0f)
}

fun View.fadeOutTranslateDownPrepare(listener: ViewPropertyAnimatorListener? = null, startDelay: Long = 0, interpolator: Interpolator? = null) {
  ViewCompat
    .animate(this)
    .alpha(0f)
    .translationYBy(context.resources.getInteger(R.integer.slide_up_translation).toFloat())
    .setDurationRes(resources, R.integer.card_disappearance_animation_duration)
    .setStartDelay(startDelay)
    .setInterpolator(interpolator?: AnticipateOvershootInterpolator())
    .setListener(listener)
    .start()
}
