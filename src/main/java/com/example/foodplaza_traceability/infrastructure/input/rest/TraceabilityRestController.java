package com.example.foodplaza_traceability.infrastructure.input.rest;

import com.example.foodplaza_traceability.application.dto.TraceabilityRequestDto;
import com.example.foodplaza_traceability.application.dto.TraceabilityResponseDto;
import com.example.foodplaza_traceability.application.handler.ITraceabilityHandler;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/traceability-micro/")
@RequiredArgsConstructor
public class TraceabilityRestController {

    private final ITraceabilityHandler traceabilityHandler;
    private static final Logger log = LoggerFactory.getLogger(TraceabilityRestController.class);
    @PostMapping("/traceability")
    public ResponseEntity<TraceabilityResponseDto> logTraceability(@RequestBody TraceabilityRequestDto requestDto) {
        TraceabilityResponseDto responseDto = traceabilityHandler.logTraceability(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @GetMapping("/orders/{orderId}")
    @PreAuthorize("hasAuthority('ROLE_Customer')")
    public ResponseEntity<List<TraceabilityResponseDto>> getTraceabilityByOrder(@PathVariable Long orderId) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        log.info("Authentication: {}", auth);
        log.info("Authorities: {}", auth.getAuthorities());

        // Obtener el userId de los detalles del token
        if (auth.getDetails() instanceof Long customerId) {
            List<TraceabilityResponseDto> traceability = traceabilityHandler.getTraceabilityByOrder(orderId, customerId);
            return ResponseEntity.ok(traceability);
        } else {
            throw new IllegalStateException("Invalid authentication details");
        }
    }





}
