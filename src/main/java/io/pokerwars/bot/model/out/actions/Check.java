package io.pokerwars.bot.model.out.actions;

public class Check implements PokerAction {

  @Override
  public String getAction() {
    return "check";
  }

  @Override
  public String toString() {
    return getAction();
  }

}
