package com.example.foodplaza.infrastructure.out.jpa.feignclients.mapper;

import com.example.foodplaza.application.dto.request.TraceabilityRequestDto;
import com.example.foodplaza.application.dto.response.TraceabilityResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "traceability-service", url = "${traceability-service.url}/traceability-micro")
public interface ITraceabilityFeignClient {

    @PostMapping("/traceability")
    TraceabilityResponseDto logTraceability(@RequestBody TraceabilityRequestDto traceabilityRequestDto);
}
