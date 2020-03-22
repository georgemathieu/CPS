package components;

import fr.sorbonne_u.components.AbstractComponent;
import interfaces.ManagementCI;
import interfaces.MessageI;
import interfaces.PublicationCI;
import message.Message;
import message.MessageFilterI;
import message.Properties;
import message.TimeStamp;
import plugins.PublisherManagementPlugin;
import plugins.PublisherPublicationPlugin;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class PublisherUK extends AbstractComponent {
    protected final static String UK_PUB_PLUGIN_URI = "publisher-uk-pub-plugin-uri";
    protected final static String UK_MAN_PLUGIN_URI = "publisher-uk-man-plugin-uri";

    protected PublisherUK(int nbThreads, int nbSchedulableThreads) {
        super(nbThreads, nbSchedulableThreads);
    }

    protected PublisherUK(String reflectionInboundPortURI) throws Exception {
        super(reflectionInboundPortURI, 0, 1);

        this.tracer.setTitle("publisher-uk");
        this.tracer.setRelativePosition(1, 3);
    }

    @Override
    public void execute() throws Exception {
        super.execute();
        PublisherPublicationPlugin pluginPublication = new PublisherPublicationPlugin();
        pluginPublication.setPluginURI(UK_PUB_PLUGIN_URI);
        this.installPlugin(pluginPublication);

        PublisherManagementPlugin managementPlugin = new PublisherManagementPlugin();
        managementPlugin.setPluginURI(UK_MAN_PLUGIN_URI);
        this.installPlugin(managementPlugin);
        String topic;
        /*createTopic("UK");
        createTopic("London");
        createTopic("Cambridge");*/
        Message msg;
        // 15 msg UK, 20 msg London, 35 msg Cambridge
        for (int i = 0; i < 10; i++) {
            if (i < 4) {
                topic = "UK";
                msg = new Message("6 degrees in average in the UK");
            } else if (i < 7) {
                topic = "London";
                msg = new Message("10 degrees in London");
            } else {
                topic = "Cambridge";
                TimeStamp ts = new TimeStamp(System.currentTimeMillis(), "PbUK");

                msg = new Message("PayloadUK", ts, "5 degrees in Cambridge1:" + i, new Properties());
                msg.getProperties().putProp("Integer", 3);
                msg.getProperties().putProp("Long", 3L);
                msg.getProperties().putProp("Character", '3');
                msg.getProperties().putProp("Short", (short) 3);
                msg.getProperties().putProp("Byte", (byte) 3);
                msg.getProperties().putProp("Float", 3f);
                msg.getProperties().putProp("String", "3");
                msg.getProperties().putProp("Boolean", true);
                msg.getProperties().putProp("Double", 3.0);
                msg.getTimeStamp().setTime(System.currentTimeMillis());
                msg.getTimeStamp().setTimestamper("PbUK");
            }

            logMessage("Publishing message " + i + " for topic : " + topic);
            publish(msg, topic);
        }


        Thread.sleep(3000);
        for (int k = 0; k < 3; k++) {
            for (int i = 0; i < 3; i++) {
                topic = "Cambridge";
                TimeStamp ts = new TimeStamp(System.currentTimeMillis(), "PbUK");

                //Now set different properies every odd send so that the filter fail sometimes
                if (i % 2 == 0) {
                    msg = new Message("PayloadUK" + k, ts, "Even message :" + i, new Properties());
                    msg.getProperties().putProp("Integer", 3);
                    msg.getProperties().putProp("Long", 3L);
                    msg.getProperties().putProp("Character", '3');
                } else {
                    msg = new Message("PayloadUK" + k, ts, "Odd message :" + i, new Properties());
                    msg.getProperties().putProp("Integer", 5);
                    msg.getProperties().putProp("Long", 5L);
                    msg.getProperties().putProp("Character", '5');
                }

                msg.getProperties().putProp("Short", (short) 3);
                msg.getProperties().putProp("Byte", (byte) 3);
                msg.getProperties().putProp("Float", 3f);
                msg.getProperties().putProp("String", "3");
                msg.getProperties().putProp("Boolean", true);
                msg.getProperties().putProp("Double", 3.0);
                msg.getTimeStamp().setTime(System.currentTimeMillis());
                msg.getTimeStamp().setTimestamper("PbUK");

                publish(msg, topic);
            }
            Thread.sleep(3000);
        }
        Map<String, Set<Message>> map = new HashMap<>();
        for (int k = 0; k < 3; k++) {
            for (int i = 0; i < 3; i++) {
                TimeStamp ts = new TimeStamp(System.currentTimeMillis(), "PbUK");

                //Now set different properies every odd send so that the filter fail sometimes
                if (k > 1) {
                    topic = "UK";
                    msg = new Message("PayloadUK" + k, ts, "MultipleMSG, mate " + i, new Properties());
                } else {
                    topic = "London";
                    msg = new Message("PayloadLondon" + k, ts, "oi ! MultipleMSG: " + i, new Properties());
                }

                if (map.containsKey(topic)) {
                    map.get(topic).add(msg);
                } else {
                    Set<Message> ms = new HashSet<>();
                    ms.add(msg);
                    map.put(topic, ms);
                }
            }
            Thread.sleep(800);
        }

        for (Map.Entry<String, Set<Message>> e : map.entrySet()) {
            //2 sets of messages for 2 different topics (keys of map).
            Message[] msgArray = new Message[e.getValue().size()];
            publish(e.getValue().toArray(msgArray), e.getKey());
        }

    }


    private void publish(Message message, String topic) throws Exception {
        ((PublisherPublicationPlugin) this.getPlugin(UK_PUB_PLUGIN_URI)).publish(message, topic);
    }

    private void publish(Message[] message, String topic) throws Exception {
        ((PublisherPublicationPlugin) this.getPlugin(UK_PUB_PLUGIN_URI)).publish(message, topic);
    }

    private void publish(Message message, String[] topic) throws Exception {
        ((PublisherPublicationPlugin) this.getPlugin(UK_PUB_PLUGIN_URI)).publish(message, topic);
    }

    private void publish(Message[] message, String[] topic) throws Exception {
        ((PublisherPublicationPlugin) this.getPlugin(UK_PUB_PLUGIN_URI)).publish(message, topic);
    }

    public void createTopic(String topic) throws Exception {
        ((PublisherManagementPlugin) this.getPlugin(UK_MAN_PLUGIN_URI)).createTopic(topic);
    }

    public void createTopic(String[] topics) throws Exception {
        ((PublisherManagementPlugin) this.getPlugin(UK_MAN_PLUGIN_URI)).createTopic(topics);
    }

    public void destroy(String topic) throws Exception {
        ((PublisherManagementPlugin) this.getPlugin(UK_MAN_PLUGIN_URI)).destroyTopic(topic);
    }

    public boolean isTopic(String topic) throws Exception {
        return ((PublisherManagementPlugin) this.getPlugin(UK_MAN_PLUGIN_URI)).isTopic(topic);
    }

    public String[] getTopics() throws Exception {
        return ((PublisherManagementPlugin) this.getPlugin(UK_MAN_PLUGIN_URI)).getTopics();
    }

    public String getPublicatinPortURI() throws Exception {
        return ((PublisherManagementPlugin) this.getPlugin(UK_MAN_PLUGIN_URI)).getPublicatinPortURI();
    }

}

