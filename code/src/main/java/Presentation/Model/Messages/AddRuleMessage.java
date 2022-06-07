package Presentation.Model.Messages;

public class AddRuleMessage {

    String ruleType;
    String ruleBase;
    String productOrCategory;

    public AddRuleMessage(){}

    public String getProductOrCategory() {
        return productOrCategory;
    }

    public String getRuleBase() {
        return ruleBase;
    }

    public String getRuleType() {
        return ruleType;
    }

    public void setProductOrCategory(String productOrCategory) {
        this.productOrCategory = productOrCategory;
    }

    public void setRuleBase(String ruleBase) {
        this.ruleBase = ruleBase;
    }

    public void setRuleType(String ruleType) {
        this.ruleType = ruleType;
    }
}
