package com.tachys.moneyshare.model;

import java.util.Date;
import java.util.HashMap;

public class Expense {
    public long Id;
    public String Name;
    public HashMap<Member, Double> PaidBy;
    public HashMap<Member, Double> PaidTo;
    public Date LastUpdated;
}
