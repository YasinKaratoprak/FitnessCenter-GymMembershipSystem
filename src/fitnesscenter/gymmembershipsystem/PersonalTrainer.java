package fitnesscenter.gymmembershipsystem;

import java.util.HashMap;
import java.util.Map;

public class PersonalTrainer {
    private final String trainerName;
    private final TrainerSpecialization specialization;
    private AvailabilityStatus availabilityStatus;
    private int totalRatingScore;
    private int ratingCount;
    private final Map<String, AvailabilityStatus> availabilityBySlot;

    public PersonalTrainer(String trainerName, AvailabilityStatus availabilityStatus) {
        this(trainerName, availabilityStatus, TrainerSpecialization.GENERAL_FITNESS);
    }

    public PersonalTrainer(String trainerName, AvailabilityStatus availabilityStatus, TrainerSpecialization specialization) {
        if (trainerName == null || trainerName.trim().isEmpty()) {
            throw new IllegalArgumentException("Trainer name cannot be empty.");
        }
        this.trainerName = trainerName;
        this.availabilityStatus = availabilityStatus == null ? AvailabilityStatus.AVAILABLE : availabilityStatus;
        this.specialization = specialization == null ? TrainerSpecialization.GENERAL_FITNESS : specialization;
        this.totalRatingScore = 0;
        this.ratingCount = 0;
        this.availabilityBySlot = new HashMap<>();
    }

    public String getTrainerName() {
        return trainerName;
    }

    public AvailabilityStatus getAvailabilityStatus() {
        return availabilityStatus;
    }

    public TrainerSpecialization getSpecialization() {
        return specialization;
    }

    public void setAvailabilityStatus(AvailabilityStatus availabilityStatus) {
        if (availabilityStatus == null) {
            throw new IllegalArgumentException("Availability status cannot be null.");
        }
        this.availabilityStatus = availabilityStatus;
    }

    public void addUserRating(int rating) {
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5.");
        }
        totalRatingScore += rating;
        ratingCount++;
    }

    public double getAverageRating() {
        if (ratingCount == 0) {
            return 0.0;
        }
        return (double) totalRatingScore / ratingCount;
    }

    public int getRatingCount() {
        return ratingCount;
    }

    public String getTrainerSummary() {
        return "Trainer: " + trainerName
                + " (Specialization: " + specialization
                + ", Status: " + availabilityStatus
                + ", Rating: " + String.format("%.2f", getAverageRating())
                + " from " + ratingCount + " vote(s))";
    }

    public void setAvailabilityForSlot(String timeSlot, AvailabilityStatus status) {
        if (timeSlot == null || timeSlot.trim().isEmpty()) {
            throw new IllegalArgumentException("Time slot cannot be empty.");
        }
        if (status == null) {
            throw new IllegalArgumentException("Availability status cannot be null.");
        }
        availabilityBySlot.put(timeSlot.trim(), status);
    }

    public AvailabilityStatus getAvailabilityForSlot(String timeSlot) {
        if (timeSlot == null || timeSlot.trim().isEmpty()) {
            return availabilityStatus;
        }
        AvailabilityStatus slotStatus = availabilityBySlot.get(timeSlot.trim());
        return slotStatus == null ? availabilityStatus : slotStatus;
    }

    public boolean isAvailableForSlot(String timeSlot) {
        return getAvailabilityForSlot(timeSlot) == AvailabilityStatus.AVAILABLE;
    }
}

