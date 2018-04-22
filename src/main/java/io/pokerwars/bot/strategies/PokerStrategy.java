package io.pokerwars.bot.strategies;

import io.pokerwars.bot.model.in.GameInfo;
import io.pokerwars.bot.model.out.actions.Call;
import io.pokerwars.bot.model.out.actions.Check;
import io.pokerwars.bot.model.out.actions.Fold;
import io.pokerwars.bot.model.out.actions.PokerAction;

public enum PokerStrategy {

  ALWAYS_FOLD {
    @Override
    public PokerAction play(GameInfo gameInfo) {
      return new Fold();
    }
  },

  ALWAYS_CHECK {
    @Override
    public PokerAction play(GameInfo gameInfo) {
      Boolean canBetOrCheck = gameInfo.canCheckOrBet();
      if (canBetOrCheck) {
        return new Check();
      } else {
        return new Fold();
      }
    }
  },

  ALWAYS_CALL {
    @Override
    public PokerAction play(GameInfo gameInfo) {
      Boolean canBetOrCheck = gameInfo.canCheckOrBet();
      if (canBetOrCheck) {
        return new Check();
      } else {
        return new Call();
      }
    }
  },

  RANDOM {
    @Override
    public PokerAction play(GameInfo gameInfo) {
      return RandomPokerStrategy.play(gameInfo);
    }

  };

  public abstract PokerAction play(GameInfo gameInfo);

}
