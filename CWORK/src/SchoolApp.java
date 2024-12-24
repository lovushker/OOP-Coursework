import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.Set;


public class SchoolApp extends JFrame {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JTable subjectNteacherTable, teacherNclassTable, studentNclassTable,teacherTable,classTable, subjectTable;
    private JPanel tablePanel;
    private JComboBox<String> tableSelector, classSelector;
    private CardLayout cardLayout;
    private JButton generateReportButton;
    private JButton addButton, editButton, deleteButton;
    private SchoolDatabase schoolDatabase = new SchoolDatabase();
    
    public SchoolApp() {
        super("Школьная информационная система");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 600);
        setLayout(new BorderLayout());

        subjectNteacherTable = new JTable();
        teacherNclassTable = new JTable();
        studentNclassTable = new JTable();
        teacherTable = new JTable();
        classTable = new JTable();
        subjectTable = new JTable();

        cardLayout = new CardLayout();
        tablePanel = new JPanel(cardLayout);
        tablePanel.add(new JScrollPane(subjectNteacherTable), "Предметы и преподаватели");
        tablePanel.add(new JScrollPane(teacherNclassTable), "Преподаватели и классы");
        tablePanel.add(new JScrollPane(studentNclassTable), "Ученики");
        tablePanel.add(new JScrollPane(teacherTable), "Преподаватели");  // Добавление таблицы преподавателей
        tablePanel.add(new JScrollPane(classTable), "Классы");  // Добавление таблицы классов
        tablePanel.add(new JScrollPane(subjectTable), "Предметы");  // Добавление таблицы классов

