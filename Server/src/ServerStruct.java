import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.io.*;
import java.util.Set;

/**
 * Created by yzs on 17-4-29.
 */
public class ServerStruct {
    private final String InfoPath = "userinfo.txt";
    private final String Secret_MD5 = "eXpzOTgxMTMwCg";


    private Map<String, String>UserInfo;
    private Map<String, Integer>UserPort;
    private boolean flag = true;


    public String MD5(String s){
        BigInteger bigInteger=null;
        try {
            MessageDigest md = MessageDigest.getInstance(Secret_MD5);
            byte[] inputData = s.getBytes();
            md.update(inputData);
            bigInteger = new BigInteger(md.digest());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bigInteger.toString(16);
    }

    public boolean CheckPass(String user, String pass){
        return UserInfo.containsKey(user) && (UserInfo.get(user).equals(MD5(pass)));
    }

    private void loadUserInfo() throws IOException {
        File file = new File(InfoPath);
        if(!file.exists())
            file.createNewFile();
        BufferedReader in = new BufferedReader(new FileReader(file));

        UserInfo = new HashMap<>();
        String s = "";
        s = in.readLine();
        while(s != null){
            String t = s;
            s = in.readLine();
            UserInfo.put(t, s);
            s = in.readLine();
        }
        in.close();
    }

    public void saveUserInfo() throws IOException {
        File file = new File(InfoPath);
        BufferedWriter out = new BufferedWriter(new FileWriter(file));
        for (Map.Entry<String, String> entry : UserInfo.entrySet()) {
            //System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
            out.write(entry.getKey());
            out.newLine();
            out.write(entry.getValue());
            out.newLine();
        }
        out.flush();
        out.close();
    }

    public void setUserPort(String user, int port){
        if(UserPort.containsKey(user))
            UserPort.remove(user);
        UserPort.put(user, port);
    }

    public void setUserInfo(String user, String pass){
        if(UserInfo.containsKey(user))
            UserInfo.remove(user);
        UserInfo.put(user, MD5(pass));
    }

    public int getUserPort(String user){
        if(UserPort.containsKey(user))
            return UserPort.get(user);
        return 0;
    }

    public boolean checkUserExists(String user){
        return UserInfo.containsKey(user);
    }

    public void setFlag(){
        flag = false;
    }

    public boolean getFlag(){
        return flag;
    }

    public ServerStruct(int _port) throws IOException {
        UserPort = new HashMap<>();
        loadUserInfo();
        ServerThread st = new ServerThread(this, _port);
    }
}
