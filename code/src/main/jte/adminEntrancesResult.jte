@import Presentation.Model.PresentationUser
@import Presentation.Model.PresentationEntrance
@import domain.user.EntranceLogger.Entrance
@import java.time.LocalDate
@import java.util.List

@param LocalDate from
@param LocalDate to

@param List<PresentationEntrance> guests
@param List<PresentationEntrance> members
@param List<PresentationEntrance> managers
@param List<PresentationEntrance> owners
@param List<PresentationEntrance> systemManagers
@param PresentationUser admin

@template.navBar(user = admin, title = "Admin Page")

<main class="container mx-3 mt-3" style="align-content: center">
    <div class="my-4 mx-4 mb-5 small container">
        <form method="get" id="filterUsers" class="form border rounded border-1 w-75"
              action="/admin/${admin.getUsername()}/systemMonitor/pastEntrances">
            <div class="row mb-2">
                <div class="col-4">
                    <div class="mt-3 mx-3 mb-3">
                        <label for="fromDate" class="form-label">From Date</label>
                        <input type="date" name="fromDate" id="fromDate" class="form-control"
                               value="${from.toString()}">
                    </div>
                </div>
                <div class="col-4">
                    <div class="mt-3 mx-3 mb-3">
                        <label for="toDate" class="form-label">From Date</label>
                        <input type="date" name="toDate" id="toDate" class="form-control" value="${to.toString()}">
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
        reset_button.addEventListener('click', () => window.location.replace("/admin/${admin.getUsername()}/systemMonitor/pastEntrances"));
    </script>

    <div class="container-fluid">
        <div class="mb-5">
            <p class="fs-3 mb-2">Guests Entrances</p>
            <table class="table">
                <thead>
                <tr>
                    <th scope="col">#</th>
                    <th colspan="1">Username</th>
                    <th colspan="1">Date Of Entrance</th>
                </tr>
                </thead>
                <tbody id="guestsTableBody">
                @for(PresentationEntrance guestEntrance : guests)
                    <tr>
                        <th scope="row"></th>
                        <td colspan="1">${guestEntrance.getEnteredUser().getUsername()}</td>
                        <td colspan="1">${guestEntrance.getDate().toString()}</td>
                    </tr>
                @endfor
                </tbody>
            </table>
        </div>

        <div class="mb-5">
            <p class="fs-3 mb-2">Members Entrances</p>
            <table class="table">
                <thead>
                <tr>
                    <th scope="col">#</th>
                    <th colspan="2">Username</th>
                    <th colspan="2">Date Of Entrance</th>
                </tr>
                </thead>
                <tbody id="membersTableBody">
                @for(PresentationEntrance memberEntrance : members)
                    <tr>
                        <th scope="row"></th>
                        <td colspan="2">${memberEntrance.getEnteredUser().getUsername()}</td>
                        <td colspan="2">${memberEntrance.getDate().toString()}</td>
                    </tr>
                @endfor
                </tbody>
            </table>
        </div>

        <div class="mb-5">
            <p class="fs-3 mb-2">Managers Entrances</p>
            <table class="table">
                <thead>
                <tr>
                    <th scope="col">#</th>
                    <th colspan="2">Username</th>
                    <th colspan="2">Date Of Entrance</th>
                </tr>
                </thead>
                <tbody id="managersTableBody">
                @for(PresentationEntrance managerEntrance : managers)
                    <tr>
                        <th scope="row"></th>
                        <td colspan="2">${managerEntrance.getEnteredUser().getUsername()}</td>
                        <td colspan="2">${managerEntrance.getDate().toString()}</td>
                    </tr>
                @endfor
                </tbody>
            </table>
        </div>

        <div class="mb-5">
            <p class="fs-3 mb-2">Owners Entrances</p>
            <table class="table">
                <thead>
                <tr>
                    <th scope="col">#</th>
                    <th colspan="2">Username</th>
                    <th colspan="2">Date Of Entrance</th>
                </tr>
                </thead>
                <tbody id="ownersTableBody">
                @for(PresentationEntrance ownerEntrance : owners)
                    <tr>
                        <th scope="row"></th>
                        <td colspan="2">${ownerEntrance.getEnteredUser().getUsername()}</td>
                        <td colspan="2">${ownerEntrance.getDate().toString()}</td>
                    </tr>
                @endfor
                </tbody>
            </table>
        </div>

        <div class="mb-5">
            <p class="fs-3 mb-2">System Managers Entrances</p>
            <table class="table">
                <thead>
                <tr>
                    <th scope="col">#</th>
                    <th colspan="2">Username</th>
                    <th colspan="2">Date Of Entrance</th>
                </tr>
                </thead>
                <tbody id="adminTableBody">
                @for(PresentationEntrance sysManagerEntrance : systemManagers)
                    <tr>
                        <th scope="row"></th>
                        <td colspan="2">${sysManagerEntrance.getEnteredUser().getUsername()}</td>
                        <td colspan="2">${sysManagerEntrance.getDate().toString()}</td>
                    </tr>
                @endfor
                </tbody>
            </table>
        </div>
    </div>
</main>

</body>
</html>
