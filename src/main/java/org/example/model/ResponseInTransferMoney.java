package org.example.model;

import java.math.BigDecimal;

public class ResponseInTransferMoney {
    private long fromPersonId;
    private long toPersonId;
    private BigDecimal money;

    public ResponseInTransferMoney() {

    }
    public ResponseInTransferMoney(long fromPersonId, long toPersonId, BigDecimal money) {
        this.fromPersonId = fromPersonId;
        this.toPersonId = toPersonId;
        this.money = money;
    }

    public long getFromPersonId() {
        return fromPersonId;
    }

    public void setFromPersonId(long fromPersonId) {
        this.fromPersonId = fromPersonId;
    }

    public long getToPersonId() {
        return toPersonId;
    }

    public void setToPersonId(long toPersonId) {
        this.toPersonId = toPersonId;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }
}
