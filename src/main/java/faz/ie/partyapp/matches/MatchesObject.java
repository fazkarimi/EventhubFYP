package faz.ie.partyapp.matches;

public class MatchesObject {
    private String userId;
    private String FullName;
    private String profileImageUrl;
    public MatchesObject (String userId, String fullName, String profileImageUrl){
        this.userId = userId;
        this.FullName = fullName;
        this.profileImageUrl = profileImageUrl;
    }

    public String getUserId(){
        return userId;
    }
    public void setUserID(String userID){
        this.userId = userId;
    }

    public String getFullName(){
        return FullName;
    }
    public void setFullName(String fullName){
        this.FullName = fullName;
    }

    public String getProfileImageUrl(){
        return profileImageUrl;
    }
    public void setProfileImageUrl(String profileImageUrl){
        this.profileImageUrl = profileImageUrl;
    }
}