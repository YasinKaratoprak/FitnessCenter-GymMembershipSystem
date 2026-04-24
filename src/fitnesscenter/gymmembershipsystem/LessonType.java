package fitnesscenter.gymmembershipsystem;

public enum LessonType {
    YOGA("Low", 60, "Mat"),
    SPINNING("Medium", 45, "Stationary Bike"),
    CROSSFIT("High", 50, "Functional Equipment"),
    PILATES("Low-Medium", 55, "Mat + Band");

    private final String difficulty;
    private final int durationMinutes;
    private final String requiredEquipment;

    LessonType(String difficulty, int durationMinutes, String requiredEquipment) {
        this.difficulty = difficulty;
        this.durationMinutes = durationMinutes;
        this.requiredEquipment = requiredEquipment;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public String getRequiredEquipment() {
        return requiredEquipment;
    }
}

