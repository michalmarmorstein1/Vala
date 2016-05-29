package com.valapay.vala.common;

/**
 * Created by noam on 2/8/16.
 */
public class UserLoginMessage extends Message {

    private String email;
    private String password;
    private String token;
    private String userId;
    private UserData userData;

    public UserLoginMessage(String email, String password, String token) {
        super();
        this.email = email;
        this.password = password;
        this.token = token;
    }

    public UserLoginMessage(String email, String password) {
        super();
        this.email = email;
        this.password = password;
    }

    public UserLoginMessage() {
        super();
    }

    public String getEmail() {
        return email;
    }

    public UserLoginMessage setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public UserLoginMessage setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public UserData getUserData() {
        return userData;
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
    }

    public class UserData {

        private String firstName;
        private String lastName;
        private String phone;
        private String coutry;
        private int balance;
        private String currency;
        private String profilePicURI;
        private Receiver[] receiverList;

        public UserData(String firstName, String lastName, String phone, String coutry, int balance, String currency, String profilePicURI, Receiver[] receiverList) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.phone = phone;
            this.coutry = coutry;
            this.balance = balance;
            this.currency = currency;
            this.profilePicURI = profilePicURI;
            this.receiverList = receiverList;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getCoutry() {
            return coutry;
        }

        public void setCoutry(String coutry) {
            this.coutry = coutry;
        }

        public int getBalance() {
            return balance;
        }

        public void setBalance(int balance) {
            this.balance = balance;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public String getProfilePicURI() {
            return profilePicURI;
        }

        public void setProfilePicURI(String profilePicURI) {
            this.profilePicURI = profilePicURI;
        }

        public Receiver[] getReceiverList() {
            return receiverList;
        }

        public void setReceiverList(Receiver[] receiverList) {
            this.receiverList = receiverList;
        }
    }

    public class Receiver {
        private String userId;
        private String name;
        private String profilePicURI;

        public Receiver(String userId, String name, String profilePicURI) {
            this.userId = userId;
            this.name = name;
            this.profilePicURI = profilePicURI;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getProfilePicURI() {
            return profilePicURI;
        }

        public void setProfilePicURI(String profilePicURI) {
            this.profilePicURI = profilePicURI;
        }
    }
}