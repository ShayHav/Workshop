package Presentation.Model;

import domain.shop.OwnerAppointment;

import java.util.HashMap;
import java.util.Map;

public class PresentationAppointment {

    private int appointmentNumber;
    private int shopId;
    private PresentationUser toAppoint;
    private PresentationUser appointer;
    private Map<String, Boolean> status;
    private boolean completed;

    public PresentationAppointment(OwnerAppointment domainAppointment, int shopId){
        this.appointmentNumber = domainAppointment.getId();
        this.shopId = shopId;
        this.toAppoint = new PresentationUser(domainAppointment.getUserToAppoint());
        this.appointer = new PresentationUser(domainAppointment.getAppointUser());
        this.status = new HashMap<>();
        domainAppointment.getOwnersToApprove().forEach((user, approved) -> status.put(user.getUserName(), approved));
        this.completed = domainAppointment.isCompleted();
    }

    public int getAppointmentNumber() {
        return appointmentNumber;
    }

    public int getShopID() {
        return shopId;
    }

    public PresentationUser getAppointer() {
        return appointer;
    }

    public PresentationUser getToAppoint() {
        return toAppoint;
    }

    public boolean isCompleted() {
        return completed;
    }

    public int getStatus(){
        if(completed)
            return 100;
        int totalNumber = status.size();
        int approved = 0;
        for(Boolean b  : status.values()){
            if(b)
                approved++;
        }
        return (approved / totalNumber) * 100;
    }

    public boolean didApporoved(PresentationUser user){
        return status.getOrDefault(user.getUsername(), false);
    }
}
