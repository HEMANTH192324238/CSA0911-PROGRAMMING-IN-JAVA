final class BankAccount {
    final String accountNumber;
    final String bankName = "National Bank";
    private double balance;
    private String ownerName;

    BankAccount(String accountNumber, double balance, String ownerName) {
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.ownerName = ownerName;
    }

    final String getAccountInfo() {
        return "Acc No: " + accountNumber + " | Bank: " + bankName + " | Owner: " + ownerName + " | Balance: $" + balance;
    }

    void deposit(double amount) {
        balance += amount;
    }

    double getBalance() {
        return balance;
    }
}

/*
class SavingsAccountExtension extends BankAccount {
}
*/

class SavingsAccount {
    private BankAccount account;
    double interestRate;
    double minimumBalance;

    SavingsAccount(String accountNumber, double balance, String ownerName, double interestRate, double minimumBalance) {
        this.account = new BankAccount(accountNumber, balance, ownerName);
        this.interestRate = interestRate;
        this.minimumBalance = minimumBalance;
    }

    void applyInterest() {
        double interest = account.getBalance() * interestRate;
        account.deposit(interest);
    }

    void deposit(double amount) {
        account.deposit(amount);
    }

    String getInfo() {
        return account.getAccountInfo() + " | Interest Rate: " + (interestRate * 100) + "%";
    }
}

class AccountManager {
    final void processTransaction(Runnable transaction) {
        System.out.println("Transaction processing started at system timestamp...");
        transaction.run();
        System.out.println("Transaction logged successfully.");
    }
}

public class BankSystemDemo {
    public static void main(String[] args) {
        SavingsAccount savings = new SavingsAccount("SA-55612", 2000.00, "Vamsi", 0.04, 500.00);
        AccountManager manager = new AccountManager();

        System.out.println("Initial State:");
        System.out.println(savings.getInfo());

        manager.processTransaction(() -> {
            savings.deposit(500.00);
        });

        manager.processTransaction(() -> {
            savings.applyInterest();
        });

        System.out.println("After Transactions:");
        System.out.println(savings.getInfo());
    }
}
