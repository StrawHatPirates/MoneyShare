package com.tachys.moneyshare.model;

public class MicroExpense {
    public MicroExpense(long payerId, long payeeId, double amount, long expenseId) {
        this.PayerId = payerId;
        this.PayeeId = payeeId;
        this.Amount = amount;
        this.ExpenseId = expenseId;
    }

    public MicroExpense(long payerId, long payeeId, double amount, long expenseId, long paymentId) {
        this.Id = paymentId;
        this.PayerId = payerId;
        this.PayeeId = payeeId;
        this.Amount = amount;
        this.ExpenseId = expenseId;
    }

    public long Id;
    public long PayerId;
    public long PayeeId;
    public double Amount;
    public long ExpenseId;
}
