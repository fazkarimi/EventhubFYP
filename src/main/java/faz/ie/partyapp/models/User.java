package faz.ie.partyapp.models;

public class User {

    private String FullName;
    private String Gender;
    private String Age;
    private String PhoneNumber;
    private String EmailAddress;
    private  String userId;
    private String profileImageUrl;


    public User()
    {

    }

    public User(String userId,String fullName, String gender, String age, String profileImageUrl) {
        FullName = fullName;
        Gender = gender;
        Age = age;
        this.userId = userId;
        this.profileImageUrl = profileImageUrl;
    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getAge() {
        return Age;
    }

    public void setAge(String age) {
        Age = age;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getEmailAddress() {
        return EmailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        EmailAddress = emailAddress;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }
}
