package org.example.model;

public class PersonInfoRequest {
    private String fromPhone;
    private String toPhone;
    public PersonInfoRequest() {

    }

    public PersonInfoRequest(String fromPhone, String toPhone) {
        this.fromPhone = fromPhone;
        this.toPhone = toPhone;
    }

    public String getFromPhone() {
        return fromPhone;
    }

    public void setFromPhone(String fromPhone) {
        this.fromPhone = fromPhone;
    }

    public String getToPhone() {
        return toPhone;
    }

    public void setToPhone(String toPhone) {
        this.toPhone = toPhone;
    }
}
