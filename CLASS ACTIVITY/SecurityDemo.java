final class SecurityUtils {
    final String encrypt(String data) {
        return new StringBuilder(data).reverse().toString();
    }

    final String decrypt(String encrypted) {
        return new StringBuilder(encrypted).reverse().toString();
    }
}

/*
class AttemptSubclass extends SecurityUtils {
}
*/

class SecureData {
    private String data;
    private SecurityUtils utils = new SecurityUtils();

    SecureData(String data) {
        this.data = utils.encrypt(data);
    }

    final String getSecureData() {
        return utils.decrypt(data);
    }
}

class ExtendedData extends SecureData {
    ExtendedData(String data) {
        super(data);
    }

    /*
    @Override
    String getSecureData() {
        return "HackData";
    }
    */
}

public class SecurityDemo {
    public static void main(String[] args) {
        SecureData sd = new SecureData("MyPassword123");
        System.out.println("Secure Data: " + sd.getSecureData());
    }
}
