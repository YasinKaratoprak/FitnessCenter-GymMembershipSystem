package fitnesscenter.gymmembershipsystem;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Locale;
import java.time.LocalDate;
import java.time.ZoneId;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class CRUD {

    private static final Path DATA_DIR = Paths.get("data");
    private static final Path MEMBERS_FILE = DATA_DIR.resolve("members.txt");
    private static final Path TRAINERS_FILE = DATA_DIR.resolve("trainers.txt");
    private static final Path BOOKINGS_FILE = DATA_DIR.resolve("class_bookings.txt");
    private static final Path WORKOUTS_FILE = DATA_DIR.resolve("workout_logs.txt");
    private static final Path EQUIPMENT_FILE = DATA_DIR.resolve("equipment.txt");
    private static final Path ROOM_SCHEDULE_FILE = DATA_DIR.resolve("room_schedules.txt");
    private static final Path REPORTS_DIR = DATA_DIR.resolve("reports");

    private final Map<String, Membership> membersById;
    private final Map<String, PersonalTrainer> trainersByName;
    private final Map<String, Equipment> equipmentsById;
    private final Map<String, RoomSchedule> roomSchedulesByName;
    private final List<TimestampedEvent> classBookings;
    private final List<TimestampedEvent> trainerSessions;
    private final List<TimestampedEvent> roomReservations;

    public CRUD() {
        this.membersById = new HashMap<>();
        this.trainersByName = new HashMap<>();
        this.equipmentsById = new HashMap<>();
        this.roomSchedulesByName = new HashMap<>();
        this.classBookings = new ArrayList<>();
        this.trainerSessions = new ArrayList<>();
        this.roomReservations = new ArrayList<>();
        initializeDataFiles();
        loadMembersFromFile();
    }

    private void loadMembersFromFile() {
        try {
            List<String> lines = Files.readAllLines(MEMBERS_FILE);
            for (String line : lines) {
                parseMemberLine(line);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load members from persistence file.", e);
        }
    }

    private void parseMemberLine(String line) {
        if (line == null || line.trim().isEmpty()) {
            return;
        }

        String[] parts = line.split("\\|", -1);
        if (parts.length < 8) {
            System.err.println("Skipping malformed member record: " + line);
            return;
        }

        try {
            String id = parts[0].trim();
            MembershipType membershipType = MembershipType.valueOf(parts[1].trim().toUpperCase());
            String name = parts[2].trim();
            String surname = parts[3].trim();
            double weight = Double.parseDouble(parts[4].trim());
            double height = Double.parseDouble(parts[5].trim());
            Date registrationDate;
            Date expireDate;
            try {
                registrationDate = parseDate(parts[6]);
            } catch (IllegalArgumentException dateError) {
                registrationDate = null;
            }
            try {
                expireDate = parseDate(parts[7]);
            } catch (IllegalArgumentException dateError) {
                expireDate = null;
            }

            Membership member = customerRegister(
                    membershipType,
                    name,
                    surname,
                    id,
                    registrationDate,
                    expireDate,
                    weight,
                    height);
            // Last valid record for the same ID wins.
            membersById.put(id, member);
        } catch (RuntimeException ex) {
            System.err.println("Skipping invalid member record: " + line + " | reason: " + ex.getMessage());
        }
    }

    private void initializeDataFiles() {
        try {
            Files.createDirectories(DATA_DIR);
            if (!Files.exists(MEMBERS_FILE)) {
                Files.createFile(MEMBERS_FILE);
            }
            if (!Files.exists(TRAINERS_FILE)) {
                Files.createFile(TRAINERS_FILE);
            }
            if (!Files.exists(BOOKINGS_FILE)) {
                Files.createFile(BOOKINGS_FILE);
            }
            if (!Files.exists(WORKOUTS_FILE)) {
                Files.createFile(WORKOUTS_FILE);
            }
            if (!Files.exists(EQUIPMENT_FILE)) {
                Files.createFile(EQUIPMENT_FILE);
            }
            if (!Files.exists(ROOM_SCHEDULE_FILE)) {
                Files.createFile(ROOM_SCHEDULE_FILE);
            }
            Files.createDirectories(REPORTS_DIR);
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize persistence files.", e);
        }
    }

    private void appendLine(Path file, String line) {
        try {
            Files.write(file, (line + System.lineSeparator()).getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            throw new RuntimeException("Failed to write to file: " + file, e);
        }
    }

    private String sanitize(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("|", "/").trim();
    }

    public Membership customerRegister(MembershipType membershipType, String name, String surname, String IDNumber,
                                       Date registDate, Date expireDate, double weight, double height) {
        if (membershipType == null) {
            throw new IllegalArgumentException("Membership type cannot be null.");
        }

        Membership customer;
        switch (membershipType) {
            case BASIC:
                customer = new BasicMembership(name, surname, IDNumber, registDate, expireDate, weight, height);
                break;
            case PREMIUM:
                customer = new PremiumMembership(name, surname, IDNumber, registDate, expireDate, weight, height);
                assignDefaultLessons(customer);
                break;
            case VIP:
                customer = new VIPMembership(name, surname, IDNumber, registDate, expireDate, weight, height);
                assignDefaultLessons(customer);
                break;
            default:
                throw new IllegalArgumentException("Invalid membership type: " + membershipType);
        }
        return customer;
    }

    private void assignDefaultLessons(Membership customer) {
        customer.addGroupLesson(new GroupLesson(LessonType.YOGA, "Selin", 18));
        customer.addGroupLesson(new GroupLesson(LessonType.SPINNING, "Murat", 20));
        customer.addGroupLesson(new GroupLesson(LessonType.CROSSFIT, "Deniz", 14));
        customer.addGroupLesson(new GroupLesson(LessonType.PILATES, "Ece", 16));
    }

    public Membership createMember(MembershipType membershipType, String name, String surname, String IDNumber,
                                   Date registDate, Date expireDate, double weight, double height) {
        if (IDNumber == null || IDNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("ID number cannot be empty.");
        }
        if (membersById.containsKey(IDNumber)) {
            throw new IllegalArgumentException("Member already exists with ID: " + IDNumber);
        }

        Membership member = customerRegister(membershipType, name, surname, IDNumber, registDate, expireDate, weight, height);
        membersById.put(IDNumber, member);
        appendLine(MEMBERS_FILE,
                sanitize(IDNumber) + "|" + membershipType + "|" + sanitize(name) + "|" + sanitize(surname)
                        + "|" + weight + "|" + height + "|" + serializeDate(registDate) + "|" + serializeDate(expireDate));
        return member;
    }

    public PersonalTrainer createTrainer(String trainerName, AvailabilityStatus availabilityStatus) {
        return createTrainer(trainerName, availabilityStatus, TrainerSpecialization.GENERAL_FITNESS);
    }

    public PersonalTrainer createTrainer(String trainerName, AvailabilityStatus availabilityStatus,
                                         TrainerSpecialization specialization) {
        if (trainerName == null || trainerName.trim().isEmpty()) {
            throw new IllegalArgumentException("Trainer name cannot be empty.");
        }

        String key = trainerName.trim().toUpperCase();
        if (trainersByName.containsKey(key)) {
            throw new IllegalArgumentException("Trainer already exists: " + trainerName);
        }

        PersonalTrainer trainer = new PersonalTrainer(trainerName, availabilityStatus, specialization);
        trainersByName.put(key, trainer);
        appendLine(TRAINERS_FILE,
                "CREATE|" + sanitize(trainerName) + "|" + availabilityStatus + "|" + trainer.getSpecialization());
        return trainer;
    }

    public Membership findMemberById(String IDNumber) {
        if (IDNumber == null) {
            return null;
        }
        return membersById.get(IDNumber);
    }

    public PersonalTrainer findTrainerByName(String trainerName) {
        if (trainerName == null) {
            return null;
        }
        return trainersByName.get(trainerName.trim().toUpperCase());
    }

    public void assignTrainerToMember(String IDNumber, String trainerName) {
        Membership member = findMemberById(IDNumber);
        if (member == null) {
            throw new IllegalArgumentException("Member not found with ID: " + IDNumber);
        }
        if (!member.hasPersonalTrainerAccess()) {
            throw new MembershipAccessDeniedException("This member does not have personal trainer access.");
        }

        PersonalTrainer trainer = findTrainerByName(trainerName);
        if (trainer == null) {
            throw new IllegalArgumentException("Trainer not found: " + trainerName);
        }
        member.setPersonalTrainer(trainer);
    }

    public void scheduleTrainerAvailability(String trainerName, String timeSlot, AvailabilityStatus status) {
        PersonalTrainer trainer = findTrainerByName(trainerName);
        if (trainer == null) {
            throw new IllegalArgumentException("Trainer not found: " + trainerName);
        }
        trainer.setAvailabilityForSlot(timeSlot, status);
        appendLine(TRAINERS_FILE,
                "SLOT|" + sanitize(trainerName) + "|" + sanitize(timeSlot) + "|" + status);
    }

    public void assignTrainerToMemberAtSlot(String IDNumber, String trainerName, String timeSlot) {
        Membership member = findMemberById(IDNumber);
        if (member == null) {
            throw new IllegalArgumentException("Member not found with ID: " + IDNumber);
        }
        if (!member.hasPersonalTrainerAccess()) {
            throw new MembershipAccessDeniedException("This member does not have personal trainer access.");
        }

        PersonalTrainer trainer = findTrainerByName(trainerName);
        if (trainer == null) {
            throw new IllegalArgumentException("Trainer not found: " + trainerName);
        }
        if (!trainer.isAvailableForSlot(timeSlot)) {
            throw new TrainerUnavailableException("Trainer is not available at slot: " + timeSlot);
        }

        member.setPersonalTrainer(trainer);
        trainer.setAvailabilityForSlot(timeSlot, AvailabilityStatus.BUSY);
        trainerSessions.add(new TimestampedEvent(new Date()));
        appendLine(TRAINERS_FILE,
                "ASSIGN|" + sanitize(trainerName) + "|" + sanitize(IDNumber) + "|" + sanitize(timeSlot));
    }

    public void bookGroupClass(String IDNumber, LessonType lessonType) {
        Membership member = findMemberById(IDNumber);
        if (member == null) {
            throw new IllegalArgumentException("Member not found with ID: " + IDNumber);
        }
        if (!member.hasGroupLessonAccess()) {
            throw new MembershipAccessDeniedException("This member does not have group lesson access.");
        }

        GroupLesson lessonToBook = null;
        for (GroupLesson lesson : member.getGroupLessons()) {
            if (lesson.getLessonType() == lessonType) {
                lessonToBook = lesson;
                break;
            }
        }

        if (lessonToBook == null) {
            throw new IllegalArgumentException("Lesson not found for member: " + lessonType);
        }

        lessonToBook.bookSeat();
        classBookings.add(new TimestampedEvent(new Date()));
        appendLine(BOOKINGS_FILE,
                sanitize(IDNumber) + "|" + lessonType + "|" + new Date());
    }

    public void logWorkout(String IDNumber, String workoutName, int durationMinutes, String notes) {
        Membership member = findMemberById(IDNumber);
        if (member == null) {
            throw new IllegalArgumentException("Member not found with ID: " + IDNumber);
        }

        WorkoutEntry workoutEntry = new WorkoutEntry(new Date(), workoutName, durationMinutes, notes);
        member.addWorkoutEntry(workoutEntry);
        appendLine(WORKOUTS_FILE,
                sanitize(IDNumber) + "|" + sanitize(workoutName) + "|" + durationMinutes + "|" + sanitize(notes)
                        + "|" + workoutEntry.getWorkoutDate());
    }

    public Equipment createEquipment(String equipmentId, String equipmentName) {
        if (equipmentsById.containsKey(equipmentId)) {
            throw new IllegalArgumentException("Equipment already exists with id: " + equipmentId);
        }
        Equipment equipment = new Equipment(equipmentId, equipmentName);
        equipmentsById.put(equipmentId, equipment);
        appendLine(EQUIPMENT_FILE, "CREATE|" + sanitize(equipmentId) + "|" + sanitize(equipmentName));
        return equipment;
    }

    public void logEquipmentUsage(String equipmentId, int usageMinutes) {
        Equipment equipment = equipmentsById.get(equipmentId);
        if (equipment == null) {
            throw new IllegalArgumentException("Equipment not found with id: " + equipmentId);
        }
        equipment.markInUse(usageMinutes);
        appendLine(EQUIPMENT_FILE,
                "USE|" + sanitize(equipmentId) + "|" + usageMinutes + "|" + new Date());
    }

    public void performEquipmentMaintenance(String equipmentId) {
        Equipment equipment = equipmentsById.get(equipmentId);
        if (equipment == null) {
            throw new IllegalArgumentException("Equipment not found with id: " + equipmentId);
        }
        equipment.performMaintenance(new Date());
        appendLine(EQUIPMENT_FILE,
                "MAINTENANCE|" + sanitize(equipmentId) + "|" + equipment.getLastMaintenanceDate());
    }

    public List<Equipment> listEquipment() {
        return new ArrayList<>(equipmentsById.values());
    }

    public void scheduleRoom(String roomName, String timeSlot, String purpose) {
        String key = roomName == null ? null : roomName.trim().toUpperCase();
        if (key == null || key.isEmpty()) {
            throw new IllegalArgumentException("Room name cannot be empty.");
        }

        RoomSchedule schedule = roomSchedulesByName.get(key);
        if (schedule == null) {
            schedule = new RoomSchedule(roomName);
            roomSchedulesByName.put(key, schedule);
        }
        schedule.bookSlot(timeSlot, purpose);
        roomReservations.add(new TimestampedEvent(new Date()));
        appendLine(ROOM_SCHEDULE_FILE,
                sanitize(roomName) + "|" + sanitize(timeSlot) + "|" + sanitize(purpose));
    }

    public List<RoomSchedule> listRoomSchedules() {
        return new ArrayList<>(roomSchedulesByName.values());
    }

    public String generateMonthlyPerformanceReport(int year, int month) {
        if (month < 1 || month > 12) {
            throw new IllegalArgumentException("Month must be between 1 and 12.");
        }

        int basicCount = 0;
        int premiumCount = 0;
        int vipCount = 0;
        for (Membership member : membersById.values()) {
            switch (member.getMembershipType()) {
                case BASIC:
                    basicCount++;
                    break;
                case PREMIUM:
                    premiumCount++;
                    break;
                case VIP:
                    vipCount++;
                    break;
                default:
                    break;
            }
        }

        int workouts = 0;
        int workoutMinutes = 0;
        for (Membership member : membersById.values()) {
            for (WorkoutEntry workoutEntry : member.getWorkoutHistory()) {
                if (isInMonth(workoutEntry.getWorkoutDate(), year, month)) {
                    workouts++;
                    workoutMinutes += workoutEntry.getDurationMinutes();
                }
            }
        }

        int monthlyClassBookings = countEventsInMonth(classBookings, year, month);
        int monthlyTrainerSessions = countEventsInMonth(trainerSessions, year, month);
        int monthlyRoomReservations = countEventsInMonth(roomReservations, year, month);

        int totalEquipmentUsage = 0;
        for (Equipment equipment : equipmentsById.values()) {
            totalEquipmentUsage += equipment.getTotalUsageMinutes();
        }

        String report = "Monthly Performance Report " + year + "-" + twoDigit(month)
                + "\nMembers: Basic=" + basicCount + ", Premium=" + premiumCount + ", VIP=" + vipCount
                + "\nClass Bookings: " + monthlyClassBookings
                + "\nTrainer Sessions: " + monthlyTrainerSessions
                + "\nWorkout Logs: " + workouts + " entries, " + workoutMinutes + " total minutes"
                + "\nRoom Reservations: " + monthlyRoomReservations
                + "\nEquipment Total Usage: " + totalEquipmentUsage + " minutes";

        Path reportFile = REPORTS_DIR.resolve("report_" + year + "_" + twoDigit(month) + ".txt");
        try {
            Files.write(reportFile, report.getBytes());
        } catch (IOException e) {
            throw new RuntimeException("Failed to write monthly report.", e);
        }
        return report;
    }

    private int countEventsInMonth(List<TimestampedEvent> events, int year, int month) {
        int count = 0;
        for (TimestampedEvent event : events) {
            if (isInMonth(event.eventDate, year, month)) {
                count++;
            }
        }
        return count;
    }

    private boolean isInMonth(Date date, int year, int month) {
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return localDate.getYear() == year && localDate.getMonthValue() == month;
    }

    private String twoDigit(int value) {
        return value < 10 ? "0" + value : String.valueOf(value);
    }

    public List<Membership> listMembers() {
        return new ArrayList<>(membersById.values());
    }

    public List<PersonalTrainer> listTrainers() {
        return new ArrayList<>(trainersByName.values());
    }

    public String getCustomerInfo(Membership customer) {
        if (customer == null) {
            return "Customer not found.";
        }

        return "Membership Type: " + customer.getMembershipType().getDisplayName()
                + "\nName: " + customer.getName()
                + "\nSurname: " + customer.getSurname()
                + "\nID Number: " + customer.getIDnumber()
                + "\nRegistration Date: " + formatDate(customer.getRegistirationDate())
                + "\nExpire Date: " + formatDate(customer.getExpireDate())
                + "\nWeight: " + customer.getWeight()
                + "\nHeight: " + customer.getHeight()
                + "\n" + customer.getAccessSummary()
                + "\n" + customer.getGroupLessonSummary()
                + "\n" + customer.getPersonalTrainerSummary()
                + "\n" + customer.getWorkoutHistorySummary();
    }

    public String getCustomerInfoById(String IDNumber) {
        return getCustomerInfo(findMemberById(IDNumber));
    }

    public List<String> getAllCustomerInfos() {
        List<String> infos = new ArrayList<>();
        for (Membership member : membersById.values()) {
            infos.add(getCustomerInfo(member));
        }
        return infos;
    }

    private String formatDate(Date date) {
        if (date == null) {
            return "-";
        }
        return date.toString();
    }

    private String serializeDate(Date date) {
        if (date == null) {
            return "-";
        }
        return String.valueOf(date.getTime());
    }

    private Date parseDate(String rawValue) {
        if (rawValue == null) {
            return null;
        }

        String value = rawValue.trim();
        if (value.isEmpty() || "-".equals(value)) {
            return null;
        }

        try {
            return new Date(Long.parseLong(value));
        } catch (NumberFormatException ignored) {
            // Support legacy date values previously stored with Date#toString().
        }

        try {
            String normalizedValue = normalizeLegacyDateValue(value);
            SimpleDateFormat legacyFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
            legacyFormat.setLenient(false);
            return legacyFormat.parse(normalizedValue);
        } catch (ParseException e) {
            throw new IllegalArgumentException("Invalid persisted date: " + rawValue);
        }
    }

    private String normalizeLegacyDateValue(String value) {
        // Java may not recognize legacy timezone abbreviations like TRT on all runtimes.
        return value
                .replace(" TRT ", " GMT+03:00 ")
                .replace(" EET ", " GMT+02:00 ")
                .replace(" EEST ", " GMT+03:00 ");
    }

    private static class TimestampedEvent {
        private final Date eventDate;

        private TimestampedEvent(Date eventDate) {
            this.eventDate = eventDate;
        }
    }
}
