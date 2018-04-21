package io.pokerwars.bot.model.out;

public class YourBot {

  private String username;
  private String token;
  private String botEndpoint;

  public YourBot(String yourBotUsername, String yourBotToken, String yourBotEndpoint) {
    this.username = yourBotUsername;
    this.token = yourBotToken;
    this.botEndpoint = yourBotEndpoint;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public String getBotEndpoint() {
    return botEndpoint;
  }

  public void setBotEndpoint(String botEndpoint) {
    this.botEndpoint = botEndpoint;
  }

  @Override
  public String toString() {
    return String.format("Player %s, endpoint: %s", username, botEndpoint);
  }

}
