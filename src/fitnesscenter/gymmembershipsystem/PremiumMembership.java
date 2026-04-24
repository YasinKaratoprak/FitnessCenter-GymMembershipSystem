package fitnesscenter.gymmembershipsystem;

import java.util.Date;

public class PremiumMembership extends Membership {

    public PremiumMembership() {
    }

    public PremiumMembership(String name, String surname, String IDnumber, Date registirationDate, Date expireDate, double weight, double height) {
        super(name, surname, IDnumber, registirationDate, expireDate, weight, height);
    }

    @Override
    public MembershipType getMembershipType() {
        return MembershipType.PREMIUM;
    }

    @Override
    public boolean hasGymAccess() {
        return true;
    }

    @Override
    public boolean hasGroupLessonAccess() {
        return true;
    }

    @Override
    public boolean hasPersonalTrainerAccess() {
        return false;
    }
}

