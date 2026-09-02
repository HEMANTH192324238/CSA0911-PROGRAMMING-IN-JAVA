import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

class Employee {
    String id;
    String name;
    String department;
    double salary;

    Employee(String id, String name, String department, double salary) {
        this.id = id;
        this.name = name;
        this.department = department;
        this.salary = salary;
    }

    @Override
    public String toString() {
        return "ID: " + id + " | Name: " + name + " | Dept: " + department + " | Salary: $" + salary;
    }
}

public class PayrollSystem {
    private HashMap<String, List<Employee>> departmentMap = new HashMap<>();

    public void addEmployee(Employee emp) {
        departmentMap.putIfAbsent(emp.department, new ArrayList<>());
        departmentMap.get(emp.department).add(emp);
    }

    public void calculateSalaryExpense() {
        System.out.println("Salary Expense Per Department:");
        for (Map.Entry<String, List<Employee>> entry : departmentMap.entrySet()) {
            double total = 0;
            for (Employee emp : entry.getValue()) {
                total += emp.salary;
            }
            System.out.println(entry.getKey() + ": $" + total);
        }
    }

    public void findHighestPaid() {
        System.out.println("Highest-Paid Employee Per Department:");
        for (Map.Entry<String, List<Employee>> entry : departmentMap.entrySet()) {
            Employee highest = null;
            for (Employee emp : entry.getValue()) {
                if (highest == null || emp.salary > highest.salary) {
                    highest = emp;
                }
            }
            if (highest != null) {
                System.out.println(entry.getKey() + ": " + highest.name + " ($" + highest.salary + ")");
            }
        }
    }

    public void removeLowEarners(double threshold) {
        Iterator<Map.Entry<String, List<Employee>>> mapIt = departmentMap.entrySet().iterator();
        while (mapIt.hasNext()) {
            Map.Entry<String, List<Employee>> entry = mapIt.next();
            List<Employee> list = entry.getValue();
            Iterator<Employee> listIt = list.iterator();
            while (listIt.hasNext()) {
                if (listIt.next().salary < threshold) {
                    listIt.remove();
                }
            }
        }
    }

    public void moveEmployee(String id, String fromDept, String toDept) {
        List<Employee> sourceList = departmentMap.get(fromDept);
        if (sourceList == null) return;

        Employee target = null;
        Iterator<Employee> it = sourceList.iterator();
        while (it.hasNext()) {
            Employee emp = it.next();
            if (emp.id.equals(id)) {
                target = emp;
                it.remove();
                break;
            }
        }

        if (target != null) {
            target.department = toDept;
            addEmployee(target);
            System.out.println("Moved Employee ID " + id + " from " + fromDept + " to " + toDept);
        } else {
            System.out.println("Employee ID " + id + " not found in department " + fromDept);
        }
    }

    public void displayAll() {
        for (Map.Entry<String, List<Employee>> entry : departmentMap.entrySet()) {
            System.out.println("Department: " + entry.getKey());
            for (Employee emp : entry.getValue()) {
                System.out.println("  " + emp);
            }
        }
    }

    public static void main(String[] args) {
        PayrollSystem system = new PayrollSystem();
        system.addEmployee(new Employee("E1", "Alice", "IT", 65000));
        system.addEmployee(new Employee("E2", "Bob", "IT", 45000));
        system.addEmployee(new Employee("E3", "Charlie", "HR", 55000));
        system.addEmployee(new Employee("E4", "David", "HR", 38000));

        System.out.println("Initial Payroll State:");
        system.displayAll();

        System.out.println("\nCalculations:");
        system.calculateSalaryExpense();
        system.findHighestPaid();

        System.out.println("\nMoving Bob from IT to HR...");
        system.moveEmployee("E2", "IT", "HR");

        System.out.println("\nRemoving employees with salary < 40000...");
        system.removeLowEarners(40000);

        System.out.println("\nUpdated Payroll State:");
        system.displayAll();
    }
}
