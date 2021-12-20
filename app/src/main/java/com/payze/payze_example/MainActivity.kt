package com.payze.payze_example

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import com.payze.paylib.Payze
import com.payze.paylib.model.CardInfo
import com.payze.payze_example.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.initView()

        binding.makePayment.setOnClickListener {
            Log.d("TAG", "onCreate: ${getCardInfo(binding)}")
            val payze = Payze(this)
            payze.pay(
                getCardInfo(binding),
                binding.transactionIdInput.text.toString(),
                onSuccess = {
                    Toast.makeText(this, getString(R.string.success_msg), Toast.LENGTH_LONG).show()
                },
                onError = { code, error ->
                    Toast.makeText(this, getString(R.string.error_msg, code, error), Toast.LENGTH_LONG).show()
                }
            )
        }

    }

    private fun ActivityMainBinding.initView() {
        (monthLayout.editText as AutoCompleteTextView).setAdapter(
            ArrayAdapter(
                this@MainActivity,
                R.layout.simple_dropdown_item,
                getOptList(1, 12)
            )
        )
        (yearLayout.editText as AutoCompleteTextView).setAdapter(
            ArrayAdapter(
                this@MainActivity,
                R.layout.simple_dropdown_item,
                getOptList(22, 10)
            )
        )
    }

    private fun getCardInfo(binding: ActivityMainBinding): CardInfo = with(binding) {
        CardInfo(
            cardNumber.text.toString(),
            cardHolder.text.toString(),
            "${cardExpMonth.text}/${cardExpYear.text}",
            cardCvv.text.toString()
        )
    }

    private fun getOptList(startNumber: Int, count: Int): List<String> {
        val result = mutableListOf<String>()
        for (i in startNumber until startNumber + count) {
            val option = if (i < 10) "0$i" else i.toString()
            result.add(option)
        }
        return result
    }
}