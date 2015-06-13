package com.tachys.moneyshare.dataaccess;

import com.tachys.moneyshare.model.Expense;
import com.tachys.moneyshare.model.Member;

import java.util.ArrayList;

public interface IDataAccess {
    Member addMember(Member member);

    ArrayList<Member> addMember(ArrayList<Member> members);

    ArrayList<Member> getMember();

    Expense addExpense(Expense expense);

    ArrayList<Expense> getExpenses();
}
