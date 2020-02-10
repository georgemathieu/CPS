package ports;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import interfaces.ManagementCI;
import interfaces.MessageFilterI;

public class SubscriberManagementOutbondPort 
extends AbstractOutboundPort 
implements  ManagementCI
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SubscriberManagementOutbondPort(Class<?> implementedInterface, ComponentI owner) throws Exception {
		super(implementedInterface, owner);
	}
	
	public SubscriberManagementOutbondPort(String uri, ComponentI owner) throws Exception {
		super(uri, ManagementCI.class, owner);
	}

	@Override
	public void createTopic(String topic) {
		((ManagementCI)this.connector).createTopic(topic);
	}

	@Override
	public void createTopics(String[] topic) {
		((ManagementCI)this.connector).createTopics(topic);
	}

	@Override
	public void destroyTopic(String topic) {
		((ManagementCI)this.connector).destroyTopic(topic);
	}

	@Override
	public boolean isTopic(String topic) {
		return ((ManagementCI)this.connector).isTopic(topic);
	}

	@Override
	public String[] getTopics() {
		return ((ManagementCI)this.connector).getTopics();
	}

	@Override
	public void subscribe(String topic, String inboundPortURI) {
		((ManagementCI)this.connector).subscribe(topic, inboundPortURI);
	}

	@Override
	public void subscribe(String[] topics, String inboutPortURI) {
		((ManagementCI)this.connector).subscribe(topics, inboutPortURI);
	}

	@Override
	public void subscribe(String topic, MessageFilterI filter, String inboutPortURI) {
		((ManagementCI)this.connector).subscribe(topic, filter, inboutPortURI);
	}

	@Override
	public void unsubscribe(String topic, String inboundPortURI) {
		((ManagementCI)this.connector).unsubscribe(topic, inboundPortURI);
	}

}
