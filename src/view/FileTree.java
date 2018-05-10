package view;

import main.FileManager;

import javax.swing.*;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.ExpandVetoException;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Objects;

public class FileTree extends JTree {
	JMenuItem create=new JMenuItem("新建文件夹");
	JMenuItem deletefile=new JMenuItem("删除文件夹");
	JMenuItem copy=new JMenuItem("复制");
	JMenuItem paste=new JMenuItem("粘贴");
	JMenuItem zip=new JMenuItem("压缩");
	static boolean  state=false;
	String path1="C:\\Users\\lenovo\\Desktop";
	String copypath=null;
	String pastepath=null;

    public TreePath mouseInPath;
    protected FileSystemView fileSystemView = FileSystemView.getFileSystemView();
    public FileTree(){
       setRootVisible(false);
       addTreeWillExpandListener(new TreeWillExpandListener() {
            @Override
            public void treeWillExpand(TreeExpansionEvent event) throws ExpandVetoException {
                DefaultMutableTreeNode lastTreeNode =(DefaultMutableTreeNode) event.getPath().getLastPathComponent();
                FileNode fileNode = (FileNode) lastTreeNode.getUserObject();
                if (!fileNode.isInit) {
                    File[] files;
                    if (fileNode.isDummyRoot) {
                        files = fileSystemView.getRoots();
                    } else {
                        files = fileSystemView.getFiles(
                                ((FileNode) lastTreeNode.getUserObject()).file,
                                false);
                    }
                    for (int i = 0; i < files.length; i++) {
                    	if(files[i].isDirectory()){
                        FileNode childFileNode = new FileNode(
                                fileSystemView.getSystemDisplayName(files[i]),
                                fileSystemView.getSystemIcon(files[i]), files[i],
                                false);
                        DefaultMutableTreeNode childTreeNode = new DefaultMutableTreeNode(childFileNode);
                        lastTreeNode.add(childTreeNode);
                    	}
                    }

                    DefaultTreeModel treeModel1 = (DefaultTreeModel) getModel();
                    treeModel1.nodeStructureChanged(lastTreeNode);
                }

                fileNode.isInit = true;
            }
            @Override
            public void treeWillCollapse(TreeExpansionEvent event) throws ExpandVetoException {

            }
        });
       addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                TreePath path=getPathForLocation(e.getX(), e.getY());
                if(path!=null){
                    if(mouseInPath!=null){
                        Rectangle oldRect=getPathBounds(mouseInPath);
                        mouseInPath=path;
                        repaint(Objects.requireNonNull(getPathBounds(path)).union(oldRect));
                    }else{
                        mouseInPath=path;
                        Rectangle bounds=getPathBounds(mouseInPath);
                        repaint(bounds);
                    }
                }else if(mouseInPath!=null){
                    Rectangle oldRect=getPathBounds(mouseInPath);
                    mouseInPath=null;
                    repaint(oldRect);
                }
            }
        });
//       addMouseListener(new MouseAdapter(){
//    	   public void mousePressed(MouseEvent e){
//    		   TreePath path=getPathForLocation(e.getX(), e.getY());
//    		   if(SwingUtilities.isLeftMouseButton(e)){
//    			   if(path != null){
//	    			   DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
//	    			   FileNode filenode  = (FileNode) node.getUserObject();
//	    			   if(filenode.file.isDirectory()){
//	    				   path1=filenode.file.getAbsolutePath();
//
//                           FileManager fileManager = new FileManager(path1);
//                           fileManager.showFile(path1);
////	    				   MainFrame.curDir = filenode.file.getAbsolutePath();
////	    				   MainFrame main = (MainFrame)getRootPane().getParent();
////	    				   main.tableDisplay();
//	    			   }
//    			   }
//    		   }
//    		   if(SwingUtilities.isRightMouseButton(e)){
//    			   if(path!=null){
//    				   flush(e);
//
//    			   }
//    		   }
//    	   }
//       }
//    		   );
       create.addMouseListener(new MouseAdapter(){
    	   public void mousePressed(MouseEvent e){
    		   if(SwingUtilities.isLeftMouseButton(e)){

    				 	String  inputValue=(String) JOptionPane.showInputDialog("请输入文件名");
    				   if(inputValue.isEmpty())
    					   	JOptionPane.showMessageDialog(null, "标题【出错啦】", "输入不能为空", JOptionPane.ERROR_MESSAGE);
    				   else{
		    			   String path=path1+"/"+inputValue;
		    			   System.out.println(path);
//		    			   ListFile op=new ListFile();
//		    			   op.newdic(path);
//		    			   MainFrame main = (MainFrame)getRootPane().getParent();
//	    				   main.treeDispaly();
    				   }
    		   }
    	   }
       });
//       deletefile.addMouseListener(new MouseAdapter(){
//    	   public void mousePressed(MouseEvent e){
//    		   if(SwingUtilities.isLeftMouseButton(e)){
//
//    				 	ListFile op=new ListFile();
//		    			op.deletfile(path1);
//		    			if(path1==copypath){
//		    				copypath=null;
//		    				state=false;
//		    			}
//
//		    			MainFrame main = (MainFrame)getRootPane().getParent();
//	    				main.treeDispaly();
//    		   }
//    	   }
//       });
//       copy.addMouseListener(new MouseAdapter(){
//    	   public void mousePressed(MouseEvent e){
//    		   if(SwingUtilities.isLeftMouseButton(e)){
//    			   state=true;
//    			   copypath=path1;
//    		   }
//    	   }
//       });
//       paste.addMouseListener(new MouseAdapter(){
//    	   public void mousePressed(MouseEvent e){
//    		   if(SwingUtilities.isLeftMouseButton(e)){
//    			   if(copypath==null){
//    				   MainFrame main = (MainFrame)getRootPane().getParent();
//    				   copypath=main.getcopypath();
//    			   }
//    			   System.out.println(copypath);
//    			   pastepath=path1;
//    			   System.out.println(pastepath);
//    			   ListFile op=new ListFile();
//    			   try {
//					op.copy(copypath,pastepath);
//					MainFrame main = (MainFrame)getRootPane().getParent();
//   					main.treeDispaly();
//   					main.tableDisplay();
//				} catch (IOException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				}
//
//    		   }
//    	   }
//       });
//       zip.addMouseListener(new MouseAdapter(){
//			public void mousePressed(MouseEvent e){
//	    		   if(SwingUtilities.isLeftMouseButton(e)){
//	    			   String  inputValue=(String) JOptionPane.showInputDialog("请输入压缩后的文件名（.zip）");
//   				   if(inputValue.isEmpty())
//   					   	JOptionPane.showMessageDialog(null, "标题【出错啦】", "输入不能为空", JOptionPane.ERROR_MESSAGE);
//   				   else{
//   					   		File file=new File(path1);
//		    			   String path=file.getParent()+"/"+inputValue;
//		    			   ListFile op=new ListFile();
//		    			  op. ZipMultiFile(path1, path);
//		    			  MainFrame main = (MainFrame)getRootPane().getParent();
//		   					main.treeDispaly();
//   				   }
//	    		   }
//	    	   }
//		});
    }
	public void flush(MouseEvent e) {
		// TODO Auto-generated method stub
		JPopupMenu menu=new JPopupMenu();
		   paste.setEnabled(state);
		   menu.add(create);
		   menu.addSeparator();
		   menu.add(deletefile);
		   menu.addSeparator();
		   menu.add(copy);
		   menu.addSeparator();
		   menu.add(paste);
		   menu.addSeparator();
		   menu.add(zip);
		   menu.show(FileTree.this, e.getX(), e.getY());
	}
}
