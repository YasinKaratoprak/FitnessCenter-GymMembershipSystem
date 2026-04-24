package fitnesscenter.gymmembershipsystem;

public enum MembershipType {
    BASIC,
    PREMIUM,
    VIP;

    public String getDisplayName() {
        switch (this) {
            case BASIC:
                return "Basic";
            case PREMIUM:
                return "Premium";
            case VIP:
                return "VIP";
            default:
                throw new IllegalStateException("Unhandled membership type: " + this);
        }
    }
}


