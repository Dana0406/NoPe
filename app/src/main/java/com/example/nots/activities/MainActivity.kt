package com.example.nots.activities

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.nots.fragments.FragmentManager
import com.example.nots.R
import com.example.nots.billing.BillingManager
import com.example.nots.databinding.ActivityMainBinding
import com.example.nots.dialogs.NewListDialog
import com.example.nots.fragments.NoteFragment
import com.example.nots.fragments.ShopListNamesFragment
import com.example.nots.settings.SettingsActivity
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

class MainActivity : AppCompatActivity(), NewListDialog.Listener {
    lateinit var binding: ActivityMainBinding
    private var currentMenuItemId = R.id.shop_list
    private var iAd: InterstitialAd? = null
    private lateinit var pref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pref = getSharedPreferences(BillingManager.MAIN_PREF, MODE_PRIVATE)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setBottomNavListener()
        FragmentManager.setFragment(ShopListNamesFragment.newInstance(), this)
        if(pref.getBoolean(BillingManager.REMOVE_ADS_KEY, false)) loadInterAd()
    }

    private fun loadInterAd(){
        val request = com.google.android.gms.ads.AdRequest.Builder().build()
        InterstitialAd.load(this, getString(R.string.inter_ad_id), request, object: InterstitialAdLoadCallback(){
            override fun onAdLoaded(ad: InterstitialAd) {
                iAd = ad
            }
            override fun onAdFailedToLoad(ad: LoadAdError) {
                iAd = null
            }
        })
    }

    private fun showInterAd(adListener: AdListener){
        if(iAd!=null){
            iAd?.fullScreenContentCallback = object: FullScreenContentCallback(){
                override fun onAdDismissedFullScreenContent() {
                    iAd = null
                    loadInterAd()
                    adListener.onFinish()
                }
                override fun onAdShowedFullScreenContent() {
                    iAd = null
                    loadInterAd()
                }
                override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                    iAd = null
                    loadInterAd()
                }
            }
            iAd?.show(this)
        }else{
            adListener.onFinish()
        }
    }

    private fun setBottomNavListener(){
        binding.bNav.setOnItemSelectedListener {
            when(it.itemId){
                R.id.settings -> {
                    showInterAd(object: AdListener{
                        override fun onFinish() {
                            startActivity(Intent(this@MainActivity, SettingsActivity::class.java))
                        }
                    })
                }
                R.id.notes -> {
                    currentMenuItemId = R.id.notes
                    FragmentManager.setFragment(NoteFragment.newInstance(), this@MainActivity)
                }
                R.id.shop_list -> {
                    currentMenuItemId = R.id.shop_list
                    FragmentManager.setFragment(ShopListNamesFragment.newInstance(), this)
                }
                R.id.new_item -> {
                    FragmentManager.currentFrag?.onClickNew()
                }
            }
            true
        }
    }

    override fun onResume() {
        super.onResume()
        binding.bNav.selectedItemId = currentMenuItemId
    }

    override fun onClick(name: String) {
        Log.d("MyLog", "Name: $name")
    }

    interface AdListener{
        fun onFinish()
    }
}