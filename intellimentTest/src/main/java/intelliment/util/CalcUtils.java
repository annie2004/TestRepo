package intelliment.util;

import intelliment.dto.RuleDto;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.net.util.SubnetUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CalcUtils {
	
	private static final Logger log = LoggerFactory
			.getLogger(CalcUtils.class);

	public static RuleDto getMatchRule(List<RuleDto> lstRules,
			RuleDto requestRule) {
		boolean matchRule = false;
		boolean find = false;
		RuleDto result = null;
		Iterator<RuleDto> i = lstRules.iterator();
		log.info("INIT SEARCHING RULE......................................");
		while (i.hasNext() && !find) {
			RuleDto txtRule = (RuleDto) i.next();

			matchRule = matchingSource(txtRule, requestRule);

			if (matchRule) {
				matchRule = matchingDestination(txtRule, requestRule);
			}

			if (matchRule) {
				matchRule = matchingProtocol(txtRule, requestRule);
			}

			if (matchRule) {
				result = txtRule;
				find = true;
				log.info("RULE MATCH!......................................");
			}

		}
		return result;
	}

	/**
	 * Search match for source request into file
	 * 
	 * @param txtRule
	 * @param requestRule
	 * @return matched
	 */
	private static boolean matchingSource(final RuleDto txtRule,
			final RuleDto requestRule) {
		boolean matchRule = false;
		if (requestRule.getSource().equalsIgnoreCase(Constants.ANY)) {
			matchRule = true;
		} else {
			if (!txtRule.getSource().equalsIgnoreCase(Constants.ANY)) {
				SubnetUtils subSource = new SubnetUtils(txtRule.getSource());
				subSource.setInclusiveHostCount(true);
				if (subSource.getInfo().isInRange(requestRule.getSource())) {
					matchRule = true;
				}
			} else {
				matchRule = true;
			}
		}
		return matchRule;
	}

	/**
	 * Search match for destination request into file
	 * 
	 * @param txtRule
	 * @param requestRule
	 * @return matched
	 */
	private static boolean matchingDestination(final RuleDto txtRule,
			final RuleDto requestRule) {
		boolean matchRule = false;
		if (requestRule.getDestination().equalsIgnoreCase(Constants.ANY)) {
			matchRule = true;
		} else {
			if (!txtRule.getDestination().equalsIgnoreCase(Constants.ANY)) {
				SubnetUtils subDestination = new SubnetUtils(
						txtRule.getDestination());
				subDestination.setInclusiveHostCount(true);
				if (subDestination.getInfo().isInRange(
						requestRule.getDestination())) {
					matchRule = true;
				} else {
					matchRule = false;
				}

			} else if (txtRule.getDestination().equalsIgnoreCase(Constants.ANY)) {
				matchRule = true;
			} else {
				matchRule = false;
			}
		}
		return matchRule;
	}

	/**
	 * Search match for protocol and ports request into file
	 * 
	 * @param txtRule
	 * @param requestRule
	 * @return matched
	 */
	private static boolean matchingProtocol(final RuleDto txtRule,
			final RuleDto requestRule) {
		boolean matchRule = false;
		if (requestRule.getProtocol().equalsIgnoreCase(Constants.ANY)) {
			matchRule = true;
		} else if (!txtRule.getProtocol().equalsIgnoreCase(Constants.ANY)) {

			if (requestRule.getShortProtocol().equalsIgnoreCase(Constants.ANY)) {
				matchRule = true;
			} else {
				if (txtRule.getShortProtocol().equalsIgnoreCase(
						requestRule.getShortProtocol())) {
					matchRule = true;
				} else {
					matchRule = false;
				}
			}
			if (!Collections.disjoint(txtRule.getPortsProtocol(),
					requestRule.getPortsProtocol())
					|| txtRule.getPortsProtocol().get(0).equals(Constants.ANY)) {
				matchRule = true;
			} else {
				matchRule = false;
			}

		} else if (txtRule.getShortProtocol().equalsIgnoreCase(Constants.ANY)) {
			matchRule = true;
		} else {
			matchRule = false;
		}

		return matchRule;
	}
}
