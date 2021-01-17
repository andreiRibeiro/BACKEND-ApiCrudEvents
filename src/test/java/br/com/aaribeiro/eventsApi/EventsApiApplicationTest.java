package br.com.aaribeiro.eventsApi;

import br.com.aaribeiro.eventsApi.configuration.SecurityConfiguration;
import br.com.aaribeiro.eventsApi.service.JwtService;
import br.com.aaribeiro.eventsApi.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {EventsApiApplication.class})
@TestPropertySource(locations = "classpath:application.properties")
public class EventsApiApplicationTest {

	@Test
	public void contextLoads() {}

}
