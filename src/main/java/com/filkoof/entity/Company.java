package com.filkoof.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "users")
@EqualsAndHashCode(exclude = "users") //In case of cyclical looping and StackOverFlow error
@Builder
@Entity
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @OneToMany(mappedBy = "company")
//    @JoinColumn(name = "company_id")
    private List<User> users;
}
