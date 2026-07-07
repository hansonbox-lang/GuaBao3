package controller;

import exception.GuaBaoException;
import java.awt.*;
import java.awt.event.*;
import java.awt.print.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import model.Employee;
import model.Member;
import model.OrderDetail;
import service.GuaBaoService;
import service.impl.GuaBaoServiceImpl;

public class GuaBao3 extends JFrame {

    // 改用介面型態接收商業邏輯層
    private GuaBaoService service = new GuaBaoServiceImpl();

    private static final Color COLOR_WHITE_BG = new Color(255, 255, 255);
    private static final Color COLOR_TEXT_BLUE = new Color(110, 140, 255);
    private static final Color COLOR_M_YELLOW = new Color(255, 255, 0);
    private static final Color COLOR_M_RED = new Color(255, 150, 150);
    private static final Color COLOR_M_ORANGE = new Color(255, 195, 100);
    private static final Color COLOR_M_SKIN = new Color(255, 215, 175);
    private static final Color COLOR_M_GREEN = new Color(195, 255, 155);
    private static final Color COLOR_M_PURPLE = new Color(255, 160, 255);
    private static final Color COLOR_M_LIGHT_Y = new Color(245, 250, 135);

    private String currentEmployeeName = null;
    private String currentEmployeeRole = null;
    private String currentMemberId = null;
    private int currentMemberPoints = 0;

    private JPanel cards;
    private CardLayout cardLayout;
    private JLabel lblLoginTime, lblOrderTime;

    private JTextField txtLoginUser;
    private JPasswordField txtLoginPass;
    private JButton btnLogin, btnLogout, btnGoOrder, btnGoMgmt;
    private JLabel lblLoginStatus;

    private JLabel lblOrderStatus;
    private int[] itemQuantities = new int[6];
    private int[] itemPrices = {70, 70, 70, 55, 55, 80};
    private String[] itemNames = {"綜合割包", "赤肉割包", "焢肉割包", "魚丸湯", "貢丸湯", "八寶湯"};
    private JLabel[] lblQuantities = new JLabel[6];
    private JButton[] btnMinusList = new JButton[6];
    private JTextArea txtOrderDetail;
    private JTextField txtMemCardNo, txtMemPoints;
    private JLabel lblMemMessage;
    private JButton btnMemLogin, btnMemLogout, btnMemAdd, btnMemQuery, btnMemUpdate, btnMemDelete;
    private JButton btnOrderLogout, btnClear, btnPrint, btnCheckout;

