package com.pacman.ollama.spring_ai_ollama.service;

import com.pacman.ollama.spring_ai_ollama.entity.Tut;

import java.util.List;

public interface ChatService {
    public List<Tut> chat(String message);
}
