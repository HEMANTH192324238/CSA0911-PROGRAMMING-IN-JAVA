import java.util.Iterator;
import java.util.Stack;

public class BrowserNavigation {
    private Stack<String> backHistory = new Stack<>();
    private Stack<String> forwardHistory = new Stack<>();
    private String currentPage = null;

    public void visitPage(String url) {
        if (currentPage != null) {
            backHistory.push(currentPage);
        }
        currentPage = url;
        forwardHistory.clear();
        System.out.println("Visited: " + url);
    }

    public void goBack() {
        if (backHistory.isEmpty()) {
            System.out.println("Cannot go back. Back history is empty.");
            return;
        }
        forwardHistory.push(currentPage);
        currentPage = backHistory.pop();
        System.out.println("Went back to: " + currentPage);
    }

    public void goForward() {
        if (forwardHistory.isEmpty()) {
            System.out.println("Cannot go forward. Forward history is empty.");
            return;
        }
        backHistory.push(currentPage);
        currentPage = forwardHistory.pop();
        System.out.println("Went forward to: " + currentPage);
    }

    public void displayState() {
        System.out.println("Current Page: " + (currentPage != null ? currentPage : "None"));
        
        System.out.print("Back History Stack (oldest to newest): ");
        Iterator<String> backIt = backHistory.iterator();
        while (backIt.hasNext()) {
            System.out.print(backIt.next() + " -> ");
        }
        System.out.println(" [Current]");

        System.out.print("Forward History Stack (newest to oldest): ");
        Iterator<String> forwardIt = forwardHistory.iterator();
        while (forwardIt.hasNext()) {
            System.out.print(forwardIt.next() + " -> ");
        }
        System.out.println(" [End]");
    }

    public static void main(String[] args) {
        BrowserNavigation nav = new BrowserNavigation();
        nav.visitPage("google.com");
        nav.visitPage("github.com");
        nav.visitPage("stackoverflow.com");
        nav.displayState();

        System.out.println("\nExecuting back action...");
        nav.goBack();
        nav.displayState();

        System.out.println("\nExecuting forward action...");
        nav.goForward();
        nav.displayState();

        System.out.println("\nExecuting back action...");
        nav.goBack();
        System.out.println("Visiting a new page (wikipedia.org)...");
        nav.visitPage("wikipedia.org");
        nav.displayState();
    }
}
