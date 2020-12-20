package com.ftn.ues.email_client.service.implementation;

import com.ftn.ues.email_client.model.User;
import com.ftn.ues.email_client.repository.database.UserRepository;
import com.ftn.ues.email_client.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Override
    public User register(User user) throws Exception {
        //Do the validation
        if (userRepository.findByUsername(user.getUsername()).isPresent()) throw new Exception("Username taken");
        if (user.getPassword().isBlank()) throw new Exception("Password cannot be empty");

        user = userRepository.save(user);

        return user;
    }
}
