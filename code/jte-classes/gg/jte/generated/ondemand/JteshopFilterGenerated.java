package gg.jte.generated.ondemand;
public final class JteshopFilterGenerated {
	public static final String JTE_NAME = "shopFilter.jte";
	public static final int[] JTE_LINE_INFO = {0,0,0,0,4,4,4,4,4,4,4,4,4,9,9,9,9,9,9,9,9,26,26,26,28};
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, String action, String filteredName) {
		jteOutput.writeContent("\r\n<div class=\"my-4 mx-4 mb-5 small container\">\r\n    <form method=\"get\" id=\"filterShops\" class=\"form border rounded border-1 w-75\"");
		if (gg.jte.runtime.TemplateUtils.isAttributeRendered(action)) {
			jteOutput.writeContent(" action=\"");
			jteOutput.setContext("form", "action");
			jteOutput.writeUserContent(action);
			jteOutput.setContext("form", null);
			jteOutput.writeContent("\"");
		}
		jteOutput.writeContent(">\r\n        <div class=\"row mb-2\">\r\n            <div class=\"col-4\">\r\n                <div class=\"mt-3 mx-3 mb-3\">\r\n                    <label for=\"shopName\" class=\"form-label\">Shop name</label>\r\n                    <input type=\"text\" name=\"shopName\" id=\"shopName\" class=\"form-control\" placeholder=\"shop name to filter\"");
		if (gg.jte.runtime.TemplateUtils.isAttributeRendered(filteredName)) {
			jteOutput.writeContent(" value=\"");
			jteOutput.setContext("input", "value");
			jteOutput.writeUserContent(filteredName);
			jteOutput.setContext("input", null);
			jteOutput.writeContent("\"");
		}
		jteOutput.writeContent(">\r\n                </div>\r\n            </div>\r\n        </div>\r\n        <div class=\"row col-4\">\r\n            <div class=\"mb-2 mx-2 col\">\r\n                <button class=\"btn btn-primary\" type=\"submit\">Filter results</button>\r\n            </div>\r\n            <div class=\"mb-2 col\">\r\n                <button type=\"button\" id=\"resetFilter\" class=\"btn btn-primary\">Reset Filter</button>\r\n            </div>\r\n        </div>\r\n    </form>\r\n</div>\r\n\r\n<script>\r\n    const reset_button = document.getElementById('resetFilter');\r\n    reset_button.addEventListener('click',()=> window.location.replace(\"");
		jteOutput.setContext("script", null);
		jteOutput.writeUserContent(action);
		jteOutput.writeContent("\"));\r\n</script>\r\n");
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		String action = (String)params.get("action");
		String filteredName = (String)params.get("filteredName");
		render(jteOutput, jteHtmlInterceptor, action, filteredName);
	}
}
