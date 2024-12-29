package com.habitapp.progress_service;

import com.habitapp.progress_service.domain.entity.Quote;
import com.habitapp.progress_service.domain.enumiration.WeekDay;
import com.habitapp.progress_service.domain.repository.QuoteRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ProgressServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProgressServiceApplication.class, args);
    }

    @Bean
    public CommandLineRunner initializeQuotes(QuoteRepository quoteRepository) {
        return args -> {
            quoteRepository.save(new Quote(0, "Start your week strong. Take one step towards your goals!", WeekDay.MONDAY));
            quoteRepository.save(new Quote(0, "Keep the momentum going. Tuesday is another chance to shine!", WeekDay.TUESDAY));
            quoteRepository.save(new Quote(0, "Halfway there! Let Wednesday be the bridge to success.", WeekDay.WEDNESDAY));
            quoteRepository.save(new Quote(0, "The weekend is near. Push harder this Thursday!", WeekDay.THURSDAY));
            quoteRepository.save(new Quote(0, "Celebrate your small wins. Finish strong this Friday!", WeekDay.FRIDAY));
            quoteRepository.save(new Quote(0, "Rest and recharge this Saturday. Great things await!", WeekDay.SATURDAY));
            quoteRepository.save(new Quote(0, "Plan and prepare for a week of greatness. Sunday is for focus.", WeekDay.SUNDAY));
        };
    }

}
