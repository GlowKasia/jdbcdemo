package jdbcdemo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
//@AllArgsConstructor
@NoArgsConstructor
public class Student {
    private Long id;
    private String name;
    private int age;
    private Double average;
    private boolean alive;

    public Student(Long id, String name, int age, Double average, boolean alive) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.average = average;
        this.alive = alive;
    }
}
