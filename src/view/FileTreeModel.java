package view;

import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import java.io.File;

public class FileTreeModel extends DefaultTreeModel {
    public FileTreeModel(TreeNode root, File file) {
        super(root);
         FileSystemView fileSystemView = FileSystemView.getFileSystemView();
            File[] files=fileSystemView.getRoots();
            File mmm = file;
            files[files.length-1] = mmm;
        for (int i = 0; i < files.length; i++) {
            FileNode childFileNode = new FileNode(fileSystemView.getSystemDisplayName(files[i]), fileSystemView.getSystemIcon(files[i]), files[i], false);
            DefaultMutableTreeNode childTreeNode = new DefaultMutableTreeNode(childFileNode);
            ((DefaultMutableTreeNode)root).add(childTreeNode);
        }
    }

    @Override
    public boolean isLeaf(Object node) {
        DefaultMutableTreeNode treeNode=(DefaultMutableTreeNode)node;
        FileNode fileNode=(FileNode)treeNode.getUserObject();
        if(fileNode.isDummyRoot)return false;
        return fileNode.file.isFile();
    }
}
