package faz.ie.partyapp.models;


public class Party {

    public String partyName;
    public String hostName;
    public String county;
    public String partyDescription;
    public String userId;
    //public String ProfileImageUrl;

    public Party(){

    }

    public Party(String partyName, String hostName, String county, String partyDescription) {
        this.partyName = partyName;
        this.hostName = hostName;
        this.county = county;
        this.partyDescription = partyDescription;
       // this.ProfileImageUrl = profileImageUrl;
    }

    public String getPartyName() {
        return partyName;
    }

    public void setPartyName(String partyName) {
        this.partyName = partyName;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getPartyDescription() {
        return partyDescription;
    }

    public void setPartyDescription(String partyDescription) {
        this.partyDescription = partyDescription;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

}

