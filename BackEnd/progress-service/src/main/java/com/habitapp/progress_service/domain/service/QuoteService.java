package com.habitapp.progress_service.domain.service;

import com.habitapp.progress_service.domain.entity.Quote;

public interface QuoteService {
    public Quote getQuoteForToday() throws Exception;
}
