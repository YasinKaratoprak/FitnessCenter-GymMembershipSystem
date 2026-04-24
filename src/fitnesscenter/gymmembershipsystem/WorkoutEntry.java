package fitnesscenter.gymmembershipsystem;

import java.util.Date;

public class WorkoutEntry {
    private final Date workoutDate;
    private final String workoutName;
    private final int durationMinutes;
    private final String notes;

    public WorkoutEntry(Date workoutDate, String workoutName, int durationMinutes, String notes) {
        if (workoutDate == null) {
            throw new IllegalArgumentException("Workout date cannot be null.");
        }
        if (workoutName == null || workoutName.trim().isEmpty()) {
            throw new IllegalArgumentException("Workout name cannot be empty.");
        }
        if (durationMinutes <= 0) {
            throw new IllegalArgumentException("Workout duration must be greater than zero.");
        }

        this.workoutDate = workoutDate;
        this.workoutName = workoutName;
        this.durationMinutes = durationMinutes;
        this.notes = notes == null ? "" : notes;
    }

    public Date getWorkoutDate() {
        return workoutDate;
    }

    public String getWorkoutName() {
        return workoutName;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public String getNotes() {
        return notes;
    }

    public String getSummary() {
        return workoutDate + " - " + workoutName + " (" + durationMinutes + " min)"
                + (notes.isEmpty() ? "" : " Notes: " + notes);
    }
}

