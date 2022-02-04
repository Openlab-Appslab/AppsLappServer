package org.appslapp.AppsLappServer.persistance;

import org.appslapp.AppsLappServer.business.user.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
}
