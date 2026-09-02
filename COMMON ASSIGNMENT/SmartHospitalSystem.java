import java.util.*;

public class SmartHospitalSystem {

    static class InvalidPatientException extends Exception {
        public InvalidPatientException(String msg) { super(msg); }
    }

    static class DuplicateAppointmentException extends Exception {
        public DuplicateAppointmentException(String msg) { super(msg); }
    }

    static class OutOfStockException extends Exception {
        public OutOfStockException(String msg) { super(msg); }
    }

    static abstract class Person {
        private String id, name, phone;
        private int age;

        public Person(String id, String name, int age, String phone) {
            this.id = id; this.name = name; this.age = age; this.phone = phone;
        }
        public String getId() { return id; }
        public String getName() { return name; }
        public int getAge() { return age; }
        public String getPhone() { return phone; }
    }

    static abstract class Patient extends Person {
        public Patient(String id, String name, int age, String phone) { super(id, name, age, phone); }
        public abstract double getConsultationFee();
        public abstract String getCategory();
        public void display() {
            System.out.printf("%-8s %-16s %-4d %-12s %-14s $%.2f%n", getId(), getName(), getAge(), getPhone(), getCategory(), getConsultationFee());
        }
    }

    static class GeneralPatient extends Patient {
        public GeneralPatient(String id, String name, int age, String phone) { super(id, name, age, phone); }
        public double getConsultationFee() { return 500.0; }
        public String getCategory() { return "General"; }
    }

    static class SeniorPatient extends Patient {
        public SeniorPatient(String id, String name, int age, String phone) { super(id, name, age, phone); }
        public double getConsultationFee() { return 300.0; }
        public String getCategory() { return "Senior (40% Off)"; }
    }

    static class Doctor extends Person {
        private String specialization;
        private ArrayList<Appointment> appointments = new ArrayList<>();
        private LinkedList<Patient> waitlist = new LinkedList<>();

        public Doctor(String id, String name, int age, String phone, String spec) {
            super(id, name, age, phone);
            this.specialization = spec;
        }
        public String getSpecialization() { return specialization; }
        public ArrayList<Appointment> getAppointments() { return appointments; }
        public LinkedList<Patient> getWaitlist() { return waitlist; }

        public synchronized boolean isBooked(String date, String time) {
            Iterator<Appointment> it = appointments.iterator();
            while (it.hasNext()) {
                Appointment a = it.next();
                if (a.date.equals(date) && a.time.equals(time) && a.status.equals("BOOKED")) return true;
            }
            return false;
        }
    }

    static class Appointment {
        String id, date, time, status = "BOOKED";
        Patient patient;
        Doctor doctor;

        public Appointment(String id, Patient p, Doctor d, String date, String time) {
            this.id = id; this.patient = p; this.doctor = d; this.date = date; this.time = time;
        }
        public void display() {
            System.out.printf("%-8s %-10s %-6s %-10s Patient: %-12s Doctor: Dr. %-12s Fee: $%.2f%n",
                    id, date, time, status, patient.getName(), doctor.getName(), patient.getConsultationFee());
        }
    }

    static abstract class Medicine {
        String id, name;
        double price;
        int qty, lowStock;

        public Medicine(String id, String name, double price, int qty, int lowStock) {
            this.id = id; this.name = name; this.price = price; this.qty = qty; this.lowStock = lowStock;
        }
        public abstract String getDosageRule();
        public abstract String getType();
        public void display() {
            System.out.printf("%-8s %-16s %-14s $%-7.2f Stock: %-4d Rule: %s%n", id, name, getType(), price, qty, getDosageRule());
        }
    }

    static class PrescriptionMedicine extends Medicine {
        public PrescriptionMedicine(String id, String name, double price, int qty, int low) { super(id, name, price, qty, low); }
        public String getDosageRule() { return "Rx Required: Take as directed by physician."; }
        public String getType() { return "Prescription"; }
    }

