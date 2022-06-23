package gg.jte.generated.ondemand;
import Presentation.Model.PresentationUser;
public final class JteloginGenerated {
	public static final String JTE_NAME = "login.jte";
	public static final int[] JTE_LINE_INFO = {0,0,1,1,1,3,3,3,38};
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, PresentationUser user) {
		jteOutput.writeContent("\r\n");
		gg.jte.generated.ondemand.JtenavBarGenerated.render(jteOutput, jteHtmlInterceptor, user, "Sign in");
		jteOutput.writeContent("\r\n<main class=\"container align-content-center\">\r\n    <div class=\"container col-5 mt-5 border\">\r\n        <div class=\"container col-4 mt-3\">\r\n            <h1 class=\"fs-2 fw-bold\">Sign-In</h1>\r\n        </div>\r\n        <div class=\"container mb-5\">\r\n            <form method=\"post\" action=\"/users/login\" class=\"form\">\r\n                <div class=\"mb-3\">\r\n                    <label for=\"userName\" class=\"form-label\">Username</label>\r\n                    <input type=\"text\" name=\"username\" class=\"form-control\" id=\"userName\" placeholder=\"username\" required>\r\n                </div>\r\n                <div class=\"mb-3\">\r\n                    <label for=\"password\" class=\"form-label\">Password</label>\r\n                    <input type=\"password\" name=\"password\" class=\"form-control\" id=\"password\" placeholder=\"password\" required>\r\n                </div>\r\n                <div class=\"mb-3\">\r\n                    <button type=\"submit\" class=\"btn btn-success\">Login</button>\r\n                    <a href=\"/\" class=\"btn btn-danger\">Cancel</a>\r\n                </div>\r\n            </form>\r\n        </div>\r\n    </div>\r\n    <div class=\"container col-4 mt-3 border-top\">\r\n        <div class=\"container align-content-center mt-3 col-6\">\r\n            <div class=\"container\">\r\n                <h2 class=\"fs-5 mb-3\">New to Market?</h2>\r\n            </div>\r\n            <div class=\"container ps-6\">\r\n                <a href=\"/users/new\" class=\"btn btn-warning btn-lg\">Register</a>\r\n            </div>\r\n        </div>\r\n    </div>\r\n</main>\r\n</body>\r\n</html>");
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		PresentationUser user = (PresentationUser)params.get("user");
		render(jteOutput, jteHtmlInterceptor, user);
	}
}
