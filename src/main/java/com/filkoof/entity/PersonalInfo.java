package com.filkoof.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Embeddable
public class PersonalInfo {

    private String firstname;
    private String lastname;

    /*
        Alternative way to register converter (HibernateUtil.class: configuration.addAttributeConverter(new BirthdayConverter());)
        @Convert(converter = BirthdayConverter.class)
    */
    //    @Column(name = "birth_date")
    private Birthday birthDate;
}
