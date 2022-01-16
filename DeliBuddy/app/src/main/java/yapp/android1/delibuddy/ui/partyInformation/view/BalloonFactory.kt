package yapp.android1.delibuddy.ui.partyInformation.view

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.skydoves.balloon.ArrowOrientation
import com.skydoves.balloon.Balloon
import com.skydoves.balloon.BalloonAnimation
import com.skydoves.balloon.BalloonSizeSpec
import yapp.android1.delibuddy.R


internal class OptionsMenuBalloonForOwnerFactory : Balloon.Factory() {

    override fun create(context: Context, lifecycle: LifecycleOwner?): Balloon {
        return Balloon.Builder(context)
            .setLayout(R.layout.view_party_options_menu)
            .setWidth(BalloonSizeSpec.WRAP)
            .setHeight(BalloonSizeSpec.WRAP)
            .setIsVisibleArrow(false)
            .setCornerRadius(10f)
            .setMarginRight(20)
            .setBalloonAnimation(BalloonAnimation.ELASTIC)
            .setLifecycleOwner(lifecycle)
            .build()
    }

}

internal class OptionsMenuBalloonForUserFactory : Balloon.Factory() {

    override fun create(context: Context, lifecycle: LifecycleOwner?): Balloon {
        return Balloon.Builder(context)
            .setLayout(R.layout.view_party_options_menu_for_user)
            .setWidth(BalloonSizeSpec.WRAP)
            .setHeight(BalloonSizeSpec.WRAP)
            .setIsVisibleArrow(false)
            .setCornerRadius(10f)
            .setMarginRight(20)
            .setBalloonAnimation(BalloonAnimation.ELASTIC)
            .setLifecycleOwner(lifecycle)
            .build()
    }

}

internal class MyPartyOptionsMenuBalloonFactory : Balloon.Factory() {

    override fun create(context: Context, lifecycle: LifecycleOwner?): Balloon {
        return Balloon.Builder(context)
            .setLayout(R.layout.view_myparty_options_menu)
            .setWidth(BalloonSizeSpec.WRAP)
            .setHeight(BalloonSizeSpec.WRAP)
            .setIsVisibleArrow(false)
            .setCornerRadius(10f)
            .setMarginRight(20)
            .setBalloonAnimation(BalloonAnimation.ELASTIC)
            .setLifecycleOwner(lifecycle)
            .build()
    }

}

internal class CommentOptionsBalloonFactory : Balloon.Factory() {

    override fun create(context: Context, lifecycle: LifecycleOwner?): Balloon {
        return Balloon.Builder(context)
            .setLayout(R.layout.view_comment_options_menu)
            .setWidth(BalloonSizeSpec.WRAP)
            .setHeight(BalloonSizeSpec.WRAP)
            .setIsVisibleArrow(false)
            .setCornerRadius(10f)
            .setMarginRight(20)
            .setBalloonAnimation(BalloonAnimation.ELASTIC)
            .setLifecycleOwner(lifecycle)
            .build()
    }

}