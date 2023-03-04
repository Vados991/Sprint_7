package courier;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;

public class CourierAssertions {
    @Step("Создание курьера с валидными тестовыми данными")
    public void creatingCourierSuccessfully(ValidatableResponse response) {
        response.assertThat()
                .statusCode(201)
                .body("ok", is(true));
    }

    @Step("Проверка создания курьера с недостаточными тестовыми данными")
    public void creationCourierFailedField(ValidatableResponse response) {
        response.assertThat()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Step("Проверка повторного создания курьера по зарегестрированному логину")
    public void creationCourierTheSameData(ValidatableResponse response) {
        response.assertThat()
                .statusCode(409)
                .body("message" , equalTo("Этот логин уже используется.Попробуйте другой")); // в документации ошибка. Там текст "Этот логин уже используется"
    }

    public void loginCourierSuccessfully(ValidatableResponse response) {
        response.assertThat()
                .statusCode(200)
                .body("id", greaterThan(0));
    }
}