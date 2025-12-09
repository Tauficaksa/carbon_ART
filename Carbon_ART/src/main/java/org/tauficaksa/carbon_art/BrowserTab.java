package org.tauficaksa.carbon_art;



import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;


import java.util.*;


public class BrowserTab {


    public static Tab createBrowserTab(String url, TabPane tabPane, TextField addressBar) {
        WebView webView = new WebView();
        WebEngine engine = webView.getEngine();
        engine.load(url);


        Tab tab = new Tab("New Tab");
        tab.setContent(webView);


        engine.locationProperty().addListener((obs, oldLoc, newLoc) -> {
            tab.setText(Utils.shortTitle(newLoc));
            if (tabPane.getSelectionModel().getSelectedItem() == tab) {
                addressBar.setText(newLoc);
            }
        });


        engine.titleProperty().addListener((obs, old, title) -> {
            if (title != null && !title.isBlank()) tab.setText(Utils.truncate(title, 30));
        });


        tab.setOnClosed(e -> {
            if (tabPane.getTabs().isEmpty()) {
                Tab t = createBrowserTab(BrowserUI.HOME, tabPane, addressBar);
                tabPane.getTabs().add(t);
                tabPane.getSelectionModel().select(t);
            }
        });


        return tab;
    }


    public static Optional<WebEngine> getEngine(TabPane tabPane) {
        var sel = tabPane.getSelectionModel().getSelectedItem();
        if (sel == null) return Optional.empty();
        if (sel.getContent() instanceof WebView) {
            return Optional.of(((WebView) sel.getContent()).getEngine());
        }
        return Optional.empty();
    }


    public static void loadAddress(TabPane tabPane, String text) {
        if (text == null || text.isBlank()) return;
        String url = Utils.normalizeUrl(text);
        getEngine(tabPane).ifPresent(engine -> engine.load(url));
    }
}