package com.example.ads

import com.example.BuildConfig

/**
 * Centralized AdMob configuration for Bhakti Sagar.
 *
 * Automatically manages test IDs in debug builds and production IDs in release builds.
 * Preserves Google AdMob policies and ensures no production ad requests occur in debug mode.
 */
object AdMobConfig {

    // Production App and Ad Unit IDs
    const val PROD_APP_ID = "ca-app-pub-4890481636897653~2027648440"
    const val PROD_APP_OPEN_AD_UNIT_ID = "ca-app-pub-4890481636897653/7309950620"
    const val PROD_BANNER_AD_UNIT_ID = "ca-app-pub-4890481636897653/4150589766"
    const val PROD_INTERSTITIAL_AD_UNIT_ID = "ca-app-pub-4890481636897653/6553391823"
    const val PROD_NATIVE_AD_UNIT_ID = "ca-app-pub-4890481636897653/6843046766"
    const val PROD_REWARDED_AD_UNIT_ID = "ca-app-pub-4890481636897653/2329086681"

    // Google Official Test IDs
    const val TEST_APP_ID = "ca-app-pub-3940256099942544~3347511713"
    const val TEST_APP_OPEN_AD_UNIT_ID = "ca-app-pub-3940256099942544/9257395921"
    const val TEST_BANNER_AD_UNIT_ID = "ca-app-pub-3940256099942544/6300978111"
    const val TEST_INTERSTITIAL_AD_UNIT_ID = "ca-app-pub-3940256099942544/1033173712"
    const val TEST_NATIVE_AD_UNIT_ID = "ca-app-pub-3940256099942544/2247696110"
    const val TEST_REWARDED_AD_UNIT_ID = "ca-app-pub-3940256099942544/5224354917"

    val isTestMode: Boolean
        get() = BuildConfig.DEBUG || BuildConfig.IS_ADMOB_TEST

    fun getAppId(): String = if (isTestMode) TEST_APP_ID else PROD_APP_ID

    fun getAppOpenAdUnitId(): String = if (isTestMode) TEST_APP_OPEN_AD_UNIT_ID else PROD_APP_OPEN_AD_UNIT_ID

    fun getBannerAdUnitId(): String = if (isTestMode) TEST_BANNER_AD_UNIT_ID else PROD_BANNER_AD_UNIT_ID

    fun getInterstitialAdUnitId(): String = if (isTestMode) TEST_INTERSTITIAL_AD_UNIT_ID else PROD_INTERSTITIAL_AD_UNIT_ID

    fun getNativeAdUnitId(): String = if (isTestMode) TEST_NATIVE_AD_UNIT_ID else PROD_NATIVE_AD_UNIT_ID

    fun getRewardedAdUnitId(): String = if (isTestMode) TEST_REWARDED_AD_UNIT_ID else PROD_REWARDED_AD_UNIT_ID
}
