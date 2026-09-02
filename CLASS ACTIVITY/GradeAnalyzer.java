import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

class Student {
    String id;
    String name;
    Map<String, Integer> grades;

    Student(String id, String name, Map<String, Integer> grades) {
        this.id = id;
        this.name = name;
        this.grades = grades;
    }

    double getAverageGrade() {
        if (grades.isEmpty()) return 0;
        double sum = 0;
        for (int grade : grades.values()) {
            sum += grade;
        }
        return sum / grades.size();
    }

    int getFailedSubjectCount() {
        int count = 0;
        for (int grade : grades.values()) {
            if (grade < 40) {
                count++;
            }
        }
        return count;
    }

    @Override
    public String toString() {
        return "ID: " + id + " | Name: " + name + " | Average: " + String.format("%.2f", getAverageGrade());
    }
}

public class GradeAnalyzer {
    private ArrayList<Student> studentList = new ArrayList<>();

    public void addStudent(Student s) {
        studentList.add(s);
    }

    public void removeFailedStudents() {
        Iterator<Student> it = studentList.iterator();
        while (it.hasNext()) {
            if (it.next().getFailedSubjectCount() > 2) {
                it.remove();
            }
        }
    }

    public void displayTopThree() {
        ArrayList<Student> sortedList = new ArrayList<>(studentList);
        Collections.sort(sortedList, new Comparator<Student>() {
            @Override
            public int compare(Student s1, Student s2) {
                return Double.compare(s2.getAverageGrade(), s1.getAverageGrade());
            }
        });

        System.out.println("Top 3 Students:");
        int limit = Math.min(3, sortedList.size());
        for (int i = 0; i < limit; i++) {
            System.out.println(sortedList.get(i));
        }
    }

    public void displayAll() {
        Iterator<Student> it = studentList.iterator();
        while (it.hasNext()) {
            System.out.println(it.next());
        }
    }

    public static void main(String[] args) {
        GradeAnalyzer analyzer = new GradeAnalyzer();

        Map<String, Integer> g1 = new HashMap<>();
        g1.put("Math", 85); g1.put("Science", 90); g1.put("English", 78);
        analyzer.addStudent(new Student("1", "Alice", g1));

        Map<String, Integer> g2 = new HashMap<>();
        g2.put("Math", 30); g2.put("Science", 35); g2.put("English", 38); // Failed in 3
        analyzer.addStudent(new Student("2", "Bob", g2));

        Map<String, Integer> g3 = new HashMap<>();
        g3.put("Math", 95); g3.put("Science", 98); g3.put("English", 92);
        analyzer.addStudent(new Student("3", "Charlie", g3));

        Map<String, Integer> g4 = new HashMap<>();
        g4.put("Math", 70); g4.put("Science", 75); g4.put("English", 80);
        analyzer.addStudent(new Student("4", "David", g4));

        System.out.println("Initial Student Records:");
        analyzer.displayAll();

        System.out.println("\nRemoving students who failed in more than 2 subjects...");
        analyzer.removeFailedStudents();

        System.out.println("\nRemaining Students:");
        analyzer.displayAll();

        System.out.println("\nDisplaying Top 3 Students:");
        analyzer.displayTopThree();
    }
}
