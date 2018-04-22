package io.pokerwars.bot.model.out.actions;

public class Fold implements PokerAction {

  @Override
  public String getAction() {
    return "fold";
  }

  @Override
  public String toString() {
    return getAction();
  }

}
