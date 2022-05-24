package gg.jte.generated.ondemand;
import Presentation.Model.PresentationUser;
public final class JteindexGenerated {
	public static final String JTE_NAME = "index.jte";
	public static final int[] JTE_LINE_INFO = {0,0,1,1,1,4,4,4,6,6,7,7,7,8,8,10,10,12,12,17,17,17,18,18,18,19,19,19,22,22,26};
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, PresentationUser user, java.util.List<Presentation.Model.PresentationShop> shops) {
		jteOutput.writeContent("\r\n");
		gg.jte.generated.ondemand.JtenavBarGenerated.render(jteOutput, jteHtmlInterceptor, user, "Market");
		jteOutput.writeContent("\r\n<main class=\"container\">\r\n    ");
		if (user.isLoggedIn()) {
			jteOutput.writeContent("\r\n        <h1 class=\"fs-1\">Welcome Back, ");
			jteOutput.setContext("h1", null);
			jteOutput.writeUserContent(user.getUsername());
			jteOutput.writeContent("!</h1>\r\n    ");
		} else {
			jteOutput.writeContent("\r\n        <h1 class=\"fs-1\">Welcome to the MarketPlace!</h1>\r\n    ");
		}
		jteOutput.writeContent("\r\n    <div class=\"container row mx-auto\">\r\n        ");
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
			jteOutput.writeContent("\" class=\"btn btn-primary\">Visit Shop</a>\r\n                </div>\r\n            </div>\r\n        ");
		}
		jteOutput.writeContent("\r\n    </div>\r\n</main>\r\n</body>\r\n</html>");
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		PresentationUser user = (PresentationUser)params.get("user");
		java.util.List<Presentation.Model.PresentationShop> shops = (java.util.List<Presentation.Model.PresentationShop>)params.get("shops");
		render(jteOutput, jteHtmlInterceptor, user, shops);
	}
}
