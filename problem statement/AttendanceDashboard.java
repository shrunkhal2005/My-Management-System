import java.util.Scanner;

public class AttendanceDashboard {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        String employeeName = "";
        String attendanceStatus = "";
        int workingDays = 0;
        int hoursPerDay = 0;

        int choice;

        do {
            System.out.println("\n=== Employee Attendance Dashboard ===");
            System.out.println("1. Mark Attendance");
            System.out.println("2. View Employee Details");
            System.out.println("3. Calculate Working Hours");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");
            choice = sc.nextInt();
            sc.nextLine(); // consume newline

            switch (choice) {

                case 1:
                    System.out.print("Enter Employee Name: ");
                    employeeName = sc.nextLine();
                    System.out.print("Enter Attendance Status (Present/Absent): ");
                    attendanceStatus = sc.nextLine();
                    System.out.println("Attendance marked for " + employeeName);
                    break;

                case 2:
                    if (employeeName.isEmpty()) {
                        System.out.println("No employee data found. Please mark attendance first.");
                    } else {
                        System.out.println("\n--- Employee Details ---");
                        System.out.println("Name   : " + employeeName);
                        System.out.println("Status : " + attendanceStatus);
                    }
                    break;

                case 3:
                    System.out.print("Enter Number of Working Days: ");
                    workingDays = sc.nextInt();
                    System.out.print("Enter Hours per Day: ");
                    hoursPerDay = sc.nextInt();
                    int totalHours = workingDays * hoursPerDay;
                    System.out.println("Total Working Hours: " + totalHours + " hrs");
                    break;

                case 4:
                    System.out.println("Exiting... Goodbye!");
                    break;

                default:
                    System.out.println("Invalid choice. Please enter 1 to 4.");
            }

        } while (choice != 4);
    }
}