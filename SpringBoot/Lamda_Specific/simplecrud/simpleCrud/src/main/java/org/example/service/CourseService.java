package org.example.service;

import org.example.dto.Course;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CourseService {
    private final List<Course> courseList= new ArrayList<>();

    public void createCourse(Course course) {
        courseList.add(course);
    }
    public List<Course> getAllCourses(){
        return courseList;
    }
    public Optional<Course> getCourseById(int id){
        return courseList.stream()
                .filter(course -> course.getId()==id)
                .findFirst();
    }
    public boolean deleteCourseById(int id){
        return courseList.removeIf(course -> course.getId() == id);
    }
    public boolean updateCourse(Course newCourse,int id){
        return getCourseById(id).map(
                existingcourse-> {
                    courseList.remove(existingcourse);
                    courseList.add(newCourse);
                    return true;
                }
        ).orElse(false);
    }
}
