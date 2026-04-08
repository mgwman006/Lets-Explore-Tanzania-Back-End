package letsexploretanzania.co.tz.letsexploretanzania.constants;

public class Constants
{
  public static final String BUCKET_NAME = "letsexploretanzania-s3";
  public static final String SUCCESS = "success";
  public static final String BOOKING_MESSAGE = """
                                                Dear %s,
                                                
                                                Thank you for booking with Lets Explore Tanzania!
                                                
                                                Your booking has been successfully received.
                                                
                                                Booking Details:
                                                ----------------------------------------
                                                Reference Number : %s
                                                Tour             : %s
                                                Date             : %s
                                                People           : %d
                                                Price per Person : %s
                                                Total Price      : %s
                                                Status           : %s
                                                ----------------------------------------
                                                
                                                Next Steps:
                                                - Our team will contact you shortly for payment.
                                                - Please keep your reference number for future communication.
                                                
                                                If you have any questions, feel free to contact us.
                                                
                                                Best regards,
                                                Lets Explore Tanzania Team
                                                """;
  public static final String BOOKING_MESSAGE_BODY_HTML = """
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <style>
    body { font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 20px; }
    .container { background: #ffffff; padding: 20px; border-radius: 8px; }
    .header { font-size: 20px; font-weight: bold; margin-bottom: 10px; }
    .section { margin-top: 20px; }
    .label { font-weight: bold; }
    .footer { margin-top: 30px; font-size: 12px; color: #777; }
  </style>
</head>
<body>
  <div class="container">
    <div class="header">Booking Confirmation</div>

    <p>Dear %s,</p>

    <p>Thank you for booking with <strong>Lets Explore Tanzania</strong>!</p>

    <div class="section">
      <p class="label">Booking Details:</p>
      <p>Reference Number: %s</p>
      <p>Tour: %s</p>
      <p>Date: %s</p>
      <p>Number of People: %d</p>
      <p>Price per Person : %s</p>
      <p>Total Price: %s</p>
      <p>Status: %s</p>
    </div>

    <div class="section">
      <p>We will contact you shortly regarding payment and further arrangements.</p>
    </div>

    <div class="footer">
      <p>Lets Explore Tanzania Team</p>
    </div>
  </div>
</body>
</html>
""";

  public static String BOOKING_EMAIL_SUBJECT = "Your Booking Details";

  private Constants(){}
}
