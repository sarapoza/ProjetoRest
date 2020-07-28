package cursorest;

import static io.restassured.RestAssured.*;
import static org.junit.Assert.assertThat;

import org.hamcrest.Matchers;
import org.hamcrest.core.AnyOf;
import org.junit.Assert;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;

public class OlaMundoTeste {

	@Test
	public void testOlaMundo() {
		Response response = request(Method.GET, "http://restapi.wcaquino.me/ola");
		Assert.assertTrue(response.getBody().asString().equals("Ola Mundo!"));
		Assert.assertTrue(response.statusCode() == 200);
		Assert.assertTrue("o status code deve ser 200", response.statusCode() == 200);
		Assert.assertEquals(200, response.statusCode());
		
		ValidatableResponse validacao = response.then();
		validacao.statusCode(200);
	}
	
	@Test
	public void devoConhecerOutrasFromasRestAssured() {
		Response response = request(Method.GET, "http://restapi.wcaquino.me/ola");		
		ValidatableResponse validacao = response.then();
		validacao.statusCode(200);
		
		get("http://restapi.wcaquino.me/ola").then().statusCode(200);
	
		given() //pré-condições
		.when()
			.get("http://restapi.wcaquino.me/ola")
		.then()
		//	.assertThat() //não altera nada, apenas garante que o status code será validado
			.statusCode(200);
	}
	
	@Test
	public void devoConhecerMatchersComHamcrest() {
		Assert.assertThat("Teste", Matchers.is("Teste"));
		
		assertThat("Maria", Matchers.is(Matchers.not("João")));
		assertThat("Maria", Matchers.not("joao"));
		assertThat("Maria", Matchers.anyOf(Matchers.is("Maria"), Matchers.is("Joaquina")));
		assertThat("Maria", Matchers.allOf(Matchers.startsWith("Ma"), Matchers.endsWith("ia"), Matchers.containsString("ri")));
				
	}
}
