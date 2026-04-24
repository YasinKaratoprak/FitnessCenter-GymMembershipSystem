package fitnesscenter.gymmembershipsystem;

import java.util.Date;

public class BasicMembership extends Membership {

    public BasicMembership() {
    }

    public BasicMembership(String name, String surname, String IDnumber, Date registirationDate, Date expireDate, double weight, double height) {
        super(name, surname, IDnumber, registirationDate, expireDate, weight, height);
    }

    @Override
    public MembershipType getMembershipType() {
        return MembershipType.BASIC;
    }

    @Override
    public boolean hasGymAccess() {
        return true;
    }

    @Override
    public boolean hasGroupLessonAccess() {
        return false;
    }

    @Override
    public boolean hasPersonalTrainerAccess() {
        return false;
    }
}
