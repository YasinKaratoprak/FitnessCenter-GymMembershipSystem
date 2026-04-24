package fitnesscenter.gymmembershipsystem;

public class GroupLesson {
    private final LessonType lessonType;
    private final String instructorName;
    private final int maxParticipantCount;
    private int bookedParticipantCount;

    public GroupLesson(LessonType lessonType, String instructorName, int maxParticipantCount) {
        if (lessonType == null) {
            throw new IllegalArgumentException("Lesson type cannot be null.");
        }
        if (maxParticipantCount <= 0) {
            throw new IllegalArgumentException("Max participant count must be greater than zero.");
        }
        this.lessonType = lessonType;
        this.instructorName = instructorName;
        this.maxParticipantCount = maxParticipantCount;
        this.bookedParticipantCount = 0;
    }

    public LessonType getLessonType() {
        return lessonType;
    }

    public String getInstructorName() {
        return instructorName;
    }

    public int getMaxParticipantCount() {
        return maxParticipantCount;
    }

    public int getBookedParticipantCount() {
        return bookedParticipantCount;
    }

    public boolean hasAvailableSeat() {
        return bookedParticipantCount < maxParticipantCount;
    }

    public void bookSeat() {
        if (!hasAvailableSeat()) {
            throw new ClassFullException("Class is full for lesson type: " + lessonType);
        }
        bookedParticipantCount++;
    }

    public String getLessonDetails() {
        return lessonType.name()
                + " (Difficulty: " + lessonType.getDifficulty()
                + ", Duration: " + lessonType.getDurationMinutes() + " min"
                + ", Equipment: " + lessonType.getRequiredEquipment()
                + ", Instructor: " + instructorName
                + ", Capacity: " + bookedParticipantCount + "/" + maxParticipantCount
                + ")";
    }
}

