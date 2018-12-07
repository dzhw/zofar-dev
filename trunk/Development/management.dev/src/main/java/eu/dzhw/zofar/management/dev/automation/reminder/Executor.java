package eu.dzhw.zofar.management.dev.automation.reminder;

import java.io.File;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.dzhw.zofar.mailReturns.scanning.ScanningClient;
import eu.dzhw.zofar.management.dev.automation.AbstractExecutor;
import eu.dzhw.zofar.management.utils.files.CSVClient;
import eu.dzhw.zofar.management.utils.files.FileClient;

public class Executor extends AbstractExecutor {

	private static final long serialVersionUID = 1954858691448432000L;

	private static final Executor INSTANCE = new Executor();
	private static final Logger LOGGER = LoggerFactory.getLogger(Executor.class);

	public enum Parameter implements ABSTRACTPARAMETER {
		tokenColumn, mailColumn, mailbox, mailboxPath, mailboxServer, mailboxUser, mailboxPass, dbServer, dbName, dbPort, dbUser, dbPass, invitations, blacklist, toRemind, toIgnore, delimiter, quote;
	};

	private Executor() {
		super();

	}

	public static Executor getInstance() {
		return INSTANCE;
	}

	public ParameterMap<ABSTRACTPARAMETER, Object> getParameterMap(final String mailbox, final String mailboxPath, final String mailboxServer, final String mailboxUser, final String mailboxPass, final String dbServer, final String dbName, final String dbPort, final String dbUser, final String dbPass, final File invitations, final File blacklist, final File toRemind, final File toIgnore, final String tokenColumn, final String mailColumn,final Character delimiter,final Character quote) {
		final ParameterMap<ABSTRACTPARAMETER, Object> back = new ParameterMap<ABSTRACTPARAMETER, Object>();
		back.put(Parameter.mailbox, mailbox);
		back.put(Parameter.mailboxPath, mailboxPath);
		back.put(Parameter.mailboxServer, mailboxServer);
		back.put(Parameter.mailboxUser, mailboxUser);
		back.put(Parameter.mailboxPass, mailboxPass);
		back.put(Parameter.dbServer, dbServer);
		back.put(Parameter.dbName, dbName);
		back.put(Parameter.dbPort, dbPort);
		back.put(Parameter.dbUser, dbUser);
		back.put(Parameter.dbPass, dbPass);
		back.put(Parameter.invitations, invitations);
		back.put(Parameter.blacklist, blacklist);

		back.put(Parameter.toRemind, toRemind);
		back.put(Parameter.toIgnore, toIgnore);
		back.put(Parameter.tokenColumn, tokenColumn);
		back.put(Parameter.mailColumn, mailColumn);

		back.put(Parameter.delimiter, delimiter);
		back.put(Parameter.quote, quote);

		return back;
	}

	@Override
	public void process(ParameterMap<ABSTRACTPARAMETER, Object> parameter) throws Exception {
		final String mailbox = (String) parameter.get(Parameter.mailbox);
		final String mailboxPath = (String) parameter.get(Parameter.mailboxPath);
		final String mailboxServer = (String) parameter.get(Parameter.mailboxServer);
		final String mailboxUser = (String) parameter.get(Parameter.mailboxUser);
		final String mailboxPass = (String) parameter.get(Parameter.mailboxPass);

		final String dbServer = (String) parameter.get(Parameter.dbServer);
		final String dbName = (String) parameter.get(Parameter.dbName);
		final String dbPort = (String) parameter.get(Parameter.dbPort);
		final String dbUser = (String) parameter.get(Parameter.dbUser);
		final String dbPass = (String) parameter.get(Parameter.dbPass);

		final File invitations = (File) parameter.get(Parameter.invitations);
		final File blacklist = (File) parameter.get(Parameter.blacklist);
		final File toRemind = (File) parameter.get(Parameter.toRemind);
		final File toIgnore = (File) parameter.get(Parameter.toIgnore);

		final String tokenColumn = (String) parameter.get(Parameter.tokenColumn);
		final String mailColumn = (String) parameter.get(Parameter.mailColumn);
		
		final Character delimiter = (Character) parameter.get(Parameter.delimiter);
		final Character quote = (Character) parameter.get(Parameter.quote);


		final List<String> blockMails = new ArrayList<String>();
		ScanningClient scanner = ScanningClient.getInstance();
		final Map<Set<String>, Set<String>> result = scanner.scan(mailboxUser, mailboxPass, mailboxServer, mailboxPath, null);
		if (result != null) {
			final Set<Set<String>> keys = result.keySet();
			if (keys != null) {
				final Iterator<Set<String>> it = keys.iterator();
				while (it.hasNext()) {
					Set<String> mails = it.next();
					for (final String mail : mails) {
						blockMails.add(mail);
					}
				}
			}
		}

		CSVClient csv = CSVClient.getInstance();
		if (blacklist != null) {
			final ArrayList<String> blackHeaders = csv.getCSVHeaders(blacklist, delimiter, quote);
			if (!blackHeaders.contains(mailColumn)) {
				throw new Exception("mail Column " + mailColumn + " is unkown in blacklist");
			}
			List<Map<String, String>> data = csv.loadCSV(blacklist, blackHeaders, true, delimiter, quote);
			for (final Map<String, String> item : data) {
				blockMails.add(item.get(mailColumn).toLowerCase());
			}
		}
		else {
			System.out.println("No Blacklist");
		}

		final List<String> blockTokens = new ArrayList<String>();
		Connection dbConn = null;
		try {
			dbConn = postgresClient.getConnection(dbServer, dbPort, dbName, dbUser, dbPass);
			if (dbConn != null) {
				final List<Map<String, String>> dbTokens = postgresClient.queryDb(dbConn, "Select token from participant where id IN (select distinct participant_id from surveyhistory where page='/end.xhtml');");
				if (dbTokens != null) {
					for (final Map<String, String> item : dbTokens) {
						blockTokens.add(item.get("token"));
					}
				}
			}
		} catch (Exception e) {
			throw e;
		} finally {
			if (dbConn != null)
				postgresClient.close(dbConn);
		}
		System.out.println("block mails : "+blockMails);
		System.out.println("block tokens : "+blockTokens);

		final ArrayList<String> headers = csv.getCSVHeaders(invitations, delimiter, quote);
		if (!headers.contains(mailColumn))
			throw new Exception("mail Column " + mailColumn + " is unkown in invitation data");
		if (!headers.contains(tokenColumn))
			throw new Exception("token Column " + tokenColumn + " is unkown in invitation data");
		List<Map<String, String>> data = csv.loadCSV(invitations, headers, true, delimiter, quote);
		List<Map<String, String>> toRemindData = new ArrayList<Map<String, String>>();
		List<Map<String, String>> toIgnoreData = new ArrayList<Map<String, String>>();
		if (data != null) {
			for (final Map<String, String> item : data) {
				boolean block = false;
				if (blockMails.contains(item.get(mailColumn).toLowerCase()))
					block = true;
				if (blockTokens.contains(item.get(tokenColumn)))
					block = true;
				if (block) {
					toIgnoreData.add(item);
				} else {
					toRemindData.add(item);
				}
			}
			FileClient.getInstance().writeToFile(toIgnore, "", false);
			FileClient.getInstance().writeToFile(toRemind, "", false);
			csv.saveCSV(toIgnore, headers, toIgnoreData);
			csv.saveCSV(toRemind, headers, toRemindData);
		}
	}

}
