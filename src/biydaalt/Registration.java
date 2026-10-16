package biydaalt;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import dataStructures.ArrayLinearList;
import dataStructures.Chain;

public class Registration {

    private static final int MAJOR_CODE_START = 5;
    private static final int MAJOR_CODE_END = 7;
    private static final int MIN_SCORE = 0;
    private static final int MAX_SCORE = 100;
    private static final int DROP_F_COUNT = 4;

    public ArrayLinearList studentList = new ArrayLinearList();
    public ArrayLinearList subjectList = new ArrayLinearList();
    public ArrayLinearList majorList = new ArrayLinearList();

    private String examsFile;
    private boolean hasUnsavedChanges = false;

    private static class Entry {

        final String studentCode;
        final int score;

        Entry(String studentCode, int score) {
            this.studentCode = studentCode;
            this.score = score;
        }
    }

    static char letterGrade(int score) {
        if (score >= 90) {
            return 'A';
        }
        if (score >= 80) {
            return 'B';
        }
        if (score >= 70) {
            return 'C';
        }
        if (score >= 60) {
            return 'D';
        }
        return 'F';
    }

    static float gradePoints(int score) {
        switch (letterGrade(score)) {
            case 'A':
                return 4.0f;
            case 'B':
                return 3.0f;
            case 'C':
                return 2.0f;
            case 'D':
                return 1.0f;
            default:
                return 0.0f;
        }
    }

    static float computeGPA(Student student) {
        float weightedSum = 0;
        float totalCredits = 0;
        for (int i = 0; i < student.lessons.size(); i++) {
            Lesson lesson = lessonAt(student, i);
            weightedSum += gradePoints(lesson.score) * lesson.learned.credit;
            totalCredits += lesson.learned.credit;
        }
        return totalCredits == 0 ? 0 : weightedSum / totalCredits;
    }

    static int countFailed(Student student) {
        int count = 0;
        for (int i = 0; i < student.lessons.size(); i++) {
            if (letterGrade(lessonAt(student, i).score) == 'F') {
                count++;
            }
        }
        return count;
    }

    static String majorCodeOf(String studentCode) {
        return studentCode.substring(MAJOR_CODE_START, MAJOR_CODE_END);
    }

    static Lesson lessonAt(Student student, int index) {
        return (Lesson) student.lessons.get(index);
    }

    Subject subjectAt(int index) {
        return (Subject) subjectList.get(index);
    }

    Major majorAt(int index) {
        return (Major) majorList.get(index);
    }

    Student studentAt(int index) {
        return (Student) studentList.get(index);
    }

    Subject findSubject(String code) {
        for (int i = 0; i < subjectList.size(); i++) {
            if (subjectAt(i).code.equals(code)) {
                return subjectAt(i);
            }
        }
        return null;
    }

    Major findMajor(String code) {
        for (int i = 0; i < majorList.size(); i++) {
            if (majorAt(i).code.equals(code)) {
                return majorAt(i);
            }
        }
        return null;
    }

    Student findStudent(String code) {
        for (int i = 0; i < studentList.size(); i++) {
            if (studentAt(i).code.equals(code)) {
                return studentAt(i);
            }
        }
        return null;
    }

    static int findLessonIndex(Student student, String subjectCode) {
        for (int i = 0; i < student.lessons.size(); i++) {
            if (lessonAt(student, i).learned.code.equals(subjectCode)) {
                return i;
            }
        }
        return -1;
    }

