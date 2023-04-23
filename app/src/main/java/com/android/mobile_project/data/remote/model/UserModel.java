package com.android.mobile_project.data.remote.model;



public class UserModel extends BaseModel {

    private Long userId;
    private String userName;

    public UserModel() {
    }

    public UserModel(Long userId, String userName) {
        this.userId = userId;
        this.userName = userName;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
