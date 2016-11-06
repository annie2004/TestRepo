package intelliment.dto;

import intelliment.util.StringsUtils;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@XmlRootElement(name = "rule")
public class RuleDto {
	
	private static final Logger log = LoggerFactory
			.getLogger(RuleDto.class);

	/**
	 * Default constructor
	 */
	public RuleDto() {

	}

	/**
	 * Constructor from a line of txt
	 * 
	 * @param aRule
	 */
	public RuleDto(String[] aRule) {
		try {
			this.id = StringsUtils.validateIntegerId(aRule[0]);
			this.source = StringsUtils.setDefaultCidr(aRule[2]);
			this.destination = StringsUtils.setDefaultCidr(aRule[4]);
			this.protocol = aRule[6];
			this.action = StringsUtils.validateAction(aRule[8]);
			this.shortProtocol = StringsUtils.getShortProtocol(aRule[6]);
			this.portsProtocol = StringsUtils.getPortsProtocol(aRule[6]);
			this.sourceBinary = StringsUtils.getIpBinary(aRule[2]);
			this.sourceCidr = StringsUtils.getCidr(aRule[2]);
			this.destinationBinary = StringsUtils.getIpBinary(aRule[4]);
			this.destinationCidr = StringsUtils.getCidr(aRule[4]);
		} catch (Exception exception) {
			log.error("ERROR ON FILE FORMAT. PLEASE CHECK THIS DATA: ");
			log.error("id:  " + aRule[0]);
			log.error("source:  " + aRule[2]);
			log.error("destination:  " + aRule[4]);
			log.error("protocol:  " + aRule[6]);
			log.error("action:  " + aRule[8]);
			throw new RuntimeException();
		}
	}

	/**
	 * Constructor for Request body Object
	 * @param packetRequest
	 */
	public RuleDto(RuleDto packetRequest) {
		try {
			this.source = StringsUtils.validateWhitoutCdir(packetRequest.getSource());
			this.destination = StringsUtils.validateWhitoutCdir(packetRequest.getDestination());
			this.protocol = packetRequest.getProtocol();
			this.shortProtocol = StringsUtils.getShortProtocol(packetRequest
					.getProtocol());
			this.portsProtocol = StringsUtils.getPortsProtocol(packetRequest
					.getProtocol());
			this.sourceBinary = StringsUtils.getIpBinary(packetRequest
					.getSource());
			this.destinationBinary = StringsUtils.getIpBinary(packetRequest
					.getDestination());
		} catch (Exception exception) {
			log.error("ERROR ON MESAGGE BODY FROM REQUEST. PLEASE CHECK THIS DATA: ");
			log.error("source:  " + packetRequest.getSource());
			log.error("destination:  " + packetRequest.getDestination());
			log.error("protocol:  " + packetRequest
					.getProtocol());
			throw new RuntimeException();
		}
	}

	/**
	 * id rule
	 */
	private String id;
	/**
	 * from rule
	 */
	private String source;
	/**
	 * to rule
	 */
	private String destination;
	/**
	 * protocol of rule
	 */
	private String protocol;
	/**
	 * level of rule
	 */
	private String action;
	/**
	 * from rule binary
	 */
	@XmlTransient
	private transient String sourceBinary;
	/**
	 * to rule binary
	 */
	@XmlTransient
	private transient String destinationBinary;
	/**
	 * cidr for source
	 */
	@XmlTransient
	private transient Integer sourceCidr;
	/**
	 * cdir for destination
	 */
	@XmlTransient
	private transient Integer destinationCidr;
	/**
	 * splited protocol
	 */
	@XmlTransient
	private transient String shortProtocol;
	/**
	 * ports protocol
	 */
	@XmlTransient
	private transient List<String> portsProtocol;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public void setDestinationCidr(Integer destinationCidr) {
		this.destinationCidr = destinationCidr;
	}

	@XmlTransient
	public String getSourceBinary() {
		return sourceBinary;
	}

	public void setSourceBinary(String sourceBinary) {
		this.sourceBinary = sourceBinary;
	}

	@XmlTransient
	public String getDestinationBinary() {
		return destinationBinary;
	}

	public void setDestinationBinary(String destinationBinary) {
		this.destinationBinary = destinationBinary;
	}

	@XmlTransient
	public Integer getSourceCidr() {
		return sourceCidr;
	}

	public void setSourceCidr(Integer sourceCidr) {
		this.sourceCidr = sourceCidr;
	}

	@XmlTransient
	public Integer getDestinationCidr() {
		return destinationCidr;
	}

	@XmlTransient
	public String getShortProtocol() {
		return shortProtocol;
	}

	public void setShortProtocol(String shortProtocol) {
		this.shortProtocol = shortProtocol;
	}

	@XmlTransient
	public List<String> getPortsProtocol() {
		return portsProtocol;
	}

	public void setPortsProtocol(List<String> portsProtocol) {
		this.portsProtocol = portsProtocol;
	}

}
