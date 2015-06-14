package com.tachys.moneyshare.dataaccess;

import com.tachys.moneyshare.model.Expense;
import com.tachys.moneyshare.model.Member;
import com.tachys.moneyshare.model.Settlement;

import java.util.ArrayList;

public interface IDataAccess {
    Member addMember(Member member);

    ArrayList<Member> addMember(ArrayList<Member> members);

    Member findMember(String email);

    ArrayList<Member> getMember();

    Member getMember(long memberId);

    Expense addExpense(Expense expense);

    ArrayList<Expense> getExpenses();

    Settlement addSettlement(Settlement settlement);
}
