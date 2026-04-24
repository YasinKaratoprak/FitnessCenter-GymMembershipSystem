package fitnesscenter.gymmembershipsystem;

import java.util.Date;
import java.util.List;

public class AdminService {

    private final String adminUsername;
    private final String adminPassword;
    private final CRUD crud;
    private boolean loggedIn;
    private UserRole currentRole;

    public AdminService(String adminUsername, String adminPassword, CRUD crud) {
        if (adminUsername == null || adminUsername.trim().isEmpty()) {
            throw new IllegalArgumentException("Admin username cannot be empty.");
        }
        if (adminPassword == null || adminPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("Admin password cannot be empty.");
        }
        if (crud == null) {
            throw new IllegalArgumentException("CRUD service cannot be null.");
        }
        this.adminUsername = adminUsername;
        this.adminPassword = adminPassword;
        this.crud = crud;
        this.loggedIn = false;
        this.currentRole = null;
    }

    public boolean login(String username, String password) {
        if (adminUsername.equals(username) && adminPassword.equals(password)) {
            loggedIn = true;
            currentRole = UserRole.ADMIN;
            return true;
        }
        return false;
    }

    public void logout() {
        loggedIn = false;
        currentRole = null;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public String getAdminUsername() {
        return adminUsername;
    }

    private void requireAdminAccess() {
        if (!loggedIn || currentRole != UserRole.ADMIN) {
            throw new SecurityException("Admin login required.");
        }
    }

    public Membership defineNewMember(MembershipType membershipType, String name, String surname, String IDNumber,
                                      Date registDate, Date expireDate, double weight, double height) {
        requireAdminAccess();
        return crud.createMember(membershipType, name, surname, IDNumber, registDate, expireDate, weight, height);
    }

    public PersonalTrainer definePersonalTrainer(String trainerName, AvailabilityStatus status) {
        requireAdminAccess();
        return crud.createTrainer(trainerName, status);
    }

    public PersonalTrainer definePersonalTrainer(String trainerName, AvailabilityStatus status,
                                                 TrainerSpecialization specialization) {
        requireAdminAccess();
        return crud.createTrainer(trainerName, status, specialization);
    }

    public void assignTrainerToMember(String memberId, String trainerName) {
        requireAdminAccess();
        crud.assignTrainerToMember(memberId, trainerName);
    }

    public void assignTrainerToMemberAtSlot(String memberId, String trainerName, String timeSlot) {
        requireAdminAccess();
        crud.assignTrainerToMemberAtSlot(memberId, trainerName, timeSlot);
    }

    public void setTrainerAvailabilityForSlot(String trainerName, String timeSlot, AvailabilityStatus status) {
        requireAdminAccess();
        crud.scheduleTrainerAvailability(trainerName, timeSlot, status);
    }

    public void bookGroupClassForMember(String memberId, LessonType lessonType) {
        requireAdminAccess();
        crud.bookGroupClass(memberId, lessonType);
    }

    public void logWorkoutForMember(String memberId, String workoutName, int durationMinutes, String notes) {
        requireAdminAccess();
        crud.logWorkout(memberId, workoutName, durationMinutes, notes);
    }

    public Equipment defineEquipment(String equipmentId, String equipmentName) {
        requireAdminAccess();
        return crud.createEquipment(equipmentId, equipmentName);
    }

    public void logEquipmentUsage(String equipmentId, int usageMinutes) {
        requireAdminAccess();
        crud.logEquipmentUsage(equipmentId, usageMinutes);
    }

    public void performEquipmentMaintenance(String equipmentId) {
        requireAdminAccess();
        crud.performEquipmentMaintenance(equipmentId);
    }

    public void scheduleRoom(String roomName, String timeSlot, String purpose) {
        requireAdminAccess();
        crud.scheduleRoom(roomName, timeSlot, purpose);
    }

    public List<Equipment> getAllEquipment() {
        requireAdminAccess();
        return crud.listEquipment();
    }

    public List<RoomSchedule> getAllRoomSchedules() {
        requireAdminAccess();
        return crud.listRoomSchedules();
    }

    public String generateMonthlyPerformanceReport(int year, int month) {
        requireAdminAccess();
        return crud.generateMonthlyPerformanceReport(year, month);
    }

    public List<Membership> getAllMembers() {
        requireAdminAccess();
        return crud.listMembers();
    }

    public List<PersonalTrainer> getAllTrainers() {
        requireAdminAccess();
        return crud.listTrainers();
    }
}


