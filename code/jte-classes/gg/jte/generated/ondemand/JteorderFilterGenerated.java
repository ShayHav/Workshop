package gg.jte.generated.ondemand;
public final class JteorderFilterGenerated {
	public static final String JTE_NAME = "orderFilter.jte";
	public static final int[] JTE_LINE_INFO = {0,0,0,0,7,7,7,7,7,7,7,7,7,12,12,12,12,12,12,12,12,12,12,12,12,12,12,12,14,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,18,20,20,20,26,26,26,26,26,26,26,26,30,30,30,30,30,30,30,30,47,47,47,49};
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, String action, Double minPrice, Double maxPrice, String minDate, String maxDate) {
		jteOutput.writeContent("\r\n<div class=\"my-4 mx-4 mb-5 small container-sm\">\r\n    <form method=\"get\" id=\"filterOrders\" class=\"form border rounded border-1 w-75\"");
		if (gg.jte.runtime.TemplateUtils.isAttributeRendered(action)) {
			jteOutput.writeContent(" action=\"");
			jteOutput.setContext("form", "action");
			jteOutput.writeUserContent(action);
			jteOutput.setContext("form", null);
			jteOutput.writeContent("\"");
		}
		jteOutput.writeContent(">\r\n        <div class=\"row mb-2\">\r\n            <div class=\"col-3\">\r\n                <div class=\"mt-3 mx-3 mb-3\">\r\n                    <label for=\"minPrice\" class=\"form-label\">Min Price</label>\r\n                    <input type=\"range\" step=\"0.1\"");
		if (gg.jte.runtime.TemplateUtils.isAttributeRendered(minPrice)) {
			jteOutput.writeContent(" min=\"");
			jteOutput.setContext("input", "min");
			jteOutput.writeUserContent(minPrice);
			jteOutput.setContext("input", null);
			jteOutput.writeContent("\"");
		}
		if (gg.jte.runtime.TemplateUtils.isAttributeRendered(maxPrice)) {
			jteOutput.writeContent(" max=\"");
			jteOutput.setContext("input", "max");
			jteOutput.writeUserContent(maxPrice);
			jteOutput.setContext("input", null);
			jteOutput.writeContent("\"");
		}
		jteOutput.writeContent(" class=\"form-range\" id=\"minPrice\" name=\"minPrice\" value=\"0\"\r\n                           onChange=\"document.getElementById('rangemin').innerText = document.getElementById('minPrice').value\">\r\n                    <span id=\"rangemin\">0");
		jteOutput.writeContent("</span>\r\n                </div>\r\n                <div class=\"mt-3 mx-3 mb-3\">\r\n                    <label for=\"maxPrice\" class=\"form-label\">Max Price</label>\r\n                    <input type=\"range\" step=\"0.1\"");
		if (gg.jte.runtime.TemplateUtils.isAttributeRendered(minPrice)) {
			jteOutput.writeContent(" min=\"");
			jteOutput.setContext("input", "min");
			jteOutput.writeUserContent(minPrice);
			jteOutput.setContext("input", null);
			jteOutput.writeContent("\"");
		}
		if (gg.jte.runtime.TemplateUtils.isAttributeRendered(maxPrice)) {
			jteOutput.writeContent(" max=\"");
			jteOutput.setContext("input", "max");
			jteOutput.writeUserContent(maxPrice);
			jteOutput.setContext("input", null);
			jteOutput.writeContent("\"");
		}
		jteOutput.writeContent(" class=\"form-range\" id=\"maxPrice\" name=\"maxPrice\"");
		if (gg.jte.runtime.TemplateUtils.isAttributeRendered(maxPrice)) {
			jteOutput.writeContent(" value=\"");
			jteOutput.setContext("input", "value");
			jteOutput.writeUserContent(maxPrice);
			jteOutput.setContext("input", null);
			jteOutput.writeContent("\"");
		}
		jteOutput.writeContent("\r\n                           onChange=\"document.getElementById('rangemax').innerText = document.getElementById('maxPrice').value\">\r\n                    <span id=\"rangemax\">");
		jteOutput.setContext("span", null);
		jteOutput.writeUserContent(maxPrice);
		jteOutput.writeContent("</span>\r\n                </div>\r\n            </div>\r\n            <div class=\"col-4\">\r\n                <div class=\"mt-3 mx-3 mb-3\">\r\n                    <label for=\"minDate\" class=\"form-label\">Min Date</label>\r\n                    <input type=\"date\" name=\"minDate\" id=\"minDate\" class=\"form-control\"");
		if (gg.jte.runtime.TemplateUtils.isAttributeRendered(minDate)) {
			jteOutput.writeContent(" value=\"");
			jteOutput.setContext("input", "value");
			jteOutput.writeUserContent(minDate);
			jteOutput.setContext("input", null);
			jteOutput.writeContent("\"");
		}
		jteOutput.writeContent(">\r\n                </div>\r\n                <div class=\"mt-3 mx-3 mb-3\">\r\n                    <label for=\"maxDate\" class=\"form-label\">Max Date</label>\r\n                    <input type=\"date\" name=\"maxDate\" id=\"maxDate\" class=\"form-control\"");
		if (gg.jte.runtime.TemplateUtils.isAttributeRendered(maxDate)) {
			jteOutput.writeContent(" value=\"");
			jteOutput.setContext("input", "value");
			jteOutput.writeUserContent(maxDate);
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
		Double minPrice = (Double)params.get("minPrice");
		Double maxPrice = (Double)params.get("maxPrice");
		String minDate = (String)params.get("minDate");
		String maxDate = (String)params.get("maxDate");
		render(jteOutput, jteHtmlInterceptor, action, minPrice, maxPrice, minDate, maxDate);
	}
}
