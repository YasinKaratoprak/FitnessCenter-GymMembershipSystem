package fitnesscenter.gymmembershipsystem;

import java.util.Date;

public class MemberRegistrationRequest {
    private final MembershipType membershipType;
    private final String name;
    private final String surname;
    private final String idNumber;
    private final Date registrationDate;
    private final Date expireDate;
    private final double weight;
    private final double height;

    public MemberRegistrationRequest(MembershipType membershipType, String name, String surname, String idNumber,
                                     Date registrationDate, Date expireDate, double weight, double height) {
        this.membershipType = membershipType;
        this.name = name;
        this.surname = surname;
        this.idNumber = idNumber;
        this.registrationDate = registrationDate;
        this.expireDate = expireDate;
        this.weight = weight;
        this.height = height;
    }

    public MembershipType getMembershipType() {
        return membershipType;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public Date getExpireDate() {
        return expireDate;
    }

    public double getWeight() {
        return weight;
    }

    public double getHeight() {
        return height;
    }
}

