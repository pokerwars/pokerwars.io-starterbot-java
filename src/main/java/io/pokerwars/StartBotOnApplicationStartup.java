package io.pokerwars;

import io.pokerwars.bot.PokerwarsService;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class StartBotOnApplicationStartup {

  private PokerwarsService pokerwarsService;

  public StartBotOnApplicationStartup(PokerwarsService pokerwarsService) {
    this.pokerwarsService = pokerwarsService;
  }

  @EventListener(ApplicationReadyEvent.class)
  public void startBotOnApplicationStartup() {
    pokerwarsService.subscribeBotToPokerwars();
  }

}
