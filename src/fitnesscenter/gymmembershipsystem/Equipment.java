package fitnesscenter.gymmembershipsystem;

import java.util.Date;

public class Equipment {
    private final String equipmentId;
    private final String equipmentName;
    private EquipmentStatus status;
    private int totalUsageMinutes;
    private Date lastMaintenanceDate;

    public Equipment(String equipmentId, String equipmentName) {
        if (equipmentId == null || equipmentId.trim().isEmpty()) {
            throw new IllegalArgumentException("Equipment id cannot be empty.");
        }
        if (equipmentName == null || equipmentName.trim().isEmpty()) {
            throw new IllegalArgumentException("Equipment name cannot be empty.");
        }
        this.equipmentId = equipmentId;
        this.equipmentName = equipmentName;
        this.status = EquipmentStatus.AVAILABLE;
        this.totalUsageMinutes = 0;
        this.lastMaintenanceDate = null;
    }

    public String getEquipmentId() {
        return equipmentId;
    }

    public String getEquipmentName() {
        return equipmentName;
    }

    public EquipmentStatus getStatus() {
        return status;
    }

    public int getTotalUsageMinutes() {
        return totalUsageMinutes;
    }

    public Date getLastMaintenanceDate() {
        return lastMaintenanceDate;
    }

    public void markInUse(int usageMinutes) {
        if (usageMinutes <= 0) {
            throw new IllegalArgumentException("Usage minutes must be greater than zero.");
        }
        if (status == EquipmentStatus.UNDER_MAINTENANCE) {
            throw new EquipmentUnavailableException("Equipment is under maintenance: " + equipmentId);
        }
        status = EquipmentStatus.IN_USE;
        totalUsageMinutes += usageMinutes;
        status = EquipmentStatus.AVAILABLE;
    }

    public void performMaintenance(Date maintenanceDate) {
        status = EquipmentStatus.UNDER_MAINTENANCE;
        lastMaintenanceDate = maintenanceDate == null ? new Date() : maintenanceDate;
        status = EquipmentStatus.AVAILABLE;
    }

    public String getSummary() {
        return equipmentId + " - " + equipmentName
                + " (Status: " + status
                + ", Total Usage: " + totalUsageMinutes + " min"
                + ", Last Maintenance: " + (lastMaintenanceDate == null ? "-" : lastMaintenanceDate)
                + ")";
    }
}

