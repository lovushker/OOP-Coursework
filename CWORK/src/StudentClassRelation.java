
public class StudentClassRelation {
    private int studentId;
    private int classId;

    public StudentClassRelation(int studentId, int classId) {
        this.studentId = studentId;
        this.classId = classId;
    }

    // Getters
    public int getStudentId() {
        return studentId;
    }

    public int getClassId() {
        return classId;
    }

    // Setters
    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }
}
