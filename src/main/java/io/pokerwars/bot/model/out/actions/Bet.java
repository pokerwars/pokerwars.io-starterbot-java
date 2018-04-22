package io.pokerwars.bot.model.out.actions;

public class Bet implements PokerAction {

  private Long chips;

  public Bet(Long chips) {
    this.chips = chips;
  }

  public Long getChips() {
    return chips;
  }

  public void setChips(Long chips) {
    this.chips = chips;
  }

  @Override
  public String getAction() {
    return "bet";
  }

  @Override
  public String toString() {
    return String.format("%s %s", getAction(), chips);
  }

}
