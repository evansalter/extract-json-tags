import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExtractJSONTags extends AnAction {
    public ExtractJSONTags() {
        super("Hello");
    }

    @Override
    public void update(AnActionEvent e) {
        final Project project = e.getProject();
        final Editor editor = e.getData(CommonDataKeys.EDITOR);

        e.getPresentation().setVisible(project != null && editor != null);
    }

    int getLineNum(Editor editor) {
        return editor.getCaretModel().getPrimaryCaret().getLogicalPosition().line;
    }

    String getLine(Document document, int start, int end) {
        return document.getText(new TextRange(start, end));
    }

    String getLineAtLineNumber(Editor editor, int lineNum) {
        final Document document = editor.getDocument();
        int lineStartOffset = document.getLineStartOffset(lineNum);
        int lineEndOffset = document.getLineEndOffset(lineNum);
        String line = getLine(document, lineStartOffset, lineEndOffset);
        System.out.println("Got line: " + line);
        return line;
    }

    String getLineAtCaret(Editor editor) {
        int lineNum = getLineNum(editor);
        return getLineAtLineNumber(editor, lineNum);
    }

    boolean isStructType(String line) {
        Pattern p = Pattern.compile("type (.*) struct");
        return p.matcher(line).matches();
    }

    String extractJSONTag(String line) {
        Pattern p = Pattern.compile("`.*json:\"(.*)\".*`");
        Matcher m = p.matcher(line);
        try {
            return m.group(0);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    public void actionPerformed(AnActionEvent e) {
        final Editor editor = e.getData(CommonDataKeys.EDITOR);

        int lineNumber = getLineNum(editor) + 1;
        String currentLine = getLineAtLineNumber(editor, lineNumber);
        ArrayList<String> lines = new ArrayList<>();
        while (!currentLine.contains("}")) {
            lines.add(currentLine);
            lineNumber++;
            currentLine = getLineAtLineNumber(editor, lineNumber);
        }

        for (String line:lines) {
            String tag = extractJSONTag(line);
            if (tag != null) {

            }
        }
    }
}