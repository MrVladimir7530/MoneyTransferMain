package org.example.model;

public class PersonInfoResponse
{
  private personInfo fromPersonId;
  private personInfo toPersonId;

  public PersonInfoResponse() {

  }
    public PersonInfoResponse(personInfo fromPersonId, personInfo toPersonId) {
        this.fromPersonId = fromPersonId;
        this.toPersonId = toPersonId;
    }

    public personInfo getFromPersonId() {
        return fromPersonId;
    }

    public personInfo getToPersonId() {
        return toPersonId;
    }
}
