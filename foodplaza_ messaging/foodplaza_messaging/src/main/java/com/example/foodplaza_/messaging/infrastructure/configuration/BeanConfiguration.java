package com.example.foodplaza_.messaging.infrastructure.configuration;

import com.example.foodplaza_.messaging.domain.api.ISmsServicePort;
import com.example.foodplaza_.messaging.domain.spi.ISmsPersistencePort;
import com.example.foodplaza_.messaging.domain.usecase.SmsUseCase;
import com.example.foodplaza_.messaging.infrastructure.output.twilio.TwilioAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    @Bean
    public ISmsPersistencePort smsPersistencePort(TwilioConfig twilioConfig) {
        return new TwilioAdapter(twilioConfig);
    }

    @Bean
    public ISmsServicePort smsServicePort(ISmsPersistencePort smsPersistencePort) {
        return new SmsUseCase(smsPersistencePort);
    }
}
