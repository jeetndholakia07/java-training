package org.example.exercise1.dao;

import org.example.exercise1.config.DBConnection;
import org.example.exercise1.models.Student;
import org.example.exercise1.utils.Status;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;

public class StudentDAOImpl implements StudentDAO{
    @Override
    public void createStudent(Student student) {
        Transaction transaction = null;
        try(Session session = DBConnection.getSessionFactory().openSession()){
            transaction = session.beginTransaction();
            session.persist(student);
            transaction.commit();
        } catch (Exception e) {
            if(transaction!=null){
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    @Override
    public Student getStudentByName(String name) {
        try(Session session = DBConnection.getSessionFactory().openSession()){
            return session.createQuery("from Student where fullName=(:name)", Student.class)
            .setParameter("name", name)
            .uniqueResult();
        }
    }

    @Override
    public List<Student> getAllStudents() {
        List<Student> studentList = new ArrayList<>();
        try(Session session = DBConnection.getSessionFactory().openSession()){
            studentList = session.createQuery("select distinct s from Student as s left join fetch s.courses where s.status != Status.D",Student.class)
            .list();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return  studentList;
    }

    @Override
    public List<Student> getStudentsById(List<Integer> ids) {
        try(Session session = DBConnection.getSessionFactory().openSession()){
            return session.createQuery("from Student s where s.id in (:ids)",
            Student.class)
            .setParameter("ids", ids)
            .getResultList();
        }
    }

    @Override
    public Student getStudentById(int id) {
        try(Session session = DBConnection.getSessionFactory().openSession()){
            return session.find(Student.class, id);
        }
    }

    @Override
    public void updateStudent(Student student) {
        Transaction transaction = null;
        try(Session session = DBConnection.getSessionFactory().openSession()){
            transaction = session.beginTransaction();
            session.merge(student);
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
    public void deleteStudent(int id) {
        Transaction transaction = null;
        try(Session session = DBConnection.getSessionFactory().openSession()){
            transaction = session.beginTransaction();
            Student s = session.find(Student.class, id);
            s.setStatus(Status.D);
            session.merge(s);
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
