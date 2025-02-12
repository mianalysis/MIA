package io.github.mianalysis.mia.process.selectors;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.LinkedList;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.AbstractListModel;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.drew.lang.annotations.Nullable;

import ij.Prefs;
import io.github.mianalysis.mia.MIA;

public class ClassSelector implements ActionListener, KeyListener {
    private boolean allowAdditions;
    private JFrame frame;
    private JTabbedPane tabbedPane;
    private SortedListModel<String> fullListModel = new SortedListModel<>();
    private JList<String> fullList = new JList<>(fullListModel);
    private SortedListModel<String> currentListModel = new SortedListModel<>();
    private JList<String> currentList = new JList<>(currentListModel);
    private RecentListModel<String> recentListModel = new RecentListModel<>();
    private JList<String> recentList = new JList<>(recentListModel);
    private SortedListModel<String> searchListModel = new SortedListModel<>();
    private JList<String> searchList = new JList<>(searchListModel);
    private String lastSelectedClass = null;

    // Tab names
    private static final String ALL_CLASSES = "All classes";
    private static final String CURRENT = "Current classes";
    private static final String RECENT = "Recent classes";
    private static final String SEARCH = "Search";
    
    // GUI buttons
    private static final String CREATE_CLASS = "+";
    private static final String APPLY_CLASS = "Apply class";

    public ClassSelector(@Nullable TreeSet<String> classes, boolean allowAdditions) {
        this.allowAdditions = allowAdditions;
        fullListModel.addAll(classes);

        String[] recentsArray = Prefs.get("MIA.classSelector.recentList", "").split(",");
        for (int i=recentsArray.length-1;i>=0;i--)
            if (fullListModel.contains(recentsArray[i]))
                recentListModel.add(recentsArray[i]);
        
        showOptionsPanel();

    }

    public boolean isActive() {
        return frame != null && frame.isVisible();
    }

