package com.nimbleways.springboilerplate.infra.ui.web;

import com.nimbleways.springboilerplate.api.ProcessOrderUseCase;
import com.nimbleways.springboilerplate.infra.ui.web.dto.ProcessOrderResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class MyController {

    private final ProcessOrderUseCase processOrderUseCase;

    @Operation(summary = "Process an order",
               description = "Decrements stock and sends notifications based on product type.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Order processed successfully"),
        @ApiResponse(responseCode = "404", description = "Order not found"),
        @ApiResponse(responseCode = "422", description = "Unsupported product type")
    })
    @PostMapping("{orderId}/processOrder")
    @ResponseStatus(HttpStatus.OK)
    public ProcessOrderResponse processOrder(
            @Parameter(description = "Order identifier") @PathVariable Long orderId) {
        return new ProcessOrderResponse(processOrderUseCase.processOrder(orderId));
    }
}
