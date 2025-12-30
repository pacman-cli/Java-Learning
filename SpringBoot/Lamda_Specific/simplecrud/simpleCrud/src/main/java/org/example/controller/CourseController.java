package org.example.controller;

import org.example.dto.Course;
import org.example.service.CourseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/courses")
public class CourseController {
    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }
    @PostMapping(produces = "application/json",consumes = "application/json")
    public ResponseEntity<Course> createCourse(@RequestBody Course course){
        courseService.createCourse(course);
        return new ResponseEntity<>(course, HttpStatus.CREATED);
    }

    @GetMapping(path = "/{id}", produces = "application/json")
    public ResponseEntity<Course> getCourseById(@PathVariable int id){
        Optional<Course> course = courseService.getCourseById(id);
        return course.map(value-> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(()-> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<List<Course>> getAllCourses(){
        List<Course> courses  =courseService.getAllCourses();
        return new ResponseEntity<>(courses, HttpStatus.OK);
    }

    @DeleteMapping(path = "/{id}", produces = "application/json")
    public ResponseEntity<Void> deleteCourseById(@PathVariable int id){
        boolean deleted = courseService.deleteCourseById(id);
        if(deleted){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(path = "/{id}",produces = "application/json",consumes = "application/json")
    public ResponseEntity<Course> updateCourse(@RequestBody Course newCourse,@PathVariable int id){
        boolean update =  courseService.updateCourse(newCourse,id);
        if(update){
            return new ResponseEntity<>(newCourse,HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
