package org.appslapp.AppsLappServer.business.pojo.users.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;

@Entity(name = "user_data")
@Getter
@Setter
@NoArgsConstructor
public class User extends org.appslapp.AppsLappServer.business.pojo.users.entity.Entity {
    @JsonIgnore
    private String verificationCode;
}