    static class OTCMedicine extends Medicine {
        public OTCMedicine(String id, String name, double price, int qty, int low) { super(id, name, price, qty, low); }
        public String getDosageRule() { return "OTC: Take 1-2 tablets every 4-6h with water."; }
        public String getType() { return "Over-The-Counter"; }
    }

    static class Inventory {
        HashMap<String, Medicine> stock = new HashMap<>();

        public synchronized void addMedicine(Medicine m) { stock.put(m.id, m); }
        public synchronized void dispense(String id, int qty) throws OutOfStockException {
            Medicine m = stock.get(id);
            if (m == null || m.qty < qty) throw new OutOfStockException("Insufficient stock for " + (m == null ? id : m.name));
            m.qty -= qty;
            if (m.qty <= m.lowStock) System.out.println("[ALERT] Low stock for " + m.name + "! Remaining: " + m.qty);
        }
        public synchronized void displayAll() {
            Iterator<Medicine> it = stock.values().iterator();
            while (it.hasNext()) it.next().display();
        }
    }

    static class HospitalManager {
        HashMap<String, Patient> patients = new HashMap<>();
        HashMap<String, Doctor> doctors = new HashMap<>();
        ArrayList<Appointment> appointments = new ArrayList<>();
        Inventory inventory = new Inventory();
        int aptCounter = 1001;

        public synchronized void registerPatient(Patient p) { patients.put(p.getId(), p); }
        public synchronized Patient getPatient(String id) throws InvalidPatientException {
            Patient p = patients.get(id);
            if (p == null) throw new InvalidPatientException("Patient ID '" + id + "' not found.");
            return p;
        }

        public synchronized void bookAppointment(String pId, String dId, String date, String time)
                throws InvalidPatientException, DuplicateAppointmentException {
            Patient p = getPatient(pId);
            Doctor d = doctors.get(dId);
            if (d == null) throw new IllegalArgumentException("Doctor not found.");

            if (d.isBooked(date, time)) {
                d.getWaitlist().addLast(p);
                throw new DuplicateAppointmentException("Slot " + time + " already booked. Patient " + p.getName() + " added to waitlist.");
            }

            Appointment apt = new Appointment("APT-" + (aptCounter++), p, d, date, time);
            d.getAppointments().add(apt);
            appointments.add(apt);
            System.out.println("[SUCCESS] Booked: " + apt.id + " for " + p.getName() + " with Dr. " + d.getName());
        }

        public synchronized void cancelAppointment(String aptId) {
            ListIterator<Appointment> lit = appointments.listIterator();
            while (lit.hasNext()) {
                Appointment a = lit.next();
                if (a.id.equalsIgnoreCase(aptId) && a.status.equals("BOOKED")) {
                    a.status = "CANCELLED";
                    System.out.println("Appointment " + aptId + " CANCELLED.");
                    Doctor d = a.doctor;
                    if (!d.getWaitlist().isEmpty()) {
                        Patient next = d.getWaitlist().removeFirst();
                        Appointment promo = new Appointment("APT-" + (aptCounter++), next, d, a.date, a.time);
                        d.getAppointments().add(promo);
                        appointments.add(promo);
                        System.out.println(">>> WAITLIST PROMOTION: Promoted " + next.getName() + " to new Apt " + promo.id);
                    }
                    return;
                }
            }
            System.out.println("Active appointment ID not found.");
        }

        public synchronized void displayPatients() {
            System.out.println("---------------- PATIENTS ----------------");
            System.out.printf("%-8s %-16s %-4s %-12s %-14s %s%n", "ID", "Name", "Age", "Phone", "Category", "Fee");
            Iterator<Patient> it = patients.values().iterator();
            while (it.hasNext()) it.next().display();
            System.out.println("------------------------------------------");
        }

        public synchronized void report() {
            System.out.println("\n================ HOSPITAL REPORT ================");
            System.out.println("Total Patients   : " + patients.size());
            System.out.println("Total Doctors    : " + doctors.size());
            System.out.println("Total Medicines  : " + inventory.stock.size());
            System.out.println("Total Bookings   : " + appointments.size());
            System.out.println("=================================================");
        }
    }

