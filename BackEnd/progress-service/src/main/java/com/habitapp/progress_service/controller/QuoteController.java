package com.habitapp.progress_service.controller;

import com.habitapp.common.http.request_response.quote.QuoteRequestResponseHttp;
import com.habitapp.progress_service.domain.entity.Quote;
import com.habitapp.progress_service.domain.enumiration.WeekDay;
import com.habitapp.progress_service.domain.service.QuoteService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/quote")
public class QuoteController {
    private final QuoteService quoteService;

    @GetMapping("/today")
    public ResponseEntity<QuoteRequestResponseHttp> getQuoteForToday() {
        try {
            QuoteRequestResponseHttp quote = this.mapToQuoteRequestResponse(quoteService.getQuoteForToday());
            return new ResponseEntity<>(quote, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    private Quote mapToQuote(QuoteRequestResponseHttp requestResponse) {
        Quote quote = new Quote();
        quote.setId(requestResponse.getId());
        quote.setMessage(requestResponse.getMessage());

        // Convert the String weekDay to the WeekDay enum, defaulting to MONDAY
        quote.setWeekDay(WeekDay.fromString(requestResponse.getWeekDay()));

        return quote;
    }

    private QuoteRequestResponseHttp mapToQuoteRequestResponse(Quote quote) {
        QuoteRequestResponseHttp requestResponse = new QuoteRequestResponseHttp();
        requestResponse.setId(quote.getId());
        requestResponse.setMessage(quote.getMessage());

        // Convert the WeekDay enum to its String representation
        requestResponse.setWeekDay(quote.getWeekDay().name());

        return requestResponse;
    }


}
