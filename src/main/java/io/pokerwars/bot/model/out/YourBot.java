package io.pokerwars.bot.model.out;

public class YourBot {

  private String username;
  private String token;
  private String botEndpoint;
  private Boolean notifications;

  public YourBot(String yourBotUsername, String yourBotToken, String yourBotEndpoint,
      Boolean notifications) {
    this.username = yourBotUsername;
    this.token = yourBotToken;
    this.botEndpoint = yourBotEndpoint;
    this.notifications = notifications;
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

  public Boolean getNotifications() {
    return notifications;
  }

  public void setNotifications(Boolean notifications) {
    this.notifications = notifications;
  }

  @Override
  public String toString() {
    return String.format("YourBot [username: %s, botEndpoint: %s, notifications: %s]", username,
        botEndpoint, notifications);
  }

}
