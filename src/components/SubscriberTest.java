package components;

import fr.sorbonne_u.components.*;
import fr.sorbonne_u.components.exceptions.*;
import interfaces.*;
import plugins.*;

/**
 * Variant of the Subscriber component. It has a different behavior.
 * It is used to publish messages.
 *
 * <p><strong>Description</strong></p>
 *
 * <p><strong>Invariant</strong></p>
 *
 * <pre>
 * invariant		true
 * </pre>
 */
public class SubscriberTest extends AbstractComponent implements ReceptionImplementationI {
    protected final static String SUB_TEST_RECEPT_PLUGIN_URI = "sub-test-recept-uri";
    protected final static String SUB_TEST_MANAGE_PLUGIN_URI = "sub-test-manage-uri";

    public String getUriCorrespondingBroker() {
        return uriCorrespondingBroker;
    }

    public String uriCorrespondingBroker;
    static int i = 0;
    static final Object iGuard = new Object();
    private final String subscriberReceptionInboundPortURI = "subscriber-test-reception-inbound-port-uri-3";


    /**
     * Subscriber creation
     *
     * <p><strong>Contract</strong></p>
     *
     * <pre>
     * pre	reflectionInboundPortURI != null
     * post	true			// no postcondition.
     * </pre>
     *
     * @param reflectionInboundPortURI uri of the owner inbound port
     * @throws Exception
     */
    protected SubscriberTest(String reflectionInboundPortURI,String brokerURI) throws Exception {
        super(reflectionInboundPortURI, 0, 1);
        this.uriCorrespondingBroker=brokerURI;
        this.tracer.setTitle("sub-test");
        this.tracer.setRelativePosition(1, 2);
    }


    /**
     * Action executed by the component
     */
    @Override
    public void execute() throws Exception {
        super.execute();
        SubscriberManagementPlugin smp = new SubscriberManagementPlugin();
        smp.setPluginURI(SUB_TEST_MANAGE_PLUGIN_URI);
        this.installPlugin(smp);


        //unstailling the plgin that will create port and publish it
        SubscriberReceptionPlugin subscriberPlugin
                = new SubscriberReceptionPlugin(subscriberReceptionInboundPortURI);
        subscriberPlugin.setPluginURI(SUB_TEST_RECEPT_PLUGIN_URI);
        this.installPlugin(subscriberPlugin);


        /*
        SubscriberReceptionPlugin subscriberPlugin = new SubscriberReceptionPlugin();
        subscriberPlugin.setPluginURI(SUB_TEST_RECEPT_PLUGIN_URI);
        this.installPlugin(subscriberPlugin);
*/
        //test scenario
        MessageFilterI filter = m -> {
            Long lng = m.getProperties().getLongProp("Long");
            Integer in = m.getProperties().getIntProp("Integer");
            Character cr = m.getProperties().getCharProp("Character");
            Byte by = m.getProperties().getByteProp("Byte");
            Boolean bo = m.getProperties().getBooleanProp("Boolean");
            String str = m.getProperties().getStringProp("String");
            Short shrt = m.getProperties().getShortProp("Short");
            Double dbl = m.getProperties().getDoubleProp("Double");
            Float flt = m.getProperties().getFloatProp("Float");
            return (lng != null && in != null && cr != null &&
                    by != null && bo != null && str != null &&
                    shrt != null && dbl != null && flt != null &&
                    lng == 3L && in == 3 && cr == '3' && by == 3
                    && bo && str.equals("3") && shrt == 3
                    && dbl == 3.0 && flt == 3.0f)
                    && m.getPayload() != null;
        };


        String[] topics = {"USA", "Test", "Anchorage", "UK"};
        subscribe(topics); // 35 msg
        subscribe("Cambridge", filter); // 35 msg

        MessageFilterI filterModified = m -> {
            Long lng = m.getProperties().getLongProp("Long");
            Integer in = m.getProperties().getIntProp("Integer");
            Character cr = m.getProperties().getCharProp("Character");
            Byte by = m.getProperties().getByteProp("Byte");
            Boolean bo = m.getProperties().getBooleanProp("Boolean");
            String str = m.getProperties().getStringProp("String");
            Short shrt = m.getProperties().getShortProp("Short");
            Double dbl = m.getProperties().getDoubleProp("Double");
            Float flt = m.getProperties().getFloatProp("Float");
            return (lng != null && in != null && cr != null &&
                    by != null && bo != null && str != null &&
                    shrt != null && dbl != null && flt != null &&
                    lng == 5L && in == 5 && cr == '5' && by == 3
                    && bo && str.equals("3") && shrt == 3
                    && dbl == 3.0 && flt == 3.0f)
                    && m.getPayload() != null;
        };
        Thread.sleep(5000);
        modifyFilter("Cambridge", filterModified);


    }

