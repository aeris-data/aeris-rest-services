package fr.aeris.rest.services;

import static org.junit.Assert.assertEquals;

import javax.inject.Inject;
import javax.ws.rs.client.WebTarget;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:test1.xml")
public class OrcidServiceTest {
  @Inject
  public WebTarget target;

  @Test
  public void hello() {
	  String string = target.path("/orcid/isalive").request().get(String.class);
	  assertEquals("YES".toLowerCase(), target.path("/orcid/isalive").request().get(String.class).toLowerCase());
	  
	  string = target.path("/orcid/name/0000-0002-9252-4391").request().get(String.class);
	  assertEquals("Marjorie Salvetat".toLowerCase(), string.toLowerCase());
	  
  }
}