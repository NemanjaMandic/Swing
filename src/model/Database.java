
package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Database {
   
    private List<Person> people;
    private Connection con;
    public Database(){
        people = new LinkedList<>();
    }
    
    public void connect() throws SQLException{
        if(con != null) return;
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
        String url = "jdbc:mysql://localhost/swingtest";
           con = DriverManager.getConnection(url, "root", "");
       
           System.out.println("Konektovan: "+con);
    }
    public void disconnect() {
       
        if(con != null){
            try {
                con.close();
            } catch (SQLException ex) {
                System.out.println("Konekcija nece da se zatvori: "+ex.getMessage());
            }
        }
    }
    public void save() throws SQLException{
        String checkSql = "Select count(*) as count from people where id=?";
        PreparedStatement checkStmt = con.prepareStatement(checkSql);
       
        String insertSql = "insert into people (id, name, age, employment_status, tax_id, us_citizen, gender, occupation) values(?,?,?,?,?,?,?,?)";
        PreparedStatement insertStatement = con.prepareStatement(insertSql);
        
        String updateSql = "update people set name=?, age=?, employment_status=?, tax_id=?, us_citizen=?, gender=?, occupation=? where id=?";
        PreparedStatement updateStatement = con.prepareStatement(updateSql);
        
        for(Person person : people){
           int id = person.getId();
           String name = person.getName();
           String occupation = person.getOccupation();
           AgeCategory age = person.getAgeCategory();
           EmploymentCategory emp = person.getEmpCat();
           String tax = person.getTaxId();
           boolean isUs = person.isUsCitizen();
           Gender gender = person.getGender();
           
            checkStmt.setInt(1, id);
            
            ResultSet checkRes = checkStmt.executeQuery();
            while(checkRes.next()){
                int count = checkRes.getInt(1);
                
                if(count == 0){
                    System.out.println("Inserting person with ID " + id);
                   
                    int col=1;
                    insertStatement.setInt(col++, id);
                    insertStatement.setString(col++, name);
                    insertStatement.setString(col++, age.name());
                    insertStatement.setString(col++, emp.name());
                    insertStatement.setString(col++, tax);
                    insertStatement.setBoolean(col++, isUs);
                    insertStatement.setString(col++, gender.name());
                    insertStatement.setString(col++, occupation);
                    
                    insertStatement.executeUpdate();
                }else{
                    System.out.println("Updating person with ID " + id);
               
                    int col=1;
                  
                    updateStatement.setString(col++, name);
                    updateStatement.setString(col++, age.name());
                    updateStatement.setString(col++, emp.name());
                    updateStatement.setString(col++, tax);
                    updateStatement.setBoolean(col++, isUs);
                    updateStatement.setString(col++, gender.name());
                    updateStatement.setString(col++, occupation);
                    updateStatement.setInt(col++, id);
                    
                    updateStatement.executeUpdate();
                }
               
            }
            
        }
        updateStatement.close();
        insertStatement.close();
        checkStmt.close();
    }
    public void load() throws SQLException{
        people.clear();
        String sql = "select id, name, age, employment_status, tax_id, us_citizen, gender, occupation from people order by name";
        try (Statement selectStatement = con.createStatement();
                 ResultSet result = selectStatement.executeQuery(sql);) {
           
            while(result.next()){
                int id = result.getInt("id");
                String name = result.getString("name");
                String age = result.getString("age");
                String emp = result.getString("employment_status");
                String taxId = result.getString("tax_id");
                boolean isUs = result.getBoolean("us_citizen");
                String gender = result.getString("gender");
                String occ = result.getString("occupation");
                
                Person person = new Person(id, name, occ, AgeCategory.valueOf(age), EmploymentCategory.valueOf(emp), taxId, isUs, Gender.valueOf(gender));
                people.add(person);
              
            }
        }
       
    }
    public void addPerson(Person person){
        people.add(person);
    }
    public void removePerson(int index){
        people.remove(index);
    }
    public List<Person> getPeople(){
        return Collections.unmodifiableList(people);
    }
    public void saveToFile(File file) throws FileNotFoundException{
       try( FileOutputStream fos = new FileOutputStream(file);
        ObjectOutputStream oos = new ObjectOutputStream(fos);){
           Person[] persons = people.toArray(new Person[people.size()]);
           oos.writeObject(persons);
       } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
    public void loadFromFile(File file) throws FileNotFoundException{
        try(FileInputStream fis = new FileInputStream(file);
                ObjectInputStream ois = new ObjectInputStream(fis)){
            
            Person[] persons = (Person[])ois.readObject();
            people.clear();
            people.addAll(Arrays.asList(persons));
        } catch (IOException | ClassNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
