import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import order.OrderClient;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.notNullValue;

public class GetOrderListTest {

    @Test
    @DisplayName("Получение списка заказов")
    @Description("Проверка успешного получения списка заказов с проверкой кода состояния")
    public void getOrderListIsOk() {
        OrderClient orderClient = new OrderClient();
        ValidatableResponse responseOrderList = orderClient.getOrderList();
        responseOrderList.assertThat()
                .statusCode(200)
                .body("orders", notNullValue());
    }
}