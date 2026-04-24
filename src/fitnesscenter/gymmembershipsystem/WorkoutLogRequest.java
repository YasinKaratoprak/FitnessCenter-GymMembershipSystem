package fitnesscenter.gymmembershipsystem;

public class WorkoutLogRequest {
    private final String memberId;
    private final String workoutName;
    private final int durationMinutes;
    private final String notes;

    public WorkoutLogRequest(String memberId, String workoutName, int durationMinutes, String notes) {
        this.memberId = memberId;
        this.workoutName = workoutName;
        this.durationMinutes = durationMinutes;
        this.notes = notes;
    }

    public String getMemberId() {
        return memberId;
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
}

