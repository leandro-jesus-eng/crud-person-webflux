package br.com.leandro.crud.util;

import br.com.leandro.crud.model.Gender;
import br.com.leandro.crud.model.Person;

import java.sql.Date;

public class PersonFactory {

    public static Person createNewPerson() {
        return Person.builder()
                .firstName("Leandro")
                .lastName("de Jesus")
                .birthday(Date.valueOf("1982-07-04"))
                .gender(Gender.MALE)
                .build();
    }

    public static Person createPersonSaved() {
        Person person = PersonFactory.createNewPerson();
        person.setId(1L);

        return person;
    }
}
