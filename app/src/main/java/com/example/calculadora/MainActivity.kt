package com.example.calculadora

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.nio.file.Files.write

class MainActivity : AppCompatActivity() {
    private lateinit var evaluator: ExpressionEvaluator
    private lateinit var txtCounts: TextView
    private var txtDisplay: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnEquals = findViewById<Button>(R.id.btnEquals)
        val btnDot = findViewById<Button>(R.id.btnDot)
        val btnNum0 = findViewById<Button>(R.id.btnNum0)
        val btnPlus = findViewById<Button>(R.id.btnPlus)
        val btnNum2 = findViewById<Button>(R.id.btnNum2)
        val btnNum3 = findViewById<Button>(R.id.btnNum3)
        val btnNum1 = findViewById<Button>(R.id.btnNum1)
        val btnMinus = findViewById<Button>(R.id.btnMinus)
        val btnNum6 = findViewById<Button>(R.id.btnNum6)
        val btnNum5 = findViewById<Button>(R.id.btnNum5)
        val btnNum4 = findViewById<Button>(R.id.btnNum4)
        val btnMulti = findViewById<Button>(R.id.btnMulti)
        val btnNum9 = findViewById<Button>(R.id.btnNum9)
        val btnNum8 = findViewById<Button>(R.id.btnNum8)
        val btnErase = findViewById<Button>(R.id.btnErase)
        val btnDelete = findViewById<Button>(R.id.btnDelete)
        val btnNum7 = findViewById<Button>(R.id.btnNum7)
        val btnDivision = findViewById<Button>(R.id.btnDivision)
        val btnPorsent = findViewById<Button>(R.id.btnPorsent)

        txtCounts = findViewById(R.id.txtCounts)

        evaluator = ExpressionEvaluator()

        btnNum0.setOnClickListener{ writeNumber(0) }
        btnNum1.setOnClickListener{ writeNumber(1) }
        btnNum2.setOnClickListener{ writeNumber(2) }
        btnNum3.setOnClickListener{ writeNumber(3) }
        btnNum4.setOnClickListener{ writeNumber(4) }
        btnNum5.setOnClickListener{ writeNumber(5) }
        btnNum6.setOnClickListener{ writeNumber(6) }
        btnNum7.setOnClickListener{ writeNumber(7) }
        btnNum8.setOnClickListener{ writeNumber(8) }
        btnNum9.setOnClickListener{ writeNumber(9) }

        btnPlus.setOnClickListener { writeOperator('+') }
        btnMinus.setOnClickListener { writeOperator('-') }
        btnMulti.setOnClickListener { writeOperator('*') }
        btnDivision.setOnClickListener { writeOperator('/') }
        btnPorsent.setOnClickListener { writeOperator('%') }
        btnDot.setOnClickListener { writeOperator('.') }

        btnDelete.setOnClickListener { clear() }

        btnErase.setOnClickListener { deleteLast() }

        btnEquals.setOnClickListener {
            val result = evaluator.evaluateExpression(txtDisplay)
            if (result == null || result == "Error") {
                txtCounts.text = "Error"
                txtDisplay = ""
            } else {
                txtDisplay = result
                txtCounts.text = txtDisplay
            }
        }

    }
    private fun writeNumber(value: Int){
        txtDisplay = txtDisplay + value
        txtCounts.text = txtDisplay
    }
    private fun clear(){
        txtCounts.text = "0"
        txtDisplay = ""
    }
    private fun writeOperator(op: Char){
        val operadores = setOf('+','-','*','/')
        if (txtDisplay.isEmpty()) {
            when (op){
                '-' -> {
                    txtDisplay = "-"
                    txtCounts.text = txtDisplay
                }
                '.' -> {
                    txtDisplay = "0."
                    txtCounts.text = txtDisplay
                }
                else -> {}
            }
            return
        }
        else{
            if (txtDisplay.last() in operadores)
                if (op == '-' && txtDisplay.last() != '-')
                    txtDisplay += op
                else
                    txtDisplay = txtDisplay.dropLast(1) + op
            else
                txtDisplay = txtDisplay + op
            txtCounts.text = txtDisplay
        }
    }
    private fun deleteLast(){
        if (txtDisplay.isEmpty())
            return
        if (txtDisplay.count() == 1)
            clear()
        else{
            txtDisplay = txtDisplay.dropLast(1)
            txtCounts.text = txtDisplay
        }
    }
}

