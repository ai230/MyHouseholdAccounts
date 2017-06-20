package MyHouseHoldAccounts;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
//import javax.swing.event.ListSelectionListener;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

public class Main extends JFrame {

    ControllDB controllDB = new ControllDB();
    JComboBox combo;
    private JTextField datePickerTxt = new JTextField();
    private JButton datePickerBtn = new JButton("Calender");

    private JLabel lbl = new JLabel("Choose Category :");
    private JLabel displayLbl = new JLabel("$0", SwingConstants.RIGHT);
    private JButton enterBtn = new JButton("Ent");
    private JButton clearBtn = new JButton("Clr");
    private JTextField txt = new JTextField("aaaaa");
    private JButton listBtn = new JButton("List");
    private JButton deleteBtn = new JButton("Delete");
    private JLabel totalAmountLbl = new JLabel("");
    private JPanel totalAmountPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    private JTable table;
    private DefaultTableModel tableModel;

    private JPanel panelTop = new JPanel(new BorderLayout());
    private JPanel panelTopCenter = new JPanel();
    private JPanel panelTopWest = new JPanel();
    
    private JPanel Amountpanel = new JPanel(new BorderLayout());
    private JPanel panel = new JPanel();
    private JPanel sortGroupPanel = new JPanel();
    private JPanel categoryPanel = new JPanel();
    private JPanel propertyPanel = new JPanel();
    private JPanel monthPanel = new JPanel();

    private ButtonGroup btnGroup = new ButtonGroup();
    private JRadioButton RadioBtn1 = new JRadioButton();
    private JRadioButton RadioBtn2 = new JRadioButton();
    private JRadioButton[] radioBtns;
    private JComboBox monthCombo;

    private String propertyList[] = {"Expence", "Income"};
    private String categoryList[] = {"Rent", "Insurance", "Food", "Income"};
    private String monthList[] = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12",};

    private String[] colName = {"#", "Date", "Property", "Category", "Amount($)"};

    private double number;
    private String numStr = "";

    private String todyayDateStr;
    private String dateStr;
    private String newDateStr;
    private String category = categoryList[0];
    private String property = propertyList[0];

    private int selectedRow;

