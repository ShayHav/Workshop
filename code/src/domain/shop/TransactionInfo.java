package domain.shop;

public class TransactionInfo {
    private boolean approve;
    public TransactionInfo()
    {
        approve = false;
    }


    public void ApproveTransaction() { this.approve = true; }

    public boolean isApprove() { return this.approve; }


}
