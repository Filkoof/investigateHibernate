package com.filkoof.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Embeddable
public class PersonalInfo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String firstname;
    private String lastname;

    /*
        Alternative way to register converter (HibernateUtil.class: configuration.addAttributeConverter(new BirthdayConverter());)
        @Convert(converter = BirthdayConverter.class)
    */
    //    @Column(name = "birth_date")
    private Birthday birthDate;
}
