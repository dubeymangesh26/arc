package com.arcltd.staff.request;

public class RegisterRequest {
    private int Id;
    private String Username;
    private String Email;
    private String MobileNo;
    private String Password;


    public int getId() {
        return Id;
    }

    public void setId(int Customer_Id) {
        this.Id = Id;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String Cust_Name) {
        this.Username = Username;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }

    public String getMobileNo() {
        return MobileNo;
    }

    public void setMobileNo(String MobileNo) {
        this.MobileNo = MobileNo;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String Password) {
        this.Password = Password;
    }
}
