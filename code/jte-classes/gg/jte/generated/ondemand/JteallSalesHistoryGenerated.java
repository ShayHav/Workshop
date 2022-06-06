package gg.jte.generated.ondemand;
import Presentation.Model.PresentationOrder;
import Presentation.Model.PresentationProduct;
import Presentation.Model.PresentationShop;
import Presentation.Model.PresentationUser;
import java.util.List;
import java.util.Map;
public final class JteallSalesHistoryGenerated {
	public static final String JTE_NAME = "allSalesHistory.jte";
	public static final int[] JTE_LINE_INFO = {0,0,1,2,3,4,5,6,6,6,18,18,18,23,23,24,24,31,31,31,34,34,34,38,38,38,39,39,39,40,40,40,46,46,46,62,62,64,64,64,67,67,67,70,70,70,73,73,73,76,76,76,80,80,80,81,81,81,82,82,82,87,87,87,89,89,93,93,93,96,96,96,99,99,99,103,103,107,107,110,110,115,115,116,116,123,123,123,126,126,126,130,130,130,131,131,131,132,132,132,138,138,138,157,157,159,159,159,162,162,162,165,165,165,168,168,168,171,171,171,174,174,174,178,178,178,179,179,179,180,180,180,185,185,185,187,187,191,191,191,194,194,194,197,197,197,201,201,205,205,208,208,215};
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, PresentationUser user, Map<PresentationUser,List<PresentationOrder>> userOrders, Map<PresentationShop,List<PresentationOrder>> shopOrders, Double shopMinPrice, Double shopMaxPrice, String shopMinDate, String shopMaxDate, Double userMinPrice, Double userMaxPrice, String userMinDate, String userMaxDate) {
		jteOutput.writeContent("\r\n");
		gg.jte.generated.ondemand.JtenavBarGenerated.render(jteOutput, jteHtmlInterceptor, user, "Sales");
		jteOutput.writeContent("\r\n<main class=\"mx-4 my-3\">\r\n    <div class=\"mb-5 mt-3\">\r\n        <div class=\"mb-3 fw-bold fs-3\">Shops History</div>\r\n        <div class=\"mb-3 fs-5\">Filter shop results:</div>\r\n        ");
		gg.jte.generated.ondemand.JteorderFilterGenerated.render(jteOutput, jteHtmlInterceptor, "/admin/" + user.getUsername() + "/sales", shopMinPrice, shopMaxPrice, shopMinDate, shopMaxDate, 1);
		jteOutput.writeContent("\r\n        ");
		for (PresentationShop shop: shopOrders.keySet()) {
			jteOutput.writeContent("\r\n            <div class=\"mt-3 mb-2 row\">\r\n                <div class=\"col-2\">\r\n                    <h3 class=\"mb-3 fs-5\">Shop Name</h3>\r\n                </div>\r\n            </div>\r\n            <div class=\" containercard\">\r\n                <div class=\"card-header\" id=\"heading");
			jteOutput.setContext("div", "id");
			jteOutput.writeUserContent(shop.name);
			jteOutput.writeContent("\">\r\n                    <div class=\"row\">\r\n                        <div class=\"col-2\">\r\n                            <p>");
			jteOutput.setContext("p", null);
			jteOutput.writeUserContent(shop.name);
			jteOutput.writeContent("</p>\r\n                        </div>\r\n                        <div class=\"col-2\">\r\n                            <button class=\"btn btn-link\" type=\"button\" data-bs-toggle=\"collapse\"\r\n                                    data-bs-target=\"#collapse");
			jteOutput.setContext("button", "data-bs-target");
			jteOutput.writeUserContent(shop.name);
			jteOutput.writeContent("\"\r\n                                    aria-expanded=\"false\" aria-controls=\"collapse");
			jteOutput.setContext("button", "aria-controls");
			jteOutput.writeUserContent(shop.name);
			jteOutput.writeContent("\"\r\n                                    id=\"collapseButton");
			jteOutput.setContext("button", "id");
			jteOutput.writeUserContent(shop.name);
			jteOutput.writeContent("\">Show Orders\r\n                            </button>\r\n                        </div>\r\n                    </div>\r\n                </div>\r\n            </div>\r\n            <div id=\"collapse");
			jteOutput.setContext("div", "id");
			jteOutput.writeUserContent(shop.name);
			jteOutput.writeContent("\" class=\"collapse\">\r\n                <div class=\"mt-3 mb-2 row\">\r\n                    <div class=\"col-2\">\r\n                        <h5>Order Number</h5>\r\n                    </div>\r\n                    <div class=\"col-2\">\r\n                        <h5>Purchase Date</h5>\r\n                    </div>\r\n                    <div class=\"col-2\">\r\n                        <h5>Bought User</h5>\r\n                    </div>\r\n                    <div class=\"col-2\">\r\n                        <h5>Total Amount</h5>\r\n                    </div>\r\n                </div>\r\n                <div style=\"align-items: center\">\r\n                    ");
			for (PresentationOrder order : shopOrders.get(shop)) {
				jteOutput.writeContent("\r\n                        <div class=\"card\">\r\n                            <div class=\"card-header\" id=\"heading");
				jteOutput.setContext("div", "id");
				jteOutput.writeUserContent(order.id);
				jteOutput.writeContent("\">\r\n                                <div class=\"row\">\r\n                                    <div class=\"col-2\">\r\n                                        <p>");
				jteOutput.setContext("p", null);
				jteOutput.writeUserContent(order.id);
				jteOutput.writeContent("</p>\r\n                                    </div>\r\n                                    <div class=\"col-2\">\r\n                                        <p>");
				jteOutput.setContext("p", null);
				jteOutput.writeUserContent(order.getPurchaseDate());
				jteOutput.writeContent("</p>\r\n                                    </div>\r\n                                    <div class=\"col-2\">\r\n                                        <p>");
				jteOutput.setContext("p", null);
				jteOutput.writeUserContent(order.userBought);
				jteOutput.writeContent("</p>\r\n                                    </div>\r\n                                    <div class=\"col-2\">\r\n                                        <p>");
				jteOutput.setContext("p", null);
				jteOutput.writeUserContent(order.totalAmount);
				jteOutput.writeContent("</p>\r\n                                    </div>\r\n                                    <div class=\"col-2\">\r\n                                        <button class=\"btn btn-link\" type=\"button\" data-bs-toggle=\"collapse\"\r\n                                                data-bs-target=\"#collapse");
				jteOutput.setContext("button", "data-bs-target");
				jteOutput.writeUserContent(order.id);
				jteOutput.writeContent("\"\r\n                                                aria-expanded=\"false\" aria-controls=\"collapse");
				jteOutput.setContext("button", "aria-controls");
				jteOutput.writeUserContent(order.id);
				jteOutput.writeContent("\"\r\n                                                id=\"collapseButton");
				jteOutput.setContext("button", "id");
				jteOutput.writeUserContent(order.id);
				jteOutput.writeContent("\">Show Items in order\r\n                                        </button>\r\n                                    </div>\r\n                                </div>\r\n                            </div>\r\n                            <div id=\"collapse");
				jteOutput.setContext("div", "id");
				jteOutput.writeUserContent(order.id);
				jteOutput.writeContent("\" class=\"collapse\">\r\n                                <div class=\"card-body\">\r\n                                    ");
				for (PresentationProduct product: order.productsBoughtWithPrices.keySet()) {
					jteOutput.writeContent("\r\n                                        <ul class=\"container rounded-1 card row mx-auto list-group-flush list-group\">\r\n                                            <li class=\"container list-group-item\">\r\n                                                <div class=\"col\">\r\n                                                    <p>Product Name: ");
					jteOutput.setContext("p", null);
					jteOutput.writeUserContent(product.name);
					jteOutput.writeContent("</p>\r\n                                                </div>\r\n                                                <div class=\"col\">\r\n                                                    <p>Amount Bought: ");
					jteOutput.setContext("p", null);
					jteOutput.writeUserContent(product.amount);
					jteOutput.writeContent("</p>\r\n                                                </div>\r\n                                                <div class=\"col\">\r\n                                                    <p>Total Price: ");
					jteOutput.setContext("p", null);
					jteOutput.writeUserContent(order.productsBoughtWithPrices.get(product));
					jteOutput.writeContent("</p>\r\n                                                </div>\r\n                                            </li>\r\n                                        </ul>\r\n                                    ");
				}
				jteOutput.writeContent("\r\n                                </div>\r\n                            </div>\r\n                        </div>\r\n                    ");
			}
			jteOutput.writeContent("\r\n                </div>\r\n            </div>\r\n        ");
		}
		jteOutput.writeContent("\r\n    </div>\r\n    <div class=\"mt-3\">\r\n        <div class=\"mb-3 fw-bold fs-3\">Users History</div>\r\n        <div class=\"mb-3 fs-5\">Filter user results:</div>\r\n        ");
		gg.jte.generated.ondemand.JteorderFilterGenerated.render(jteOutput, jteHtmlInterceptor, "/admin/" + user.getUsername() + "/sales", userMinPrice, userMaxPrice, userMinDate, userMaxDate, 2);
		jteOutput.writeContent("\r\n        ");
		for (PresentationUser u: userOrders.keySet()) {
			jteOutput.writeContent("\r\n            <div class=\"mt-3 mb-2 row\">\r\n                <div class=\"col-2\">\r\n                    <h3 class=\"mb-3 fs-5\">Username</h3>\r\n                </div>\r\n            </div>\r\n            <div class=\"card\">\r\n                <div class=\"card-header\" id=\"heading");
			jteOutput.setContext("div", "id");
			jteOutput.writeUserContent(u.getUsername());
			jteOutput.writeContent("\">\r\n                    <div class=\"row\">\r\n                        <div class=\"col-2\">\r\n                            <p>");
			jteOutput.setContext("p", null);
			jteOutput.writeUserContent(u.getUsername());
			jteOutput.writeContent("</p>\r\n                        </div>\r\n                        <div class=\"col-2\">\r\n                            <button class=\"btn btn-link\" type=\"button\" data-bs-toggle=\"collapse\"\r\n                                    data-bs-target=\"#collapse");
			jteOutput.setContext("button", "data-bs-target");
			jteOutput.writeUserContent(u.getUsername());
			jteOutput.writeContent("\"\r\n                                    aria-expanded=\"false\" aria-controls=\"collapse");
			jteOutput.setContext("button", "aria-controls");
			jteOutput.writeUserContent(u.getUsername());
			jteOutput.writeContent("\"\r\n                                    id=\"collapseButton");
			jteOutput.setContext("button", "id");
			jteOutput.writeUserContent(u.getUsername());
			jteOutput.writeContent("\">Show Orders\r\n                            </button>\r\n                        </div>\r\n                    </div>\r\n                </div>\r\n            </div>\r\n            <div id=\"collapse");
			jteOutput.setContext("div", "id");
			jteOutput.writeUserContent(u.getUsername());
			jteOutput.writeContent("\" class=\"collapse\">\r\n                <div class=\"mt-3 mb-2 row\">\r\n                    <div class=\"col-2\">\r\n                        <h5>Shop name</h5>\r\n                    </div>\r\n                    <div class=\"col-2\">\r\n                        <h5>Order Number</h5>\r\n                    </div>\r\n                    <div class=\"col-2\">\r\n                        <h5>Purchase Date</h5>\r\n                    </div>\r\n                    <div class=\"col-2\">\r\n                        <h5>Bought User</h5>\r\n                    </div>\r\n                    <div class=\"col-2\">\r\n                        <h5>Total Amount</h5>\r\n                    </div>\r\n                </div>\r\n                <div>\r\n                    ");
			for (PresentationOrder order : userOrders.get(u)) {
				jteOutput.writeContent("\r\n                        <div class=\"card\">\r\n                            <div class=\"card-header\" id=\"heading");
				jteOutput.setContext("div", "id");
				jteOutput.writeUserContent(order.id);
				jteOutput.writeContent("\">\r\n                                <div class=\"row\">\r\n                                    <div class=\"col-2\">\r\n                                        <p>");
				jteOutput.setContext("p", null);
				jteOutput.writeUserContent(order.shopName);
				jteOutput.writeContent("</p>\r\n                                    </div>\r\n                                    <div class=\"col-2\">\r\n                                        <p>");
				jteOutput.setContext("p", null);
				jteOutput.writeUserContent(order.id);
				jteOutput.writeContent("</p>\r\n                                    </div>\r\n                                    <div class=\"col-2\">\r\n                                        <p>");
				jteOutput.setContext("p", null);
				jteOutput.writeUserContent(order.getPurchaseDate());
				jteOutput.writeContent("</p>\r\n                                    </div>\r\n                                    <div class=\"col-2\">\r\n                                        <p>");
				jteOutput.setContext("p", null);
				jteOutput.writeUserContent(order.userBought);
				jteOutput.writeContent("</p>\r\n                                    </div>\r\n                                    <div class=\"col-2\">\r\n                                        <p>");
				jteOutput.setContext("p", null);
				jteOutput.writeUserContent(order.totalAmount);
				jteOutput.writeContent("</p>\r\n                                    </div>\r\n                                    <div class=\"col-2\">\r\n                                        <button class=\"btn btn-link\" type=\"button\" data-bs-toggle=\"collapse\"\r\n                                                data-bs-target=\"#collapse");
				jteOutput.setContext("button", "data-bs-target");
				jteOutput.writeUserContent(order.id);
				jteOutput.writeContent("\"\r\n                                                aria-expanded=\"false\" aria-controls=\"collapse");
				jteOutput.setContext("button", "aria-controls");
				jteOutput.writeUserContent(order.id);
				jteOutput.writeContent("\"\r\n                                                id=\"collapseButton");
				jteOutput.setContext("button", "id");
				jteOutput.writeUserContent(order.id);
				jteOutput.writeContent("\">Show Items in order\r\n                                        </button>\r\n                                    </div>\r\n                                </div>\r\n                            </div>\r\n                            <div id=\"collapse");
				jteOutput.setContext("div", "id");
				jteOutput.writeUserContent(order.id);
				jteOutput.writeContent("\" class=\"collapse\">\r\n                                <div class=\"card-body\">\r\n                                    ");
				for (PresentationProduct product: order.productsBoughtWithPrices.keySet()) {
					jteOutput.writeContent("\r\n                                        <ul class=\"container rounded-1 card row mx-auto list-group-flush list-group\">\r\n                                            <li class=\"container list-group-item\">\r\n                                                <div class=\"col\">\r\n                                                    <p>Product Name: ");
					jteOutput.setContext("p", null);
					jteOutput.writeUserContent(product.name);
					jteOutput.writeContent("</p>\r\n                                                </div>\r\n                                                <div class=\"col\">\r\n                                                    <p>Amount Bought: ");
					jteOutput.setContext("p", null);
					jteOutput.writeUserContent(product.amount);
					jteOutput.writeContent("</p>\r\n                                                </div>\r\n                                                <div class=\"col\">\r\n                                                    <p>Total Price: ");
					jteOutput.setContext("p", null);
					jteOutput.writeUserContent(order.productsBoughtWithPrices.get(product));
					jteOutput.writeContent("</p>\r\n                                                </div>\r\n                                            </li>\r\n                                        </ul>\r\n                                    ");
				}
				jteOutput.writeContent("\r\n                                </div>\r\n                            </div>\r\n                        </div>\r\n                    ");
			}
			jteOutput.writeContent("\r\n                </div>\r\n            </div>\r\n        ");
		}
		jteOutput.writeContent("\r\n    </div>\r\n</main>\r\n</body>\r\n\r\n</html>\r\n        \r\n");
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		PresentationUser user = (PresentationUser)params.get("user");
		Map<PresentationUser,List<PresentationOrder>> userOrders = (Map<PresentationUser,List<PresentationOrder>>)params.get("userOrders");
		Map<PresentationShop,List<PresentationOrder>> shopOrders = (Map<PresentationShop,List<PresentationOrder>>)params.get("shopOrders");
		Double shopMinPrice = (Double)params.get("shopMinPrice");
		Double shopMaxPrice = (Double)params.get("shopMaxPrice");
		String shopMinDate = (String)params.get("shopMinDate");
		String shopMaxDate = (String)params.get("shopMaxDate");
		Double userMinPrice = (Double)params.get("userMinPrice");
		Double userMaxPrice = (Double)params.get("userMaxPrice");
		String userMinDate = (String)params.get("userMinDate");
		String userMaxDate = (String)params.get("userMaxDate");
		render(jteOutput, jteHtmlInterceptor, user, userOrders, shopOrders, shopMinPrice, shopMaxPrice, shopMinDate, shopMaxDate, userMinPrice, userMaxPrice, userMinDate, userMaxDate);
	}
}
