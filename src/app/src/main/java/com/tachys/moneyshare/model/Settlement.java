package com.tachys.moneyshare.model;

public class Settlement {

    public Settlement(long payerId, long payeeId, double paymentAmount) {
        this.PayerId = payerId;
        this.PayeeId = payeeId;
        this.PaymentAmount = paymentAmount;
    }

    public Settlement(long payerId, long payeeId, double paymentAmount, long paymentId) {
        this.Id = paymentId;
        this.PayerId = payerId;
        this.PayeeId = payeeId;
        this.PaymentAmount = paymentAmount;
    }

    public long Id;
    public long PayerId;
    public long PayeeId;
    public double PaymentAmount;
}
