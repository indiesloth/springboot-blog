package me.sanghun.springbootdeveloper.controller;

import java.time.LocalDate;
import java.util.*;
import lombok.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ExampleController {

  @GetMapping("/thymeleaf/example")
  public String thymeleafExample(Model model) {
    Person examplePerson = new Person();
    examplePerson.setId(1L);
    examplePerson.setName("홍길동");
    examplePerson.setAge(11);
    examplePerson.setHobbies(List.of("운동", "독서"));
    /*
      (Java9부터) 변경 불가능한 리스트를 생성하며, 원하는 값을 직접 제공할 수 있습니다.
      List.of("운동", "독서");

      하나씩 생성하는 방법
      List<String> hobbies = new ArrayList<>();
      hobbies.add("운동");
      hobbies.add("독서");
     */

    model.addAttribute("person", examplePerson);
    model.addAttribute("today", LocalDate.now());

    return "example";
  }

  @Getter
  @Setter
  class Person {

    private Long id;
    private String name;
    private int age;
    private List<String> hobbies;
  }
}

