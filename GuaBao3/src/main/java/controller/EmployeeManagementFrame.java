package controller;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import model.Employee;
import service.GuaBaoService;

public class EmployeeManagementFrame extends JFrame {
    private GuaBaoService service;
    private JFrame mainFrame;

    private JTextField txtEmpId, txtEmpName;
    private JPasswordField txtEmpPass;
    private JComboBox<String> cbEmpRole;
    private JTable empTable;
    private DefaultTableModel empTableModel;

    public EmployeeManagementFrame(JFrame mainFrame, GuaBaoService service, String statusMsg) {
        this.mainFrame = mainFrame;
        this.service = service;

        setTitle("好家割包總店 員工管理後台");
        setBounds(100, 100, 930, 630);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JPanel panel = new JPanel(null);
        panel.setBackground(Color.WHITE);
        getContentPane().add(panel, BorderLayout.CENTER);

        JLabel lblTitle = new JLabel("好家割包總店 員工管理", SwingConstants.CENTER);
        lblTitle.setFont(new Font("微軟正黑體", Font.BOLD, 24));
        lblTitle.setForeground(new Color(110, 140, 255)); 
        lblTitle.setBounds(200, 20, 500, 35); 
        panel.add(lblTitle);

        JLabel lblMgmtStatus = new JLabel(statusMsg, SwingConstants.RIGHT);
        lblMgmtStatus.setFont(new Font("微軟正黑體", Font.BOLD, 14)); 
        lblMgmtStatus.setForeground(new Color(0, 128, 64));
        lblMgmtStatus.setBounds(550, 70, 300, 20); 
        panel.add(lblMgmtStatus);

        JPanel inputPanel = new JPanel(null);
        inputPanel.setBackground(new Color(255, 255, 240));
        inputPanel.setBorder(new TitledBorder(new LineBorder(new Color(245, 250, 135), 2, true), "員工資料維護"));
        inputPanel.setBounds(30, 110, 270, 350); 
        panel.add(inputPanel);

        JLabel lblId = new JLabel("員工帳號:"); lblId.setBounds(15, 35, 80, 25); inputPanel.add(lblId);
        txtEmpId = new JTextField(); txtEmpId.setBounds(95, 35, 150, 25); inputPanel.add(txtEmpId);

        JLabel lblPass = new JLabel("員工密碼:"); lblPass.setBounds(15, 80, 80, 25); inputPanel.add(lblPass);
        txtEmpPass = new JPasswordField(); txtEmpPass.setBounds(95, 80, 150, 25); inputPanel.add(txtEmpPass);

        JLabel lblName = new JLabel("員工姓名:"); lblName.setBounds(15, 125, 80, 25); inputPanel.add(lblName);
        txtEmpName = new JTextField(); txtEmpName.setBounds(95, 125, 150, 25); inputPanel.add(txtEmpName);

        JLabel lblRole = new JLabel("員工權限:"); lblRole.setBounds(15, 170, 80, 25); inputPanel.add(lblRole);
        cbEmpRole = new JComboBox<>(new String[]{"一般權限", "最高權限"}); cbEmpRole.setBounds(95, 170, 150, 25); inputPanel.add(cbEmpRole);

        JButton btnEmpAdd = new JButton("新增"); btnEmpAdd.setBounds(20, 230, 105, 35); styleButton(btnEmpAdd, new Color(195, 255, 155)); inputPanel.add(btnEmpAdd);
        JButton btnEmpQuery = new JButton("查詢"); btnEmpQuery.setBounds(140, 230, 105, 35); styleButton(btnEmpQuery, new Color(255, 195, 100)); inputPanel.add(btnEmpQuery);
        JButton btnEmpUpdate = new JButton("修改"); btnEmpUpdate.setBounds(20, 285, 105, 35); styleButton(btnEmpUpdate, new Color(255, 215, 175)); inputPanel.add(btnEmpUpdate);
        JButton btnEmpDelete = new JButton("刪除"); btnEmpDelete.setBounds(140, 285, 105, 35); styleButton(btnEmpDelete, new Color(255, 150, 150)); inputPanel.add(btnEmpDelete);

        String[] columns = {"員工帳號", "員工姓名", "員工權限"};
        empTableModel = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        empTable = new JTable(empTableModel);
        empTable.setSelectionBackground(new Color(255, 215, 175));
        empTable.setRowHeight(22);
        
        JTableHeader header = empTable.getTableHeader();
        header.setBackground(new Color(110, 140, 255)); header.setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(empTable);
        scrollPane.setBorder(new LineBorder(new Color(110, 140, 255), 2, true));
        scrollPane.setBounds(320, 110, 550, 350); 
        panel.add(scrollPane);

        empTable.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                int row = empTable.getSelectedRow();
                if (row != -1) {
                    try {
                        Employee emp = service.getEmployeeById(empTable.getValueAt(row, 0).toString());
                        if (emp != null) {
                            txtEmpId.setText(emp.getEmployeeId());
                            txtEmpPass.setText(emp.getPassword());
                            txtEmpName.setText(emp.getName());
                            cbEmpRole.setSelectedItem(emp.getRole());
                        }
                    } catch (Exception ex) { ex.printStackTrace(); }
                }
            }
        });

	     // 修改前：JButton btnToOrder = new JButton("返回點餐系統");
	     // 修改後如下：
	     JButton btnToOrder = new JButton("點餐系統"); 
	     btnToOrder.setBounds(380, 500, 180, 40); 
	     styleButton(btnToOrder, new Color(255, 255, 0)); 
	     panel.add(btnToOrder);

	  // 專門修改此處的監聽器內容：
	     btnToOrder.addActionListener(e -> {
	         // 1. 透過呼叫主視窗的方法，讓主視窗直接切換到前台點餐卡片
	         if (mainFrame instanceof GuaBao3) {
	             ((GuaBao3) mainFrame).switchToOrderCard();
	         }
	         
	         // 2. 顯示主視窗並關閉目前的後台視窗
	         mainFrame.setVisible(true);
	         this.dispose();
	     });

	     // 監聽視窗關閉事件，點選叉叉也能回主頁面
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                mainFrame.setVisible(true);
            }
        });

