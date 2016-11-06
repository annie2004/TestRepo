package intelliment.util;

import intelliment.dto.RuleDto;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class StringsUtils {

	public static String ruleDtoToStringJson(RuleDto rule) {
		Gson gson = new Gson();
		String ruleString = gson.toJson(rule);
		return ruleString;
	}

	public static String rulelistOfruleDtoToStringJson(List<RuleDto> lstRules) {
		Gson gson = new Gson();
		Type listOfRulesString = new TypeToken<List<RuleDto>>() {
		}.getType();
		String lstRulesJson = gson.toJson(lstRules, listOfRulesString);
		return lstRulesJson;
	}

	public static List<RuleDto> rulelistOfStringJsonToListRuleDto(String arrayJson) {
		Gson gson = new Gson();
		List<RuleDto> lstRules = gson.fromJson(arrayJson,
				new TypeToken<List<RuleDto>>() {
				}.getType());
		return lstRules;
	}
	
	public static RuleDto ruleStringJsonToRuleDto(String json) {
		Gson gson = new Gson();
		RuleDto rule = gson.fromJson(json,
				new TypeToken<RuleDto>() {
				}.getType());
		return rule;
	}

	/**
	 * 
	 * @param ip
	 * @return an Array of strings 0 for ip -- 1 for cidr null for any
	 * @throws Exception
	 */
	public static String[] getSeparatesIpAndCidr(String ip) throws Exception {
		String[] ipAndCidr = null;
		if (!ip.equalsIgnoreCase(Constants.ANY)) {
			String[] output = ip.split("/");
			if (output[0] == null || output[0].isEmpty()) {
				if (!ip.equalsIgnoreCase(Constants.ANY)) {
					throw new Exception();
				}
			} else {
				ipAndCidr = output;
			}
		}
		return ipAndCidr;
	}

	/**
	 * 
	 * @param ipDecimal
	 * @return ip string in format 00000000000000000000000000000000 assert 32
	 *         bits
	 * @throws Exception
	 */
	public static String getIpBinary(String ipDecimal) throws Exception {

		String[] ipAndCidr = getSeparatesIpAndCidr(ipDecimal);
		StringBuffer sb = new StringBuffer();
		if (ipAndCidr != null) {
			String[] partIp = ipAndCidr[0].split("\\.");
			if (partIp.length != 4) {
				throw new Exception();
			}
			for (int i = 0; i < 4; i++) {
				int x = new Integer(partIp[i]).intValue();
				String binaryPart = Integer.toBinaryString(x);
				// if (binaryPart.length() != 8) {
				if (binaryPart.length() < 8) {
					int zerosToAdd = 8 - binaryPart.length();
					for (int j = 0; j < zerosToAdd; j++) {
						binaryPart = "0" + binaryPart;
					}
				} else if (binaryPart.length() > 8) {
					throw new Exception();
				}
				sb.append(binaryPart);
			}
		}

		return sb.toString();
	}

	/**
	 * 
	 * @param ipDecimal
	 * @return get cidr from an ip assert ipv4
	 * @throws Exception
	 */
	public static Integer getCidr(String ipDecimal) throws Exception {
		Integer cidr = null;
		String[] ipAndCidr = getSeparatesIpAndCidr(ipDecimal);
		if (ipAndCidr != null && ipAndCidr.length > 1) {
			Integer iCidr = new Integer(ipAndCidr[1]);
			if (iCidr > 32) {
				throw new Exception();
			}
			cidr = new Integer(ipAndCidr[1]);
		}
		return cidr;
	}

	/**
	 * 
	 * @param ipDecimal
	 * @return a valid ip
	 * @throws Exception
	 */
	public static String validateWhitoutCdir(String ipDecimal) throws Exception {
		String[] ipAndCidr = getSeparatesIpAndCidr(ipDecimal);
		if (ipAndCidr != null && ipAndCidr.length > 1) {
			throw new Exception();
		}
		return ipDecimal;
	}

	/**
	 * 
	 * @param ipDecimal
	 * @return set default cidr to an ip
	 * @throws Exception
	 */
	public static String setDefaultCidr(String ipDecimal) throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append(ipDecimal);
		String[] ipAndCidr = getSeparatesIpAndCidr(ipDecimal);
		// If Array only have one element ip dont carry cidr (set /32 default)
		if (ipAndCidr != null && ipAndCidr.length == 1) {
			if (!ipAndCidr[0].equalsIgnoreCase(Constants.ANY)) {
				sb.append(Constants.DEFAULT_CIDR);
			}
		}
		return sb.toString();
	}

	/**
	 * 
	 * @param protocol
	 * @return protocol from an string whith format tcp/xxx,xxxx
	 * @throws Exception
	 */
	public static String getShortProtocol(String protocol) throws Exception {
		String[] output = null;
		output = protocol.split("/");
		if (output[0].isEmpty()
				|| (!output[0].equalsIgnoreCase(Constants.TCP)
						&& !output[0].equalsIgnoreCase(Constants.UDP) && !output[0]
							.equalsIgnoreCase(Constants.ANY))) {
			throw new Exception();
		}
		return output[0];
	}

	/**
	 * 
	 * @param protocol
	 * @return get ports from an string with format xxx/52571,28456,23556
	 * @throws Exception
	 */
	public static List<String> getPortsProtocol(String protocol)
			throws Exception {
		String[] output = null;
		List<String> lstPorts = new LinkedList<>();
		output = protocol.split("/");
		if (output.length > 1) {
			String[] tmpArr = output[1].split(",");
			Collections.addAll(lstPorts, tmpArr);
			if (!output[1].equalsIgnoreCase(Constants.ANY)) {
				List<String> errorPorts = lstPorts.stream()
						.filter(item -> new Integer(item) > 65535)
						.collect(Collectors.toList());
				if (errorPorts != null && errorPorts.size() > 0) {
					throw new Exception();
				}
			}
		}

		return lstPorts;
	}

	/**
	 * 
	 * @param action
	 * @return assert action only allow and deny
	 * @throws Exception
	 */
	public static String validateAction(String action) throws Exception {
		if (!action.equalsIgnoreCase(Constants.ALLOW)
				&& !action.equalsIgnoreCase(Constants.DENY)) {
			throw new Exception();
		}
		return action;
	}

	/**
	 * Check a valid Integer
	 * 
	 * @param id
	 * @return string id
	 * @throws Exception
	 */
	public static String validateIntegerId(String id) throws Exception {
		// Only validate an Integer Id. If not throws exception
		@SuppressWarnings("unused")
		Integer tmpId = new Integer(id);
		return id;
	}
}
