package io.pokerwars.bot.model.in;

public class PlayerInfo {

  private String username;
  private Long chips;
  private Long pot;
  private Boolean allIn;
  private Boolean hasFolded;

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public Long getChips() {
    return chips;
  }

  public void setChips(Long chips) {
    this.chips = chips;
  }

  public Long getPot() {
    return pot;
  }

  public void setPot(Long pot) {
    this.pot = pot;
  }

  public Boolean isAllIn() {
    return allIn;
  }

  public void setAllIn(Boolean allIn) {
    this.allIn = allIn;
  }

  public Boolean getHasFolded() {
    return hasFolded;
  }

  public void setHasFolded(Boolean hasFolded) {
    this.hasFolded = hasFolded;
  }

  @Override
  public String toString() {
    return String.format("[username: %s, chips: %s, pot=%s, isAllIn: %s, hasFolded: %s]", username,
        chips, pot, allIn, hasFolded);
  }


}
