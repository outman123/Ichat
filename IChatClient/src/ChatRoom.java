/**
 * Created by xuhuan on 2017/11/19.
 */

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.Socket;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

/*
* 该类实现了好友列表*/
class CellRenderer extends JLabel implements ListCellRenderer {
    CellRenderer() {
        setOpaque(true);//设置是否透明
    }

    public Component getListCellRendererComponent(JList list, Object value,
                                                  int index, boolean isSelected, boolean cellHasFocus) {

        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));// 加入宽度为5的空白边框

        if (value != null) {
            setText(value.toString());
            setIcon(new ImageIcon("sourse/头像.png"));
        }
        if (isSelected) {
            setBackground(new Color(255, 255, 153));// 设置背景色
            setForeground(Color.black);
        } else {
            // 设置选取与取消选取的前景与背景颜色.
            setBackground(Color.white); // 设置背景色
            setForeground(Color.black);
        }
        setEnabled(list.isEnabled());
        setFont(new Font("sdf", Font.ROMAN_BASELINE, 13));
        setOpaque(true);
        return this;
    }
}


class ListModel extends AbstractListModel{

    private Vector vs;

    public ListModel(Vector vs){
        this.vs = vs;
    }

    @Override
    public Object getElementAt(int index) {
        // TODO Auto-generated method stub
        return vs.get(index);
    }

    @Override
    public int getSize() {
        // TODO Auto-generated method stub
        return vs.size();
    }

}

/*
* 该类实现了会话框主要功能*/
public class ChatRoom extends JFrame implements ActionListener {

    private static final long serialVersionUID = 6129126482250125466L;

    private static JPanel main_jPanel;
    private static Socket clientSocket;
    private static ObjectOutputStream objectOutputStream;
    private static ObjectInputStream objectInputStream;
    private static String name;
    private static JTextArea showMessageTextArea;
    private static AbstractListModel listmodel;
    private static JList list;
    private static Vector onlines;

    JTextArea editMessageTextArea = new JTextArea();
    private JMenuBar mb1=new JMenuBar();
    private JMenu m1=new JMenu("file");
    private JMenu m2=new JMenu("color");
    private JMenu m3=new JMenu("font");
    private JMenu m4=new JMenu("state");
    private JMenuItem mi1=new JMenuItem("加载");
    private JMenuItem mi2=new JMenuItem("red");
    private JMenuItem mi3=new JMenuItem("yellow");
    private JMenuItem mi4=new JMenuItem("green");
    private JMenuItem mi5=new JMenuItem("字体1");
    private JMenuItem mi6=new JMenuItem("字体2");
    private JMenuItem mi7=new JMenuItem("字体3");
    private JMenuItem mi8=new JMenuItem("与服务器断开连接");
    private JMenuItem mi9=new JMenuItem("开始连接服务器");
    JButton sendMessageButton = new JButton("发送");
//    public static void main(String[] args) {
//        Socket client = null;
//        try {
//            client = new Socket("localhost", 8520);
//            ChatRoom chat=new ChatRoom("123",client);
//            chat.setVisible(true);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }
    /**
     * 创建会话界面.
     */

    public ChatRoom(String user_name, Socket client) {
        // 赋值
        name = user_name;
        clientSocket = client;
        onlines = new Vector();

        SwingUtilities.updateComponentTreeUI(this); //刷新UI界面

//        try {
//            String lookAndFeel = UIManager.getSystemLookAndFeelClassName();
//            UIManager.setLookAndFeel(lookAndFeel);
//        } catch (ClassNotFoundException e1) {
//            // TODO Auto-generated catch block
//            e1.printStackTrace();
//        } catch (InstantiationException e1) {
//            // TODO Auto-generated catch block
//            e1.printStackTrace();
//        } catch (IllegalAccessException e1) {
//            // TODO Auto-generated catch block
//            e1.printStackTrace();
//        } catch (UnsupportedLookAndFeelException e1) {
//            // TODO Auto-generated catch block
//            e1.printStackTrace();
//        }



        setTitle(name);
        setResizable(false); //不可自由改变大小（更新版本的时候，这个地方可做改进）
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setBounds(200, 100, 688, 510);
        main_jPanel = new JPanel() {
            private static final long serialVersionUID = 1L;
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(new ImageIcon("sourse/聊天背景.jpg").getImage(), 0, 0,
                        getWidth(), getHeight(), null);
            }

        };
        setContentPane(main_jPanel);
        main_jPanel.setLayout(null);

        // 聊天信息显示区域
        JScrollPane chatMessageScrollPane = new JScrollPane();
        chatMessageScrollPane.setBounds(260, 10, 410, 320);
        getContentPane().add(chatMessageScrollPane);

