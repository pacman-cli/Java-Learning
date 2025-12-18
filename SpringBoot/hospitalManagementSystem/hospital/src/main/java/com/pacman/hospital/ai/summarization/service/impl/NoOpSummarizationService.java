package com.pacman.hospital.ai.summarization.service.impl;

import com.pacman.hospital.ai.summarization.service.SummarizationService;
import org.springframework.stereotype.Service;

@Service
public class NoOpSummarizationService implements SummarizationService {
    @Override
    public String summarize(String text) {
        if (text == null) {
            return null;
        }
        // simple naive "summary" — first 160 chars (you'll replace with real AI)
        return text.length() == 0 ? "" : text.substring(0, text.length() - 1);
    }
}
