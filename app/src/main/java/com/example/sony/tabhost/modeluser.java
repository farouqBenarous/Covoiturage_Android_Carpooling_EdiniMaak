

package com.example.sony.tabhost;

public class modeluser {

    private String email;
    private String fullname;
    private String gender;
    private String age;
    private String photo_profile;
    private String Phone_number;
    private String cars;
    private String password;
    private String friendlist;
    private String Requestsended;
    private String requestlist;
    private String Chat;




    public modeluser() { }
    public modeluser (String fullname) {
        this.fullname = fullname;
    }

    public modeluser(String email, String fullname, String gender, String age, String Phone_number, String photo_profile, String cars,String password,
                     String friendlist,String requestlist, String Requestsended,String Chat)
    {
        this.email = email;
        this.fullname = fullname;
        this.gender = gender;
        this.age = age;
        this.Phone_number = Phone_number;
        this.photo_profile = photo_profile;
        this.cars = cars;
        this.password = password;
        this.friendlist = friendlist;
        this.requestlist = requestlist;
        this.Requestsended = Requestsended;
        this.Chat = Chat;

    }
    public modeluser(String email, String fullname, String gender, String age, String Phone_number) {
        this.email = email;
        this.fullname = fullname;
        this.gender = gender;
        this.age = age;
        this.Phone_number = Phone_number;
    }
    public modeluser(String email, String fullname, String gender, String age) {
        this.email = email;
        this.fullname = fullname;
        this.gender = gender;
        this.age = age;
    }

    public modeluser(String email, String fullname, String photo_profile) {
        this.email = email;
        this.fullname = fullname;
        this.photo_profile = photo_profile;
    }


    public String getfullName() {
        return fullname;
    }

    public void setfullName(String fullname) {
        this.fullname = fullname;
    }

    public String getPhoto_profile() {
        return photo_profile;
    }

    public void setPhoto_profile(String photo_profile) {
        this.photo_profile = photo_profile;
    }


    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhone_number() {
        return Phone_number;
    }

    public void setPhone_number(String phone_number) {
        Phone_number = phone_number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCars() {
        return cars;
    }

    public void setCars(String cars) {
        this.cars = cars;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFriendlist() {
        return friendlist;
    }

    public void setFriendlist(String friendlist) {
        this.friendlist = friendlist;
    }

    public String getRequestsended() {
        return Requestsended;
    }

    public void setRequestsended(String requestsended) {
        Requestsended = requestsended;
    }

    public String getRequestlist() {
        return requestlist;
    }

    public void setRequestlist(String requestlist) {
        this.requestlist = requestlist;
    }

    public String getChat() {
        return Chat;
    }

    public void setChat(String chat) {
        Chat = chat;
    }
}
