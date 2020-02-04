package uk.co.huntersix.spring.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import uk.co.huntersix.spring.rest.exception.ExistingPersonException;
import uk.co.huntersix.spring.rest.exception.PersonNotFoundException;
import uk.co.huntersix.spring.rest.model.Person;
import uk.co.huntersix.spring.rest.referencedata.PersonDataService;

import java.util.List;

@RestController
public class PersonController {
    private PersonDataService personDataService;

    public PersonController(@Autowired PersonDataService personDataService) {
        this.personDataService = personDataService;
    }

    @GetMapping("/person/{lastName}/{firstName}")
    public Person person(@PathVariable(value="lastName") String lastName,
                                        @PathVariable(value="firstName") String firstName) throws PersonNotFoundException {

        return personDataService.findPerson(lastName, firstName);
    }

    @GetMapping("/persons/{lastName}")
    public List<Person> persons(@PathVariable(value="lastName") String lastName) throws PersonNotFoundException {
        return personDataService.findPersons(lastName);
    }

    @PostMapping("/persons")
    @ResponseStatus(HttpStatus.CREATED)
    public Boolean persons(@RequestBody Person person) throws ExistingPersonException {
        return personDataService.addPerson(person);
    }

    @PatchMapping("/persons/{firstName}")
    @ResponseStatus(HttpStatus.PARTIAL_CONTENT)
    public Person persons(@RequestBody Person person, @PathVariable String firstName) throws PersonNotFoundException {
        return personDataService.updatePerson(person, firstName);
    }
}