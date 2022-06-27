@import Presentation.Model.PresentationUser
@import Presentation.Model.PresentationBid
@import java.util.List

@param PresentationUser user
@param List<PresentationBid> bids

@template.navBar(user = user, title = "Bids")

<main class="container my-3">
    <h1 class="fs-2">Bids Request</h1>
    <ul class="list-group">
        @for(PresentationBid bid : bids)
            <li class="list-group-item">
                <div>
                    <h3>${bid.getProductName()}</h3>
                    <p><b>price:</b> ${bid.getPrice()}</p>
                    <p><b>amount:</b> ${bid.getQuantity()}</p>
                    <p><b>requesting user:</b> ${bid.getRequestedUser().getUsername()}</p>
                </div>
                <div class="progress">
                    <div class="progress-bar bg-success" role="progressbar" style="width: ${bid.getStatus()}%" aria-valuenow="${bid.getStatus()}" aria-valuemin="0" aria-valuemax="100"></div>
                </div>
                @if(!bid.isCompleted())
                    <div class="row my-3">
                        <div class="col-2">
                        <form method="post" action="/shops/${bid.getShopID()}/approveBid">
                            <input type="hidden" name="bidID" value="${bid.getBidID()}">
                            @if(!bid.didApproved(user))
                                <button class="btn btn-success">Approve</button>
                            @else
                                <button class="btn btn-success" disabled>Approve</button>
                            @endif
                        </form>
                        </div>
                        <div class="col-2">
                        <form method="post" action="/shops/${bid.getShopID()}/declineBid">
                            <input type="hidden" name="bidID" value="${bid.getBidID()}">
                            <button class="btn btn-danger">Decline</button>
                        </form>
                        </div>
                    </div>
                @endif
            </li>
        @endfor
    </ul>

</main>

</body>
</html>