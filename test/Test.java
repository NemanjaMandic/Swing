
import javax.swing.JButton;


public class Test {
     private String but(int[] num, JButton prc){
        String mm = "";
        for(int i=0; i<=num.length; i++){
           mm += "Gumb: "+num[i];
       }
        return mm;
    }
   public static void main(String[] args){
       Test test = new Test();
       int[] cvrc = {1455};
       test.but(cvrc, null);
       System.out.println( test.but(cvrc, null));
   } 
}
