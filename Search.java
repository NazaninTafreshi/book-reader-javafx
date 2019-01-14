package bookapp;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author Nazanin
 */
public class Search extends Task<ArrayList<BookSearchResult>> {

    private final String bookName;
    private ArrayList<BookSearchResult> allResults = new ArrayList<>();

    private final static long MAX_PROGRESS_VALUE = 50;

    Search(String bookName) {
        this.bookName = bookName;
    }

    @Override
    protected ArrayList<BookSearchResult> call() throws Exception {
        URL searchurl;
        NodeList list[] = new NodeList[4];
        Node items[] = new Node[8];
        String forSearch = bookName.replaceAll(" ", "+");

        try {
            searchurl = new URL("https://www.goodreads.com/search/index.xml?key=qc3UkMKGKtSGCPxvvr2lMQ&q=" + forSearch);

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(searchurl.openStream());
            list[0] = doc.getElementsByTagName("work");
            list[1] = doc.getElementsByTagName("best_book");
            list[2] = doc.getElementsByTagName("image_url");
            list[3] = doc.getElementsByTagName("author");

            for (int k = 0; k < 10; k++) {
//                System.out.println("*****************************************************");
                String title = null, bookID = null, authorName = null, cover = null, rating = null;
                for (int i = 0; i < 4; i++) {
                    items[2 * i] = list[i].item(k);
                    if (items[2 * i].getNodeType() == Node.ELEMENT_NODE) {
                        Element bestBook = (Element) items[2 * i];
                        NodeList childNodes = bestBook.getChildNodes();
                        for (int j = 0; j < childNodes.getLength(); j++) {
                            items[(2 * i) + 1] = childNodes.item(j);
                            if (items[(2 * i) + 1].getNodeType() == Node.ELEMENT_NODE) {
                                Element name = (Element) items[(2 * i) + 1];
                                if (name.getTagName().equals("average_rating")) {
                                    updateProgress(10, MAX_PROGRESS_VALUE);
                                    rating = name.getTextContent();
//                                    System.out.println("average_rating  = " + name.getTextContent());
                                }
                                if (i == 1 && name.getTagName().equals("id")) {
                                    updateProgress(20, MAX_PROGRESS_VALUE);
                                    bookID = name.getTextContent();
//                                    System.out.println("best_book id = " + name.getTextContent());
                                }
                                if (name.getTagName().equals("title")) {
                                    updateProgress(30, MAX_PROGRESS_VALUE);
                                    title = name.getTextContent();
//                                    System.out.println("title = " + name.getTextContent());
                                }
                                if (name.getTagName().equals("image_url")) {
                                    updateProgress(40, MAX_PROGRESS_VALUE);
                                    cover = name.getTextContent();
//                                if (i == 3 && name.getTagName().equals("id")) {
//                                    System.out.println("author id = " + name.getTextContent());
                                }
                                if (name.getTagName().equals("name")) {
                                    updateProgress(50, MAX_PROGRESS_VALUE);
                                    authorName = name.getTextContent();
//                                    System.out.println("name  = " + name.getTextContent());
                                }
                            }
                        }
                    }
                }
                allResults.add(new BookSearchResult(title, bookID, authorName, cover, rating));
            }
//            System.out.println("k = " + allResults);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return allResults;
    }

    @Override
    protected void succeeded() {
        
        try {
            Read reader;
            ArrayList<BookSearchResult> get = this.getValue();
            System.out.println("get = " + get);
            for (BookSearchResult item : get) {
                Alert alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Dialog");
                alert.setHeaderText("Look, a Confirmation Dialog");
                alert.setContentText("Are you ok with this?\n" + item.toString());
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    // ... user chose OK
                    reader=new Read(item);
                    reader.run();
                    break;
                } else {
                    // ... user chose CANCEL or closed the dialog
                }

//                int showConfirmDialog = JOptionPane.showConfirmDialog(null, "Do You Mean " + item.toString() + "?", "What Do You Mean?", JOptionPane.YES_NO_OPTION);
//                if (showConfirmDialog == JOptionPane.YES_OPTION) {
////                    System.out.println("item = " + item);
//                    //pass selected item to the Read
////                    Read read = new Read(item);
//                    read.execute();
//                    break;
//                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
