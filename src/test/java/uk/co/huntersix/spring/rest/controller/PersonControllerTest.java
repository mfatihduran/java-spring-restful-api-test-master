package uk.co.huntersix.spring.rest.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import uk.co.huntersix.spring.rest.exception.ExistingPersonException;
import uk.co.huntersix.spring.rest.exception.PersonNotFoundException;
import uk.co.huntersix.spring.rest.model.Person;
import uk.co.huntersix.spring.rest.referencedata.PersonDataService;
import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@WebMvcTest(PersonController.class)
public class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonDataService personDataService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void shouldReturnPersonFromService() throws Exception, PersonNotFoundException {
        Person person = new Person("Mary", "Smith");
        when(personDataService.findPerson(any(), any())).thenReturn(person);
        this.mockMvc.perform(get("/person/smith/mary"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("id").exists())
            .andExpect(jsonPath("firstName").value("Mary"))
            .andExpect(jsonPath("lastName").value("Smith"));
    }

    @Test
    public void shouldReturnNoContentWhenNoPersonFound() throws Exception, PersonNotFoundException {
        when(personDataService.findPerson(any(), any())).thenThrow(new PersonNotFoundException("Not found"));
        this.mockMvc.perform(get("/person/smith/mary"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldReturnPersonsByHavingMultipleMatches() throws Exception, PersonNotFoundException {
        List<Person> persons = Arrays.asList(new Person("Mary", "Smith"), new Person("Harry", "Smith"));
        when(personDataService.findPersons(eq("smith"))).thenReturn(persons);
        this.mockMvc.perform(get("/persons/smith"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]id").exists())
                .andExpect(jsonPath("$[0].firstName").value("Mary"))
                .andExpect(jsonPath("$[0].lastName").value("Smith"))
                .andExpect(jsonPath("$[1]id").exists())
                .andExpect(jsonPath("$[1].firstName").value("Harry"))
                .andExpect(jsonPath("$[1].lastName").value("Smith"));
    }

    @Test
    public void shouldReturnPersonByHavingSingleMatch() throws Exception, PersonNotFoundException {
        List<Person> persons = Arrays.asList(new Person("Bob", "Johns"));
        when(personDataService.findPersons(eq("johns"))).thenReturn(persons);
        this.mockMvc.perform(get("/persons/johns"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("Bob"))
                .andExpect(jsonPath("$[0].lastName").value("Johns"));
    }

    @Test
    public void shouldReturnNoContentFoundByHavingNoMatch() throws Exception, PersonNotFoundException {
        when(personDataService.findPersons(anyString())).thenThrow(new PersonNotFoundException("Not found"));
        this.mockMvc.perform(get("/persons/smith"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string("Not found"));
    }

    @Test
    public void shouldReturnOKWhenNewPersonRecorded() throws ExistingPersonException, Exception {
        Person person = new Person("Martin", "Fowler");
        when(personDataService.addPerson(any(Person.class))).thenReturn(true);
        mockMvc.perform(post("/persons")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(person))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

    }

    @Test
    public void shouldReturnConflictWhenPostSentIfPersonExists() throws ExistingPersonException, Exception {
        Person person = new Person("Martin", "Fowler");
        when(personDataService.addPerson(any(Person.class))).thenThrow(new ExistingPersonException("Already exists"));

        this.mockMvc.perform(post("/persons")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("{}")
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isConflict())
                .andExpect(content().string("Already exists"));

    }

    @Test
    public void shouldUpdateFirstNameIfPersonExists() throws Exception, PersonNotFoundException {
        Person person = new Person("Martin", "Fowler");
        when(personDataService.updatePerson(any(Person.class), eq("Matt"))).thenReturn(new Person("Matt", "Fowler"));

        this.mockMvc.perform(patch("/persons/Matt")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(person)))
                .andExpect(status().isPartialContent())
                .andExpect(jsonPath("$.firstName").value("Matt"))
                .andExpect(jsonPath("$.lastName").value("Fowler"));

    }

    @Test
    public void shouldNotUpdateFirstNameIfPersonNotExist() throws PersonNotFoundException, Exception {
        when(personDataService.updatePerson(any(Person.class), anyString())).thenThrow(new PersonNotFoundException("Not found"));

        this.mockMvc.perform(patch("/persons/Matt")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("{}"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Not found"));

    }
}