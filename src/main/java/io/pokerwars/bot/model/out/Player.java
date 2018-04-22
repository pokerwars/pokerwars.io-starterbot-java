package io.pokerwars.bot.model.out;

public class Player {

  private String username;

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  @Override
  public String toString() {
    return String.format("Player %s", getUsername());
  }

}
