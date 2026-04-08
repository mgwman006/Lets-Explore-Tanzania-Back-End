package letsexploretanzania.co.tz.letsexploretanzania.controller;

import com.paypal.sdk.models.*;
import jakarta.validation.Valid;
import letsexploretanzania.co.tz.letsexploretanzania.models.dto.requests.PayPalOrderDetailsDTO;
import letsexploretanzania.co.tz.letsexploretanzania.service.PayPalService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders")
public class PayPalController
{
  private final PayPalService payPalService;

  public PayPalController(PayPalService payPalService)
  {
    this.payPalService = payPalService;
  }


  @PostMapping("/{orderID}/capture")
  public ResponseEntity<Order> captureOrder(@PathVariable String orderID)
  {
    try
    {
      Order response = payPalService.captureOrders(orderID);
      return new ResponseEntity<>(response, HttpStatus.OK);
    }
    catch (Exception exception)
    {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PostMapping
  public ResponseEntity<Order> createOrder(@Valid @RequestBody PayPalOrderDetailsDTO orderDetailsDTO)
  {
    try
    {
      Order response = payPalService.createOrder(orderDetailsDTO);
      return new ResponseEntity<>(response, HttpStatus.OK);
    }
    catch (Exception e)
    {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

}




