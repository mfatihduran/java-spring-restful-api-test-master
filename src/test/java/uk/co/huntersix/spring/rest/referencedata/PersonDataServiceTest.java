package uk.co.huntersix.spring.rest.referencedata;

import org.junit.Test;
import uk.co.huntersix.spring.rest.exception.ExistingPersonException;
import uk.co.huntersix.spring.rest.exception.PersonNotFoundException;
import uk.co.huntersix.spring.rest.model.Person;

import java.util.List;

import static org.hamcrest.Matchers.in;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static uk.co.huntersix.spring.rest.referencedata.PersonDataService.PERSON_DATA;

public class PersonDataServiceTest {

    private PersonDataService target = new PersonDataService();

    @Test
    public void shouldFindExistingPerson() throws PersonNotFoundException {
        Person person = target.findPerson("Smith", "Mary");
        assertTrue(PERSON_DATA.contains(person));
    }

    @Test(expected = PersonNotFoundException.class)
    public void shouldThrowExceptionWhenNoPersonFound() throws PersonNotFoundException {
        target.findPerson("Someone", "Someone");
    }

    @Test
    public void shouldFindExistingPersons() throws PersonNotFoundException {
        List<Person> persons = target.findPersons("Smith");
        assertTrue(persons.size() > 0);
        assertThat(persons.get(0), in(PERSON_DATA));
    }

    @Test(expected = PersonNotFoundException.class)
    public void shouldThrowExceptionWhenNoPersonsFound() throws PersonNotFoundException {
        target.findPersons("Someone");
    }

    @Test
    public void shouldAddIfPersonNotExist() throws ExistingPersonException {
        Person person = new Person("John", "Wick");
        target.addPerson(person);
        assertTrue(PERSON_DATA.contains(person));
    }

    @Test(expected = ExistingPersonException.class)
    public void shouldThrowExceptionWhenAddingIfPersonExists() throws ExistingPersonException {
        Person person = PERSON_DATA.get(0);
        target.addPerson(person);
    }

    @Test
    public void shouldUpdateExistingPerson() throws PersonNotFoundException {
        Person person = PERSON_DATA.get(1);
        Person personUpdated = target.updatePerson(person, "Robert");
        assertTrue(PERSON_DATA.contains(personUpdated));
    }

    @Test(expected = PersonNotFoundException.class)
    public void shouldThrowExceptionWhenUpdatingIfPersonNotExists() throws PersonNotFoundException {
        Person person = new Person("Tom", "Kuhn");
        target.updatePerson(person, "Tommy");
    }

}