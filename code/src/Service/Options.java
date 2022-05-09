package Service;

public class Options {

    private static String [] HROpts = {"Update Shift", "Change Worker's personal information", "Look at this week's shifts", "Look at next week's shifts", "Look at a specific shift",
            "Register new worker", "Get the recommended lineup of a shift","Change the recommended lineup of a shift", "Get the recommended number of employees of a shift",
            "Make Shifts for next week","Show Employee List", "Show Driver List", "Fire employee", "Cancel Delivery Request","Logout"};
    private static String [] SMOpts = {"Look at this week's shifts", "Look at next week's shifts", "Look at a specific shift", "Get the recommended lineup of a shift", "Get the recommended number of employees of a shift","Show EmployeeList","SupplierMenu","create Report", "Show Driver List", "Logout"};
    private static String [] RWOpts = {"Show Constraints","Update Constraints", "Look at this week's shifts", "Look at next week's shifts", "Look at a specific shift" ,"Logout"};
    private static String [] ChangeConstraintsOpts = {"Change all shift constraints for the upcoming week", "Change a specific shift constraint in the upcoming week",
            "Return"};
    private static String [] registerWorkerOpts = {"Register a new regular worker", "Register a new HR manager","Register a new Driver","Register a new TPManager", "Register a new Storekeeper","Register a new Store Manager",
            "Return"};
    private static String [] ChangeInfoOpts = {"First name", "Last name", "Wage", "Monthly days off", " Monthly sick days", "Bank Information", "Advanced study funds", "Return"} ;
    private static String[] firstOts = {"Load data","LogIn","Exit"};
    private static String[] SKOpts = {"Supplier menu","inventory menu","Show Constraints","Update Constraints", "Look at this week's shifts", "Look at next week's shifts", "Look at a specific shift" ,"Cancel Delivery Request","Logout"};
    public static String[] getHROpts() {
        return HROpts;
    }
    public static String[] getRWOpts() {return RWOpts;}
    public static String[] getChangeInfoOpts() {return ChangeInfoOpts;}
    public static String[] getChangeConstraintsOpts() {return ChangeConstraintsOpts; }
    public static String[] getRegisterWorkerOpts() {return registerWorkerOpts;}
    public static String[] getFirstOts(){ return firstOts;}
    public static String[] getSMOpts() {
        return SMOpts;
    }

    public static String[] getSKOpts() {
        return SKOpts;
    }
}
