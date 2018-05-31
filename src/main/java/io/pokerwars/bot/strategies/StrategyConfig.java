package io.pokerwars.bot.strategies;

import org.springframework.beans.factory.annotation.Value;

public class StrategyConfig {

  @Value("${pokerwars.bot.strategy.aggressiveness}")
  private Integer aggressiveness;

  @Value("${pokerwars.bot.strategy.bluff}")
  private Integer bluff;

  @Value("${pokerwars.bot.strategy.raise-factor}")
  private Integer raiseFactor;

  public Integer getAggressiveness() {
    return sanitiseConfig(aggressiveness);
  }

  public boolean isAggressivePlayer() {
    return getAggressiveness() > 3;
  }

  public Integer getBluff() {
    return sanitiseConfig(bluff);
  }

  public Integer getRaiseFactor() {
    return sanitiseConfig(raiseFactor);
  }

  private Integer sanitiseConfig(Integer value) {
    if(value < 0) {
      return 0;
    } else if (value > 5) {
      return 5;
    } else {
      return value;
    }
  }
}
