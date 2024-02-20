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
import javax.swing.text.rtf.RTFEditorKit;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.jsoup.select.NodeTraversor;
import org.jsoup.select.NodeVisitor;

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
        final ClipboardContent clipboardContent = new ClipboardContent();
        if (clipboard.hasHtml() || clipboard.hasRtf()) {
            clipboardContent.putString(
                    clipboard.hasHtml()
                    ? removeFormattingHTML(clipboard.getHtml())
                    : removeFormattingRTF(clipboard.getRtf()));
            clipboard.setContent(clipboardContent);
        }
    }

    private String removeFormattingHTML(String html) {
        FormattingVisitor formatter = new FormattingVisitor();
        NodeTraversor.traverse(formatter, Jsoup.parse(html));
        return formatter.toString();
    }

    private String removeFormattingRTF(String rtf) throws IOException, BadLocationException {
        final RTFEditorKit rtfEditorKit = new RTFEditorKit();
        final javax.swing.text.Document document = rtfEditorKit.createDefaultDocument();
        rtfEditorKit.read(new ByteArrayInputStream(rtf.getBytes()), document, 0);
        return document.getText(0, document.getLength());
    }

    private static class FormattingVisitor implements NodeVisitor {

        private final StringBuilder stringBuilder;

        public FormattingVisitor() {
            stringBuilder = new StringBuilder();
        }

        @Override
        public void head(Node node, int depth) {
            if (node instanceof TextNode) {
                stringBuilder.append(((TextNode) node).text());
            } else if (Tag.valueOf(node.nodeName()).isBlock()) {
                if (!node.nodeName().equals("li")) {
                    stringBuilder.append("\n");
                }
            }
        }

        @Override
        public void tail(Node node, int depth) {
            if (Tag.valueOf(node.nodeName()).isBlock()) {
                stringBuilder.append("\n");
            }
        }

        @Override
        public String toString() {
            return stringBuilder.toString().trim();
        }

    }

}
