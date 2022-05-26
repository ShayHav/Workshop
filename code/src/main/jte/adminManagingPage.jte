@import Presentation.Model.PresentationUser
@import java.util.List
@param PresentationUser admin

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
                                        15
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
                                    <p class="text-sm mb-0 text-capitalize font-weight-bold">Total registered members</p>
                                    <h5 id="registeredMembers" class="text-secondary font-weight-bolder mt-1 mb-0">
                                        5
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
                                        10
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
                                       5
                                    </h5>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="container-fluid">


        <div class="mb-3 fs-3">Users</div>
        <div class="mt-3 mb-2 row">
            <div class="col-3">
                <p class="fs-6">Username</p>
            </div>
            <div class="col-3">
                <p class="fs-6">Guest/Member</p>
            </div>
            <div class="col-3">
                <p class="fs-6  ">Active/Disconnected</p>
            </div>
        </div>
        <div>
            <ul class="container rounded-1 card row mx-auto list-group-flush list-group">
                <li class="container list-group-item">
                    <div class="row">
                    <div class="col-3">
                        <p>shahar</p>
                    </div>
                    <div class="col-3">
                        <p>member</p>
                    </div>
                    <div class="col-3">
                        <p>active</p>
                    </div>
                    </div>
                </li>

            </ul>
        </div>

    </div>
</main>

</body>

<script>


</script>
</html>
