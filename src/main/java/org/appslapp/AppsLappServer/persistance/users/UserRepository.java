package org.appslapp.AppsLappServer.persistance.users;

import org.appslapp.AppsLappServer.business.pojo.users.user.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends EntityRepository<User> {
    Optional<User> findByEmail(String email);
    Optional<User> findByVerificationCode(String code);
    List<User> findAllByAuthorityAndEnabled(String authority, boolean enabled);
}
