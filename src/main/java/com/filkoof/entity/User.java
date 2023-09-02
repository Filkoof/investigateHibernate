package com.filkoof.entity;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "username")
@ToString(exclude = "company")
@Builder
@Entity
@Table(name = "users", schema = "public")
@TypeDef(name = "vladmihalceaJsonb", typeClass = JsonBinaryType.class)
//@Access(AccessType.PROPERTY) getters and setters access for Hibernate (AccessType.FIELD default, access with reflection API)
public class User {

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

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "company_id")
    private Company company;
}

    /*
        @Temporal(TemporalType.TIMESTAMP)
        private LocalDateTime localDateTime;

        @Temporal(TemporalType.DATE)
        private LocalDate localDate;

        @Temporal(TemporalType.TIME)
        private LocalTime localTime;
    */
