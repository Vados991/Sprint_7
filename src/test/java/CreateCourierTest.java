
import courier.*;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CreateCourierTest {
    private CourierGenerator courierGenerator = new CourierGenerator();
    private CourierClient courierClient;
    private Courier courier;
    private CourierAssertions courierAssertions;
    int idCourier;

    @Before
    @Step("Предварительные условия для создания тестов")
    public void setUp() {
        courierClient = new CourierClient();
        courier = courierGenerator.getCourierRandom();
        courierAssertions = new CourierAssertions();
    }

    @Test
    @DisplayName("Создание нового курьера")
    @Description("Тест на успешное создание с проверкой кода состояния")
    public void courierCanBeCreatedWith201CodeMessageOk() {
        ValidatableResponse responseCreateCourier = courierClient.createCourier(courier);
        courierAssertions.creatingCourierSuccessfully(responseCreateCourier);
        Credentials credentials = Credentials.from(courier);
        ValidatableResponse responseLoginCourier = courierClient.loginCourier(credentials);
        idCourier = responseLoginCourier.extract().path("id");
    }

    @Test
    @DisplayName("Создание курьера без логина")
    @Description("Проверка создания нового курьера без логина. Проверка кода состояния")
    public void courierCanNotBeCreatedWithoutLoginField() {
        courier.setLogin(null);
        ValidatableResponse responseNullLogin = courierClient.createCourier(courier);
        courierAssertions.creationCourierFailedField(responseNullLogin);
    }

    @Test
    @DisplayName("Создание курьера без пароля")
    @Description("Проверка создания курьера без пароля. Проверка кода состояния")
    public void courierCanNotBeCreatedWithoutPasswordField() {
        courier.setPassword(null);
        ValidatableResponse responseNullPassword = courierClient.createCourier(courier);
        courierAssertions.creationCourierFailedField(responseNullPassword);
    }

    @Test
    @DisplayName("Создание курьера без логина и пароля")
    @Description("Проверка создания нового курьера без логина и пароля. Проверка кода состояния")
    public void courierCanNotBeCreatedWithoutLoginAndPasswordFields() {
        courier.setLogin(null);
        courier.setPassword(null);
        ValidatableResponse responseNullFields = courierClient.createCourier(courier);
        courierAssertions.creationCourierFailedField(responseNullFields);
    }

    @Test
    @DisplayName("Создание курьера с существующими данными")
    @Description("Проверка создания курьера с уже существующими данными. Проверка кода состояния")
    public void courierCanNotBeCreatedWithTheSameData() {
        courierClient.createCourier(courier);
        ValidatableResponse responseCreateCourier = courierClient.createCourier(courier);
        courierAssertions.creationCourierTheSameData(responseCreateCourier);
    }

    @After
    @Step("Удаление курьера")
    public void deleteCourier() {
        if (idCourier != 0) {
            courierClient.deleteCourier(idCourier);
        }
    }
}