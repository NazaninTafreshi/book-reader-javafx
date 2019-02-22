/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bookapp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import javafx.concurrent.Task;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import static jdk.nashorn.internal.runtime.Context.printStackTrace;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

/**
 *
 * @author Nazanin
 */
public class Read extends Task<BookSearchResult> {

    private final BookSearchResult book;
    

    Read(BookSearchResult book) {
        this.book = book;
    }

    public BookSearchResult getBook() {
        return book;
    }

    @Override
    protected BookSearchResult call() throws Exception {
        try {
            URL searchurl1;

            searchurl1 = new URL("https://www.goodreads.com/book/show/" + book.getBookID() + "?format=xml&key=qc3UkMKGKtSGCPxvvr2lMQ");
            System.out.println("searchurl1 = " + searchurl1);
            DocumentBuilderFactory dbf1 = DocumentBuilderFactory.newInstance();
            DocumentBuilder db1 = dbf1.newDocumentBuilder();
            Document doc1 = db1.parse(searchurl1.openStream());
            NodeList elementsByTagName1 = doc1.getElementsByTagName("description");
            book.setDescription(elementsByTagName1.item(0).getTextContent());
            updateProgress(60, 100);
            NodeList elementsByTagName2 = doc1.getElementsByTagName("num_pages");
            book.setNum_pages(elementsByTagName2.item(0).getTextContent());
            updateProgress(70, 100);
            NodeList elementsByTagName3 = doc1.getElementsByTagName("publication_year");
            book.setPublication_year(elementsByTagName3.item(0).getTextContent());
            updateProgress(80, 100);
            NodeList elementsByTagName4 = doc1.getElementsByTagName("language_code");
            book.setLanguage_code(elementsByTagName4.item(0).getTextContent().toUpperCase());
            updateProgress(90, 100);
            NodeList elementsByTagName5 = doc1.getElementsByTagName("id");
            ArrayList<String> quotes = new ArrayList<>();

            URL url = null;
            URLConnection urlConn = null;
            try {
// Assume index.html is a default home page name
                url = new URL("https://www.goodreads.com/work/quotes/" + elementsByTagName5.item(1).getTextContent());
                urlConn = url.openConnection();
            } catch (Exception e) {
                System.out.println("Can't connect to the provided URL:"
                        + e.toString());
            }
            try (InputStreamReader inStream = new InputStreamReader(
                    urlConn.getInputStream(), "UTF8");
                    BufferedReader buff = new BufferedReader(inStream);) {
                buff.lines().filter((String t) -> t.contains("&ldquo;"))
                        .forEach(a -> {
                            quotes.add(a.replaceAll("\\s*&[rl]dquo;", ""));
                        });
                book.setQuotes(quotes);
                updateProgress(100, 100);

            } catch (Exception ioe) {
                System.out.println("Can't read from the Internet: "
                        + ioe.toString());
            }

        } catch (Exception ex) {
            printStackTrace(ex);
        }
        return book;

    }

    @Override
    protected void succeeded() {
        try {
            BookSearchResult results = this.get();
            //System.out.println("ptrResults = " + results);
            System.out.println("description = " + results.getDescription());
            //FXMLBookController.GUI.setResult(book);
            FXMLBookController.GUI.setResult(book);
            
            System.out.println("description2 = " + book.getDescription());
            System.out.println("cover = " + results.getCover());
            System.out.println("title = " + results.getTitle());
            System.out.println("author = " + results.getAuthorName());
            System.out.println("pages = " + results.getNum_pages());
            System.out.println("pub year = " + results.getPublication_year());


        } catch (InterruptedException | ExecutionException ex) {
            System.out.println("Read Done Get Exception " + ex.getMessage());
        }
    }

}
