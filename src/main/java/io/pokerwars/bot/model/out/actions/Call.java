package io.pokerwars.bot.model.out.actions;

public class Call implements PokerAction {

  @Override
  public String getAction() {
    return "call";
  }

  @Override
  public String toString() {
    return getAction();
  }

}
