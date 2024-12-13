package com.diamondxe.Beans

data class AccountStatementResponse(
    val status: Int,
    val msg: String,
    val details: AccountDetails
)

data class AccountDetails(
    val account_balance: String,
    val total_records: Int,
    val history: List<Transaction>
)

data class Transaction(
    val reference_no: String,
    val type: String,
    val currency_code: String,
    val currency_symbol: String,
    val exchange_rate: String,
    val amount: String,
    val bank_charge_perc: String,
    val bank_charge: String,
    val final_amount: String,
    val payment_mode: String,
    val bank_utr_no: String?,
    val bank_payment_method: String?,
    val bank_transaction_date: String?,
    val payment_status: String,
    val opening_balance: String,
    val closing_balance: String,
    val description: String,
    var created_at: String,
    val transaction_id: String
)
