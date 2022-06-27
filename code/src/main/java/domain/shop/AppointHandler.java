package domain.shop;

import domain.Exceptions.*;
import domain.user.User;

import java.util.ArrayList;
import java.util.List;

public class AppointHandler {
    private int appointmentNumberGen;
    private final List<OwnerAppointment> ownerAppointments;

    public AppointHandler(){
        ownerAppointments = new ArrayList<>();
        appointmentNumberGen = 1;
    }

    public int addNewAppoint(User userToAppoint, User appointUser, Shop shop , List<User> toConfirm) throws BidNotFoundException, IncorrectIdentification, InvalidSequenceOperationsExc, CriticalInvariantException, BlankDataExc {
        OwnerAppointment appointment = new OwnerAppointment(userToAppoint,appointUser,shop,toConfirm, appointmentNumberGen);
        appointmentNumberGen++;
        synchronized (ownerAppointments) {
            ownerAppointments.add(appointment);
        }
        return appointment.getId();
    }

    public void removeAppointment(int appointmentNumber){
        synchronized (ownerAppointments) {
            for (OwnerAppointment appointment : ownerAppointments) {
                if (appointment.getId() == appointmentNumber) {
                    ownerAppointments.remove(appointment);
                    return;
                }
            }
        }
    }

    public OwnerAppointment getAppointment(int appointmentNumber) throws BidNotFoundException {
        for(OwnerAppointment appointment: ownerAppointments){
            if (appointment.getId() == appointmentNumber)
                return appointment;
        }
        throw new BidNotFoundException(String.format("Bid %d was not found", appointmentNumber));
    }

    public void acceptAppoint(int appointmentNumber, User approver) throws BidNotFoundException, CriticalInvariantException, IncorrectIdentification, InvalidSequenceOperationsExc, BlankDataExc {
        getAppointment(appointmentNumber).approve(approver);
    }

    public void declineAppoint(int appointmentNumber, User decliner) throws BidNotFoundException, CriticalInvariantException {
        getAppointment(appointmentNumber).decline(decliner);
    }
}
