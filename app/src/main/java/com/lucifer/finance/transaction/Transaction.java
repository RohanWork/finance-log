package com.lucifer.finance.transaction;

import java.io.Serializable;
import java.math.BigDecimal;

public class Transaction implements Serializable {
    private String date;
    private String time;
    private BigDecimal amount;
    private String bankName;
    private int bankLogo;
    private boolean isCredit;

    public Transaction(String date, String time, BigDecimal amount, String bankName, int bankLogo, boolean isCredit) {
        this.date = date;
        this.time = time;
        this.amount = amount;
        this.bankName = bankName;
        this.bankLogo = bankLogo;
        this.isCredit = isCredit;
    }

    public Transaction(String date, String time, BigDecimal amount, String bankName, boolean isCredit) {
        this.date = date;
        this.time = time;
        this.amount = amount;
        this.bankName = bankName;
        this.bankLogo = bankLogo;
        this.isCredit = isCredit;
    }

    public boolean isCredit() {
        return isCredit;
    }

    public void setCredit(boolean credit) {
        isCredit = credit;
    }
    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getBankName() {
        return bankName;
    }

    public int getBankLogo() {
        return bankLogo;
    }
}
