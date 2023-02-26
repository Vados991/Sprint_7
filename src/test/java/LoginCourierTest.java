import courier.*;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;;

import static org.hamcrest.CoreMatchers.equalTo;

public class LoginCourierTest {
    private CourierGenerator courierGenerator = new CourierGenerator();
    private Credentials credentials;
    private CourierClient courierClient;
    private Courier courier;
    CourierAssertions courierAssertions;
    int idCourier;

    @Before
    @Step("Предусловия для тестов входа в систему с созданием курьера")
    public void setUp() {
        courierClient = new CourierClient();
        courier = courierGenerator.getCourierRandom();
        courierClient.createCourier(courier);
        credentials = Credentials.from(courier);
        courierAssertions = new CourierAssertions();
    }

    @Test
    @DisplayName("Вход курьером с действительными УЗ")
    @Description("Проверка кода состояния на непустом id")
    public void courierCanSuccessfullyLogin() {
        ValidatableResponse responseLoginCourier = courierClient.loginCourier(credentials);
        courierAssertions.LoginCourierSuccessfully(responseLoginCourier);
        idCourier = responseLoginCourier.extract().path("id");
    }

    @Test
    @DisplayName("Вход без заполнения логина")
    @Description("Проверка авторизации без ввода логина. Проверка кода состояния")
    public void courierLoginUnsuccessfullyWithoutLogin() {
        Credentials credentialsWithoutLogin = new Credentials("", courier.getPassword()); // c null тесты виснут
        ValidatableResponse responseLoginErrorMessage = courierClient.loginCourier(credentialsWithoutLogin).statusCode(400);
        responseLoginErrorMessage.assertThat().body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Вход курьера без заполнения пароля")
    @Description("Проверка авторизации без ввода пароля. Проверка кода состояния")
    public void courierLoginUnsuccessfullyWithoutPassword() {
        Credentials credentialsWithoutLogin = new Credentials(courier.getLogin(), "");
        ValidatableResponse responsePasswordErrorMessage = courierClient.loginCourier(credentialsWithoutLogin).statusCode(400);
        responsePasswordErrorMessage.assertThat().body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Вход без заполнения логина и пароля")
    @Description("Проверка авторизации без логина и пароля. Проверка кода состояния")
    public void courierLoginWithoutLoginAndPassword() {
        Credentials credentialsWithoutLoginAndPassword = new Credentials("", "");
        ValidatableResponse responseWithoutLoginAndPasswordMessage = courierClient.loginCourier(credentialsWithoutLoginAndPassword).statusCode(400);
        responseWithoutLoginAndPasswordMessage.assertThat().body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Вход курьером с несуществующим логином")
    @Description("Проверка авторизации с несуществующим логином курьера. Проверка кода состояния")
    public void courierLoginWithNotExistingLogin() {
        Credentials credentialsWithNotExistingLogin = new Credentials(RandomStringUtils.randomAlphanumeric(6), courier.getPassword());
        ValidatableResponse responseWithWithNotExistingLoginMessage = courierClient.loginCourier(credentialsWithNotExistingLogin).statusCode(404);
        responseWithWithNotExistingLoginMessage.assertThat().body("message", equalTo("Учетная запись не найдена"));
    }
    @After
    @Step("Удаление созданного курьера")
    public void deleteCourier() {
        if (idCourier != 0) {
            courierClient.deleteCourier(idCourier);
        }
    }
}