        showMessageTextArea = new JTextArea();
        showMessageTextArea.setEditable(false);
        showMessageTextArea.setLineWrap(true);//可以自动换行
        showMessageTextArea.setWrapStyleWord(true);//断行不断字
        showMessageTextArea.setFont(new Font("sdf", Font.BOLD, 13));
        chatMessageScrollPane.setViewportView(showMessageTextArea);   //将showMessageTextArea添加到滚动窗口内

        mb1.add(m1);mb1.add(m2);mb1.add(m3);mb1.add(m4);
        m1.add(mi1);
        m2.add(mi2);m2.add(mi3);m2.add(mi4);
        m3.add(mi5);m3.add(mi6);m3.add(mi7);
        m4.add(mi8);m4.add(mi9);
        mi1.addActionListener(this);
        mi2.addActionListener(this);
        mi3.addActionListener(this);
        mi4.addActionListener(this);
        mi5.addActionListener(this);
        mi6.addActionListener(this);
        mi7.addActionListener(this);
        mi8.addActionListener(this);
        mi9.addActionListener(this);
        mb1.setBounds(260,330,410,30);
        getContentPane().add(mb1);
        // 消息编辑区域
        JScrollPane editMessageScrollPane = new JScrollPane();
        editMessageScrollPane.setBounds(260, 360, 410, 80);
        getContentPane().add(editMessageScrollPane);

        
        editMessageTextArea.setLineWrap(true);//可以自动换行
        editMessageTextArea.setWrapStyleWord(true);//设置断行不断字
        editMessageScrollPane.setViewportView(editMessageTextArea);

        // 关闭按钮
        final JButton closeWinButton = new JButton("关闭");
        closeWinButton.setBounds(214, 400, 60, 30);
//        getContentPane().add(closeWinButton);

        // 发送按钮

        sendMessageButton.setBounds(610, 448, 60, 30);
        getRootPane().setDefaultButton(sendMessageButton);
        getContentPane().add(sendMessageButton);

        // 在线用户列表
        listmodel = new ListModel(onlines) ;
        list = new JList(listmodel);
        list.setCellRenderer(new CellRenderer());  //调用set方法为列表绘制组件（该组件为自定义的CellRenderer）
        list.setOpaque(false);
        Border etchedBorder = BorderFactory.createEtchedBorder();  //调用BorderFactory中的方法创建一个四周有凹痕的边界
        //建立一个设置了标题名称，位置，字体等参数的标题边界
        list.setBorder(BorderFactory.createTitledBorder(etchedBorder, "在线好友列表", TitledBorder.LEADING, TitledBorder.TOP, new Font(
                "sdf", Font.BOLD, 20), Color.CYAN));

        JScrollPane onlineUserScrollPane = new JScrollPane(list);
        onlineUserScrollPane.setBounds(10, 10, 240, 450);
        onlineUserScrollPane.setOpaque(false);
        onlineUserScrollPane.getViewport().setOpaque(false);
        getContentPane().add(onlineUserScrollPane);



