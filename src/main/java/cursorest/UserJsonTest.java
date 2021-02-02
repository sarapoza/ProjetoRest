package cursorest;

import org.hamcrest.Matchers;
import org.junit.Test;
import static io.restassured.RestAssured.*;

public class UserJsonTest {
	
	@Test
	public void deveVerificarPrimeiroNivel() {
		given()
		.when()
			.get("http://restapi.wcaquino.me/users/1")
		.then()
			.statusCode(200)
			.body("id", Matchers.is(1))
			.body("name", Matchers.containsString("Silva"));			
	}
	
	@Test
	public void deveVerificarPrimeiroNivelOutrasFormas() {
		
	}
}
