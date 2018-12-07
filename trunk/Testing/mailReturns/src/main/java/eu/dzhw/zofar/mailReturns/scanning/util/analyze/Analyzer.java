package eu.dzhw.zofar.mailReturns.scanning.util.analyze;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Analyzer {

	private static final Logger LOGGER = LoggerFactory.getLogger(Analyzer.class);

	private static final Analyzer INSTANCE = new Analyzer();

	private final LinkedHashMap<String, Boolean> needles;

	public static enum TYPE {
		ADDRESS, REASONS, SENDER
	};

	private Analyzer() {
		super();
		this.needles = new LinkedHashMap<String, Boolean>();
		// this.needles.put("Recipient address re(j(ect(ed)?)?)?", true);
		// this.needles.put("User (is )?unknown", true);
		// this.needles.put("[Aa]ddress re(j(ect(ed)?)?)?", true);
		// this.needles.put("[Aa]ddress unknown", true);
		// this.needles.put("cannot be resolved", true);
		// this.needles.put("invalid address", true);
		// this.needles.put("mailbox unavailable", true);
		// this.needles.put("unknown user account", true);
		// this.needles.put("Sorry,_I_wasn't_able_to_establish_an_SMTP_connection",
		// true);
		// this.needles.put("I wasn't able to establish an SMTP connection",
		// true);
		// this.needles.put("451 (has too )?much mail stored in his mailbox",
		// true);
		// this.needles.put("430 Timeout connecting", true);
		// this.needles.put("550 [Uu]nrouteable address", true);
		// this.needles.put("553 address expired", true);
		// this.needles.put("This user doesn't have a yahoo.com account", true);
		// this.needles.put("mailbox is full", true);
		// this.needles.put("Over quota", true);
		// this.needles.put("undefined mail delivery mode", true);
		// this.needles.put("User '([^\']*)' has mail disabled.", true);

		// this.needles.put("address re(j(ect(ed)?)?)?", true);
		// this.needles.put("re(j(ect(ed)?)?)? address", true);

		this.needles.put("recipnotfound", true);
		this.needles.put("unknown recipient", true);
		this.needles.put("recipient unknown", true);
		this.needles.put("nonexisting recipient", true);
		this.needles.put("invalid recipient", true);

		this.needles.put("user (is )?unknown", true);
		this.needles.put("user unkown", true);

		this.needles.put("unknown (local )?user", true);
		this.needles.put("user not found", true);
		this.needles.put("user disabled", true);

		this.needles.put("([<a-zA-Z0-9]*) expired", true);

		this.needles.put("address unknown", true);
		this.needles.put("address expired", true);
		this.needles.put("address rejected", true);
		this.needles.put("address invalid", true);
		this.needles.put("unknown address", true);
		this.needles.put("unrouteable address", true);
		this.needles.put("rejected address", true);

		this.needles.put("account unknown", true);
		this.needles.put("account inactive", true);
		this.needles.put("unknown account", true);
		this.needles.put("terminated account", true);
		this.needles.put("account is disabled", true);
		this.needles.put("this account has been disabled or discontinued", true);

		this.needles.put("this user doesn't have a ([^ ]*) account", true);

		this.needles.put("invalid address", true);
		this.needles.put("invalid mailbox", true);
		this.needles.put("mailbox unavailable", true);
		this.needles.put("mailbox (temporarily )?disabled", true);

		this.needles.put("([^ ]*) cannot be resolved", true);
		this.needles.put("mailbox ([^ ]*) does not exist", true);

		this.needles.put("quota", true);
		this.needles.put("mailbox is full", true);
		this.needles.put("mailbox closed", true);
		this.needles.put("451 (has too )?much mail stored in his mailbox", true);

		this.needles.put("i.wasn't.able.to.establish.an.smtp.connection", true);
		
		this.needles.put("https://support.google.com/mail/?p=nosuchuser", true);

		this.needles.put("timeout connecting", true);

		this.needles.put("undefined mail delivery mode", true);
		this.needles.put("user '([^\']*)' has mail disabled.", true);

		this.needles.put("permission denied", true);
		this.needles.put("access denied", true);
		this.needles.put("550 administrative", true);

		this.needles.put("die eingegebene e-mail-adresse konnte nicht gefunden werden", true);
		this.needles.put("input/output error", true);
		this.needles.put("bad destination email address 'reject'", true);
		this.needles.put("relaying denied", true);
		this.needles.put("550 new address", true);
		this.needles.put("can't verify address", true);
		this.needles.put("mailbox and mailboxaccount do not exist", true);
		this.needles.put("das format der e-mail-adresse ist ungültig", true);
		this.needles.put("das postfach des empfängers ist voll", true);
		this.needles.put("der empfaenger ist syntaktisch scheinbar fehlerhaft", true);
		this.needles.put("der empfaenger wurde vom mailserver nicht akzeptiert", true);
		this.needles.put("dns lookup timed out", true);
		this.needles.put("timeout", true);
		this.needles.put("not authorized", true);
		this.needles.put("verification failed", true);
		this.needles.put("can't be verified", true);
		this.needles.put("no mailbox here by that name", true);
		this.needles.put("552 for explanation visit ([^ ]*)", true);
		this.needles.put("mail receiving disabled", true);
		this.needles.put("bad recipient address syntax", true);

		this.needles.put("mail loop", true);
		this.needles.put("loops back to myself", true);

		this.needles.put("delivery temporarily suspended", true);
		this.needles.put("aol will not accept delivery of this message", true);
		this.needles.put("message contains invalid header", true);

		// HISBUS
		this.needles.put("gruende: (.*) bitte entfernt die personendaten", false);
		this.needles.put("kein Student mehr", true);
		this.needles.put("Studium beendet", true);
		// this.needles.put("Einladungen kamen zu haeufig", true);

		// this.needles.put("", true);
		// this.needles.put("", true);

	}

	public static Analyzer getInstance() {
		return INSTANCE;
	}

	public Map<TYPE, Set<String>> analyze(final String sender, final String mailBody) {
		if (mailBody == null)
			return null;
		final Map<TYPE, Set<String>> back = new HashMap<TYPE, Set<String>>();
		final Set<String> recipients = this.retrieveMails(mailBody);
		final Set<String> reasons = this.retrieveReasons(mailBody);
		if (recipients != null) {
			// if (recipients.isEmpty())
			// LOGGER.info("no recipients found for {}", mailBody);
			back.put(TYPE.ADDRESS, recipients);
		}
		if (reasons != null) {
			// if (reasons.isEmpty())
			// LOGGER.info("no reasons found for {}", mailBody);
			back.put(TYPE.REASONS, reasons);
		}
		return back;
	}

	private Set<String> retrieveMailsHelper(final String patternStr, final String plainContent) {
		// final Pattern emailPattern =
		// Pattern.compile("([a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4})");
		final Pattern emailPattern = Pattern.compile(patternStr);
		final List<String> tmp = new ArrayList<String>();
		final Matcher matcher = emailPattern.matcher(plainContent);
		while (matcher.find()) {

			final String found = matcher.group(1);
			if (found != null) {
				String mail = found.trim();
				if (mail.toUpperCase().endsWith("HIS.DE")) {

				} else if (mail.toUpperCase().endsWith("HIS.EU")) {

				} else if (mail.toUpperCase().endsWith("DZHW.EU")) {

				} else if (mail.toUpperCase().endsWith("DZHW.DE")) {

				} else {
					mail = mail.toLowerCase();
					if (!mail.contains("@"))
						continue;
					if (mail.startsWith("<"))
						mail = mail.substring(2);
					if (mail.startsWith(">"))
						mail = mail.substring(1, mail.length() - 1);
					if (!mail.equals("name@example.com"))
						tmp.add(mail.toLowerCase());
				}

			}

		}
		final Set<String> back = new HashSet<String>();
		back.addAll(tmp);
		return back;
	}

	private Set<String> retrieveMails(final String plainContent) {
		Set<String> tmp = retrieveMailsHelper("To: ([^ ]*)", plainContent);
		if (tmp.isEmpty())
			tmp = retrieveMailsHelper("X-cloud-security-recipient: ([^ ]*)", plainContent);
		if (tmp.isEmpty())
			tmp = retrieveMailsHelper("([a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4})", plainContent);
		final Set<String> back = new HashSet<String>();
		back.addAll(tmp);
		return back;
	}

	private Set<String> retrieve(String haystack, final String needle, final boolean ignoreGroup) {
		final HashSet<String> back = new HashSet<String>();

		Pattern patt = null;

		patt = Pattern.compile(needle);
		try {
			final Matcher matcher = patt.matcher(haystack);
			final int groupCount = matcher.groupCount();
			while (matcher.find()) {
				if (groupCount == 0 || ignoreGroup) {
					final String found = matcher.group();
					if (found != null)
						back.add(found.trim());
				} else {
					for (int a = 1; a <= groupCount; a++) {
						final String found = matcher.group(a);
						if (found != null)
							back.add(found.trim());
					}
				}
			}
		} catch (final Exception exp) {
		}
		return back;
	}

	private Set<String> retrieveReasons(String plainContent) {
		plainContent = plainContent.toLowerCase();
		final List<String> tmp = new LinkedList<String>();
		final Set<String> needleSet = this.needles.keySet();
		if (needleSet != null) {
			final Iterator<String> needleIt = needleSet.iterator();
			while (needleIt.hasNext()) {
				if ((tmp != null) && (!tmp.isEmpty()))
					break;
				final String needle = needleIt.next();
				final boolean flag = this.needles.get(needle);

				final Set<String> reasons = retrieve(plainContent, needle, flag);
				tmp.addAll(reasons);
			}
		}
		if ((tmp == null) || (tmp.isEmpty())) {
		} else {
			final ListIterator<String> it = tmp.listIterator();
			while (it.hasNext()) {
				String reason = it.next();
				reason = reason.trim();
				if (reason.equals(""))
					it.remove();
			}
		}
		final Set<String> back = new LinkedHashSet<String>();
		back.addAll(tmp);
		return back;
	}
}
