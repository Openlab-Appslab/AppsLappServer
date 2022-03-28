package org.appslapp.AppsLappServer.persistance.users;

import org.appslapp.AppsLappServer.business.pojo.users.entity.Entity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;

@NoRepositoryBean
interface EntityRepository<T extends Entity> extends CrudRepository<T, Long> {
    Optional<T> findByUsername(String username);
}
