package fitnesscenter.gymmembershipsystem;

import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class main {

    public static void main(String[] args) {
        GymApplicationService app = new GymApplicationService("admin", "1234");
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Fitness Center CLI ===");
        System.out.print("Admin username: ");
        String username = scanner.nextLine();
        System.out.print("Admin password: ");
        String password = scanner.nextLine();

        if (!app.loginAdmin(username, password)) {
            throw new SecurityException("Admin login failed.");
        }

        boolean running = true;
        while (running) {
            printMenu();
            String choice = scanner.nextLine();

            try {
                switch (choice) {
                    case "1":
                        registerMemberFlow(app, scanner);
                        break;
                    case "2":
                        registerTrainerFlow(app, scanner);
                        break;
                    case "3":
                        setTrainerAvailabilityFlow(app, scanner);
                        break;
                    case "4":
                        assignTrainerFlow(app, scanner);
                        break;
                    case "5":
                        bookClassFlow(app, scanner);
                        break;
                    case "6":
                        logWorkoutFlow(app, scanner);
                        break;
                    case "7":
                        registerEquipmentFlow(app, scanner);
                        break;
                    case "8":
                        logEquipmentUsageFlow(app, scanner);
                        break;
                    case "9":
                        performMaintenanceFlow(app, scanner);
                        break;
                    case "10":
                        scheduleRoomFlow(app, scanner);
                        break;
                    case "11":
                        showMemberInfoFlow(app, scanner);
                        break;
                    case "12":
                        showAllMembersFlow(app);
                        break;
                    case "13":
                        generateMonthlyReportFlow(app, scanner);
                        break;
                    case "0":
                        running = false;
                        break;
                    default:
                        System.out.println("Invalid choice. Try again.");
                }
            } catch (RuntimeException ex) {
                System.out.println("Operation failed: " + ex.getMessage());
            }
        }

        app.logoutAdmin();
        scanner.close();
        System.out.println("Goodbye.");
    }

    private static void printMenu() {
        System.out.println("\n--- Main Menu ---");
        System.out.println("1) Register Member");
        System.out.println("2) Register Trainer");
        System.out.println("3) Set Trainer Availability");
        System.out.println("4) Assign Trainer Session");
        System.out.println("5) Book Group Class");
        System.out.println("6) Log Workout");
        System.out.println("7) Register Equipment");
        System.out.println("8) Log Equipment Usage");
        System.out.println("9) Perform Equipment Maintenance");
        System.out.println("10) Schedule Room");
        System.out.println("11) Show Member Info");
        System.out.println("12) Show All Member Infos");
        System.out.println("13) Generate Monthly Report");
        System.out.println("0) Exit");
        System.out.print("Select: ");
    }

    private static void registerMemberFlow(GymApplicationService app, Scanner scanner) {
        System.out.print("Membership type (BASIC/PREMIUM/VIP): ");
        MembershipType type = MembershipType.valueOf(scanner.nextLine().trim().toUpperCase());
        System.out.print("Name: ");
        String name = scanner.nextLine();
        System.out.print("Surname: ");
        String surname = scanner.nextLine();
        System.out.print("ID Number: ");
        String id = scanner.nextLine();
        System.out.print("Weight: ");
        double weight = Double.parseDouble(scanner.nextLine());
        System.out.print("Height: ");
        double height = Double.parseDouble(scanner.nextLine());

        app.registerMember(new MemberRegistrationRequest(type, name, surname, id, new Date(), null, weight, height));
        System.out.println("Member registered.");
    }

    private static void registerTrainerFlow(GymApplicationService app, Scanner scanner) {
        System.out.print("Trainer name: ");
        String name = scanner.nextLine();
        System.out.print("Availability (AVAILABLE/BUSY/ON_LEAVE): ");
        AvailabilityStatus status = AvailabilityStatus.valueOf(scanner.nextLine().trim().toUpperCase());
        System.out.print("Specialization (GENERAL_FITNESS/YOGA/SPINNING/CROSSFIT/PILATES/STRENGTH_CONDITIONING): ");
        TrainerSpecialization specialization = TrainerSpecialization.valueOf(scanner.nextLine().trim().toUpperCase());

        app.registerTrainer(name, status, specialization);
        System.out.println("Trainer registered.");
    }

    private static void setTrainerAvailabilityFlow(GymApplicationService app, Scanner scanner) {
        System.out.print("Trainer name: ");
        String trainerName = scanner.nextLine();
        System.out.print("Time slot (example: 2026-04-25 10:00): ");
        String timeSlot = scanner.nextLine();
        System.out.print("Availability (AVAILABLE/BUSY/ON_LEAVE): ");
        AvailabilityStatus status = AvailabilityStatus.valueOf(scanner.nextLine().trim().toUpperCase());

        app.setTrainerAvailability(trainerName, timeSlot, status);
        System.out.println("Trainer availability updated.");
    }

    private static void assignTrainerFlow(GymApplicationService app, Scanner scanner) {
        System.out.print("Member ID: ");
        String memberId = scanner.nextLine();
        System.out.print("Trainer name: ");
        String trainerName = scanner.nextLine();
        System.out.print("Time slot: ");
        String timeSlot = scanner.nextLine();

        app.assignTrainer(memberId, trainerName, timeSlot);
        System.out.println("Trainer assigned.");
    }

    private static void bookClassFlow(GymApplicationService app, Scanner scanner) {
        System.out.print("Member ID: ");
        String memberId = scanner.nextLine();
        System.out.print("Class type (YOGA/SPINNING/CROSSFIT/PILATES): ");
        LessonType lessonType = LessonType.valueOf(scanner.nextLine().trim().toUpperCase());

        app.bookClass(memberId, lessonType);
        System.out.println("Class booked.");
    }

    private static void logWorkoutFlow(GymApplicationService app, Scanner scanner) {
        System.out.print("Member ID: ");
        String memberId = scanner.nextLine();
        System.out.print("Workout name: ");
        String workoutName = scanner.nextLine();
        System.out.print("Duration (minutes): ");
        int duration = Integer.parseInt(scanner.nextLine());
        System.out.print("Notes: ");
        String notes = scanner.nextLine();

        app.logWorkout(new WorkoutLogRequest(memberId, workoutName, duration, notes));
        System.out.println("Workout logged.");
    }

    private static void registerEquipmentFlow(GymApplicationService app, Scanner scanner) {
        System.out.print("Equipment ID: ");
        String equipmentId = scanner.nextLine();
        System.out.print("Equipment name: ");
        String equipmentName = scanner.nextLine();

        app.registerEquipment(equipmentId, equipmentName);
        System.out.println("Equipment registered.");
    }

    private static void logEquipmentUsageFlow(GymApplicationService app, Scanner scanner) {
        System.out.print("Equipment ID: ");
        String equipmentId = scanner.nextLine();
        System.out.print("Usage minutes: ");
        int usage = Integer.parseInt(scanner.nextLine());

        app.logEquipmentUsage(equipmentId, usage);
        System.out.println("Equipment usage logged.");
    }

    private static void performMaintenanceFlow(GymApplicationService app, Scanner scanner) {
        System.out.print("Equipment ID: ");
        String equipmentId = scanner.nextLine();

        app.performEquipmentMaintenance(equipmentId);
        System.out.println("Maintenance completed.");
    }

    private static void scheduleRoomFlow(GymApplicationService app, Scanner scanner) {
        System.out.print("Room name: ");
        String roomName = scanner.nextLine();
        System.out.print("Time slot: ");
        String timeSlot = scanner.nextLine();
        System.out.print("Purpose: ");
        String purpose = scanner.nextLine();

        app.scheduleRoom(roomName, timeSlot, purpose);
        System.out.println("Room scheduled.");
    }

    private static void showMemberInfoFlow(GymApplicationService app, Scanner scanner) {
        System.out.print("Member ID: ");
        String memberId = scanner.nextLine();
        System.out.println(app.getMemberInfo(memberId));
    }

    private static void showAllMembersFlow(GymApplicationService app) {
        List<String> infos = app.getAllMemberInfos();
        if (infos.isEmpty()) {
            System.out.println("No members found.");
            return;
        }
        for (String info : infos) {
            System.out.println(info);
            System.out.println("--------------------");
        }
    }

    private static void generateMonthlyReportFlow(GymApplicationService app, Scanner scanner) {
        System.out.print("Year (YYYY): ");
        int year = Integer.parseInt(scanner.nextLine());
        System.out.print("Month (1-12): ");
        int month = Integer.parseInt(scanner.nextLine());

        String report = app.generateMonthlyReport(year, month);
        System.out.println(report);
    }
}