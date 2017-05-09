import java.io.IOException;
import java.util.Scanner;

/**
 * Created by yzs on 17-4-29.
 */
public class ServerMain {
    public static void main(String args[]) throws IOException {
        int port = Integer.parseInt(args[0]);

        ServerStruct ss = new ServerStruct(port);
        System.out.println("Ready for connection!");

        String cmd = "";
        Scanner scanner = new Scanner(System.in);
        cmd = scanner.nextLine();
        while(!cmd.equals("q") && !cmd.equals("quit")){
            scanner = new Scanner(System.in);
            cmd = scanner.toString();
        }
        ss.setFlag();
        System.out.println("Exiting...");
    }
}
