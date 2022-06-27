package gg.jte.generated.ondemand;
public final class JtenavBarGenerated {
	public static final String JTE_NAME = "navBar.jte";
	public static final int[] JTE_LINE_INFO = {0,0,0,0,12,12,12,12,16,16,16,16,16,16,23,23,23,23,23,23,50,50,57,57,59,59,77,77,77,93,93,96,96,97,97,97,99,99,101,101,101,102,102,102,103,103,105,105,105,106,106,106,108,108,108,111,111,117,117,117,132,132,145,145,146};
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, Presentation.Model.PresentationUser user, String title) {
		jteOutput.writeContent("<!doctype html>\r\n<html lang=\"en\" xmlns=\"http://www.w3.org/1999/html\">\r\n<head>\r\n    <meta charset=\"UTF-8\">\r\n    <meta name=\"viewport\"\r\n          content=\"width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0\">\r\n    <meta http-equiv=\"X-UA-Compatible\" content=\"ie=edge\">\r\n    <script src=\"https://cdn.jsdelivr.net/npm/bootstrap@5.0.1/dist/js/bootstrap.min.js\"\r\n            integrity=\"sha384-Atwg2Pkwv9vp0ygtn1JAojH0nYbwNJLPhwyoVbhoPwBhjQPR5VtM2+xf0Uwh9KtT\"\r\n            crossorigin=\"anonymous\"></script>\r\n    <title>");
		jteOutput.setContext("title", null);
		jteOutput.writeUserContent(title);
		jteOutput.writeContent("</title>\r\n    <link href=\"https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css\" rel=\"stylesheet\"\r\n          integrity=\"sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3\" crossorigin=\"anonymous\">\r\n    <script>\r\n        const messagesSocket = new WebSocket('ws://localhost:");
		jteOutput.setContext("script", null);
		jteOutput.writeUserContent(Presentation.Main.port);
		jteOutput.writeContent("/users/");
		jteOutput.setContext("script", null);
		jteOutput.writeUserContent(user.getUsername());
		jteOutput.writeContent("/messages/getCount');\r\n        messagesSocket.onmessage = (event) => {\r\n            console.log(event.data);\r\n            let count = event.data;\r\n            let counter = document.querySelector('#messageCount');\r\n            counter.textContent = count === 0 || count === '0' ? '' : count;\r\n        }\r\n        const activeUserSocket = new WebSocket('ws://localhost:");
		jteOutput.setContext("script", null);
		jteOutput.writeUserContent(Presentation.Main.port);
		jteOutput.writeContent("/users/");
		jteOutput.setContext("script", null);
		jteOutput.writeUserContent(user.getUsername());
		jteOutput.writeContent("/active');\r\n\r\n        setInterval(()=>{\r\n            activeUserSocket.send(\"I am active\");\r\n        }, 10000);\r\n\r\n    </script>\r\n</head>\r\n<body>\r\n<nav class=\"sticky-top navbar navbar-expand-lg navbar-dark bg-dark\">\r\n    <div class=\"container-fluid\">\r\n        <div class=\"navbar-brand\">\r\n            <svg xmlns=\"http://www.w3.org/2000/svg\" width=\"25\" height=\"25\" fill=\"currentColor\" class=\"bi bi-shop-window\"\r\n                 viewBox=\"0 0 16 16\">\r\n                <path d=\"M2.97 1.35A1 1 0 0 1 3.73 1h8.54a1 1 0 0 1 .76.35l2.609 3.044A1.5 1.5 0 0 1 16 5.37v.255a2.375 2.375 0 0 1-4.25 1.458A2.371 2.371 0 0 1 9.875 8 2.37 2.37 0 0 1 8 7.083 2.37 2.37 0 0 1 6.125 8a2.37 2.37 0 0 1-1.875-.917A2.375 2.375 0 0 1 0 5.625V5.37a1.5 1.5 0 0 1 .361-.976l2.61-3.045zm1.78 4.275a1.375 1.375 0 0 0 2.75 0 .5.5 0 0 1 1 0 1.375 1.375 0 0 0 2.75 0 .5.5 0 0 1 1 0 1.375 1.375 0 1 0 2.75 0V5.37a.5.5 0 0 0-.12-.325L12.27 2H3.73L1.12 5.045A.5.5 0 0 0 1 5.37v.255a1.375 1.375 0 0 0 2.75 0 .5.5 0 0 1 1 0zM1.5 8.5A.5.5 0 0 1 2 9v6h12V9a.5.5 0 0 1 1 0v6h.5a.5.5 0 0 1 0 1H.5a.5.5 0 0 1 0-1H1V9a.5.5 0 0 1 .5-.5zm2 .5a.5.5 0 0 1 .5.5V13h8V9.5a.5.5 0 0 1 1 0V13a1 1 0 0 1-1 1H4a1 1 0 0 1-1-1V9.5a.5.5 0 0 1 .5-.5z\"/>\r\n            </svg>\r\n            <button class=\"navbar-toggler\" type=\"button\" data-bs-toggle=\"collapse\"\r\n                    data-bs-target=\"#navbarSupportedContent\" aria-controls=\"navbarSupportedContent\"\r\n                    aria-expanded=\"false\" aria-label=\"Toggle navigation\">\r\n                <span class=\"navbar-toggler-icon\"></span>\r\n            </button>\r\n        </div>\r\n        <div class=\"collapse navbar-collapse\" id=\"navbarSupportedContent\">\r\n            <ul class=\"navbar-nav me-auto mb-2 mb-lg-0\">\r\n                <li class=\"nav-item\">\r\n                    <a class=\"nav-link\" aria-current=\"page\" href=\"/\">Home</a>\r\n                </li>\r\n                ");
		if (user.isLoggedIn()) {
			jteOutput.writeContent("\r\n                    <li class=\"nav-item\">\r\n                        <button id=\"createShopBtn\" data-bs-toggle=\"collapse\" data-bs-target=\"#createShop\"\r\n                                aria-expanded=\"false\" aria-controls=\"createShop\" class=\"nav-link bg-dark border-0\">\r\n                            Create Shop\r\n                        </button>\r\n                    </li>\r\n                ");
		} else {
			jteOutput.writeContent("\r\n                    <a class=\"nav-link\" href=\"/users/login\">Create Shop</a>\r\n                ");
		}
		jteOutput.writeContent("\r\n            </ul>\r\n            <div class=\"me-auto col-6\">\r\n                <form class=\"d-flex\" method=\"get\" action=\"/search\">\r\n                    <div class=\"col-3\">\r\n                        <select class=\"form-select border-end-0 rounded-start\" name=\"searchBy\"\r\n                                aria-label=\"Disabled select example\">\r\n                            <option selected value=\"products\">Product's Name</option>\r\n                            <option value=\"category\">Category</option>\r\n                            <option value=\"keyword\">Keyword</option>\r\n                        </select>\r\n                    </div>\r\n                    <input class=\"form-control me-2\" name=\"query\" type=\"search\" placeholder=\"Search\"\r\n                           aria-label=\"Search\">\r\n                    <button class=\"btn btn-outline-success\" type=\"submit\">Search</button>\r\n                </form>\r\n            </div>\r\n            <div class=\"me-3\">\r\n                <a href=\"/users/");
		jteOutput.setContext("a", "href");
		jteOutput.writeUserContent(user.getUsername());
		jteOutput.writeContent("/messages\" class=\"nav-link bg-dark position-relative\">\r\n                  <svg xmlns=\"http://www.w3.org/2000/svg\" width=\"25\" height=\"25\" fill=\"currentColor\" class=\"bi bi-envelope\" viewBox=\"0 0 16 16\">\r\n                        <path d=\"M0 4a2 2 0 0 1 2-2h12a2 2 0 0 1 2 2v8a2 2 0 0 1-2 2H2a2 2 0 0 1-2-2V4Zm2-1a1 1 0 0 0-1 1v.217l7 4.2 7-4.2V4a1 1 0 0 0-1-1H2Zm13 2.383-4.708 2.825L15 11.105V5.383Zm-.034 6.876-5.64-3.471L8 9.583l-1.326-.795-5.64 3.47A1 1 0 0 0 2 13h12a1 1 0 0 0 .966-.741ZM1 11.105l4.708-2.897L1 5.383v5.722Z\"/>\r\n                    </svg>\r\n                    <span id='messageCount'\r\n                          class=\"position-absolute top-35 start-100 translate-middle badge rounded-pill bg-danger\"></span>\r\n                </a>\r\n            </div>\r\n            <div class=\"col-2\">\r\n                <ul class=\"navbar-nav me-auto mb-2 mb-lg-0\">\r\n                    <div class=\"dropdown\">\r\n                        <button class=\"nav-link bg-dark border-0 dropdown-toggle\" type=\"button\" id=\"dropdownMenuButton2\"\r\n                                data-bs-toggle=\"dropdown\" aria-expanded=\"false\">\r\n                            Account\r\n                        </button>\r\n                        <ul class=\"dropdown-menu dropdown-menu-dark\" aria-labelledby=\"dropdownMenuButton2\">\r\n                            ");
		if (!user.isLoggedIn()) {
			jteOutput.writeContent("\r\n                                <li><a class=\"dropdown-item-text\" href=\"/users/login\">Sign in</a></li>\r\n\r\n                            ");
		} else {
			jteOutput.writeContent("\r\n                                <li><p class=\"dropdown-item-text\">Signed in as <span style=\"font-weight: bold; color: #59c03c \">");
			jteOutput.setContext("span", null);
			jteOutput.writeUserContent(user.getUsername());
			jteOutput.writeContent("</span></p></li>\r\n\r\n                                ");
			if (user.isAdmin()) {
				jteOutput.writeContent("\r\n                                    <li><hr class=\"dropdown-divider\"></li>\r\n                                    <li><a class=\"dropdown-item\" href=\"/admin/");
				jteOutput.setContext("a", "href");
				jteOutput.writeUserContent(user.getUsername());
				jteOutput.writeContent("/systemMonitor\">System Information</a></li>\r\n                                    <li><a class=\"dropdown-item\" href=\"/admin/");
				jteOutput.setContext("a", "href");
				jteOutput.writeUserContent(user.getUsername());
				jteOutput.writeContent("/sales\">All Sales History</a></li>\r\n                                ");
			}
			jteOutput.writeContent("\r\n                                <li><hr class=\"dropdown-divider\"></li>\r\n                                <li><a class=\"dropdown-item\" href=\"/users/");
			jteOutput.setContext("a", "href");
			jteOutput.writeUserContent(user.getUsername());
			jteOutput.writeContent("/orders\">My Orders</a></li>\r\n                                <li><a class=\"dropdown-item\" href=\"/users/");
			jteOutput.setContext("a", "href");
			jteOutput.writeUserContent(user.getUsername());
			jteOutput.writeContent("/shops\">My Shops</a></li>\r\n                                <li><hr class=\"dropdown-divider\"></li>\r\n                                <form method=\"post\" action=\"/users/");
			jteOutput.setContext("form", "action");
			jteOutput.writeUserContent(user.getUsername());
			jteOutput.writeContent("/logout\">\r\n                                    <button class=\"dropdown-item\">Logout</button>\r\n                                </form>\r\n                            ");
		}
		jteOutput.writeContent("\r\n\r\n\r\n                        </ul>\r\n                    </div>\r\n                    <li class=\"nav-item\">\r\n                        <a class=\"nav-link\" href=\"/users/");
		jteOutput.setContext("a", "href");
		jteOutput.writeUserContent(user.getUsername());
		jteOutput.writeContent("/cart\">\r\n                            <svg xmlns=\"http://www.w3.org/2000/svg\" width=\"25\" height=\"25\" fill=\"currentColor\"\r\n                                 class=\"bi bi-cart-check\" viewBox=\"0 0 16 16\">\r\n                                <path d=\"M11.354 6.354a.5.5 0 0 0-.708-.708L8 8.293 6.854 7.146a.5.5 0 1 0-.708.708l1.5 1.5a.5.5 0 0 0 .708 0l3-3z\"/>\r\n                                <path d=\"M.5 1a.5.5 0 0 0 0 1h1.11l.401 1.607 1.498 7.985A.5.5 0 0 0 4 12h1a2 2 0 1 0 0 4 2 2 0 0 0 0-4h7a2 2 0 1 0 0 4 2 2 0 0 0 0-4h1a.5.5 0 0 0 .491-.408l1.5-8A.5.5 0 0 0 14.5 3H2.89l-.405-1.621A.5.5 0 0 0 2 1H.5zm3.915 10L3.102 4h10.796l-1.313 7h-8.17zM6 14a1 1 0 1 1-2 0 1 1 0 0 1 2 0zm7 0a1 1 0 1 1-2 0 1 1 0 0 1 2 0z\"/>\r\n                            </svg>\r\n                        </a>\r\n                    </li>\r\n\r\n                </ul>\r\n            </div>\r\n        </div>\r\n    </div>\r\n</nav>\r\n\r\n");
		if (user.isLoggedIn()) {
			jteOutput.writeContent("\r\n<div class=\"collapse container border border-1 mt-3 col-6 rounded\" id=\"createShop\">\r\n    <form class=\"mt-3\" method=\"post\" action=\"/shops\">\r\n        <h3 class=\"fs-5\">create new shop in market:</h3>\r\n        <div class=\" mb-2\">\r\n            <input type=\"text\" name=\"shopName\" class=\"form-control\" id=\"shopName\" placeholder=\"shopName\">\r\n        </div>\r\n        <div class=\"mb-2\">\r\n            <input type=\"text\" name=\"description\" class=\"form-control\" id=\"description\" placeholder=\"description\">\r\n        </div>\r\n        <button class=\"btn btn-success\" type=\"submit\">create new shop</button>\r\n    </form>\r\n</div>\r\n");
		}
		jteOutput.writeContent("\r\n");
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		Presentation.Model.PresentationUser user = (Presentation.Model.PresentationUser)params.get("user");
		String title = (String)params.get("title");
		render(jteOutput, jteHtmlInterceptor, user, title);
	}
}
