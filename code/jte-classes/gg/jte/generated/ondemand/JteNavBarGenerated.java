package gg.jte.generated.ondemand;
public final class JtenavBarGenerated {
	public static final String JTE_NAME = "navBar.jte";
	public static final int[] JTE_LINE_INFO = {0,0,0,0,19,19,19,25,25,27,27,46,46,50,50,51,51,51,54,54,56,56,56,91};
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, Presentation.Model.PresentationUser user) {
		jteOutput.writeContent("<nav class=\"navbar navbar-expand-lg navbar-dark bg-dark\">\r\n    <div class=\"container-fluid\">\r\n        <div class=\"navbar-brand\">\r\n            <svg xmlns=\"http://www.w3.org/2000/svg\" width=\"16\" height=\"16\" fill=\"currentColor\" class=\"bi bi-shop-window\"\r\n                 viewBox=\"0 0 16 16\">\r\n                <path d=\"M2.97 1.35A1 1 0 0 1 3.73 1h8.54a1 1 0 0 1 .76.35l2.609 3.044A1.5 1.5 0 0 1 16 5.37v.255a2.375 2.375 0 0 1-4.25 1.458A2.371 2.371 0 0 1 9.875 8 2.37 2.37 0 0 1 8 7.083 2.37 2.37 0 0 1 6.125 8a2.37 2.37 0 0 1-1.875-.917A2.375 2.375 0 0 1 0 5.625V5.37a1.5 1.5 0 0 1 .361-.976l2.61-3.045zm1.78 4.275a1.375 1.375 0 0 0 2.75 0 .5.5 0 0 1 1 0 1.375 1.375 0 0 0 2.75 0 .5.5 0 0 1 1 0 1.375 1.375 0 1 0 2.75 0V5.37a.5.5 0 0 0-.12-.325L12.27 2H3.73L1.12 5.045A.5.5 0 0 0 1 5.37v.255a1.375 1.375 0 0 0 2.75 0 .5.5 0 0 1 1 0zM1.5 8.5A.5.5 0 0 1 2 9v6h12V9a.5.5 0 0 1 1 0v6h.5a.5.5 0 0 1 0 1H.5a.5.5 0 0 1 0-1H1V9a.5.5 0 0 1 .5-.5zm2 .5a.5.5 0 0 1 .5.5V13h8V9.5a.5.5 0 0 1 1 0V13a1 1 0 0 1-1 1H4a1 1 0 0 1-1-1V9.5a.5.5 0 0 1 .5-.5z\"/>\r\n            </svg>\r\n            <button class=\"navbar-toggler\" type=\"button\" data-bs-toggle=\"collapse\"\r\n                    data-bs-target=\"#navbarSupportedContent\" aria-controls=\"navbarSupportedContent\"\r\n                    aria-expanded=\"false\" aria-label=\"Toggle navigation\">\r\n                <span class=\"navbar-toggler-icon\"></span>\r\n            </button>\r\n        </div>\r\n        <div class=\"collapse navbar-collapse\" id=\"navbarSupportedContent\">\r\n            <ul class=\"navbar-nav me-auto mb-2 mb-lg-0\">\r\n                <li class=\"nav-item\">\r\n                    <a class=\"nav-link\" aria-current=\"page\" href=\"/\">Home</a>\r\n                </li>\r\n                ");
		if (user.isLoggedIn()) {
			jteOutput.writeContent("\r\n                <li class=\"nav-item\">\r\n                    <button id=\"createShopBtn\" class=\"nav-link bg-dark border-0\">\r\n                    Create Shop\r\n                    </button>\r\n                </li>\r\n                    ");
		} else {
			jteOutput.writeContent("\r\n                    <a class=\"nav-link\" href=\"/users/login\">Create Shop</a>\r\n                ");
		}
		jteOutput.writeContent("\r\n            </ul>\r\n            <div class=\"me-auto col-7\">\r\n                <form class=\"d-flex\" method=\"get\">\r\n                    <div class=\"col-2\">\r\n                        <select class=\"form-select border-end-0 rounded-start\" name=\"searchBy\"\r\n                                aria-label=\"Disabled select example\">\r\n                            <option selected value=\"product\">Product's Name</option>\r\n                            <option value=\"shop\">Shop's Name</option>\r\n                            <option value=\"category\">Category</option>\r\n                            <option value=\"keyword\">Keyword</option>\r\n                        </select>\r\n                    </div>\r\n                    <input class=\"form-control me-2\" type=\"search\" placeholder=\"Search\" aria-label=\"Search\">\r\n                    <button class=\"btn btn-outline-success\" type=\"submit\">Search</button>\r\n                </form>\r\n            </div>\r\n            <div class=\"col-2\">\r\n                <ul class=\"navbar-nav me-auto mb-2 mb-lg-0\">\r\n                    ");
		if (!user.isLoggedIn()) {
			jteOutput.writeContent("\r\n                        <li class=\"nav-item\">\r\n                            <a class=\"nav-link\" aria-current=\"page\" href=\"/users/login\">Sign in</a>\r\n                        </li>\r\n                    ");
		} else {
			jteOutput.writeContent("\r\n                        <form method=\"post\" action=\"/users/");
			jteOutput.setContext("form", "action");
			jteOutput.writeUserContent(user.getUsername());
			jteOutput.writeContent("/logout\">\r\n                            <button class=\"btn btn-dark text-warning\">Logout</button>\r\n                        </form>\r\n                    ");
		}
		jteOutput.writeContent("\r\n                    <li class=\"nav-item\">\r\n                        <a class=\"nav-link\" href=\"/users/");
		jteOutput.setContext("a", "href");
		jteOutput.writeUserContent(user.getUsername());
		jteOutput.writeContent("/cart\">\r\n                            <svg xmlns=\"http://www.w3.org/2000/svg\" width=\"16\" height=\"16\" fill=\"currentColor\"\r\n                                 class=\"bi bi-cart-check\" viewBox=\"0 0 16 16\">\r\n                                <path d=\"M11.354 6.354a.5.5 0 0 0-.708-.708L8 8.293 6.854 7.146a.5.5 0 1 0-.708.708l1.5 1.5a.5.5 0 0 0 .708 0l3-3z\"/>\r\n                                <path d=\"M.5 1a.5.5 0 0 0 0 1h1.11l.401 1.607 1.498 7.985A.5.5 0 0 0 4 12h1a2 2 0 1 0 0 4 2 2 0 0 0 0-4h7a2 2 0 1 0 0 4 2 2 0 0 0 0-4h1a.5.5 0 0 0 .491-.408l1.5-8A.5.5 0 0 0 14.5 3H2.89l-.405-1.621A.5.5 0 0 0 2 1H.5zm3.915 10L3.102 4h10.796l-1.313 7h-8.17zM6 14a1 1 0 1 1-2 0 1 1 0 0 1 2 0zm7 0a1 1 0 1 1-2 0 1 1 0 0 1 2 0z\"/>\r\n                            </svg>\r\n                        </a>\r\n                    </li>\r\n\r\n                </ul>\r\n            </div>\r\n        </div>\r\n    </div>\r\n</nav>\r\n<div class=\"collapse container border border-2 mt-3 col-6\" id=\"createShop\">\r\n    <form class=\"m-3\" method=\"post\" action=\"/shop\">\r\n        <h3 class=\"fs-5\">create new shop in market:</h3>\r\n        <div class=\" mb-2\">\r\n            <input type=\"text\" name=\"shopName\" class=\"form-control\" id=\"shopName\" placeholder=\"shopName\">\r\n        </div>\r\n        <div class=\"mb-2\">\r\n            <input type=\"text\" name=\"description\" class=\"form-control\" id=\"description\" placeholder=\"description\">\r\n        </div>\r\n        <button class=\"btn btn-success\" type=\"submit\">create new shop</button>\r\n    </form>\r\n</div>\r\n\r\n<script>\r\n    const button = document.querySelector(\"#createShopBtn\");\r\n    button.addEventListener('click',function (){\r\n        const toCollapse = document.querySelector(\"#createShop\");\r\n        toCollapse.classList.toggle('collapse');\r\n    })\r\n</script>\r\n\r\n");
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		Presentation.Model.PresentationUser user = (Presentation.Model.PresentationUser)params.get("user");
		render(jteOutput, jteHtmlInterceptor, user);
	}
}