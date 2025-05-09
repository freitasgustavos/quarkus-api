package org.acme.mapper;

import org.acme.dto.PersonDTO;
import org.acme.model.Person;

public class PersonMapper {
    public static PersonDTO toDTO(Person person) {
        if (person == null) {
            return null;
        }
        
        PersonDTO dto = new PersonDTO();
        dto.id = person.id;
        dto.name = person.name;
        dto.email = person.email;
        dto.phone = person.phone;
        dto.birthDate = person.birthDate;
        dto.documentNumber = person.documentNumber;

        return dto;
    }
}