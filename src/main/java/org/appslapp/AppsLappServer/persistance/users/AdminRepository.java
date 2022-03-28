package org.appslapp.AppsLappServer.persistance.users;

import org.appslapp.AppsLappServer.business.pojo.users.admin.Admin;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends EntityRepository<Admin> {
}
