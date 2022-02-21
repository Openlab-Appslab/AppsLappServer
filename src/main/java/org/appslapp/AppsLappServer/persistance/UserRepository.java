package org.appslapp.AppsLappServer.persistance;

import org.appslapp.AppsLappServer.business.pojo.user.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<User> findByVerificationCode(String code);
    List<User> findAllByAuthorityAndEnabled(String authority, boolean enabled);
}
