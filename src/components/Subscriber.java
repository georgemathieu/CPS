package components;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.cvm.AbstractCVM;
import fr.sorbonne_u.components.exceptions.PostconditionException;
import fr.sorbonne_u.components.exceptions.PreconditionException;
import fr.sorbonne_u.components.ports.PortI;
import interfaces.MessageI;
import interfaces.ReceptionCI;
import ports.PublisherManagementOutboundPort;
import ports.SubscriberManagementOutbondPort;
import ports.SubscriberReceptionInboundPort;

public class Subscriber extends AbstractComponent{
	
	protected String subscriberReceptionInboundPortURI;
	
	protected SubscriberManagementOutbondPort smop;

	public Subscriber(int nbThreads, int nbSchedulableThreads) {
		super(nbThreads, nbSchedulableThreads);
	}
	
	protected Subscriber(
			String uri,
			String receptionInboundPortURI,
			String managementOutboundPortURI) throws Exception
		{
			super(uri, 1, 0);

			assert	uri != null :
						new PreconditionException("uri can't be null!") ;
			assert	receptionInboundPortURI != null :
						new PreconditionException("receptionInboundPortURI can't be null!") ;

			this.subscriberReceptionInboundPortURI = uri ;
			
			//Publish the management outbound port
			this.smop = new SubscriberManagementOutbondPort(managementOutboundPortURI, this);
			this.smop.localPublishPort();

			//Publish the reception inbound port
			PortI p = new SubscriberReceptionInboundPort(receptionInboundPortURI, this) ;
			p.publishPort() ;

			if (AbstractCVM.isDistributed) {
				this.executionLog.setDirectory(System.getProperty("user.dir")) ;
			} else {
				this.executionLog.setDirectory(System.getProperty("user.home")) ;
			}
			
			this.tracer.setTitle("subscriber") ;
			this.tracer.setRelativePosition(0, 1) ;

			Subscriber.checkInvariant(this) ;
			assert	this.subscriberReceptionInboundPortURI.equals(uri) :
						new PostconditionException("The URI prefix has not "
													+ "been initialised!") ;
			assert	this.isPortExisting(receptionInboundPortURI) :
						new PostconditionException("The component must have a "
								+ "port with URI " + receptionInboundPortURI) ;
			assert	this.findPortFromURI(receptionInboundPortURI).
						getImplementedInterface().equals(ReceptionCI.class) :
						new PostconditionException("The component must have a "
								+ "port with implemented interface URIProviderI") ;
			assert	this.findPortFromURI(receptionInboundPortURI).isPublished() :
						new PostconditionException("The component must have a "
								+ "port published with URI " + receptionInboundPortURI) ;
		}
	
	public void acceptMessage(MessageI m) throws Exception {

		logMessage("Getting message "+m);
	}
	
	public void acceptMessage(MessageI[] ms) throws Exception 
	{

		for (int i = 0; i < ms.length; i++) {
			logMessage("Getting message " + ms[i]);
		}
	}

}
