package io.pokerwars;

import io.pokerwars.bot.strategies.StrategyConfig;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class PokerwarsStarterBotApplication {

  public static void main(String[] args) {
    SpringApplication.run(PokerwarsStarterBotApplication.class, args);
  }

  @Bean
  public Client client() {
    return ClientBuilder.newClient();
  }

  @Bean
  public StrategyConfig strategyConfig() {
    return new StrategyConfig();
  }

}
