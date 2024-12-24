
import javax.swing.*;
import java.io.*;
import java.util.*;

public class PerformanceReportGenerator {

    // Метод для генерации отчета
    public static String generateReport(List<Student> students, double excellentGradeThreshold, double goodGradeThreshold, double failGradeThreshold, double averageGradeThreshold) {
        // Списки для хранения отличников, хорошистов, троечников и двоечников
        List<String> excellentStudents = new ArrayList<>();
        List<String> goodStudents = new ArrayList<>();
        List<String> averageStudents = new ArrayList<>();
        List<String> failingStudents = new ArrayList<>();

        // Проходим по всем студентам и классифицируем их
        for (Student student : students) {
            double grade = student.getGrade();

            if (grade >= excellentGradeThreshold) {
                excellentStudents.add(student.getFirstName() + " " + student.getLastName() + " (оценка: " + grade + ")");
            } else if (grade >= goodGradeThreshold) {
                goodStudents.add(student.getFirstName() + " " + student.getLastName() + " (оценка: " + grade + ")");
            } else if (grade >= averageGradeThreshold) {
                averageStudents.add(student.getFirstName() + " " + student.getLastName() + " (оценка: " + grade + ")");
            } else if (grade <= failGradeThreshold) {
                failingStudents.add(student.getFirstName() + " " + student.getLastName() + " (оценка: " + grade + ")");
            }
        }

        // Формируем отчет
        StringBuilder report = new StringBuilder();
        report.append("Отчет об успеваемости:\n");
        report.append("Общее количество учеников: ").append(students.size()).append("\n");

        // Количество и фамилии отличников
        report.append("\nОтличники (оценка >= ").append(excellentGradeThreshold).append("):\n");
        report.append("Количество отличников: ").append(excellentStudents.size()).append("\n");
        if (excellentStudents.isEmpty()) {
            report.append("Нет отличников\n");
        } else {
            for (String student : excellentStudents) {
                report.append(student).append("\n");
            }
        }

        // Количество и фамилии хорошистов
        report.append("\nХорошисты (оценка >= ").append(goodGradeThreshold).append(" и < ").append(excellentGradeThreshold).append("):\n");
        report.append("Количество хорошистов: ").append(goodStudents.size()).append("\n");
        if (goodStudents.isEmpty()) {
            report.append("Нет хорошистов\n");
        } else {
            for (String student : goodStudents) {
                report.append(student).append("\n");
            }
        }

        // Количество и фамилии троечников
        report.append("\nТроечники (оценка >= ").append(averageGradeThreshold).append(" и < ").append(goodGradeThreshold).append("):\n");
        report.append("Количество троечников: ").append(averageStudents.size()).append("\n");
        if (averageStudents.isEmpty()) {
            report.append("Нет троечников\n");
        } else {
            for (String student : averageStudents) {
                report.append(student).append("\n");
            }
        }

        // Количество и фамилии двоечников
        report.append("\nДвоечники (оценка <= ").append(failGradeThreshold).append("):\n");
        report.append("Количество двоечников: ").append(failingStudents.size()).append("\n");
        if (failingStudents.isEmpty()) {
            report.append("Нет двоечников\n");
        } else {
            for (String student : failingStudents) {
                report.append(student).append("\n");
            }
        }

        return report.toString();
    }

    // Метод для сохранения отчета в файл
    public static void saveReportToFile(String report) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Сохранить отчет");
        fileChooser.setSelectedFile(new File("report.txt"));

        int userSelection = fileChooser.showSaveDialog(null);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileToSave))) {
                writer.write(report);
                JOptionPane.showMessageDialog(null, "Отчет успешно сохранен!");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Ошибка при сохранении отчета: " + e.getMessage());
            }
        }
    }
}
