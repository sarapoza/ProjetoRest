package cursorest;

import static org.hamcrest.Matchers.*;

import org.junit.Assert;
import org.junit.Test;

import io.restassured.http.ContentType;

import static io.restassured.RestAssured.*;

public class UserXmlTest {

	@Test
	public void devoCadastrarUsuarioUsandoXML() {
		User user = new User("Usuario XML", 40);
		given()
			.log().all()
			.contentType(ContentType.XML)
			.body(user)
		.when()
			.post("http://restapi.wcaquino.me/usersXML")
		.then()
			.statusCode(201)
			//lembre do rootPath 
			.rootPath("user")
			.body("name", is("Usuario XML"))
			.body("@id", is(notNullValue()))
			.body("age", is("40"))
			;
	}

	@Test
	public void devoDesserializarXmlAoCadastrarUsuario() {
		User user = new User("Usuario XML", 40);
		
		User usuarioInserido = given()
			.log().all()
			.contentType(ContentType.XML)
			.body(user)
		.when()
			.post("http://restapi.wcaquino.me/usersXML")
		.then()
			.statusCode(201)
			.extract().body().as(User.class)
			;
		Assert.assertThat(usuarioInserido.getId(), is(notNullValue()));
		Assert.assertThat(usuarioInserido.getName(), is("Usuario XML"));
	}
	
	@Test
	public void devoTrabalharComXml() {
		given()
		.when()
			.get("http://restapi.wcaquino.me/usersXML/3")
		.then()
			.statusCode(200)
			.rootPath("user")
			.body("name", is("Ana Julia"))
			.body("@id", is("3"))
			.body("filhos.name.size()", is(2))
			.body("filhos.name", hasItems("Luizinho","Zezinho"))
			;
	}
}
