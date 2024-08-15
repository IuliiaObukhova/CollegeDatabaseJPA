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
public class TestCRUDCourse extends JUnitBase {

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

		
long result=getTotalCount(em,PeerTutor.class);
		assertThat(result, is(comparesEqualTo(0L)));

	}

	@Test
	void test02_Create() {
		et.begin();
		course = new Course();
		course.setCourse("CST8277", "Enterprise Application Programming", 2022, "AUTUMN", 3, (byte) 0);

		
		em.persist(course);
		et.commit();
long result=getCountWithId(em,Course.class,Integer.class, Course_.id,course.getId());
		
		// There should only be one row in the DB
		//assertThat(result, is(greaterThanOrEqualTo(1L)));
		assertEquals(result, 1);
	}

	@Test
	void test03_CreateInvalid() {
		et.begin();
		Course course2 = new Course();
		
		// We expect a failure because course is part of the composite key
		assertThrows(PersistenceException.class, () -> em.persist(course2));
		et.commit();
		
	}

	@Test
	void test04_Read() {
		
		// Get the result as row count
		List<Course> courses = getAll(em,Course.class);

		assertThat(courses, contains(equalTo(course)));
	}

	//@Test
	/*void test05_ReadDependencies() {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		// Create query for PeerTutorRegistration
		CriteriaQuery<PeerTutorRegistration> query = builder.createQuery(PeerTutorRegistration.class);
		// Select ptr from PeerTutorRegistration ptr
		Root<PeerTutorRegistration> root = query.from(PeerTutorRegistration.class);
		query.select(root);
		query.where(builder.equal(root.get(PeerTutorRegistration_.id), builder.parameter(PeerTutorRegistrationPK.class, "id")));
		// Create query and set the parameter
		TypedQuery<PeerTutorRegistration> tq = em.createQuery(query);
		tq.setParameter("id", peerTutorRegistration.getId());
		// Get the result as row count
		PeerTutorRegistration returnedPeerTutorRegistration = tq.getSingleResult();

		assertThat(returnedPeerTutorRegistration.getStudent(), equalTo(student));
		assertThat(returnedPeerTutorRegistration.getLetterGrade(), equalTo(LETTER_GRADE));
		assertThat(returnedPeerTutorRegistration.getNumericGrade(), equalTo(NUMERIC_GRADE));
		assertThat(returnedPeerTutorRegistration.getCourse(), equalTo(course));
		assertThat(returnedPeerTutorRegistration.getPeerTutor(), equalTo(peerTutor));
	}*/
	
	@Test
	void test05_ReadDependencies() {
	    // Create a course
	    Course course = new Course();
	    course.setCourseCode("CST8277");
	    course.setCourseTitle("Enterprise Application Programming");
	    course.setYear(2022);
	    course.setSemester("AUTUMN");
	    course.setCreditUnits(3);
	    course.setOnline((byte) 0);
	    et.begin();
	    em.persist(course);
	    et.commit();

	    // Create a peer tutor
	    PeerTutor peerTutor = new PeerTutor();
	    peerTutor.setPeerTutor("Peter", "Schmidt", "Information and Communications Technology");
	    et.begin();
	    em.persist(peerTutor);
	    et.commit();

	    // Create a student
	    Student student = new Student();
	    student.setFullName("John", "Doe");
	    et.begin();
	    em.persist(student);
	    et.commit();

	    // Create a peer tutor registration
	    PeerTutorRegistration peerTutorRegistration = new PeerTutorRegistration();
	    peerTutorRegistration.setCourse(course);
	    peerTutorRegistration.setPeerTutor(peerTutor);
	    peerTutorRegistration.setStudent(student);
	    peerTutorRegistration.setLetterGrade("A+");
	    peerTutorRegistration.setNumericGrade(100);
	    et.begin();
	    em.persist(peerTutorRegistration);
	    et.commit();

	    // Perform the read dependencies test
	    CriteriaBuilder builder = em.getCriteriaBuilder();
	    CriteriaQuery<PeerTutorRegistration> query = builder.createQuery(PeerTutorRegistration.class);
	    Root<PeerTutorRegistration> root = query.from(PeerTutorRegistration.class);
	    query.select(root);
	    query.where(builder.equal(root.get(PeerTutorRegistration_.id), builder.parameter(PeerTutorRegistrationPK.class, "id")));
	    TypedQuery<PeerTutorRegistration> tq = em.createQuery(query);
	    tq.setParameter("id", peerTutorRegistration.getId());
	    PeerTutorRegistration returnedPeerTutorRegistration = tq.getSingleResult();

	    assertThat(returnedPeerTutorRegistration.getStudent(), equalTo(student));
	    assertThat(returnedPeerTutorRegistration.getLetterGrade(), equalTo("A+"));
	    assertThat(returnedPeerTutorRegistration.getNumericGrade(), equalTo(100));
	    assertThat(returnedPeerTutorRegistration.getCourse(), equalTo(course));
	    assertThat(returnedPeerTutorRegistration.getPeerTutor(), equalTo(peerTutor));
	}


	@Test
	void test06_Update() {
		
		Course returnedCourse=getWithId(em, Course.class,Integer.class, Course_.id, course.getId());

		String newCourseCode = "1l2p3d";
		String newCourseTitle="Java Programming";
		int newYear = 2024;
		String newSemester="Winter";
		int newCreditUnits=70;
		

		et.begin();
		returnedCourse.setCourseCode(newCourseCode);
		returnedCourse.setCourseTitle(newCourseTitle);
		returnedCourse.setYear(newYear);
		returnedCourse.setSemester(newSemester);
		returnedCourse.setCreditUnits(newCreditUnits);
		
		em.merge(returnedCourse);
		et.commit();

		returnedCourse=getWithId(em, Course.class,Integer.class, Course_.id, course.getId());

		assertThat(returnedCourse.getCourseCode(), equalTo(newCourseCode));
		assertThat(returnedCourse.getCourseTitle(), equalTo(newCourseTitle));
		assertThat(returnedCourse.getYear(), equalTo(newYear));
		assertThat(returnedCourse.getSemester(), equalTo(newSemester));
		assertThat(returnedCourse.getCreditUnits(), equalTo(newCreditUnits));
		
	}

	
	@Test
	void test09_Delete() {
		
		Course returnedCourse=getWithId(em, Course.class,Integer.class, Course_.id, course.getId());

		et.begin();
		// Add another row to db to make sure only the correct row is deleted
		Course course2 = new Course();
		course2.setCourseCode("gh");
		course2.setCourseTitle("Database Administation");
		course2.setYear(2024);
		course2.setSemester("Fall");
		course2.setCreditUnits(80);
		
		em.persist(course2);
		et.commit();

		et.begin();
		em.remove(returnedCourse);
		et.commit();

		long result=getCountWithId(em,Course.class,Integer.class, Course_.id,returnedCourse.getId());
		
		assertThat(result, is(equalTo(0L)));

		// Create query and set the parameter
		result=getCountWithId(em,Course.class,Integer.class, Course_.id,course2.getId());
		assertThat(result, is(equalTo(1L)));
	}
	
}



