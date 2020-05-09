package com.ayevbeosa.paystackintegration

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import co.paystack.android.PaystackSdk

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        PaystackSdk.setPublicKey("YOUR PUBLIC KEY GOES HERE")
    }
}