    static ArrayLinearList readLines(String fileName) {
        ArrayLinearList lines = new ArrayLinearList();
        try (BufferedReader input = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = input.readLine()) != null) {
                lines.add(lines.size(), line);
            }
        } catch (java.io.FileNotFoundException e) {
            System.out.println("Файл олдсонгүй: " + fileName);
            System.exit(1);
        } catch (IOException e) {
            System.out.println("Унших алдаа: " + e.getMessage());
            System.exit(1);
        }
        return lines;
    }

    static void reportSkipped(String fileName, int lineNo, String reason, String line) {
        System.out.println(fileName + " " + lineNo + "-р мөр: " + reason + ", алгаслаа: " + line);
    }

    static ArrayLinearList readSubjects(String fileName) {
        ArrayLinearList list = new ArrayLinearList();
        ArrayLinearList lines = readLines(fileName);
        for (int i = 0; i < lines.size(); i++) {
            String line = (String) lines.get(i);
            int lineNo = i + 1;
            if (line.trim().isEmpty()) {
                continue;
            }
            String[] values = line.split("/", -1);
            if (values.length != 3) {
                reportSkipped("Subjects.txt", lineNo, "талбарын тоо буруу", line);
                continue;
            }
            Subject subject = new Subject();
            subject.code = values[0].trim();
            subject.name = values[1].trim();
            try {
                subject.credit = Float.parseFloat(values[2].trim());
            } catch (NumberFormatException e) {
                reportSkipped("Subjects.txt", lineNo, "кредит тоо биш", line);
                continue;
            }
            list.add(list.size(), subject);
        }
        return list;
    }

    static ArrayLinearList readMajors(String fileName) {
        ArrayLinearList list = new ArrayLinearList();
        ArrayLinearList lines = readLines(fileName);
        for (int i = 0; i < lines.size(); i++) {
            String line = (String) lines.get(i);
            int lineNo = i + 1;
            if (line.trim().isEmpty()) {
                continue;
            }
            String[] values = line.split("/", -1);
            if (values.length != 2) {
                reportSkipped("Professions.txt", lineNo, "талбарын тоо буруу", line);
                continue;
            }
            Major major = new Major();
            major.code = values[0].trim();
            major.name = values[1].trim();
            list.add(list.size(), major);
        }
        return list;
    }

    void readExams(String fileName) {
        ArrayLinearList lines = readLines(fileName);
        for (int i = 0; i < lines.size(); i++) {
            String line = (String) lines.get(i);
            int lineNo = i + 1;
            if (line.trim().isEmpty()) {
                continue;
            }
            String[] values = line.split("/", -1);
            if (values.length != 3) {
                reportSkipped("Exams.txt", lineNo, "талбарын тоо буруу", line);
                continue;
            }
            int score;
            try {
                score = Integer.parseInt(values[2].trim());
            } catch (NumberFormatException e) {
                reportSkipped("Exams.txt", lineNo, "оноо тоо биш", line);
                continue;
            }
            String error = addGrade(values[0].trim(), values[1].trim(), score);
            if (error != null) {
                reportSkipped("Exams.txt", lineNo, error, line);
            }
        }
    }

    String addGrade(String studentCode, String subjectCode, int score) {
        if (studentCode.length() < MAJOR_CODE_END) {
            return "оюутны код хэт богино";
        }
        if (findMajor(majorCodeOf(studentCode)) == null) {
            return "мэргэжил " + majorCodeOf(studentCode) + " Professions.txt-д байхгүй";
        }
        Subject subject = findSubject(subjectCode);
        if (subject == null) {
            return "хичээл " + subjectCode + " Subjects.txt-д байхгүй";
        }
        if (score < MIN_SCORE || score > MAX_SCORE) {
            return "оноо " + score + " нь " + MIN_SCORE + "-" + MAX_SCORE + " хооронд биш";
        }

        Student student = findStudent(studentCode);
        if (student == null) {
            student = new Student();
            student.code = studentCode;
            student.lessons = new Chain();
            studentList.add(studentList.size(), student);
        } else if (findLessonIndex(student, subjectCode) >= 0) {
            return "оюутан " + subjectCode + " хичээлийн дүнтэй байна";
        }

        Lesson lesson = new Lesson();
        lesson.learned = subject;
        lesson.score = score;
        student.lessons.add(student.lessons.size(), lesson);
        student.GPA = computeGPA(student);
        return null;
    }

    void showSubjects() {
        System.out.println("Код    Нэр                   Кредит");
        for (int i = 0; i < subjectList.size(); i++) {
            Subject subject = subjectAt(i);
            System.out.printf("%-6s %-21s %.1f%n", subject.code, subject.name, subject.credit);
        }
    }

    void showMajors() {
        System.out.println("Код   Нэр");
        for (int i = 0; i < majorList.size(); i++) {
            Major major = majorAt(i);
            System.out.printf("%-5s %s%n", major.code, major.name);
        }
    }

    void showGPAs() {
        if (studentList.isEmpty()) {
            System.out.println("Оюутан алга.");
            return;
        }
        float total = 0;
        for (int i = 0; i < studentList.size(); i++) {
            Student student = studentAt(i);
            student.GPA = computeGPA(student);
            total += student.GPA;
            System.out.printf("%-12s Голч %.2f%n", student.code, student.GPA);
        }
        System.out.printf("Бүх оюутны голч дүнгийн дундаж: %.2f%n", total / studentList.size());
    }

    void showDropped() {
        boolean found = false;
        for (int i = 0; i < studentList.size(); i++) {
            Student student = studentAt(i);
            int failed = countFailed(student);
            if (failed >= DROP_F_COUNT) {
                System.out.printf("%-12s %d хичээлд F%n", student.code, failed);
                found = true;
            }
        }
        if (!found) {
            System.out.println(DROP_F_COUNT + " ба түүнээс дээш F авсан оюутан алга.");
        }
    }

    private static void insertByScoreDescending(ArrayLinearList entries, Entry entry) {
        int position = 0;
        while (position < entries.size() && ((Entry) entries.get(position)).score >= entry.score) {
            position++;
        }
        entries.add(position, entry);
    }

    ArrayLinearList entriesForSubject(Subject subject) {
        ArrayLinearList entries = new ArrayLinearList();
        for (int i = 0; i < studentList.size(); i++) {
            Student student = studentAt(i);
            int lessonIndex = findLessonIndex(student, subject.code);
            if (lessonIndex >= 0) {
                int score = lessonAt(student, lessonIndex).score;
                insertByScoreDescending(entries, new Entry(student.code, score));
            }
        }
        return entries;
    }

    void showBySubject() {
        for (int i = 0; i < subjectList.size(); i++) {
            Subject subject = subjectAt(i);
            ArrayLinearList entries = entriesForSubject(subject);
            System.out.println("== " + subject.code + " " + subject.name
                    + " (" + entries.size() + " оюутан)");
            for (int j = 0; j < entries.size(); j++) {
                Entry entry = (Entry) entries.get(j);
                System.out.printf("   %-12s %3d  %c%n",
                        entry.studentCode, entry.score, letterGrade(entry.score));
            }
        }
    }

    void showByMajor() {
        for (int i = 0; i < majorList.size(); i++) {
            Major major = majorAt(i);
            System.out.println("== " + major.code + " " + major.name);
            for (int j = 0; j < studentList.size(); j++) {
                Student student = studentAt(j);
                if (!majorCodeOf(student.code).equals(major.code)) {
                    continue;
                }
                System.out.printf("   %-12s Голч %.2f%n", student.code, computeGPA(student));
                for (int k = 0; k < student.lessons.size(); k++) {
                    Lesson lesson = lessonAt(student, k);
                    System.out.printf("      %-4s %3d  %c%n",
                            lesson.learned.code, lesson.score, letterGrade(lesson.score));
                }
            }
        }
    }

    static String ask(Scanner in, String prompt) {
        System.out.print(prompt);
        return in.nextLine().trim();
    }

    void addGradeFromInput(Scanner in) {
        String studentCode = ask(in, "Оюутны код: ");
        String subjectCode = ask(in, "Хичээлийн код: ");
        int score;
        try {
            score = Integer.parseInt(ask(in, "Оноо (" + MIN_SCORE + "-" + MAX_SCORE + "): "));
        } catch (NumberFormatException e) {
            System.out.println("Оноо бүхэл тоо байх ёстой.");
            return;
        }
        String error = addGrade(studentCode, subjectCode, score);
        if (error != null) {
            System.out.println("Нэмээгүй: " + error);
            return;
        }
        hasUnsavedChanges = true;
        System.out.println("Нэмлээ.");
    }

    void deleteGradeFromInput(Scanner in) {
        String studentCode = ask(in, "Оюутны код: ");
        String subjectCode = ask(in, "Хичээлийн код: ");
        Student student = findStudent(studentCode);
        if (student == null) {
            System.out.println("Оюутан олдсонгүй.");
            return;
        }
        int lessonIndex = findLessonIndex(student, subjectCode);
        if (lessonIndex < 0) {
            System.out.println("Оюутанд энэ хичээлийн дүн байхгүй.");
            return;
        }

        student.lessons.remove(lessonIndex);
        if (student.lessons.isEmpty()) {
            studentList.remove(studentList.indexOf(student));
        } else {
            student.GPA = computeGPA(student);
        }
        hasUnsavedChanges = true;
        System.out.println("Устгалаа.");
    }

    void save() {
        try (PrintWriter out = new PrintWriter(new FileWriter(examsFile))) {
            for (int i = 0; i < studentList.size(); i++) {
                Student student = studentAt(i);
                for (int j = 0; j < student.lessons.size(); j++) {
                    Lesson lesson = lessonAt(student, j);
                    out.println(student.code + "/" + lesson.learned.code + "/" + lesson.score);
                }
            }
            hasUnsavedChanges = false;
            System.out.println("Хадгаллаа: " + examsFile);
        } catch (IOException e) {
            System.out.println("Хадгалж чадсангүй: " + e.getMessage());
        }
    }

    static void printMenu() {
        System.out.println();
        System.out.println("1. Хичээлийн жагсаалт харуулах");
        System.out.println("2. Мэргэжлийн жагсаалт харуулах");
        System.out.println("3. Оюутан бүрийн голч дүн ба дундаж харуулах");
        System.out.println("4. Хасагдах оюутнууд (" + DROP_F_COUNT + "+ F)");
        System.out.println("5. Хичээл бүрээр оюутнуудын дүн (оноогоор буурах)");
        System.out.println("6. Мэргэжил бүрээр оюутнуудын дүн");
        System.out.println("7. Дүн нэмэх");
        System.out.println("8. Дүн устгах");
        System.out.println("9. Exams.txt-д хадгалах");
        System.out.println("0. Гарах");
        System.out.print("Сонгох: ");
    }

    void confirmSaveBeforeExit(Scanner in) {
        if (!hasUnsavedChanges) {
            return;
        }
        String answer = ask(in, "Хадгалаагүй өөрчлөлт байна. Гарахын өмнө хадгалах уу? (y/n): ");
        if (answer.equalsIgnoreCase("y")) {
            save();
        }
    }

    void loadData(String dir) {
        examsFile = dir + "Exams.txt";
        subjectList = readSubjects(dir + "Subjects.txt");
        majorList = readMajors(dir + "Professions.txt");
        readExams(examsFile);
        System.out.println("Уншлаа: " + subjectList.size() + " хичээл, "
                + majorList.size() + " мэргэжил, " + studentList.size() + " оюутан.");
    }

    void runMenu() {
        Scanner in = new Scanner(System.in);
        while (true) {
            printMenu();
            if (!in.hasNextLine()) {
                return;
            }
            String choice = in.nextLine().trim();
            System.out.println();
            switch (choice) {
                case "1":
                    showSubjects();
                    break;
                case "2":
                    showMajors();
                    break;
                case "3":
                    showGPAs();
                    break;
                case "4":
                    showDropped();
                    break;
                case "5":
                    showBySubject();
                    break;
                case "6":
                    showByMajor();
                    break;
                case "7":
                    addGradeFromInput(in);
                    break;
                case "8":
                    deleteGradeFromInput(in);
                    break;
                case "9":
                    save();
                    break;
                case "0":
                    confirmSaveBeforeExit(in);
                    return;
                default:
                    System.out.println("Буруу сонголт.");
            }
        }
    }

    static String findDataDir(String[] args) {
        if (args.length > 0) {
            return args[0].endsWith("/") ? args[0] : args[0] + "/";
        }
        if (new File("biydaalt/Subjects.txt").exists()) {
            return "biydaalt/";
        }
        return "src/biydaalt/";
    }

    public static void main(String[] args) {
        Registration registration = new Registration();
        registration.loadData(findDataDir(args));
        registration.runMenu();
    }
}
