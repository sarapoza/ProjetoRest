package cursorest;

import static io.restassured.RestAssured.given;

import java.util.HashMap;
import java.util.Map;

import org.hamcrest.Matchers;
import org.junit.Test;

import io.restassured.http.ContentType;
import io.restassured.matcher.RestAssuredMatchers;
import io.restassured.path.xml.XmlPath;
import io.restassured.path.xml.XmlPath.CompatibilityMode;

public class AuthTest {

	@Test
	public void deveAcessarSWAPI() {			
			given()
				.log().all()
			.when()
				.get("https://swapi.dev/api/people/1")
			.then()
				.log().all()
				.statusCode(200)
				.body("name", Matchers.is("Luke Skywalker"))
			;
	}
	
	@Test
	public void deveObterClima() {			
			given()
				.log().all()
				.queryParam("q", "Fortaleza,BR")
				.queryParam("appid", "9cc29c8276e420e94794b769cb4be12e")
				.queryParam("units", "metric")
			.when()
				.get("https://api.openweathermap.org/data/2.5/weather")
			.then()
				.log().all()
				.statusCode(200)
				.body("name", Matchers.is("Fortaleza"))
				.body("coord.lon", Matchers.is(-38.5247f))
				.body("main.temp", Matchers.greaterThan(25f))
			;
	}
	
	@Test
	public void naoDeveAcessarSemSenha() {
		given()
			.log().all()
		.when()
			.get("https://restapi.wcaquino.me/basicauth")
		.then()
			.log().all()
			.statusCode(401)
		;
	}
	
	@Test
	public void deveFazerAutenticacaoBasica() {
		given()
			.log().all()
		.when()
			.get("https://admin:senha@restapi.wcaquino.me/basicauth")
		.then()
			.log().all()
			.statusCode(200)
			.body("status", Matchers.is("logado"))
		;
	}
	
	@Test
	public void deveFazerAutenticacaoBasica2() {
		given()
			.log().all()
			.auth().basic("admin", "senha")
		.when()
			.get("https://restapi.wcaquino.me/basicauth")
		.then()
			.log().all()
			.statusCode(200)
			.body("status", Matchers.is("logado"))
		;
	}
	
	@Test
	public void deveFazerAutenticacaoBasicaChallenge() {
		given()
			.log().all()
			.auth().preemptive().basic("admin", "senha")
		.when()
			.get("https://restapi.wcaquino.me/basicauth2")
		.then()
			.log().all()
			.statusCode(200)
			.body("status", Matchers.is("logado"))
		;
	}
	

	@Test
	public void deveFazerAutenticacaoComToken() {
		Map<String, String> login = new HashMap<String, String>();
		login.put("email", "sara@teste");
		login.put("senha", "123456");
		
//		login na api e receber token
		String token = given()
			.log().all()
			.body(login)
			.contentType(ContentType.JSON)
		.when()
			.post("http://barrigarest.wcaquino.me/signin")
		.then()
			.log().all()
			.statusCode(200)
			.extract().path("token")
		;
		
//		obter contas
			given()
				.log().all()
				.header("Authorization", "JWT " + token)
			.when()
				.get("http://barrigarest.wcaquino.me/contas")
			.then()
				.log().all()
				.statusCode(200)
				.body("nome", Matchers.hasItem("Conta de Teste"))
			;
	}
	
	@Test
	public void deveAcessarAplicacaoWeb() {

		String cookie = given()
			.log().all()
			.formParam("email", "sara@teste")
			.formParam("senha", "123456")
			.contentType(ContentType.URLENC.withCharset("UTF-8"))
		.when()
			.post("http://seubarriga.wcaquino.me/logar")
		.then()
			.log().all()
			.statusCode(200)
			.extract().header("set-cookie")
		;

		cookie = cookie.split("=")[1].split(";")[0];
		System.out.println("cookie: " + cookie);
	
//		obter contas
			String body = given()
				.log().all()
				.cookie("connect.sid", cookie)
			.when()
				.get("http://seubarriga.wcaquino.me/contas")
			.then()
				.log().all()
				.statusCode(200)
				.body("html.body.table.tbody.tr[0].td[0]", Matchers.is("Conta de Teste"))
				.extract().body().asString()
			;
			
			System.out.println("-----------------------");
			XmlPath xmlPath = new XmlPath(CompatibilityMode.HTML, body);
			System.out.println(xmlPath.getString("html.body.table.tbody.tr[0].td[0]"));
	}
}

