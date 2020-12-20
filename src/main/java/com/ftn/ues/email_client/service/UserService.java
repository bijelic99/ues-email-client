package com.ftn.ues.email_client.service;

import com.ftn.ues.email_client.model.User;

public interface UserService {
    public User register(User user) throws Exception;
}