    static class BookingThread implements Runnable {
        HospitalManager mgr;
        String pId, dId, date, time;

        public BookingThread(HospitalManager m, String p, String d, String dt, String tm) {
            this.mgr = m; this.pId = p; this.dId = d; this.date = dt; this.time = tm;
        }
        public void run() {
            try {
                mgr.bookAppointment(pId, dId, date, time);
            } catch (Exception e) {
                System.out.println("[" + Thread.currentThread().getName() + "] " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        HospitalManager mgr = new HospitalManager();
        Scanner sc = new Scanner(System.in);

        mgr.registerPatient(new GeneralPatient("P101", "Anirudh", 21, "9876543210"));
        mgr.registerPatient(new SeniorPatient("P102", "Ravi", 68, "9876501234"));
        mgr.doctors.put("D101", new Doctor("D101", "Priya", 45, "9123456780", "Cardiology"));
        mgr.inventory.addMedicine(new PrescriptionMedicine("M101", "Amoxicillin", 15.0, 50, 10));
        mgr.inventory.addMedicine(new OTCMedicine("M102", "Paracetamol", 5.0, 8, 10));

        while (true) {
            System.out.println("\n1.Register Patient 2.View Patients 3.Book Appt 4.Cancel Appt 5.Inventory/Dispense 6.Concurrency Demo 7.Report 8.Exit");
            System.out.print("Choice: ");
            int ch;
            try { ch = Integer.parseInt(sc.nextLine().trim()); } catch (Exception e) { continue; }

            try {
                if (ch == 1) {
                    System.out.print("ID: "); String id = sc.nextLine();
                    System.out.print("Name: "); String n = sc.nextLine();
                    System.out.print("Age: "); int age = Integer.parseInt(sc.nextLine());
                    System.out.print("Phone: "); String ph = sc.nextLine();
                    System.out.print("1.General 2.Senior: "); int t = Integer.parseInt(sc.nextLine());
                    mgr.registerPatient(t == 2 ? new SeniorPatient(id, n, age, ph) : new GeneralPatient(id, n, age, ph));
                    mgr.displayPatients();
                } else if (ch == 2) {
                    mgr.displayPatients();
                } else if (ch == 3) {
                    System.out.print("Patient ID: "); String p = sc.nextLine();
                    System.out.print("Doctor ID: "); String d = sc.nextLine();
                    System.out.print("Date: "); String dt = sc.nextLine();
                    System.out.print("Time: "); String tm = sc.nextLine();
                    mgr.bookAppointment(p, d, dt, tm);
                } else if (ch == 4) {
                    System.out.print("Appointment ID: "); String a = sc.nextLine();
                    mgr.cancelAppointment(a);
                } else if (ch == 5) {
                    mgr.inventory.displayAll();
                    System.out.print("Dispense Medicine ID (or Enter to skip): "); String m = sc.nextLine();
                    if (!m.isEmpty()) {
                        System.out.print("Qty: "); int q = Integer.parseInt(sc.nextLine());
                        mgr.inventory.dispense(m, q);
                        System.out.println("Dispensed successfully.");
                    }
                } else if (ch == 6) {
                    Thread t1 = new Thread(new BookingThread(mgr, "P101", "D101", "2026-10-01", "10:00"), "Thread-1");
                    Thread t2 = new Thread(new BookingThread(mgr, "P102", "D101", "2026-10-01", "10:00"), "Thread-2");
                    t1.setPriority(Thread.MIN_PRIORITY);
                    t2.setPriority(Thread.MAX_PRIORITY);
                    t1.start(); t2.start();
                    t1.join(); t2.join();
                } else if (ch == 7) {
                    mgr.report();
                } else if (ch == 8) {
                    System.out.println("Exiting. Thank you!");
                    break;
                }
            } catch (Exception e) {
                System.out.println("[ERROR] " + e.getMessage());
            }
        }
    }
}
