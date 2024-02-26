package com.example.bootclotto

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.NumberPicker
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.example.bootclotto.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val numTextViewList: List<TextView> by lazy {
        listOf(
            binding.tvNum1,
            binding.tvNum2,
            binding.tvNum3,
            binding.tvNum4,
            binding.tvNum5,
            binding.tvNum6,
        )
    }
    private val pickNumSet = hashSetOf<Int>()
    private var didRun = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.npNum.minValue = 1
        binding.npNum.maxValue = 45

        initRunButton()
        initAddButton()
        initClearButton()
    }

    private fun initAddButton() {
        binding.btnAdd.setOnClickListener {
            when {
                didRun -> showToast("초기화 후에 시도해주세요.")
                pickNumSet.size >= 5 -> showToast("숫자는 최대 5개까지 선택할 수 있습니다.")
                pickNumSet.contains(binding.npNum.value) -> showToast("이미 선택한 숫자입니다.")
                else -> {
                    val textView = numTextViewList[pickNumSet.size]
                    textView.isVisible = true
                    textView.text = binding.npNum.value.toString()

                    setNumBack(binding.npNum.value, textView)
                    pickNumSet.add(binding.npNum.value)
                }
            }
        }
    }

    private fun setNumBack(number: Int, textView: TextView) {
        val background = when (number) {
            in 1..10 -> R.drawable.circle_yellow
            in 11..20 -> R.drawable.circle_blue
            in 21..30 -> R.drawable.circle_red
            in 31..40 -> R.drawable.circle_gray
            else -> R.drawable.circle_green
        }
        textView.background = ContextCompat.getDrawable(this, background)
    }

    private fun initClearButton() {
        binding.btnClear.setOnClickListener {
            pickNumSet.clear()
            numTextViewList.forEach { it.isVisible = false }
            didRun = false
            binding.npNum.value = 1
        }
    }

    private fun initRunButton() {
        binding.btnRun.setOnClickListener {
            val list = getRandom()
            didRun = true

            list.forEachIndexed { index, number ->
                val textView = numTextViewList[index]
                textView.text = number.toString()
                textView.isVisible = true
                setNumBack(number, textView)
            }
        }
    }

    private fun getRandom(): List<Int> {
        val numbers = (1..45).filter { it !in pickNumSet }
        return (pickNumSet + numbers.shuffled().take(6 - pickNumSet.size)).sorted()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}