    public Main() {
        //current date
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();
        dateStr = dateFormat.format(date);

        setTitle("Quick Money " + dateStr);

        //--Panel North--
        JPanel pn = new JPanel();
        pn.setLayout(new BoxLayout(pn, BoxLayout.Y_AXIS));

        //date picker
        //create text field
        datePickerTxt = new JTextField(dateStr);
        //set bounds of text field
        datePickerTxt.setBounds(101, 107, 86, 20);

        datePickerBtn.addActionListener(new ActionListener() {
            //performed action
            public void actionPerformed(ActionEvent arg0) {
                //create frame new object  f
                final JFrame f = new JFrame();
                datePickerTxt.setText(dateStr);
                newDateStr = new DatePicker(f).setPickedDate();
                System.out.println("1: data=" + dateStr + " new date=" + newDateStr);
                if (newDateStr != null) {
                    //set text which is collected by date picker i.e. set date 
                    datePickerTxt.setText(newDateStr);
                    dateStr = newDateStr;
                    System.out.println("2: data=" + dateStr + " new date=" + newDateStr);
                }
            }
        });

        //add panel of category label and combo
        combo = new JComboBox(categoryList);
        combo.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String cmd = (String) combo.getSelectedItem();

                if (cmd.matches("Choose Category :")) {
                    //none
                } else if (cmd.matches("Income")) {
                    property = "income";
                    category = cmd;
                } else {
                    property = "expence";
                    category = cmd;
                }
                System.out.println("insert " + dateStr + "," + property + "," + category + "," + number);
            }
        });

        //add amount label
        displayLbl.setBackground(Color.LIGHT_GRAY);
        displayLbl.setOpaque(true);

        BufferedImage myPicture = null;
        try {
            myPicture = ImageIO.read(new File("/Users/yamamotoai/Desktop/MYICON.png"));
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        JLabel picLabel = new JLabel(new ImageIcon(myPicture));
        
        
        
        panelTopCenter.setLayout(new FlowLayout());
        panelTopCenter.add(picLabel);
        panelTopCenter.add(lbl);
        panelTopCenter.add(combo);
        panelTopCenter.add(datePickerTxt);
        panelTopCenter.add(datePickerBtn);
        panelTopCenter.setAlignmentY(panelTop.CENTER_ALIGNMENT);
            
        JPanel p = new JPanel(new GridBagLayout());
        p.add(panelTopCenter);
        panelTopWest.add(picLabel);
        
        panelTop.add(panelTopWest, BorderLayout.WEST);
        panelTop.add(p, BorderLayout.CENTER);
        
        //for create some space for panel2
        panel.setBackground(Color.lightGray);
        panel.setPreferredSize(new Dimension(100, 0));
        
        Amountpanel.add(panel, BorderLayout.EAST);
        Amountpanel.setPreferredSize(new Dimension(0, 50));
        Amountpanel.setAlignmentX(CENTER_ALIGNMENT);
        Amountpanel.setBackground(Color.lightGray);
        Amountpanel.add(displayLbl, BorderLayout.CENTER);
        
        pn.add(panelTop);
        pn.add(Amountpanel);

        //--panel Center--
        JPanel pc = new JPanel();
        pc.setLayout(new GridLayout(4, 4, 10, 10));

        String[] btnlbls = {"7", "8", "9", "4", "5", "6", "1", "2", "3", "00", "0", "."};
        for (int i = 0; i < btnlbls.length; i++) {
            JButton btn = new JButton(btnlbls[i]);
            pc.add(btn);
            btn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent e) {

                    String cmd = e.getActionCommand();
                    numStr += cmd;
                    number = Double.parseDouble(numStr);
                    display(number);
                    System.err.println("clicked " + cmd);
                }
            });
        }

        //--panel East--
        JPanel pe = new JPanel();
        pe.setLayout(new BoxLayout(pe, BoxLayout.Y_AXIS));

        pe.add(clearBtn);
        clearBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                number = 0;
                numStr = "";
                display(number);
            }

        });
        pe.add(enterBtn);
        enterBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                ArrayList<InputData> dataListEnt = new ArrayList<>();
                String cmd = e.getActionCommand();

                //(1)read Database ->(2)create array of data ->(3)insert data to database
                dataListEnt = controllDB.readDB();

                try {
                    System.out.println("insert " + dateStr + "," + property + "," + category + "," + number);
                    controllDB.insertDB(dateStr, property, category, number);
                } catch (SQLException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
                //Reset selected 
                number = 0;
                numStr = "";
                display(number);
                combo.setSelectedIndex(0);
            }
        });

        pe.add(listBtn);
        listBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                ListBtnClicked();

            }
        });

        pe.add(deleteBtn);
        deleteBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    controllDB.deleteDB(selectedRow);
                } catch (SQLException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
                selectedRow = 0;
                ListBtnClicked();
            }
        });

        //--Panel south--
        JPanel ps = new JPanel();
        ps.setLayout(new BoxLayout(ps, BoxLayout.Y_AXIS));

        sortGroupPanel.setBorder(new javax.swing.border.TitledBorder("SORTING"));
        sortGroupPanel.setLayout(new FlowLayout());

        //displayRadioBtn
        displayRadioBtn(propertyList.length, propertyList, propertyPanel, "Property");
        displayRadioBtn(categoryList.length - 1, categoryList, categoryPanel, "Category");

        //month    
        monthCombo = new JComboBox(monthList);
        monthPanel.setBorder(new javax.swing.border.TitledBorder("Month"));
        monthPanel.add(monthCombo);
        monthCombo.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<InputData> dataList = new ArrayList<>();
                dataList = controllDB.readDB();

                double totalExpence = 0.0;
                double totalIncome = 0.0;
                //reset tablemodel
                tableModel.setRowCount(0);

                String cmd = (String) monthCombo.getSelectedItem();
                int n = Integer.parseInt(cmd);
                for (int i = 0; i < dataList.size(); i++) {
                    //getting month
                    Date date = convertToDate(dataList.get(i).getDateStr());
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(date);
                    int month = (int) cal.get(Calendar.MONTH);
                    //n is from combobox, (n-1) because jan,feb -> show 0,1
                    if (month == n - 1) {

                        if (dataList.get(i).getProperty().matches("expence")) {

                            totalExpence += dataList.get(i).getAmount();

                        } else {

                            totalIncome = dataList.get(i).getAmount();;

                        }
                        Object[] obj = {dataList.get(i).getID(), dataList.get(i).getDateStr(), dataList.get(i).getProperty(), dataList.get(i).getCategory(), dataList.get(i).getAmount()};
                        tableModel.addRow(obj);
                    }
                }
                totalAmountLbl.setText("<Total Expence> $" + String.format("%.2f", totalExpence) + "  <Total Income> $" + String.format("%.2f", totalIncome));
            }
        });

        sortGroupPanel.add(monthPanel);
        ps.add(sortGroupPanel);
        totalAmountPanel.setPreferredSize(new Dimension(0, 30));
        totalAmountPanel.setBackground(Color.lightGray);
        totalAmountPanel.add(totalAmountLbl);
        ps.add(totalAmountPanel);
        tableModel = new DefaultTableModel(colName, 0);
        JTable table = new JTable(tableModel);

        table.setAutoCreateRowSorter(true);
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
            // do some actions here, for example
                // print first column value from selected row
