package org.appslapp.AppsLappServer.business.pojo.lab;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Lab {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private long labMasterId;

    @ElementCollection
    private List<Long> studentIds;
}
