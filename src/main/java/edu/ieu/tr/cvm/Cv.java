package edu.ieu.tr.cvm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
  A CV can be created via constructor-like style
  by using a utility method Cv.create(...).
  Example:
  Cv cv = Cv.create("a", 0, "b", "c", "d", "e", "f", "g");
  or
  Cv cv = Cv.create("a", 0, "b", "c", "d", "e", "f",
  skills,
  education,
  additionalSkills,
  tags);
  These two CVs have 0 id, since they have not been inserted yet.
  However, it can be passed as the first argument.

  The above method implicitly uses Cv.Builder.getInstance()...build().
  Example:
  Cv cv = Cv.Builder.getInstance()
                    .fullName("a")
                    .birthYear(2001)
                    .email("a@izmirekonomi.edu.tr")
                    .description("I am a student.")
                    .homeAddress("izmir")
                    .jobAdress("izmir")
                    .telephoneNumber("unknown")
                    .websiteLink("izmirekonomi.edu.tr")
                    .build();
  The build method returns a new CV with the attributes of the builder.
  
  If a cv shall be created from an existing CV to be updated,
  then use Cv.Builder.getInstance(cv)...build().
  Example:
  // the same as cv1
  Cv cv2 = Cv.Builder.getInstance(cv1).build();
  // the same as cv1 but its name is b
  Cv cv3 = Cv.Builder.getInstance(cv1).fullName("b").build();
  
  Why is Builder pattern necessary?
  The Cv class is immutable and it is hard to update its attributes without
  Builder pattern.
  Immutable classes are generally thread-safe,
  thus they can be passed to threads without considering synchronisation.
 */
public final class Cv {
    public static final class Builder {
        public static Builder getInstance(final Cv cv) {
            return cv == null ? new Builder()
                    : new Builder()
                            .fullName(cv.getFullName())
                            .birthYear(cv.getBirthYear())
                            .email(cv.getEmail())
                            .description(cv.getDescription())
                            .homeAddress(cv.getHomeAddress())
                            .jobAdress(cv.getJobAdress())
                            .telephoneNumber(cv.getTelephoneNumber())
                            .websiteLink(cv.getWebsiteLink())
                            .skills(cv.getSkills())
                            .education(cv.getEducation())
                            .additionalSkills(cv.getAdditionalSkills())
                            .tags(cv.getTags());
        }

        public static Builder getInstance() {
            return getInstance(null);
        }

        private int id;
        private String fullName;
        private int birthYear;
        private String email;
        private String description;
        private String homeAddress;
        private String jobAdress;
        private String telephoneNumber;
        private String websiteLink;
        private Map<String, Integer> skills;
        private Map<String, Integer> education;
        private Map<String, Integer> additionalSkills;

        private List<String> tags;

        private Builder() {
        }

        public Builder id(final int id) {
            this.id = id;
            return this;
        }

        public Builder fullName(final String fullName) {
            this.fullName = fullName;
            return this;
        }

        public Builder birthYear(final int birthYear) {
            this.birthYear = birthYear;
            return this;
        }

        public Builder email(final String email) {
            this.email = email;
            return this;
        }

        public Builder description(final String description) {
            this.description = description;
            return this;
        }

        public Builder homeAddress(final String homeAddress) {
            this.homeAddress = homeAddress;
            return this;
        }

        public Builder jobAdress(final String jobAdress) {
            this.jobAdress = jobAdress;
            return this;
        }

        public Builder telephoneNumber(final String telephoneNumber) {
            this.telephoneNumber = telephoneNumber;
            return this;
        }

        public Builder websiteLink(final String websiteLink) {
            this.websiteLink = websiteLink;
            return this;
        }

        public Builder skills(final Map<String, Integer> skills) {
            this.skills = skills;
            return this;
        }

        public Builder education(final Map<String, Integer> education) {
            this.education = education;
            return this;
        }

        public Builder additionalSkills(final Map<String, Integer> additionalSkills) {
            this.additionalSkills = additionalSkills;
            return this;
        }

