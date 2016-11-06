package intellimentTest;

import intelliment.dto.RuleDto;
import intelliment.publishing.impl.RulesRestImpl;
import intelliment.util.GettersConfigProperties;
import intelliment.util.StringsUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;

import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.cxf.jaxrs.lifecycle.SingletonResourceProvider;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestIntellimentTest extends Assert {

	private static final Logger log = LoggerFactory.getLogger(TestIntellimentTest.class);

	private final static String ENDPOINT_ADDRESS = GettersConfigProperties
			.getPropertyTest("intelliment.test.endpoint.address");
	private final static String WADL_ADDRESS = ENDPOINT_ADDRESS
			+ GettersConfigProperties
					.getPropertyTest("intelliment.test.wadl.address");
	private static final String JSON_REQUEST = GettersConfigProperties
			.getPropertyTest("intelliment.test.jsonRequest");
	private static final String EXPECTED_ID = GettersConfigProperties
			.getPropertyTest("intelliment.test.knownIdjsonResponse");
	private static final String NUM_LINES_TXT = GettersConfigProperties
			.getPropertyTest("intelliment.test.numLines.txt");
	private static Server server;

	@BeforeClass
	public static void initialize() throws Exception {
		startServer();
		waitForWADL();
	}

	private static void startServer() throws Exception {
		JAXRSServerFactoryBean sf = new JAXRSServerFactoryBean();
		sf.setResourceClasses(RulesRestImpl.class);

		List<Object> providers = new ArrayList<Object>();
		sf.setProviders(providers);

		sf.setResourceProvider(RulesRestImpl.class,
				new SingletonResourceProvider(new RulesRestImpl(), true));
		sf.setAddress(ENDPOINT_ADDRESS);

		server = sf.create();
	}

	@SuppressWarnings("static-access")
	private static void waitForWADL() throws Exception {
		WebClient client = WebClient.create(WADL_ADDRESS);
		for (int i = 0; i < 20; i++) {
			Thread.currentThread().sleep(1000);
			Response response = client.get();
			if (response.getStatus() == 200) {
				break;
			}
		}
	}

	@AfterClass
	public static void destroy() throws Exception {
		server.stop();
		server.destroy();
	}

	/**
	 * Test RulesRestImpl--> getAllRules() method
	 * Configure NUM_LINES_TXT in testConfigIntelli.properties
	 */
	@Test
	public void testGetRuleById_ResponseNotNull() {
		WebClient client = WebClient.create(ENDPOINT_ADDRESS);
		client.accept("application/json");
		client.path("/acl");
		String jsonResponse = client.get(String.class);
		List<RuleDto> lstRules = StringsUtils
				.rulelistOfStringJsonToListRuleDto(jsonResponse);
		assertNotEquals("NO RULES INTO RESPONSE.......................",
				lstRules, null);
		assertTrue("INCOMPLETE LIST OF RULES.......................",
				lstRules.size() == new Integer(NUM_LINES_TXT).intValue());
	}

	/**
	 * Test RulesRestImpl--> getRuleById(@PathParam("id") Integer id)
	 * Configure EXPECTED_ID in testConfigIntelli.properties
	 */
	@Test
	public void testGetRuleById() {
		WebClient client = WebClient.create(ENDPOINT_ADDRESS);
		client.accept("application/json");
		client.path("/acl/" + EXPECTED_ID);
		String jsonResponse = client.get(String.class);
		assertNotEquals(jsonResponse, null);
		RuleDto rule = StringsUtils.ruleStringJsonToRuleDto(jsonResponse);
		assertTrue("NO RULE FIND...................", rule != null);
		assertTrue("WRONG RULE INTO RESPONSE.................", rule.getId()
				.equals(EXPECTED_ID));
	}

	/**
	 * Test RulesRestImpl--> getRuleByJson(String paramJson) method
	 * Configure JSON_REQUEST in testConfigIntelli.properties
	 */
	@Test
	public void testSearchRuleFromJson() {
		try {
			URL url = new URL(ENDPOINT_ADDRESS + "acl");
			URLConnection con = url.openConnection();
			HttpURLConnection http = (HttpURLConnection) con;
			http.setRequestMethod("POST"); // PUT is another valid option
			http.setDoOutput(true);

			byte[] out = JSON_REQUEST.getBytes(StandardCharsets.UTF_8);
			int length = out.length;

			http.setFixedLengthStreamingMode(length);
			http.setRequestProperty("Content-Type",
					"application/json; charset=UTF-8");
			http.connect();

			try (OutputStream os = http.getOutputStream()) {
				os.write(out);
			}
			int status = http.getResponseCode();
			assertTrue("NO RESPONSE RECIVED", status == 200);

			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(http.getInputStream()));
			StringBuilder stringBuilder = new StringBuilder();

			String line = null;
			String jsonString = null;
			while ((line = bufferedReader.readLine()) != null) {
				stringBuilder.append(line + '\n');
			}

			jsonString = stringBuilder.toString();

			RuleDto rule = StringsUtils.ruleStringJsonToRuleDto(jsonString);

			assertTrue("NO RULES INTO RESPONSE...............", rule != null);
			assertTrue("NO RULE EXPECTED.................", rule.getId()
					.equals(EXPECTED_ID));

		} catch (Exception exception) {
			log.error("EXCEPTION TESTING SEARCH RULES FROM JSON");
		}
	}

}
