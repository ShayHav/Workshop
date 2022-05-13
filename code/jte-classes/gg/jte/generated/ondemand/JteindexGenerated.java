package gg.jte.generated.ondemand;
import Presentation.Model.PresentationUser;
public final class JteindexGenerated {
	public static final String JTE_NAME = "index.jte";
	public static final int[] JTE_LINE_INFO = {0,0,1,1,1,15,15,15,19,19,24,24,24,25,25,25,26,26,26,29,29,33};
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, PresentationUser user, java.util.List<Presentation.Model.PresentationShop> shops) {
		jteOutput.writeContent("<!doctype html>\r\n<html lang=\"en\">\r\n<head>\r\n    <meta charset=\"UTF-8\">\r\n    <meta name=\"viewport\"\r\n          content=\"width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0\">\r\n    <meta http-equiv=\"X-UA-Compatible\" content=\"ie=edge\">\r\n    <title>WorkShop</title>\r\n    <link href=\"https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css\" rel=\"stylesheet\"\r\n          integrity=\"sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3\" crossorigin=\"anonymous\">\r\n</head>\r\n<body>\r\n");
		gg.jte.generated.ondemand.JtenavBarGenerated.render(jteOutput, jteHtmlInterceptor, user);
		jteOutput.writeContent("\r\n<main class=\"container\">\r\n    <h1 class=\"fs-1\">Welcome to the MarketPlace!</h1>\r\n    <div class=\"container row mx-auto\">\r\n        ");
		for (Presentation.Model.PresentationShop shop: shops) {
			jteOutput.writeContent("\r\n            <div class=\"card m-1\" style=\"width: 18rem\">\r\n                <img src=\"https://cdn.pixabay.com/photo/2017/08/07/19/46/shop-2607121_960_720.jpg\"\r\n                     class=\"card-img-top\" alt=\"shop front image\">\r\n                <div class=\"card-body\">\r\n                    <h2 class=\"card-title\">");
			jteOutput.setContext("h2", null);
			jteOutput.writeUserContent(shop.name);
			jteOutput.writeContent("</h2>\r\n                    <p class=\"card-text\">");
			jteOutput.setContext("p", null);
			jteOutput.writeUserContent(shop.description);
			jteOutput.writeContent("</p>\r\n                    <a href=\"/shops/");
			jteOutput.setContext("a", "href");
			jteOutput.writeUserContent(shop.id);
			jteOutput.writeContent("/products\" class=\"btn btn-primary\">View Products</a>\r\n                </div>\r\n            </div>\r\n        ");
		}
		jteOutput.writeContent("\r\n    </div>\r\n</main>\r\n</body>\r\n</html>");
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		PresentationUser user = (PresentationUser)params.get("user");
		java.util.List<Presentation.Model.PresentationShop> shops = (java.util.List<Presentation.Model.PresentationShop>)params.get("shops");
		render(jteOutput, jteHtmlInterceptor, user, shops);
	}
}
