class Person {
    String name;
    int age;

    Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    void displayDetails() {
        System.out.println("Name: " + name);
        System.out.println("Age: " + age);
    }
}

class Employee extends Person {
    String employeeId;
    double salary;

    Employee(String name, int age, String employeeId, double salary) {
        super(name, age);
        this.employeeId = employeeId;
        this.salary = salary;
    }

    @Override
    void displayDetails() {
        super.displayDetails();
        System.out.println("Employee ID: " + employeeId);
        System.out.println("Salary: $" + salary);
    }
}

class Manager extends Employee {
    int teamSize;
    double bonus;

    Manager(String name, int age, String employeeId, double salary, int teamSize, double bonus) {
        super(name, age, employeeId, salary);
        this.teamSize = teamSize;
        this.bonus = bonus;
    }

    @Override
    void displayDetails() {
        super.displayDetails();
        System.out.println("Team Size: " + teamSize);
        System.out.println("Bonus: $" + bonus);
    }
}

public class InheritanceDemo {
    public static void main(String[] args) {
        Manager mgr = new Manager("Alice Vance", 35, "M104", 95000.00, 8, 12000.00);
        mgr.displayDetails();
    }
}
