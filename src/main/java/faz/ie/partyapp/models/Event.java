package faz.ie.partyapp.models;

public class Event {

    public String eventName;
    public String hostName;
    public String county;
    public String partyDescription;
    public String userId;
    public String ProfileImageUrl;


    public Event()
    {

    }
    public Event (String partyName, String hostName, String county, String partyDescription) {
        this.eventName = partyName;
        this.hostName = hostName;
        this.county = county;
        this.partyDescription = partyDescription;
        //this.ProfileImageUrl = profileImageUrl;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
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

    public String getProfileImageUrl() {
        return ProfileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.ProfileImageUrl = profileImageUrl;
    }

}