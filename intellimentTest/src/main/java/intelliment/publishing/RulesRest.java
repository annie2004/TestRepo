package intelliment.publishing;


public interface RulesRest {

	String getRuleById(Integer id);

	String getAllRules();

	String getRuleByJson(String paramJson);

}
