package org.endeavourhealth.informationmanager.concepteditor;

import org.endeavourhealth.imapi.model.tripletree.TTConcept;
import org.endeavourhealth.informationmanager.common.transform.IMLValidator;
import org.endeavourhealth.informationmanager.common.transform.TTManager;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.*;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;



public class ConceptEditor extends JFrame {

      private Integer badStart;
      private Integer badStop;
      private JButton getConceptButton;
      private JButton saveConceptButton;
      private String ontologyFile;
      private SimpleAttributeSet wrongSyntax;
      private List<String> autoSuggest= new ArrayList<>();
      private Rectangle caretCoords;
      private boolean isBadToken;
      private int selectedToken;
      private boolean autoFocus;
      private TokenHelper tokenHelper;



      SimpleAttributeSet[] attrs;

      DefaultStyledDocument doc;

      IMLValidator checker;

      JTextPane textPane;

      static final int MAX_CHARACTERS = 300;

      JTextField changeLog;


      String newline = "\n";

      HashMap<Object, Action> actions;

      // undo helpers
      protected UndoAction undoAction;

      protected RedoAction redoAction;

      protected UndoManager undo = new UndoManager();

      public ConceptEditor(IMLValidator checker, String ontologyFile) throws Exception {
            this.checker= checker;
            this.ontologyFile= ontologyFile;
            this.setTitle("Concept editor");

      }

      private void getConcept() throws BadLocationException {

      }

      private void saveConcept() throws BadLocationException, IOException {
            TTConcept concept= checker.parseToConcept(doc.getText(0,doc.getLength()));
            TTManager manager= new TTManager();
            manager.loadDocument(ontologyFile);
            if (manager.getConcept(concept.getIri())==null)
                  manager.getDocument().addConcept(concept);
            else {
                  TTConcept old= manager.getConcept(concept.getIri());
                  manager.getDocument().getConcepts().remove(old);
                  manager.getDocument().addConcept(concept);
                  manager.saveDocument(new File(ontologyFile));
            }

      }

      public void createAndShowGUI(String preText) {

            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            //JMenuBar mb= new JMenuBar();
            //JMenu mn= new JMenu("Edit");
            //mb.add(mn);
            //setJMenuBar(mb);
            getContentPane().setLayout(new GridBagLayout());


            //Creates the textpane for main editor
            doc = new DefaultStyledDocument();
            tokenHelper = new TokenHelper(doc);
            textPane = new JTextPane(doc);
            textPane.setCaretPosition(0);
            textPane.setMargin(new Insets(5, 5, 5, 5));
            textPane.setText(preText);
            textPane.setFont(textPane.getFont().deriveFont(14f));
            JScrollPane scrollPane = new JScrollPane(textPane);
            changeLog = new JTextField();
            JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
              scrollPane, changeLog);
            splitPane.setResizeWeight(0.95);
           // Font f = textPane.getFont();
           // textPane.setFont(new Font(f.getFontName(), f.getStyle(), f.getSize() + 4));
            //scrollPane.setPreferredSize(new Dimension(1000, 400));
            GridBagConstraints c= new GridBagConstraints();
            c.fill = GridBagConstraints.BOTH;
            c.gridx=0;
            c.gridy=0;
            c.weightx=0.5;
            c.ipady=400;
            c.ipadx= 600;
            getContentPane().add(splitPane,c);

