package gg.jte.generated.ondemand;
import Presentation.Main;
import java.util.List;
import java.util.Map;
public final class JteadminManagingPageGenerated {
	public static final String JTE_NAME = "adminManagingPage.jte";
	public static final int[] JTE_LINE_INFO = {0,0,1,2,3,4,4,4,11,11,11,26,26,26,43,43,43,61,61,61,77,77,77,101,101,103,103,103,104,104,104,105,105,107,107,109,109,111,111,112,112,114,114,116,116,117,117,120,120,120,129,129,129,129,129,129,129,129,132,132,134,134,142,142,142,142,142,142,197,197,197,233};
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, PresentationUser admin, Map<Integer,PresentationUser> users, Integer activeUsers, int activeMembers, int activeGuests, int totalRegistered) {
		jteOutput.writeContent("\r\n");
		gg.jte.generated.ondemand.JtenavBarGenerated.render(jteOutput, jteHtmlInterceptor, admin, "Admin Page");
		jteOutput.writeContent("\r\n\r\n<main class=\"container mx-3 mt-3\" style=\"align-content: center\">\r\n\r\n    <div class=\"container-fluid py-4\">\r\n        <div class=\"mb-3 fs-3\">System Information</div>\r\n        <div class=\"row\">\r\n            <div class=\"col-xl-6 col-sm-6 mb-xl-0 mb-4 align-content-center\">\r\n                <div class=\"card\">\r\n                    <div class=\"card-body p-3\">\r\n                        <div class=\"row\">\r\n                            <div class=\"col-8\">\r\n                                <div class=\"text\">\r\n                                    <p class=\"text-sm mb-0 text-capitalize font-weight-bold\">Active users in Market</p>\r\n                                    <h5 id=\"activeUsers\" class=\"text-success font-weight-bolder mt-1 mb-0\">\r\n                                        ");
		jteOutput.setContext("h5", null);
		jteOutput.writeUserContent(activeUsers);
		jteOutput.writeContent("\r\n                                    </h5>\r\n                                </div>\r\n                            </div>\r\n                        </div>\r\n                    </div>\r\n                </div>\r\n            </div>\r\n            <div class=\"col-xl-4 col-sm-6 mb-xl-0 mb-4\">\r\n                <div class=\"card\">\r\n                    <div class=\"card-body p-3\">\r\n                        <div class=\"row\">\r\n                            <div class=\"col-8\">\r\n                                <div class=\"text\">\r\n                                    <p class=\"text-sm mb-0 text-capitalize font-weight-bold\">Total registered\r\n                                        members</p>\r\n                                    <h5 id=\"registeredMembers\" class=\"text-secondary font-weight-bolder mt-1 mb-0\">\r\n                                        ");
		jteOutput.setContext("h5", null);
		jteOutput.writeUserContent(totalRegistered);
		jteOutput.writeContent("\r\n                                    </h5>\r\n                                </div>\r\n                            </div>\r\n                        </div>\r\n                    </div>\r\n                </div>\r\n            </div>\r\n        </div>\r\n        <div class=\"row mt-3\">\r\n            <div class=\"col-xl-3 col-sm-6 mb-xl-0 mb-4\">\r\n                <div class=\"card\">\r\n                    <div class=\"card-body p-3\">\r\n                        <div class=\"row\">\r\n                            <div class=\"col-8\">\r\n                                <div class=\"text\">\r\n                                    <p class=\"text-sm mb-0 text-capitalize font-weight-bold\">Active Members</p>\r\n                                    <h5 id=\"activeMembers\" class=\"text-success font-weight-bolder mt-1 mb-0\">\r\n                                        ");
		jteOutput.setContext("h5", null);
		jteOutput.writeUserContent(activeMembers);
		jteOutput.writeContent("\r\n                                    </h5>\r\n                                </div>\r\n                            </div>\r\n                        </div>\r\n                    </div>\r\n                </div>\r\n            </div>\r\n            <div class=\"col-xl-3 col-sm-6 mb-xl-0 mb-4\">\r\n                <div class=\"card\">\r\n                    <div class=\"card-body p-3\">\r\n                        <div class=\"row\">\r\n                            <div class=\"col-8\">\r\n                                <div class=\"text\">\r\n                                    <p class=\"text-sm mb-0 text-capitalize font-weight-bold\">Active Guests</p>\r\n                                    <h5 id=\"activeGuests\" class=\"text-success font-weight-bolder mt-1 mb-0\">\r\n                                        ");
		jteOutput.setContext("h5", null);
		jteOutput.writeUserContent(activeGuests);
		jteOutput.writeContent("\r\n                                    </h5>\r\n                                </div>\r\n                            </div>\r\n                        </div>\r\n                    </div>\r\n                </div>\r\n            </div>\r\n        </div>\r\n    </div>\r\n\r\n    <div class=\"container-fluid\">\r\n        <p class=\"fs-3\">Users</p>\r\n        <table class=\"table\">\r\n            <thead>\r\n            <tr>\r\n                <th scope=\"col\">#</th>\r\n                <th colspan=\"2\">Username</th>\r\n                <th colspan=\"2\">Guest/Member/Admin</th>\r\n                <th scope=\"col\">Active/Disconnected</th>\r\n                <th colspan=\"2\"></th>\r\n            </tr>\r\n            </thead>\r\n            <tbody id=\"usersTableBody\">\r\n            ");
		for (Integer i : users.keySet()) {
			jteOutput.writeContent("\r\n                <tr>\r\n                    <th scope=\"row\">");
			jteOutput.setContext("th", null);
			jteOutput.writeUserContent(i);
			jteOutput.writeContent("</th>\r\n                    <td colspan=\"2\">");
			jteOutput.setContext("td", null);
			jteOutput.writeUserContent(users.get(i).getUsername());
			jteOutput.writeContent("</td>\r\n                    ");
			if (users.get(i).isGuest()) {
				jteOutput.writeContent("\r\n                        <td colspan=\"2\">guest</td>\r\n                    ");
			} else if (users.get(i).isAdmin()) {
				jteOutput.writeContent("\r\n                        <td colspan=\"2\">Admin</td>\r\n                    ");
			} else {
				jteOutput.writeContent("\r\n                        <td colspan=\"2\">member</td>\r\n                    ");
			}
			jteOutput.writeContent("\r\n                    ");
			if (users.get(i).isLoggedIn()) {
				jteOutput.writeContent("\r\n                        <td colspan=\"2\" class=\"table-success\">active</td>\r\n                    ");
			} else {
				jteOutput.writeContent("\r\n                        <td colspan=\"2\">disconnected</td>\r\n                    ");
			}
			jteOutput.writeContent("\r\n                    ");
			if (!users.get(i).isGuest() && !users.get(i).isAdmin() && !users.get(i).isLoggedIn()) {
				jteOutput.writeContent("\r\n                    <td colspan=\"2\">\r\n                        <form method=\"post\" class=\"form\" id=\"removeUser\" name=\"removeUser\"\r\n                              action=\"/admin/");
				jteOutput.setContext("form", "action");
				jteOutput.writeUserContent(admin.getUsername());
				jteOutput.writeContent("/removeUser\">\r\n                            <button class=\"btn btn-sm btn-danger\" type=\"submit\">\r\n                                <svg xmlns=\"http://www.w3.org/2000/svg\" width=\"16\" height=\"16\" fill=\"currentColor\"\r\n                                     class=\"bi bi-trash\" viewBox=\"0 0 16 16\">\r\n                                    <path d=\"M5.5 5.5A.5.5 0 0 1 6 6v6a.5.5 0 0 1-1 0V6a.5.5 0 0 1 .5-.5zm2.5 0a.5.5 0 0 1 .5.5v6a.5.5 0 0 1-1 0V6a.5.5 0 0 1 .5-.5zm3 .5a.5.5 0 0 0-1 0v6a.5.5 0 0 0 1 0V6z\"/>\r\n                                    <path fill-rule=\"evenodd\"\r\n                                          d=\"M14.5 3a1 1 0 0 1-1 1H13v9a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V4h-.5a1 1 0 0 1-1-1V2a1 1 0 0 1 1-1H6a1 1 0 0 1 1-1h2a1 1 0 0 1 1 1h3.5a1 1 0 0 1 1 1v1zM4.118 4 4 4.059V13a1 1 0 0 0 1 1h6a1 1 0 0 0 1-1V4.059L11.882 4H4.118zM2.5 3V2h11v1h-11z\"/>\r\n                                </svg>\r\n                            </button>\r\n                            <input class=\"visually-hidden\" name=\"username\"");
				if (gg.jte.runtime.TemplateUtils.isAttributeRendered(users.get(i).getUsername())) {
					jteOutput.writeContent(" value=\"");
					jteOutput.setContext("input", "value");
					jteOutput.writeUserContent(users.get(i).getUsername());
					jteOutput.setContext("input", null);
					jteOutput.writeContent("\"");
				}
				jteOutput.writeContent(" readonly>\r\n                        </form>\r\n                    </td>\r\n                    ");
			}
			jteOutput.writeContent("\r\n                </tr>\r\n            ");
		}
		jteOutput.writeContent("\r\n            </tbody>\r\n        </table>\r\n    </div>\r\n</main>\r\n</body>\r\n\r\n<script>\r\n    const socket = new WebSocket(\"ws://localhost:");
		jteOutput.setContext("script", null);
		jteOutput.writeUserContent(Main.port);
		jteOutput.writeContent("/admin/");
		jteOutput.setContext("script", null);
		jteOutput.writeUserContent(admin.getUsername());
		jteOutput.writeContent("/systemMonitor\");\r\n    \r\n    socket.onmessage = function (event) {\r\n        const infoMessage = JSON.parse(event.data);\r\n\r\n        let activeUsers = document.getElementById('activeUsers');\r\n        activeUsers.innerText = infoMessage.currentActiveUsers;\r\n\r\n        let activeMembers = document.getElementById('registeredMembers');\r\n        activeMembers.innerText = infoMessage.currentActiveMembers;\r\n\r\n        let activeGuests = document.getElementById('activeGuests');\r\n        activeMembers.innerText = infoMessage.currentActiveGuests;\r\n\r\n        let totalRegistered = document.getElementById('registeredMembers');\r\n        totalRegistered.innerText = infoMessage.totalRegisteredMembers;\r\n\r\n\r\n        let users = new Map(infoMessage.users);\r\n        let tableBody = document.getElementById(\"usersTableBody\");\r\n        tableBody.innerHTML = \"\";\r\n        for (const usersKey in users) {\r\n            let new_table_row = document.createElement('tr')\r\n            let table_th1 = document.createElement('th');\r\n            table_th1.scope = \"row\";\r\n            table_th1.innerText = usersKey;\r\n\r\n            let table_td1 = document.createElement('td');\r\n            table_td1.colSpan = 2;\r\n            table_td1.innerText = users.get(usersKey).getUsername();\r\n\r\n            let table_td2 = document.createElement('td');\r\n            table_td2.colSpan = 2;\r\n            if(users.get(usersKey).isGuest())\r\n                table_td2.innerText = \"guest\";\r\n            else\r\n                table_td2.innerText = \"member\";\r\n\r\n            let table_td3 = document.createElement('td');\r\n            table_td3.colSpan = 2;\r\n            if(users.get(usersKey).isLoggedIn()) {\r\n                table_td3.innerText = \"active\";\r\n                table_td3.classList.add(\"table-success\")\r\n            }\r\n            else\r\n                table_td3.innerText = \"disconnected\";\r\n\r\n            let table_td4 = document.createElement('td');\r\n            table_td1.colSpan = 2;\r\n\r\n            if(!users.get(usersKey).isGuest()) {\r\n                let removeForm = document.createElement('form');\r\n                removeForm.method = \"post\";\r\n                removeForm.classList.add(\"form\");\r\n                removeForm.id = \"removeUser\";\r\n                removeForm.action = \"/admin/");
		jteOutput.setContext("script", null);
		jteOutput.writeUserContent(admin.getUsername());
		jteOutput.writeContent("/removeUser\";\r\n\r\n                let remove_btn = document.createElement(\"button\");\r\n                remove_btn.classList.add('btn','btn-sm','btn-danger');\r\n                remove_btn.type = \"submit\";\r\n                remove_btn.innerText = '<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"16\" height=\"16\" fill=\"currentColor\" class=\"bi bi-trash\" viewBox=\"0 0 16 16\">' +\r\n                    '<path d=\"M5.5 5.5A.5.5 0 0 1 6 6v6a.5.5 0 0 1-1 0V6a.5.5 0 0 1 .5-.5zm2.5 0a.5.5 0 0 1 .5.5v6a.5.5 0 0 1-1 0V6a.5.5 0 0 1 .5-.5zm3 .5a.5.5 0 0 0-1 0v6a.5.5 0 0 0 1 0V6z\"/>' +\r\n                    '<path fill-rule=\"evenodd\" d=\"M14.5 3a1 1 0 0 1-1 1H13v9a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V4h-.5a1 1 0 0 1-1-1V2a1 1 0 0 1 1-1H6a1 1 0 0 1 1-1h2a1 1 0 0 1 1 1h3.5a1 1 0 0 1 1 1v1zM4.118 4 4 4.059V13a1 1 0 0 0 1 1h6a1 1 0 0 0 1-1V4.059L11.882 4H4.118zM2.5 3V2h11v1h-11z\"/>' +\r\n                    '</svg>';\r\n\r\n                let input = document.createElement(\"input\");\r\n                input.classList.add('visually-hidden');\r\n                input.name = \"username\";\r\n                input.value = users.get(usersKey).getUsername();\r\n                input.readOnly = true;\r\n\r\n                removeForm.appendChild(remove_btn);\r\n                removeForm.appendChild(input);\r\n                table_td4.appendChild(removeForm);\r\n            }\r\n\r\n            new_table_row.appendChild(table_th1);\r\n            new_table_row.appendChild(table_td1);\r\n            new_table_row.appendChild(table_td2);\r\n            new_table_row.appendChild(table_td3);\r\n            new_table_row.appendChild(table_td4);\r\n\r\n            tableBody.appendChild(new_table_row);\r\n        }\r\n\r\n\r\n    }\r\n\r\n\r\n</script>\r\n</html>\r\n");
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		PresentationUser admin = (PresentationUser)params.get("admin");
		Map<Integer,PresentationUser> users = (Map<Integer,PresentationUser>)params.get("users");
		Integer activeUsers = (Integer)params.get("activeUsers");
		int activeMembers = (int)params.get("activeMembers");
		int activeGuests = (int)params.get("activeGuests");
		int totalRegistered = (int)params.get("totalRegistered");
		render(jteOutput, jteHtmlInterceptor, admin, users, activeUsers, activeMembers, activeGuests, totalRegistered);
	}
}
