public class Subject {
	public String code;
	public String name;
	public float credit;
}

public class Major {
public String code;
public String name;
}

public class Student {      // Оюутан
    public String code;     // код
    public float GPA;       // голч дүн (тооцоолж олно)
    public Chain lessons;   // үзсэн хичээлүүд (Lesson объектууд)
}

public class Lesson {       // Оюутны үзсэн нэг хичээл
    public Subject learned; // үзсэн хичээл (subjectList доторх объект)
    public int score;       // шалгалтын оноо (0–100)
}

public class Registration {
    public ArrayLinearList studentList;
    public ArrayLinearList subjectList;
    public ArrayLinearList majorList;
    ...
}


Subjects.txt
code/name/credit
CSM203/Ugugduliin butets ba algorithm/3.0

Professions.txt
code/name
30/Information technology

Exams.txt
studentCode/subjectCode/score


Шаардлагууд:
Бүх хичээлийн жагсаалтыг харуулах (код, нэр, кредит).

Бүх мэргэжлийн жагсаалтыг харуулах (код, нэр).

Оюутан бүрийн голч дүнг харуулах, мөн бүх оюутны голч дүнгийн дундажийг харуулах.

Гурваас олон (4 ба түүнээс дээш) хичээлд «F» үнэлгээ авсан, хасагдах оюутнуудын жагсаалтыг хэвлэх.

Хичээл бүрээр тухайн хичээлийг үзсэн оюутнуудын дүнгийн жагсаалтыг онооны буурах эрэмбээр харуулах.

Мэргэжил бүрээр тухайн мэргэжлийн оюутнуудын дүнгийн жагсаалтыг харуулах.

Шинэ дүн нэмэх: оюутны код, хичээлийн код, оноог гараас оруулж жагсаалтад нэмэх.

Дүн устгах: оюутны код, хичээлийн кодоор хайж олоод жагсаалтаас устгах.

Өөрчлөлтийг Exams.txt файлд хадгалах.
