import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by yzs on 17-4-29.
 */
public class ServerThread {
    private static int portnum = 9000;
    volatile private static ServerStruct ss;
    private static ServerSocket server;


    private void StartServer() throws IOException {
        while(ss.getFlag()){
            Socket socket = server.accept();
            new Thread(() -> {
                try {
                    invoke(socket);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

    private static void invoke(final Socket client) throws IOException {
        BufferedReader in = null;
        PrintWriter out = null;
        try {
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            out = new PrintWriter(client.getOutputStream());
            String cmd = in.readLine();
            String user = "", pass = "", newpass = "";
            int port = 0;
            while (true) {
                switch (cmd){
                    case "login":
                        user = in.readLine();
                        System.out.println("user:" + user);
                        pass = in.readLine();
                        System.out.println("password:" + pass);
                        port = Integer.parseInt(in.readLine());
                        System.out.println("port:" + port);
                        if(ss.CheckPass(user, pass)){
                            ss.setUserPort(user, port);
                            out.println("Sign in successfully. Port is " + port);
                        }
                        else{
                            out.println("Password Denied. User: " + user + "Pass: " + pass);
                        }
                        out.flush();
                        break;
                    case "sign up":
                        user = in.readLine();
                        System.out.println("user:" + user);
                        pass = in.readLine();
                        System.out.println("password:" + pass);
                        port = Integer.parseInt(in.readLine());
                        System.out.println("port:" + port);
                        if(!ss.checkUserExists(user)){
                            ss.setUserInfo(user, pass);
                            ss.saveUserInfo();
                            out.println("Sign up successfully. User: " + user + "Port is " + port);
                        }
                        else{
                            out.println("Username already exists. User: " + user);
                        }
                        out.flush();
                        break;
                    case "modify":
                        user = in.readLine();
                        System.out.println("user:" + user);
                        pass = in.readLine();
                        System.out.println("password:" + pass);
                        newpass = in.readLine();
                        System.out.println("new password:" + newpass);
                        if(ss.CheckPass(user, pass)){
                            ss.setUserInfo(user, newpass);
                            ss.saveUserInfo();
                            out.println("Password changed successfully. New password is " + newpass);
                        }
                        else{
                            out.println("Password Denied. User: " + user + "Pass: " + pass);
                        }
                        out.flush();
                        break;
                    case "query":
                        user = in.readLine();
                        System.out.println("user:" + user);
                        if(ss.getUserPort(user) != 0){
                            out.println(ss.getUserPort(user));
                        }
                        else{
                            out.println("User: " + user + "not found");
                        }
                        out.flush();
                        break;
                    case "byebye":
                        Thread.currentThread().interrupt();
                        break;
                }
            }
        } catch(IOException e) {
            e.printStackTrace();
        } finally {
            assert in != null;
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            assert out != null;
            out.close();
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public ServerThread(ServerStruct a, int _portnum) throws IOException {
        portnum = _portnum;
        ss = a;
        server = new ServerSocket(portnum);
        new Thread(() -> {
            try {
                StartServer();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
}

