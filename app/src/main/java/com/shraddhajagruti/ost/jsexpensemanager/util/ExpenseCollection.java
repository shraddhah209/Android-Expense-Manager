package com.shraddhajagruti.ost.jsexpensemanager.util;

import com.shraddhajagruti.ost.jsexpensemanager.table.model.Expense;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by Z50 on 05/09/2016.
 */
public class ExpenseCollection {

    private List<Expense> expenses;

    public ExpenseCollection(List<Expense> expenses) {
        this.expenses = expenses;
    }

    public Long getTotalExpense(){
        Long totalExpense = 0l;
        for (Expense expense : expenses) {
            totalExpense += expense.getAmount();
        }

        return totalExpense;
    }

    public Map<String, List<Expense>> groupByDate() {
        Map<String, List<Expense>> expensesByDate = new HashMap<>();
        for (Expense expense : expenses) {
            if(expensesByDate.get(expense.getDate()) == null){
                List<Expense> expensesList = new ArrayList<>();
                expensesList.add(expense);
                expensesByDate.put(expense.getDate(), expensesList);

            } else {
                expensesByDate.get(expense.getDate()).add(expense);
            }
        }

        return expensesByDate;
    }

    public List<Expense> withoutMoneyTransfer() {
        ArrayList<Expense> expenses = new ArrayList<>();
        for (Expense expense : this.expenses) {
            if(!Objects.equals(expense.getType(), "Money-Transfer"))
                expenses.add(expense);
        }

        return expenses;
    }
}
