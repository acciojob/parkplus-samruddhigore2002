package com.driver.services;


import com.driver.model.User;

public interface UserService {

	void deleteUser(Integer userId);
	User updatePassword(Integer userId, String password);
    public void register(String name, String phoneNumber, String password);
}
