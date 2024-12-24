
public class Student {
    private int id;
    private String firstName;
    private String lastName;
    private double grade; // Успеваемость

    // Конструктор
    public Student(int id, String firstName, String lastName, double grade) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.grade = grade;
    }

    // Геттеры
    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public double getGrade() {
        return grade;
    }

    // Сеттеры
    public void setFirstName(String firstName) {
        if (firstName != null && !firstName.trim().isEmpty()) {
            this.firstName = firstName;
        } else {
            throw new IllegalArgumentException("Имя не может быть пустым");
        }
    }

    public void setLastName(String lastName) {
        if (lastName != null && !lastName.trim().isEmpty()) {
            this.lastName = lastName;
        } else {
            throw new IllegalArgumentException("Фамилия не может быть пустой");
        }
    }

    public void setGrade(double grade) {
        if (grade >= 0.0 && grade <= 5.0) { 
            this.grade = grade;
        } else {
            throw new IllegalArgumentException("Успеваемость должна быть от 0.0 до 5.0");
        }
    }
}
