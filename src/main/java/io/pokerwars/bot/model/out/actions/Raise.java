package io.pokerwars.bot.model.out.actions;

public class Raise implements PokerAction {

  private Long chips;

  public Raise(Long chips) {
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
    return "raise";
  }

  @Override
  public String toString() {
    return String.format("%s %s", getAction(), chips);
  }

}
