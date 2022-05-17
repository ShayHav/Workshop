package gg.jte.generated;
import Presentation.Model.PresentationUser;
public final class JteloginGenerated {
	public static final String JTE_NAME = "login.jte";
	public static final int[] JTE_LINE_INFO = {0,0,1,1,1,53,53,53,53,99};
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, PresentationUser user) {
		jteOutput.writeContent("<!doctype html>\r\n<html lang=\"en\">\r\n<head>\r\n    <meta charset=\"UTF-8\">\r\n    <meta name=\"viewport\"\r\n          content=\"width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0\">\r\n    <meta http-equiv=\"X-UA-Compatible\" content=\"ie=edge\">\r\n    <title>SignIn</title>\r\n    <link href=\"https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css\" rel=\"stylesheet\" integrity=\"sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3\" crossorigin=\"anonymous\">\r\n</head>\r\n<body>\r\n    <nav class=\"navbar navbar-expand-lg navbar-light bg-light\">\r\n        <div class=\"container-fluid\">\r\n            <div class=\"navbar-brand\">\r\n            <svg xmlns=\"http://www.w3.org/2000/svg\" width=\"16\" height=\"16\" fill=\"currentColor\" class=\"bi bi-shop-window\" viewBox=\"0 0 16 16\">\r\n                <path d=\"M2.97 1.35A1 1 0 0 1 3.73 1h8.54a1 1 0 0 1 .76.35l2.609 3.044A1.5 1.5 0 0 1 16 5.37v.255a2.375 2.375 0 0 1-4.25 1.458A2.371 2.371 0 0 1 9.875 8 2.37 2.37 0 0 1 8 7.083 2.37 2.37 0 0 1 6.125 8a2.37 2.37 0 0 1-1.875-.917A2.375 2.375 0 0 1 0 5.625V5.37a1.5 1.5 0 0 1 .361-.976l2.61-3.045zm1.78 4.275a1.375 1.375 0 0 0 2.75 0 .5.5 0 0 1 1 0 1.375 1.375 0 0 0 2.75 0 .5.5 0 0 1 1 0 1.375 1.375 0 1 0 2.75 0V5.37a.5.5 0 0 0-.12-.325L12.27 2H3.73L1.12 5.045A.5.5 0 0 0 1 5.37v.255a1.375 1.375 0 0 0 2.75 0 .5.5 0 0 1 1 0zM1.5 8.5A.5.5 0 0 1 2 9v6h12V9a.5.5 0 0 1 1 0v6h.5a.5.5 0 0 1 0 1H.5a.5.5 0 0 1 0-1H1V9a.5.5 0 0 1 .5-.5zm2 .5a.5.5 0 0 1 .5.5V13h8V9.5a.5.5 0 0 1 1 0V13a1 1 0 0 1-1 1H4a1 1 0 0 1-1-1V9.5a.5.5 0 0 1 .5-.5z\"/>\r\n            </svg>\r\n            <button class=\"navbar-toggler\" type=\"button\" data-bs-toggle=\"collapse\" data-bs-target=\"#navbarSupportedContent\" aria-controls=\"navbarSupportedContent\" aria-expanded=\"false\" aria-label=\"Toggle navigation\">\r\n                <span class=\"navbar-toggler-icon\"></span>\r\n            </button>\r\n            </div>\r\n            <div class=\"collapse navbar-collapse\" id=\"navbarSupportedContent\">\r\n                <ul class=\"navbar-nav me-auto mb-2 mb-lg-0\">\r\n                    <li class=\"nav-item\">\r\n                        <a class=\"nav-link\" aria-current=\"page\" href=\"/\">Home</a>\r\n                    </li>\r\n                    <li class=\"nav-item\">\r\n                        <a class=\"nav-link\" href=\"#\">Link</a>\r\n                    </li>\r\n\r\n                </ul>\r\n                <div class=\"me-auto col-7\">\r\n                    <form class=\"d-flex\" method=\"get\">\r\n                        <div class=\"col-2\">\r\n                            <select class=\"form-select border-end-0 rounded-start\" name=\"searchBy\" aria-label=\"Disabled select example\">\r\n                                <option selected value=\"product\">Product's Name</option>\r\n                                <option value=\"shop\">Shop's Name</option>\r\n                                <option value=\"category\">Category</option>\r\n                                <option value=\"keyword\">Keyword</option>\r\n                            </select>\r\n                        </div>\r\n                        <input class=\"form-control me-2\" type=\"search\" placeholder=\"Search\" aria-label=\"Search\">\r\n                        <button class=\"btn btn-outline-success\" type=\"submit\">Search</button>\r\n                    </form>\r\n                </div>\r\n                <div class=\"col-2\">\r\n                    <ul class=\"navbar-nav me-auto mb-2 mb-lg-0\">\r\n                        <li class=\"nav-item\">\r\n                            <a class=\"nav-link active\" aria-current=\"page\" href=\"/users/login\">Sign in</a>\r\n                        </li>\r\n                        <li class=\"nav-item\">\r\n                            <a class=\"nav-link\" href=\"/users/");
		jteOutput.setContext("a", "href");
		jteOutput.writeUserContent(user.getUsername());
		jteOutput.writeContent("/cart\">\r\n                                <svg xmlns=\"http://www.w3.org/2000/svg\" width=\"16\" height=\"16\" fill=\"currentColor\" class=\"bi bi-cart-check\" viewBox=\"0 0 16 16\">\r\n                                    <path d=\"M11.354 6.354a.5.5 0 0 0-.708-.708L8 8.293 6.854 7.146a.5.5 0 1 0-.708.708l1.5 1.5a.5.5 0 0 0 .708 0l3-3z\"/>\r\n                                    <path d=\"M.5 1a.5.5 0 0 0 0 1h1.11l.401 1.607 1.498 7.985A.5.5 0 0 0 4 12h1a2 2 0 1 0 0 4 2 2 0 0 0 0-4h7a2 2 0 1 0 0 4 2 2 0 0 0 0-4h1a.5.5 0 0 0 .491-.408l1.5-8A.5.5 0 0 0 14.5 3H2.89l-.405-1.621A.5.5 0 0 0 2 1H.5zm3.915 10L3.102 4h10.796l-1.313 7h-8.17zM6 14a1 1 0 1 1-2 0 1 1 0 0 1 2 0zm7 0a1 1 0 1 1-2 0 1 1 0 0 1 2 0z\"/>\r\n                                </svg>\r\n                            </a>\r\n                        </li>\r\n                    </ul>\r\n                </div>\r\n            </div>\r\n        </div>\r\n    </nav>\r\n    <main class=\"container align-content-center\">\r\n        <div class=\"container col-5 mt-5 border\">\r\n            <div class=\"container col-4 mt-3\">\r\n                <h1 class=\"fs-2 fw-bold\">Sign-In</h1>\r\n            </div>\r\n            <div class=\"container mb-5\">\r\n                <form method=\"post\" action=\"/users/login\" class=\"form\">\r\n                    <div class=\"mb-3\">\r\n                        <label for=\"userName\" class=\"form-label\">Username</label>\r\n                        <input type=\"text\" name=\"username\" class=\"form-control\" id=\"userName\" placeholder=\"username\">\r\n                    </div>\r\n                    <div class=\"mb-3\">\r\n                        <label for=\"password\" class=\"form-label\">Password</label>\r\n                        <input type=\"password\" name=\"password\" class=\"form-control\" id=\"password\" placeholder=\"password\">\r\n                    </div>\r\n                    <div class=\"mb-3\">\r\n                        <button type=\"submit\" class=\"btn btn-success\">Login</button>\r\n                        <a href=\"/\" class=\"btn btn-danger\">Cancel</a>\r\n                    </div>\r\n                </form>\r\n            </div>\r\n        </div>\r\n        <div class=\"container col-4 mt-3 border-top\">\r\n            <div class=\"container align-content-center mt-3 col-6\">\r\n                <div class=\"container\">\r\n                    <h2 class=\"fs-5 mb-3\">New to Market?</h2>\r\n                </div>\r\n                <div class=\"container ps-6\">\r\n                    <a href=\"/users/new\" class=\"btn btn-warning\">Register</a>\r\n                </div>\r\n            </div>\r\n        </div>\r\n    </main>\r\n</body>\r\n</html>");
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		PresentationUser user = (PresentationUser)params.get("user");
		render(jteOutput, jteHtmlInterceptor, user);
	}
}