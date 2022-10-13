package com.example.demo.student;

import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<Student> getStudent() {
        return studentRepository.findAll();
    }

    public void addNewStudent(Student student) {
        Optional<Student> studentOptional =  studentRepository.findStudentByEmail(
                student.getEmail()
        );
        if(studentOptional.isPresent()){
            throw new IllegalStateException("email taken");
        }
        studentRepository.save(student);
    }

    public void deleteStudent(Long studentId) {
        boolean existById =  studentRepository.existsById(studentId);
        if(!existById){
            throw new IllegalStateException("Student id "+studentId+"does not exist");
        }
        studentRepository.deleteById(studentId);

    }

    @Transactional
    public void updateStudent(Long studentId,
                              String firstName,
                              String lastName,
                              String email) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(()-> new IllegalStateException(
                        "student with id"+studentId +"does not exist"
                ));
        if(firstName != null && firstName.length()>0 &&
        !Objects.equals(student.getFirstName(),firstName)){
            student.setFirstName(firstName);
        }
        if(lastName != null && lastName.length()>0 &&
                !Objects.equals(student.getLastName(),lastName)){
            student.setLastName(lastName);
        }
        if(email != null && email.length()>0 &&
                !Objects.equals(student.getEmail(),email)){
            Optional<Student> studentOptional = studentRepository.findStudentByEmail(email);
            if(studentOptional.isPresent()){
                throw new IllegalStateException("email taken");
            }
            student.setLastName(lastName);
        }
    }
}
