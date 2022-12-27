package edu.ieu.tr.cvm;

import javafx.scene.control.TreeItem;

import java.util.List;
import java.util.Map;

public class Cv extends TreeItem<String> {
    private final int id;
    private String fullName;
    private int age;
    private int birthYear;
    private String email;
    private String description;
    private String homeAddress;
    private String jobAdress;
    private String telephoneNumber;
    private String websiteLink;
    private Map<String,Integer> skills;
    private Map<String,Integer> education;
    private Map<String,Integer> additionalSkills;
    private List<String> tags;
    
    public Cv(){
        id = -1;
    }

    public Cv(int id, String fullName, int age, int birthYear, String email, String description, String homeAddress, String jobAdress, String telephoneNumber, String websiteLink, Map<String, Integer> skills, Map<String, Integer> education, Map<String, Integer> additionalSkills, List<String> tags) {
        this.id = id;
        this.fullName = fullName;
        this.age = age;
        this.birthYear = birthYear;
        this.email = email;
        this.description = description;
        this.homeAddress = homeAddress;
        this.jobAdress = jobAdress;
        this.telephoneNumber = telephoneNumber;
        this.websiteLink = websiteLink;
        this.skills = skills;
        this.education = education;
        this.additionalSkills = additionalSkills;
        this.tags = tags;
    }

    public int getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(int birthYear) {
        this.birthYear = birthYear;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(String homeAddress) {
        this.homeAddress = homeAddress;
    }

    public String getJobAdress() {
        return jobAdress;
    }

    public void setJobAdress(String jobAdress) {
        this.jobAdress = jobAdress;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    public String getWebsiteLink() {
        return websiteLink;
    }

    public void setWebsiteLink(String websiteLink) {
        this.websiteLink = websiteLink;
    }

    public Map<String, Integer> getSkills() {
        return skills;
    }

    public void setSkills(Map<String, Integer> skills) {
        this.skills = skills;
    }

    public Map<String, Integer> getEducation() {
        return education;
    }

    public void setEducation(Map<String, Integer> education) {
        this.education = education;
    }

    public Map<String, Integer> getAdditionalSkills() {
        return additionalSkills;
    }

    public void setAdditionalSkills(Map<String, Integer> additionalSkills) {
        this.additionalSkills = additionalSkills;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public int getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public int getAge() {
        return age;
    }

    @Override
    public String toString() {
        return "Cv{" +
                "id=" + id +
                ", fullName='" + fullName + '\'' +
                ", age=" + age +
                ", birthYear=" + birthYear +
                ", email='" + email + '\'' +
                ", description='" + description + '\'' +
                ", homeAddress='" + homeAddress + '\'' +
                ", jobAdress='" + jobAdress + '\'' +
                ", telephoneNumber='" + telephoneNumber + '\'' +
                ", websiteLink='" + websiteLink + '\'' +
                ", skills=" + skills +
                ", education=" + education +
                ", additionalSkills=" + additionalSkills +
                ", tags=" + tags +
                '}';
    }
}
