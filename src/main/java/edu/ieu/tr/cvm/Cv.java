package edu.ieu.tr.cvm;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Cv {
    private int id;
    private String fullName;
    private int birthYear;
    private double gpa;
    private String email;
    private String description;
    private String homeAddress;
    private String phone;
    private String website;
    private Map<String, Integer> education;
    private Map<String, Integer> skills;
    private List<String> tags;




    public Cv(final int id,
            final String fullName,
            final int birthYear,
            final double gpa,
            final String email,
            final String description,
            final String homeAddress,
            final String phone,
            final String website,
            final Map<String, Integer> education,
            final Map<String, Integer> skills,
            final List<String> tags) {
        this.id = id;
        this.fullName = fullName;
        this.birthYear = birthYear;
        this.gpa = gpa;
        this.email = email;
        this.description = description;
        this.homeAddress = homeAddress;
        this.phone = phone;
        this.website = website;
        this.education = education;
        this.skills = skills;
        this.tags = tags;
    }

    public Cv(
            final String fullName,
            final int birthYear,
            final double gpa,
            final String email,
            final String description,
            final String homeAddress,
            final String phone,
            final String website,
            final Map<String, Integer> education,
            final Map<String, Integer> skills,
            final List<String> tags) {
        
             this(0,
                fullName,
                birthYear,
                gpa,
                email,
                description,
                homeAddress,
                phone,
                website,
                education,
                skills,
                tags);
    }




    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(final String fullName) {
        this.fullName = fullName;
    }

    public int getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(final int birthYear) {
        this.birthYear = birthYear;
    }

    public double getGpa() {
        return gpa;
    }

    public void setGpa(final double gpa) {
        this.gpa = gpa;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public String getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(final String homeAddress) {
        this.homeAddress = homeAddress;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(final String phone) {
        this.phone = phone;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(final String website) {
        this.website = website;
    }

    public Map<String, Integer> getEducation() {
        return education;
    }

    public void setEducation(final Map<String, Integer> education) {
        this.education = education;
    }

    public Map<String, Integer> getSkills() {
        return skills;
    }

    public void setSkills(final Map<String, Integer> skills) {
        this.skills = skills;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(final List<String> tags) {
        this.tags = tags;
    }

    public void print() {
        System.out.println(fullName + birthYear + gpa + email + description + homeAddress + phone + website + education
                + skills + tags);
    }
}
