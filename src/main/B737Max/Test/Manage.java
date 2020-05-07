package B737Max.Test;

import B737Max.Components.ServerAPIAdapter;

import java.io.BufferedReader;
import java.io.InputStreamReader;

class ManageTool {
    public static void main(String[] args) throws Exception {
        ServerAPIAdapter ad = ServerAPIAdapter.getInstance();
        BufferedReader buf = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Usage:\nreset | exit\n");
        while (true) {
            String command = buf.readLine();
            switch (command) {
                case "reset":
                    ad.reset();
                    System.out.println("Success");
                    break;
                case "exit":
                    System.exit(0);
                    break;
                default:
                    System.out.println("Usage:\nreset | exit\n");
                    break;
            }
        }
    }
}
