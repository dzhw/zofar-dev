/*
 * SSH Client to communicate secured with remote server
 */
package eu.dzhw.zofar.management.comm.ssh;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.HostKey;
import com.jcraft.jsch.HostKeyRepository;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UserInfo;

/**
 * The Class SSHClient.
 */
public class SSHClient {

	/**
	 * The Class MyUserInfo.
	 */
	private class MyUserInfo implements UserInfo {

		/*
		 * (non-Javadoc)
		 *
		 * @see com.jcraft.jsch.UserInfo#getPassphrase()
		 */
		@Override
		public String getPassphrase() {
			LOGGER.error("MyUserInfo.getPassphrase not implemented yet");
			return null;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see com.jcraft.jsch.UserInfo#getPassword()
		 */
		@Override
		public String getPassword() {
			LOGGER.error("MyUserInfo.getPassword not implemented yet");
			return null;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see com.jcraft.jsch.UserInfo#promptPassphrase(java.lang.String)
		 */
		@Override
		public boolean promptPassphrase(final String arg0) {
			// TODO Auto-generated method stub
			return false;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see com.jcraft.jsch.UserInfo#promptPassword(java.lang.String)
		 */
		@Override
		public boolean promptPassword(final String arg0) {
			// TODO Auto-generated method stub
			return false;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see com.jcraft.jsch.UserInfo#promptYesNo(java.lang.String)
		 */
		@Override
		public boolean promptYesNo(final String arg0) {
			return true;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see com.jcraft.jsch.UserInfo#showMessage(java.lang.String)
		 */
		@Override
		public void showMessage(final String arg0) {
			LOGGER.info(arg0);
		}

	};
	
	public static class MyLogger implements com.jcraft.jsch.Logger {
		    static java.util.Hashtable name=new java.util.Hashtable();
		    static{
		      name.put(new Integer(DEBUG), "DEBUG: ");
		      name.put(new Integer(INFO), "INFO: ");
		      name.put(new Integer(WARN), "WARN: ");
		      name.put(new Integer(ERROR), "ERROR: ");
		      name.put(new Integer(FATAL), "FATAL: ");
		    }
		    public boolean isEnabled(int level){
		      return true;
		    }
		    public void log(int level, String message){
		      System.err.print(name.get(new Integer(level)));
		      System.err.println(message);
		    }
		}

	/** The Constant INSTANCE. */
	private static final SSHClient INSTANCE = new SSHClient();

	/** The Constant LOGGER. */
	final static Logger LOGGER = LoggerFactory.getLogger(SSHClient.class);

	/** The known hosts file. */
	private File knownHostsFile;

	/**
	 * Instantiates a new SSH client.
	 */
	private SSHClient() {
		super();
		JSch.setLogger(new MyLogger());
	}

	/**
	 * Gets the single instance of SSHClient.
	 *
	 * @return single instance of SSHClient
	 */
	public static synchronized SSHClient getInstance() {
		return INSTANCE;
	}

	/**
	 * Gets the known hosts file.
	 *
	 * @return the known hosts file
	 */
	private File getKnownHostsFile() {
		if (this.knownHostsFile == null) {
			final String dirPath = System.getProperty("user.home") + File.separator + ".zofar";
			final File dir = new File(dirPath);
			if (!dir.exists()) {
				try {
					FileUtils.forceMkdir(dir);
				} catch (final IOException e) {
					LOGGER.error("", e);
				}
			}
			this.knownHostsFile = new File(dir, "ssh_known_hosts");
			if (!this.knownHostsFile.exists()) {
				try {
					this.knownHostsFile.createNewFile();
				} catch (final IOException e) {
					LOGGER.error("", e);
				}
			}
		}
		return this.knownHostsFile;
	}

	/**
	 * Gets a list of kown hosts.
	 *
	 * @return the kown hosts
	 */
	public HostKey[] getKownHosts() {
		try {
			final JSch jsch = new JSch();
			jsch.setKnownHosts(getKnownHostsFile().getAbsolutePath());

			final HostKeyRepository hkr = jsch.getHostKeyRepository();
			final HostKey[] hks = hkr.getHostKey();
			if (hks != null) {
				LOGGER.info("Host keys in {}", hkr.getKnownHostsRepositoryID());
				for (int i = 0; i < hks.length; i++) {
					final HostKey hk = hks[i];
					LOGGER.info("{} {} {}", hk.getHost(), hk.getType(), hk.getFingerPrint(jsch));
				}
			}
			return hks;
		} catch (final Exception e) {
			LOGGER.error("", e);
		}
		return null;
	}

	/**
	 * Gets an secured console.
	 *
	 * @param server
	 *            the server
	 * @param user
	 *            the user
	 * @param pass
	 *            the pass
	 * @return the console
	 */
	public void getConsole(final String server, final String user, final String pass) {
		try {
			final JSch jsch = new JSch();
			jsch.setKnownHosts(getKnownHostsFile().getAbsolutePath());

			final Session session = jsch.getSession(user, server, 22);
			session.setPassword(pass);

			final UserInfo ui = new MyUserInfo();
			session.setUserInfo(ui);

			session.connect(30000); // making a connection with timeout.

			final Channel channel = session.openChannel("shell");
			channel.setInputStream(System.in);
			channel.setOutputStream(System.out);
			channel.connect(3 * 1000);
		} catch (final Exception e) {
			LOGGER.error("", e);
		}
	}

	private class DummyUserInfo implements UserInfo {

		final List<String> passwords;
		final Iterator<String> it;

		public DummyUserInfo(final List<String> passwords) {
			super();
			this.passwords = passwords;
			this.it = this.passwords.iterator();
		}

		@Override
		public String getPassphrase() {
			String back = null;
			if (it.hasNext()) {
				back = this.it.next();
			}
//			System.out.println("DUMMY USER getPassphrase : " + back);
			return back;
		}

		@Override
		public String getPassword() {
			String back = null;
			if (it.hasNext()) {
				back = this.it.next();
			}
//			System.out.println("DUMMY USER getPassword : " + back);
			return back;
		}

		@Override
		public boolean promptPassphrase(final String arg0) {
			return true;
		}

		@Override
		public boolean promptPassword(final String arg0) {
			return true;
		}

		@Override
		public boolean promptYesNo(final String arg0) {
			return true;
		}

		@Override
		public void showMessage(final String arg0) {
			System.out.println("DUMMY USER " + arg0);
		}

	};

	public Session connect(final String server, final String user, final ArrayList<String> passwords,
			final Map<String, String> identities) throws JSchException {
		return this.connectByHop(null, 0, 22, server, user, passwords, identities);
	}

	public Session connectByHop(final Session parent, final int localPort, final int remotePort, final String server,
			final String user, final ArrayList<String> passwords, final Map<String, String> identities)
			throws JSchException {
		final JSch jsch = new JSch();
		jsch.setKnownHosts(getKnownHostsFile().getAbsolutePath());

		if (identities != null) {
			for (final Map.Entry<String, String> identity : identities.entrySet()) {
				jsch.addIdentity(identity.getKey(), identity.getValue());
			}
		}
		Session session = null;
		String host = server;
		int port = 22;

		if (parent != null) {
			port = parent.setPortForwardingL(localPort, server, remotePort);
			host = "127.0.0.1";
		}
		session = jsch.getSession(user, host, port);

		session.setUserInfo(new DummyUserInfo(passwords));
		session.setHostKeyAlias(server);

		final Properties config = new Properties();
		config.put("StrictHostKeyChecking", "no");
		session.setConfig(config);
		session.connect();

//		System.out.println("The session has been established from port " + localPort + " to " + user + "@" + server
//				+ ":" + remotePort);
		return session;
	}

	public Session connectByProxy(final int localPort, final String proxyServer, final String proxyUser,
			final ArrayList<String> proxyPasswords, final Map<String, String> proxyIdentities,
			final String targetServer, final String targetUser, final ArrayList<String> targetPasswords,
			final Map<String, String> targetIdentities) throws JSchException, IOException {
		final JSch jsch = new JSch();
		jsch.setKnownHosts(getKnownHostsFile().getAbsolutePath());

		if (proxyIdentities != null) {
			for (final Map.Entry<String, String> identity : proxyIdentities.entrySet()) {
				jsch.addIdentity(identity.getKey(), identity.getValue());
			}
		}
		final Session proxySession = jsch.getSession(proxyUser, proxyServer, 22);
		proxySession.setUserInfo(new DummyUserInfo(proxyPasswords));
		proxySession.setHostKeyAlias(proxyServer);

		final Properties config = new Properties();
		config.put("StrictHostKeyChecking", "no");
		proxySession.setConfig(config);

		final int proxyPort = proxySession.setPortForwardingL(0, targetServer, 22);
		proxySession.connect();
		proxySession.openChannel("direct-tcpip");

		System.out.println(
				"Proxy Connection : " + proxyPort + ":" + proxySession.getHost() + ":" + proxySession.getPort());

		// create a session connected to port proxyPort on the local host.
		final Session secondSession = jsch.getSession(targetUser, "localhost", proxyPort);
		secondSession.setUserInfo(new DummyUserInfo(targetPasswords));
		secondSession.setConfig("StrictHostKeyChecking", "no");

		secondSession.connect();

		System.out.println("target Connection : " + secondSession.getHost() + ":" + secondSession.getPort());
		return secondSession;
	}

	public List<String> exec(final Session session, final String cmd) throws JSchException, IOException {
		final Channel c = session.openChannel("exec");
		final ChannelExec ce = (ChannelExec) c;
//		System.out.println(session.hashCode() + " send cmd : " + cmd);
		ce.setCommand(cmd);

		final InputStream in = ce.getInputStream();
		final OutputStream out = ce.getOutputStream();
		ce.setErrStream(System.err);

		ce.connect();

		final List<String> back = new ArrayList<String>();

		final BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		String line;
		while ((line = reader.readLine()) != null) {
			// System.out.println(line);
			back.add(line);
		}

		ce.disconnect();
		// session.disconnect();
		if (ce.getExitStatus() != 0) {
			throw new IOException("Command failed");
		}
		return back;
	}

	public void remotSudoCmd(final String server, final String user, final String pass, final String sudoPass,
			final String cmd) throws JSchException, IOException {
		Session session = null;
		ChannelExec ce = null;
		try {
			final JSch jsch = new JSch();
			jsch.setKnownHosts(getKnownHostsFile().getAbsolutePath());

			session = jsch.getSession(user, server, 22);
			session.setPassword(pass);
			session.setOutputStream(System.out);

			final Properties config = new Properties();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
			System.out.print("SSH Session connect... ");
			session.connect();
			System.out.println("done");
			final Channel c = session.openChannel("exec");
			ce = (ChannelExec) c;
			ce.setPty(true);
			ce.setCommand("sudo -S -p '' " + cmd);

			System.out.println("send Command : sudo -S -p '' " + cmd + " to " + server);

			final InputStream in = ce.getInputStream();
			final OutputStream out = ce.getOutputStream();
			ce.setErrStream(System.err);

			ce.connect();

			out.write((sudoPass + "\n").getBytes());
			out.flush();
			
			final StringBuffer result = new StringBuffer();
			// final BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			// String line;
			// while ((line = reader.readLine()) != null) {
			// System.out.println(line);
			// result.append(line+"\n");
			// }
			//
			// ce.disconnect();
			// int status = ce.getExitStatus();
			// session.disconnect();
			// System.out.println("Status : "+status+ " ( after : "+ce.getExitStatus()+")
			// Result : "+result.toString());
			// if (status != 0) {
			// throw new IOException("Command failed Status = "+status);
			// }

			byte[] tmp = new byte[1024];
			while (true) {
				while (in.available() > 0) {
					int i = in.read(tmp, 0, 1024);
					if (i < 0)	break;
					System.out.print(new String(tmp, 0, i));
					result.append(new String(tmp, 0, i));
				}
				if (ce.isClosed()) {
					break;
				}
				try {
					Thread.sleep(1000);
				} catch (Exception ee) {
				}
			}
			System.out.println("exit-status: " + ce.getExitStatus() +" Result : "+result.toString());
			if(ce.getExitStatus() == 1)throw new IOException(result.toString());
		} catch (Exception e) {
			throw new IOException("Failed to excecute Sudo Command",e);
		}
		finally {
			if(ce != null)ce.disconnect();
			if(session != null)session.disconnect();
		}

	}

	/**
	 * Secured copy to Server.
	 *
	 * @param server
	 *            the server
	 * @param user
	 *            the user
	 * @param pass
	 *            the pass
	 * @param from
	 *            the local source File
	 * @param toPath
	 *            the path to target dir at remote server
	 */
	public void scpTo(final String server, final String user, final String pass, final File from, final String toPath) {
		try {
			final JSch jsch = new JSch();
			
			jsch.setKnownHosts(getKnownHostsFile().getAbsolutePath());

			final Session session = jsch.getSession(user, server, 22);
			session.setPassword(pass);

			final UserInfo ui = new MyUserInfo();
			session.setUserInfo(ui);
			session.connect();

			final String lfile = from.getAbsolutePath();
//			final String rfile = toPath + File.separator + from.getName();
			final String rfile = toPath + "/" + from.getName();

			FileInputStream fis = null;
			try {

				// exec 'scp -t rfile' remotely
				String command = "scp -t " + rfile;
				final Channel channel = session.openChannel("exec");
				((ChannelExec) channel).setCommand(command);

				// get I/O streams for remote scp
				final OutputStream out = channel.getOutputStream();
				final InputStream in = channel.getInputStream();

				channel.connect();

				if (checkAck(in) != 0) {
					return;
				}

				final File _lfile = new File(lfile);

				// send "C0644 filesize filename", where filename should not
				// include '/'
				final long filesize = _lfile.length();
				command = "C0644 " + filesize + " ";
				if (lfile.lastIndexOf('/') > 0) {
					command += lfile.substring(lfile.lastIndexOf('/') + 1);
				} else {
					command += lfile;
				}
				command += "\n";
				out.write(command.getBytes());
				out.flush();
				if (checkAck(in) != 0) {
					return;
				}

				// send a content of lfile
				fis = new FileInputStream(lfile);
				final byte[] buf = new byte[1024];
				while (true) {
					final int len = fis.read(buf, 0, buf.length);
					if (len <= 0) {
						break;
					}
					out.write(buf, 0, len); // out.flush();
				}
				fis.close();
				fis = null;
				// send '\0'
				buf[0] = 0;
				out.write(buf, 0, 1);
				out.flush();
				if (checkAck(in) != 0) {
					return;
				}
				out.close();

				channel.disconnect();
				session.disconnect();
				return;
			} catch (final Exception e) {
				LOGGER.error("", e);
				try {
					if (fis != null) {
						fis.close();
					}
				} catch (final Exception ee) {
				}
			}
		} catch (final Exception e) {
			LOGGER.error("", e);
		}
	}

	public void scpTo(final Session session, final File from, final String toPath) {
		try {
			final String lfile = from.getAbsolutePath();
			final String rfile = toPath + File.separator + from.getName();

			FileInputStream fis = null;
			try {

				// exec 'scp -t rfile' remotely
				String command = "scp -t " + rfile;
				final Channel channel = session.openChannel("exec");
				((ChannelExec) channel).setCommand(command);

				// get I/O streams for remote scp
				final OutputStream out = channel.getOutputStream();
				final InputStream in = channel.getInputStream();

				channel.connect();

				if (checkAck(in) != 0) {
					return;
				}

				final File _lfile = new File(lfile);

				// send "C0644 filesize filename", where filename should not
				// include '/'
				final long filesize = _lfile.length();
				command = "C0644 " + filesize + " ";
				if (lfile.lastIndexOf('/') > 0) {
					command += lfile.substring(lfile.lastIndexOf('/') + 1);
				} else {
					command += lfile;
				}
				command += "\n";
				out.write(command.getBytes());
				out.flush();
				if (checkAck(in) != 0) {
					return;
				}

				// send a content of lfile
				fis = new FileInputStream(lfile);
				final byte[] buf = new byte[1024];
				while (true) {
					final int len = fis.read(buf, 0, buf.length);
					if (len <= 0) {
						break;
					}
					out.write(buf, 0, len); // out.flush();
				}
				fis.close();
				fis = null;
				// send '\0'
				buf[0] = 0;
				out.write(buf, 0, 1);
				out.flush();
				if (checkAck(in) != 0) {
					return;
				}
				out.close();

				channel.disconnect();
				// session.disconnect();
				return;
			} catch (final Exception e) {
				LOGGER.error("", e);
				try {
					if (fis != null) {
						fis.close();
					}
				} catch (final Exception ee) {
				}
			}
		} catch (final Exception e) {
			LOGGER.error("", e);
		}
	}

	/**
	 * Secured copy from Server.
	 *
	 * @param server
	 *            the server
	 * @param user
	 *            the user
	 * @param pass
	 *            the pass
	 * @param from
	 *            the remote source path
	 * @return the file
	 */
	public File scpFrom(final String server, final String user, final String pass, final String from) {
		FileOutputStream fos = null;
		try {
			final JSch jsch = new JSch();
			jsch.setKnownHosts(getKnownHostsFile().getAbsolutePath());

			final Session session = jsch.getSession(user, server, 22);

			session.setPassword(pass);

			final UserInfo ui = new MyUserInfo();
			session.setUserInfo(ui);
			session.connect();

			final File back = File.createTempFile(from.replaceAll(Pattern.quote(File.separator), "_") + "_", "_loaded");

			// exec 'scp -f rfile' remotely
			final String command = "scp -f " + from;
			final Channel channel = session.openChannel("exec");
			((ChannelExec) channel).setCommand(command);

			// get I/O streams for remote scp
			final OutputStream out = channel.getOutputStream();
			final InputStream in = channel.getInputStream();

			channel.connect();

			final byte[] buf = new byte[1024];

			// send '\0'
			buf[0] = 0;
			out.write(buf, 0, 1);
			out.flush();

			while (true) {
				final int c = checkAck(in);
				if (c != 'C') {
					break;
				}

				// read '0644 '
				in.read(buf, 0, 5);

				long filesize = 0L;
				while (true) {
					if (in.read(buf, 0, 1) < 0) {
						// error
						break;
					}
					if (buf[0] == ' ') {
						break;
					}
					filesize = ((filesize * 10L) + buf[0]) - '0';
				}

				// String file = null;
				for (int i = 0;; i++) {
					in.read(buf, i, 1);
					if (buf[i] == (byte) 0x0a) {
						// file = new String(buf, 0, i);
						break;
					}
				}

				// System.out.println("filesize="+filesize+", file="+file);

				// send '\0'
				buf[0] = 0;
				out.write(buf, 0, 1);
				out.flush();

				// read a content of rfile
				fos = new FileOutputStream(back.getAbsolutePath());
				int foo;
				while (true) {
					if (buf.length < filesize) {
						foo = buf.length;
					} else {
						foo = (int) filesize;
					}
					foo = in.read(buf, 0, foo);
					if (foo < 0) {
						// error
						break;
					}
					fos.write(buf, 0, foo);
					filesize -= foo;
					if (filesize == 0L) {
						break;
					}
				}
				fos.close();
				fos = null;

				if (checkAck(in) != 0) {
					System.exit(0);
				}

				// send '\0'
				buf[0] = 0;
				out.write(buf, 0, 1);
				out.flush();
			}
			channel.disconnect();
			session.disconnect();

			return back;
		} catch (final Exception e) {
			LOGGER.error("", e);
		}
		return null;
	}

	/**
	 * Check ack.
	 *
	 * @param in
	 *            the in
	 * @return the int
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private int checkAck(final InputStream in) throws IOException {
		final int b = in.read();
		// b may be 0 for success,
		// 1 for error,
		// 2 for fatal error,
		// -1
		if (b == 0) {
			return b;
		}
		if (b == -1) {
			return b;
		}

		if ((b == 1) || (b == 2)) {
			final StringBuffer sb = new StringBuffer();
			int c;
			do {
				c = in.read();
				sb.append((char) c);
			} while (c != '\n');
			if (b == 1) { // error
				LOGGER.error(sb.toString());
			}
			if (b == 2) { // fatal error
				LOGGER.error(sb.toString());
			}
		}
		return b;
	}

}