    private void showOptionsPanel() {
        frame = new JFrame();

        frame.setAlwaysOnTop(true);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                lastSelectedClass = "None";
            };
        });

        frame.setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(5, 5, 5, 5);
        c.gridwidth = 4;
        c.fill = GridBagConstraints.BOTH;

        tabbedPane = new JTabbedPane();
        frame.add(tabbedPane, c);

        currentList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        fullList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        recentList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        searchList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane fullListScrollPane = new JScrollPane(fullList);
        fullListScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        fullListScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        fullListScrollPane.getVerticalScrollBar().setUnitIncrement(10);
        fullListScrollPane.setViewportBorder(BorderFactory.createEmptyBorder());
        tabbedPane.add(ALL_CLASSES, fullListScrollPane);

        JScrollPane currentListScrollPane = new JScrollPane(currentList);
        currentListScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        currentListScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        currentListScrollPane.getVerticalScrollBar().setUnitIncrement(10);
        currentListScrollPane.setViewportBorder(BorderFactory.createEmptyBorder());
        tabbedPane.add(CURRENT, currentListScrollPane);

        JScrollPane recentListScrollPane = new JScrollPane(recentList);
        recentListScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        recentListScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        recentListScrollPane.getVerticalScrollBar().setUnitIncrement(10);
        recentListScrollPane.setViewportBorder(BorderFactory.createEmptyBorder());
        tabbedPane.add(RECENT, recentListScrollPane);        

        JPanel searchPane = new JPanel();
        searchPane.setLayout(new BoxLayout(searchPane, BoxLayout.PAGE_AXIS));

        JScrollPane searchListScrollPane = new JScrollPane(searchList);
        searchListScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        searchListScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        searchListScrollPane.getVerticalScrollBar().setUnitIncrement(10);
        searchListScrollPane.setViewportBorder(BorderFactory.createEmptyBorder());
        searchPane.add(searchListScrollPane);

        JTextField searchField = new JTextField();
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            void updateSearchList() {
                String searchString = searchField.getText().toLowerCase();

                TreeSet<String> searchItems = new TreeSet<>();
                for (String item : fullListModel.items())
                    if (item.toLowerCase().contains(searchString))
                        searchItems.add(item);
                searchListModel.setItems(searchItems);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateSearchList();
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                updateSearchList();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateSearchList();
            }
        });
        searchPane.add(searchField);
        tabbedPane.add(SEARCH, searchPane);

        JButton createClassButton = new JButton(CREATE_CLASS);
        createClassButton.addActionListener(this);
        createClassButton.setActionCommand(CREATE_CLASS);
        c.gridx = 0;
        c.gridy++;
        c.gridwidth = 1;
        c.weightx = 1;
        c.fill = GridBagConstraints.VERTICAL;
        c.anchor = GridBagConstraints.WEST;
        if (allowAdditions)
            frame.add(createClassButton, c);

        JButton applyClassButton = new JButton(APPLY_CLASS);
        applyClassButton.addActionListener(this);
        applyClassButton.setActionCommand(APPLY_CLASS);
        c.gridx = 3;
        c.anchor = GridBagConstraints.EAST;
        frame.add(applyClassButton, c);

        frame.pack();
        frame.setLocation(100, 100);
        frame.setVisible(false); // Don't show till needed

    }

    public void setVisible(boolean visible) {
        frame.setVisible(visible);

    }

    public String getLastSelectedClass() {
        return lastSelectedClass;
    }

    public TreeSet<String> getAllClasses() {
        return fullListModel.items();
    }

    public void addExistingClass(String existingClass) {
        fullListModel.add(existingClass);
        recentListModel.add(existingClass);
        currentListModel.add(existingClass);
    }

    public void addExistingClasses(TreeSet<String> existingClasses) {
        fullListModel.addAll(existingClasses);
        recentListModel.addAll(existingClasses);
        currentListModel.addAll(existingClasses);
    }

    void saveRecentClassesToPrefs() {
        String recentList = String.join(",", recentListModel.items());
        Prefs.set("MIA.classSelector.recentList", recentList);
        Prefs.savePreferences();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case (APPLY_CLASS):
                String activeList = tabbedPane.getTitleAt(tabbedPane.getSelectedIndex());

                switch (activeList) {
                    case ALL_CLASSES:
                        lastSelectedClass = fullList.getSelectedValue();
                        currentListModel.add(lastSelectedClass);
                        recentListModel.add(lastSelectedClass);
                        break;
                    case CURRENT:
                        lastSelectedClass = currentList.getSelectedValue();
                        recentListModel.add(lastSelectedClass);
                        break;
                    case RECENT:
                        lastSelectedClass = recentList.getSelectedValue();
                        recentListModel.add(lastSelectedClass); // This brings it to the top of the list
                        currentListModel.add(lastSelectedClass);
                        break;
                    case SEARCH:
                        lastSelectedClass = searchList.getSelectedValue();
                        recentListModel.add(lastSelectedClass); // This brings it to the top of the list
                        currentListModel.add(lastSelectedClass);
                        break;
                }

                saveRecentClassesToPrefs();

                setVisible(false);

                break;

            case (CREATE_CLASS):
                JFrame jf = new JFrame();
                jf.setAlwaysOnTop(true);
                String newClass = JOptionPane.showInputDialog(jf, "Enter new class name");

                fullListModel.add(newClass);
                int idx = fullListModel.getIndex(newClass);
                fullList.setSelectedIndex(idx);
                fullList.ensureIndexIsVisible(idx);
                tabbedPane.setSelectedIndex(0); // Selecting "All items" tab
                break;
        }
    }

    @Override
    public void keyPressed(KeyEvent arg0) {
    }

    @Override
    public void keyReleased(KeyEvent arg0) {
    }

    @Override
    public void keyTyped(KeyEvent arg0) {
    }

    class SortedListModel<E> extends AbstractListModel<String> {
        private TreeSet<String> items = new TreeSet<>();

        public SortedListModel() {

        }

        public SortedListModel(TreeSet<String> items) {
            this.items = items;
        }

        public void setItems(TreeSet<String> items) {
            this.items = items;
            fireContentsChanged(this, 0, getSize());
        }

        public void add(String newItem) {
            items.add(newItem);
            fireContentsChanged(this, 0, getSize());
        }

        public void addAll(Set<String> newItems) {
            items.addAll(newItems);
            fireContentsChanged(this, 0, getSize());
        }

        public boolean contains(String item) {
            return items.contains(item);
        }

        public int getSize() {
            return items.size();
        }

        public String getElementAt(int index) {
            return (String) items.toArray()[index];
        }

        public TreeSet<String> items() {
            return items;
        }

        public int getIndex(String item) {
            int idx = 0;
            for (String listItem : items)
                if (listItem == item)
                    return idx;
                else
                    idx++;

            // Return -1 if not found
            return -1;

        }
    }

    class RecentListModel<E> extends AbstractListModel<String> {
        private int capacity = 100;
        private LinkedList<String> items = new LinkedList<>();

        public RecentListModel() {

        }

        public RecentListModel(LinkedList<String> items) {
            this.items = items;

        }

        public void addNoUpdate(String newItem) {
            // If adding again (to move to top of the list), remove first
            if (items.contains(newItem))
                items.remove(newItem);

            items.addFirst(newItem);

            if (items.size() > capacity)
                items.removeLast();

        }

        public void add(String newItem) {
            addNoUpdate(newItem);
            fireContentsChanged(this, 0, getSize());

        }

        public void addAll(Set<String> newItems) {
            for (String newItem:newItems)
                addNoUpdate(newItem);

            fireContentsChanged(this, 0, getSize());
        }

        public boolean contains(String item) {
            return items.contains(item);
        }

        public int getSize() {
            return items.size();
        }

        public String getElementAt(int index) {
            return (String) items.toArray()[index];
        }

        public LinkedList<String> items() {
            return items;
        }
    }
}