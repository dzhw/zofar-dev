/**
 *
 */
package de.his.zofar.service.surveyengine.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.UUID;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.his.zofar.persistence.surveyengine.daos.AnswerOptionDao;
import de.his.zofar.persistence.surveyengine.daos.ParticipantDao;
import de.his.zofar.persistence.surveyengine.daos.RandomViewTransitionDao;
import de.his.zofar.persistence.surveyengine.daos.SortingDao;
import de.his.zofar.persistence.surveyengine.daos.SurveyDataDao;
import de.his.zofar.persistence.surveyengine.daos.SurveyHistoryDao;
import de.his.zofar.persistence.surveyengine.entities.AnswerOptionEntity;
import de.his.zofar.persistence.surveyengine.entities.ParticipantEntity;
import de.his.zofar.persistence.surveyengine.entities.RandomViewTransitionEntity;
import de.his.zofar.persistence.surveyengine.entities.Sorting;
import de.his.zofar.persistence.surveyengine.entities.SurveyDataEntity;
import de.his.zofar.persistence.surveyengine.entities.SurveyHistoryEntity;
import de.his.zofar.service.surveyengine.model.Participant;
import de.his.zofar.service.surveyengine.model.SurveyData;
import de.his.zofar.service.surveyengine.model.SurveyHistory;
import de.his.zofar.service.surveyengine.service.SurveyEngineService;

/**
 * @author le
 * 
 */
@Service(value = "surveyEngineService")
//public class SurveyEngineServiceImpl extends AbstractService implements SurveyEngineService {
public class SurveyEngineServiceImpl implements SurveyEngineService {

	/**
     *
     */
	private static final Logger LOGGER = LoggerFactory
			.getLogger(SurveyEngineServiceImpl.class);

	@Inject
	private final ParticipantDao participantDao;

	@Inject
	private final SurveyDataDao surveyDataDao;

	@Inject
	private final SurveyHistoryDao surveyHistoryDao;

	@Inject
	private final SortingDao sortingDao;

	@Inject
	private final RandomViewTransitionDao randomViewTransitionDao;

	@Inject
	private final AnswerOptionDao answerOptionDao;

	/**
	 * constructor injection to provide daos and the dozer mapper to the
	 * instance.
	 * 
	 * the dozer mapper must be also injected otherwise a NullPointerException
	 * will be thrown.
	 * 
	 * @param participantDao
	 * @param surveyHistoryDao
	 * @param mapper
	 */
	@Inject
	public SurveyEngineServiceImpl(final ParticipantDao participantDao,
			final SurveyHistoryDao surveyHistoryDao,
			final SurveyDataDao surveyDataDao, final SortingDao sortingDao,
			final RandomViewTransitionDao randomViewTransitionDao,
			final AnswerOptionDao answerOptionDao) {
		super();
		this.surveyHistoryDao = surveyHistoryDao;
		this.surveyDataDao = surveyDataDao;
		this.participantDao = participantDao;
		this.sortingDao = sortingDao;
		this.answerOptionDao = answerOptionDao;
		this.randomViewTransitionDao = randomViewTransitionDao;
	}

	@Override
	@Transactional(readOnly = true)
	public List<SurveyHistory> loadHistory(final Participant participant) {
		if (LOGGER.isDebugEnabled())
			LOGGER.debug("mapping participant model to participant entity");
		final ParticipantEntity participantEntity = new ParticipantEntity(
				participant.getToken(), participant.getPassword());
		participantEntity.setId(participant.getId());

		if (LOGGER.isDebugEnabled())
			LOGGER.debug("load history for participant {} from db",
					participant.getUsername());
		// final List<SurveyHistoryEntity> entities = this.surveyHistoryDao
		// .findCustomByParticipantOrderByTimestampAsc(participantEntity);
		final List<SurveyHistoryEntity> entities = this.surveyHistoryDao
				.findByParticipantOrderByTimestampAsc(participantEntity);

		if (entities == null) {
			return null;
		}

		if (LOGGER.isDebugEnabled())
			LOGGER.debug("remove duplicates (SAME movement) size {}",
					entities.size());
		final ListIterator<SurveyHistoryEntity> it = entities.listIterator();
		final List<SurveyHistoryEntity> cleanedEntities = new ArrayList<SurveyHistoryEntity>();
		SurveyHistoryEntity pointer = null;
		while (it.hasNext()) {
			final SurveyHistoryEntity tmp = it.next();
			if ((pointer != null) && (pointer.getPage().equals(tmp.getPage())))
				continue;
			pointer = tmp;
			cleanedEntities.add(tmp);
		}
		entities.clear();
		entities.addAll(cleanedEntities);
		if (LOGGER.isDebugEnabled())
			LOGGER.debug("duplicates removed size {}", entities.size());

		if (LOGGER.isDebugEnabled())
			LOGGER.debug("mapping history entities to history models");
		final List<SurveyHistory> back = new ArrayList<SurveyHistory>();
		for (final SurveyHistoryEntity entry : entities) {
			back.add(new SurveyHistory(participant, entry.getPage(), entry
					.getTimestamp(), entry.getId(), entry.getVersion()));
		}
		if (LOGGER.isDebugEnabled())
			LOGGER.debug("loadHistory() end");
		return back;
	}

