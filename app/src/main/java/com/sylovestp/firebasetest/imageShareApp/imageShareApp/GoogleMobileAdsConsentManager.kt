package com.sylovestp.firebasetest.imageShareApp.imageShareApp

import android.app.Activity
import android.content.Context
import com.google.android.ump.ConsentDebugSettings
import com.google.android.ump.ConsentForm.OnConsentFormDismissedListener
import com.google.android.ump.ConsentInformation
import com.google.android.ump.ConsentRequestParameters
import com.google.android.ump.FormError
import com.google.android.ump.UserMessagingPlatform

/**
 * The Google Mobile Ads SDK provides the User Messaging Platform (Google's IAB Certified consent
 * management platform) as one solution to capture consent for users in GDPR impacted countries.
 * This is an example and you can choose another consent management platform to capture consent.
 */
class GoogleMobileAdsConsentManager private constructor(context: Context) {
    private val consentInformation: ConsentInformation =
        UserMessagingPlatform.getConsentInformation(context)

    /** Interface definition for a callback to be invoked when consent gathering is complete. */
    fun interface OnConsentGatheringCompleteListener {
        fun consentGatheringComplete(error: FormError?)
    }

    /** Helper variable to determine if the app can request ads. */
    val canRequestAds: Boolean
        get() = consentInformation.canRequestAds()

    /** Helper variable to determine if the privacy options form is required. */
    val isPrivacyOptionsRequired: Boolean
        get() =
            consentInformation.privacyOptionsRequirementStatus ==
                    ConsentInformation.PrivacyOptionsRequirementStatus.REQUIRED

    // 동의, 비동의 체크를 위해 추가한 코드 [[
    // https://itnext.io/android-admob-consent-with-ump-personalized-or-non-personalized-ads-in-eea-3592e192ec90
    // https://stackoverflow.com/questions/65351543/how-to-implement-ump-sdk-correctly-for-eu-consent/68310602#68310602
    fun isGDPR(context: Context): Boolean {
        val prefs = context.getSharedPreferences(context.packageName + "_preferences", Context.MODE_PRIVATE)
        val gdpr = prefs.getInt("IABTCF_gdprApplies", 0)
        return gdpr == 1
    }

    fun canShowAds(context: Context): Boolean {
        //val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        // PreferenceManager 가 Deprecated 되어서 아래와 같이 사용함
        val prefs = context.getSharedPreferences(context.packageName + "_preferences", Context.MODE_PRIVATE)
        val purposeConsent = prefs.getString("IABTCF_PurposeConsents", "") ?: ""
        val vendorConsent = prefs.getString("IABTCF_VendorConsents", "") ?: ""
        val vendorLI = prefs.getString("IABTCF_VendorLegitimateInterests", "") ?: ""
        val purposeLI = prefs.getString("IABTCF_PurposeLegitimateInterests", "") ?: ""

        val googleId = 755
        val hasGoogleVendorConsent = hasAttribute(vendorConsent, googleId)
        val hasGoogleVendorLI = hasAttribute(vendorLI, googleId)

        // Personalized Ads: listOf(1, 3, 4)
        return hasConsentFor(listOf(1), purposeConsent, hasGoogleVendorConsent)
                && hasConsentOrLegitimateInterestFor(
            listOf(2,7,9,10),
            purposeConsent,
            purposeLI,
            hasGoogleVendorConsent,
            hasGoogleVendorLI
        )
    }

    // Check if a binary string has a "1" at position "index" (1-based)
    private fun hasAttribute(input: String, index: Int): Boolean {
        return input.length >= index && input[index-1] == '1'
    }

    // Check if consent is given for a list of purposes
    private fun hasConsentFor(purposes: List<Int>, purposeConsent: String, hasVendorConsent: Boolean): Boolean {
        return purposes.all { p -> hasAttribute(purposeConsent, p)} && hasVendorConsent
    }

    // Check if a vendor either has consent or legitimate interest for a list of purposes
    private fun hasConsentOrLegitimateInterestFor(purposes: List<Int>, purposeConsent: String, purposeLI: String, hasVendorConsent: Boolean, hasVendorLI: Boolean): Boolean {
        return purposes.all { p ->
            (hasAttribute(purposeLI, p) && hasVendorLI) ||
                    (hasAttribute(purposeConsent, p) && hasVendorConsent)
        }
    }
    // 동의, 비동의 체크를 위해 추가한 코드 ]]

    /**
     * Helper method to call the UMP SDK methods to request consent information and load/show a
     * consent form if necessary.
     */
    fun gatherConsent(
        activity: Activity,
        onConsentGatheringCompleteListener: OnConsentGatheringCompleteListener
    ) {
        // For testing purposes, you can force a DebugGeography of EEA or NOT_EEA.
        val debugSettings =
            ConsentDebugSettings.Builder(activity)
                .setDebugGeography(ConsentDebugSettings.DebugGeography.DEBUG_GEOGRAPHY_EEA) // EEA 지역이라고 강제 설정
                // Check your logcat output for the hashed device ID e.g.
                // "Use new ConsentDebugSettings.Builder().addTestDeviceHashedId("ABCDEF012345")" to use
                // the debug functionality.
                .addTestDeviceHashedId("본인의-테스트-디바이스-해시-아이디")
                .build()

        val params = ConsentRequestParameters
            .Builder()
            .setConsentDebugSettings(debugSettings) // 테스트 시에만 사용
            .build()

        // consentInformation.reset() // 동의 상태 재설정, 테스트 시에만 사용
        // Requesting an update to consent information should be called on every app launch.
        consentInformation.requestConsentInfoUpdate(
            activity,
            params,
            {
                UserMessagingPlatform.loadAndShowConsentFormIfRequired(activity) { formError ->
                    // Consent has been gathered.
                    onConsentGatheringCompleteListener.consentGatheringComplete(formError)
                }
            },
            { requestConsentError ->
                onConsentGatheringCompleteListener.consentGatheringComplete(requestConsentError)
            }
        )
    }

    /** Helper method to call the UMP SDK method to show the privacy options form. */
    fun showPrivacyOptionsForm(
        activity: Activity,
        onConsentFormDismissedListener: OnConsentFormDismissedListener
    ) {
        UserMessagingPlatform.showPrivacyOptionsForm(activity, onConsentFormDismissedListener)
    }

    companion object {
        @Volatile private var instance: GoogleMobileAdsConsentManager? = null

        fun getInstance(context: Context) =
            instance
                ?: synchronized(this) {
                    instance ?: GoogleMobileAdsConsentManager(context).also { instance = it }
                }
    }
}
