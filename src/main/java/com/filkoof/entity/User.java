package com.filkoof.entity;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "username")
@ToString(exclude = {"company", "userChats", "payments"})
@Table(name = "users", schema = "public")
@TypeDef(name = "vladmihalceaJsonb", typeClass = JsonBinaryType.class)
@Builder
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
//@DiscriminatorColumn(name = "type") for SINGLE_TABLE
//@Access(AccessType.PROPERTY) getters and setters access for Hibernate (AccessType.FIELD default, access with reflection API)
public class User implements Comparable<User>, BaseEntity<Long> {

    /*
        @GeneratedValue(generator = "user_gen", strategy = GenerationType.TABLE)
        @TableGenerator(name = "user_gen", table = "all_sequence",
            pkColumnName = "table_name", valueColumnName = "pk_value",
            allocationSize = 1)
    */
    /*
        @GeneratedValue(generator = "user_gen", strategy = GenerationType.SEQUENCE) also need for TABLE_PER_CLASS
        @SequenceGenerator(name = "user_gen", sequenceName = "users_id_seq", allocationSize = 1)
    */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //    @EmbeddedId //- in case embeddable key
    @AttributeOverride(name = "birthDate", column = @Column(name = "birth_date"))
    private PersonalInfo personalInfo;

    @Column(unique = true)
    private String username;


    @Type(type = "vladmihalceaJsonb")
    private String info;

    //@Transient don't need to serialize this field
    @Enumerated(EnumType.STRING) //EnumType.ORDINAL for mapping enum to int
    private Role role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    @Fetch(FetchMode.JOIN)
    private Company company;

//    @OneToOne(
//            mappedBy = "user",
//            cascade = CascadeType.ALL,
//            fetch = FetchType.LAZY
//    )
//    private Profile profile;

    @Builder.Default
    @OneToMany(mappedBy = "user")
    private Set<UserChat> userChats = new HashSet<>();

    @Builder.Default
//    @BatchSize(size = 3)
    @Fetch(FetchMode.SUBSELECT)
    @OneToMany(mappedBy = "receiver")
    private List<Payment> payments = new ArrayList<>();

    @Override
    public int compareTo(User o) {
        return username.compareTo(o.username);
    }

    public String fullName() {
        return getPersonalInfo().getFirstname() + " " + getPersonalInfo().getLastname();
    }
}

    /*
        @Temporal(TemporalType.TIMESTAMP)
        private LocalDateTime localDateTime;

        @Temporal(TemporalType.DATE)
        private LocalDate localDate;

        @Temporal(TemporalType.TIME)
        private LocalTime localTime;
    */
