package faz.ie.partyapp.matches;

public class MatchesObject
{
   // private String userId;
    private String FullName;
    private String profileImageUrl;



    public MatchesObject(String fullName,String profileImageUrl) {

       // this.userId = userid;
        this.FullName = fullName;
        this.profileImageUrl = profileImageUrl;
    }

   /* public String getUserid() {
        return userId;
    }
    public void setUserid(String userid) {
        userId = userid;
    }*/

    public String getFullName() {
        return FullName;
    }
    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }
    public void setProfileImageUrl(String profileImageUrl) {
        profileImageUrl = profileImageUrl;
    }
}
