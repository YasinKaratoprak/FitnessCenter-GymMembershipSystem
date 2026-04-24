package fitnesscenter.gymmembershipsystem;

import java.util.List;

public class GymApplicationService {

    private final CRUD crud;
    private final AdminService adminService;

    public GymApplicationService(String adminUsername, String adminPassword) {
        this.crud = new CRUD();
        this.adminService = new AdminService(adminUsername, adminPassword, crud);
    }

    public boolean loginAdmin(String username, String password) {
        return adminService.login(username, password);
    }

    public void logoutAdmin() {
        adminService.logout();
    }

    public Membership registerMember(MemberRegistrationRequest request) {
        return adminService.defineNewMember(
                request.getMembershipType(),
                request.getName(),
                request.getSurname(),
                request.getIdNumber(),
                request.getRegistrationDate(),
                request.getExpireDate(),
                request.getWeight(),
                request.getHeight());
    }

    public PersonalTrainer registerTrainer(String trainerName, AvailabilityStatus status) {
        return adminService.definePersonalTrainer(trainerName, status);
    }

    public PersonalTrainer registerTrainer(String trainerName, AvailabilityStatus status,
                                           TrainerSpecialization specialization) {
        return adminService.definePersonalTrainer(trainerName, status, specialization);
    }

    public void setTrainerAvailability(String trainerName, String timeSlot, AvailabilityStatus status) {
        adminService.setTrainerAvailabilityForSlot(trainerName, timeSlot, status);
    }

    public void assignTrainer(String memberId, String trainerName, String timeSlot) {
        adminService.assignTrainerToMemberAtSlot(memberId, trainerName, timeSlot);
    }

    public void bookClass(String memberId, LessonType lessonType) {
        adminService.bookGroupClassForMember(memberId, lessonType);
    }

    public void logWorkout(WorkoutLogRequest request) {
        adminService.logWorkoutForMember(
                request.getMemberId(),
                request.getWorkoutName(),
                request.getDurationMinutes(),
                request.getNotes());
    }

    public Equipment registerEquipment(String equipmentId, String equipmentName) {
        return adminService.defineEquipment(equipmentId, equipmentName);
    }

    public void logEquipmentUsage(String equipmentId, int usageMinutes) {
        adminService.logEquipmentUsage(equipmentId, usageMinutes);
    }

    public void performEquipmentMaintenance(String equipmentId) {
        adminService.performEquipmentMaintenance(equipmentId);
    }

    public void scheduleRoom(String roomName, String timeSlot, String purpose) {
        adminService.scheduleRoom(roomName, timeSlot, purpose);
    }

    public List<Equipment> getEquipment() {
        return adminService.getAllEquipment();
    }

    public List<RoomSchedule> getRoomSchedules() {
        return adminService.getAllRoomSchedules();
    }

    public String generateMonthlyReport(int year, int month) {
        return adminService.generateMonthlyPerformanceReport(year, month);
    }

    public String getMemberInfo(String memberId) {
        return crud.getCustomerInfoById(memberId);
    }

    public List<String> getAllMemberInfos() {
        return crud.getAllCustomerInfos();
    }

    public List<Membership> getMembers() {
        return adminService.getAllMembers();
    }

    public List<PersonalTrainer> getTrainers() {
        return adminService.getAllTrainers();
    }
}

