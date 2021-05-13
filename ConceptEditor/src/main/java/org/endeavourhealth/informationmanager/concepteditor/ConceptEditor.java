package org.endeavourhealth.informationmanager.concepteditor;

import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.informationmanager.TTDocumentFiler;
import org.endeavourhealth.informationmanager.common.transform.EditorChecker;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Collection;
import java.util.HashMap;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.*;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;

/*
import org.endeavourhealth.dataaccess.IConceptService;
import org.endeavourhealth.imapi.model.Concept;
*/


public class ConceptEditor extends JFrame {

      private Integer badStart;
      private Integer badStop;
      private JButton getConceptButton;
      private JButton saveConceptButton;
      private TTDocumentFiler filer;

      SimpleAttributeSet[] attrs;

      DefaultStyledDocument doc;

      EditorChecker checker;

      JTextPane textPane;

      SquigglePainter red;

      static final int MAX_CHARACTERS = 300;

      JTextArea changeLog;


      String newline = "\n";

      HashMap<Object, Action> actions;

      // undo helpers
      protected UndoAction undoAction;

      protected RedoAction redoAction;

      protected UndoManager undo = new UndoManager();

      public ConceptEditor(EditorChecker checker) throws Exception {
            this.checker= checker;
            filer= new TTDocumentFiler(IM.GRAPH_DISCOVERY);
      }



      private void getConcept() throws BadLocationException {
            String text= checker.getConcept(doc.getText(0,doc.getLength()));
            textPane.setText("new concept");

      }
      private void saveConcept(){

      }

      public void createAndShowGUI(String preText) {

            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setSize(1000, 1400);
            setLocationRelativeTo(null);
            doc = new DefaultStyledDocument();
            textPane = new JTextPane(doc);
            textPane.setCaretPosition(0);
            textPane.setMargin(new Insets(5, 5, 5, 5));
            textPane.setPreferredSize(new Dimension(1000,400));
            textPane.setText(preText);


            JScrollPane scrollPane = new JScrollPane(textPane);// get the current font
            Font f = textPane.getFont();
            textPane.setFont(new Font(f.getFontName(), f.getStyle(), f.getSize() + 4));
            scrollPane.setPreferredSize(new Dimension(1000, 400));

            // Create the text area for the status log and configure it.
            changeLog = new JTextArea(5, 30);
            changeLog.setEditable(false);
            JScrollPane scrollPaneForLog = new JScrollPane(changeLog);

            // Create a split pane for the change log and the text area.
            JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, scrollPane, scrollPaneForLog);
            splitPane.setOneTouchExpandable(true);

            //Create the button row
            JPanel buttonPane= new JPanel(new GridBagLayout());
            buttonPane.setPreferredSize( new Dimension(1000,50));


            // Create the status area.
            JPanel statusPane = new JPanel(new GridLayout(1, 1));
            CaretListenerLabel caretListenerLabel = new CaretListenerLabel("Caret Status");
            statusPane.add(caretListenerLabel);

            //Buttons
            getConceptButton= new JButton("Get Concept");
            getConceptButton.setPreferredSize(new Dimension(200,32));
            getConceptButton.setBounds(300,780,200,300);
            getConceptButton.addActionListener(new ActionListener() {
                  @Override
                  public void actionPerformed(ActionEvent e) {
                        try {
                              getConcept();
                        } catch (BadLocationException badLocationException) {
                              badLocationException.printStackTrace();
                        }
                  }
            });
            saveConceptButton= new JButton("Save concept");
            saveConceptButton.setPreferredSize(new Dimension(200,32));
            saveConceptButton.addActionListener(new ActionListener() {
                  @Override
                  public void actionPerformed(ActionEvent e) {
                        saveConcept();
                  }
            });

            // Add the components.
            GridBagConstraints c= new GridBagConstraints();
            c.fill = GridBagConstraints.VERTICAL;
            c.gridx=0;
            c.gridy=0;
            c.weightx=0.5;
            c.insets.left=100;
            buttonPane.add(getConceptButton,c);

            c= new GridBagConstraints();
            c.fill= GridBagConstraints.VERTICAL;
            c.gridx=1;
            c.gridy=0;
            buttonPane.add(saveConceptButton,c);
            getContentPane().add(splitPane,BorderLayout.NORTH);
            getContentPane().add(buttonPane, BorderLayout.CENTER);
            getContentPane().add(statusPane, BorderLayout.SOUTH);

