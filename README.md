# 好家割包總店 點餐與員工管理系統 (GuaBao Management System)

本專案是一套專為割包店設計的 **POS 前台點餐系統** 與 **後台員工資料維護系統**。系統基於 Java Swing 進行視窗介面開發，並嚴格遵循標準的 **MVC + DAO (Model-View-Controller + Data Access Object) 建築模式** 進行分層設計，透過 Maven 進行專案依賴管理，核心資料庫則採用 MySQL。

[點我前往 GuaBao3 專案](https://github.com/hansonbox-lang/GuaBao3#)

## 🌟 系統三大核心視窗功能

1. **員工登入視窗 (`GuaBao3` - LOGIN_CARD)**
   - 提供系統安全驗證，員工需輸入正確的「帳號」與「密碼」方可進入系統。
   - 系統依據登入員工的權限別（一般權限 / 最高權限）動態控管功能。
   - 具備即時系統時間時鐘顯示。

2. **前台點餐系統視窗 (`GuaBao3` - ORDER_CARD)**
   - **商品點購區**：提供熱門品項（綜合割包、赤肉割包、焢肉割包、魚丸湯、貢丸湯、八寶湯）的增減點購，具備防呆機制（數量為 0 時自動禁用減號按鈕）。
   - **點餐明細框**：即時計算商品數量、單價、小計、預計新積點與應付總金額，支援「清除明細」功能。
   - **會員中心**：
     - 支援會員登入、登出、新增、刪除、修改點點數與彈出式完整名冊查詢。
     - 整合結帳回饋邏輯：消費滿 100 元積 1 點；集滿 10 點時，結帳系統會自動觸發扣除機制並**免費贈送綜合割包**。
   - **列印 PDF**：支援將當前點餐明細以標準收據格式輸出至 PDF 檔案。

3. **後台員工管理視窗 (`EmployeeManagementFrame`)**
   - 僅限「最高權限」員工進入。
   - 支援員工帳號、密碼、姓名與權限（一般權限/最高權限）的全面 **CRUD (增刪查改)** 操作。
   - 表格具備滑鼠點擊事件監聽，點選任意列即可將資料自動填入維護欄位。
   - 按下「點餐系統」按鈕可無縫導回前台點餐畫面。

---

## 📂 專案目錄結構 (Maven + MVC+dao Pattern)

專案嚴格遵循介面（Interface）與實作（Implementation）分離架構，目錄規劃如下：

```text
src/main/java
 ├── model                 # 資料實體層 (Domain Object / POJO)
 │    ├── Employee.java        - 員工資料模型
 │    ├── Member.java          - 會員資料模型
 │    ├── Order.java           - 訂單主檔模型
 │    └── OrderDetail.java     - 訂單明細模型
 ├── dao                   # 數據訪問層 - 介面定義
 │    ├── EmployeeDao.java     - 員工資料庫操作介面
 │    ├── MemberDao.java       - 會員資料庫操作介面
 │    └── OrderDao.java        - 結帳與訂單交易介面
 ├── dao.impl              # 數據訪問層 - 實作類別
 │    ├── EmployeeDaoImpl.java - 實作 EmployeeDao (JDBC 操作)
 │    ├── MemberDaoImpl.java   - 實作 MemberDao (JDBC 操作)
 │    └── OrderDaoImpl.java    - 實作 OrderDao (處理 ACID 事務交易)
 ├── service               # 商業邏輯層 - 介面定義
 │    └── GuaBaoService.java   - 系統核心業務邏輯介面
 ├── service.impl          # 商業邏輯層 - 實作類別
 │    └── GuaBaoServiceImpl.java - 實作業務封裝，呼叫 DAO 層
 ├── controller            # 控制層與 UI 視窗
 │    ├── GuaBao3.java               - 主程式進入點 / 登入卡片 / 前台點餐卡片
 │    ├── EmployeeManagementFrame.java - 獨立的後台員工管理視窗
 │    └── MemberTableDialog.java      - 獨立的會員名冊彈出對話框
 ├── util                  # 工具類別
 │    └── DbUtil.java          - 資料庫連接管理 (Connection Pool/Driver)
 └── exception             # 自訂異常處理
      └── GuaBaoException.java - 系統業務自訂異常類別
