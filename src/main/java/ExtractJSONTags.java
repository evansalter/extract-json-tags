import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.ide.CopyPasteManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.TextRange;

import java.awt.datatransfer.StringSelection;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExtractJSONTags extends AnAction {

    @Override
    public void update(AnActionEvent e) {
        final Project project = e.getProject();
        final Editor editor = e.getData(CommonDataKeys.EDITOR);

        boolean isVisible = false;
        if (project != null && editor != null) {
            String line = getLineAtCaret(editor);
            isVisible = isStructType(line);
        }
        e.getPresentation().setVisible(isVisible);
    }

    private int getCurrentLineNum(Editor editor) {
        return editor.getCaretModel().getPrimaryCaret().getLogicalPosition().line;
    }

    private String getLine(Document document, int start, int end) {
        return document.getText(new TextRange(start, end));
    }

    private String getLineAtLineNumber(Editor editor, int lineNum) {
        final Document document = editor.getDocument();
        int lineStartOffset = document.getLineStartOffset(lineNum);
        int lineEndOffset = document.getLineEndOffset(lineNum);
        String line = getLine(document, lineStartOffset, lineEndOffset);
        System.out.println("Got line: " + line);
        return line;
    }

    private String getLineAtCaret(Editor editor) {
        int lineNum = getCurrentLineNum(editor);
        return getLineAtLineNumber(editor, lineNum);
    }

    private boolean isStructType(String line) {
        Pattern p = Pattern.compile("type .* struct");
        return p.matcher(line).find();
    }

    protected String extractJSONTag(String line) {
        Pattern p = Pattern.compile("`.*json:\"(.*)\".*`");
        Matcher m = p.matcher(line);
        if (!m.find()) {
            return null;
        }
        String tag;
        try {
            tag = m.group(1);
        } catch (IndexOutOfBoundsException | IllegalStateException e) {
            System.out.printf("Error getting group: %s\n", e.getMessage());
            return null;
        }
        String[] parts = tag.split("[, ]");
        if (parts.length > 1) {
            return parts[0];
        }
        return tag;
    }

    private ArrayList<String> getLinesOfStruct(Editor editor) {
        int lineNumber = getCurrentLineNum(editor) + 1;
        String currentLine = getLineAtLineNumber(editor, lineNumber);
        ArrayList<String> lines = new ArrayList<>();
        while (!currentLine.contains("}")) {
            lines.add(currentLine);
            lineNumber++;
            currentLine = getLineAtLineNumber(editor, lineNumber);
        }

        return lines;
    }

    private JsonObject constructJSONObject(ArrayList<String> lines) {
        JsonObject jsonObject = new JsonObject();
        for (String line:lines) {
            String tag = extractJSONTag(line);
            if (tag != null) {
                jsonObject.addProperty(tag, "");
            }
        }
        return jsonObject;
    }

    private boolean checkJSONObject(JsonObject jsonObject) {
        if (jsonObject.keySet().size() == 0) {
            String msg = "No fields with JSON tags found.";
            Messages.showMessageDialog(msg, "Extract JSON Tags", Messages.getInformationIcon());
            return false;
        }
        return true;
    }

    private void copyJSONObjectToClipboard(Project project, JsonObject jsonObject) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        StringSelection data = new StringSelection(gson.toJson(jsonObject));
        CopyPasteManager.getInstance().setContents(data);

        Notification n = new Notification("Extract JSON Tags", "JSON string copied to clipboard.", "", NotificationType.INFORMATION);
        n.notify(project);
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        final Project project = e.getProject();
        final Editor editor = e.getData(CommonDataKeys.EDITOR);

        ArrayList<String> lines = getLinesOfStruct(editor);
        JsonObject jsonObject = constructJSONObject(lines);
        if (!checkJSONObject(jsonObject)) {
            return;
        }
        copyJSONObjectToClipboard(project, jsonObject);
    }
}