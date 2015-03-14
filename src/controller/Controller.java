
package controller;

import gui.FormEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.List;
import model.AgeCategory;
import model.Database;
import model.EmploymentCategory;
import model.Gender;
import model.Person;

public class Controller {
    Database db = new Database();
    
    public List<Person> getPeople(){
       return db.getPeople();
    }
    public void removePerson(int index){
        db.removePerson(index);
    }
    public void save() throws SQLException{
        db.save();
    }
    public void connect() throws SQLException{
        db.connect();
    }
    public void load() throws SQLException{
        db.load();
    }
    public void disconnect(){
        db.disconnect();
    }
    public void addPerson(FormEvent ev){
        String name = ev.getName();
        String occupation = ev.getOccupation();
        int ageCatId = ev.getAgeCategory();
        String empCat = ev.getEmploymentCategory();
        boolean isUs = ev.isUsCitizen();
        String taxId = ev.getTaxId();
        String gender = ev.getGender();
        
      
        AgeCategory ageCategory=null;
        switch(ageCatId){
            case 0:
                ageCategory = AgeCategory.child;
                break;
            case 1:
                ageCategory = AgeCategory.adult;
                break;
            case 2:
                ageCategory = AgeCategory.senior;
                break;
        }
        EmploymentCategory empCategory=null;
         switch(empCat){
             case "employed":
                 empCategory = EmploymentCategory.employed;
                 break;
             case "self-employed":
                 empCategory = EmploymentCategory.selfEmployed;
                 break;
             case "unemployed":
                 empCategory = EmploymentCategory.unemployed;
                 break;
             default:
                 empCategory = EmploymentCategory.other;
                 break;
         }
         
        Gender genderCat;
        if(gender.equalsIgnoreCase("male")){
            genderCat = Gender.male;
        }else{
            genderCat = Gender.female;
        }
        
        Person person = new Person(name, occupation, ageCategory, 
                empCategory,taxId, isUs, genderCat);
        db.addPerson(person);
    }
    public void saveToFile(File file) throws FileNotFoundException{
        db.saveToFile(file);
    }
    public void loadFromFile(File file) throws FileNotFoundException{
        db.loadFromFile(file);
    }
}
