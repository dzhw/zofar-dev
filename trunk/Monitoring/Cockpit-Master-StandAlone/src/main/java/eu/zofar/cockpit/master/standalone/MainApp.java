package eu.zofar.cockpit.master.standalone;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import eu.zofar.cockpit.service.impl.Client;
import eu.zofar.cockpit.service.transfer.ClientTransfer;

public class MainApp {

	public static void main(String[] args) {
		final Client client = new Client();
		ClientTransfer status = null;
		try {
			status = client.status();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (status != null) {
			// final DateFormat feedbackFormat = new
			// SimpleDateFormat("dd.MM.yyyy");
			final DateFormat feedbackParseFormat = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			final DateFormat feedbackOutputFormat = new SimpleDateFormat(
					"dd.MM.yyyy");
			final DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss z");
			System.out.println("Timestamp : "
					+ df.format(status.getTimestamp()));
			System.out.println("Finished : " + status.getFinished().intValue());
			System.out.println("Participated : "
					+ status.getParticipated().intValue());
			System.out.println("Exit Pages : ");
			final String[][] exits = status.getExitPages();
			for (final String[] exit : exits) {
				System.out.println("\t" + exit[0] + " = " + exit[1]);
			}

//			// TextClient textClient = TextClient.getInstance();
//			try {
//				// WriterDocument doc = textClient.createDocument();
//
//				final Feedback feedback = status.getFeedback();
//
//				Map<Timestamp, List<Entry>> tmp = feedback.getData();
//
//				final List<String> columnLabels = new ArrayList<String>();
//				int rowCount = 0;
//				for (final Map.Entry<Timestamp, List<Entry>> entry : tmp
//						.entrySet()) {
//					final List<Entry> tokens = entry.getValue();
//					rowCount = Math.max(rowCount, tokens.size());
//					Date date = feedbackParseFormat.parse(entry.getKey()
//							.toString());
//					columnLabels.add(feedbackOutputFormat.format(date));
//				}
//
//				OdfTextElement[][] data = new OdfTextElement[rowCount][columnLabels
//						.size()];
//
//				final List<String> invited = new ArrayList<String>();
//				final List<String> finished = new ArrayList<String>();
//				int colIndex = 0;
//				for (final Map.Entry<Timestamp, List<Entry>> entry : tmp
//						.entrySet()) {
//					int rowIndex = 0;
//					final List<Entry> tokens = entry.getValue();
//					for (final Entry token : tokens) {
//						String content = token.getToken();
//						Color color = Color.WHITE;
//						if (token.getStatus().equals(Feedback.Status.INVITED)) {
//
//							if (invited.contains(token.getToken())) {
//								content = "";
//							} else {
//								color = Color.BLUE;
//								invited.add(token.getToken());
//							}
//						} else if (token.getStatus().equals(
//								Feedback.Status.PARTICIPATED)) {
//							color = Color.RED;
//						} else if (token.getStatus().equals(
//								Feedback.Status.FINISHED)) {
//
//							if (finished.contains(token.getToken())) {
//								content = "";
//							} else {
//								color = Color.GREEN;
//								finished.add(token.getToken());
//							}
//						}
//
//						data[rowIndex][colIndex] = new OdfColoredText(content,
//								color);
//						rowIndex++;
//					}
//					colIndex++;
//				}
//
//				for (final String label : columnLabels) {
//					System.out.print(label + "\t");
//				}
//				System.out.println();
//				for (final OdfTextElement[] row : data) {
//					for (final OdfTextElement item : row) {
//
//						System.out.print(item.getContent() + "\t");
//					}
//					System.out.println();
//				}
//
//				// textClient.addTable(doc, "Feedback", columnLabels.toArray(new
//				// String[0]), data);
//				// textClient.saveDocument(doc, "Feedback.odf");
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
		}
		System.exit(1);
	}

}
