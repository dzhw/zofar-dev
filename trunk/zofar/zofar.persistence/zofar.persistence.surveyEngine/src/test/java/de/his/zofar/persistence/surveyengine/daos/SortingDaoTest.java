package de.his.zofar.persistence.surveyengine.daos;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import de.his.zofar.persistence.surveyengine.entities.ParticipantEntity;
import de.his.zofar.persistence.surveyengine.entities.Sorting;
import de.his.zofar.persistence.test.daos.AbstractDaoTest;

/**
 * Simple Dao Test.
 * 
 * @author Reitmann
 */
public class SortingDaoTest extends AbstractDaoTest {
    @Resource
    private SortingDao sortingDao;

    @Resource
    private ParticipantDao participantDao;

    private ParticipantEntity createParticipant() {
        final ParticipantEntity participant = new ParticipantEntity();
        participant.setPassword("hurz");
        participant.setToken("hurz");
        return this.participantDao.save(participant);
    }

    private Sorting createSortedUIDList(final ParticipantEntity participant) {
        final Sorting uidList = new Sorting();
        uidList.setParentUID("hurz");
        final List<String> uids = new ArrayList<String>();
        uids.add("erster");
        uids.add("zweiter");
        uids.add("dritter");
        uidList.setSortedChildrenUIDs(uids);
        uidList.setParticipant(participant);
        return uidList;
    }

    @Test
    @Transactional
    @Rollback(value = true)
    public void loadSortingByParentUID() {
        final ParticipantEntity participant = createParticipant();
        final Sorting sorting = createSortedUIDList(participant);
        this.sortingDao.save(sorting);

        final Sorting sorting2 = this.sortingDao.findByParticipantAndParentUID(
                participant, "hurz");

        Assert.assertArrayEquals(sorting.getSortedChildrenUIDs().toArray(),
                sorting2.getSortedChildrenUIDs().toArray());
    }

    @Test(expected = DataIntegrityViolationException.class)
    @Transactional
    @Rollback(value = true)
    public void saveSorting() {
        final ParticipantEntity participant = createParticipant();

        final Sorting sorting = createSortedUIDList(participant);
        this.sortingDao.save(sorting);

        Sorting sorting2 = createSortedUIDList(participant);

        sorting2 = this.sortingDao.save(sorting2);

        this.sortingDao.flush();

        Assert.fail("Should not be reached, cause we insert duplicate parentUIDs for one participant!");
    }

}
