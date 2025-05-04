package Assessment;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
enum UserType
{
    SM,
    PM,
    Admin;
}
public class User {
    public String name;
    public String location;
    public String email;
    private String phoneNum;
    private String username;
    private String password;
    private  UserType userType;
    
    public User(String N,String L,String E, String PN, String UN,String PWD,UserType UT)
    {
        name=N;
        location=L;
        email=E;
        phoneNum=PN;
        username=UN;
        password=PWD;
        userType=UT;
    }
    public User(String UN,String PWD, UserType UT)
    {
        username=UN;
        password=PWD;
        userType=UT;
    }
    public void setUN(String UN ){ username=UN; }
    public void setPWD(String PWD) { password=PWD; }
    public void setType(UserType UT) { userType=UT; }
    public void setName(String N) { name=N; }
    public void setLocation(String L) { location=L; }
    public void setEmail(String E) { email=E; }
    public void setPhone(String PN) { phoneNum=PN; }
    public String getName() { return name; }
    public String getLocation() { return location; }
    public String getEmail() { return email; }
    public String getPhone() { return phoneNum; }
    public String getUN() { return username; }
    public String getPWD() { return password; }
    public UserType getUT() { return userType; }
    
    public void wri2file() 
    {
        File file1=new File("Userinfo.txt");
        File file2=new File("UserDetails.txt");
        try
       {
           FileWriter fw=new FileWriter(file1,true);
           BufferedWriter bw = new BufferedWriter(fw);
           PrintWriter pw = new PrintWriter(bw);
           String Line=username+" "+password+" "+userType+"\n";
           pw.write(Line);
           pw.close();
           FileWriter fw1=new FileWriter(file2,true);
           BufferedWriter bw1 = new BufferedWriter(fw1);
           PrintWriter pw1 = new PrintWriter(bw1);
           String Line1=name+" "+location+" "+email+" "+phoneNum+" "+username+" "+password+" "+userType+"\n";
           pw1.write(Line1);
           pw1.close();
           System.out.println("Data Written Successfully");
       }
       catch(IOException Ex)
       {
           System.out.println("File Error");
       }
                
    }
    
}
