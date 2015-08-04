package com.tachys.moneyshare.model;

import java.util.Date;
import java.util.HashMap;

public class Expense {
    public Expense() {
        this.PaidBy = new HashMap<>();
        this.PaidTo = new HashMap<>();
    }
    public long Id;
    public String Name;
    public HashMap<Member, Double> PaidBy;
    public HashMap<Member, Double> PaidTo;
    public Date LastUpdated;

    public double getAmountForPerson(long memberId) {
        double paidMoney = 0;
        double owedMoney = 0;

        for (Member member : this.PaidBy.keySet()) {
            if (member != null && member.Id == memberId) {
                paidMoney = this.PaidBy.get(member);
                break;
            }
        }

        for (Member member : this.PaidTo.keySet()) {
            if (member != null && member.Id == memberId) {
                owedMoney = this.PaidTo.get(member);
                break;
            }
        }

        return paidMoney - owedMoney;
    }

    public double getTotalAmount() {
        double total = 0;
        for (Member member : this.PaidBy.keySet()) {
            total += this.PaidBy.get(member);
        }
        return total;
    }
}
