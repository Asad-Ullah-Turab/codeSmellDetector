package com.scd.codeSmellDetector;

/**
 * This is a test file containing various code smells for testing purposes
 */
public class TestFileWithSmells {
    private String name;
    private String email;
    private String phone;
    private String address;
    private int age;
    private double salary;
    private String department;
    private String manager;
    private String position;
    private boolean active;

    // Long Method - This method has way too many lines
    public void processUserDataAndValidateAndSaveAndNotify(String firstName, String lastName, String emailAddress,
            String phoneNumber, String homeAddress, int userAge,
            double monthlySalary, String deptName, String managerName,
            String jobPosition, boolean isActive) {
        System.out.println("Processing user data...");
        this.name = firstName + " " + lastName;
        this.email = emailAddress;
        this.phone = phoneNumber;
        this.address = homeAddress;
        this.age = userAge;
        this.salary = monthlySalary;
        this.department = deptName;
        this.manager = managerName;
        this.position = jobPosition;
        this.active = isActive;

        // Validate email
        if (emailAddress != null && emailAddress.contains("@")) {
            System.out.println("Email is valid");
        } else {
            System.out.println("Email is invalid");
        }

        // Validate phone
        if (phoneNumber != null && phoneNumber.length() >= 10) {
            System.out.println("Phone is valid");
        } else {
            System.out.println("Phone is invalid");
        }

        // Validate age
        if (userAge > 0 && userAge < 150) {
            System.out.println("Age is valid");
        } else {
            System.out.println("Age is invalid");
        }

        // Save to database
        System.out.println("Saving user to database...");
        saveUserToDatabase(firstName, lastName, emailAddress, phoneNumber, homeAddress);

        // Send notification
        System.out.println("Sending notification email...");
        sendNotificationEmail(emailAddress, firstName);

        // Log activity
        System.out.println("Logging activity...");
        logActivity("USER_CREATED", firstName + " " + lastName);
    }

    // Too Many Parameters
    public boolean updateUserInfo(String id, String name, String email, String phone, String address,
            int age, double salary, String dept, String manager, String position,
            boolean active, String createdDate, String updatedDate, String notes,
            String status) {
        System.out.println("Updating user info...");
        return true;
    }

    // God Object - too many methods (this is just part of a larger class)
    public void method1() {
        System.out.println("Method 1");
    }

    public void method2() {
        System.out.println("Method 2");
    }

    public void method3() {
        System.out.println("Method 3");
    }

    public void method4() {
        System.out.println("Method 4");
    }

    public void method5() {
        System.out.println("Method 5");
    }

    public void method6() {
        System.out.println("Method 6");
    }

    public void method7() {
        System.out.println("Method 7");
    }

    public void method8() {
        System.out.println("Method 8");
    }

    public void method9() {
        System.out.println("Method 9");
    }

    public void method10() {
        System.out.println("Method 10");
    }

    public void method11() {
        System.out.println("Method 11");
    }

    public void method12() {
        System.out.println("Method 12");
    }

    public void method13() {
        System.out.println("Method 13");
    }

    public void method14() {
        System.out.println("Method 14");
    }

    public void method15() {
        System.out.println("Method 15");
    }

    public void method16() {
        System.out.println("Method 16");
    }

    public void method17() {
        System.out.println("Method 17");
    }

    public void method18() {
        System.out.println("Method 18");
    }

    public void method19() {
        System.out.println("Method 19");
    }

    public void method20() {
        System.out.println("Method 20");
    }

    public void method21() {
        System.out.println("Method 21");
    }

    private void saveUserToDatabase(String firstName, String lastName, String email, String phone, String address) {
        System.out.println("Saving to DB: " + firstName + " " + lastName);
    }

    private void sendNotificationEmail(String email, String name) {
        System.out.println("Sending email to: " + email);
    }

    private void logActivity(String action, String details) {
        System.out.println("Logging: " + action + " - " + details);
    }
}
