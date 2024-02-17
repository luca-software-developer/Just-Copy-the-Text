package justcopythetext;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.rtf.RTFEditorKit;
import org.jsoup.Jsoup;

/**
 * La classe {@code JustCopyTheTextController} rappresenta il controller MVC
 * dell'applicazione JavaFX.
 */
public class JustCopyTheTextController implements Initializable {

    @FXML
    private Button btnRemoveFormatting;

    private Clipboard clipboard;

    /**
     * Metodo {@code initialize} dell'applicazione JavaFX.
     *
     * @param url URL utilizzata per risolvere i path relativi.
     * @param rb Risorse utilizzate per localizzare l'oggetto radice.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        clipboard = Clipboard.getSystemClipboard();
    }

    @FXML
    private void removeFormatting(ActionEvent actionEvent) throws IOException, BadLocationException {
        if (clipboard.hasHtml()) {
            final ClipboardContent clipboardContent = new ClipboardContent();
            clipboardContent.putString(Jsoup.parse(clipboard.getHtml()).text());
            clipboard.setContent(clipboardContent);
        } else if (clipboard.hasRtf()) {
            final RTFEditorKit rtfEditorKit = new RTFEditorKit();
            final Document document = rtfEditorKit.createDefaultDocument();
            final ClipboardContent clipboardContent = new ClipboardContent();
            rtfEditorKit.read(new ByteArrayInputStream(clipboard.getRtf().getBytes()), document, 0);
            clipboardContent.putString(document.getText(0, document.getLength()));
            clipboard.setContent(clipboardContent);
        }
    }

}
