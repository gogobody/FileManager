package main;

//import view.FileNode;

import component.circleprogressbar.CircleProgressBar;
import component.circleprogressbar.Cpu;
import config.Constant;
import control.FileEncAndDec;
import control.FileOperate;
import control.ZipUtil;
import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper;
import view.*;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import javax.swing.plaf.FontUIResource;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.List;
import java.util.Timer;

import org.hyperic.sigar.CpuInfo;
import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.Mem;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.Swap;

public class FileManager {
    public static String[] FILEPATH = {"文稿", "下载", "桌面", "文稿", "下载"};
    public static String RButtonSelect = "";
    public static String localPath = "";
    private FileOperate operate = new FileOperate();
    private static FileTree fileTree = new FileTree();
    public JScrollPane scoll;
    private MouseRightPopup popup = new MouseRightPopup();
    private String path1 = ""; //临时目录
    private JPanel topPanel;
    private JButton new_file;
    private JButton back;
    private JButton forward;
    private JTextField input_filed;
    private JPanel rootPanel;
    private JPanel panel_right_al;
    private JPanel panel_right;
    private JPanel left_top_panel;
    private JPanel left_down_panel;
    private JPanel left_panel;
    private JSplitPane splitpanel;
    private JButton new_dir;
    private JLabel file_info;
    private JLabel enter;
    private JPanel midpanel;
    private TreePath mouseInPath;

    private FileManager(String str) throws Exception {

        init(str);
    }

