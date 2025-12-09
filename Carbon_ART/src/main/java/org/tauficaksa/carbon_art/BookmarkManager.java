package org.tauficaksa.carbon_art;


import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TabPane;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;


public class BookmarkManager {
    private static final Path BOOKMARKS_FILE = Paths.get(System.getProperty("user.home"), ".carbon_art_bookmarks.txt");


    public List<String> loadBookmarks() {
        try {
            if (Files.exists(BOOKMARKS_FILE)) {
                return new ArrayList<>(Files.readAllLines(BOOKMARKS_FILE));
            }
        } catch (IOException ignored) {}
        return new ArrayList<>();
    }


    public void saveBookmarks(List<String> bookmarks) {
        try {
            Files.write(BOOKMARKS_FILE, bookmarks, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void updateBookmarksMenu(MenuButton menu, List<String> bookmarks, TabPane tabPane) {
        menu.getItems().clear();
        if (bookmarks.isEmpty()) menu.getItems().add(new MenuItem("No bookmarks"));
        else {
            for (String b : bookmarks) {
                MenuItem mi = new MenuItem(b);
                mi.setOnAction(e -> {
                    BrowserTab.getEngine(tabPane).ifPresent(engine -> engine.load(b));
                });
                menu.getItems().add(mi);
            }
            menu.getItems().add(new SeparatorMenuItem());
            MenuItem clear = new MenuItem("Clear Bookmarks");
            clear.setOnAction(e -> {
                bookmarks.clear();
                try { Files.write(BOOKMARKS_FILE, new ArrayList<>() , StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING); } catch (IOException ex) { ex.printStackTrace(); }
                updateBookmarksMenu(menu, bookmarks, tabPane);
            });
            menu.getItems().add(clear);
        }
    }
}