    /**
     * @see ReceptionCI#acceptMessage(MessageI)
     */
    public void acceptMessage(MessageI m) throws Exception {
        logMessage("Getting message " + m);
        if (m.getPayload() != null) {
            logMessage(" Pl : " + m.getPayload());
        }
        if (m.getTimeStamp() != null) {
            logMessage(" Ts : " + m.getTimeStamp().getTime() + ":" + m.getTimeStamp().getTimestamper());
        }

    }

    /**
     * @see ReceptionCI#acceptMessage(MessageI[])
     */
    public void acceptMessage(MessageI[] ms) throws Exception {
        for (MessageI m : ms) {
            acceptMessage(m);
        }
    }

    /**
     * @see ManagementCI#subscribe(String, String)
     */
    public void subscribe(String topic) throws Exception {


        logMessage("Subscribing to " + topic);

        //sending the port over the Broker
        ((SubscriberManagementPlugin) this.getPlugin(
                SUB_TEST_MANAGE_PLUGIN_URI)).subscribe(topic, subscriberReceptionInboundPortURI);


        assert this.subscriberReceptionInboundPortURI.equals("subscriber-test-reception-inbound-port-uri-3") :
                new PostconditionException("The URI prefix has not "
                        + "been initialised!");
        assert this.isPortExisting(subscriberReceptionInboundPortURI) :
                new PostconditionException("The component must have a "
                        + "port with URI " + subscriberReceptionInboundPortURI);
        assert this.findPortFromURI(subscriberReceptionInboundPortURI).isPublished() :
                new PostconditionException("The component must have a "
                        + "port published with URI " + subscriberReceptionInboundPortURI);

    }

    /**
     * @see ManagementCI#subscribe(String[], String)
     */
    public void subscribe(String[] topics) throws Exception {
        ((SubscriberManagementPlugin) this.getPlugin(SUB_TEST_MANAGE_PLUGIN_URI)).subscribe(topics, subscriberReceptionInboundPortURI);
    }

    /**
     * @see ManagementCI#subscribe(String, MessageFilterI, String)
     */
    public void subscribe(String topic, MessageFilterI filter) throws Exception {
        ((SubscriberManagementPlugin) this.getPlugin(SUB_TEST_MANAGE_PLUGIN_URI)).subscribe(topic, filter, subscriberReceptionInboundPortURI);
    }

    /**
     * @see ManagementCI#modifyFilter(String, MessageFilterI, String)
     */
    public void modifyFilter(String topic, MessageFilterI newFilter) throws Exception {
        ((SubscriberManagementPlugin) this.getPlugin(SUB_TEST_MANAGE_PLUGIN_URI)).modifyFilter(topic, newFilter, subscriberReceptionInboundPortURI);
    }

    /**
     * @see ManagementCI#unsubscribe(String, String)
     */
    public void unsubscribe(String topic) throws Exception {
        ((SubscriberManagementPlugin) this.getPlugin(SUB_TEST_MANAGE_PLUGIN_URI)).unsubscribe(topic, subscriberReceptionInboundPortURI);
    }

    /**
     * @see ManagementCI#createTopic(String)
     */
    public void createTopic(String topic) throws Exception {
        ((SubscriberManagementPlugin) this.getPlugin(SUB_TEST_MANAGE_PLUGIN_URI)).createTopic(topic);
    }

    /**
     * @see ManagementCI#createTopics(String[])
     */
    public void createTopic(String[] topics) throws Exception {
        ((SubscriberManagementPlugin) this.getPlugin(SUB_TEST_MANAGE_PLUGIN_URI)).createTopic(topics);
    }

    /**
     * @see ManagementCI#destroyTopic(String)
     */
    public void destroy(String topic) throws Exception {
        ((SubscriberManagementPlugin) this.getPlugin(SUB_TEST_MANAGE_PLUGIN_URI)).destroyTopic(topic);
    }

    /**
     * @see ManagementCI#isTopic(String)
     */
    public boolean isTopic(String topic) throws Exception {
        return ((SubscriberManagementPlugin) this.getPlugin(SUB_TEST_MANAGE_PLUGIN_URI)).isTopic(topic);
    }

    /**
     * @see ManagementCI#getTopics()
     */
    public String[] getTopics() throws Exception {
        return ((SubscriberManagementPlugin) this.getPlugin(SUB_TEST_MANAGE_PLUGIN_URI)).getTopics();
    }

    /**
     * @see ManagementCI#getPublicatinPortURI()
     */
    public String getPublicatinPortURI() throws Exception {
        return ((PublisherManagementPlugin) this.getPlugin(SUB_TEST_MANAGE_PLUGIN_URI)).getPublicatinPortURI();
    }

}