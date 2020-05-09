package com.ayevbeosa.paystackintegration

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import co.paystack.android.Paystack
import co.paystack.android.PaystackSdk
import co.paystack.android.Transaction
import co.paystack.android.exceptions.ExpiredAccessCodeException
import co.paystack.android.model.Card
import co.paystack.android.model.Charge
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        PaystackSdk.setPublicKey("YOUR PUBLIC KEY GOES HERE")
    }

    /**
     * On calling this function, transaction can be initiated and
     * if successful, the card is charged.
     */
    fun performCharge() {

        if (!isCardDetailsValid()) {
            return
        }

        if (createCardObject().isValid) {

            //create a Charge object
            val charge = Charge()
            charge.card = createCardObject() // sets the card to charge
            charge.amount = 100
            charge.currency = "CURRENCY"
            charge.email = "example@android.com"
            charge.reference = "ChargedFromAndroid_" + System.currentTimeMillis()
            charge.putCustomField("Charged From", "Android SDK")

            PaystackSdk.chargeCard(this, charge, object : Paystack.TransactionCallback {
                override fun onSuccess(transaction: Transaction?) {
                    // This is called only after transaction is deemed successful.
                    // Retrieve the transaction, and send its reference to your server
                    // for verification.
                    Toast.makeText(this@MainActivity, transaction?.reference, Toast.LENGTH_LONG)
                        .show()
                }

                override fun beforeValidate(transaction: Transaction?) {
                    // This is called only before requesting OTP.
                    // Save reference so you may send to server. If
                    // error occurs with OTP, you should still verify on server.
                    Toast.makeText(this@MainActivity, transaction?.reference, Toast.LENGTH_LONG)
                        .show()
                }

                override fun onError(error: Throwable, transaction: Transaction?) {
                    //handle error here

                    if (error is ExpiredAccessCodeException) {
                        performCharge()
                        return
                    }

                    if (transaction!!.reference != null) {
                        Toast.makeText(
                            this@MainActivity,
                            transaction.reference
                                .toString() + " concluded with error: " + error.message,
                            Toast.LENGTH_LONG
                        ).show()
                        val errorText = String.format(
                            "%s  concluded with error: %s %s",
                            transaction.reference,
                            error.javaClass.simpleName,
                            error.message
                        )
                        Log.e(this@MainActivity.javaClass.simpleName, errorText)
                    } else {
                        Toast.makeText(this@MainActivity, error.message, Toast.LENGTH_LONG)
                            .show()
                        val errorText = String.format(
                            "Error: %s %s",
                            error.javaClass.simpleName,
                            error.message
                        )
                        Log.e(this@MainActivity.javaClass.simpleName, errorText)
                    }
                }
            })
        } else {

            Snackbar.make(
                findViewById(android.R.id.content),
                "Invalid card details",
                Snackbar.LENGTH_SHORT
            )
                .show()
        }
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
