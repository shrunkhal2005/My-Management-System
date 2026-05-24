public class Student {
 
    int studentId;
    String studentName;
    double marks;
 
    Student(int id, String name, double marks) {
        this.studentId = id;
        this.studentName = name;
        this.marks = marks;
    }
 
    void display() {
        System.out.println("ID: " + studentId + " | Name: " + studentName + " | Marks: " + marks);
    }
 
    public static void main(String[] args) {
 
        Student s1 = new Student(101, "Gobu Goba", 88.5);
        Student s2 = new Student(102, "soham rahangdale", 74.0);
        Student s3 = new Student(103, "Rohan Rajvaadi", 92.0);
 
        System.out.println("List of Students:-");
        s1.display();
        s2.display();
        s3.display();
 
        double highest = Math.max(s1.marks, Math.max(s2.marks, s3.marks));
        double average = (s1.marks + s2.marks + s3.marks) / 3;
 
        System.out.println("\nHighest Marks : " + highest);
        System.out.printf("Average Marks : %.2f%n", average);
    }
}
 