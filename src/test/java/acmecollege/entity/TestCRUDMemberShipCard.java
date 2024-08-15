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

import java.time.LocalDateTime;
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
public class TestCRUDMemberShipCard extends JUnitBase {

	private EntityManager em;
	private EntityTransaction et;

	private static Course course;
	private static PeerTutor peerTutor;
	private static Student student;
	private static PeerTutorRegistration peerTutorRegistration;
	private static final String LETTER_GRADE = "A+";
	private static final int NUMERIC_GRADE = 100;
	private static MembershipCard membershipCard;
	private static ClubMembership clubMembership;
	private static StudentClub studentClub;

	
	@BeforeAll
	static void setupAllInit() {
		student = new Student();
		student.setFullName("John", "Smith");
		//student.
		studentClub = new AcademicStudentClub();
		studentClub.setName("Computer Programming Club");
		clubMembership = new ClubMembership();
		clubMembership.getDurationAndStatus().setEndDate(LocalDateTime.now());
		clubMembership.getDurationAndStatus().setStartDate(LocalDateTime.now());
		clubMembership.setStudentClub(studentClub);
		
		
		
	
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
		student = new Student();
		student.setFullName("John", "Smith");
		
		em.persist(student);
		et.commit();
long result=getCountWithId(em,Student.class,Integer.class, Student_.id,student.getId());
		
		// There should only be one row in the DB
		//assertThat(result, is(greaterThanOrEqualTo(1L)));
		assertEquals(result, 1);
	
		et.begin();
		
		membershipCard = new MembershipCard();
		membershipCard.setClubMembership(clubMembership);
		membershipCard.setOwner(student);
		

		
		em.persist(membershipCard);
		et.commit();
 result=getCountWithId(em,MembershipCard.class,Integer.class, MembershipCard_.id,membershipCard.getId());
		
		// There should only be one row in the DB
		//assertThat(result, is(greaterThanOrEqualTo(1L)));
	   // assertEquals(1, result);
		//System.out.println("ABRACADABRA");
	}

	@Test
	void test03_CreateInvalid() {
		et.begin();
		//MembershipCard membershipCard2 = new MembershipCard();
		
		// We expect a failure because course is part of the composite key
		//assertThrows(PersistenceException.class, () -> em.persist(membershipCard2));
		et.commit();
		
	}

	@Test
	void test04_Read() {
		
		// Get the result as row count
		List<MembershipCard> membershipCards = getAll(em,MembershipCard.class);

		assertThat(membershipCards, contains(equalTo(membershipCard)));
	}

	

	@Test
	void test06_Update() {
		
		MembershipCard returnedMembershipCard=getWithId(em, MembershipCard.class,Integer.class, MembershipCard_.id, membershipCard.getId());

		byte newSigned=(byte)1;

		et.begin();
		returnedMembershipCard.setSigned(newSigned);
		
		em.merge(returnedMembershipCard);
		et.commit();

		returnedMembershipCard=getWithId(em, MembershipCard.class,Integer.class, MembershipCard_.id, membershipCard.getId());

		assertThat(returnedMembershipCard.getSigned(), equalTo(newSigned));
	}
	

	
	@Test
	void test09_Delete() {
		et.begin();
		student = new Student();
		student.setFullName("John2", "Smith2");
		studentClub = new AcademicStudentClub();
		studentClub.setName("Computer Programming Club2");
		clubMembership = new ClubMembership();
		clubMembership.getDurationAndStatus().setEndDate(LocalDateTime.now());
		clubMembership.getDurationAndStatus().setStartDate(LocalDateTime.now());
		clubMembership.setStudentClub(studentClub);
		
		em.persist(student);
		em.persist(studentClub);
		em.persist(clubMembership);
	
		et.commit();
		MembershipCard returnedMembershipCard=getWithId(em, MembershipCard.class,Integer.class, MembershipCard_.id, membershipCard.getId());

		et.begin();
		// Add another row to db to make sure only the correct row is deleted
		MembershipCard membershipCard2 = new MembershipCard();
		membershipCard2.setSigned((byte)1);
		membershipCard2.setClubMembership(clubMembership);
		membershipCard2.setOwner(student);
		
		
		em.persist(membershipCard2);
		et.commit();

		et.begin();
		em.remove(returnedMembershipCard);
		et.commit();

		long result=getCountWithId(em,MembershipCard.class,Integer.class, MembershipCard_.id,returnedMembershipCard.getId());
		
		assertThat(result, is(equalTo(0L)));

		// Create query and set the parameter
		result=getCountWithId(em,MembershipCard.class,Integer.class, MembershipCard_.id,membershipCard2.getId());
		assertThat(result, is(equalTo(1L)));
	}
}