    public static void main(String[] args) {
        UIManager.put("Label.font", new Font("微軟正黑體", Font.BOLD, 14));
        UIManager.put("Button.font", new Font("微軟正黑體", Font.BOLD, 14));
        UIManager.put("TitledBorder.font", new Font("微軟正黑體", Font.BOLD, 14));
        
        EventQueue.invokeLater(() -> {
            try {
                GuaBao3 frame = new GuaBao3();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public GuaBao3() {
        setTitle("★ 好家割包總店 點餐系統 ★");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 930, 690);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        cards = new JPanel(cardLayout);
        cards.setBackground(COLOR_WHITE_BG);
        getContentPane().add(cards, BorderLayout.CENTER);

        initLoginPanel();
        initOrderPanel();
        startClock();
        updateLoginState();
    }

    /**
     * 提供給後台視窗呼叫，用以直接切換至前台點餐系統畫面
     */
    public void switchToOrderCard() {
        if (cardLayout != null && cards != null) {
            cardLayout.show(cards, "ORDER_CARD");
        }
    }    
    
    private void startClock() {
        Timer timer = new Timer(1000, e -> {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String now = sdf.format(new Date());
            lblLoginTime.setText("時間: " + now);
            if (lblOrderTime != null) lblOrderTime.setText("時間: " + now);
        });
        timer.start();
    }

    private void updateLoginState() {
        if (currentEmployeeName == null) {
            String statusText = "[ 員工未登入 ]";
            lblLoginStatus.setText(statusText); lblLoginStatus.setForeground(Color.RED);
            lblOrderStatus.setText(statusText); lblOrderStatus.setForeground(Color.RED);
            btnLogout.setEnabled(false); btnGoOrder.setEnabled(false); btnGoMgmt.setEnabled(false);
        } else {
            String statusText = "登入員工: " + currentEmployeeName + " [" + currentEmployeeRole + "]";
            lblLoginStatus.setText(statusText); lblLoginStatus.setForeground(new Color(0, 128, 64));
            lblOrderStatus.setText(statusText); lblOrderStatus.setForeground(new Color(0, 128, 64));
            btnLogout.setEnabled(true); btnGoOrder.setEnabled(true);
            btnGoMgmt.setEnabled("最高權限".equals(currentEmployeeRole));
        }
    }

    private void styleButton(JButton btn, Color backColor) {
        btn.setBackground(backColor);
        btn.setForeground(new Color(60, 60, 60));
        btn.setFocusPainted(false);
        btn.setBorder(new LineBorder(backColor.darker(), 1, true));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void initLoginPanel() {
        JPanel panel = new JPanel(null);
        panel.setBackground(COLOR_WHITE_BG);
        cards.add(panel, "LOGIN_CARD");

        JLabel lblTitle = new JLabel("好家割包總店 點餐系統登入", SwingConstants.CENTER);
        lblTitle.setFont(new Font("微軟正黑體", Font.BOLD, 26));
        lblTitle.setForeground(COLOR_TEXT_BLUE); 
        lblTitle.setBounds(200, 40, 500, 40);
        panel.add(lblTitle);

        lblLoginTime = new JLabel("系統時間: 載入中...", SwingConstants.CENTER);
        lblLoginTime.setForeground(Color.GRAY);
        lblLoginTime.setBounds(200, 90, 500, 20);
        panel.add(lblLoginTime);

        JPanel loginBox = new JPanel(null);
        loginBox.setBackground(COLOR_M_SKIN);
        loginBox.setBorder(new LineBorder(COLOR_M_ORANGE, 2, true));
        loginBox.setBounds(260, 140, 380, 160);
        panel.add(loginBox);

        JLabel lblUser = new JLabel("帳號:"); lblUser.setFont(new Font("微軟正黑體", Font.BOLD, 16)); lblUser.setBounds(40, 35, 80, 30); loginBox.add(lblUser);
        txtLoginUser = new JTextField(); txtLoginUser.setBounds(130, 35, 200, 30); txtLoginUser.setBorder(new LineBorder(Color.WHITE, 2, true)); loginBox.add(txtLoginUser);

        JLabel lblPass = new JLabel("密碼:"); lblPass.setFont(new Font("微軟正黑體", Font.BOLD, 16)); lblPass.setBounds(40, 95, 80, 30); loginBox.add(lblPass);
        txtLoginPass = new JPasswordField(); txtLoginPass.setBounds(130, 95, 200, 30); txtLoginPass.setBorder(new LineBorder(Color.WHITE, 2, true)); loginBox.add(txtLoginPass);

        lblLoginStatus = new JLabel("員工未登入", SwingConstants.CENTER);
        lblLoginStatus.setFont(new Font("微軟正黑體", Font.BOLD, 16));
        lblLoginStatus.setBounds(200, 320, 500, 30);
        panel.add(lblLoginStatus);

        btnLogin = new JButton("員工登入"); btnLogin.setBounds(180, 390, 120, 40); styleButton(btnLogin, COLOR_M_GREEN);
        btnLogout = new JButton("員工登出"); btnLogout.setBounds(320, 390, 120, 40); styleButton(btnLogout, COLOR_M_RED);
        btnGoOrder = new JButton("點餐系統"); btnGoOrder.setBounds(460, 390, 120, 40); styleButton(btnGoOrder, COLOR_M_YELLOW);
        btnGoMgmt = new JButton("員工管理"); btnGoMgmt.setBounds(600, 390, 120, 40); styleButton(btnGoMgmt, COLOR_M_PURPLE);

        panel.add(btnLogin); panel.add(btnLogout); panel.add(btnGoOrder); panel.add(btnGoMgmt);

        btnLogin.addActionListener(e -> {
            try {
                Employee emp = service.employeeLogin(txtLoginUser.getText().trim(), new String(txtLoginPass.getPassword()));
                currentEmployeeName = emp.getName();
                currentEmployeeRole = emp.getRole();
                JOptionPane.showMessageDialog(this, "登入成功！歡迎 " + currentEmployeeName);
                updateLoginState();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "登入失敗", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnLogout.addActionListener(e -> {
            currentEmployeeName = null; currentEmployeeRole = null;
            txtLoginUser.setText(""); txtLoginPass.setText("");
            JOptionPane.showMessageDialog(this, "已登出系統！");
            updateLoginState();
        });

        btnGoOrder.addActionListener(e -> cardLayout.show(cards, "ORDER_CARD"));
        
        // 進入抽離出的後台員工管理視窗
        btnGoMgmt.addActionListener(e -> {
            String statusText = "登入員工: " + currentEmployeeName + " [" + currentEmployeeRole + "]";
            EmployeeManagementFrame mgmtFrame = new EmployeeManagementFrame(this, service, statusText);
            mgmtFrame.setVisible(true);
            this.setVisible(false); // 隱藏主視窗
        });
    }

    private void initOrderPanel() {
        JPanel panel = new JPanel(null);
        panel.setBackground(COLOR_WHITE_BG);
        cards.add(panel, "ORDER_CARD");

        JLabel lblTitle = new JLabel("★ 好家割包總店 點餐系統 ★", SwingConstants.CENTER);
        lblTitle.setFont(new Font("微軟正黑體", Font.BOLD, 24));
        lblTitle.setForeground(COLOR_TEXT_BLUE); lblTitle.setBounds(200, 15, 500, 35); panel.add(lblTitle);

        lblOrderTime = new JLabel("系統時間: 載入中...", SwingConstants.CENTER);
        lblOrderTime.setForeground(Color.GRAY); lblOrderTime.setBounds(200, 50, 500, 20); panel.add(lblOrderTime);

        lblOrderStatus = new JLabel("員工未登入", SwingConstants.RIGHT);
        lblOrderStatus.setFont(new Font("微軟正黑體", Font.BOLD, 14)); lblOrderStatus.setBounds(550, 75, 300, 20); panel.add(lblOrderStatus);

        JPanel goodsPanel = new JPanel(null);
        goodsPanel.setBackground(new Color(245, 255, 240)); 
//        goodsPanel.setBorder(new TitledBorder(new LineBorder(COLOR_M_GREEN, 2, true), "商品點購區"));
        goodsPanel.setBorder(new TitledBorder(new LineBorder(COLOR_M_GREEN, 2, true), "商品點餐"));
        goodsPanel.setBounds(20, 100, 410, 320); panel.add(goodsPanel);

        for (int i = 0; i < 6; i++) {
            int yPos = 25 + (i * 45);
            JLabel lblItem = new JLabel(itemNames[i] + " (" + itemPrices[i] + "元)");
            lblItem.setBounds(20, yPos, 160, 30); goodsPanel.add(lblItem);

            JButton btnMinus = new JButton("-"); btnMinus.setBounds(185, yPos, 45, 30); styleButton(btnMinus, COLOR_M_RED); btnMinus.setEnabled(false); goodsPanel.add(btnMinus);
            btnMinusList[i] = btnMinus;

            JLabel lblQty = new JLabel("0", SwingConstants.CENTER); lblQty.setBounds(235, yPos, 40, 30); lblQty.setFont(new Font("Comic Sans MS", Font.BOLD, 16)); goodsPanel.add(lblQty);
            lblQuantities[i] = lblQty;

            JButton btnPlus = new JButton("+"); btnPlus.setBounds(280, yPos, 45, 30); styleButton(btnPlus, COLOR_M_YELLOW); goodsPanel.add(btnPlus);

            final int idx = i;
            btnPlus.addActionListener(e -> {
                itemQuantities[idx]++; lblQuantities[idx].setText(String.valueOf(itemQuantities[idx]));
                btnMinusList[idx].setEnabled(true); calculateOrderTotal();
            });
            btnMinus.addActionListener(e -> {
                if (itemQuantities[idx] > 0) {
                    itemQuantities[idx]--; lblQuantities[idx].setText(String.valueOf(itemQuantities[idx]));
                    if (itemQuantities[idx] == 0) btnMinusList[idx].setEnabled(false);
                    calculateOrderTotal();
                }
            });
        }
        
        txtOrderDetail = new JTextArea(); txtOrderDetail.setEditable(false);
        txtOrderDetail.setFont(new Font("Monospaced", Font.BOLD, 13)); txtOrderDetail.setBackground(new Color(255, 254, 250));
        JScrollPane scrollDetail = new JScrollPane(txtOrderDetail);
//        scrollDetail.setBorder(new TitledBorder(new LineBorder(COLOR_M_ORANGE, 2, true), "點餐明細框"));
        scrollDetail.setBorder(new TitledBorder(new LineBorder(COLOR_M_ORANGE, 2, true), "點餐明細"));
        scrollDetail.setBounds(450, 100, 440, 320); panel.add(scrollDetail);
        
        JPanel memberPanel = new JPanel(null);
        memberPanel.setBackground(new Color(255, 255, 245));
//        memberPanel.setBorder(new TitledBorder(new LineBorder(COLOR_M_LIGHT_Y, 2, true), "會員中心框"));
        memberPanel.setBorder(new TitledBorder(new LineBorder(COLOR_M_LIGHT_Y, 2, true), "會員中心"));
        memberPanel.setBounds(20, 430, 870, 130); panel.add(memberPanel);

//        JLabel lblRule = new JLabel("提示: 會員消費100元增加1點, 集滿10點贈送綜合割包1個");
        JLabel lblRule = new JLabel("提示: 會員消費滿100元增加1點, 集滿10點贈送綜合割包1個");
        lblRule.setForeground(new Color(220, 100, 50)); lblRule.setBounds(15, 18, 380, 20); memberPanel.add(lblRule);

        lblMemMessage = new JLabel(""); lblMemMessage.setForeground(COLOR_TEXT_BLUE); lblMemMessage.setBounds(400, 18, 450, 20); memberPanel.add(lblMemMessage);

        JLabel lblMemCard = new JLabel("會員卡號:"); lblMemCard.setBounds(15, 50, 65, 25); memberPanel.add(lblMemCard);
        txtMemCardNo = new JTextField(); txtMemCardNo.setBounds(85, 50, 120, 25); memberPanel.add(txtMemCardNo);

        JLabel lblMemPts = new JLabel("累積點數:"); lblMemPts.setBounds(220, 50, 65, 25); memberPanel.add(lblMemPts);
        txtMemPoints = new JTextField(); txtMemPoints.setBounds(290, 50, 80, 25); memberPanel.add(txtMemPoints);

        btnMemLogin = new JButton("會員登入"); btnMemLogin.setBounds(15, 90, 95, 25); styleButton(btnMemLogin, COLOR_M_GREEN); memberPanel.add(btnMemLogin);
        btnMemLogout = new JButton("會員登出"); btnMemLogout.setBounds(115, 90, 95, 25); styleButton(btnMemLogout, COLOR_M_RED); memberPanel.add(btnMemLogout);
        btnMemAdd = new JButton("新增會員"); btnMemAdd.setBounds(225, 90, 95, 25); styleButton(btnMemAdd, COLOR_M_YELLOW); memberPanel.add(btnMemAdd);
        btnMemQuery = new JButton("查詢點數"); btnMemQuery.setBounds(325, 90, 95, 25); styleButton(btnMemQuery, COLOR_M_SKIN); memberPanel.add(btnMemQuery);
        btnMemUpdate = new JButton("修改點數"); btnMemUpdate.setBounds(430, 90, 95, 25); styleButton(btnMemUpdate, COLOR_M_PURPLE); memberPanel.add(btnMemUpdate);
        btnMemDelete = new JButton("刪除會員"); btnMemDelete.setBounds(530, 90, 95, 25); styleButton(btnMemDelete, COLOR_M_RED); memberPanel.add(btnMemDelete);

        btnOrderLogout = new JButton("員工登出"); btnOrderLogout.setBounds(180, 575, 120, 38); styleButton(btnOrderLogout, COLOR_M_RED); panel.add(btnOrderLogout);
        btnClear = new JButton("清除明細"); btnClear.setBounds(320, 575, 120, 38); styleButton(btnClear, COLOR_M_SKIN); panel.add(btnClear);
        btnPrint = new JButton("列印 PDF"); btnPrint.setBounds(460, 575, 120, 38); styleButton(btnPrint, COLOR_M_PURPLE); panel.add(btnPrint);
        btnCheckout = new JButton("確認結帳"); btnCheckout.setBounds(600, 575, 140, 38); styleButton(btnCheckout, COLOR_M_GREEN); panel.add(btnCheckout);

        btnMemLogin.addActionListener(e -> {
            try {
                Member mem = service.memberLogin(txtMemCardNo.getText().trim());
                currentMemberId = mem.getMemberId();
                currentMemberPoints = mem.getTotalPoints();
                txtMemPoints.setText(String.valueOf(currentMemberPoints));
                lblMemMessage.setText("歡迎會員 " + currentMemberId + "，目前積點：" + currentMemberPoints + "點");
                calculateOrderTotal();
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage()); }
        });

        btnMemLogout.addActionListener(e -> {
            currentMemberId = null; currentMemberPoints = 0;
            txtMemCardNo.setText(""); txtMemPoints.setText(""); lblMemMessage.setText("");
            JOptionPane.showMessageDialog(this, "會員已登出！"); calculateOrderTotal();
        });

//        btnMemAdd.addActionListener(e -> {
//            try {
//                String card = txtMemCardNo.getText().trim();
//                service.addMember(card);
//                JOptionPane.showMessageDialog(this, "會員新增成功！");
//                // 呼叫拆分出去的 MemberTableDialog
//                MemberTableDialog.showMembers(this, service, "新增會員成功之名冊");
//            } catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage()); }
//        });

     // 請找到 GuaBao3.java 中 btnMemAdd 的事件監聽器，專門修改為以下內容：
        btnMemAdd.addActionListener(e -> {
            String card = txtMemCardNo.getText().trim();
            try {
                service.addMember(card);
                JOptionPane.showMessageDialog(this, "會員新增成功！");
                // 呼叫拆分出去的 MemberTableDialog
                MemberTableDialog.showMembers(this, service, "新增會員成功之名冊");
            } catch (Exception ex) {
                // 專門修改此處：捕捉重複卡號的例外訊息並轉換為中文
                String errorMsg = ex.getMessage();
                if (errorMsg != null && errorMsg.contains("Duplicate entry") && errorMsg.contains("members.PRIMARY")) {
                    JOptionPane.showMessageDialog(this, "會員卡號'" + card + "'已重複", "新增失敗", JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, ex.getMessage());
                }
            }
        });        
        
        // 呼叫拆分出去的 MemberTableDialog
        btnMemQuery.addActionListener(e -> MemberTableDialog.showMembers(this, service, "會員查詢點數列表"));
        
        btnMemUpdate.addActionListener(e -> {
            try {
                String card = txtMemCardNo.getText().trim();
                service.updateMemberPoints(card, txtMemPoints.getText().trim());
                if (card.equals(currentMemberId)) {
                    currentMemberPoints = Integer.parseInt(txtMemPoints.getText().trim());
                    lblMemMessage.setText("歡迎會員 " + currentMemberId + "，目前積點：" + currentMemberPoints + "點");
                }
                JOptionPane.showMessageDialog(this, "點數修改成功！");
                MemberTableDialog.showMembers(this, service, "修改後會員名冊"); calculateOrderTotal();
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage()); }
        });

        btnMemDelete.addActionListener(e -> {
            try {
                String card = txtMemCardNo.getText().trim();
                int confirm = JOptionPane.showConfirmDialog(this, "確定要刪除會員 " + card + " 嗎？", "確認刪除", JOptionPane.YES_NO_OPTION);
                if (confirm != JOptionPane.YES_OPTION) return;
                service.deleteMember(card);
                if(card.equals(currentMemberId)) {
                    currentMemberId = null; currentMemberPoints = 0;
                    txtMemCardNo.setText(""); txtMemPoints.setText(""); lblMemMessage.setText("");
                }
                JOptionPane.showMessageDialog(this, "會員刪除成功！");
                MemberTableDialog.showMembers(this, service, "刪除後會員名冊"); calculateOrderTotal();
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage()); }
        });

        btnOrderLogout.addActionListener(e -> {
            currentEmployeeName = null; currentEmployeeRole = null;
            txtLoginUser.setText(""); txtLoginPass.setText(""); updateLoginState();
            cardLayout.show(cards, "LOGIN_CARD"); JOptionPane.showMessageDialog(this, "已登出系統！");
        });

        btnClear.addActionListener(e -> clearOrderData());

        btnPrint.addActionListener(e -> {
            String receiptText = txtOrderDetail.getText();
            if (receiptText.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "目前無任何點餐明細可以輸出！", "提示", JOptionPane.WARNING_MESSAGE); return;
            }
            PrinterJob job = PrinterJob.getPrinterJob();
            job.setJobName("GuaBao_Receipt_PDF");
            job.setPrintable((graphics, pageFormat, pageIndex) -> {
                if (pageIndex > 0) return Printable.NO_SUCH_PAGE;
                Graphics2D g2d = (Graphics2D) graphics;
                g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
                g2d.setFont(new Font("細明體", Font.PLAIN, 11));
                String[] lines = receiptText.split("\n");
                int y = 30;
                for (String line : lines) {
                    if (line.contains("\t")) {
                        String[] cols = line.split("\t");
                        if (cols.length > 0) g2d.drawString(cols[0].trim(), 20, y);
                        if (cols.length > 1) g2d.drawString(cols[1].trim(), 140, y);
                        if (cols.length > 2) g2d.drawString(cols[2].trim(), 200, y);
                        if (cols.length > 3) g2d.drawString(cols[3].trim(), 260, y);
                    } else { g2d.drawString(line, 20, y); }
                    y += 18;
                }
                return Printable.PAGE_EXISTS;
            });
            if (job.printDialog()) {
                try { job.print(); JOptionPane.showMessageDialog(this, "PDF 檔案已成功輸出！"); } 
                catch (PrinterException ex) { JOptionPane.showMessageDialog(this, "輸出失敗: " + ex.getMessage()); }
            }
        });

        btnCheckout.addActionListener(e -> {
            int totalAmount = 0;
            List<OrderDetail> details = new ArrayList<>();
            for (int i = 0; i < 6; i++) {
                if (itemQuantities[i] > 0) {
                    int sub = itemQuantities[i] * itemPrices[i];
                    totalAmount += sub;
                    details.add(new OrderDetail(itemNames[i], itemPrices[i], itemQuantities[i], sub));
                }
            }
            if (details.isEmpty()) {
                JOptionPane.showMessageDialog(this, "購物車內無商品，無法進行結帳！", "提示", JOptionPane.WARNING_MESSAGE); return;
            }
            int confirm = JOptionPane.showConfirmDialog(this, "應付總金額為 $" + totalAmount + " 元，確定要進行結帳嗎？", "確認結帳", JOptionPane.YES_NO_OPTION);
            if (confirm != JOptionPane.YES_OPTION) return;

            try {
                int earnedPoints = totalAmount / 100;
                int generatedOrderId = service.checkout(currentMemberId, totalAmount, details, currentMemberPoints);
                
                int giftCount = 0;
                if (currentMemberId != null) {
                    int tempPoints = currentMemberPoints + earnedPoints;
                    if (tempPoints >= 10) {
                        giftCount = tempPoints / 10;
                        currentMemberPoints = tempPoints - (giftCount * 10);
                    } else {
                        currentMemberPoints = tempPoints;
                    }
                    txtMemPoints.setText(String.valueOf(currentMemberPoints));
                    lblMemMessage.setText("歡迎會員 " + currentMemberId + "，目前積點：" + currentMemberPoints + "點");
                }

                String bonusMessage = "";
                if (giftCount > 0) {
                    bonusMessage = "\n\n恭喜！會員總點數已達標！\n系統自動扣除點數，【免費贈送綜合割包 " + giftCount + " 個】！";
                    txtOrderDetail.append(bonusMessage);
                }
                String msg = "結帳成功並已存入資料庫！\n訂單編號: " + generatedOrderId;
                if (currentMemberId != null) msg += "\n本次消費獲得新積點: " + earnedPoints + " 點。" + bonusMessage;
                
                JOptionPane.showMessageDialog(this, msg, "結帳完成", JOptionPane.INFORMATION_MESSAGE);
                clearOrderData();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "結帳交易失敗: " + ex.getMessage(), "錯誤", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void clearOrderData() {
        for (int i = 0; i < 6; i++) {
            itemQuantities[i] = 0; lblQuantities[i].setText("0"); btnMinusList[i].setEnabled(false);
        }
        txtOrderDetail.setText("");
    }

    private void calculateOrderTotal() {
        int totalAmount = 0; int totalQuantity = 0;
        String purchaseTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        StringBuilder sb = new StringBuilder();
        sb.append("--- 好家割包總店 點餐明細 ---\n");
        sb.append("購買時間: ").append(purchaseTime).append("\n");
        sb.append(currentMemberId != null ? "會員卡號: " + currentMemberId + " (現有積點: " + currentMemberPoints + " 點)\n" : "會員卡號: 無(非會員消費)\n");
        sb.append("==========================================\n");
        sb.append(String.format("%-12s\t%-6s\t%-4s\t%-6s\n", "品項", "單價", "數量", "小計"));
        sb.append("------------------------------------------\n");
        for (int i = 0; i < 6; i++) {
            if (itemQuantities[i] > 0) {
                int sum = itemQuantities[i] * itemPrices[i];
                totalAmount += sum; totalQuantity += itemQuantities[i];
                sb.append(String.format("%-12s\t$%d\t%d\t$%d\n", itemNames[i], itemPrices[i], itemQuantities[i], sum));
            }
        }
        sb.append("------------------------------------------\n");
        sb.append("商品總計數量: ").append(totalQuantity).append(" 件\n");
        sb.append("本次消費預計可得新積點: ").append(totalAmount / 100).append(" 點\n");
        sb.append("應付總金額: $").append(totalAmount).append(" 元\n");
        sb.append("==========================================\n");
        txtOrderDetail.setText(sb.toString());
    }
}