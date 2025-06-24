package letsexploretanzania.co.tz.letsexploretanzania.controller;

import letsexploretanzania.co.tz.letsexploretanzania.common.enums.CurrencyEnum;
import letsexploretanzania.co.tz.letsexploretanzania.common.enums.TourDestinationEnum;
import letsexploretanzania.co.tz.letsexploretanzania.common.utils.ApiResponse;
import letsexploretanzania.co.tz.letsexploretanzania.models.responses.CurrencyDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("api/v1/currency")
public class CurrencyController {
    @GetMapping
    public ResponseEntity<ApiResponse<List<CurrencyDTO>>> getAllDestinations()
    {
        List<CurrencyDTO> currencyDTOList = Arrays.stream(CurrencyEnum.values())
                .map( c ->
                        new CurrencyDTO(
                                c.getCode(),
                                c.getSymbol()
                        )
                ).toList();
        return ResponseEntity.ok(ApiResponse.success(currencyDTOList, HttpStatus.OK.value()));
    }
}
