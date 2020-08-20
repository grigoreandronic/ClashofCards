package com.unitn.clashofcards.feature.onboarding.entity
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.unitn.clashofcards.R

enum class OnBoardingPage(
   // @StringRes val titleResource: Int,
   // @StringRes val subTitleResource: Int,
    @StringRes val descriptionResource: Int,
    @DrawableRes val logoResource: Int
) {

    ONE( R.string.onboarding_slide1_desc,R.drawable.onevsone),
    TWO(R.string.onboarding_slide2_desc, R.drawable.championship),
    THREE(R.string.onboarding_slide3_desc, R.drawable.knockout),
    FOUR(R.string.onboarding_slide4_desc,  R.drawable.lega)
}