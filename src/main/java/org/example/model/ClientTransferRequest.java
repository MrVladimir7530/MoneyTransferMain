package org.example.model;

import java.math.BigDecimal;

public class ClientTransferRequest {
    private String fromPhone;
    private String toPhone;
    private BigDecimal amount;

    public String getFromPhone() {
        return fromPhone;
    }

    public String getToPhone() {
        return toPhone;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}
