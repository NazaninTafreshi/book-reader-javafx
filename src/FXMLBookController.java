/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bookapp;

import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.jsoup.Jsoup;

/**
 *
 * @author Nazanin
 */
public class FXMLBookController implements Initializable {

    ExecutorService executor = Executors.newSingleThreadExecutor();
    @FXML
    private TextField searchTextField, title, author, pages, language, published, rates;
    @FXML
    private ProgressIndicator progressbar;
    @FXML
    private TextArea description, quote;
    @FXML
    private ImageView cover;

    static FXMLBookController GUI;
    BookSearchResult book;

    @FXML
    private void searchButtonOnAction(ActionEvent event) {
        //System.out.println(searchTextField.getText());
        Search search = new Search(searchTextField.getText());
        progressbar.progressProperty().bind(search.progressProperty());
        executor.submit(search);
    }

    @FXML
    private void goToNextQuote(ActionEvent event) {

        quote.setText("\"" + Jsoup.parse(book.getQuotes().get(new Random().nextInt(book.getQuotes().size() - 1))).text() + "\"");
    }

    public void setResult(BookSearchResult book) {
        URL url;
        this.book = book;
        //System.out.println("boook" + book.getDescription());

        title.setText(book.getTitle());
        author.setText(book.getAuthorName());
        rates.setText(book.getRating() + "/5");
        pages.setText(book.getNum_pages());
        published.setText(book.getPublication_year());
        language.setText(book.getLanguage_code());
        cover.setImage(new Image(book.getCover()));

        description.setText("Story : " + Jsoup.parse(book.getDescription()).text());
        quote.setText("\"" + Jsoup.parse(book.getQuotes().get(new Random().nextInt(book.getQuotes().size() - 1))).text() + "\"");

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        GUI = this;
    }

}
