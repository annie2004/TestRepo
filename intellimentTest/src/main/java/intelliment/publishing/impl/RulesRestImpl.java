package intelliment.publishing.impl;

import intelliment.dto.RuleDto;
import intelliment.publishing.RulesRest;
import intelliment.util.CalcUtils;
import intelliment.util.Constants;
import intelliment.util.GettersConfigProperties;
import intelliment.util.StringsUtils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

@Produces("application/json")
public class RulesRestImpl implements RulesRest {
	
	private static final Logger log = LoggerFactory
			.getLogger(RulesRestImpl.class);

	@GET
	@Path("/acl/{id}")
	public String getRuleById(@PathParam("id") Integer id) {
		RuleDto rule = new RuleDto();
		try {
			final Map<Integer, RuleDto> mapRules = readRules();
			rule = mapRules.get(id);
		} catch (Exception exception) {
			log.error("---------EXCEPTION RETURNING RULE BY ID---------");
		}
		
		String ruleString = StringsUtils.ruleDtoToStringJson(rule);

		return ruleString;
	}

	@GET
	@Path("/acl")
	public String getAllRules() {
		List<RuleDto> lstRules = null;
		try {
			final Map<Integer, RuleDto> mapRules = readRules();
			lstRules = new LinkedList<RuleDto>(mapRules.values());
		} catch (Exception exception) {
			log.error("----------EXCEPTION RETURNING ALL RULES----------");
		}
		String lstRulesJson = StringsUtils.rulelistOfruleDtoToStringJson(lstRules);

		return lstRulesJson;
	}

	@POST
	@Consumes("application/json")
	@Path("/acl")
	public String getRuleByJson(String paramJson) {
		List<RuleDto> lstRules = null;
		RuleDto result = null;
		try {
			// Getting packet from request
			Gson gson = new Gson();
			JsonParser parser = new JsonParser();
			JsonObject jObject = parser.parse(paramJson).getAsJsonObject();
			RuleDto requestRule = gson.fromJson(jObject, RuleDto.class);
			// Validations for entry on construct RuleDto
			log.info("ANALYZING REQUEST BODY..................................");
			requestRule = new RuleDto(requestRule);
			log.info("REQUEST BODY OK........................................");
			// Reading from rules
			final Map<Integer, RuleDto> mapRules = readRules();
			lstRules = new LinkedList<RuleDto>(mapRules.values());
			result = CalcUtils.getMatchRule(lstRules, requestRule);

		} catch (Exception exception) {
			if (exception instanceof JsonSyntaxException) {
				log.error("----------EXCEPTION: JSONSYNTAXEXCEPTION GETTING FROM JSON REQUEST----------");
			}
			log.error("----------EXCEPTION GETTING RULE FROM JSON REQUEST----------");
		}
		String ruleString = StringsUtils.ruleDtoToStringJson(result);

		return ruleString;
	}

	/**
	 * 
	 * @return Map key = id 
	 */
	private Map<Integer, RuleDto> readRules() {

		Map<Integer, RuleDto> mapRules = new LinkedHashMap<>();
		// Map<Integer, RuleDto> mapOrdered = new LinkedHashMap<>();
		try {
			InputStream in = this.getClass().getResourceAsStream(
					GettersConfigProperties.getPropertyConfig(Constants.RUTE_FILE_RULES));
			try (BufferedReader reader = new BufferedReader(
					new InputStreamReader(in, "UTF-8"))) {

				mapRules = reader
						.lines()
						.collect(Collectors.toList())
						.stream()
						.map((s) -> s.split(" "))
						.collect(Collectors.toList())
						.stream()
						.collect(
								Collectors.toMap(x -> new Integer(x[0]),
										x -> new RuleDto(x)));
				log.info("---------LINES IN FILE: " + mapRules.size());
			}
		} catch (Exception exception) {
			log.error("---------EXCEPTION READING RULES FROM TXT---------");
			throw new RuntimeException();
		}

		return mapRules;
	}
}
