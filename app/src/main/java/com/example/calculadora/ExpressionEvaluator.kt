package com.example.calculadora

class ExpressionEvaluator {

    fun evaluateExpression(input: String): String? {
        val expr = input.replace("\\s".toRegex(), "")
        if (expr.isEmpty()) return null

        val tokens = tokenize(expr) ?: return null
        val rpn = toRPN(tokens) ?: return null
        val result = evalRPN(rpn) ?: return null

        return formatResult(result)
    }

    // --- Tokenizer ---
    private fun tokenize(s: String): List<String>? {
        val tokens = mutableListOf<String>()
        var number = StringBuilder()
        var i = 0

        fun flushNumber() {
            if (number.isNotEmpty()) {
                tokens.add(number.toString())
                number = StringBuilder()
            }
        }

        while (i < s.length) {
            val c = s[i]
            when {
                c.isDigit() || c == '.' -> number.append(c)
                c == '%' -> {
                    if (number.isEmpty()) return null
                    val n = number.toString().toDoubleOrNull() ?: return null
                    number = StringBuilder((n / 100).toString())
                    flushNumber()
                }
                c in "+-*/()" -> {
                    flushNumber()
                    // detectar signo negativo unario
                    if (c == '-' && (tokens.isEmpty() || tokens.last() in listOf("+", "-", "*", "/", "("))) {
                        tokens.add("u-")
                    } else {
                        tokens.add(c.toString())
                    }
                }
                else -> return null
            }
            i++
        }
        flushNumber()
        return tokens
    }

    // --- Shunting Yard (Infix a RPN) ---
    private fun toRPN(tokens: List<String>): List<String>? {
        val output = mutableListOf<String>()
        val stack = ArrayDeque<String>()

        val precedence = mapOf("u-" to 3, "*" to 2, "/" to 2, "+" to 1, "-" to 1)
        val leftAssoc = mapOf("u-" to false, "*" to true, "/" to true, "+" to true, "-" to true)

        for (token in tokens) {
            when {
                token.toDoubleOrNull() != null -> output.add(token)
                token in precedence.keys -> {
                    val p = precedence[token]!!
                    val left = leftAssoc[token] ?: true
                    while (stack.isNotEmpty() && stack.first() in precedence.keys) {
                        val top = stack.first()
                        val pTop = precedence[top]!!
                        if ((left && pTop >= p) || (!left && pTop > p)) {
                            output.add(stack.removeFirst())
                        } else break
                    }
                    stack.addFirst(token)
                }
                token == "(" -> stack.addFirst(token)
                token == ")" -> {
                    while (stack.isNotEmpty() && stack.first() != "(") {
                        output.add(stack.removeFirst())
                    }
                    if (stack.isEmpty() || stack.first() != "(") return null
                    stack.removeFirst()
                }
                else -> return null
            }
        }

        while (stack.isNotEmpty()) {
            val t = stack.removeFirst()
            if (t == "(" || t == ")") return null
            output.add(t)
        }
        return output
    }

    // --- Evaluar RPN ---
    private fun evalRPN(rpn: List<String>): Double? {
        val stack = ArrayDeque<Double>()

        for (t in rpn) {
            val num = t.toDoubleOrNull()
            if (num != null) {
                stack.addFirst(num)
            } else when (t) {
                "u-" -> {
                    if (stack.isEmpty()) return null
                    val a = stack.removeFirst()
                    stack.addFirst(-a)
                }
                "+", "-", "*", "/" -> {
                    if (stack.size < 2) return null
                    val b = stack.removeFirst()
                    val a = stack.removeFirst()
                    val res = when (t) {
                        "+" -> a + b
                        "-" -> a - b
                        "*" -> a * b
                        "/" -> if (b == 0.0) return null else a / b
                        else -> return null
                    }
                    stack.addFirst(res)
                }
                else -> return null
            }
        }
        if (stack.size != 1) return null
        return stack.first()
    }

    // --- Formateo ---
    private fun formatResult(value: Double): String {
        if (!value.isFinite()) return "Error"
        val longVal = value.toLong()
        return if (value == longVal.toDouble()) longVal.toString()
        else String.format("%.8f", value).trimEnd('0').trimEnd('.')
    }
}