            // Set up the menu bar.
            createActionTable(textPane);
            JMenu editMenu = createEditMenu();

            JMenuBar mb = new JMenuBar();
            mb.add(editMenu);

            setJMenuBar(mb);

            // Add some key bindings.
            addBindings();

            // Start watching for undoable edits and caret changes.
            doc.addUndoableEditListener(new MyUndoableEditListener());
            textPane.addCaretListener(caretListenerLabel);
            doc.setDocumentFilter(new HighlightDocumentFilter(textPane));
            initAttributes(6);
            pack();
            setVisible(true);

      }

      // This listens for and reports caret movements.
      protected class CaretListenerLabel extends JLabel implements CaretListener {
            public CaretListenerLabel(String label) {
                  super(label);
            }

            // Might not be invoked from the event dispatching thread.
            public void caretUpdate(CaretEvent e) {
                  displaySelectionInfo(e.getDot(), e.getMark());
            }

            protected void displaySelectionInfo(final int dot, final int mark) {
                  SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                              if (dot == mark) { // no selection
                                    try {
                                          Rectangle caretCoords = textPane.modelToView(dot);
                                          // Convert it to view coordinates.
                                          setText("caret: text position: " + dot + ", view location = [" + caretCoords.x + ", "
                                              + caretCoords.y + "]" + newline);
                                    } catch (BadLocationException ble) {
                                          setText("caret: text position: " + dot + newline);
                                    }
                              } else if (dot < mark) {
                                    setText("selection from: " + dot + " to " + mark + newline);
                              } else {
                                    setText("selection from: " + mark + " to " + dot + newline);
                              }
                        }
                  });
            }
      }

      // This one listens for edits that can be undone.
      protected class MyUndoableEditListener implements UndoableEditListener {
            public void undoableEditHappened(UndoableEditEvent e) {
                  // Remember the edit and update the menus.
                  undo.addEdit(e.getEdit());
                  undoAction.updateUndoState();
                  redoAction.updateRedoState();
            }
      }

      public class HighlightDocumentFilter extends DocumentFilter {

            private DefaultHighlighter.DefaultHighlightPainter highlightPainter = new DefaultHighlighter.DefaultHighlightPainter(Color.YELLOW);
            private JTextPane textPane;
            private SimpleAttributeSet background;

            public HighlightDocumentFilter(JTextPane textPane) {
                  this.textPane = textPane;
                  background = new SimpleAttributeSet();
                  StyleConstants.setBackground(background, Color.RED);
            }

            @Override
            public void insertString(FilterBypass fb, int offset, String text, AttributeSet attr) throws BadLocationException {
                  System.out.println("insert");
                  super.insertString(fb, offset, text, attr);
            }

            @Override
            public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
                  super.remove(fb, offset, length);
                  grammarCheck(fb);
            }
            private void grammarCheck(FilterBypass fb) throws BadLocationException {
                  Document tempDoc = fb.getDocument();
                  Collection<String> expecting = checker.checkSyntax(tempDoc.getText(0, tempDoc.getLength()));
                  changeLog.setText("");
                  textPane.getHighlighter().removeAllHighlights();
                  String allText= tempDoc.getText(0,tempDoc.getLength());
                  fb.replace(0,tempDoc.getLength(),allText, attrs[0]);
                  if (expecting != null) {
                        for (String s : expecting) {
                              changeLog.append(s + "\n");
                        }
                        if (checker.getExpectedLiterals().size()>0) {
                              Collection<String> expected= checker.getExpectedLiterals();
                              Integer startIndex = checker.getBadTokenStart();
                              Integer endIndex = (tempDoc.getLength());
                              if (startIndex < tempDoc.getLength()) {
                                    if (startIndex <= endIndex) {
                                          String replace = tempDoc.getText(startIndex, endIndex - startIndex);
                                          if (!goodToken(expected, replace))
                                                fb.replace(startIndex, endIndex - startIndex, replace, attrs[5]);
                                          //  textPane.getHighlighter()
                                          //    .addHighlight(startIndex,endIndex, red);
                                    }
                              }

                        }
                  }
            }
            private boolean goodToken(Collection<String> expected, String text){
                  boolean result= false;
                  for (String literal:expected)
                        if (literal.toLowerCase().startsWith(text.toLowerCase()))
                              result= true;

                  return result;
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet att) throws BadLocationException {
                  super.replace(fb, offset, length, text, att);
                  grammarCheck(fb);

            }

      }


      // Add a couple of emacs key bindings for navigation.
      protected void addBindings() {
            InputMap inputMap = textPane.getInputMap();

            // Ctrl-b to go backward one character
            KeyStroke key = KeyStroke.getKeyStroke(KeyEvent.VK_B, Event.CTRL_MASK);
            inputMap.put(key, DefaultEditorKit.backwardAction);

            // Ctrl-f to go forward one character
            key = KeyStroke.getKeyStroke(KeyEvent.VK_F, Event.CTRL_MASK);
            inputMap.put(key, DefaultEditorKit.forwardAction);

            // Ctrl-p to go up one line
            key = KeyStroke.getKeyStroke(KeyEvent.VK_P, Event.CTRL_MASK);
            inputMap.put(key, DefaultEditorKit.upAction);

            // Ctrl-n to go down one line
            key = KeyStroke.getKeyStroke(KeyEvent.VK_N, Event.CTRL_MASK);
            inputMap.put(key, DefaultEditorKit.downAction);
      }

      // Create the edit menu.
      protected JMenu createEditMenu() {
            JMenu menu = new JMenu("Edit");

            // Undo and redo are actions of our own creation.
            undoAction = new UndoAction();
            menu.add(undoAction);

            redoAction = new RedoAction();
            menu.add(redoAction);


            return menu;
      }

      // Create the style menu.


      protected SimpleAttributeSet[] initAttributes(int length) {
            // Hard-code some attributes.
            red = new SquigglePainter(Color.RED);
            attrs = new SimpleAttributeSet[length];

            attrs[0] = new SimpleAttributeSet();
            //StyleConstants.setFontFamily(attrs[0], "SansSerif");
            //StyleConstants.setFontSize(attrs[0], 16);

            attrs[1] = new SimpleAttributeSet(attrs[0]);
            StyleConstants.setBold(attrs[1], true);

            attrs[2] = new SimpleAttributeSet(attrs[0]);
            StyleConstants.setItalic(attrs[2], true);

            attrs[3] = new SimpleAttributeSet(attrs[0]);
            StyleConstants.setFontSize(attrs[3], 20);

            attrs[4] = new SimpleAttributeSet(attrs[0]);
            StyleConstants.setFontSize(attrs[4], 12);

            attrs[5] = new SimpleAttributeSet(attrs[0]);
            StyleConstants.setForeground(attrs[5], Color.red);


            return attrs;
      }

      // The following two methods allow us to find an
      // action provided by the editor kit by its name.
      private void createActionTable(JTextComponent textComponent) {
            actions = new HashMap<Object, Action>();
            Action[] actionsArray = textComponent.getActions();
            for (int i = 0; i < actionsArray.length; i++) {
                  Action a = actionsArray[i];
                  actions.put(a.getValue(Action.NAME), a);
            }
      }

      private Action getActionByName(String name) {
            return actions.get(name);
      }

      class UndoAction extends AbstractAction {
            public UndoAction() {
                  super("Undo");
                  setEnabled(false);
            }

            public void actionPerformed(ActionEvent e) {
                  try {
                        undo.undo();
                  } catch (CannotUndoException ex) {
                        System.out.println("Unable to undo: " + ex);
                        ex.printStackTrace();
                  }
                  updateUndoState();
                  redoAction.updateRedoState();
            }

            protected void updateUndoState() {
                  if (undo.canUndo()) {
                        setEnabled(true);
                        putValue(Action.NAME, undo.getUndoPresentationName());
                  } else {
                        setEnabled(false);
                        putValue(Action.NAME, "Undo");
                  }
            }
      }

      class RedoAction extends AbstractAction {
            public RedoAction() {
                  super("Redo");
                  setEnabled(false);
            }

            public void actionPerformed(ActionEvent e) {
                  try {
                        undo.redo();
                  } catch (CannotRedoException ex) {
                        System.out.println("Unable to redo: " + ex);
                        ex.printStackTrace();
                  }
                  updateRedoState();
                  undoAction.updateUndoState();
            }

            protected void updateRedoState() {
                  if (undo.canRedo()) {
                        setEnabled(true);
                        putValue(Action.NAME, undo.getRedoPresentationName());
                  } else {
                        setEnabled(false);
                        putValue(Action.NAME, "Redo");
                  }
            }
      }




}

