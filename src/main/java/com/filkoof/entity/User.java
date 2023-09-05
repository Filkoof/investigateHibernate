package com.filkoof.entity;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "username")
@ToString(exclude = {"company", "profile", "userChats"})
@Builder
@Table(name = "users", schema = "public")
@TypeDef(name = "vladmihalceaJsonb", typeClass = JsonBinaryType.class)
@Entity
//@Access(AccessType.PROPERTY) getters and setters access for Hibernate (AccessType.FIELD default, access with reflection API)
public class User implements Comparable<User>, BaseEntity<Long> {

    /*
        @GeneratedValue(generator = "user_gen", strategy = GenerationType.TABLE)
        @TableGenerator(name = "user_gen", table = "all_sequence",
            pkColumnName = "table_name", valueColumnName = "pk_value",
            allocationSize = 1)
    */
    /*
        @GeneratedValue(generator = "user_gen", strategy = GenerationType.SEQUENCE)
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
    private Company company;

    @OneToOne(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            optional = false
    )
    private Profile profile;

    @Builder.Default
    @OneToMany(mappedBy = "user")
    private List<UserChat> userChats = new ArrayList<>();

    @Override
    public int compareTo(User o) {
        return username.compareTo(o.username);
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
