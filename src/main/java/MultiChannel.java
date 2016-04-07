import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;
import org.pircbotx.exception.IrcException;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.JoinEvent;
import org.pircbotx.hooks.events.TopicEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class MultiChannel {
  private static Logger LOG = LoggerFactory.getLogger(MultiChannel.class);

  public static void main(String[] args) {
    new MultiChannel();
  }


  public MultiChannel() {
    final String botName = createBotName("AS_");

    Configuration.Builder builder = new Configuration.Builder();
    builder.addServer("irc.abjects.net");
    builder.setName(botName);
    builder.addAutoJoinChannel("#moviegods");
    builder.addAutoJoinChannel("#mg-chat");
    builder.addListener(new ListenerAdapter() {

      @Override
      public void onJoin(JoinEvent event) throws Exception {
        String name = event.getChannel().getName();
        LOG.info("JoinEvent; {} - {} users", name, event.getChannel().getUsersNicks().size());
      }

      @Override
      public void onTopic(TopicEvent event) throws Exception {
        String topic = event.getTopic();
        String name = event.getChannel().getName();

        LOG.info("TopicEvent; {} ({})", name, topic);
      }
    });
    PircBotX bot = new PircBotX(builder.buildConfiguration());

    try {
      bot.startBot();
    } catch(IOException e) {
      e.printStackTrace();
    } catch(IrcException e) {
      e.printStackTrace();
    }
  }

  private String createBotName(String prefix) {
    StringBuilder s = new StringBuilder(prefix);
    String allowedChars = "abcdefghijklmnopqrstuvwxyz1234567890";
    for(int i = 0; i < 2; i++) {
      int index = (int) (Math.random() * allowedChars.length());
      char charAt = allowedChars.charAt(index);
      s.append(charAt);
    }

    return s.toString();
  }

}
