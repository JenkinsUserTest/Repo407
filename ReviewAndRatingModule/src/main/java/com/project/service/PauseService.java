package com.project.service;

import org.springframework.cloud.context.restart.PauseHandler;
import org.springframework.stereotype.Component;

@Component
public class PauseService implements PauseHandler {
    @Override
    public void pause() {
        System.out.println("paused");
    }

    @Override
    public void resume() {
      System.out.println("resume");
    }
}
