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
public class TestCRUDPeerTutor extends JUnitBase {

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
		peerTutor = new PeerTutor();
		peerTutor.setFirstName("John");
		peerTutor.setLastName("Smith");
		peerTutor.setProgram("Computer Programming");
		
		em.persist(peerTutor);
		et.commit();
long result=getCountWithId(em,PeerTutor.class,Integer.class, PeerTutor_.id,peerTutor.getId());
		
		// There should only be one row in the DB
		//assertThat(result, is(greaterThanOrEqualTo(1L)));
		assertEquals(result, 1);
	}

	@Test
	void test03_CreateInvalid() {
		et.begin();
		PeerTutor peerTutor2 = new PeerTutor();
		peerTutor2.setFirstName("John");
		peerTutor2.setLastName("Smith");
		// We expect a failure because course is part of the composite key
		assertThrows(PersistenceException.class, () -> em.persist(peerTutor2));
		et.commit();
		
	}

	@Test
	void test04_Read() {
		
		// Get the result as row count
		List<PeerTutor> peerTutors = getAll(em,PeerTutor.class);

		assertThat(peerTutors, contains(equalTo(peerTutor)));
	}



	@Test
	void test06_Update() {
		
		PeerTutor returnedPeerTutor=getWithId(em, PeerTutor.class,Integer.class, PeerTutor_.id, peerTutor.getId());

		String newFirstName = "Ann";
		String newLastName="Brown";
		String newProgram = "Computer Engineering";

		et.begin();
		returnedPeerTutor.setFirstName(newFirstName);
		returnedPeerTutor.setLastName(newLastName);
		returnedPeerTutor.setProgram(newProgram);
		
		em.merge(returnedPeerTutor);
		et.commit();

		returnedPeerTutor=getWithId(em, PeerTutor.class,Integer.class, PeerTutor_.id, peerTutor.getId());

		assertThat(returnedPeerTutor.getFirstName(), equalTo(newFirstName));
		assertThat(returnedPeerTutor.getLastName(), equalTo(newLastName));
		assertThat(returnedPeerTutor.getProgram(), equalTo(newProgram));
		
	}

	

	@Test
	void test09_Delete() {
		
		PeerTutor returnedPeerTutor=getWithId(em, PeerTutor.class,Integer.class, PeerTutor_.id, peerTutor.getId());

		et.begin();
		// Add another row to db to make sure only the correct row is deleted
		PeerTutor peerTutor2 = new PeerTutor();
		peerTutor2.setFirstName("Cassiopeia");
		peerTutor2.setLastName("Paccino");
		peerTutor2.setProgram("Computer Engineering");
		
		em.persist(peerTutor2);
		et.commit();

		et.begin();
		em.remove(returnedPeerTutor);
		et.commit();

		long result=getCountWithId(em,PeerTutor.class,Integer.class, PeerTutor_.id,returnedPeerTutor.getId());
		
		assertThat(result, is(equalTo(0L)));

		// Create query and set the parameter
		result=getCountWithId(em,PeerTutor.class,Integer.class, PeerTutor_.id,peerTutor2.getId());
		assertThat(result, is(equalTo(1L)));
	}
	
}



