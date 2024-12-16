package ch.ivyteam.test.rest.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import jakarta.inject.Singleton;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Link;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import org.apache.commons.lang3.StringUtils;

/**
 * Simple RESTful service. The REST interface is defined by the JAX-RS annotations on the methods and it's path.<br/>
 * 
 * @since 6.1.1
 */
@Singleton
@Path("persons")
public class PersonService {

	private List<Person> persons = new ArrayList<>();
	
	public PersonService()
	{
		addNewPerson("Bruno", "Bütler");
		addNewPerson("Reto", "Weiss");
		addNewPerson("Renato", "Stalder");
		addNewPerson("Reguel", "Wermelinger");
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getPersons()
	{
		return Response.status(Status.OK)
				.entity(persons)
				.build();
	}
	
	private Person addNewPerson(String firstname, String lastname) 
	{
		Person person = new Person();
		person.setFirstname(firstname);
		person.setLastname(lastname);
		person.setId(persons.size());
		persons.add(person);
		return person;
	}
}
