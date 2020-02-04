package uk.co.huntersix.spring.rest.referencedata;

import org.springframework.stereotype.Service;
import uk.co.huntersix.spring.rest.exception.ExistingPersonException;
import uk.co.huntersix.spring.rest.exception.PersonNotFoundException;
import uk.co.huntersix.spring.rest.model.Person;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class PersonDataService {

    public static final List<Person> PERSON_DATA = new ArrayList<>(Arrays.asList(
        new Person("Mary", "Smith"),
        new Person("Brian", "Archer"),
        new Person("Collin", "Brown"))
    );

    public Person findPerson(String lastName, String firstName) throws PersonNotFoundException {
        List<Person> persons = PERSON_DATA.stream()
                .filter(p -> p.getFirstName().equalsIgnoreCase(firstName)
                        && p.getLastName().equalsIgnoreCase(lastName))
                .collect(Collectors.toList());
        if (Objects.isNull(persons) || persons.isEmpty()) {
            throw new PersonNotFoundException("Person not found.");
        }
        else {
            return persons.get(0);
        }
    }

    public List<Person> findPersons(String lastName) throws PersonNotFoundException {
        List<Person> persons = PERSON_DATA.stream()
                .filter(p -> p.getLastName().equalsIgnoreCase(lastName)
                        && p.getLastName().equalsIgnoreCase(lastName))
                .collect(Collectors.toList());
        if (Objects.isNull(persons) || persons.isEmpty()) {
            throw new PersonNotFoundException("Persons not found.");
        }
        else {
            return persons;
        }
    }

    public boolean addPerson(Person person) throws ExistingPersonException {
        if (PERSON_DATA.contains(person)) {
            throw new ExistingPersonException("Customer already exists.");
        }
        else {
           return PERSON_DATA.add(person);
        }
    }

    public Person updatePerson(Person person, String newFirstName) throws PersonNotFoundException {
        int index = PERSON_DATA.indexOf(person);
        if (index >= 0) {
            person.setFirstName(newFirstName);
            return PERSON_DATA.set(index, person);
        }
        else {
            throw new PersonNotFoundException("Person not found.");
        }
    }
}