        tableSelector = new JComboBox<>(new String[]{"Предметы и преподаватели", "Преподаватели и классы", "Ученики", "Преподаватели", "Классы", "Предметы"});
        tableSelector.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String selectedTable = (String) tableSelector.getSelectedItem();
                cardLayout.show(tablePanel, selectedTable);
                classSelector.setVisible(selectedTable.equals("Ученики"));
            }
        });

        classSelector = new JComboBox<>(new String[]{"Все классы"});
        classSelector.setVisible(false);

        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");

        JMenuItem loadXmlItem = new JMenuItem("Load XML");
        JMenuItem saveXmlItem = new JMenuItem("Save XML");

        fileMenu.add(loadXmlItem);
        fileMenu.add(saveXmlItem);

        menuBar.add(fileMenu);
        setJMenuBar(menuBar);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout());

        addButton = new JButton("Добавить");
        editButton = new JButton("Редактировать");
        deleteButton = new JButton("Удалить");
        generateReportButton = new JButton("Создать отчет");

        bottomPanel.add(addButton);
        bottomPanel.add(editButton);
        bottomPanel.add(deleteButton);
        bottomPanel.add(generateReportButton);

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout());
        topPanel.add(tableSelector);
        topPanel.add(classSelector);

        add(topPanel, BorderLayout.NORTH);
        add(tablePanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        loadXmlItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int option = fileChooser.showOpenDialog(SchoolApp.this);
                if (option == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    try {
                        // Загружаем данные из XML
                        schoolDatabase.loadData(file);
                        updateTables();  // Обновляем таблицы после загрузки данных
                        JOptionPane.showMessageDialog(SchoolApp.this, "Данные успешно загружены из XML.", "Успех", JOptionPane.INFORMATION_MESSAGE);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(SchoolApp.this, "Ошибка при загрузке XML файла.", "Ошибка", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        
        saveXmlItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                // Показать диалоговое окно сохранения файла
                int option = fileChooser.showSaveDialog(SchoolApp.this);
                if (option == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    try {
                        // Сохраняем данные в XML
                        schoolDatabase.saveDataToXML(file);
                        JOptionPane.showMessageDialog(SchoolApp.this, "Данные успешно сохранены в XML.", "Успех", JOptionPane.INFORMATION_MESSAGE);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(SchoolApp.this, "Ошибка при сохранении XML файла.", "Ошибка", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        /*
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               
            }
        });
*/

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Создание окна выбора
                String[] options = {"Ученик", "Преподаватель", "Предмет", "Класс"};
                
                // Окно выбора
                int choice = JOptionPane.showOptionDialog(
                        null, 
                        "Выберите, что хотите добавить:", 
                        "Добавить запись", 
                        JOptionPane.DEFAULT_OPTION, 
                        JOptionPane.INFORMATION_MESSAGE, 
                        null, 
                        options, 
                        options[0] // По умолчанию выбран первый элемент
                );
                
                // В зависимости от выбора открываем нужное окно для добавления
                switch (choice) {
                    case 0:
                        addStudent(); // Метод для добавления ученика
                        break;
                    case 1:
                        addTeacher(); // Метод для добавления преподавателя
                        break;
                    case 2:
                        addSubject(); // Метод для добавления предмета
                        break;
                    case 3:
                        addClass(); // Метод для добавления класса
                        break;
                    default:
                        break;
                }
            }
        });
        
        
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = -1;
                String selectedTable = (String) tableSelector.getSelectedItem(); // Получаем текущую активную таблицу

                if ("Предметы и преподаватели".equals(selectedTable)) {
                    selectedRow = subjectNteacherTable.getSelectedRow();
                    if (selectedRow != -1) {
                        editSubjectNTeacher(selectedRow);  // Редактируем предмет и преподавателя
                    }
                } else if ("Преподаватели и классы".equals(selectedTable)) {
                    selectedRow = teacherNclassTable.getSelectedRow();
                    if (selectedRow != -1) {
                        editTeacherNClass(selectedRow);  // Редактируем преподавателя и класс
                    }
                } else if ("Ученики".equals(selectedTable)) {
                    selectedRow = studentNclassTable.getSelectedRow();
                    if (selectedRow != -1) {
                        editStudentNClass(selectedRow);  // Редактируем ученика и класс
                    }
                } else if ("Преподаватели".equals(selectedTable)) {
                    selectedRow = teacherTable.getSelectedRow();
                    if (selectedRow != -1) {
                        editTeacher(selectedRow);  // Редактируем преподавателя
                    }
                } else if ("Классы".equals(selectedTable)) {
                    selectedRow = classTable.getSelectedRow();
                    if (selectedRow != -1) {
                        editClass(selectedRow);  // Редактируем класс
                    }
                } else if ("Предметы".equals(selectedTable)) {
                    selectedRow = subjectTable.getSelectedRow();
                    if (selectedRow != -1) {
                        editSubject(selectedRow);  // Редактируем предмет
                    }
                }

                // Если не выбрана строка в таблице, показываем сообщение
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(null, "Выберите запись для редактирования.");
                }
            }
        });

        

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = -1;
                String selectedTable = (String) tableSelector.getSelectedItem(); // Получаем текущую активную таблицу

                // Проверяем, какая таблица активна, и получаем выбранную строку
                if ("Ученики".equals(selectedTable)) {
                    selectedRow = studentNclassTable.getSelectedRow();
                    if (selectedRow != -1) {
                        deleteStudent(selectedRow);  // Удаляем ученика
                    }
                } else if ("Преподаватели".equals(selectedTable)) {
                    selectedRow = teacherTable.getSelectedRow();
                    if (selectedRow != -1) {
                        deleteTeacher(selectedRow);  // Удаляем преподавателя
                    }
                } else if ("Предметы".equals(selectedTable)) {
                    selectedRow = subjectTable.getSelectedRow();
                    if (selectedRow != -1) {
                        deleteSubject(selectedRow);  // Удаляем предмет
                    }
                } else if ("Классы".equals(selectedTable)) {
                    selectedRow = classTable.getSelectedRow();
                    if (selectedRow != -1) {
                        deleteClass(selectedRow);  // Удаляем класс
                    }
                }

                // Если не выбрана строка в таблице, показываем сообщение
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(null, "Выберите запись для удаления или другую таблицу.");
                    
                }
            }
        });

        generateReportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	generateAndSavePerformanceReport();
            }
        });

        
        classSelector.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                filterStudentsByClass((String) classSelector.getSelectedItem());
            }
        });
        
        
        
        
        
        
}
    
    
    private void addStudent() {
        // Создаем панель для ввода данных ученика
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 2));
        
        JTextField nameField = new JTextField();
        JTextField surnameField = new JTextField();
        JTextField gradeField = new JTextField();
        
        // Создаем JComboBox для выбора класса
        JComboBox<String> classComboBox = new JComboBox<>();
        classComboBox.addItem("Не определен");  // Добавляем пункт "Не определен"
        
        // Заполняем JComboBox существующими классами
        for (SchoolClass schoolClass : schoolDatabase.schoolClasses) {
            classComboBox.addItem(schoolClass.getName());
        }
        
        panel.add(new JLabel("Имя:"));
        panel.add(nameField);
        
        panel.add(new JLabel("Фамилия:"));
        panel.add(surnameField);
        
        panel.add(new JLabel("Успеваемость:"));
        panel.add(gradeField);
        
        panel.add(new JLabel("Класс:"));
        panel.add(classComboBox);

        int option = JOptionPane.showConfirmDialog(null, panel, "Добавить ученика", JOptionPane.OK_CANCEL_OPTION);
        
        if (option == JOptionPane.OK_OPTION) {
            // Сохраняем введенные данные
            String name = nameField.getText();
            String surname = surnameField.getText();
            double grade = 0.0;
            
            // Проверяем, что введенное значение для grade можно преобразовать в double
            try {
                grade = Double.parseDouble(gradeField.getText());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Ошибка: введено некорректное значение для успеваемости.");
                return;  // Если ввод некорректен, выходим из метода
            }

            // Генерация уникального id для нового студента
            int newId = schoolDatabase.students.stream()
                .mapToInt(student -> student.getId())
                .max()
                .orElse(0) + 1;  // Если список студентов пуст, начнем с id = 1
            
            // Получаем выбранный класс
            String selectedClassName = (String) classComboBox.getSelectedItem();
            int classId = -1;
            
            if (!selectedClassName.equals("Не определен")) {
                // Ищем id класса по имени
                classId = schoolDatabase.schoolClasses.stream()
                    .filter(schoolClass -> schoolClass.getName().equals(selectedClassName))
                    .findFirst()
                    .map(schoolClass -> schoolClass.getId())
                    .orElse(-1);
            }
            
            // Добавляем ученика в базу данных
            Student newStudent = new Student(newId, name, surname, grade);
            schoolDatabase.students.add(newStudent);
            
            // Если выбран существующий класс, добавляем зависимость через addStudentClassRelation
            if (classId != -1) {
                boolean relationAdded = schoolDatabase.addStudentClassRelation(newStudent.getId(), classId);
                if (!relationAdded) {
                    JOptionPane.showMessageDialog(null, "Ошибка: не удалось добавить зависимость между студентом и классом.");
                }
            }
            
            // Обновляем таблицу
            updateTables();
        }
    }

    private void addTeacher() {
        // Создаем панель для ввода данных учителя
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 2));

        JTextField nameField = new JTextField();
        JTextField surnameField = new JTextField();

        // Создаем списки для выбора нескольких предметов и классов
        JList<String> subjectList = new JList<>();
        JList<String> classList = new JList<>();

        // Заполняем список предметов
        DefaultListModel<String> subjectModel = new DefaultListModel<>();
        for (Subject subject : schoolDatabase.subjects) {
            subjectModel.addElement(subject.getName());
        }
        subjectList.setModel(subjectModel);
        subjectList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION); // Множественный выбор

        // Заполняем список классов
        DefaultListModel<String> classModel = new DefaultListModel<>();
        for (SchoolClass schoolClass : schoolDatabase.schoolClasses) {
            classModel.addElement(schoolClass.getName());
        }
        classList.setModel(classModel);
        classList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION); // Множественный выбор

        // Добавляем компоненты на панель
        panel.add(new JLabel("Имя:"));
        panel.add(nameField);
        panel.add(new JLabel("Фамилия:"));
        panel.add(surnameField);
        panel.add(new JLabel("Предметы:"));
        panel.add(new JScrollPane(subjectList)); // Прокрутка для длинного списка
        panel.add(new JLabel("Классы:"));
        panel.add(new JScrollPane(classList)); // Прокрутка для длинного списка

        // Диалоговое окно для подтверждения
        int option = JOptionPane.showConfirmDialog(null, panel, "Добавить учителя", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            // Сохраняем введенные данные
            String name = nameField.getText();
            String surname = surnameField.getText();

            if (name.isEmpty() || surname.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Ошибка: имя и фамилия обязательны для заполнения.");
                return;
            }

            // Генерация уникального ID для нового учителя
            int newId = schoolDatabase.teachers.stream()
                .mapToInt(teacher -> teacher.getId())
                .max()
                .orElse(0) + 1;

            // Добавляем учителя в базу данных
            Teacher newTeacher = new Teacher(newId, name, surname);
            schoolDatabase.addTeacher(newTeacher);

            // Добавляем зависимости с предметами
            for (String selectedSubject : subjectList.getSelectedValuesList()) {
                int subjectId = schoolDatabase.subjects.stream()
                    .filter(subject -> subject.getName().equals(selectedSubject))
                    .findFirst()
                    .map(subject -> subject.getId())
                    .orElse(-1);

                if (subjectId != -1) {
                    schoolDatabase.addTeacherSubjectRelation(newTeacher.getId(), subjectId);
                }
            }

            // Добавляем зависимости с классами
            for (String selectedClass : classList.getSelectedValuesList()) {
                int classId = schoolDatabase.schoolClasses.stream()
                    .filter(schoolClass -> schoolClass.getName().equals(selectedClass))
                    .findFirst()
                    .map(schoolClass -> schoolClass.getId())
                    .orElse(-1);

                if (classId != -1) {
                    schoolDatabase.addTeacherClassRelation(newTeacher.getId(), classId);
                }
            }

            // Обновляем таблицы
            updateTables();
        }
    }
    
    private void addSubject() {
        // Создаем панель для ввода данных
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1, 2)); // Одна строка и два столбца

        JTextField nameField = new JTextField(); // Поле для ввода названия предмета

        panel.add(new JLabel("Название предмета:"));
        panel.add(nameField);

        // Отображаем окно ввода данных
        int option = JOptionPane.showConfirmDialog(
            null, panel, "Добавить предмет", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            // Получаем введенное название предмета
            String name = nameField.getText().trim();

            // Проверяем, что название введено
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Ошибка: название предмета не может быть пустым.");
                return;
            }

            // Генерируем уникальный id для нового предмета
            int newId = schoolDatabase.subjects.stream()
                .mapToInt(subject -> subject.getId()) // Получаем все id предметов
                .max()                          // Находим максимальный id
                .orElse(0) + 1;                 // Если список пуст, начинаем с 1

            // Создаем новый предмет
            Subject newSubject = new Subject(newId, name);

            // Добавляем предмет в базу данных
            schoolDatabase.addSubject(newSubject);

            // Обновляем таблицу с предметами
            updateTables();

            // Сообщаем об успешном добавлении
            JOptionPane.showMessageDialog(null, "Предмет успешно добавлен.");
        }
    }
    
    private void addClass() {
        // Создаем панель для ввода данных
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1, 2)); // Одна строка и два столбца

        JTextField nameField = new JTextField(); // Поле для ввода названия класса

        panel.add(new JLabel("Название класса:"));
        panel.add(nameField);

        // Отображаем окно ввода данных
        int option = JOptionPane.showConfirmDialog(
            null, panel, "Добавить класс", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            // Получаем введенное название класса
            String name = nameField.getText().trim();

            // Проверка, что название введено
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Ошибка: название класса не может быть пустым.");
                return;
            }

            // Генерация уникального id для нового класса
            int newId = schoolDatabase.schoolClasses.stream()
                .mapToInt(schoolClass -> schoolClass.getId()) // Получаем все id классов
                .max()                                  // Находим максимальный id
                .orElse(0) + 1;                         // Если список пуст, начинаем с 1

            // Создаем новый класс
            SchoolClass newClass = new SchoolClass(newId, name);

            // Добавляем класс в базу данных
            schoolDatabase.addSchoolClass(newClass);

            // Обновляем таблицу с классами
            updateTables();

            // Сообщаем об успешном добавлении
            JOptionPane.showMessageDialog(null, "Класс успешно добавлен.");
        }
    }

    
    private void deleteStudent(int selectedRow) {
        // Получаем имя студента из таблицы
        String studentName = studentNclassTable.getValueAt(selectedRow, 0).toString();
        
        // Находим студента по имени
        Student selectedStudent = schoolDatabase.students.stream()
            .filter(student -> (student.getFirstName() + " " + student.getLastName()).equals(studentName))
            .findFirst()
            .orElse(null);
        
        if (selectedStudent == null) {
            JOptionPane.showMessageDialog(null, "Ошибка: студент не найден.");
            return;
        }

        // Запрос на удаление
        int confirm = JOptionPane.showConfirmDialog(null, 
            "Вы уверены, что хотите удалить студента " + studentName + "?", 
            "Подтверждение удаления", 
            JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            // Удаляем студента
            schoolDatabase.removeStudent(selectedStudent.getId());
            
            // Обновляем зависимости класса
            schoolDatabase.studentClassRelations.removeIf(rel -> rel.getStudentId() == selectedStudent.getId());

            // Обновляем таблицу
            updateTables();
        }
    }

    
    private void deleteTeacher(int selectedRow) {
        // Получаем ID преподавателя из первого столбца таблицы
        Object value = teacherTable.getValueAt(selectedRow, 0);
        final int teacherId;
        
        // Проверка типа данных в ячейке и преобразование в целое число
        if (value instanceof Integer) {
            teacherId = (Integer) value;
        } else if (value instanceof String) {
            try {
                teacherId = Integer.parseInt((String) value);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Ошибка: ID преподавателя не является числом.");
                return;
            }
        } else {
            // Если значение в ячейке не является строкой или числом, показываем ошибку
            JOptionPane.showMessageDialog(null, "Ошибка: неверный формат данных в ячейке.");
            return;
        }

        // Находим преподавателя по ID
        Teacher selectedTeacher = schoolDatabase.teachers.stream()
            .filter(teacher -> teacher.getId() == teacherId)
            .findFirst()
            .orElse(null);
        
        if (selectedTeacher == null) {
            JOptionPane.showMessageDialog(null, "Ошибка: преподаватель не найден.");
            return;
        }

        // Запрос на удаление
        int confirm = JOptionPane.showConfirmDialog(null, 
            "Вы уверены, что хотите удалить преподавателя " + selectedTeacher.getFirstName() + " " + selectedTeacher.getLastName() + "?", 
            "Подтверждение удаления", 
            JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            // Удаляем преподавателя
            schoolDatabase.removeTeacher(selectedTeacher.getId());
            
            // Обновляем зависимости
            schoolDatabase.teacherClassRelations.removeIf(rel -> rel.getTeacherId() == selectedTeacher.getId());
            schoolDatabase.teacherSubjectRelations.removeIf(rel -> rel.getTeacherId() == selectedTeacher.getId());
            // Обновляем таблицу
            updateTables();
        }
    }

    
    private void deleteSubject(int selectedRow) {
        // Получаем ID предмета из первого столбца таблицы
        Object value = subjectTable.getValueAt(selectedRow, 0);
        final int subjectId;

        // Проверка типа данных в ячейке и преобразование в целое число
        if (value instanceof Integer) {
            subjectId = (Integer) value;
        } else if (value instanceof String) {
            try {
                subjectId = Integer.parseInt((String) value);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Ошибка: ID предмета не является числом.");
                return;
            }
        } else {
            // Если значение в ячейке не является строкой или числом, показываем ошибку
            JOptionPane.showMessageDialog(null, "Ошибка: неверный формат данных в ячейке.");
            return;
        }

        // Находим предмет по ID
        Subject selectedSubject = schoolDatabase.subjects.stream()
            .filter(subject -> subject.getId() == subjectId)
            .findFirst()
            .orElse(null);

        if (selectedSubject == null) {
            JOptionPane.showMessageDialog(null, "Ошибка: предмет не найден.");
            return;
        }

        // Запрос на удаление
        int confirm = JOptionPane.showConfirmDialog(null,
            "Вы уверены, что хотите удалить предмет " + selectedSubject.getName() + "?",
            "Подтверждение удаления",
            JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            // Удаляем предмет
            schoolDatabase.removeSubject(selectedSubject.getId());

            // Обновляем зависимости
            schoolDatabase.teacherSubjectRelations.removeIf(rel -> rel.getSubjectId() == selectedSubject.getId());

            // Обновляем таблицу
            updateTables();
        }
    }

    
    private void deleteClass(int selectedRow) {
        // Получаем ID класса из первого столбца таблицы
        Object value = classTable.getValueAt(selectedRow, 0);
        final int classId;

        // Проверка типа данных в ячейке и преобразование в целое число
        if (value instanceof Integer) {
            classId = (Integer) value;
        } else if (value instanceof String) {
            try {
                classId = Integer.parseInt((String) value);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Ошибка: ID класса не является числом.");
                return;
            }
        } else {
            // Если значение в ячейке не является строкой или числом, показываем ошибку
            JOptionPane.showMessageDialog(null, "Ошибка: неверный формат данных в ячейке.");
            return;
        }

        // Находим класс по ID
        SchoolClass selectedClass = schoolDatabase.schoolClasses.stream()
            .filter(schoolClass -> schoolClass.getId() == classId)
            .findFirst()
            .orElse(null);

        if (selectedClass == null) {
            JOptionPane.showMessageDialog(null, "Ошибка: класс не найден.");
            return;
        }

        // Запрос на удаление
        int confirm = JOptionPane.showConfirmDialog(null,
            "Вы уверены, что хотите удалить класс " + selectedClass.getName() + "?",
            "Подтверждение удаления",
            JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            // Удаляем класс
            schoolDatabase.removeSchoolClass(selectedClass.getId());

            // Обновляем зависимости
            schoolDatabase.studentClassRelations.removeIf(rel -> rel.getClassId() == selectedClass.getId());
            schoolDatabase.teacherClassRelations.removeIf(rel -> rel.getClassId() == selectedClass.getId());

            // Обновляем таблицу
            updateTables();
        }
    }

    
    
    
    private void editSubjectNTeacher(int selectedRow) {
        // Получаем ID выбранного предмета
    	int subjectId = Integer.parseInt(subjectTable.getValueAt(selectedRow, 0).toString());


        // Находим предмет по ID
        Subject selectedSubject = schoolDatabase.subjects.stream()
            .filter(subject -> subject.getId() == subjectId)
            .findFirst()
            .orElse(null);

        if (selectedSubject == null) {
            JOptionPane.showMessageDialog(null, "Ошибка: предмет не найден.");
            return;
        }

        // Создаем список преподавателей для выбора
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JLabel("Выберите преподавателей для предмета: " + selectedSubject.getName()), BorderLayout.NORTH);

        // Список всех преподавателей с объединенными именем и фамилией
        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (Teacher teacher : schoolDatabase.teachers) {
            listModel.addElement(teacher.getId() + " - " + teacher.getFirstName() + " " + teacher.getLastName());
        }

        JList<String> teacherList = new JList<>(listModel);
        teacherList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        panel.add(new JScrollPane(teacherList), BorderLayout.CENTER);

        // Определяем текущие зависимости предмета и преподавателей
        Set<Integer> currentRelations = schoolDatabase.teacherSubjectRelations.stream()
            .filter(rel -> rel.getSubjectId() == subjectId)
            .map(rel -> rel.getTeacherId())
            .collect(Collectors.toSet());

        // Автоматически выделяем преподавателей с текущими зависимостями
        for (int i = 0; i < listModel.size(); i++) {
            int teacherId = Integer.parseInt(listModel.get(i).split(" - ")[0]);
            if (currentRelations.contains(teacherId)) {
                teacherList.addSelectionInterval(i, i);
            }
        }

        int option = JOptionPane.showConfirmDialog(null, panel, "Редактирование зависимостей", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            // Получаем выбранных преподавателей
            List<Integer> selectedTeacherIds = teacherList.getSelectedValuesList().stream()
                .map(item -> Integer.parseInt(item.split(" - ")[0]))
                .collect(Collectors.toList());

            // Добавляем новые зависимости
            for (int teacherId : selectedTeacherIds) {
                if (!currentRelations.contains(teacherId)) {
                    schoolDatabase.addTeacherSubjectRelation(teacherId, subjectId);
                }
            }

            // Удаляем старые зависимости
            for (int teacherId : currentRelations) {
                if (!selectedTeacherIds.contains(teacherId)) {
                    schoolDatabase.teacherSubjectRelations.removeIf(rel ->
                        rel.getSubjectId() == subjectId && rel.getTeacherId() == teacherId);
                }
            }

            // Обновляем таблицу
            updateTables();
        }
    }
    
    private void editTeacherNClass(int selectedRow) {
        // Получаем ID выбранного учителя
        int teacherId = Integer.parseInt(teacherTable.getValueAt(selectedRow, 0).toString());

        // Находим учителя по ID
        Teacher selectedTeacher = schoolDatabase.teachers.stream()
            .filter(teacher -> teacher.getId() == teacherId)
            .findFirst()
            .orElse(null);

        if (selectedTeacher == null) {
            JOptionPane.showMessageDialog(null, "Ошибка: преподаватель не найден.");
            return;
        }

        // Создаем список классов для выбора
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JLabel("Выберите классы для преподавателя: " + selectedTeacher.getFirstName() + " " + selectedTeacher.getLastName()), BorderLayout.NORTH);

        // Список всех классов
        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (SchoolClass schoolClass : schoolDatabase.schoolClasses) {
            listModel.addElement(schoolClass.getId() + " - " + schoolClass.getName());
        }

        JList<String> classList = new JList<>(listModel);
        classList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        panel.add(new JScrollPane(classList), BorderLayout.CENTER);

        // Определяем текущие зависимости учителя и классов
        Set<Integer> currentRelations = schoolDatabase.teacherClassRelations.stream()
            .filter(rel -> rel.getTeacherId() == teacherId)
            .map(rel -> rel.getClassId())
            .collect(Collectors.toSet());

        // Автоматически выделяем классы с текущими зависимостями
        for (int i = 0; i < listModel.size(); i++) {
            int classId = Integer.parseInt(listModel.get(i).split(" - ")[0]);
            if (currentRelations.contains(classId)) {
                classList.addSelectionInterval(i, i);
            }
        }

        int option = JOptionPane.showConfirmDialog(null, panel, "Редактирование зависимостей", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            // Получаем выбранные классы
            List<Integer> selectedClassIds = new ArrayList<>();
            for (String item : classList.getSelectedValuesList()) {
                selectedClassIds.add(Integer.parseInt(item.split(" - ")[0]));
            }

            // Добавляем новые зависимости
            for (int classId : selectedClassIds) {
                if (!currentRelations.contains(classId)) {
                    schoolDatabase.addTeacherClassRelation(teacherId, classId);
                }
            }

            // Удаляем старые зависимости
            for (int classId : currentRelations) {
                if (!selectedClassIds.contains(classId)) {
                    schoolDatabase.removeTeacherClassRelation(teacherId, classId);
                }
            }

            // Обновляем таблицу
            updateTables();
        }
    }


    private void editStudentNClass(int selectedRow) {
        // Получаем имя студента из первой колонки таблицы
        String studentName = studentNclassTable.getValueAt(selectedRow, 0).toString();

        // Находим студента по имени (если база данных позволяет это)
        Student selectedStudent = schoolDatabase.students.stream()
            .filter(student -> (student.getFirstName() + " " + student.getLastName()).equals(studentName))
            .findFirst()
            .orElse(null);

        if (selectedStudent == null) {
            JOptionPane.showMessageDialog(null, "Ошибка: студент не найден.");
            return;
        }

        // Создаем панель для редактирования данных
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10)); // Ровные отступы
        panel.add(new JLabel("Имя:"));
        JTextField firstNameField = new JTextField(selectedStudent.getFirstName());
        panel.add(firstNameField);

        panel.add(new JLabel("Фамилия:"));
        JTextField lastNameField = new JTextField(selectedStudent.getLastName());
        panel.add(lastNameField);

        panel.add(new JLabel("Выберите класс:"));

        // Список классов для выбора
        JComboBox<String> classComboBox = new JComboBox<>();
        for (SchoolClass schoolClass : schoolDatabase.schoolClasses) {
            classComboBox.addItem(schoolClass.getId() + " - " + schoolClass.getName());
        }
        panel.add(classComboBox);

        panel.add(new JLabel("Успеваемость:"));
        JTextField gradeField = new JTextField(String.valueOf(selectedStudent.getGrade()));
        panel.add(gradeField);

        // Определяем текущий класс студента
        int currentClassId = schoolDatabase.studentClassRelations.stream()
            .filter(rel -> rel.getStudentId() == selectedStudent.getId())
            .map(rel -> rel.getClassId())
            .findFirst()
            .orElse(-1);

        if (currentClassId != -1) {
            for (int i = 0; i < classComboBox.getItemCount(); i++) {
                int classId = Integer.parseInt(classComboBox.getItemAt(i).split(" - ")[0].trim());
                if (classId == currentClassId) {
                    classComboBox.setSelectedIndex(i);
                    break;
                }
            }
        }

        int option = JOptionPane.showConfirmDialog(null, panel, "Редактирование данных студента", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            // Получаем введенные данные
            String newFirstName = firstNameField.getText();
            String newLastName = lastNameField.getText();
            int selectedClassId = Integer.parseInt(classComboBox.getSelectedItem().toString().split(" - ")[0].trim());
            double grade = Double.parseDouble(gradeField.getText());

            // Обновляем данные студента
            schoolDatabase.editStudent(selectedStudent.getId(), newFirstName, newLastName, grade);

            // Обновляем зависимости класса
            if (currentClassId != selectedClassId) {
                if (currentClassId != -1) {
                    schoolDatabase.removeStudentClassRelation(selectedStudent.getId(), currentClassId);
                }
                schoolDatabase.addStudentClassRelation(selectedStudent.getId(), selectedClassId);
            }

            // Обновляем таблицу
            updateTables();
        }
    }


    
    private void editTeacher(int selectedRow) {
        // Проверяем, что выбрана строка
        if (selectedRow == -1 || teacherTable.getColumnCount() < 2) {
            JOptionPane.showMessageDialog(null, "Ошибка: Недостаточно данных для редактирования.");
            return;
        }

        try {
            // Получаем ID преподавателя из таблицы (предположим, что он в первом столбце)
            int teacherId = Integer.parseInt(teacherTable.getValueAt(selectedRow, 0).toString()); // ID преподавателя в первом столбце

            // Получаем полное имя преподавателя из таблицы (во втором столбце)
            String teacherFullName = (String) teacherTable.getValueAt(selectedRow, 1); // Имя преподавателя во втором столбце

            // Разделяем полное имя на имя и фамилию
            String[] nameParts = teacherFullName.split(" ");
            String teacherFirstName = nameParts.length > 0 ? nameParts[0] : "";
            String teacherLastName = nameParts.length > 1 ? nameParts[1] : "";

            // Создаем панель для редактирования
            JPanel panel = new JPanel(new GridLayout(2, 2));
            JTextField firstNameField = new JTextField(teacherFirstName);
            JTextField lastNameField = new JTextField(teacherLastName);

            panel.add(new JLabel("Имя:"));
            panel.add(firstNameField);
            panel.add(new JLabel("Фамилия:"));
            panel.add(lastNameField);

            int option = JOptionPane.showConfirmDialog(null, panel, "Редактирование преподавателя", JOptionPane.OK_CANCEL_OPTION);

            if (option == JOptionPane.OK_OPTION) {
                String newFirstName = firstNameField.getText();
                String newLastName = lastNameField.getText();

                // Если имя или фамилия пустые, выводим ошибку
                if (!newFirstName.isEmpty() && !newLastName.isEmpty()) {
                    // Обновляем информацию о преподавателе в базе данных
                    schoolDatabase.editTeacher(teacherId, newFirstName, newLastName); // Используем ID для точного обновления
                    JOptionPane.showMessageDialog(null, "Преподаватель обновлен!");
                    // Обновляем таблицу
                    updateTables();
                } else {
                    JOptionPane.showMessageDialog(null, "Имя и фамилия не могут быть пустыми.");
                }
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Ошибка: Некорректный ID преподавателя.");
        }
    }

    
    private void editClass(int selectedRow) {
        // Получаем ID класса и его название из таблицы
        String classIdString = (String) classTable.getValueAt(selectedRow, 0);  // ID класса в первом столбце
        int classId = -1;
        try {
            classId = Integer.parseInt(classIdString);  // Преобразуем ID в число
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Ошибка: ID класса не является числом.");
            return;
        }

        String className = (String) classTable.getValueAt(selectedRow, 1);  // Название класса

        // Создаем панель для редактирования
        JPanel panel = new JPanel(new GridLayout(2, 2));
        JTextField nameField = new JTextField(className);

        panel.add(new JLabel("Название класса:"));
        panel.add(nameField);

        int option = JOptionPane.showConfirmDialog(null, panel, "Редактирование класса", JOptionPane.OK_CANCEL_OPTION);
        
        if (option == JOptionPane.OK_OPTION) {
            String newName = nameField.getText();
            
            // Если название класса пустое, выводим ошибку
            if (!newName.isEmpty()) {
                schoolDatabase.editSchoolClass(classId, newName);
                JOptionPane.showMessageDialog(null, "Класс обновлен!");
                // Обновляем таблицу
                updateTables();
            } else {
                JOptionPane.showMessageDialog(null, "Название класса не может быть пустым.");
            }
        }
    }

    
    private void editSubject(int selectedRow) {
        // Получаем значение из ячейки таблицы, которое, скорее всего, строка
        String subjectIdString = (String) subjectTable.getValueAt(selectedRow, 0);  // id в первом столбце
        
        // Преобразуем строку в int
        int subjectId = -1;
        try {
            subjectId = Integer.parseInt(subjectIdString);  // Преобразуем в число
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Ошибка: ID не является числом.");
            return;  // Если не удалось преобразовать, выходим
        }

        String subjectName = (String) subjectTable.getValueAt(selectedRow, 1);

        // Создаем панель для редактирования
        JPanel panel = new JPanel(new GridLayout(2, 2));
        JTextField nameField = new JTextField(subjectName);

        panel.add(new JLabel("Название предмета:"));
        panel.add(nameField);

        int option = JOptionPane.showConfirmDialog(null, panel, "Редактирование предмета", JOptionPane.OK_CANCEL_OPTION);
        
        if (option == JOptionPane.OK_OPTION) {
            String newName = nameField.getText();
            // Если имя не пустое, обновляем предмет
            if (!newName.isEmpty()) {
                schoolDatabase.editSubject(subjectId, newName);
                JOptionPane.showMessageDialog(null, "Предмет обновлен!");
                // Обновляем таблицу
                updateTables();
            } else {
                JOptionPane.showMessageDialog(null, "Имя не может быть пустым.");
            }
        }
    }
    
    
    

    private void updateTables() {
        updateStudentTable(schoolDatabase.students); // Передаем список в метод обновления таблицы студентов
        updateSubjectNteacherTable();
        updateTeacherNclassTable();
        updateTeacherTable();
        updateClassTable();
        updateSubjectTable();
        updateClassSelector();
    }

    private void updateClassSelector() {
        // Получаем все классы из базы данных
        List<SchoolClass> classes = schoolDatabase.schoolClasses;

        // Преобразуем список классов в массив строк для JComboBox
        String[] classNames = new String[classes.size() + 1];
        classNames[0] = "Все классы"; // Первый элемент — "Все классы"
        
        for (int i = 0; i < classes.size(); i++) {
            classNames[i + 1] = classes.get(i).getName(); // Заполняем остальные классы
        }

        // Обновляем JComboBox с новыми классами
        classSelector.setModel(new DefaultComboBoxModel<>(classNames));
    }


    private void updateStudentTable(List<Student> students) {
        // Создаем двумерный массив для данных студентов, размерность зависит от количества студентов
        String[][] studentData = new String[students.size()][3];
        
        // Перебираем студентов и заполняем таблицу
        for (int i = 0; i < students.size(); i++) {
            Student student = students.get(i);

            // Формируем ФИО студента
            studentData[i][0] = student.getFirstName() + " " + student.getLastName();

            // Ищем название класса для студента через связи в studentClassRelations
            String className = schoolDatabase.studentClassRelations.stream()
                .filter(relation -> relation.getStudentId() == student.getId())
                .map(relation -> {
                    // Ищем класс по ID, связанному с этим студентом
                    return schoolDatabase.schoolClasses.stream()
                            .filter(schoolClass -> schoolClass.getId() == relation.getClassId())
                            .findFirst()
                            .map(schoolClass -> schoolClass.getName())
                            .orElse("Не определено");
                })
                .findFirst()
                .orElse("Не определено");  // Если класс не найден, ставим "Не определено"
            
            studentData[i][1] = className;

            // Успеваемость студента. Если она определена (больше 0), выводим ее. Если нет, то "Не определено"
            String performance = (student.getGrade() > 0) ? String.valueOf(student.getGrade()) : "Не определено";
            studentData[i][2] = performance;
        }

        // Обновляем модель таблицы
        studentNclassTable.setModel(new DefaultTableModel(studentData, new String[]{"Фамилия", "Класс", "Успеваемость"}));
    }



    private void updateSubjectNteacherTable() {
        List<Subject> subjects = schoolDatabase.subjects;
        String[][] subjectData = new String[subjects.size()][2];
        for (int i = 0; i < subjects.size(); i++) {
            Subject subject = subjects.get(i);
            subjectData[i][0] = subject.getName();

            // Ищем всех преподавателей для данного предмета
            List<String> teacherNames = schoolDatabase.teacherSubjectRelations.stream()
                .filter(r -> r.getSubjectId() == subject.getId())  // Фильтруем по предмету
                .map(r -> r.getTeacherId())  // Получаем ID преподавателя
                .distinct()  // Убираем дублирующихся преподавателей
                .map(teacherId -> schoolDatabase.teachers.stream()
                    .filter(t -> t.getId() == teacherId)
                    .findFirst()
                    .map(t -> t.getFirstName() + " " + t.getLastName())
                    .orElse("Не назначен"))
                .collect(Collectors.toList());  // Собираем имена преподавателей в список

            // Объединяем все имена преподавателей в одну строку через запятую
            subjectData[i][1] = String.join(", ", teacherNames);
        }

        // Обновляем модель таблицы с новыми данными
        subjectNteacherTable.setModel(new DefaultTableModel(subjectData, new String[]{"Предмет", "Преподаватели"}));
    }

    private void updateTeacherNclassTable() {
        List<SchoolClass> schoolClasses = schoolDatabase.schoolClasses;
        List<Teacher> teachers = schoolDatabase.teachers;
        
        // Создаём список данных для таблицы
        List<String[]> classDataList = new ArrayList<>();
        
        // Проходим по каждому преподавателю
        for (Teacher teacher : teachers) {
            // Ищем все классы, которые ведет этот преподаватель
            List<String> classNames = schoolDatabase.teacherClassRelations.stream()
                .filter(r -> r.getTeacherId() == teacher.getId())  // Фильтруем по преподавателю
                .map(r -> r.getClassId())  // Получаем ID класса
                .distinct()  // Убираем дублирующиеся классы
                .map(classId -> schoolClasses.stream()
                    .filter(sc -> sc.getId() == classId)
                    .findFirst()
                    .map(sc -> sc.getName())
                    .orElse("Не определён"))
                .collect(Collectors.toList());  // Собираем имена классов в список

            // Объединяем все имена классов в одну строку через запятую
            String classes = String.join(", ", classNames);
            
            // Добавляем данные преподавателя и классов в список
            classDataList.add(new String[]{teacher.getFirstName() + " " + teacher.getLastName(), classes});
        }
        
        // Преобразуем список данных в массив для таблицы
        String[][] classData = classDataList.toArray(new String[0][0]);
        
        // Обновляем модель таблицы
        teacherNclassTable.setModel(new DefaultTableModel(classData, new String[]{"Преподаватель", "Классы"}));
    }


    private void updateTeacherTable() {
        String[][] teacherData = new String[schoolDatabase.teachers.size()][2];
        for (int i = 0; i < schoolDatabase.teachers.size(); i++) {
            Teacher teacher = schoolDatabase.teachers.get(i);
            teacherData[i][0] = String.valueOf(teacher.getId());  // ID учителя
            teacherData[i][1] = teacher.getFirstName() + " " + teacher.getLastName();  // Имя и фамилия учителя
        }
        teacherTable.setModel(new DefaultTableModel(teacherData, new String[]{"ID", "Имя и Фамилия"}));
    }

    private void updateClassTable() {
        String[][] classData = new String[schoolDatabase.schoolClasses.size()][2];
        for (int i = 0; i < schoolDatabase.schoolClasses.size(); i++) {
            SchoolClass schoolClass = schoolDatabase.schoolClasses.get(i);
            classData[i][0] = String.valueOf(schoolClass.getId());  // ID класса
            classData[i][1] = schoolClass.getName();  // Название класса
        }
        classTable.setModel(new DefaultTableModel(classData, new String[]{"ID", "Класс"}));
    }

    private void updateSubjectTable() {
        String[][] subjectData = new String[schoolDatabase.subjects.size()][2];
        for (int i = 0; i < schoolDatabase.subjects.size(); i++) {
            Subject subject = schoolDatabase.subjects.get(i);
            subjectData[i][0] = String.valueOf(subject.getId());  // ID предмета
            subjectData[i][1] = subject.getName();  // Название предмета
        }
        subjectTable.setModel(new DefaultTableModel(subjectData, new String[]{"ID", "Предмет"}));
    }
    
    private void filterStudentsByClass(String selectedClass) {
        // Фильтруем студентов по классу
        List<Student> filteredStudents = new ArrayList<>();
        
        if (selectedClass.equals("Все классы")) {
            filteredStudents = schoolDatabase.students;  // Показываем всех студентов, если выбран "Все классы"
        } else {
            // Ищем id класса, соответствующее выбранному названию
            int selectedClassId = schoolDatabase.schoolClasses.stream()
                    .filter(schoolClass -> schoolClass.getName().equals(selectedClass))
                    .findFirst()
                    .map(schoolClass -> schoolClass.getId())
                    .orElse(-1); // Если класс не найден, используем -1 как индикатор ошибки

            // Фильтруем студентов, используя связи между студентами и классами
            for (Student student : schoolDatabase.students) {
                // Проверяем, есть ли связь студента с этим классом
                boolean isInClass = schoolDatabase.studentClassRelations.stream()
                    .anyMatch(relation -> relation.getStudentId() == student.getId() && relation.getClassId() == selectedClassId);
                
                if (isInClass) {
                    filteredStudents.add(student);
                }
            }
        }
        
        // Обновляем таблицу студентов с отфильтрованными данными
        updateStudentTable(filteredStudents);
    }


    private void generateAndSavePerformanceReport() {
        // Пороговые значения по умолчанию
        double defaultExcellentGradeThreshold = 4.5; // Порог для отличников
        double defaultGoodGradeThreshold = 3.7;     // Порог для хорошистов
        double defaultAverageGradeThreshold = 3.0;   // Порог для троечников
        double defaultFailGradeThreshold = 2.0;      // Порог для двоечников

        // Панель для ввода значений порогов
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        
        // Текстовые поля для порогов
        JTextField excellentField = new JTextField(String.valueOf(defaultExcellentGradeThreshold), 10);
        JTextField goodField = new JTextField(String.valueOf(defaultGoodGradeThreshold), 10);
        JTextField averageField = new JTextField(String.valueOf(defaultAverageGradeThreshold), 10);
        JTextField failField = new JTextField(String.valueOf(defaultFailGradeThreshold), 10);

        // Добавляем компоненты на панель
        panel.add(new JLabel("Порог для отличников (оценка >=):"));
        panel.add(excellentField);
        panel.add(new JLabel("Порог для хорошистов (оценка >=):"));
        panel.add(goodField);
        panel.add(new JLabel("Порог для троечников (оценка >=):"));
        panel.add(averageField);
        panel.add(new JLabel("Порог для двоечников (оценка <=):"));
        panel.add(failField);

        // Окно с панелью ввода
        int option = JOptionPane.showConfirmDialog(null, panel, "Введите пороги для категорий", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            // Чтение значений из текстовых полей
            double excellentGradeThreshold = parseDouble(excellentField.getText(), defaultExcellentGradeThreshold);
            double goodGradeThreshold = parseDouble(goodField.getText(), defaultGoodGradeThreshold);
            double averageGradeThreshold = parseDouble(averageField.getText(), defaultAverageGradeThreshold);
            double failGradeThreshold = parseDouble(failField.getText(), defaultFailGradeThreshold);

            // Генерация отчета
            String report = PerformanceReportGenerator.generateReport(schoolDatabase.students, excellentGradeThreshold, goodGradeThreshold, failGradeThreshold, averageGradeThreshold);

            // Сохранение отчета в файл
            PerformanceReportGenerator.saveReportToFile(report);
        }
    }

    // Метод для преобразования строки в число с использованием значения по умолчанию
    private double parseDouble(String input, double defaultValue) {
        try {
            return Double.parseDouble(input);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }


    
    
    
  
    public static void main(String[] args) {
        SchoolApp app = new SchoolApp();
        app.setVisible(true);
    }
}
