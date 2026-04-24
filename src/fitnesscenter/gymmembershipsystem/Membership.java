package fitnesscenter.gymmembershipsystem;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public abstract class Membership {
    // Default user details
    String name;
    String surname;
    String IDnumber; // Citizen ID
    Date registirationDate;
    Date expireDate;
    double weight;
    double height;

    private final List<GroupLesson> groupLessons;
    private final List<WorkoutEntry> workoutHistory;
    private PersonalTrainer personalTrainer;

    public Membership() {
        this.groupLessons = new ArrayList<>();
        this.workoutHistory = new ArrayList<>();
    }

    public Membership(String name, String surname, String IDnumber, Date registirationDate, Date expireDate, double weight, double height) {
        this.name = name;
        this.surname = surname;
        this.IDnumber = IDnumber;
        this.registirationDate = registirationDate;
        this.expireDate = expireDate;
        this.weight = weight;
        this.height = height;
        this.groupLessons = new ArrayList<>();
        this.workoutHistory = new ArrayList<>();
    }

    public abstract MembershipType getMembershipType();

    public abstract boolean hasGymAccess();

    public abstract boolean hasGroupLessonAccess();

    public abstract boolean hasPersonalTrainerAccess();

    public String getAccessSummary() {
        return "Gym: " + yesNo(hasGymAccess())
                + "\nGroup Lesson: " + yesNo(hasGroupLessonAccess())
                + "\nPersonal Trainer: " + yesNo(hasPersonalTrainerAccess());
    }

    private String yesNo(boolean value) {
        return value ? "Yes" : "No";
    }

    public void addGroupLesson(GroupLesson lesson) {
        if (!hasGroupLessonAccess()) {
            throw new MembershipAccessDeniedException("This membership does not include group lessons.");
        }
        if (lesson == null) {
            throw new IllegalArgumentException("Lesson cannot be null.");
        }
        groupLessons.add(lesson);
    }

    public List<GroupLesson> getGroupLessons() {
        return new ArrayList<>(groupLessons);
    }

    public String getGroupLessonSummary() {
        if (!hasGroupLessonAccess()) {
            return "Group lessons are not available for this membership.";
        }
        if (groupLessons.isEmpty()) {
            return "No group lesson assigned yet.";
        }

        StringBuilder builder = new StringBuilder("Group Lessons:");
        for (GroupLesson lesson : groupLessons) {
            builder.append("\n- ").append(lesson.getLessonDetails());
        }
        return builder.toString();
    }

    public void addWorkoutEntry(WorkoutEntry workoutEntry) {
        if (workoutEntry == null) {
            throw new IllegalArgumentException("Workout entry cannot be null.");
        }
        workoutHistory.add(workoutEntry);
    }

    public List<WorkoutEntry> getWorkoutHistory() {
        return new ArrayList<>(workoutHistory);
    }

    public String getWorkoutHistorySummary() {
        if (workoutHistory.isEmpty()) {
            return "Workout History: No workout recorded yet.";
        }

        StringBuilder builder = new StringBuilder("Workout History:");
        for (WorkoutEntry workoutEntry : workoutHistory) {
            builder.append("\n- ").append(workoutEntry.getSummary());
        }
        return builder.toString();
    }

    public PersonalTrainer getPersonalTrainer() {
        return personalTrainer;
    }

    public void setPersonalTrainer(PersonalTrainer personalTrainer) {
        if (!hasPersonalTrainerAccess() && personalTrainer != null) {
            throw new MembershipAccessDeniedException("This membership does not include personal trainer.");
        }
        this.personalTrainer = personalTrainer;
    }

    public String getPersonalTrainerSummary() {
        if (!hasPersonalTrainerAccess()) {
            return "Personal trainer is not available for this membership.";
        }
        if (personalTrainer == null) {
            return "No personal trainer assigned yet.";
        }
        return personalTrainer.getTrainerSummary();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getIDnumber() {
        return IDnumber;
    }

    public void setIDnumber(String IDnumber) {
        this.IDnumber = IDnumber;
    }

    public Date getRegistirationDate() {
        return registirationDate;
    }

    public void setRegistirationDate(Date registirationDate) {
        this.registirationDate = registirationDate;
    }

    public Date getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }
}
