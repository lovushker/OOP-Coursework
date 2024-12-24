
class TeacherClassRelation {
    private int teacherId;
    private int classId;

    public TeacherClassRelation(int teacherId, int classId) {
        this.teacherId = teacherId;
        this.classId = classId;
    }

    // Getters
    public int getTeacherId() {
        return teacherId;
    }

    public int getClassId() {
        return classId;
    }

    // Setters
    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }
}
