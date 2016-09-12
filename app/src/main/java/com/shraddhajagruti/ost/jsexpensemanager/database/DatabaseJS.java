package com.shraddhajagruti.ost.jsexpensemanager.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.shraddhajagruti.ost.jsexpensemanager.table.ExpenseTable;
import com.shraddhajagruti.ost.jsexpensemanager.table.Expensetypestable;
import com.shraddhajagruti.ost.jsexpensemanager.table.model.Expense;
import com.shraddhajagruti.ost.jsexpensemanager.table.model.ExpenseType;
import com.shraddhajagruti.ost.jsexpensemanager.util.DateUtil;

import java.util.ArrayList;
import java.util.List;

import static com.shraddhajagruti.ost.jsexpensemanager.util.DateUtil.getCurrentDate;
import static com.shraddhajagruti.ost.jsexpensemanager.util.DateUtil.getCurrentWeeksDates;
import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;
/**
 * Created by Z50 on 05/09/2016.
 */
public class DatabaseJS extends SQLiteOpenHelper {
    public static final String EXPENSE_DB = "expense";

    public DatabaseJS(Context context) {
        super(context, EXPENSE_DB, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(ExpenseTable.CREATE_TABLE_QUERY);
        sqLiteDatabase.execSQL(Expensetypestable.CREATE_TABLE_QUERY);
        seedExpenseTypes(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

    }

    public List<String> getExpenseTypes() {
        ArrayList<String> expenseTypes = new ArrayList<>();

        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(Expensetypestable.SELECT_ALL, null);

        if(isCursorPopulated(cursor)){
            do {
                String type = cursor.getString(cursor.getColumnIndex(Expensetypestable.TYPE));
                expenseTypes.add(type);
            } while(cursor.moveToNext());
        }

        return expenseTypes;
    }

    public void deleteAll() {
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(Expensetypestable.TABLE_NAME, "", new String[]{});
        database.delete(ExpenseTable.TABLE_NAME, "", new String[]{});
        database.close();
    }

    public void addExpense(Expense expense) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ExpenseTable.AMOUNT, expense.getAmount());
        values.put(ExpenseTable.TYPE, expense.getType());
        values.put(ExpenseTable.DATE, expense.getDate());

        database.insert(ExpenseTable.TABLE_NAME, null, values);
    }

    public List<Expense> getExpenses() {
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(ExpenseTable.SELECT_ALL, null);

        return buildExpenses(cursor);
    }

    public List<Expense> getTodaysExpenses() {
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(ExpenseTable.getExpensesForDate(getCurrentDate()), null);

        return buildExpenses(cursor);
    }

    public List<Expense> getCurrentWeeksExpenses() {
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(ExpenseTable.getConsolidatedExpensesForDates(getCurrentWeeksDates()), null);
        return buildExpenses(cursor);
    }

    public List<Expense> getExpensesGroupByCategory() {
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(ExpenseTable.SELECT_ALL_GROUP_BY_CATEGORY, null);
        return buildExpenses(cursor);
    }

    public List<Expense> getExpensesForCurrentMonthGroupByCategory() {
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(ExpenseTable.getExpenseForCurrentMonth(DateUtil.currentMonthOfYear()), null);
        return buildExpenses(cursor);
    }

    public void addExpenseType(ExpenseType type) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ExpenseTable.TYPE, type.getType());

        database.insert(Expensetypestable.TABLE_NAME, null, values);
    }

    public void truncate(String tableName) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.execSQL("delete from " + tableName);
    }

    private List<Expense> buildExpenses(Cursor cursor) {
        List<Expense> expenses = new ArrayList<>();
        if(isCursorPopulated(cursor)){
            do {
                String type = cursor.getString(cursor.getColumnIndex(ExpenseTable.TYPE));
                String amount = cursor.getString(cursor.getColumnIndex(ExpenseTable.AMOUNT));
                String date = cursor.getString(cursor.getColumnIndex(ExpenseTable.DATE));
                String id = cursor.getString(cursor.getColumnIndex(ExpenseTable._ID));

                Expense expense = id == null ? new Expense(parseLong(amount), type, date) : new Expense(parseInt(id), parseLong(amount), type, date);
                expenses.add(expense);
            } while(cursor.moveToNext());
        }

        return expenses;
    }

    private boolean isCursorPopulated(Cursor cursor) {
        return cursor != null && cursor.moveToFirst();
    }

    private void seedExpenseTypes(SQLiteDatabase sqLiteDatabase) {
        List<ExpenseType> expenseTypes = Expensetypestable.seedData();
        for (ExpenseType expenseType : expenseTypes) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(Expensetypestable.TYPE, expenseType.getType());

            sqLiteDatabase.insert(Expensetypestable.TABLE_NAME, null, contentValues);
        }
    }
}