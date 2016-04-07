import com.google.common.collect.ImmutableSortedSet;
import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;
import org.pircbotx.exception.IrcException;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.JoinEvent;
import org.pircbotx.hooks.events.TopicEvent;
import org.pircbotx.hooks.events.UserListEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class MultiChannel {
  private static Logger LOG = LoggerFactory.getLogger(MultiChannel.class);

  public static void main(String[] args) {
    new MultiChannel();
  }


  public MultiChannel() {

    final String botName = createBotName("AS_");
    final String channel = "#moviegods";
    final String server = "irc.abjects.net";
    final Set<String> additionalChannels = new HashSet<>();

    PircBotX bot = createBot(botName, channel, server);
    bot.getConfiguration().getListenerManager().addListener(new ListenerAdapter() {

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

    try {
      bot.startBot();
    } catch(IOException e) {
      e.printStackTrace();
    } catch(IrcException e) {
      e.printStackTrace();
    }
  }

  private PircBotX createBot(String botName, String channel, String server) {
    Configuration.Builder builder = new Configuration.Builder();
    builder.addServer(server);
    builder.setName(botName);
    builder.addAutoJoinChannel(channel);
    builder.addAutoJoinChannel("#mg-chat");

    return new PircBotX(builder.buildConfiguration());
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
