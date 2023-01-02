package edu.ieu.tr.cvm;

import java.util.List;
import java.util.Map;

public class AcademicCv extends Cv {
    private String jobAddress;
    private Map<String, Integer> publications;
    
    public AcademicCv(final int id, final String fullName, final int birthYear, final double gpa, final String email,
            final String description,
            final String homeAddress, final String jobAddress, final String phone, final String website,
            final Map<String, Integer> education,
            final Map<String, Integer> skills, final Map<String, Integer> publications, final List<String> tags) {
        super(id, fullName, birthYear, gpa, email, description, homeAddress, phone, website, education,
                skills,
                tags);
        this.jobAddress = jobAddress;
        this.publications = publications;
    }

    public AcademicCv(final String fullName, final int birthYear, final double gpa, final String email,
            final String description,
            final String homeAddress, final String jobAddress, final String phone, final String website,
            final Map<String, Integer> education,
            final Map<String, Integer> skills, final Map<String, Integer> publications, final List<String> tags) {
        this(-1, fullName, birthYear, gpa, email, description, homeAddress, jobAddress, phone, website, education,
                skills,
             publications,
                tags);
    }
     

	public String getJobAddress() {
		return jobAddress;
	}

	public void setJobAddress(String jobAddress) {
		this.jobAddress = jobAddress;
	}

	public Map<String, Integer> getPublications() {
		return publications;
	}

	public void setPublications(Map<String, Integer> publications) {
		this.publications = publications;
	}
}