//            System.out.println("1) " + table.getValueAt(table.getSelectedRow(), 0));
                //selectedRow = Integer.parseInt(table.getValueAt(table.getSelectedRow(), 0).toString());
                 selectedRow = table.getSelectedRow()+1;
//            selectedRow = (int) table.getValueAt(table.getSelectedRow(), 0).toString();
                System.out.println("number: " + selectedRow);

            }
        });
        JScrollPane scroll = new JScrollPane(table);

        scroll.setPreferredSize(new Dimension(600, 300));

        ps.add(scroll);

        getContentPane().add(pn, java.awt.BorderLayout.NORTH);
        getContentPane().add(pc, java.awt.BorderLayout.CENTER);
        getContentPane().add(pe, java.awt.BorderLayout.EAST);
        getContentPane().add(ps, java.awt.BorderLayout.SOUTH);
        pack();

        setVisible(true);
    }

    public void display(Double number) {
        displayLbl.setText("$" + number.toString());
    }

    public void ListBtnClicked() {
        System.out.println("ListBtnClicked");
        ArrayList<InputData> dataList = new ArrayList<>();
                dataList = controllDB.readDB();

                double totalExpence = 0.0;
                double totalIncome = 0.0;
                tableModel.setRowCount(0);
                for (int i = 0; i < dataList.size(); i++) {

                    if (dataList.get(i).getProperty().matches("expence")) {

                        totalExpence += dataList.get(i).getAmount();

                    } else {

                        totalIncome = dataList.get(i).getAmount();;

                    }
                    Object[] obj = {dataList.get(i).getID(), dataList.get(i).getDateStr(), dataList.get(i).getProperty(), dataList.get(i).getCategory(), dataList.get(i).getAmount()};

                    tableModel.addRow(obj);
                }
                totalAmountLbl.setText("<Total Expence> $" + String.format("%.2f", totalExpence) + " <Total Income> $" + String.format("%.2f", totalIncome));

    }

    public static Date convertToDate(String dateStr) {
        Date date = null;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
            date = dateFormat.parse(dateStr);
            System.out.println(date);

        } catch (ParseException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        return date;
    }

    public void displayRadioBtn(int num, String[] arr, JPanel panelName, String name) {
        radioBtns = new JRadioButton[num];
        for (int i = 0; i < num; i++) {
            radioBtns[i] = new JRadioButton("radioBtn" + i);
            radioBtns[i].setText(arr[i]);
            radioBtns[i].addActionListener(new RadioBtnsActionListener());
            btnGroup.add(radioBtns[i]);

            panelName.setBorder(new javax.swing.border.TitledBorder(name));
            panelName.setLayout(new BoxLayout(panelName, BoxLayout.Y_AXIS));

            panelName.add(radioBtns[i]);
            sortGroupPanel.add(panelName);

        }
    }

    class RadioBtnsActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("e.getActionCommand() is " + e.getActionCommand());

            ArrayList<InputData> dataList = new ArrayList<>();
            dataList = controllDB.readDB();

            double totalExpence = 0.0;
            double totalIncome = 0.0;

            tableModel.setRowCount(0);

            switch (e.getActionCommand()) {
                case "Expence":
                    for (int i = 0; i < dataList.size(); i++) {

                        if (dataList.get(i).getProperty().matches("expence")) {

                            totalExpence += dataList.get(i).getAmount();

                            Object[] obj = {dataList.get(i).getID(), dataList.get(i).getDateStr(), dataList.get(i).getProperty(), dataList.get(i).getCategory(), dataList.get(i).getAmount()};
                            tableModel.addRow(obj);
                        }
                    }
                    totalAmountLbl.setText("<Total Expence> $" + String.format("%.2f", totalExpence));
                    break;
                case "Income":
                    for (int i = 0; i < dataList.size(); i++) {

                        if (dataList.get(i).getProperty().matches("income")) {

                            totalIncome += dataList.get(i).getAmount();

                            Object[] obj = {dataList.get(i).getID(), dataList.get(i).getDateStr(), dataList.get(i).getProperty(), dataList.get(i).getCategory(), dataList.get(i).getAmount()};
                            tableModel.addRow(obj);
                        }
                    }
                    totalAmountLbl.setText(" <Total Income> $" + String.format("%.2f", totalIncome));
                    break;
                case "Rent":
                    for (int i = 0; i < dataList.size(); i++) {

                        if (dataList.get(i).getCategory().matches("Rent")) {

                            totalExpence += dataList.get(i).getAmount();
                            totalIncome += dataList.get(i).getAmount();

                            Object[] obj = {dataList.get(i).getID(), dataList.get(i).getDateStr(), dataList.get(i).getProperty(), dataList.get(i).getCategory(), dataList.get(i).getAmount()};
                            tableModel.addRow(obj);
                        }
                    }
                    totalAmountLbl.setText("<Total Expence> $" + String.format("%.2f", totalExpence));
                    break;
                case "Insurance":
                    for (int i = 0; i < dataList.size(); i++) {

                        if (dataList.get(i).getCategory().matches("Insurance")) {

                            totalExpence += dataList.get(i).getAmount();
                            totalIncome += dataList.get(i).getAmount();

                            Object[] obj = {dataList.get(i).getID(), dataList.get(i).getDateStr(), dataList.get(i).getProperty(), dataList.get(i).getCategory(), dataList.get(i).getAmount()};
                            tableModel.addRow(obj);
                        }
                    }
                    totalAmountLbl.setText("<Total Expence> $" + String.format("%.2f", totalExpence));
                    break;
                case "Food":
                    for (int i = 0; i < dataList.size(); i++) {

                        if (dataList.get(i).getCategory().matches("Food")) {

                            totalExpence += dataList.get(i).getAmount();
                            totalIncome += dataList.get(i).getAmount();

                            Object[] obj = {dataList.get(i).getID(), dataList.get(i).getDateStr(), dataList.get(i).getProperty(), dataList.get(i).getCategory(), dataList.get(i).getAmount()};
                            tableModel.addRow(obj);
                        }
                    }
                    totalAmountLbl.setText("<Total Expence> $" + String.format("%.2f", totalExpence));
                    break;
                default:
                    System.out.println("It went something wrong..!");
                    break;

            }

        }

    }

    public static void main(String[] args) {
        new Main();
    }

}
