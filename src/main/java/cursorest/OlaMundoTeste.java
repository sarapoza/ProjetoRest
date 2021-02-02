package cursorest;

import static io.restassured.RestAssured.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.util.HashMap;
import java.util.Map;

import org.hamcrest.Matchers;
import org.hamcrest.core.IsNot;
import org.junit.Assert;
import org.junit.Test;

import com.sun.istack.NotNull;

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
	
	@Test
	public void devoValidarOBody() {
		given() //pré-condições
		.when()
			.get("http://restapi.wcaquino.me/ola")
		.then()
		//	.assertThat() //não altera nada, apenas garante que o status code será validado
			.statusCode(200)
			.body(Matchers.is("Ola Mundo!"));
	}

	@Test
	public void devoInserirUsuario() {
		given()
			.log().all()
			.contentType("application/json")
			.body("{\"name\": \"Kalel\", \"age\" : \"1\"}")
			.pathParam("entidade", "users")
			.pathParam("userId", 1)
		.when()
			.post("https://restapi.wcaquino.me/{entidade}/{userId}")
		.then()
			.log().all()
			.statusCode(200)
			.body("name", Matchers.is("Kalel"));
	}
	
	@Test
	public void devoInserirUsuarioUsandoMap() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("name", " Usuario Map");
		params.put("age", 25);

		given()
			.log().all()
			.contentType("application/json")
			.pathParam("entidade", "users")
			.body(params)
		.when()
			.post("https://restapi.wcaquino.me/{entidade}")
		.then()
			.log().all()
			.statusCode(201)
			.body("id", Matchers.notNullValue())
			.body("name", Matchers.is("Usuario Map"))
			.body("age", Matchers.is(25))
			;
	}
	
	@Test
	public void devoInserirUsuarioUsandoObjeto() {
		User user = new User("Usuario Obj", 35);

		given()
			.log().all()
			.contentType("application/json")
			.pathParam("entidade", "users")
			.body(user)
		.when()
			.post("https://restapi.wcaquino.me/{entidade}")
		.then()
			.log().all()
			.statusCode(201)
			.body("id", Matchers.notNullValue())
			.body("name", Matchers.is("Usuario Obj"))
			.body("age", Matchers.is(35))
			;
	}
	
	@Test
	public void devoDesserializarObjeto() {
		User user = new User("Usuario desserializado", 35);

		User usuarioInserido = given()
			.log().all()
			.contentType("application/json")
			.pathParam("entidade", "users")
			.body(user)
		.when()
			.post("https://restapi.wcaquino.me/{entidade}")
		.then()
			.log().all()
			.statusCode(201)
			.extract().body().as(User.class)
			;
		System.out.println(usuarioInserido);
		Assert.assertThat(usuarioInserido.getId(), Matchers.notNullValue());
		Assert.assertEquals("Usuario desserializado", usuarioInserido.getName());
		Assert.assertThat(usuarioInserido.getAge(), Matchers.is(35));
	}
	
	@Test
	public void devoCustomizarURL() {
		given()
			.log().all()
			.contentType("application/json")
			.body("{\"name\": \"Kalel\", \"age\" : \"1\"}")
			.pathParam("entidade", "users")
			.pathParam("userId", 1)
		.when()
			.put("https://restapi.wcaquino.me/{entidade}/{userId}")
		.then()
			.log().all()
			.statusCode(200)
			.body("name", Matchers.is("Kalel"));
	}
	
	@Test
	public void deveRemoverUsuario() {
		given()
			.log().all()
		.when()
			.delete("https://restapi.wcaquino.me/users/1")
		.then()
			.log().all()
			.statusCode(204)
		;
	}
	
	@Test
	public void naoDeveRemoverUsuario() {
		given()
			.log().all()
		.when()
			.delete("https://restapi.wcaquino.me/users/1000")
		.then()
			.log().all()
			.statusCode(400)
			.body("error", Matchers.is("Registro inexistente"))
		;
	}
}
