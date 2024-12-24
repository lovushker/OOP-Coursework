
public class TeacherSubjectRelation {
    private int teacherId;
    private int subjectId;

    public TeacherSubjectRelation(int teacherId, int subjectId) {
        this.teacherId = teacherId;
        this.subjectId = subjectId;
    }

    // Getters
    public int getTeacherId() {
        return teacherId;
    }

    public int getSubjectId() {
        return subjectId;
    }

    // Setters
    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }
}