	// /**
	// * @return The ViewId which was picked randomly previously.
	// */
	@Override
	@Transactional(readOnly = true)
	public String loadRandomViewTransition(final Participant participant,
			final String fromViewId) {
		final ParticipantEntity participantEntity = new ParticipantEntity();
		participantEntity.setId(participant.getId());
		participantEntity.setPassword(participant.getPassword());
		participantEntity.setToken(participant.getToken());
		participantEntity.setVersion(participant.getVersion());
		final RandomViewTransitionEntity entity = this.randomViewTransitionDao
				.findByParticipantAndFromViewId(participantEntity,fromViewId);

		if (entity != null) {
			return entity.getToViewId();
		} else {
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.his.zofar.service.surveyengine.service.SurveyEngineService#loadParticipant
	 * (java.lang.String)
	 */
	@Override
	@Transactional(readOnly = true)
	public Participant loadParticipant(final String token) {
		final ParticipantEntity participantEntity = this.participantDao
				.findByToken(token);
		if (participantEntity == null)
			return null;
		// final Participant participant = this.mapper.map(entity,
		// Participant.class);
		/* mapping back of entiteis to model */
		final Participant participant = new Participant();
		participant.setId(participantEntity.getId());
		participant.setPassword(participantEntity.getPassword());
		participant.setToken(participantEntity.getToken());
		participant.setVersion(participantEntity.getVersion());
		for (final Map.Entry<String, SurveyDataEntity> entry : participantEntity
				.getSurveyData().entrySet()) {
			final SurveyData entity = new SurveyData();
			entity.setId(entry.getValue().getId());
			entity.setValue(entry.getValue().getValue());
			entity.setVariableName(entry.getValue().getVariableName());
			entity.setVersion(entry.getValue().getVersion());
			entity.setParticipant(participant);
			participant.addSurveyData(entity);
		}
		return participant;
	}

	@Override
	@Transactional(readOnly = true)
	public List<ParticipantEntity> exportParticipants() {
		final List<ParticipantEntity> entities = this.participantDao.findAll();
		if (entities != null) {
			for (ParticipantEntity participant : entities) {
				participant.getSurveyData().size();
			}
		}
		return entities;
	}

	/**
	 * Load all sortings stored for the given participant.
	 * 
	 * @param participant
	 * @return A map parent component uid -> list of children uids
	 */
	@Override
	@Transactional(readOnly = true)
	public Map<String, List<String>> loadSortings(final Participant participant) {

		final ParticipantEntity partEntity = new ParticipantEntity();
		partEntity.setId(participant.getId());
		partEntity.setPassword(participant.getPassword());
		partEntity.setToken(participant.getToken());
		partEntity.setVersion(participant.getVersion());
		for (final Map.Entry<String, SurveyData> entry : participant
				.getSurveyData().entrySet()) {
			if (entry != null) {
				final SurveyDataEntity entity = new SurveyDataEntity();
				entity.setId(entry.getValue().getId());
				entity.setValue(entry.getValue().getValue());
				entity.setVariableName(entry.getValue().getVariableName());
				entity.setVersion(entry.getValue().getVersion());
				entity.setParticipant(partEntity);
				partEntity.addSurveyData(entity);
			}
		}
		final List<Sorting> sortings = this.sortingDao
				.findByParticipant(partEntity);

		// final List<Sorting> sortings = this.sortingDao
		// .findByParticipant(this.mapper.map(participant,
		// ParticipantEntity.class));

		final Map<String, List<String>> result = new HashMap<String, List<String>>();
		for (final Sorting sorting : sortings) {
			// we need to convert the hibernate list into an array list for
			// serialization
			final List<String> children = new ArrayList<String>(sorting
					.getSortedChildrenUIDs().size());
			children.addAll(sorting.getSortedChildrenUIDs());

			result.put(sorting.getParentUID(), children);
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.his.zofar.service.surveyengine.service.SurveyEngineService#saveHistory
	 * (de.his.zofar.service.surveyengine.model.SurveyHistory)
	 */
	// @Override
	// @Transactional
	// public void saveHistory(final SurveyHistory history) {
	// final SurveyHistoryEntity mapped = this.mapper.map(history,
	// SurveyHistoryEntity.class);
	// this.surveyHistoryDao.save(mapped);
	// }
	// @Override
	// @Transactional
	// public void saveHistory(final SurveyHistory history) {
	// final SurveyHistoryEntity mapped = this.mapper.map(history,
	// SurveyHistoryEntity.class);
	// this.surveyHistoryDao.save(mapped);
	// }
	@Override
	@Transactional
	public void saveHistory(final SurveyHistory history) {
		final SurveyHistoryEntity historyEntity = new SurveyHistoryEntity();
		historyEntity.setId(history.getId());
		historyEntity.setPage(history.getPage());
		final ParticipantEntity partEntity = new ParticipantEntity();
		partEntity.setId(history.getParticipant().getId());
		partEntity.setPassword(history.getParticipant().getPassword());
		partEntity.setToken(history.getParticipant().getToken());
		partEntity.setVersion(history.getParticipant().getVersion());
		historyEntity.setParticipant(partEntity);
		// historyEntity.setVersion(history.getVersion());
		this.surveyHistoryDao.save(historyEntity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.his.zofar.service.surveyengine.service.SurveyEngineService#saveParticipant
	 * (de.his.zofar.service.surveyengine.model.Participant)
	 */
	// @Override
	// @Transactional
	// public Participant saveParticipant(final Participant participant) {
	// final ParticipantEntity mapped = this.mapper.map(participant,
	// ParticipantEntity.class);
	//
	// // saving
	// final ParticipantEntity saved = this.participantDao.save(mapped);
	//
	// return this.mapper.map(saved, Participant.class);
	// }
	@Override
	@Transactional
	public Participant saveParticipant(final Participant participant) {
		final ParticipantEntity partEntity = new ParticipantEntity();
		partEntity.setId(participant.getId());
		partEntity.setId(participant.getId());
		partEntity.setPassword(participant.getPassword());
		partEntity.setToken(participant.getToken());
		partEntity.setVersion(participant.getVersion());
		final ParticipantEntity savedEntity = this.participantDao
				.save(partEntity);
		final Participant finalPart = new Participant();
		finalPart.setId(savedEntity.getId());
		finalPart.setToken(savedEntity.getToken());
		finalPart.setPassword(savedEntity.getPassword());
		return finalPart;
	}

	@Transactional
	public Participant saveSurveyParticipant(final Participant participant) {
		/* mapping from given participant-model to participant-entity */
		final ParticipantEntity partEntity = new ParticipantEntity();
		partEntity.setId(participant.getId());
		partEntity.setPassword(participant.getPassword());
		partEntity.setToken(participant.getToken());
		partEntity.setVersion(participant.getVersion());

		for (final Map.Entry<String, SurveyData> entry : participant
				.getSurveyData().entrySet()) {
			if (entry != null) {
				final SurveyDataEntity entity = new SurveyDataEntity();
				entity.setId(entry.getValue().getId());
				entity.setValue(entry.getValue().getValue());
				entity.setVariableName(entry.getValue().getVariableName());
				entity.setVersion(entry.getValue().getVersion());
				entity.setParticipant(partEntity);
				partEntity.addSurveyData(entity);
			}
		}
		final ParticipantEntity savedEntity = this.participantDao
				.saveAndFlush(partEntity);

		/* mapping back of entiteis to model */
		for (final Map.Entry<String, SurveyDataEntity> entry : savedEntity
				.getSurveyData().entrySet()) {
			final SurveyData entity = new SurveyData();
			entity.setId(entry.getValue().getId());
			entity.setValue(entry.getValue().getValue());
			entity.setVariableName(entry.getValue().getVariableName());
			entity.setVersion(entry.getValue().getVersion());
			entity.setParticipant(participant);
			participant.addSurveyData(entity);
		}

		return participant;
	}

	@Override
	@Transactional
	public Participant createAnonymousParticipant() {
		final String token = UUID.randomUUID().toString().replaceAll("-", "");
		return createParticipant(token, token);
	}

	@Override
	@Transactional
	public Participant createParticipant(final String token,
			final String password) {
		return createParticipant(token, password, null);
	}

	@Override
	@Transactional
	public Participant createParticipant(final String token,
			final String password, final Map<String, String> preloads) {
		Participant participant = new Participant();
		participant.setToken(token);
		final org.springframework.security.authentication.encoding.ShaPasswordEncoder encoder = new org.springframework.security.authentication.encoding.ShaPasswordEncoder();
		participant.setPassword(encoder.encodePassword(password, null));

		if (preloads != null) {
			Iterator<String> preloadIt = preloads.keySet().iterator();
			while (preloadIt.hasNext()) {
				final String key = preloadIt.next();
				final String value = preloads.get(key);

				final SurveyData data = new SurveyData();
				data.setVariableName(key);
				data.setValue(value);
				data.setParticipant(participant);
				participant.addSurveyData(data);
			}
		}

		return this.saveParticipant(participant);

		// final ParticipantEntity mapped = this.mapper.map(participant,
		// ParticipantEntity.class);
		//
		// // saving
		// final ParticipantEntity saved = this.participantDao.save(mapped);
		//
		// return this.mapper.map(saved, Participant.class);
	}

	@Override
	@Transactional
	public void saveRandomViewTransition(final Participant participant,
			final String fromViewId, final String toViewId) {
		// final ParticipantEntity participantEntity = this.mapper.map(
		// participant, ParticipantEntity.class);
		final ParticipantEntity participantEntity = new ParticipantEntity();
		participantEntity.setId(participant.getId());
		participantEntity.setPassword(participant.getPassword());
		participantEntity.setToken(participant.getToken());
		participantEntity.setVersion(participant.getVersion());

		// check for existing transition
		RandomViewTransitionEntity entity = this.randomViewTransitionDao
				.findByParticipantAndFromViewId(participantEntity, fromViewId);

		if (entity == null) {
			entity = new RandomViewTransitionEntity();
		}
		entity.setToViewId(toViewId);
		entity.setParticipant(participantEntity);
		entity.setFromViewId(fromViewId);

		this.randomViewTransitionDao.save(entity);
	}

	/**
	 * Create or update given sorting.
	 * 
	 * @param parentUID
	 *            The uid of the parent component.
	 * @param childrenUIDs
	 *            The sorted list of children componten uids.
	 * @param participant
	 *            The participant for which the sorting is saved.
	 */
	@Override
	@Transactional
	public void saveSorting(final String parentUID,
			final List<String> childrenUIDs, final Participant participant) {
		// final ParticipantEntity participantEntity = this.mapper.map(
		// participant, ParticipantEntity.class);

		final ParticipantEntity participantEntity = new ParticipantEntity();
		participantEntity.setId(participant.getId());
		participantEntity.setPassword(participant.getPassword());
		participantEntity.setToken(participant.getToken());
		participantEntity.setVersion(participant.getVersion());
		// for (final Map.Entry<String, SurveyData> entry : participant
		// .getSurveyData().entrySet()) {
		// if (entry != null) {
		// final SurveyDataEntity entity = new SurveyDataEntity();
		// entity.setId(entry.getValue().getId());
		// entity.setValue(entry.getValue().getValue());
		// entity.setVariableName(entry.getValue().getVariableName());
		// entity.setVersion(entry.getValue().getVersion());
		// entity.setParticipant(partEntity);
		// partEntity.addSurveyData(entity);
		// }
		// }

		// check for existing sorting
		Sorting sorting = this.sortingDao.findByParticipantAndParentUID(
				participantEntity, parentUID);

		if (sorting == null) {
			sorting = new Sorting();
		}

		sorting.setParentUID(parentUID);
		sorting.setParticipant(participantEntity);
		sorting.setSortedChildrenUIDs(childrenUIDs);
		this.sortingDao.save(sorting);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.his.zofar.service.surveyengine.service.SurveyEngineService#
	 * loadLabelsAndConditions(java.lang.String, java.lang.String)
	 */
	@Override
	@Transactional(readOnly = true)
	public Map<String, String> loadLabelsAndConditions(final String variable,
			final String answerOptionUid) {
		// TODO make this method cacheable using the @Cacheable annotation from
		// spring as the result of this method is guaranteed for the arguments
		// within a running survey. We can therefore reduce the amount of
		// database queries. See
		// http://docs.spring.io/spring/docs/3.2.4.RELEASE/spring-framework-reference/html/cache.html
		// for further informations.

		if (LOGGER.isDebugEnabled())
			LOGGER.debug("loading labels and condition for: {} - {}", variable,
					answerOptionUid);
		final AnswerOptionEntity entity = this.answerOptionDao
				.findByVariableNameAndAnswerOptionUid(variable, answerOptionUid);

		Map<String, String> result = new HashMap<String, String>();

		if (entity != null) {
			result = entity.getConditionedResourceKeys();
		}

		return result;
	}

}