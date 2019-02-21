/**
 * Created by xuhuan on 2017/11/19.
 */

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Properties;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;


public class Login implements ActionListener {

    private JFrame f=new JFrame("登陆界面");

    private JPanel p0=new JPanel();
    private JPanel p1=new JPanel();
    private JPanel p2=new JPanel();
    private JPanel p3=new JPanel();
    private JButton loginButton=new JButton("登陆");
    private JButton registerButton=new JButton("注册");
    private JTextField textField;
    private JPasswordField passwordField;
    private JLabel l[]=new JLabel[2];



    /**
     * 登录界面设计
     */
    public Login() {
        f.setSize(330,230);
        loginButton.setSize(10,2);
        registerButton.setSize(10,2);
        loginButton.addActionListener(this);
        registerButton.addActionListener(this);

        Font font=new Font("黑体", 1, 15);
        loginButton.setFont(font);loginButton.setForeground(SystemColor.blue);
        loginButton.setContentAreaFilled(false);
        registerButton.setFont(font);registerButton.setForeground(SystemColor.blue);
        registerButton.setContentAreaFilled(false);
        JLabel l1=new JLabel("账号");
        l1.setFont(font);l1.setForeground(Color.red);
        JLabel l2=new JLabel("密码");
        l2.setFont(font);l2.setForeground(Color.red);
        textField=new JTextField(15);
        passwordField=new JPasswordField(15);
        p0.setOpaque(false);
        p1.setOpaque(false);
        p2.setOpaque(false);
        p3.setOpaque(false);



        ImageIcon image=new ImageIcon("sourse/注册.png");
        JLabel l=new JLabel();
        Image img = image.getImage();
        img = img.getScaledInstance(f.getWidth(), f.getHeight(), Image.SCALE_DEFAULT);
        image.setImage(img);
        l.setIcon(image);
        l.setBounds(0,0,image.getIconWidth(),image.getIconHeight());
        f.getLayeredPane().add(l,new Integer(Integer.MIN_VALUE));
        Container con=f.getContentPane();

        con.setLayout(new BorderLayout());
        p0.setBorder(new TitledBorder("请输入"));
        p0.setLayout(new GridLayout(2,1));
        p1.add(l1);p1.add(textField);
        p2.add(l2);p2.add(passwordField);
        p3.add(loginButton);p3.add(registerButton);
        p0.add(p1);p0.add(p2);
        con.add(p0,BorderLayout.CENTER);
        con.add(p3,BorderLayout.SOUTH);
        ((JPanel)con).setOpaque(false);



        f.setLocationRelativeTo(null);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
    }
    /**
     * main()方法，启动程序.
     */
    public static void main(String[] args) {

        // 启动登陆界面
        Login f = new Login();
    }

    protected void displayError(String str) {
        JOptionPane.showMessageDialog(f,str);
    }


    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==loginButton){
            Properties userPro = new Properties();
            File file = new File("Users.properties");//创建配置文件保存用户账号信息
            LoadUser.loadPro(userPro, file);
            String user_name = textField.getText();
            if (file.length() != 0) {

                if (userPro.containsKey(user_name)) {
                    String user_pwd = new String(passwordField.getPassword());
                    if (user_pwd.equals(userPro.getProperty(user_name))) {

                        try {
                            Socket client = new Socket("localhost", 8520);

                            loginButton.setEnabled(false);
                            ChatRoom frame = new ChatRoom(user_name,
                                    client);
                            frame.setVisible(true);// 显示聊天界面
                            f.setVisible(false);// 隐藏掉登陆界面

                        } catch (UnknownHostException e1) {
                            // TODO Auto-generated catch block
                            displayError("未连接服务器");
                        } catch (IOException e1) {
                            // TODO Auto-generated catch block
                            displayError("未连接服务器");
                            System.exit(0);
                        }

                    } else {
                        displayError("您输入的密码有误！");
                        passwordField.setText("");
                        textField.requestFocus();
                    }
                } else {
                    displayError("您输入昵称不存在！");
                    textField.setText("");
                    passwordField.setText("");
                    textField.requestFocus();
                }
            } else {
                displayError("您输入昵称不存在！");
                textField.setText("");
                passwordField.setText("");
                textField.requestFocus();
            }
        }
        if(e.getSource()==registerButton){
            registerButton.setEnabled(false);
            UserRegister frame = new UserRegister();
            f.setVisible(false);// 隐藏掉登陆界面
        }
    }

}