public class Student {
    private int id;
    private String name;
    private String course;
    private String email;
    private String dateCreated;
    private String dateUpdated;

    // ✅ KEEP YOUR ORIGINAL CONSTRUCTOR (unchanged)
    public Student(int id, String name, String course, String email) {
        this.id = id;
        this.name = name;
        this.course = course;
        this.email = email;
        this.dateCreated = new java.util.Date().toString();
        this.dateUpdated = new java.util.Date().toString();
    }

    // ✅ ADD THIS (REQUIRED for CSV loading)
    public Student(int id, String name, String course, String email,
                   String dateCreated, String dateUpdated) {
        this.id = id;
        this.name = name;
        this.course = course;
        this.email = email;
        this.dateCreated = dateCreated;
        this.dateUpdated = dateUpdated;
    }

    public int getId() { 
        return id; 
    }

    public String getName() { 
        return name; 
    }

    public String getCourse() { 
        return course; 
    }

    public String getEmail() { 
        return email; 
    }

    public String getDateCreated() { 
        return dateCreated; 
    }

    public String getDateUpdated() { 
        return dateUpdated; 
    }

    public void setName(String name) {
        this.name = name;
        this.dateUpdated = java.time.LocalDateTime.now().toString();
    }

    public void setCourse(String course) {
        this.course = course;
        this.dateUpdated = java.time.LocalDateTime.now().toString();
    }

    public void setEmail(String email) {
        this.email = email;
        this.dateUpdated = java.time.LocalDateTime.now().toString();
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public void setDateUpdated(String dateUpdated) {
        this.dateUpdated = dateUpdated;
    }
}