
import org.w3c.dom.*;
import java.io.*;
import java.util.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

// Класс для управления базой данных
class SchoolDatabase {
    List<Student> students = new ArrayList<>();
    List<Teacher> teachers = new ArrayList<>();
    List<Subject> subjects = new ArrayList<>();
    List<SchoolClass> schoolClasses = new ArrayList<>();
    List<TeacherSubjectRelation> teacherSubjectRelations = new ArrayList<>();
    List<TeacherClassRelation> teacherClassRelations = new ArrayList<>();
    List<StudentClassRelation> studentClassRelations = new ArrayList<>();

    
    
    

    // Сохранение данных в XML файл вручную
    public void saveDataToXML(File file) throws Exception {
        // Создание документа XML
        DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
        Document document = documentBuilder.newDocument();

        // Создание корневого элемента <SchoolData>
        Element root = document.createElement("SchoolData");
        document.appendChild(root);

        // Сохраняем студентов
        Element studentsElement = document.createElement("Students");
        root.appendChild(studentsElement);
        for (Student student : students) {
            Element studentElement = document.createElement("Student");
            studentElement.setAttribute("id", String.valueOf(student.getId()));
            studentElement.setAttribute("firstName", student.getFirstName());
            studentElement.setAttribute("lastName", student.getLastName());
            studentElement.setAttribute("grade", String.valueOf(student.getGrade()));
            studentsElement.appendChild(studentElement);
        }


        // Сохраняем учителей
        Element teachersElement = document.createElement("Teachers");
        root.appendChild(teachersElement);
        for (Teacher teacher : teachers) {
            Element teacherElement = document.createElement("Teacher");
            teacherElement.setAttribute("id", String.valueOf(teacher.getId()));
            teacherElement.setAttribute("firstName", teacher.getFirstName());
            teacherElement.setAttribute("lastName", teacher.getLastName());
            teachersElement.appendChild(teacherElement);
        }

        // Сохраняем предметы
        Element subjectsElement = document.createElement("Subjects");
        root.appendChild(subjectsElement);
        for (Subject subject : subjects) {
            Element subjectElement = document.createElement("Subject");
            subjectElement.setAttribute("id", String.valueOf(subject.getId()));
            subjectElement.setAttribute("name", subject.getName());
            subjectsElement.appendChild(subjectElement);
        }

        // Сохраняем классы
        Element schoolClassesElement = document.createElement("SchoolClasses");
        root.appendChild(schoolClassesElement);
        for (SchoolClass schoolClass : schoolClasses) {
            Element schoolClassElement = document.createElement("SchoolClass");
            schoolClassElement.setAttribute("id", String.valueOf(schoolClass.getId()));
            schoolClassElement.setAttribute("name", schoolClass.getName());
            schoolClassesElement.appendChild(schoolClassElement);
        }

        // Сохраняем связи между учителями и предметами
        Element teacherSubjectRelationsElement = document.createElement("TeacherSubjectRelations");
        root.appendChild(teacherSubjectRelationsElement);
        for (TeacherSubjectRelation relation : teacherSubjectRelations) {
            Element relationElement = document.createElement("Relation");
            relationElement.setAttribute("teacherId", String.valueOf(relation.getTeacherId()));
            relationElement.setAttribute("subjectId", String.valueOf(relation.getSubjectId()));
            teacherSubjectRelationsElement.appendChild(relationElement);
        }

        // Сохраняем связи между учителями и классами
        Element teacherClassRelationsElement = document.createElement("TeacherClassRelations");
        root.appendChild(teacherClassRelationsElement);
        for (TeacherClassRelation relation : teacherClassRelations) {
            Element relationElement = document.createElement("Relation");
            relationElement.setAttribute("teacherId", String.valueOf(relation.getTeacherId()));
            relationElement.setAttribute("classId", String.valueOf(relation.getClassId()));
            teacherClassRelationsElement.appendChild(relationElement);
        }
     // Сохраняем связи между студентами и классами
        Element studentClassRelationsElement = document.createElement("StudentClassRelations");
        root.appendChild(studentClassRelationsElement);
        for (StudentClassRelation relation : studentClassRelations) {
            Element relationElement = document.createElement("Relation");
            relationElement.setAttribute("studentId", String.valueOf(relation.getStudentId()));
            relationElement.setAttribute("classId", String.valueOf(relation.getClassId()));
            studentClassRelationsElement.appendChild(relationElement);
        }


        // Создание Transformer для записи XML в файл
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();

        // Форматирование вывода
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");

        // Запись в файл
        DOMSource source = new DOMSource(document);
        StreamResult result = new StreamResult(file);

        transformer.transform(source, result);

        // Вывод сообщения о сохранении
        System.out.println("Данные успешно сохранены в XML файл.");
    }
    


