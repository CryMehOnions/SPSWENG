import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.font.*;

public class TaskView extends JFrame implements View {
    /**** GUI Elements ***/
	private JTextField taskTxt;
    private JButton addButton;
    private JButton clrButton;
    private JPanel taskList;
    private JLabel taskNum;
    private ArrayList<JCheckBox> taskChkBox;

    private Controller controller;         //view now requires a controller to mediate with

    public TaskView(Controller controller) {
        this.controller = controller;
        initComponents();
    }

    /**** Layouting of the gui ***/
    private void initComponents() {
        taskTxt = new JTextField("Add New Tasks");
        taskList = new JPanel();
        taskNum = new JLabel();
        clrButton = new JButton("Clear Completed Tasks");
        taskChkBox = new ArrayList<JCheckBox>();

        taskTxt.setForeground(new Color(153, 153, 153));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("To-Do List");
        setResizable(false);
        setVisible(true);

        taskTxt.addKeyListener(new txtFieldKeyListener());
        taskTxt.addFocusListener(new txtFieldFocusListener());
        clrButton.addActionListener(new clrTaskListener());

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                    .addComponent(taskList, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(taskNum)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 175, Short.MAX_VALUE)
                        .addComponent(clrButton))
                    .addComponent(taskTxt, GroupLayout.Alignment.LEADING))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(taskTxt, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(taskList, GroupLayout.PREFERRED_SIZE, 417, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(taskNum)
                    .addComponent(clrButton))
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        
        updateView();
        pack();
    }

    /**** method to update the GUI ***/
    private void updateView() {
    	/*** Resetting the layout of TaskList***/
        taskList.removeAll();
    	taskList.setAlignmentX(LEFT_ALIGNMENT);
        BoxLayout boxLayout = new BoxLayout(taskList, BoxLayout.Y_AXIS);
        taskList.setBorder(BorderFactory.createEtchedBorder());
        taskList.setLayout(boxLayout);

        /*** Adding Checkboxes in panel ***/
        boolean buttonEnabler = false;      //to check for completed tasks
        for(JCheckBox chk: taskChkBox) {
        	taskList.add(chk);
            if(chk.isSelected())
                buttonEnabler = true;
        }

        clrButton.setEnabled(buttonEnabler);//Enables the clear button when there is a completed task
        taskNum.setText("" + controller.getCount() + " Completed Tasks");

        taskList.requestFocusInWindow();    //to remove focus from the textfield
        validate();                         //updating the JFrame to show updated view
        repaint();
    }                

    public void update(Iterator tasks) {
        ArrayList<JCheckBox> chk = new ArrayList<JCheckBox>(); //new ArrayList of chockboxes for view

        /**** Settting StrikeThrough Font Style ***/
        Font font = new Font("Tahoma", Font.PLAIN, 11);
        Map attributes = font.getAttributes();
        attributes.put(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON);
        Font strikeFont = new Font(attributes);                 
        
        /**** Creates a new List of Checkboxes containing the revised iterator of tasks ***/
        for(Iterator iterator = tasks; iterator.hasNext();) {
            Task t = (Task) iterator.next();

            JCheckBox box = new JCheckBox(t.toString());        //creates a checkbox containing a task.
            if(t.isComplete()) {                                 //text will be striked if the task is already completed
                box.setFont(strikeFont);                
                box.setSelected(true);                          //ticks the checkbox
                box.setForeground(new Color(153, 153, 153));
            }
            box.addActionListener(new chkBoxListener());
            chk.add(box);
        } taskChkBox = chk;
        
        /**** actual updating of view ***/
        updateView();
    }

    private class chkBoxListener implements ActionListener {
        public void actionPerformed(ActionEvent evt) {
            JCheckBox chk = (JCheckBox)evt.getSource();
            controller.setTask(chk.getText(), chk.isSelected());
        }
    }

    private class clrTaskListener implements ActionListener {
        public void actionPerformed(ActionEvent evt) {
            controller.clearComplete();
        }
    }

    private class txtFieldFocusListener implements FocusListener {   //For graphical purposes only
        public void focusGained(FocusEvent evt) {
            taskTxt.setText("");
            taskTxt.setForeground(Color.black);
        }

        public void focusLost(FocusEvent evt) {
            taskTxt.setText("Add New Tasks");
            taskTxt.setForeground(new Color(153, 153, 153));
        }
    }

    private class txtFieldKeyListener implements KeyListener {
        public void keyReleased(KeyEvent evt) {
            //do  nothing
        }

        public void keyTyped(KeyEvent evt) {
            //do nothing
        }

        public void keyPressed(KeyEvent evt) {
            if(evt.getKeyCode() == KeyEvent.VK_ENTER)
                controller.addTask(taskTxt.getText());
        }
    }
}
