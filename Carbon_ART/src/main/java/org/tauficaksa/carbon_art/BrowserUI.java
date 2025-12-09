package org.tauficaksa.carbon_art;


import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.ToolBar;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;


import java.util.List;
public class BrowserUI {
    static final String HOME = "https://www.google.com";

    public void start(Stage stage) {
        stage.setTitle("Carbon ART - Basic (Multi-file)");

        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.ALL_TABS);

        ToolBar toolBar = new ToolBar();
        Button backBtn = new Button("←");
        Button forwardBtn = new Button("→");
        Button reloadBtn = new Button("⟳");
        Button homeBtn = new Button("🏠");
        TextField addressBar = new TextField();
        Button goBtn = new Button("Go");
        Button newTabBtn = new Button("+");
        Button bookmarkBtn = new Button("☆");
        MenuButton bookmarksMenu = new MenuButton("Bookmarks");

        toolBar.getItems().addAll(backBtn, forwardBtn, reloadBtn, homeBtn, addressBar, goBtn, newTabBtn, bookmarkBtn, bookmarksMenu);

        BorderPane root = new BorderPane();
        root.setTop(toolBar);
        root.setCenter(tabPane);

        BookmarkManager bm = new BookmarkManager();
        List<String> bookmarks = bm.loadBookmarks();
        BookmarkManager.updateBookmarksMenu(bookmarksMenu, bookmarks, tabPane);

        // First tab
        Tab first = BrowserTab.createBrowserTab(HOME, tabPane, addressBar);
        tabPane.getTabs().add(first);
        tabPane.getSelectionModel().select(first);

        newTabBtn.setOnAction(e -> {
            Tab t = BrowserTab.createBrowserTab(HOME, tabPane, addressBar);
            tabPane.getTabs().add(t);
            tabPane.getSelectionModel().select(t);
        });

        goBtn.setOnAction(e -> BrowserTab.loadAddress(tabPane, addressBar.getText()));
        addressBar.setOnKeyPressed(k -> {
            if (k.getCode() == KeyCode.ENTER) BrowserTab.loadAddress(tabPane, addressBar.getText());
        });

        homeBtn.setOnAction(e -> BrowserTab.loadAddress(tabPane, HOME));
        reloadBtn.setOnAction(e -> BrowserTab.getEngine(tabPane).ifPresent(WebEngine::reload));
        backBtn.setOnAction(e -> BrowserTab.getEngine(tabPane).ifPresent(engine -> {
            var history = engine.getHistory();
            if (history.getCurrentIndex() > 0) history.go(-1);
        }));
        forwardBtn.setOnAction(e -> BrowserTab.getEngine(tabPane).ifPresent(engine -> {
            var history = engine.getHistory();
            if (history.getCurrentIndex() < history.getEntries().size() - 1) history.go(1);
        }));

        bookmarkBtn.setOnAction(e -> {
            BrowserTab.getEngine(tabPane).ifPresent(engine -> {
                String url = engine.getLocation();
                if (!bookmarks.contains(url)) {
                    bookmarks.add(url);
                    bm.saveBookmarks(bookmarks);
                    BookmarkManager.updateBookmarksMenu(bookmarksMenu, bookmarks, tabPane);
                }
            });
        });

        tabPane.getSelectionModel().selectedItemProperty().addListener((obs, oldT, newT) -> {
            if (newT != null && newT.getContent() instanceof WebView) {
                WebView view = (WebView) newT.getContent();
                addressBar.setText(view.getEngine().getLocation());
            }
        });

        Scene scene = new Scene(root, 1000, 700);
        stage.setScene(scene);
        stage.show();
    }
}