package letsexploretanzania.co.tz.letsexploretanzania.service;

import com.paypal.sdk.PaypalServerSdkClient;
import com.paypal.sdk.controllers.OrdersController;
import com.paypal.sdk.exceptions.ErrorException;
import com.paypal.sdk.http.response.ApiResponse;
import com.paypal.sdk.models.*;
import letsexploretanzania.co.tz.letsexploretanzania.common.enums.BookingStatus;
import letsexploretanzania.co.tz.letsexploretanzania.common.utils.Result;
import letsexploretanzania.co.tz.letsexploretanzania.models.dto.requests.PayPalOrderDetailsDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.Collections;

@Service
public class PayPalService
{
  private static final Logger log = LoggerFactory.getLogger(PayPalService.class);
  private final PaypalServerSdkClient client;
  private final TourBookingService tourBookingService;

  public PayPalService(PaypalServerSdkClient client, TourBookingService tourBookingService)
  {
    this.client = client;
    this.tourBookingService = tourBookingService;
  }

  public Order captureOrders(String orderID)
  {
    try
    {
      CaptureOrderInput ordersCaptureInput = new CaptureOrderInput.Builder(orderID, null).build();
      OrdersController ordersController = client.getOrdersController();
      ApiResponse<Order> response = ordersController.captureOrder(ordersCaptureInput);
      Order responseOrder = response.getResult();
      PurchaseUnit purchaseUnit = responseOrder.getPurchaseUnits().get(0);
      Result<String> result = tourBookingService.updateTourPaymentStatus(purchaseUnit.getReferenceId(), BookingStatus.CONFIRMED);
      if (!result.isSuccess()) {
        log.error("Failed to update tour payment status with id {}. Reason: {}",
          purchaseUnit.getReferenceId(),
          result.getMessage());
      }

      return responseOrder;
    }//try
    catch (Exception e)
    {
      Throwable cause = e.getCause();
      if (cause instanceof ErrorException paypalError) {
        log.error("PayPal API error: {}" ,paypalError.getMessage());
      } else {
        log.error("Unexpected error: {}",cause.getMessage());
      }

      throw new RuntimeException("Failed to create PayPal order", cause);
    }//catch
  }

  public Order createOrder(PayPalOrderDetailsDTO orderDetailsDTO)
  {
    try
    {
      CreateOrderInput createOrderInput = new CreateOrderInput.Builder(
        null,
        new OrderRequest.Builder(
          CheckoutPaymentIntent.CAPTURE,
          Collections.singletonList(
            new PurchaseUnitRequest.Builder(
              new AmountWithBreakdown.Builder(orderDetailsDTO.currency(), orderDetailsDTO.amount()).build()
            ).referenceId(orderDetailsDTO.referenceNumber()).build()
          )
        ).build()
      ).build();

      OrdersController ordersController = client.getOrdersController();
      ApiResponse<Order> response = ordersController.createOrder(createOrderInput);
      return response.getResult();

    }
    catch (Exception e)
    {
      Throwable cause = e.getCause();
      if (cause instanceof ErrorException paypalError) {
        log.error("Create Order PayPal API error: {}" ,paypalError.getMessage());
      } else {
        log.error("Create Order Unexpected error: {}",cause.getMessage());
      }
      throw new RuntimeException("Failed to create PayPal order", cause);
    }
  }
}