//        btnEmpAdd.addActionListener(e -> {
//            try {
//                Employee emp = new Employee(txtEmpId.getText().trim(), new String(txtEmpPass.getPassword()), txtEmpName.getText().trim(), cbEmpRole.getSelectedItem().toString());
//                service.addEmployee(emp);
//                JOptionPane.showMessageDialog(this, "新增員工成功！");
//                loadEmployeeTableData();
//            } catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage()); }
//        });

     // 請找到 EmployeeManagementFrame.java 中 btnEmpAdd 的事件監聽器，專門修改為以下內容：
        btnEmpAdd.addActionListener(e -> {
            String empId = txtEmpId.getText().trim();
            try {
                Employee emp = new Employee(empId, new String(txtEmpPass.getPassword()), txtEmpName.getText().trim(), cbEmpRole.getSelectedItem().toString());
                service.addEmployee(emp);
                JOptionPane.showMessageDialog(this, "新增員工成功！");
                loadEmployeeTableData();
            } catch (Exception ex) {
                // 專門修改此處：捕捉重複帳號的例外訊息並轉換為中文
                String errorMsg = ex.getMessage();
                if (errorMsg != null && errorMsg.contains("Duplicate entry") && errorMsg.contains("employees.PRIMARY")) {
                    JOptionPane.showMessageDialog(this, "員工帳號'" + empId + "'已重複", "新增失敗", JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, errorMsg);
                }
            }
        });        
        
        btnEmpQuery.addActionListener(e -> {
            try {
                List<Employee> list = service.queryEmployees(txtEmpId.getText().trim());
                empTableModel.setRowCount(0);
                for (Employee emp : list) {
                    empTableModel.addRow(new Object[]{emp.getEmployeeId(), emp.getName(), emp.getRole()});
                }
            } catch (Exception ex) { ex.printStackTrace(); }
        });

        btnEmpUpdate.addActionListener(e -> {
            try {
                Employee emp = new Employee(txtEmpId.getText().trim(), new String(txtEmpPass.getPassword()), txtEmpName.getText().trim(), cbEmpRole.getSelectedItem().toString());
                service.updateEmployee(emp);
                JOptionPane.showMessageDialog(this, "修改員工成功！");
                loadEmployeeTableData();
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage()); }
        });

        btnEmpDelete.addActionListener(e -> {
            try {
                String id = txtEmpId.getText().trim();
                int confirm = JOptionPane.showConfirmDialog(this, "確定要刪除此員工？", "確認刪除", JOptionPane.YES_NO_OPTION);
                if (confirm != JOptionPane.YES_OPTION) return;
                service.deleteEmployee(id);
                JOptionPane.showMessageDialog(this, "刪除員工成功！");
                loadEmployeeTableData();
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage()); }
        });

        loadEmployeeTableData();
    }

    private void styleButton(JButton btn, Color backColor) {
        btn.setBackground(backColor);
        btn.setForeground(new Color(60, 60, 60));
        btn.setFocusPainted(false);
        btn.setBorder(new LineBorder(backColor.darker(), 1, true));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void loadEmployeeTableData() {
        try {
            List<Employee> list = service.getAllEmployees();
            empTableModel.setRowCount(0);
            for (Employee emp : list) {
                empTableModel.addRow(new Object[]{ emp.getEmployeeId(), emp.getName(), emp.getRole() });
            }
        } catch (Exception ex) { ex.printStackTrace(); }
    }
}