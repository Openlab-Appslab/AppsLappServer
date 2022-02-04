package org.appslapp.AppsLappServer.business.user;

import org.appslapp.AppsLappServer.persistance.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(@Autowired UserRepository repository) {
        this.userRepository = repository;
    }


    public long save(User user) {
        return userRepository.save(user).getId();
    }

    public Optional<User> getUser(long id) {
        return userRepository.findById(id);
    }
}