        public Builder tags(final List<String> tags) {
            this.tags = tags;
            return this;
        }

        public Cv build() {
            return new Cv(this);
        }
    }

    public static Cv create(final int id, final String fullName,
            final int birthYear, final String email,
            final String description, final String homeAddress,
            final String jobAdress, final String telephoneNumber,
            final String websiteLink, final Map<String, Integer> skills,
            final Map<String, Integer> education,
            final Map<String, Integer> additionalSkills,
            final List<String> tags) {
        return Cv.Builder.getInstance()
                .fullName(fullName)
                .birthYear(birthYear)
                .email(email)
                .description(jobAdress)
                .homeAddress(homeAddress)
                .jobAdress(jobAdress)
                .telephoneNumber(telephoneNumber)
                .websiteLink(websiteLink)
                .skills(skills)
                .education(education)
                .additionalSkills(additionalSkills)
                .tags(tags)
                .build();
    }
    public static Cv create(final int id, final String fullName,
            final int birthYear, final String email,
            final String description, final String homeAddress,
            final String jobAdress, final String telephoneNumber,
            final String websiteLink) {
        return Cv.create(id, fullName, birthYear, email, description,
                homeAddress, jobAdress, telephoneNumber,
                websiteLink, new HashMap<>(), new HashMap<>(), new HashMap<>(),
                new ArrayList<>());
    }
    public static Cv create(final String fullName,
            final int birthYear, final String email,
            final String description, final String homeAddress,
            final String jobAdress, final String telephoneNumber,
            final String websiteLink, final Map<String, Integer> skills,
            final Map<String, Integer> education,
            final Map<String, Integer> additionalSkills,
            final List<String> tags) {
        return Cv.create(0, fullName, birthYear, email, description,
                homeAddress, jobAdress, telephoneNumber,
                websiteLink, skills, education, additionalSkills,
                tags);
    }
    public static Cv create(final String fullName,
            final int birthYear, final String email,
            final String description, final String homeAddress,
            final String jobAdress, final String telephoneNumber,
            final String websiteLink) {
        return Cv.create(0, fullName, birthYear, email, description,
                homeAddress, jobAdress, telephoneNumber,
                websiteLink);
    }
    private final int id;
    private final String fullName;
    private final int birthYear;
    private final String email;
    private final String description;
    private final String homeAddress;
    private final String jobAdress;
    private final String telephoneNumber;

    private final String websiteLink;

    private final Map<String, Integer> skills;

    private final Map<String, Integer> education;

    private final Map<String, Integer> additionalSkills;

    private final List<String> tags;

    private Cv(final Builder builder) {
        id = builder.id;
        fullName = builder.fullName;
        birthYear = builder.birthYear;
        email = builder.email;
        description = builder.description;
        homeAddress = builder.homeAddress;
        jobAdress = builder.jobAdress;
        telephoneNumber = builder.telephoneNumber;
        websiteLink = builder.websiteLink;
        skills = builder.skills == null ? new HashMap<>() : builder.skills;
        education = builder.education == null
                ? new HashMap<>()
                : builder.education;
        additionalSkills = builder.additionalSkills == null
                ? new HashMap<>()
                : builder.additionalSkills;
        tags = builder.tags == null ? new ArrayList<>() : builder.tags;
    }

    public int getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public int getBirthYear() {
        return birthYear;
    }

    public String getEmail() {
        return email;
    }

    public String getDescription() {
        return description;
    }

    public String getHomeAddress() {
        return homeAddress;
    }

    public String getJobAdress() {
        return jobAdress;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public String getWebsiteLink() {
        return websiteLink;
    }

    public Map<String, Integer> getSkills() {
        return skills;
    }

    public Map<String, Integer> getEducation() {
        return education;
    }

    public Map<String, Integer> getAdditionalSkills() {
        return additionalSkills;
    }

    public List<String> getTags() {
        return tags;
    }
}
