****# Student Grade Registration – Notes

## 1. Big picture

```
Subjects.txt ──► readSubjects ──► subjectList (ArrayLinearList of Subject)
Professions.txt ► readMajors  ──► majorList   (ArrayLinearList of Major)
Exams.txt ──► readExams ──► addGrade ──► studentList (ArrayLinearList of Student)
                                              └─ Student.lessons (Chain of Lesson)
                                                    └─ Lesson.learned ──► Subject in subjectList
                         ▲
                         │ same addGrade is used by menu action 7
menu (runMenu) ──► reports (1–6) / add (7) / delete (8) / save (9) ──► Exams.txt
```

Start-up (`main`): `findDataDir` → `loadData` (read the 3 files, build the 3 lists) → `runMenu` (loop until `0`).

### Data structures (only lecture structures, no java.util collections)

| Data | Structure | Why |
|---|---|---|
| studentList, subjectList, majorList | `ArrayLinearList` | main lists; fast `get(i)` by index |
| `Student.lessons` | `Chain` (linked list) | each student's own variable-length list |
| sorted report rows | `ArrayLinearList` of `Entry` | built by insertion sort |

`Lesson.learned` points at the *same* `Subject` object stored in `subjectList` (no copy), so credit is always read from the one place.

Every list is searched linearly (`findStudent`, `findSubject`, `findMajor`, `findLessonIndex`).

### Classes
- `Subject` (code, name, credit), `Major` (code, name)
- `Student` (code, GPA, lessons), `Lesson` (learned, score)
- `Registration` – lists, file reading, reports, menu
- `Registration.Entry` – private (studentCode, score) row used only for sorting

## 2. Formulas and rules

**Score → letter → points**

| Score | Letter | Points |
|---|---|---|
| 90–100 | A | 4.0 |
| 80–89 | B | 3.0 |
| 70–79 | C | 2.0 |
| 60–69 | D | 1.0 |
| 0–59 | F | 0.0 |

Implemented in `letterGrade` (if-chain from the top) and `gradePoints` (switch on the letter). `Exams.txt` stores the raw score; the letter is derived on read.

**GPA (credit weighted)** – `computeGPA`

```
GPA = Σ(points × credit) / Σ(credit)      (0 if the student has no credits)
```
Example: A in a 3-credit subject and C in a 4-credit one → (4×3 + 2×4) / (3+4) = 20/7 = 2.86.

**Average GPA** (action 3) = Σ GPA of all students / number of students (simple mean of the per-student GPAs, not credit weighted).

**Major code** – `majorCodeOf`: `studentCode.substring(5, 7)`. `B160930001` → `30`.

**Dropped student** (action 4): number of F lessons ≥ 4 (`DROP_F_COUNT`), counted by `countFailed`.

## 3. Algorithms

| Where | Algorithm | Cost |
|---|---|---|
| `find*` methods | linear search over the list | O(n) |
| `entriesForSubject` / `insertByScoreDescending` | **insertion sort**: for each new row walk from the front until a row with a smaller score, insert there. Equal scores keep file order (`>=` so the new row goes after them) | O(n²) worst case for n students of one subject |
| `showBySubject` | for each subject: collect entries of students that took it, sorted | O(subjects × students × lessons) |
| `showByMajor` | for each major, scan students and print those whose major code matches | O(majors × students) |
| `addGrade` | validate → find/create student → reject duplicate → append lesson to the end of the Chain → recompute GPA | O(n) |
| delete (action 8) | find student → find lesson index → `Chain.remove(index)`; if that was the last lesson, remove the student from `studentList` too (a student with no grades cannot be restored from Exams.txt); else recompute GPA | O(n) |
| `save` | write every lesson of every student as `student/subject/score`, one line each | O(total lessons) |

Chain `get(i)` is O(n) while `ArrayLinearList.get(i)` is O(1); loops over a student's lessons are therefore O(lessons²) – fine for a few lessons per student.

## 4. Validation and logging

There is no log file. "Logging" is console messages printed while loading; a bad line never stops the program, it is reported and **skipped**.

Format:
```
<File> <line number>-р мөр: <reason>, алгаслаа: <original line>
```

| Check | Where | Message (reason) |
|---|---|---|
| wrong number of fields (`split("/", -1)` keeps empty trailing fields, so `a/b/` counts as 3) | all three readers | талбарын тоо буруу |
| credit not a number | Subjects | кредит тоо биш |
| score not a number | Exams | оноо тоо биш |
| student code shorter than 7 chars | `addGrade` | оюутны код хэт богино |
| major code not in Professions.txt | `addGrade` | мэргэжил … Professions.txt-д байхгүй |
| subject code not in Subjects.txt | `addGrade` | хичээл … Subjects.txt-д байхгүй |
| score outside 0–100 | `addGrade` | оноо … нь 0-100 хооронд биш |
| student already has that subject | `addGrade` | оюутан … хичээлийн дүнтэй байна |

Other messages:
- File missing / unreadable → message and `System.exit(1)` (`readLines`).
- After loading: `Уншлаа: N хичээл, N мэргэжил, N оюутан.` – counts of what was accepted.
- Interactive add (7) reuses `addGrade`, so typed input gets exactly the same checks as file lines; a rejected add prints `Нэмээгүй: <reason>`.
- Non-numeric score typed at the prompt → `Оноо бүхэл тоо байх ёстой.`

Order of checks in `addGrade`: code length → major → subject → score range → duplicate. The student object is created only after the first four pass, so a rejected line never leaves an empty student behind.

## 5. Saving and unsaved changes

- Add/delete only change memory and set `hasUnsavedChanges`.
- Action 9 rewrites `Exams.txt` completely from memory. **The skipped (invalid) lines are not written back**, so they disappear from the file after a save. Keep a copy of the original `Exams.txt` if the invalid examples are needed for demonstration.
- Choosing `0` with unsaved changes asks `y/n` before exit.

## 6. Test data (prepared files)

- Subjects: S1…S8 (credits 1–4) – 8 subjects.
- Professions: 30 / 50 / 75 – 3 majors.
- Exams: 16 valid students, 3 of them with ≥ 4 F (`B160930003`, `B160950007`, `B160975013`).
- 8 invalid lines at the end of `Exams.txt`, one per check: unknown subject, score 105, score −5, unknown major 99, too few fields, too many fields, non-numeric score, duplicate student+subject.

## 7. Running

```
just            # from dsa/: compile into bin/ and run
java -cp ../bin biydaalt.Registration [dataDir]     # from dsa/src
```
Without `dataDir`, the program uses `biydaalt/` (run from `src`) or `src/biydaalt/` (run from the project root, e.g. Eclipse).
