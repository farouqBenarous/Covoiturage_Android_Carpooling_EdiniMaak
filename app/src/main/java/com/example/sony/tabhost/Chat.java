package com.example.sony.tabhost;

public class Chat {
    private String id;
   private  String profilepic;
   private  String USername;
   private  String lastmsg;
  private   String Messages;
  public Chat () {}

public  Chat ( String profilepic ,String USername,String lastmsg) {
    this.profilepic = profilepic;
    this.USername = USername;
    this.lastmsg = lastmsg;
}

    public String getProfilepic() {
        return profilepic;
    }

    public void setProfilepic(String profilepic) {
        this.profilepic = profilepic;
    }

    public String getUSername() {
        return USername;
    }

    public void setUSername(String USername) {
        this.USername = USername;
    }

    public String getLastmsg() {
        return lastmsg;
    }

    public void setLastmsg(String lastmsg) {
        this.lastmsg = lastmsg;
    }

    public String getMessages() {
        return Messages;
    }

    public void setMessages(String messages) {
        Messages = messages;
    }
}
