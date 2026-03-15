package letsexploretanzania.co.tz.letsexploretanzania.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paypal.sdk.PaypalServerSdkClient;
import com.paypal.sdk.controllers.OrdersController;
import com.paypal.sdk.exceptions.ApiException;
import com.paypal.sdk.exceptions.ErrorException;
import com.paypal.sdk.http.response.ApiResponse;
import com.paypal.sdk.models.*;
import letsexploretanzania.co.tz.letsexploretanzania.models.requests.PayPalOrderDetailsDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.CompletionException;

@RestController
@RequestMapping("/api/v1/orders")
public class PayPalController
{
  private final ObjectMapper objectMapper;
  private final PaypalServerSdkClient client;

  public PayPalController(ObjectMapper objectMapper, PaypalServerSdkClient client) {
    this.objectMapper = objectMapper;
    this.client = client;
  }
  @PostMapping("/{orderID}/capture")
  public ResponseEntity<Order> captureOrder(@PathVariable String orderID) {
    try {
      Order response = captureOrders(orderID);
      System.out.println("Captured: " + response);
      return new ResponseEntity<>(response, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  private Order captureOrders(String orderID)
  {
    try
    {
      CaptureOrderInput ordersCaptureInput = new CaptureOrderInput.Builder(orderID, null).build();
      OrdersController ordersController = client.getOrdersController();
      ApiResponse<Order> response = ordersController.captureOrder(ordersCaptureInput);
      return response.getResult();
    }//try
    catch (Exception e)
    {
      Throwable cause = e.getCause();
      if (cause instanceof ErrorException) {
        ErrorException paypalError = (ErrorException) cause;
        System.err.println("PayPal API error: " + paypalError.getMessage());
      } else {
        System.err.println("Unexpected error: " + cause.getMessage());
      }

      throw new RuntimeException("Failed to create PayPal order", cause);
    }//catch
  }

  @PostMapping()
  public ResponseEntity<Order> createOrder(@RequestBody PayPalOrderDetailsDTO orderDetailsDTO)
  {
    try
    {
      Order response = createOrderHelper(orderDetailsDTO);
      return new ResponseEntity<>(response, HttpStatus.OK);
    }
    catch (Exception e)
    {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  public Order createOrderHelper(PayPalOrderDetailsDTO orderDetailsDTO)
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
            ).build()
          )
        ).build()
      ).build();

      OrdersController ordersController = client.getOrdersController();
      ApiResponse<Order> response = ordersController.createOrder(createOrderInput);
      return response.getResult();

    }
    catch (CompletionException e)
    {
      Throwable cause = e.getCause();
      if (cause instanceof ErrorException) {
        ErrorException paypalError = (ErrorException) cause;
        System.err.println("PayPal API error: " + paypalError.getMessage());
      } else {
        System.err.println("Unexpected error: " + cause.getMessage());
      }
      throw new RuntimeException("Failed to create PayPal order", cause);
    }
    catch (IOException e)
    {
      throw new RuntimeException(e);
    }
    catch (ApiException e)
    {
      throw new RuntimeException(e);
    }
  }

}