 // Загрузка данных из XML файла вручную
    public void loadData(File file) throws Exception {
        // Проверяем, существует ли файл
        if (!file.exists()) return;

        // Очищаем коллекции перед загрузкой новых данных
        students.clear();
        teachers.clear();
        subjects.clear();
        schoolClasses.clear();
        teacherSubjectRelations.clear();
        teacherClassRelations.clear();
        studentClassRelations.clear();

        // Создаем парсер
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(file);
        doc.getDocumentElement().normalize();

        // Загружаем студентов с учетом нового атрибута grade
        NodeList studentList = doc.getElementsByTagName("Student");
        for (int i = 0; i < studentList.getLength(); i++) {
            Node node = studentList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                int id = Integer.parseInt(element.getAttribute("id"));
                String firstName = element.getAttribute("firstName");
                String lastName = element.getAttribute("lastName");
                double grade = Double.parseDouble(element.getAttribute("grade")); // Извлекаем атрибут grade
                students.add(new Student(id, firstName, lastName, grade)); // Создаем студента с grade
            }
        }

        // Загружаем учителей
        NodeList teacherList = doc.getElementsByTagName("Teacher");
        for (int i = 0; i < teacherList.getLength(); i++) {
            Node node = teacherList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                int id = Integer.parseInt(element.getAttribute("id"));
                String firstName = element.getAttribute("firstName");
                String lastName = element.getAttribute("lastName");
                teachers.add(new Teacher(id, firstName, lastName));
            }
        }

        // Загружаем предметы
        NodeList subjectList = doc.getElementsByTagName("Subject");
        for (int i = 0; i < subjectList.getLength(); i++) {
            Node node = subjectList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                int id = Integer.parseInt(element.getAttribute("id"));
                String name = element.getAttribute("name");
                subjects.add(new Subject(id, name));
            }
        }

        // Загружаем классы
        NodeList classList = doc.getElementsByTagName("SchoolClass");
        for (int i = 0; i < classList.getLength(); i++) {
            Node node = classList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                int id = Integer.parseInt(element.getAttribute("id"));
                String name = element.getAttribute("name");
                schoolClasses.add(new SchoolClass(id, name));
            }
        }

        // Загружаем связи между учителями и предметами
        NodeList teacherSubjectRelationList = doc.getElementsByTagName("TeacherSubjectRelations");
        if (teacherSubjectRelationList.getLength() > 0) {
            Node teacherSubjectNode = teacherSubjectRelationList.item(0);
            if (teacherSubjectNode.getNodeType() == Node.ELEMENT_NODE) {
                Element teacherSubjectElement = (Element) teacherSubjectNode;
                NodeList relations = teacherSubjectElement.getElementsByTagName("Relation");
                for (int i = 0; i < relations.getLength(); i++) {
                    Node relationNode = relations.item(i);
                    if (relationNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element relationElement = (Element) relationNode;
                        int teacherId = Integer.parseInt(relationElement.getAttribute("teacherId"));
                        int subjectId = Integer.parseInt(relationElement.getAttribute("subjectId"));
                        teacherSubjectRelations.add(new TeacherSubjectRelation(teacherId, subjectId));
                    }
                }
            }
        }

        // Загружаем связи между учителями и классами
        NodeList teacherClassRelationList = doc.getElementsByTagName("TeacherClassRelations");
        if (teacherClassRelationList.getLength() > 0) {
            Node teacherClassNode = teacherClassRelationList.item(0);
            if (teacherClassNode.getNodeType() == Node.ELEMENT_NODE) {
                Element teacherClassElement = (Element) teacherClassNode;
                NodeList relations = teacherClassElement.getElementsByTagName("Relation");
                for (int i = 0; i < relations.getLength(); i++) {
                    Node relationNode = relations.item(i);
                    if (relationNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element relationElement = (Element) relationNode;
                        int teacherId = Integer.parseInt(relationElement.getAttribute("teacherId"));
                        int classId = Integer.parseInt(relationElement.getAttribute("classId"));
                        teacherClassRelations.add(new TeacherClassRelation(teacherId, classId));
                    }
                }
            }
        }
     // Загружаем связи между студентами и классами
        NodeList studentClassRelationList = doc.getElementsByTagName("StudentClassRelations");
        if (studentClassRelationList.getLength() > 0) {
            Node studentClassNode = studentClassRelationList.item(0);
            if (studentClassNode.getNodeType() == Node.ELEMENT_NODE) {
                Element studentClassElement = (Element) studentClassNode;
                NodeList relations = studentClassElement.getElementsByTagName("Relation");
                for (int i = 0; i < relations.getLength(); i++) {
                    Node relationNode = relations.item(i);
                    if (relationNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element relationElement = (Element) relationNode;
                        int studentId = Integer.parseInt(relationElement.getAttribute("studentId"));
                        int classId = Integer.parseInt(relationElement.getAttribute("classId"));
                        studentClassRelations.add(new StudentClassRelation(studentId, classId));
                    }
                }
            }
        }

        // Вывод сообщения о загрузке
        System.out.println("Данные загружены из XML файла.");
    }


    // Добавление данных
 // Добавление студентов
    public void addStudent(Student student) {
        students.add(student);
    }
    public void addStudent(String firstName, String lastName, double grade) {
        int newId = students.isEmpty() ? 1 : students.get(students.size() - 1).getId() + 1;
        students.add(new Student(newId, firstName, lastName, grade));
    }


    // Удаление студента
    public void removeStudent(int id) {
        students.removeIf(student -> student.getId() == id);
    }

    // Редактирование студента
    public void editStudent(int id, String firstName, String lastName, double grade) {
        for (Student student : students) {
            if (student.getId() == id) {
                student.setFirstName(firstName);
                student.setLastName(lastName);
                student.setGrade(grade);
                break;
            }
        }
    }

    // Добавление учителей
    public void addTeacher(Teacher teacher) {
        teachers.add(teacher);
    }

    // Удаление учителя
    public void removeTeacher(int id) {
        teachers.removeIf(teacher -> teacher.getId() == id);
    }

    // Редактирование учителя
    public void editTeacher(int id, String firstName, String lastName) {
        for (Teacher teacher : teachers) {
            if (teacher.getId() == id) {
                teacher.setFirstName(firstName); 
                teacher.setLastName(lastName);
                break;
            }
        }
    }

    // Добавление предметов
    public void addSubject(Subject subject) {
        subjects.add(subject);
    }

    // Удаление предмета
    public void removeSubject(int id) {
        subjects.removeIf(subject -> subject.getId() == id);
    }

    // Редактирование предмета
    public void editSubject(int id, String name) {
        for (Subject subject : subjects) {
            if (subject.getId() == id) {
                subject.setName(name);
                break;
            }
        }
    }

    // Добавление классов
    public void addSchoolClass(SchoolClass schoolClass) {
        schoolClasses.add(schoolClass);
    }

    // Удаление класса
    public void removeSchoolClass(int id) {
        schoolClasses.removeIf(schoolClass -> schoolClass.getId() == id);
    }

    // Редактирование класса
    public void editSchoolClass(int id, String name) {
        for (SchoolClass schoolClass : schoolClasses) {
            if (schoolClass.getId() == id) {
                schoolClass.setName(name);
                break;
            }
        }
    }

    public boolean addTeacherSubjectRelation(int teacherId, int subjectId) {
        if (teachers.stream().noneMatch(t -> t.getId() == teacherId) || 
            subjects.stream().noneMatch(s -> s.getId() == subjectId)) {
            return false; // Нарушение целостности
        }
        teacherSubjectRelations.add(new TeacherSubjectRelation(teacherId, subjectId));
        return true;
    }

    public boolean removeTeacherSubjectRelation(int teacherId, int subjectId) {
        return teacherSubjectRelations.removeIf(r -> r.getTeacherId() == teacherId && r.getSubjectId() == subjectId);
    }
    
    public boolean addTeacherClassRelation(int teacherId, int classId) {
        if (teachers.stream().noneMatch(t -> t.getId() == teacherId) || 
            schoolClasses.stream().noneMatch(c -> c.getId() == classId)) {
            return false; // Нарушение целостности
        }
        teacherClassRelations.add(new TeacherClassRelation(teacherId, classId));
        return true;
    }
    
    public boolean removeTeacherClassRelation(int teacherId, int classId) {
        return teacherClassRelations.removeIf(r -> r.getTeacherId() == teacherId && r.getClassId() == classId);
    }
    
    public boolean addStudentClassRelation(int studentId, int classId) {
        if (students.stream().noneMatch(s -> s.getId() == studentId) || 
            schoolClasses.stream().noneMatch(c -> c.getId() == classId)) {
            return false; // Нарушение целостности
        }
        studentClassRelations.add(new StudentClassRelation(studentId, classId));
        return true;
    }

    public boolean removeStudentClassRelation(int studentId, int classId) {
        return studentClassRelations.removeIf(relation -> relation.getStudentId() == studentId && relation.getClassId() == classId);
    }

}
