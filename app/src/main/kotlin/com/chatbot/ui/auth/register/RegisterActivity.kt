package com.chatbot.ui.auth.register

import android.os.Bundle
import android.transition.Transition
import android.transition.TransitionInflater
import android.view.View
import com.chatbot.R
import com.chatbot.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_register.*
import javax.inject.Inject
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.TargetApi
import android.view.animation.AccelerateInterpolator
import android.view.ViewAnimationUtils
import android.os.Build
import android.support.annotation.RequiresApi


/**
 * @author lusinabrian on 30/10/17.
 * @Notes Register Activity to create a new user with the system
 */
class RegisterActivity : BaseActivity(), RegisterView, View.OnClickListener {

    @Inject
    lateinit var registerPresenter: RegisterPresenter<RegisterView>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // inject dependencies into this activity
        activityComponent.injectRegisterActivity(this)

        // attach your presenter
        registerPresenter.onAttach(this)
    }

    override fun showEnterAnimation() {
        val transition = TransitionInflater.from(this).inflateTransition(R.transition.fabtransition)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.sharedElementEnterTransition = transition
        }
        transition.addListener(object : Transition.TransitionListener {
            override fun onTransitionStart(transition: Transition) {
                registerCardView.visibility = View.GONE
            }

            override fun onTransitionEnd(transition: Transition) {
                transition.removeListener(this)
                registerPresenter.onTransitionEnd()
            }

            override fun onTransitionCancel(transition: Transition) {

            }

            override fun onTransitionPause(transition: Transition) {

            }

            override fun onTransitionResume(transition: Transition) {

            }
        })
    }

    override fun setListeners() {
        registerFab.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v) {
            registerFab -> {
                registerPresenter.onRegisterClose()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun animateRevealShow() {
        val mAnimator = ViewAnimationUtils.createCircularReveal(registerCardView,
                registerCardView.width / 2, 0, (registerFab.width / 2).toFloat(), registerCardView.height.toFloat())
        mAnimator.duration = 500
        mAnimator.interpolator = AccelerateInterpolator()
        mAnimator.addListener(object : AnimatorListenerAdapter() {

            override fun onAnimationStart(animation: Animator) {
                registerCardView.visibility = View.VISIBLE
                super.onAnimationStart(animation)
            }
        })
        mAnimator.start()
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    override fun animateRevealClose() {
        val mAnimator = ViewAnimationUtils.createCircularReveal(registerCardView,
                registerCardView.width / 2, 0,
                registerCardView.height.toFloat(), (registerFab.width / 2).toFloat())
        mAnimator.duration = 500
        mAnimator.interpolator = AccelerateInterpolator()
        mAnimator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                registerCardView.visibility = View.INVISIBLE
                super.onAnimationEnd(animation)
                registerFab.setImageResource(R.drawable.plus)
                onBackPressed()
            }
        })
        mAnimator.start()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        animateRevealClose()
    }
}