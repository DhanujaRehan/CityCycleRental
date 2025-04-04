package com.example.bicyclerentalapp;

public class User {
    private String firstName;
    private String secondName;
    private String nic;
    private String phoneNumber;
    private String email;
    private byte[] image;


    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getSecondName() { return secondName; }
    public void setSecondName(String secondName) { this.secondName = secondName; }

    public String getNic() { return nic; }
    public void setNic(String nic) { this.nic = nic; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public byte[] getImage() { return image; }
    public void setImage(byte[] image) { this.image = image; }
}
