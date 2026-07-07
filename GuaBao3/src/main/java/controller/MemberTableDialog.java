package controller;

import java.awt.Dimension;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import model.Member;
import service.GuaBaoService;

public class MemberTableDialog {
    public static void showMembers(java.awt.Component parent, GuaBaoService service, String title) {
        String[] columns = {"會員卡號", "累積點數"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        try {
            List<Member> list = service.getAllMembers();
            for (Member m : list) { 
                model.addRow(new Object[]{ m.getMemberId(), m.getTotalPoints() }); 
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(parent, "讀取資料庫錯誤: " + ex.getMessage()); 
            return;
        }
        JTable table = new JTable(model);
        table.setSelectionBackground(new java.awt.Color(255, 215, 175));
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(400, 300));
        JOptionPane.showMessageDialog(parent, scrollPane, title, JOptionPane.INFORMATION_MESSAGE);
    }
}