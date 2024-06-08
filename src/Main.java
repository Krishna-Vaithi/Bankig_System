import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("Enter 1.SignUp 2.Login 3.Exit");
            int loginOpt = sc.nextInt();
            switch (loginOpt) {
                case 1:
                    Operations.signUp();
                    break;
                case 2:
                    int user = Operations.logIn();
                    if (user>0){
                        System.out.println("Enter\n1.Transfer Money\n2.Check Balance");
                        int i = sc.nextInt();
                        switch (i){
                            case 1:
                                Operations.transferMoney(user);
                                break;
                            case 2:
                                System.out.println(Operations.getBalance(user));
                                break;
                        }
                    }
                    break;
                case 3:
                    return;
                    case 41411040:
                        System.out.println("Enter\n1.Add Money\n2.Delete Account");
                        int adminOpt = sc.nextInt();
                        switch (adminOpt){
                            case 1:
                                Operations.addMoney();
                                break;
                            case 2:
                                Operations.deleteAccount();
                                break;
                        }
                default:
                    System.out.println("Invalid Input");
            }
        }
    }
}
