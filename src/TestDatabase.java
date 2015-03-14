
import java.sql.SQLException;
import model.AgeCategory;
import model.Database;
import model.EmploymentCategory;
import model.Gender;
import model.Person;

public class TestDatabase {

    public static void main(String[] args) throws SQLException {
        System.out.println("Running database test");

        Database db = new Database();

        try {
            db.connect();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        db.addPerson(new Person("Joe", "builder", AgeCategory.adult, EmploymentCategory.employed, "777", true, Gender.male));
        db.addPerson(new Person("Sue", "artist", AgeCategory.adult, EmploymentCategory.selfEmployed, null, false, Gender.female));

        try{
             db.save();
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        try {
            db.load();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        db.disconnect();
    }
}
