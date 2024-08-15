package common;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.SingularAttribute;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import acmecollege.entity.AcademicStudentClub;
import acmecollege.entity.ClubMembership;
import acmecollege.entity.Course;
import acmecollege.entity.DurationAndStatus;
import acmecollege.entity.MembershipCard;
import acmecollege.entity.NonAcademicStudentClub;
import acmecollege.entity.PeerTutor;
import acmecollege.entity.PeerTutorRegistration;
import acmecollege.entity.PeerTutorRegistrationPK;
import acmecollege.entity.PeerTutorRegistration_;
import acmecollege.entity.Student;
import acmecollege.entity.StudentClub;

/**
 * Super class for all JUnit tests, holds common methods for creating  an EntityManagerFactory and truncating the DB
 * before all.
 */
public class JUnitBase {

	protected static final Logger LOG = LogManager.getLogger();

	/**
	 * Default name of Persistence Unit = "acmecollege-PU"
	 */
	private static final String PERSISTENCE_UNIT = "acmecollege-PU";

	/**
	 * Static instance of EntityManagerFactory for subclasses
	 */
	protected static EntityManagerFactory emf;

	/**
	 * @return An instance of EntityManagerFactory
	 */
	protected static EntityManagerFactory buildEMF() {
		return buildEMF(PERSISTENCE_UNIT);
	}

	/**
	 * Create an instance of EntityManagerFactory using provided Persistence Unit name.
	 * 
	 * @return An instance of EntityManagerFactory
	 */
	protected static EntityManagerFactory buildEMF(String persistenceUnitName) {
		Objects.requireNonNull(persistenceUnitName, "Persistence Unit name cannot be null");
		if (persistenceUnitName.isBlank()) {
			throw new IllegalArgumentException("Persistence Unit name cannot be empty or just white space");
		}
		return Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
	}

	/**
	 * Create a new instance of EntityManager.
	 * call  JUnitBase#buildEMF() or JUnitBase#buildEMF(String) first.
	 * 
	 * @return An instance of  EntityManager
	 */
	protected static EntityManager getEntityManager() {
		if (emf == null) {
			throw new IllegalStateException("EntityManagerFactory is null, must call JUnitBase::buildEMF first");
		}
		return emf.createEntityManager();
	}

	/**
	 * Delete all Entities.  Order of delete matters.
	 */
	protected static void deleteAllData() {
		EntityManager em = getEntityManager();

		// Begin transaction and truncate all tables.
		em.getTransaction().begin();
        em.createQuery("DELETE FROM ClubMembership").executeUpdate();
        em.createQuery("DELETE FROM PeerTutorRegistration").executeUpdate();
        em.createQuery("DELETE FROM MembershipCard").executeUpdate();
        em.createQuery("DELETE FROM StudentClub").executeUpdate();
        em.createQuery("DELETE FROM Course").executeUpdate();
        em.createQuery("DELETE FROM PeerTutor").executeUpdate();
        em.createQuery("DELETE FROM Student").executeUpdate();
        em.getTransaction().commit();
    }

	

	/**
	 * Delete all instances of provided type form the DB.  Same operation as truncate.
	 * 
	 * @param <T>        - Type of entity to delete, can be inferred by JVM when method is being executed.
	 * @param entityType - Class type of entity, like PeerTutor.class
	 * @param em         - EntityManager to be used
	 * @return The number of entities updated or deleted
	 */
	public static <T> int deleteAllFrom(Class<T> entityType, EntityManager em) {
		// Using CriteriaBuilder create a CriteriaDelete to execute a truncate on DB.
		CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaDelete<T> delete = cb.createCriteriaDelete(entityType);
        return em.createQuery(delete).executeUpdate();
		
	}

