package com.habitapp.progress_service.domain.service.imp;

import com.habitapp.progress_service.domain.entity.Quote;
import com.habitapp.progress_service.domain.enumiration.WeekDay;
import com.habitapp.progress_service.domain.repository.QuoteRepository;
import com.habitapp.progress_service.domain.service.QuoteService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;

@Service
@AllArgsConstructor
public class QuoteServiceImp implements QuoteService {
    private final QuoteRepository quoteRepository;


    @Override
    public Quote getQuoteForToday() throws Exception {
        WeekDay currentDay = mapToWeekDay(LocalDate.now().getDayOfWeek());

        return quoteRepository.findAll()
                .stream()
                .filter(quote -> quote.getWeekDay() == currentDay)
                .findFirst()
                .orElseThrow(() -> new Exception("No quote found for today: " + currentDay));
    }

    private WeekDay mapToWeekDay(DayOfWeek dayOfWeek) {
        return WeekDay.valueOf(dayOfWeek.name());
    }
}
