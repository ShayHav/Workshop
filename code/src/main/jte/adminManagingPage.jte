@import Presentation.Main
@import Presentation.Model.PresentationUser
@import java.util.List
@import java.util.Map
@param PresentationUser admin
@param Map<Integer,PresentationUser> users
@param Integer activeUsers
@param int activeMembers
@param int activeGuests
@param int totalRegistered



@template.navBar(user = admin, title = "Admin Page")

<main class="container mx-3 mt-3" style="align-content: center">

    <div class="container-fluid py-4">
        <div class="mb-3 fs-3">System Information</div>
        <div class="row">
            <div class="col-xl-6 col-sm-6 mb-xl-0 mb-4 align-content-center">
                <div class="card">
                    <div class="card-body p-3">
                        <div class="row">
                            <div class="col-8">
                                <div class="text">
                                    <p class="text-sm mb-0 text-capitalize font-weight-bold">Active users in Market</p>
                                    <h5 id="activeUsers" class="text-success font-weight-bolder mt-1 mb-0">
                                        ${activeUsers}
                                    </h5>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-xl-4 col-sm-6 mb-xl-0 mb-4">
                <div class="card">
                    <div class="card-body p-3">
                        <div class="row">
                            <div class="col-8">
                                <div class="text">
                                    <p class="text-sm mb-0 text-capitalize font-weight-bold">Total registered
                                        members</p>
                                    <h5 id="registeredMembers" class="text-secondary font-weight-bolder mt-1 mb-0">
                                        ${totalRegistered}
                                    </h5>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="row mt-3">
            <div class="col-xl-3 col-sm-6 mb-xl-0 mb-4">
                <div class="card">
                    <div class="card-body p-3">
                        <div class="row">
                            <div class="col-8">
                                <div class="text">
                                    <p class="text-sm mb-0 text-capitalize font-weight-bold">Active Members</p>
                                    <h5 id="activeMembers" class="text-success font-weight-bolder mt-1 mb-0">
                                        ${activeMembers}
                                    </h5>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-xl-3 col-sm-6 mb-xl-0 mb-4">
                <div class="card">
                    <div class="card-body p-3">
                        <div class="row">
                            <div class="col-8">
                                <div class="text">
                                    <p class="text-sm mb-0 text-capitalize font-weight-bold">Active Guests</p>
                                    <h5 id="activeGuests" class="text-success font-weight-bolder mt-1 mb-0">
                                        ${activeGuests}
                                    </h5>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>



    <div class="mb-5 py-4 small container-fluid">
        <div class="mb-3 fs-4">Get Filtered List of Entrances:</div>
        <form method="get" id="filterUsers" class="form border rounded border-1 w-75" action="/admin/${admin.getUsername()}/systemMonitor/pastEntrances">
            <div class="row mb-2">
                <div class="col-4">
                    <div class="mt-3 mx-3 mb-3">
                        <label for="fromDate" class="form-label">From Date</label>
                        <input type="date" name="fromDate" id="fromDate" class="form-control">
                    </div>
                </div>
                <div class="col-4">
                    <div class="mt-3 mx-3 mb-3">
                        <label for="toDate" class="form-label">From Date</label>
                        <input type="date" name="toDate" id="toDate" class="form-control">
                    </div>
                </div>
            </div>
            <div class="row col-4">
                <div class="mb-2 mx-2 col">
                    <button class="btn btn-primary" type="submit">Filter results</button>
                </div>
                <div class="mb-2 col">
                    <button type="button" id="resetFilter" class="btn btn-primary">Reset Filter</button>
                </div>
            </div>
        </form>
    </div>

    <script>
        const reset_button = document.getElementById('resetFilter');
        reset_button.addEventListener('click',()=> window.location.reload());
    </script>

    <div class="container-fluid">
        <p class="fs-3">Users</p>
        <table class="table">
            <thead>
            <tr>
                <th scope="col">#</th>
                <th colspan="2">Username</th>
                <th colspan="2">Guest/Member/Admin</th>
                <th scope="col">Active/Disconnected</th>
                <th colspan="2"></th>
            </tr>
            </thead>
            <tbody id="usersTableBody">
            @for(Integer i : users.keySet())
                <tr>
                    <th scope="row">${i}</th>
                    <td colspan="2">${users.get(i).getUsername()}</td>
                    @if(users.get(i).isGuest())
                        <td colspan="1">guest</td>
                    @elseif(users.get(i).isAdmin())
                        <td colspan="2">Admin</td>
                    @else
                        <td colspan="2">member</td>
                    @endif
                    @if(users.get(i).isLoggedIn() || users.get(i).isGuest())
                        <td colspan="2" class="table-success">active</td>
                    @else
                        <td colspan="2">disconnected</td>
                    @endif
                    @if(!users.get(i).isGuest() && !users.get(i).isAdmin() && !users.get(i).isLoggedIn())
                    <td colspan="2">
                        <form method="post" class="form" id="removeUser" name="removeUser"
                              action="/admin/${admin.getUsername()}/removeUser">
                            <button class="btn btn-sm btn-danger" type="submit">
                                <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor"
                                     class="bi bi-trash" viewBox="0 0 16 16">
                                    <path d="M5.5 5.5A.5.5 0 0 1 6 6v6a.5.5 0 0 1-1 0V6a.5.5 0 0 1 .5-.5zm2.5 0a.5.5 0 0 1 .5.5v6a.5.5 0 0 1-1 0V6a.5.5 0 0 1 .5-.5zm3 .5a.5.5 0 0 0-1 0v6a.5.5 0 0 0 1 0V6z"/>
                                    <path fill-rule="evenodd"
                                          d="M14.5 3a1 1 0 0 1-1 1H13v9a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V4h-.5a1 1 0 0 1-1-1V2a1 1 0 0 1 1-1H6a1 1 0 0 1 1-1h2a1 1 0 0 1 1 1h3.5a1 1 0 0 1 1 1v1zM4.118 4 4 4.059V13a1 1 0 0 0 1 1h6a1 1 0 0 0 1-1V4.059L11.882 4H4.118zM2.5 3V2h11v1h-11z"/>
                                </svg>
                            </button>
                            <input class="visually-hidden" name="username" value="${users.get(i).getUsername()}" readonly>
                        </form>
                    </td>
                    @endif
                </tr>
            @endfor
            </tbody>
        </table>
    </div>
