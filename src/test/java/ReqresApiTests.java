import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

import static io.restassured.RestAssured.*;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;


public class ReqresApiTests extends TestBase {

    @DisplayName("Успешное создание пользователя")
    @Test
    void successfulCreateUser() {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String todayDate = simpleDateFormat.format(date.getTime());

        String userName = "Anastasiya";
        String userJob = "QA";

        given()
                .body("{\"name\": \"" + userName + "\", \"job\": \"" + userJob + "\"}")
                .contentType(JSON)
                .log().all()
            .when()
                .post("/users")
            .then()
                .log().all()
                .statusCode(201)
                .body("name", is(userName))
                .body("job", is(userJob))
                .body("createdAt", containsString(todayDate));
    }

    @DisplayName("Успешное получение одного пользователя")
    @Test
    void successfulGetSingleUser() {
        int userId = 3;
        String userEmail = "emma.wong@reqres.in";
        String userFirstName = "Emma";
        String userLastName = "Wong";

        given()
                    .log().all()
                .when()
                    .get("/users/" + userId)
                .then()
                    .log().all()
                    .statusCode(200)
                    .body("data.email", is(userEmail))
                    .body("data.first_name", is(userFirstName))
                    .body("data.last_name", is(userLastName))
                    .body("data.avatar", is("https://reqres.in/img/faces/" + userId + "-image.jpg"));

    }

    @DisplayName("Успешное редактирование пользователя")
    @Test
    void successfulUpdateUser() {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String todayDate = simpleDateFormat.format(date.getTime());

        String userJob = "engineer";

        given()
                .body("{\"job\": \"" + userJob +"\"}")
                .contentType(JSON)
                .log().all()
        .when()
                .patch("/users/2")
        .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("job", is(userJob))
                .body("updatedAt", containsString(todayDate));
    }

    @DisplayName("Невозможно зарегистрировать пользователя без пароля")
    @Test
    void unsuccessfulRegistrationUser() {
        given()
                .body("{\"email\": \"sydney@fife\"}")
                .contentType(JSON)
                .log().all()
        .when()
                .post("/register")
        .then()
                .log().status()
                .log().body()
                .statusCode(400)
                .body("error", is("Missing password"));

    }

    @DisplayName("Успешное удаление пользователя")
    @Test
    void successfulDeleteUser() {
        int userId = 2;

        given()
                .log().all()
            .when()
                .delete("/users/" + userId)
            .then()
                .log().all()
                .statusCode(204);
    }
}
