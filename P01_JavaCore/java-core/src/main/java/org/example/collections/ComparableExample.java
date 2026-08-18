package org.example.collections;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ComparableExample {
    public static void main(String[] args) {
        List<Student> students = new ArrayList<>();
        students.add(new Student("Phat", 8.5));
        students.add(new Student("Nam", 7.0));
        students.add(new Student("An", 9.0));

        Collections.sort(students);

        System.out.println("Students sorted by grade ascending:");
        for (Student student : students) {
            System.out.println(student);
        }
    }

    static class Student implements Comparable<Student> {
        private final String name;
        private final double grade;

        public Student(String name, double grade) {
            this.name = name;
            this.grade = grade;
        }

        @Override
        public int compareTo(Student other) {
            return Double.compare(this.grade, other.grade);
        }

        @Override
        public String toString() {
            return "Student{" +
                    "name='" + name + '\'' +
                    ", grade=" + grade +
                    '}';
        }
    }
}