	public static void addSampleData(EntityManager em) {
		EntityTransaction et = em.getTransaction();
		et.begin();

		StudentClub clubAcademic = new AcademicStudentClub();
		clubAcademic.setName("Computer Programming Club");
		em.persist(clubAcademic);

		StudentClub clubNonAcademic = new NonAcademicStudentClub();
		clubNonAcademic.setName("Student Hiking Club");
		em.persist(clubNonAcademic);

		Course course1 = new Course();
		course1.setCourse("CST8277", "Enterprise Application Programming", 2022, "AUTUMN", 3, (byte) 0);

		Course course2 = new Course();
		course2.setCourse("CST8284", "Object-Oriented Programming in Java", 2022, "SUMMER", 3, (byte) 1);

		PeerTutor peerTutor = new PeerTutor();
		peerTutor.setPeerTutor("Peter", "Schmidt", "Information and Communications Technology");

		Student s = new Student();
		s.setFullName("John", "Smith");

		DurationAndStatus ds = new DurationAndStatus();
		ds.setDurationAndStatus(LocalDateTime.of(2022, 8, 28, 0, 0), LocalDateTime.of(2023, 8, 27, 0, 0) , "+");

		DurationAndStatus ds2 = new DurationAndStatus();
		ds2.setDurationAndStatus(LocalDateTime.of(2021, 1, 1, 0, 0), LocalDateTime.of(2021, 12, 31, 0, 0) , "-");

		PeerTutorRegistration ptr1 = new PeerTutorRegistration();
		ptr1.setPeerTutor(peerTutor);
		ptr1.setCourse(course1);
		ptr1.setLetterGrade("A+");
		ptr1.setNumericGrade(100);
		ptr1.setStudent(s);
		em.persist(ptr1);

		PeerTutorRegistration ptr2 = new PeerTutorRegistration();
		ptr2.setStudent(s);
		ptr2.setCourse(course2);
		ptr2.setNumericGrade(85);
		ptr2.setStudent(s);
		em.persist(ptr2);

		ClubMembership membership = new ClubMembership();
		membership.setDurationAndStatus(ds);
		membership.setStudentClub(clubNonAcademic);
		em.persist(membership);

		ClubMembership membership2 = new ClubMembership();
		membership2.setDurationAndStatus(ds2);
		membership2.setStudentClub(clubAcademic);
		em.persist(membership2);

		MembershipCard card = new MembershipCard();
		card.setOwner(s);
		card.setSigned(true);
		card.setClubMembership(membership);
		em.persist(card);

		MembershipCard card2 = new MembershipCard();
		card2.setOwner(s);
		card2.setSigned(false);
		card2.setClubMembership(membership);
		em.persist(card2);

		MembershipCard card3 = new MembershipCard();
		card3.setOwner(s);
		card3.setSigned(true);
		card3.setClubMembership(membership2);
		em.persist(card3);

		et.commit();
	}

	protected static <T> long getTotalCount(EntityManager em, Class<T> clazz) {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		// Create query for long as we need the number of found rows
		CriteriaQuery<Long> query = builder.createQuery(Long.class);
		// Select count(ptr) from PeerTutorRegistration ptr
		Root<T> root = query.from(clazz);
		query.select(builder.count(root));
		// Create query and set the parameter
		TypedQuery<Long> tq = em.createQuery(query);
		// Get the result as row count
		return tq.getSingleResult();
	}

	protected static <T> List<T> getAll(EntityManager em, Class<T> clazz) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> query = cb.createQuery(clazz);
        query.from(clazz);
        return em.createQuery(query).getResultList();
	}

	protected static <T, R> T getWithId(EntityManager em, Class<T> clazz, Class<R> classPK,
			SingularAttribute<? super T, R> sa, R id) {
		
		CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> query = cb.createQuery(clazz);
        query.where(cb.equal(query.from(clazz).get(sa), id));
        return em.createQuery(query).getSingleResult();
		
	
	}

	protected static <T, R> long getCountWithId(EntityManager em, Class<T> clazz, Class< R> classPK,
			SingularAttribute<? super T, R> sa, R id) {
		/*CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> query = cb.createQuery(Long.class);
        query.select(cb.count(query.from(clazz)));
        query.where(cb.equal(query.from(clazz).get(sa), id));
        return em.createQuery(query).getSingleResult();
		 */      
        CriteriaBuilder builder = em.getCriteriaBuilder();
		// Create query for long as we need the number of found rows
		CriteriaQuery<Long> query = builder.createQuery(Long.class);
		// Select count(ptr) from PeerTutorRegistration ptr where ptr.id = :id
		Root<T> root = query.from(clazz);
		query.select(builder.count(root));
		query.where(builder.equal(root.get(sa), builder.parameter(classPK, "id")));
		// Create query and set the parameter
		TypedQuery<Long> tq = em.createQuery(query);
		tq.setParameter("id",id);
		// Get the result as row count
		return tq.getSingleResult();

	}

	@BeforeAll
	static void setupAll() {
		emf = buildEMF();
		deleteAllData();
	}

	@AfterAll
	static void tearDownAll() {
		emf.close();
	}
}
