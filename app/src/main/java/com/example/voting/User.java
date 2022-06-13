package com.example.voting;
public class User {

    public String email;
    public boolean isVoted;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isVoted() {
        return isVoted;
    }

    public void setVoted(boolean voted) {
        isVoted = voted;
    }

    public User(String email, boolean isVoted) {
        this.email = email;
        this.isVoted = isVoted;
    }

}