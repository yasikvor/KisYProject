package com.kis.viewmodel;

import com.kis.constant.ConnectionObjectConstant;
import com.kis.constant.GrammarConstants;
import com.kis.domain.TreeNode;
import com.kis.loader.dbloader.exception.ObjectNotFoundException;
import com.kis.loader.dbloader.utils.DbObjectComparator;
import com.kis.model.*;
import com.kis.printer.PrinterManager;
import com.kis.repository.LayoutRepository;
import com.kis.repository.ProjectRepository;
import com.kis.service.ScriptGenerator;
import com.kis.service.TreeNodeService;
import com.kis.task.LoadNodeTask;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import org.xml.sax.SAXException;

import java.io.File;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class TreeNodeViewModel {

    private final SimpleBooleanProperty visibleIndicator = new SimpleBooleanProperty();

    private final SimpleObjectProperty<TreeItem<TreeNode>> root = new SimpleObjectProperty<>();

    private final SimpleObjectProperty<TreeItem<TreeNode>> selectedItem = new SimpleObjectProperty<>();

    private final SimpleObjectProperty<ObservableList<Map.Entry<String, String>>> tableView =
            new SimpleObjectProperty<>(FXCollections.emptyObservableList());

    private final SimpleStringProperty textArea = new SimpleStringProperty();

    private final SimpleStringProperty searchTextField = new SimpleStringProperty();

    private final SimpleBooleanProperty treeViewIsDisabled = new SimpleBooleanProperty();

    private final SimpleBooleanProperty createSqlMenuItemDisabled = new SimpleBooleanProperty();

    private final SimpleBooleanProperty reloadMenuItemDisabled = new SimpleBooleanProperty();

    private final SimpleBooleanProperty loadAllDatabaseMenuItemDisabled = new SimpleBooleanProperty();

    private final SimpleBooleanProperty tableVisible = new SimpleBooleanProperty();

    private final SimpleObjectProperty<javafx.scene.Node> script = new SimpleObjectProperty<>();

    public TreeNodeViewModel() {


        root.addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                ViewModelRepository.getRootViewModel().saveProjectDisabledProperty().set(true);
                ViewModelRepository.getRootViewModel().closeProjectDisabledProperty().set(true);
                ViewModelRepository.getRootViewModel().newProjectDisabledProperty().set(false);
                ViewModelRepository.getRootViewModel().openProjectDisabledProperty().set(false);
            } else {
                ViewModelRepository.getRootViewModel().saveProjectDisabledProperty().set(false);
                ViewModelRepository.getRootViewModel().closeProjectDisabledProperty().set(false);
                ViewModelRepository.getRootViewModel().newProjectDisabledProperty().set(true);
                ViewModelRepository.getRootViewModel().openProjectDisabledProperty().set(true);

            }
        });


        selectedItemProperty().addListener(((observable, oldValue, newValue) -> {

            if (newValue == null) {
                tableVisible.set(false);
                script.set(null);
                return;

            }
            if (ConnectionModel.getConnection() == null && newValue.getValue().getAttribute(ConnectionObjectConstant.IS_LOADED) == null && DbObjectComparator.isDbObject(newValue.getValue())) {
                tableVisible.set(false);
                createSqlMenuItemDisabled.set(true);

            } else {
                updateTree(newValue);

                boolean canBePrinted = DbObjectComparator.hasSql(newValue.getValue()) || newValue.getValue().is(GrammarConstants.NodeNames.SCHEMA);
                boolean canBeReloaded = !canBeReloaded();
                ViewModelRepository.getRootViewModel().createSqlDisabledProperty().set(canBePrinted);
                ViewModelRepository.getRootViewModel().reloadDisabledProperty().set(canBeReloaded);

                createSqlMenuItemDisabled.set(!canBePrinted);
                reloadMenuItemDisabled.set(canBeReloaded);

                if (newValue.getValue().getAttributes() != null) {
                    tableViewProperty().set(FXCollections.observableArrayList(filteredAttributes(newValue.getValue().getAttributes()).entrySet()));
                }
                tableVisible.set(haveProperties());
            }


        }));
    }

    private Map<String, String> filteredAttributes(Map<String, String> attributes) {
        return attributes.entrySet().stream().
                filter(isDatabaseAttribute()).
                collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private Predicate<Map.Entry<String, String>> isDatabaseAttribute() {
        return attr ->
                !attr.getKey().equals(ConnectionObjectConstant.IS_EXPANDED) &&
                !attr.getKey().equals(ConnectionObjectConstant.IS_SELECTED) &&
                !attr.getKey().equals(ConnectionObjectConstant.IS_LOADED);
    }

    public void saveProject(File file) {
        if (NodeModel.getRoot() == null) return;
        ProjectRepository.save(NodeModel.getRootTreeItem(), NodeModel.getSelectedItem(), file);
    }

    public void openProject(File file) throws SAXException, SQLException {
        TreeItem<TreeNode> root = ProjectRepository.open(file);
        this.selectedItem.set(NodeModel.getSelectedItem());
        NodeModel.setRootTreeItem(root);
        this.setRoot(root);

    }

    public void search() {
        if (NodeModel.getRoot() == null) return;
        setRoot(TreeNodeService.search(searchTextField.get()));
    }

    public void loadFullDatabase() {
        treeViewIsDisabled.set(true);
        visibleIndicator.set(true);
        Task task = new LoadDbTask();
        task.setOnSucceeded(event -> {

            Platform.runLater(() -> root.set(NodeModel.getRootTreeItem()));
            treeViewIsDisabled.set(false);
            visibleIndicator.set(false);

        });
        ExecutorService service = Executors.newSingleThreadExecutor();

        service.submit(task);
    }

    public void saveScript(File file) {
        if (!selectedItem.get().getValue().is(GrammarConstants.NodeNames.SCHEMA))
            ScriptGenerator.printScript(selectedItem.get(), file);
        else {
            treeViewIsDisabled.set(true);
            visibleIndicator.set(true);
            Task task = new LoadDbTask();
            task.setOnSucceeded(event -> {

                ScriptGenerator.printScript(NodeModel.getRootTreeItem(), file);
                Platform.runLater(() -> root.set(NodeModel.getRootTreeItem()));
                treeViewIsDisabled.set(false);
                visibleIndicator.set(false);

            });
            ExecutorService service = Executors.newSingleThreadExecutor();

            service.submit(task);

        }
    }

    public void createRoot() {
        this.setRoot(NodeModel.getRootTreeItem());
    }

    public void reload() throws ObjectNotFoundException {

        ReloadNodeTask task = new ReloadNodeTask();
        ExecutorService service = Executors.newSingleThreadExecutor();

        service.submit(task);

    }

    private void updateTree(TreeItem<TreeNode> selectedItem) {

        if (selectedItem.getValue().getAttribute(ConnectionObjectConstant.IS_LOADED) != null) {
            printColoredScript();
        }
        if (selectedItem.getValue().getAttribute(ConnectionObjectConstant.IS_LOADED) == null &&
                !DbObjectComparator.isDbObject(selectedItem.getValue()) &&
                !DbObjectComparator.isCategory(selectedItem.getValue()))
            printColoredScript();
        if (!selectedItem.getValue().getChildren().isEmpty() && selectedItem.getValue().getChildren().get(0).getType().equals("Expanding...")) {

            selectedItem.getValue().addAttribute(ConnectionObjectConstant.IS_LOADED, "TRUE");
            treeViewIsDisabled.set(true);
            selectedItem.getChildren().get(0).getValue().setType("Expanding...");
            visibleIndicator.set(true);
            Task task = new LoadNodeTask(selectedItem);
            task.setOnSucceeded(event -> {
                printColoredScript();
                treeViewIsDisabled.set(false);
                visibleIndicator.set(false);

            });
            ExecutorService service = Executors.newSingleThreadExecutor();
            service.submit(task);

        }


    }

    public void closeProject() throws SQLException {
        ViewModelRepository.getRootViewModel().reloadDisabledProperty().set(true);
        ViewModelRepository.getRootViewModel().createSqlDisabledProperty().set(true);
        LayoutRepository.getMainStage().setTitle("KisYProject");
        ViewModelRepository.getRootViewModel().loadAllDatabaseDisabledProperty().set(true);
        ViewModelRepository.getTreeNodeViewModel().loadAllDatabaseMenuItemDisabledProperty().set(true);
        NodeModel.close();
        ConnectionModel.close();
        ProjectModel.setProjectFile(null);
        textArea.set("");
        tableView.set(null);
        tableVisible.set(false);
        root.set(null);
        selectedItem.set(null);
    }

    private boolean haveProperties() {
        return !tableView.get().isEmpty();
    }

    public void deleteCurrentItem() {
        TreeNodeService.delete(selectedItem.get());
    }

    private boolean canBeReloaded() {
        if (ConnectionModel.getConnection() == null) return false;

        TreeNode selectedTreeNode = selectedItem.get().getValue();
        return DbObjectComparator.isDbObject(selectedTreeNode) ||
                DbObjectComparator.isCategory(selectedTreeNode) ||
                selectedTreeNode.is(GrammarConstants.NodeNames.SCHEMA);

    }

    private void setRoot(TreeItem<TreeNode> tree) {
        this.root.set(tree);
        root.get().addEventHandler(TreeItem.branchExpandedEvent(), (TreeItem.TreeModificationEvent<TreeNode> event) -> {

            if (DbObjectComparator.isDbObject(event.getTreeItem().getValue()) && event.getTreeItem().getValue().getAttribute(ConnectionObjectConstant.IS_LOADED) == null)
                try {
                    changeSelection(event.getTreeItem());
                } catch (SQLException e) {
                    if (enterPassword())
                        selectedItemProperty().set(event.getTreeItem());
                    else
                        event.getTreeItem().setExpanded(false);

                }
        });

    }

    public void resetFilter() {
        if (NodeModel.getRoot() == null)
            return;
        TreeNodeService.resetFilter();
        setRoot(NodeModel.getRootTreeItem());
    }

    public void changeSelection(TreeItem<TreeNode> newSelectedItem) throws SQLException {
        if (newSelectedItem == null)
            return;
        if (ConnectionModel.getConnection() == null &&
                newSelectedItem.getValue().getAttribute(ConnectionObjectConstant.IS_LOADED) == null &&
                DbObjectComparator.isDbObject(newSelectedItem.getValue()))
            throw new SQLException();
        selectedItemProperty().set(newSelectedItem);
    }

    public SimpleObjectProperty<TreeItem<TreeNode>> selectedItemProperty() {
        return selectedItem;

    }

    public boolean enterPassword() {

        ViewModelRepository.getConnectionViewModel().serverTextAreaProperty().set(ConnectionModel.getServer());
        ViewModelRepository.getConnectionViewModel().portTextAreaProperty().set(ConnectionModel.getPort());
        ViewModelRepository.getConnectionViewModel().schemaTextAreaProperty().set(ConnectionModel.getSchema());
        ViewModelRepository.getConnectionViewModel().userTextAreaProperty().set(ConnectionModel.getUser());

        ViewModelRepository.getConnectionViewModel().serverDisableProperty().set(true);
        ViewModelRepository.getConnectionViewModel().portDisableProperty().set(true);
        ViewModelRepository.getConnectionViewModel().schemaDisableProperty().set(true);
        ViewModelRepository.getConnectionViewModel().userDisableProperty().set(true);

        LayoutRepository.getConnectionLayout().showAndWait();

        ViewModelRepository.getConnectionViewModel().serverDisableProperty().set(false);
        ViewModelRepository.getConnectionViewModel().portDisableProperty().set(false);
        ViewModelRepository.getConnectionViewModel().schemaDisableProperty().set(false);
        ViewModelRepository.getConnectionViewModel().userDisableProperty().set(false);

        return ConnectionModel.getConnection() != null;
    }

    public SimpleObjectProperty<TreeItem<TreeNode>> rootProperty() {
        return root;
    }

    public SimpleObjectProperty<ObservableList<Map.Entry<String, String>>> tableViewProperty() {
        return tableView;
    }

    public SimpleStringProperty textAreaProperty() {
        return textArea;
    }

    public SimpleStringProperty searchTextFieldProperty() {
        return searchTextField;
    }

    public SimpleBooleanProperty visibleIndicatorProperty() {
        return visibleIndicator;
    }

    public SimpleBooleanProperty treeViewIsDisabledProperty() {
        return treeViewIsDisabled;
    }

    public SimpleBooleanProperty tableVisibleProperty() {
        return tableVisible;
    }

    public SimpleObjectProperty<javafx.scene.Node> scriptProperty() {
        return script;
    }

    public SimpleBooleanProperty loadAllDatabaseMenuItemDisabledProperty() {
        return loadAllDatabaseMenuItemDisabled;
    }

    private class LoadDbTask extends Task<Void> {

        @Override
        protected Void call() throws SQLException {

            NodeModel.setRootTreeItem((TreeNodeService.loadAllDatabase()));
            NodeModel.getRootTreeItem().addEventHandler(TreeItem.branchExpandedEvent(), (TreeItem.TreeModificationEvent<TreeNode> event) -> {

                if (DbObjectComparator.isDbObject(event.getTreeItem().getValue()) && event.getTreeItem().getValue().getAttribute(ConnectionObjectConstant.IS_LOADED) == null)
                    selectedItemProperty().set(event.getTreeItem());
            });
            return null;
        }
    }

    private void printColoredScript() {

        TextFlow textFlow = new TextFlow();
        TreeNode object = selectedItem.get().getValue();
        if(DbObjectComparator.hasSql(object)) {
            String sql = PrinterManager.print(object);
            textFlow.getChildren().addAll(createTextFlowTexts(sql));
        }
        script.set(textFlow);

    }

    private List<Text> createTextFlowTexts(String sql) {
        ArrayList<String> mysqlReservedWordsList = Collections.list(ResourceBundle.getBundle("com/kis/reservedWords", Locale.getDefault()).getKeys());
        List<Text> texts = new ArrayList<>();
        Pattern pattern1 = Pattern.compile("([^`\"]*)([`\"][^`\"]+[`\"])([^`\"]*)");

        Matcher matcher1 = pattern1.matcher(sql);
        Pattern pattern2 = Pattern.compile("([\\W]*)([\\w]+)([\\W]*)");
        boolean find = false;
        while (matcher1.find()) {
            if (!find) {
                find = true;
            }
            Matcher matcher2 = pattern2.matcher(matcher1.group(1));
            verifyAndAddReservedWords(matcher2, texts, mysqlReservedWordsList);
            texts.add(createColoredText(matcher1.group(2), Color.GREEN));

            Matcher matcher3 = pattern2.matcher(matcher1.group(3));
            verifyAndAddReservedWords(matcher3, texts, mysqlReservedWordsList);
        }
        if (!find) {
            StringBuffer sb = new StringBuffer();
            matcher1.appendTail(sb);
            if (!sb.toString().isEmpty()) {
                Matcher matcher4 = pattern2.matcher(sb.toString());
                verifyAndAddReservedWords(matcher4, texts, mysqlReservedWordsList);
            }
        }
        return texts;

    }

    private void verifyAndAddReservedWords(Matcher matcher, List<Text> texts, ArrayList<String> mysqlReservedWordsList) {
        StringBuffer sb = new StringBuffer();
        boolean find = false;
        while (matcher.find()) {
            if (!find) {
                find = true;
            }
            if (matcher.group(1) != null && !matcher.group(1).isEmpty()) {
                texts.add(createColoredText(matcher.group(1), Color.BLACK));
            }
            if (mysqlReservedWordsList.contains(matcher.group(2).toUpperCase())) {
                Text text = createColoredText(matcher.group(2), Color.BLUE);
                text.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
                texts.add(text);
            } else {
                texts.add(createColoredText(matcher.group(2), Color.BLACK));
            }
            if (matcher.group(3) != null && !matcher.group(3).isEmpty()) {
                texts.add(createColoredText(matcher.group(3), Color.BLACK));
            }
        }
        if (!find) {
            matcher.appendTail(sb);
            if (!sb.toString().isEmpty()) {
                texts.add(createColoredText(sb.toString(), Color.BLACK));
            }
        }
    }

    private Text createColoredText(String s, Color color) {
        Text text = new Text(s);
        text.setFill(color);
        return text;
    }

    private class ReloadNodeTask extends Task<Void> {

        @Override
        protected Void call() throws Exception {
            treeViewIsDisabled.set(true);
            visibleIndicator.set(true);
            TreeNodeService.reload(selectedItem.get());
            selectedItem.get().setExpanded(true);


            return null;
        }

        @Override
        protected void succeeded() {
            treeViewIsDisabled.set(false);
            visibleIndicator.set(false);
        }
    }
}
