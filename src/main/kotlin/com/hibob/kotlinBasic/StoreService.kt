package com.hibob.kotlinBasic

import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.LocalDateTime

@Component
class StoreService {

    fun pay(cart: List<Cart>, payment: Payment): Map<String, Check> {
        /*return cart.map { it -> it.clientId to checkout(it, payment) }.toMap()*/
        return cart.associate { it -> it.clientId to checkout(it, payment) }
    }

    fun checkout(cart : Cart , payment: Payment): Check {
        val totalAmount = cart.products.filter { product -> check(product.custom) }.sumOf { product->product.price }

        if (checkPayment(payment , totalAmount)) return Check(cart.clientId , Statuses.SUCCESS , totalAmount)
        return Check(cart.clientId , Statuses.FAILURE , 0.0)
    }

/*
    fun checkPayment (payment: Payment ,price : Double): Boolean {
    return when(payment){
        is Payment.Cash-> fail("you cant pay with cash")
        is Payment.PayPal-> '@' in payment.email
        is Payment.CreditCard-> (payment.type == CreditCardType.MASTERCARD || payment.type == CreditCardType.VISA ) && (payment.expiryDate.isAfter(LocalDate.now())) &&(price<payment.limit)&&(payment.number.length==10)
    }
    }

 */

    fun checkPayment (payment: Payment ,price : Double): Boolean {
        return when(payment){
            is Payment.Cash-> fail("you cant pay with cash")
            is Payment.PayPal-> "@" in payment.email
            is Payment.CreditCard-> validateCreditCard(payment, price)
        }
    }

    private fun validateCreditCard(payment: Payment.CreditCard, price: Double) =
        isSupportedType(payment.type) && isCardDetailsValid(payment) && isNotOverTheLimit(price, payment)

    private fun isSupportedType(cardType: CreditCardType) =
        (cardType == CreditCardType.MASTERCARD || cardType == CreditCardType.VISA)

    private fun isCardDetailsValid(payment: Payment.CreditCard) =
        payment.expiryDate.isAfter(LocalDate.now())  && payment.number.length == 10

    private fun isNotOverTheLimit(price: Double, payment: Payment.CreditCard) = price < payment.limit

    fun check(obj: Any): Boolean {
        return obj is Boolean && obj==true
    }

    fun fail(message: String): Nothing {
        throw IllegalStateException(message)
    }


}

//6. write function called checkout and get cart and payment that pay the money
//   * only custom with true are valid
//   * cash payment is not valid to pay so if the payment is cash use fail function
//   * in case of credit card need to validate the expiryDate is after the current date + limit is bigger than the total we need to pay and we allow to use only  VISA or MASTERCARD
//   * in case of payPal validate we hae @
///  * the return value of this function, should be a data class with employee id, status (success or failed) and total called Check