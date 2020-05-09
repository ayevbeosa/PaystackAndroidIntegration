package com.ayevbeosa.paystackintegration

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import co.paystack.android.PaystackSdk
import co.paystack.android.model.Card
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        PaystackSdk.setPublicKey("YOUR PUBLIC KEY GOES HERE")
    }

    /**
     * Function to create new Card class.
     */
    private fun createCardObject(): Card {
        // Get fields
        val cardNum = card_number_edit_text.text.toString().trim()
        val expiryMonth = cvc_edit_text.text.toString().trim().toInt()
        val expiryYear = expiry_year_edit_text.text.toString().trim().toInt()
        val cvc: String = cvc_edit_text.text.toString().trim()

        return Card.Builder(cardNum, expiryMonth, expiryYear, cvc)
            .build()
    }

    private fun isCardDetailsValid(): Boolean {
        var valid = true

        val inputCardNumber = card_number_edit_text.text.toString().trim()
        if (inputCardNumber.isNotEmpty()) {
            card_number_edit_text_layout.error = null
        } else {
            valid = false
            card_number_edit_text_layout.error = "This field is required"
        }

        val inputCVC = cvc_edit_text.text.toString().trim()
        if (inputCVC.isNotEmpty()) {
            cvc_edit_text_layout.error = null
        } else {
            valid = false
            cvc_edit_text_layout.error = "This field is required"
        }

        val inputExpMonth = expiry_month_edit_text.text.toString().trim()
        if (inputExpMonth.isNotEmpty()) {
            expiry_month_edit_text_layout.error = null
        } else {
            valid = false
            expiry_month_edit_text_layout.error = "This field is required"
        }

        val inputExpYear = expiry_year_edit_text.text.toString().trim()
        if (inputExpYear.isNotEmpty()) {
            expiry_year_edit_text_layout.error = null
        } else {
            valid = false
            expiry_year_edit_text_layout.error = "This field is required"
        }

        return valid
    }
}