        try {
            objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            // 记录上线客户的信息在Connection中，并发送给服务器
            Connection con = new Connection();
            con.setType(0);
            con.setName(name);
            con.setTimer(LoadUser.getTimer());
            objectOutputStream.writeObject(con);
            objectOutputStream.flush();


            // 启动客户接收线程
            new ClientInputThread().start();

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,"向服务器放消息失败");
            e.printStackTrace();
        }

        // 发送按钮事件监听
        sendMessageButton.addActionListener(this);

        // 关闭按钮
        closeWinButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                    closeWinButton.setEnabled(false);
                    Connection clientBean = new Connection();
                    clientBean.setType(-1);
                    clientBean.setName(name);
                    clientBean.setTimer(LoadUser.getTimer());
                    sendMessage(clientBean);
            }
        });

        // 离开
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // TODO Auto-generated method stub
                    int result = JOptionPane.showConfirmDialog(getContentPane(),
                            "您确定要离开聊天室");
                    if (result == 0) {
                        Connection clientConnection = new Connection();
                        clientConnection.setType(-1);
                        clientConnection.setName(name);
                        clientConnection.setTimer(LoadUser.getTimer());
                        sendMessage(clientConnection);
                    }

            }
        });

        // 列表监听
        list.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                List SelectedTo = list.getSelectedValuesList();
                if (e.getClickCount() == 2) {

                    if (SelectedTo.toString().contains(name+"(自己)")) {
                        JOptionPane
                                .showMessageDialog(getContentPane(), "不能向自己发送文件");
                        return;
                    }
                }
            }
        });

    }
    //事件监听
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==mi1){
            JFileChooser jfc = new JFileChooser();
            jfc.showOpenDialog(jfc);
            File f = jfc.getSelectedFile();
            String s="";
            try {
                if (f != null) {
                    BufferedReader br = new BufferedReader(new FileReader(f.getAbsoluteFile()));
                    if ((s=br.readLine()) != null)
                        editMessageTextArea.setText(s);
                }
            } catch (FileNotFoundException e1) {
                System.out.println("找不到文件");
            } catch (IOException e1) {
                System.out.println("输入输出错误");
            }
        }

        if(e.getSource()==mi2){
            editMessageTextArea.setBackground(Color.red);
        }
        if(e.getSource()==mi3){
            editMessageTextArea.setBackground(Color.yellow);
        }
        if(e.getSource()==mi4){
            editMessageTextArea.setBackground(Color.blue);
        }
        if(e.getSource()==mi5){
            editMessageTextArea.setFont(new Font("宋体",Font.BOLD,15));
        }
        if(e.getSource()==mi6){
            editMessageTextArea.setFont(new Font("黑体", Font.BOLD,15));
        }
        if(e.getSource()==mi7){
            editMessageTextArea.setFont(new Font("微软雅黑",Font.BOLD,15));
        }


        if(e.getSource()==mi8){
            try {
                clientSocket.close();
                objectInputStream.close();
                objectOutputStream.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        if(e.getSource()==sendMessageButton){
            String info = editMessageTextArea.getText();
            List selectedTo = list.getSelectedValuesList();

            if (selectedTo.size() < 1) {

                Connection clients=new Connection();
                clients.setType(2);
                clients.setName(name);
                String time = LoadUser.getTimer();
                clients.setTimer(time);
                clients.setInfo(info);



                //showMessageTextArea.append(time + " 我说:\r" + info + "\r\n");



                sendMessage(clients);
                editMessageTextArea.setText(null);
                editMessageTextArea.requestFocus();
                return;
            }
            if (selectedTo.toString().contains(name+"(自己)")) {
                JOptionPane.showMessageDialog(getContentPane(), "不能向自己发送信息");
                return;
            }
            if (info.equals("")) {
                JOptionPane.showMessageDialog(getContentPane(), "不能发送空信息");
                return;
            }

            Connection clientCon = new Connection();
            clientCon.setType(1);
            clientCon.setName(name);
            String time = LoadUser.getTimer();
            clientCon.setTimer(time);
            clientCon.setInfo(info);
            HashSet set = new HashSet();
            set.addAll(selectedTo);
            clientCon.setClients(set);

            // 自己发的内容也要现实在自己的屏幕上面
//            showMessageTextArea.append(time + " 我对"+clientCon.getName()+"说:\r" + info + "\r\n");

            sendMessage(clientCon);
            editMessageTextArea.setText(null);
            editMessageTextArea.requestFocus();
        }



    }


    //接收消息线程
    class ClientInputThread extends Thread {

        @Override
        public void run() {
            try {
                // 不停的从服务器接收信息
                while (true) {
                    objectInputStream = new ObjectInputStream(clientSocket.getInputStream());
                    final Connection  connection = (Connection) objectInputStream.readObject();
                    switch (connection.getType()) {
                        case -1: {

                            return;
                        }
                        case 0: {
                            // 更新列表
                            onlines.clear();
                            HashSet<String> clients = connection.getClients();
                            Iterator<String> it = clients.iterator();
                            while (it.hasNext()) {
                                String ele = it.next();
                                if (name.equals(ele)) {
                                    onlines.add(ele + "(自己)");
                                } else {
                                    onlines.add(ele);
                                }
                            }

                            listmodel = new ListModel(onlines);
                            list.setModel(listmodel);
                            showMessageTextArea.append(connection.getInfo() + "\r\n");
                            showMessageTextArea.selectAll();
                            break;
                        }
                        case 1: {

                            String info = connection.getTimer() + "  " + connection.getName()
                                    + "对我说:\r";
                            if (info.contains(name) ) {
                                info = info.replace(name, "我");
                            }
                            showMessageTextArea.append(info+connection.getInfo() + "\r\n");
                            showMessageTextArea.selectAll();
                            break;
                        }
                        //
                        case 2: {
                            String info = connection.getTimer() + "  " + connection.getName()
                                    + "说:\r";
                            if (info.contains(name) ) {
                                info = info.replace(name, "我");
                            }
                            showMessageTextArea.append(info+connection.getInfo() + "\r\n");
                            showMessageTextArea.selectAll();
                            break;
                        }
                        case 3: {}

                        case 4: {

                            break;
                        }
                        default: {
                            break;
                        }
                    }

                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                if (clientSocket != null) {
                    try {
                        clientSocket.close();
                    } catch (IOException e) {

                        e.printStackTrace();
                    }
                }
                System.exit(0);
            }
        }
    }
    //发送消息
    private void sendMessage(Connection clientBean) {
        try {
            objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            objectOutputStream.writeObject(clientBean);
            objectOutputStream.flush();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,"发送消息失败");
//            System.exit(0);
            e.printStackTrace();
        }
    }

}