</main>
</body>

<script>
    const socket = new WebSocket("ws://localhost:${Main.port}/admin/${admin.getUsername()}/systemMonitor");
    
    socket.onmessage = function (event) {
        const infoMessage = JSON.parse(event.data);

        let activeUsers = document.getElementById('activeUsers');
        activeUsers.innerText = infoMessage.currentActiveUsers;

        let activeMembers = document.getElementById('registeredMembers');
        activeMembers.innerText = infoMessage.currentActiveMembers;

        let activeGuests = document.getElementById('activeGuests');
        activeMembers.innerText = infoMessage.currentActiveGuests;

        let totalRegistered = document.getElementById('registeredMembers');
        totalRegistered.innerText = infoMessage.totalRegisteredMembers;


        let users = new Map(infoMessage.users);
        let tableBody = document.getElementById("usersTableBody");
        tableBody.innerHTML = "";
        for (const usersKey in users) {
            let new_table_row = document.createElement('tr')
            let table_th1 = document.createElement('th');
            table_th1.scope = "row";
            table_th1.innerText = usersKey;

            let table_td1 = document.createElement('td');
            table_td1.colSpan = 2;
            table_td1.innerText = users.get(usersKey).getUsername();

            let table_td2 = document.createElement('td');
            table_td2.colSpan = 2;
            if(users.get(usersKey).isGuest())
                table_td2.innerText = "guest";
            else
                table_td2.innerText = "member";

            let table_td3 = document.createElement('td');
            table_td3.colSpan = 2;
            if(users.get(usersKey).isLoggedIn()) {
                table_td3.innerText = "active";
                table_td3.classList.add("table-success")
            }
            else
                table_td3.innerText = "disconnected";

            let table_td4 = document.createElement('td');
            table_td1.colSpan = 2;

            if(!users.get(usersKey).isGuest()) {
                let removeForm = document.createElement('form');
                removeForm.method = "post";
                removeForm.classList.add("form");
                removeForm.id = "removeUser";
                removeForm.action = "/admin/${admin.getUsername()}/removeUser";

                let remove_btn = document.createElement("button");
                remove_btn.classList.add('btn','btn-sm','btn-danger');
                remove_btn.type = "submit";
                remove_btn.innerText = '<svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-trash" viewBox="0 0 16 16">' +
                    '<path d="M5.5 5.5A.5.5 0 0 1 6 6v6a.5.5 0 0 1-1 0V6a.5.5 0 0 1 .5-.5zm2.5 0a.5.5 0 0 1 .5.5v6a.5.5 0 0 1-1 0V6a.5.5 0 0 1 .5-.5zm3 .5a.5.5 0 0 0-1 0v6a.5.5 0 0 0 1 0V6z"/>' +
                    '<path fill-rule="evenodd" d="M14.5 3a1 1 0 0 1-1 1H13v9a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V4h-.5a1 1 0 0 1-1-1V2a1 1 0 0 1 1-1H6a1 1 0 0 1 1-1h2a1 1 0 0 1 1 1h3.5a1 1 0 0 1 1 1v1zM4.118 4 4 4.059V13a1 1 0 0 0 1 1h6a1 1 0 0 0 1-1V4.059L11.882 4H4.118zM2.5 3V2h11v1h-11z"/>' +
                    '</svg>';

                let input = document.createElement("input");
                input.classList.add('visually-hidden');
                input.name = "username";
                input.value = users.get(usersKey).getUsername();
                input.readOnly = true;

                removeForm.appendChild(remove_btn);
                removeForm.appendChild(input);
                table_td4.appendChild(removeForm);
            }

            new_table_row.appendChild(table_th1);
            new_table_row.appendChild(table_td1);
            new_table_row.appendChild(table_td2);
            new_table_row.appendChild(table_td3);
            new_table_row.appendChild(table_td4);

            tableBody.appendChild(new_table_row);
        }


    }


</script>
</html>
