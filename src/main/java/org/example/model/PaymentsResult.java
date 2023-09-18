package org.example.model;

import java.math.BigDecimal;

public class PaymentsResult {

    private BigDecimal amount;
    private String status;
    private String account_from;
    private String account_to;

    public PaymentsResult() {

    }


    public PaymentsResult(BigDecimal amount, String status, String account_from, String account_to) {
        this.amount = amount;
        this.status = status;
        this.account_from = account_from;
        this.account_to = account_to;
    }



    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAccount_from() {
        return account_from;
    }

    public void setAccount_from(String account_from) {
        this.account_from = account_from;
    }

    public String getAccount_to() {
        return account_to;
    }

    public void setAccount_to(String account_to) {
        this.account_to = account_to;
    }
}
