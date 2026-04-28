package org.example.exercise1.dao;

import org.example.exercise1.config.DBConnection;
import org.example.exercise1.models.Course;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class CourseDAOImpl implements CourseDAO{
    @Override
    public List<Course> getAllCourses() {
        try(Session session = DBConnection.getSessionFactory().openSession()) {
            return session.createQuery("from Course", Course.class)
            .list();
        }
    }

    @Override
    public void createCourse(Course course) {
        Transaction transaction = null;
        try(Session session = DBConnection.getSessionFactory().openSession()){
            transaction = session.beginTransaction();
            session.persist(course);
            transaction.commit();
        }
        catch (Exception e){
            if(transaction!=null){
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    @Override
    public Course getCourseById(int id) {
        try(Session session = DBConnection.getSessionFactory().openSession()){
            return session.find(Course.class, id);
        }
    }

    @Override
    public Course getCourseByName(String name) {
        try(Session session = DBConnection.getSessionFactory().openSession()){
            return session.createQuery("from Course where courseName=(:name)", Course.class)
            .setParameter("name", name)
            .uniqueResult();
        }
    }

    @Override
    public List<Course> getCoursesById(List<Integer> ids) {
        try(Session session = DBConnection.getSessionFactory().openSession()){
            return session.createQuery("from Course c where c.id in (:ids)",
                    Course.class)
                    .setParameter("ids", ids)
                    .getResultList();
        }
    }

    @Override
    public void updateCourse(Course course) {
        Transaction transaction = null;
        try(Session session = DBConnection.getSessionFactory().openSession()){
            transaction = session.beginTransaction();
            session.merge(course);
            transaction.commit();
        }
        catch (Exception e){
            if(transaction!=null){
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }
}
