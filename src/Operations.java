import java.sql.*;
import java.util.Scanner;

public class Operations {
    static Scanner sc = new Scanner(System.in);

    public static void signUp(){
        try {
            System.out.println("Enter Name : ");
            String name = sc.next();
            System.out.println("Enter the mobile Number : ");
            long mobileNo = sc.nextLong();
            System.out.println("Create a Password : ");
            String pass = sc.next();
            Connection con = DataConnection.getConnection();
            String query = "select max(accNo) from customer";
            Statement stmt = con.createStatement();
            ResultSet resultSet =  stmt.executeQuery(query);
            resultSet.next();
            int Accno = resultSet.getInt(1);
            String query1 = "insert into customer values(?,?,?,?)";
            PreparedStatement preStmt = con.prepareStatement(query1);
            preStmt.setString(1,name);
            preStmt.setLong(2,mobileNo);
            preStmt.setInt(3,++Accno);
            preStmt.setString(4,pass);
            int i = preStmt.executeUpdate();
            String query2 = "insert into account values('SBI','Surandai','SBIN0011065',?,0)";
            preStmt = con.prepareStatement(query2);
            preStmt.setInt(1,Accno);
            int j = preStmt.executeUpdate();
            if(i==j){
                System.out.println("User Created With\nAccount Number : "+(Accno)+"\nPassword : "+pass);
            }
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static int logIn(){
        try {
            System.out.println("Enter The Account Number : ");
            int accNo = sc.nextInt();
            System.out.println("Enter The Password : ");
            String pass = sc.next();
            Connection con = DataConnection.getConnection();
            String query1 = "Select * From customer where accNo =? AND password =?";
            PreparedStatement prpdStmt = con.prepareStatement(query1);
            prpdStmt.setInt(1,accNo);
            prpdStmt.setString(2,pass);
            ResultSet resultSet = prpdStmt.executeQuery();
            if (resultSet!=null){
                resultSet.next();
                System.out.println("Login Success as "+resultSet.getString(1));
                return accNo;
            }
            else{
                System.out.println("Invalid User");
                return -1;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void transferMoney(int user) {
        try {
            System.out.println("Enter The Account Number To Send Money");
            int toAccNo = sc.nextInt();
            ResultSet resultSet = Operations.getAccDetails(toAccNo);
            resultSet.next();
            if (resultSet.getInt(3) == toAccNo) {
                System.out.println("Enter the Money to Transfer : ");
                int amount = sc.nextInt();
                int balance = Operations.getBalance(user);
                if (balance >= amount) {
                    Connection con = DataConnection.getConnection();
                    String query = "Update account set balance = balance - ? where accNo = ?";
                    PreparedStatement prpdStmt = con.prepareStatement(query);
                    prpdStmt.setInt(1, amount);
                    prpdStmt.setInt(2, user);
                    int i = prpdStmt.executeUpdate();
                    String query1 = "Update account set balance = balance + ? where accNo = ?";
                    prpdStmt = con.prepareStatement(query1);
                    prpdStmt.setInt(1, amount);
                    prpdStmt.setInt(2, toAccNo);
                    try {
                        int j = prpdStmt.executeUpdate();
                    }
                    catch (SQLException e){
                        System.out.println("Invalid Receiver Account");
                    }
                    if (i == 1) {
                        System.out.println("Transaction Success");
                    }
                }
                else {
                        System.out.println("Transaction Failed Due to Low Balance");
                    }
                }
        }catch(SQLException e){
                throw new RuntimeException(e);
            }
        }

        public static int getBalance(int user) {
        try {
            Connection con = DataConnection.getConnection();
            String query = "Select balance from account where accNo = ?";
            PreparedStatement prpdStmt = con.prepareStatement(query);
            prpdStmt.setInt(1,user);
            ResultSet resultSet = prpdStmt.executeQuery();
            resultSet.next();
            return resultSet.getInt(1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static ResultSet getAccDetails(int accNo) {
        try{
            Connection con = DataConnection.getConnection();
            String query1 = "Select * from customer where accno = ?";
            PreparedStatement preparedStatement = con.prepareStatement(query1);
            preparedStatement.setInt(1,accNo);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void addMoney() {
        try {
            System.out.println("Enter The Account Number : ");
            int accNo = sc.nextInt();
            ResultSet resultSet = Operations.getAccDetails(accNo);
            resultSet.next();
            if (resultSet.getInt(3) == accNo) {
                System.out.println("Enter the Money to Add : ");
                int amount = sc.nextInt();
                Connection con = DataConnection.getConnection();
                String query = "Update account set balance = balance + ? where accNo = ?";
                PreparedStatement prpdStmt = con.prepareStatement(query);
                prpdStmt.setInt(1, amount);
                prpdStmt.setInt(2, accNo);
                int i = prpdStmt.executeUpdate();
                if (i == 1) {
                    System.out.println("Money Added Successfully");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void deleteAccount() {
        try {
            System.out.println("Enter The Account Number : ");
            int accNo = sc.nextInt();
            ResultSet resultSet = Operations.getAccDetails(accNo);
            resultSet.next();
            if (resultSet.getInt(3) == accNo) {
                Connection con = DataConnection.getConnection();
                String query = "Delete from customer where accNo = ?";
                PreparedStatement prpdStmt = con.prepareStatement(query);
                prpdStmt.setInt(1, accNo);
                int i = prpdStmt.executeUpdate();
                String query1 = "Delete from account where accNo = ?";
                prpdStmt = con.prepareStatement(query1);
                prpdStmt.setInt(1, accNo);
                int j = prpdStmt.executeUpdate();
                if (i == j) {
                    System.out.println("Account Deleted Successfully");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}