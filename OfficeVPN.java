import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.HashMap;
import java.util.Scanner;

class User {
    String username;
    String password;

    User(String u, String p) {
        this.username = u;
        this.password = p;
    }
}

class EncryptionService {
    private SecretKey key;

    EncryptionService() throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(128);
        this.key = keyGen.generateKey();
    }

    public String encrypt(String data) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encVal = cipher.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encVal);
    }

    public String decrypt(String encryptedData) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decodedValue = Base64.getDecoder().decode(encryptedData);
        byte[] decValue = cipher.doFinal(decodedValue);
        return new String(decValue);
    }
}

class VPNService {
    private boolean connected = false;

    public void connect(String username) {
        connected = true;
        System.out.println("✅ Secure Tunnel Created for: " + username);
    }

    public void disconnect() {
        connected = false;
        System.out.println("🔴 VPN Disconnected");
    }

    public boolean isConnected() {
        return connected;
    }
}

public class OfficeVPN {
    static HashMap<String, User> users = new HashMap<>();
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        users.put("admin", new User("admin", "12345"));
        users.put("employee", new User("employee", "pass"));

        try {
            EncryptionService enc = new EncryptionService();
            VPNService vpn = new VPNService();

            while (true) {
                System.out.println("\n----------- OFFICE VPN SYSTEM -----------");
                System.out.println("1. Login");
                System.out.println("2. Send Secure Message");
                System.out.println("3. Disconnect VPN");
                System.out.println("4. Exit");
                System.out.print("Enter choice: ");
                int ch = sc.nextInt();

                switch (ch) {
                    case 1:
                        login(vpn);
                        break;

                    case 2:
                        if (vpn.isConnected()) {
                            System.out.print("Enter message: ");
                            sc.nextLine();
                            String msg = sc.nextLine();

                            String encrypted = enc.encrypt(msg);
                            System.out.println("🔐 Encrypted Message: " + encrypted);

                            String decrypted = enc.decrypt(encrypted);
                            System.out.println("🔓 Decrypted Message: " + decrypted);
                        } else {
                            System.out.println("❌ Connect VPN first!");
                        }
                        break;

                    case 3:
                        vpn.disconnect();
                        break;

                    case 4:
                        System.out.println("Exiting...");
                        return;

                    default:
                        System.out.println("Invalid choice!");
                }
            }
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }

    public static void login(VPNService vpn) {
        System.out.print("Username: ");
        String u = sc.next();
        System.out.print("Password: ");
        String p = sc.next();

        if (users.containsKey(u) && users.get(u).password.equals(p)) {
            System.out.println("✅ Login Successful!");
            vpn.connect(u);
        } else {
            System.out.println("❌ Invalid credentials");
        }
    }
}