            //Token helper and IRI look up
            JScrollPane scrollList = new JScrollPane(tokenHelper);
            c= new GridBagConstraints();
            c.fill= GridBagConstraints.BOTH;
            c.gridx=1;
            c.gridy=0;
            c.weightx=0.5;
            c.ipady=400;
            c.ipadx= 200;
            getContentPane().add(scrollList,c);



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
                        try {
                              saveConcept();
                        } catch (BadLocationException | IOException badLocationException) {
                              badLocationException.printStackTrace();
                        }
                  }
            });

            // Add the components.
            c= new GridBagConstraints();
            c.fill = GridBagConstraints.LINE_START;
            c.gridx=0;
            c.gridy=1;
            c.weightx=0.5;
            c.insets.bottom=10;
            getContentPane().add(getConceptButton,c);

            c= new GridBagConstraints();
            c.fill= GridBagConstraints.LINE_END;
            c.gridx=1;
            c.gridy=1;
            c.insets.bottom=10;
            getContentPane().add(saveConceptButton,c);



            // Add some key bindings.
            addBindings();

            //textPane.addCaretListener(caretListenerLabel);
            doc.setDocumentFilter(new HighlightDocumentFilter(textPane));
            initAttributes(6);
            pack();
            setLocationRelativeTo(null);
            setVisible(true);


      }

      // This listens for and reports caret movements.
      protected class CaretListenerLabel extends JLabel implements CaretListener {

            public CaretListenerLabel(String label) {
                  super(label);
            }

            // Might not be invoked from the event dispatching thread.
            public void caretUpdate(CaretEvent e) {
                  try {

                        caretCoords= textPane.modelToView(e.getDot());
                  } catch (BadLocationException badLocationException) {
                        badLocationException.printStackTrace();
                  }


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

                 super.insertString(fb, offset, text, attr);
                  if (text.length()!=1)
                        return;
                grammarCheck(fb,text);
            }

            @Override
            public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
                 super.remove(fb, offset, length);
                  //grammarCheck(fb,null);
            }



            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet att) throws BadLocationException {
                  super.replace(fb, offset, length, text, att);
                  grammarCheck(fb,text);

            }

      }

      private void grammarCheck(DocumentFilter.FilterBypass fb, String character) throws BadLocationException {

            Document tempDoc = fb.getDocument();
            tokenHelper.getContents().clear();

            changeLog.setText("");
            textPane.getHighlighter().removeAllHighlights();
            String allText= tempDoc.getText(0,tempDoc.getLength());
            int caretPos= textPane.getCaretPosition();
            fb.replace(0,tempDoc.getLength(),allText, attrs[0]);
            checker.checkSyntax(tempDoc.getText(0,tempDoc.getLength()),caretPos);

            if (checker.getBadTokenStart()!=null) {
                  //Avoid the valid token problem by checking bad token start position
                  if (checker.getBadTokenStart() < caretPos | isSpace(character)) {
                        if (checker.getAutoSuggestions() != null) {
                              for (String option : checker.getAutoSuggestions()) {
                                   tokenHelper.getContents().addElement(option);
                              }
                              tokenHelper.setSelectedIndex(checker.getSelectedToken());
                              if (isSpace(character))
                                  if (acceptedSuggestion(fb, caretPos)) {
                                       grammarCheck(fb, character);
                                         return;
                                    }
                        }

                        if (!checker.isGoodToken()) {
                              String badToken = checker.getBadToken();
                              if (badToken != null) {
                                    int badTokenStart = checker.getBadTokenStart();
                                    if (badTokenStart < caretPos) {
                                          fb.replace(badTokenStart,
                                            badToken.length(),
                                            badToken,
                                            wrongSyntax);
                                    }
                              }
                        }




                  }

            }

            if (checker.getHelpMessage() != null)
                        changeLog.setText(checker.getHelpMessage());
            if (checker.getSemanticErrors()!=null)
                  for (String semantics:checker.getSemanticErrors())
                        changeLog.setText(changeLog.getText()+"\n"+ semantics);


            if (checker.getAutoSuggestions()!=null){
                  if (checker.getBadTokenStart()!=null)
                  if (checker.getBadTokenStart()<caretPos) {
                        String prefix = checker.getBadToken();
                        String bestToken = checker.getBestToken(prefix);
                        if (bestToken != null) {
                              String completion = bestToken.substring(prefix.length());
                              SwingUtilities.invokeLater(
                                new CompletionTask(completion, caretPos));
                        }
                  }

            }

            textPane.grabFocus();
            textPane.setCaretPosition(caretPos);


      }

      private boolean isSpace(String character){
            if (character==null)
                  return false;
            if (character.equals(" "))
                  return true;
            if (character.equals("\n"))
                  return true;
            return false;
      }

      private boolean acceptedSuggestion(DocumentFilter.FilterBypass fb, int caretPos) throws BadLocationException {
            if (checker.getBadToken()!=null) {
                  String badToken = checker.getBadToken();
                  int badTokenStart = checker.getBadTokenStart();
                  if (badTokenStart < caretPos)
                        if (checker.isGoodToken()) {
                              String token = checker.getAutoSuggestions().get(checker.getSelectedToken());
                              fb.replace(badTokenStart,badToken.length(),token,attrs[0]);
                              return true;
                        }
            }
            return false;
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
            addPopUpKeyBinding();
      }
      private void addPopUpKeyBinding() {
            textPane.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0, true), "Down released");
            textPane.getActionMap().put("Down released", new AbstractAction() {
                  @Override
                  public void actionPerformed(ActionEvent ae) {
                        int caretPos= textPane.getCaretPosition();
                        if (autoSuggest.size() > 0) {
                              if (selectedToken<(autoSuggest.size()-1)){
                                    autoFocus=true;
                                    if (selectedToken>-1) {
                                          JMenuItem item = (JMenuItem) tokenHelper.getComponent(selectedToken);
                                          item.setBackground(null);
                                    }
                                    selectedToken++;
                                    JMenuItem item = (JMenuItem) tokenHelper.getComponent(selectedToken);
                                    item.setBackground((Color.GRAY));
                              }
                              textPane.grabFocus();
                              textPane.setCaretPosition(caretPos);
                        }
                  }

            });
            textPane.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent
              .VK_UP, 0, true), "Up released");
            textPane.getActionMap().put("Up released", new AbstractAction() {
                  @Override
                  public void actionPerformed(ActionEvent ae) {
                        int caretPos= textPane.getCaretPosition();
                        if (autoSuggest.size() > 0) {
                              if (selectedToken==0)
                                    autoFocus=false;
                              if (selectedToken>-1) {
                                    JMenuItem item = (JMenuItem) tokenHelper.getComponent(selectedToken);
                                    item.setBackground(null);
                              }
                              if (selectedToken>0)
                                    selectedToken--;
                              if (selectedToken>-1){
                                  JMenuItem item = (JMenuItem) tokenHelper.getComponent(selectedToken);
                                  item.setBackground((Color.GRAY));
                              }
                              textPane.grabFocus();
                              textPane.setCaretPosition(caretPos);
                        }
                  }

            });
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
            attrs = new SimpleAttributeSet[length];

            attrs[0] = new SimpleAttributeSet();

            attrs[1] = new SimpleAttributeSet(attrs[0]);
            StyleConstants.setBold(attrs[1], true);

            attrs[2] = new SimpleAttributeSet(attrs[0]);
            StyleConstants.setItalic(attrs[2], true);

            attrs[3] = new SimpleAttributeSet(attrs[0]);

            attrs[4] = new SimpleAttributeSet(attrs[0]);

            attrs[5] = new SimpleAttributeSet(attrs[0]);
            StyleConstants.setForeground(attrs[5], Color.red);
            wrongSyntax= attrs[5];

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

      private class CompletionTask implements Runnable {
            String completion;
            int position;

            CompletionTask(String completion, int position) {
                  this.completion = completion;
                  this.position = position;
            }

            public void run() {
                  try {
                        doc.insertString(position,completion, attrs[0]);
                  } catch (BadLocationException e) {
                        e.printStackTrace();
                  }
                  textPane.setCaretPosition(position + completion.length());
                  textPane.moveCaretPosition(position);

            }
      }




}

