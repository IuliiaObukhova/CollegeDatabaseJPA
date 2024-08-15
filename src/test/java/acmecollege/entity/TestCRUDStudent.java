package acmecollege.entity;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.comparesEqualTo;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import common.JUnitBase;

@TestMethodOrder(MethodOrderer.MethodName.class)
public class TestCRUDStudent extends JUnitBase {

	private EntityManager em;
	private EntityTransaction et;

	private static Course course;
	private static PeerTutor peerTutor;
	private static Student student;
	private static PeerTutorRegistration peerTutorRegistration;
	private static final String LETTER_GRADE = "A+";
	private static final int NUMERIC_GRADE = 100;
	private static MembershipCard membershipCard;

	
	@BeforeAll
	static void setupAllInit() {
		course = new Course();
		course.setCourse("CST8277", "Enterprise Application Programming", 2022, "AUTUMN", 3, (byte) 0);

		peerTutor = new PeerTutor();
		peerTutor.setPeerTutor("Peter", "Schmidt", "Information and Communications Technology");

		
		
		membershipCard=new MembershipCard();
	
	}

	@BeforeEach
	void setup() {
		em = getEntityManager();
		et = em.getTransaction();
	}

	@AfterEach
	void tearDown() {
		em.close();
	}

	@Test
	void test01_Empty() {

		
long result=getTotalCount(em,PeerTutorRegistration.class);
		assertThat(result, is(comparesEqualTo(0L)));

	}

	@Test
	void test02_Create() {
		et.begin();
		student = new Student();
		student.setFullName("John", "Smith");
		
		em.persist(student);
		et.commit();
long result=getCountWithId(em,Student.class,Integer.class, Student_.id,student.getId());
		
		// There should only be one row in the DB
		//assertThat(result, is(greaterThanOrEqualTo(1L)));
		assertEquals(result, 1);
	}

	@Test
	void test03_CreateInvalid() {
		et.begin();
		Student student2 = new Student();
		
		// We expect a failure because course is part of the composite key
		assertThrows(PersistenceException.class, () -> em.persist(student2));
		et.commit();
		
	}

	@Test
	void test04_Read() {
		
		// Get the result as row count
		List<Student> students = getAll(em,Student.class);

		assertThat(students, contains(equalTo(student)));
	}

	
	@Test
	void test06_Update() {
		
		Student returnedStudent=getWithId(em, Student.class,Integer.class, Student_.id, student.getId());

		String newFirstName = "Ann";
		String newLastName="Brown";
		int newNumericGrade = 85;

		et.begin();
		returnedStudent.setFirstName(newFirstName);
		returnedStudent.setLastName(newLastName);
		
		em.merge(returnedStudent);
		et.commit();

		returnedStudent=getWithId(em, Student.class,Integer.class, Student_.id, student.getId());

		assertThat(returnedStudent.getFirstName(), equalTo(newFirstName));
		assertThat(returnedStudent.getLastName(), equalTo(newLastName));
	}

	
	
	@Test
	void test09_Delete() {
		
		Student returnedStudent=getWithId(em, Student.class,Integer.class, Student_.id, student.getId());

		et.begin();
		// Add another row to db to make sure only the correct row is deleted
		Student student2 = new Student();
		student2.setFirstName("Andromeda");
		student2.setLastName("Caprio");
		
		em.persist(student2);
		et.commit();

		et.begin();
		em.remove(returnedStudent);
		et.commit();

		long result=getCountWithId(em,Student.class,Integer.class, Student_.id,returnedStudent.getId());
		
		assertThat(result, is(equalTo(0L)));

		// Create query and set the parameter
		result=getCountWithId(em,Student.class,Integer.class, Student_.id,student2.getId());
		assertThat(result, is(equalTo(1L)));
	}
}

