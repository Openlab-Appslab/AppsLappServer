package org.appslapp.AppsLappServer.business.security.user;

import org.appslapp.AppsLappServer.persistance.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    public UserService(@Autowired UserRepository repository, @Autowired PasswordEncoder encoder) {
        this.userRepository = repository;
        this.encoder = encoder;
    }


    public long save(User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        return userRepository.save(user).getId();
    }

    public Optional<User> getUserById(long id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserByName(String username) {
        return userRepository.findByUsername(username);
    }
}