    public static void main(String[] args) throws Exception {
        FileSystemView rootview = FileSystemView.getFileSystemView();
        File root = rootview.getDefaultDirectory();        //获取初始系统默认
        InitGlobalFont(new Font("微软雅黑", Font.PLAIN, 18));
        try {
            org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper.launchBeautyEyeLNF();
            BeautyEyeLNFHelper.frameBorderStyle = BeautyEyeLNFHelper.FrameBorderStyle.translucencySmallShadow;
            org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper.launchBeautyEyeLNF();
            UIManager.put("RootPane.setupButtonVisible", false);
            //BeautyEyeLNFHelper.translucencyAtFrameInactive = false;
            //UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("set skin fail!");
        }
        JFrame frame = new JFrame("文件管理器");
        frame.setContentPane(new FileManager(root.getPath()).rootPanel);
        Toolkit kit = Toolkit.getDefaultToolkit();  //拿到默认工具包
        Dimension screen = kit.getScreenSize(); //获取屏幕大小


        frame.setPreferredSize(new Dimension(screen.width / 2+400, screen.height / 2+400));
        frame.setLocationByPlatform(true); //窗口出现在本机的默认位置
        frame.setResizable(false);//不可以改变大小
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); //

        
        frame.pack();
        frame.setVisible(true);

    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }

    private void init(String str) throws Exception {
        panel_right.setPreferredSize(new Dimension(840, 600));
        panel_right.setBackground(Color.WHITE);
        panel_right.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        //
        ImageIcon imageIcon = new ImageIcon("./image/key_return.png");
        imageIcon.setImage(imageIcon.getImage().getScaledInstance(68, 68,
                Image.SCALE_DEFAULT));
        enter.setIcon(imageIcon);
        //
        midpanel.setPreferredSize(new Dimension(midpanel.getSize().width,40));
        //初始化显示右边文件
        showFile(str);

        //初始化 jTree
        initJtree();
        //左边树结构 鼠标 点击事件
        mouseControlFilelist();
        //右键菜单
        mouseRightMenuFunction();
        mouseControlFile();
        //按钮初始化
        BtnListenner();
        //circleProgress
        circleProgress();
    }

    private void initJtree() {
        //初始化 Jtree
        File[] root = FileSystemView.getFileSystemView().getRoots();//根目录节点 root[0]

        FileTreeModel model = new FileTreeModel(new DefaultMutableTreeNode(new FileNode("root", null, null, true)), root[0]);
        fileTree.setModel(model);
        fileTree.setCellRenderer(new FileTreeRenderer());
        fileTree.expandRow(0);
        JScrollPane sp = new JScrollPane(fileTree);

        sp.setBorder(BorderFactory.createEtchedBorder(Color.white, new Color(148, 145, 140)));
        splitpanel.setDividerLocation(300); //设置面板左边大小
        left_down_panel.add(sp);
    }

    //显示文件
    private void showFile(String path) {
        panel_right.removeAll();
        showpath(path);
        File f = new File(path);
        if (f.isDirectory()) {
            FILEPATH = f.list();
            if (FILEPATH == null) {
                FILEPATH = new String[1];
                FILEPATH[0] = "";
            } else {
                List<JPanel> dirlist = new ArrayList<>();
                List<JPanel> filelist = new ArrayList<>();

                for (String aFILEPATH : FILEPATH) {
                    JPanel panel_item = new JPanel();
                    ImageIcon icon = new ImageIcon("./image/wenjianjia.png");
                    ImageIcon icon_file = new ImageIcon("./image/wenjian.png");
                    JLabel label;
                    File file = new File(path + "/" + aFILEPATH);
                    if (file.isDirectory()) {
                        label = new JLabel(icon);
                        JLabel label1 = new JLabel(aFILEPATH);
                        label1.setPreferredSize(new Dimension(50, 20));
                        panel_item.setLayout(new BorderLayout());
                        panel_item.setBackground(null);

                        panel_item.add(label, BorderLayout.NORTH);
                        panel_item.add(label1, BorderLayout.CENTER);

                        label1.setHorizontalAlignment(SwingConstants.CENTER);
                        dirlist.add(panel_item);
                    } else {
                        label = new JLabel(icon_file);
                        JLabel label1 = new JLabel(aFILEPATH);
                        label1.setPreferredSize(new Dimension(50, 20));
                        panel_item.setLayout(new BorderLayout());
                        panel_item.setBackground(null);

                        panel_item.add(label, BorderLayout.NORTH);
                        panel_item.add(label1, BorderLayout.CENTER);

                        label1.setHorizontalAlignment(SwingConstants.CENTER);
                        filelist.add(panel_item);
                    }
                    panel_item.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseEntered(MouseEvent e) {
                            super.mouseEntered(e);
                            panel_item.setBackground(Color.lightGray);
                        }

                        @Override
                        public void mouseExited(MouseEvent e) {
                            super.mouseExited(e);
                            panel_item.setBackground(null);

                        }
                    });

                    panel_item.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            //设置左键双击打开文件夹
                            if (e.getClickCount() == 1) {
                                String length = null;
                                String name = null;
                                String time = null;
                                name  = file.getName();
                                if (file.length() > 1000 * 1000 * 1000){
                                    length = file.length() / 1000000000 + "G ";
                                }
                                else if (file.length() > 1000 * 1000){
                                    length = file.length() / 1000000 + "M ";
                                }
                                else if(file.length() > 1000){
                                    length = file.length() / 1000 + "K ";
                                }
                                else{
                                    length = file.length() + "B ";
                                }
                                time = new Date(file.lastModified()).toLocaleString();
                                file_info.setText(name+"  "+length+"  "+time+"  ");
                            }

                            if (e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1) {
                                JPanel item = (JPanel) e.getComponent();
                                JLabel la = (JLabel) item.getComponent(1);
                                String str = la.getText();
                                showFile(localPath + File.separator + str);
                            }


                            //设置右键显示菜单
                            if (e.getButton() == MouseEvent.BUTTON3) {
                                panel_item.setBackground(Color.lightGray);
                                JPanel item = (JPanel) e.getComponent();
                                JLabel la = (JLabel) item.getComponent(1);
                                RButtonSelect = la.getText();
                                popup.show(e.getComponent(), e.getX(), e.getY());
                            }
                        }
                    });
                }
                for (JPanel label : dirlist) {
                    panel_right.add(label);
                    panel_right.updateUI();
                }
                for (JPanel label : filelist) {
                    panel_right.add(label);
                    panel_right.updateUI();
                }
            }

            localPath = path;
        }
        panel_right.repaint();


    }

    //左边树结构 鼠标 点击事件
    private void mouseControlFilelist() {

        fileTree.addMouseListener(new MouseAdapter() {
                                      public void mousePressed(MouseEvent e) {
                                          TreePath path = fileTree.getPathForLocation(e.getX(), e.getY());
                                          if (SwingUtilities.isLeftMouseButton(e)) {
                                              if (path != null) {
                                                  DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
                                                  FileNode filenode = (FileNode) node.getUserObject();
                                                  if (filenode.file.isDirectory()) {
                                                      path1 = filenode.file.getAbsolutePath();
                                                      //更新显示
                                                      showFile(path1);

                                                  }
                                              }
                                          }
                                          if (SwingUtilities.isRightMouseButton(e)) {
                                              if (path != null) {
                                                  fileTree.flush(e);

                                              }
                                          }
                                      }
                                  }
        );

    }

    //鼠标右键
    private void mouseRightMenuFunction() {
        ActionListener itemAction = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO 自动生成的方法存根
                String temp = e.getActionCommand();
                if (temp.equals("打开")) {
                    if (!RButtonSelect.equals("")) {
                        showFile(localPath + File.separator + RButtonSelect);
                        RButtonSelect = "";
                        showFile(localPath);
                        panel_right.updateUI();
                    } else {
                        JOptionPane.showMessageDialog(null, "没有文件或文件夹被选中", "提示", JOptionPane.WARNING_MESSAGE);
                    }
                }
                if (temp.equals("删除")) {
                    try {
                        if (!RButtonSelect.equals("")) {
                            operate.deleteFile(localPath + File.separator + RButtonSelect);
                            RButtonSelect = "";
                            showFile(localPath);
                            panel_right.updateUI();
                        } else {
                            JOptionPane.showMessageDialog(null, "没有文件或文件夹被选中", "提示", JOptionPane.WARNING_MESSAGE);
                        }
                    } catch (FileNotFoundException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                }
                if (temp.equals("复制")) {
                    Constant.COPYPARTH = localPath + File.separator + RButtonSelect;
                }
                if (temp.equals("粘贴")) {
                    String fileName = operate.getFilenameFromPath(Constant.COPYPARTH);

                    operate.copy(Constant.COPYPARTH, localPath + File.separator + operate.getFilenameFromPath(Constant.COPYPARTH));
                    showFile(localPath);
                    panel_right.updateUI();

                }
                if (temp.equals("压缩")) {
                    String  inputValue=(String) JOptionPane.showInputDialog("请输入压缩后的文件名");

                        ZipUtil.zip(localPath + File.separator + RButtonSelect);
                    showFile(localPath);
                    panel_right.updateUI();
                }
                if (temp.equals("解压")) {
                    ZipUtil.unzip(localPath + File.separator + RButtonSelect);
                    showFile(localPath);
                    panel_right.updateUI();
                }
                if (temp.equals("加密")) {
                    try {

                        FileEncAndDec.EncFile(localPath + File.separator + RButtonSelect, localPath + File.separator + "加密" + RButtonSelect);
                        showFile(localPath);
                        panel_right.updateUI();
                    } catch (Exception e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                }
                if (temp.equals("解密")) {
                    try {
                        FileEncAndDec.DecFile(localPath + File.separator + RButtonSelect, localPath + File.separator + "解密" + RButtonSelect);
                        showFile(localPath);
                        panel_right.updateUI();
                    } catch (Exception e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                }
                if (temp.equals("刷新")) {
                    showFile(localPath);
                    panel_right.updateUI();
                }

            }
        };


        popup.addItemListener(0, itemAction);
        popup.addItemListener(1, itemAction);
        popup.addItemListener(2, itemAction);
        popup.addItemListener(3, itemAction);
        popup.addItemListener(4, itemAction);
        popup.addItemListener(5, itemAction);
        popup.addItemListener(6, itemAction);
        popup.addItemListener(7, itemAction);
        popup.addItemListener(8, itemAction);
    }

    private void mouseControlFile() {

        panel_right.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //设置右键显示菜单
                if (e.getButton() == MouseEvent.BUTTON3) {
                    popup.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });
    }

    //显示字符串路径
    private void showpath(String path)  //显示路径字符串
    {
        input_filed.setText(path);
    }

    //按钮初始化
    private void BtnListenner() {
        back.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                showFile(operate.pathBackTo(localPath));
            }

        });
        forward.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                showFile(operate.pathForwardTo(localPath));
            }

        });
        new_dir.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                String inputValue = (String) JOptionPane.showInputDialog("请输入文件夹名");
                if (inputValue.isEmpty())
                    JOptionPane.showMessageDialog(null, "标题【出错啦】", "输入不能为空", JOptionPane.ERROR_MESSAGE);
                else {
                    operate.addDir(localPath + File.separator + inputValue);
                    showFile(localPath);
                    panel_right.updateUI();
                }
            }

        });
        new_file.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                String inputValue = (String) JOptionPane.showInputDialog("请输入文件名");
                if (inputValue.isEmpty())
                    JOptionPane.showMessageDialog(null, "标题【出错啦】", "输入不能为空", JOptionPane.ERROR_MESSAGE);
                else {
                    operate.addFile(localPath + File.separator + inputValue + ".txt");
                    showFile(localPath);
                    panel_right.updateUI();
                }

            }

        });
    }

    //圆形进度条 //cpu 使用率
    private void circleProgress() throws Exception {

        CircleProgressBar progressBar = new CircleProgressBar(); //实例化
        progressBar.setMinimumProgress(0); //设置最小进度值
        progressBar.setMaximumProgress(100); //设置最大进度值
        int rate = Cpu.cpuRate();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    int rate = Cpu.cpuRate();
                    progressBar.setProgress(rate);
                } catch (SigarException e) {
                    e.printStackTrace();
                }
            }
        },1000,1000);


        progressBar.setProgress(rate); //设置当前进度值
        progressBar.setBackgroundColor(new Color(209, 206, 200)); //设置背景颜色
        progressBar.setForegroundColor(new Color(0, 255, 0)); //设置前景颜色
        progressBar.setDigitalColor(Color.BLACK); //设置数字颜色
        progressBar.setSize(new Dimension(300, 400));
        left_top_panel.add(progressBar,BorderLayout.CENTER);
        JLabel top = new JLabel("CPU利用率：");
        top.setFont(new Font(null, Font.PLAIN, 20));
        left_top_panel.add(top,BorderLayout.NORTH);

        JPanel nor = new JPanel(new GridLayout(3,1));
        JLabel memory = new JLabel("Memory Total:" + Cpu.memoryTotal());
        memory.setFont(new Font(null, Font.PLAIN, 20));
        JLabel memoryused = new JLabel("Memory Used :" + Cpu.memoryUsed());
        memoryused.setFont(new Font(null, Font.PLAIN, 20));
        JLabel memoryfree = new JLabel("Memory Free :" + Cpu.memoryFree());
        memoryfree.setFont(new Font(null, Font.PLAIN, 20));
        nor.add(memory);
        nor.add(memoryused);
        nor.add(memoryfree);
        left_top_panel.add(nor,BorderLayout.SOUTH);
    }

    private static void InitGlobalFont(Font font) {
        FontUIResource fontRes = new FontUIResource(font);
        for (Enumeration<Object> keys = UIManager.getDefaults().keys(); keys.hasMoreElements();) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof FontUIResource) {
                UIManager.put(key, fontRes);
            }
        }
    }
}


