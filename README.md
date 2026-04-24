# FitnessCenter-GymMembershipSystem

Backend is prepared for future Swing GUI integration without including GUI code yet.

## GUI-Ready Layer

Use `GymApplicationService` as the single entry point from Swing forms/panels.

- Admin auth: `loginAdmin`, `logoutAdmin`
- Member registration: `registerMember(MemberRegistrationRequest)`
- Trainer operations: `registerTrainer`, `setTrainerAvailability`, `assignTrainer`
- Class booking: `bookClass`
- Workout logging: `logWorkout(WorkoutLogRequest)`
- Equipment lifecycle: `registerEquipment`, `logEquipmentUsage`, `performEquipmentMaintenance`
- Room scheduling: `scheduleRoom`, `getRoomSchedules`
- Monthly reports: `generateMonthlyReport`
- Read models for UI: `getMemberInfo`, `getAllMemberInfos`, `getMembers`, `getTrainers`

## Persistence

Text files are created under `data/`:

- `members.txt`
- `trainers.txt`
- `class_bookings.txt`
- `workout_logs.txt`
- `equipment.txt`
- `room_schedules.txt`
- `reports/report_YYYY_MM.txt`

## Run Demo

```bash
javac src/fitnesscenter/gymmembershipsystem/*.java
java -cp src fitnesscenter.gymmembershipsystem.main
```

