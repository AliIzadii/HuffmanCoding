import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.io.Serializable;

public class Huffmantree {
    static boolean[] Select = new boolean[2];
    static int index = 0;
    static JFrame Menu = new JFrame("HuffMan Tree Coder/Decode");
    static JDialog SecondMenu = new JDialog();
    static JDialog getFile_Dialog = new JDialog();
    static HashMap<String, String> hashMap = new HashMap<>();
    static int[] frequency = new int[128];
    static String coded, decoded;
    static Node rootN;
    static File[] files = new File[5];

    public static String Decode(Node root, String s) {
        String result = "";
        Node tmp = root;
        if (root.isLeaf()) {
            for (int i = 0; i < s.length(); i++)
                result += String.valueOf(root.data);
            return result;
        }
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '0')
                tmp = tmp.Left;
            else
                tmp = tmp.Right;
            if (tmp.isLeaf()) {
                result += tmp.data;
                tmp = root;
            }
        }
        return result;
    }

    public static String[] Decode_returnable(Node root, String s) {
        String result = "";
        int flag = 0;
        Node tmp = root;
        String[] t = new String[2];
        t[0] = "";
        if (root.isLeaf()) {
            for (int i = 0; i < s.length(); i++)
                result += String.valueOf(root.data);
            t[1] = result;
            return t;
        }
        int k = 0;
        for (int i = 0; i < s.length(); i++) {

            if (s.charAt(i) == '0')
                tmp = tmp.Left;
            else
                tmp = tmp.Right;
            if (tmp.isLeaf()) {
                result += tmp.data;
                tmp = root;
                k = i;
                flag = 1;
            }
        }
        t[1] = result;
        if (flag == 1)
            t[0] = s.substring(k + 1);
        else t[0] = s;
        return t;
    }

    public static String Encoding(String a) {
        String result = "";
        for (int i = 0; i < a.length(); i++)
            result += (hashMap.get(String.valueOf(a.charAt(i))));
        return result;
    }
       
    public static void HashMapBuilder(Node root, String s) {
        if (root.isLeaf()) {
            hashMap.put(String.valueOf(root.data), s);
        } else {
            HashMapBuilder(root.Left, s + "0");
            HashMapBuilder(root.Right, s + "1");
        }
    }

    public static void AddSorted(List<Node> nodes, Node x) {
        for (int i = 0; i < nodes.size(); i++)
            if (nodes.get(i).getFrequnecy() >= x.getFrequnecy()) {
                nodes.add(i, x);
                return;
            }
        nodes.add(x);
    }

    public static void initializeFrequencies() {
        for (int i = 0; i < frequency.length; i++)
            frequency[i] = 0;
    }

    public static void UpdateFrequency(String a){
        for (int i = 0; i < a.length(); i++)
            frequency[(int) a.charAt(i)]++;
    }

    public static Node HuffmanTreeGenerator(){
        List<Node> nodes = new LinkedList<>();
        for (int i = 0; i < frequency.length; i++) {
            if (frequency[i] > 0)
                nodes.add(new Node((char) i, frequency[i]));
        }
        for (int i = 0; i < nodes.size() - 1; i++)
            for (int j = i + 1; j < nodes.size(); j++)
                if (nodes.get(j).getFrequnecy() < nodes.get(i).getFrequnecy()) {
                    Node f = nodes.get(i);
                    nodes.set(i, nodes.get(j));
                    nodes.set(j, f);
                }
        if (nodes.size() == 1)
            hashMap.put(String.valueOf(nodes.get(0).data), "0");
        while (nodes.size() > 1) {
            Node x = nodes.remove(0);
            Node y = nodes.remove(0);
            AddSorted(nodes, new Node(x, y));
        }
        return nodes.get(0);
    }

    public static String writerCoded(String a, OutputStream S) throws IOException {
        int k = a.length() / 8;
        int i = 0, j = 0;
        if (k > 0){
            byte[] tmp = new byte[a.length() / 8];
            for (; i < a.length() - a.length() % 8; i += 8){
                tmp[j] = ((byte) (Integer.parseInt(a.substring(i, i + 8), 2)));
                j++;
            }
            S.write(tmp);
        }
        return a.substring(i, i + a.length() % 8);
    }

    public static void main(String[] args) throws IOException{
        initializeFrequencies();
        Select[0] = false;
        Select[1] = false;
        SecondMenu.setSize(300,150);
        getFile_Dialog.setSize(600,400);
        Menu.setSize(350,250);
        Menu.setIconImage( new ImageIcon("photos/b-2.png").getImage());
        JPanel panel = new JPanel();
        JPanel[] panels = new JPanel[3];
        JPanel[] panels1 = new JPanel[3];
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setBackground(Color.cyan);
        JRadioButton[] jRadioButtons = new JRadioButton[4];
        //------------------------------------>
        jRadioButtons[0] = new JRadioButton("Coding");
        panels[0] = new JPanel();
        panels[0].add(jRadioButtons[0]);
        panels[0].setBackground(Color.cyan);
        //------------------------------------>
        jRadioButtons[1] = new JRadioButton("Decoding :");
        panels[1] = new JPanel();
        panels[1].add(jRadioButtons[1]);
        panels[1].add(new JSeparator());
        panels[1].setBackground(new Color(0,0.3f,1f));
        //-------------<<
        jRadioButtons[2] = new JRadioButton("@By node");
        jRadioButtons[3] = new JRadioButton("@By table ");
        panels[2] = new JPanel();
        panels[2].add(jRadioButtons[2]);
        panels[2].add(jRadioButtons[3]);
        panels[1].add(panels[2]);
        panels[2].setBackground(new Color(0,0.6f,1f));
        //----------------------------------------------
        panel.add(panels[0]);
        panel.add(panels[1]);
        panel.setBackground(new Color(0.6f,0.4f,1f));
        JButton ok = new JButton();
        ImageIcon im = new ImageIcon("photos/b5.png");
        Image image = im.getImage();
        image = image.getScaledInstance(90,90,java.awt.Image.SCALE_SMOOTH);
        im.setImage(image);
        ok.setBackground(Color.white);
        ok.setIcon(im);
        panel.add(ok);
        getFile_Dialog.add(fileChooser);
        fileChooser.addActionListener(e -> {
            files[index] = fileChooser.getSelectedFile();
            getFile_Dialog.setVisible(false);
        });
        JButton[] jButtons = new JButton[6];
        for (int m = 0; m < 6; m++) {
            jButtons[m] = new JButton();
            jButtons[m].setBackground(new Color(m*0.1f,0.3f,0.9f));
            jButtons[m].setIcon(new ImageIcon(new ImageIcon("photos/b"+String.valueOf(m)+".png").getImage().getScaledInstance(30,30,Image.SCALE_SMOOTH)));
        }
        jButtons[0].addActionListener(e -> {
            getFile_Dialog.setVisible(true);
            index = 0;
        });
        jButtons[0].setText("Data");
        jButtons[1].addActionListener(e -> {
            getFile_Dialog.setVisible(true);
            index = 1;
        });
        jButtons[1].setText("File1");
        jButtons[2].addActionListener(e -> {
            getFile_Dialog.setVisible(true);
            index = 2;
        });
        jButtons[2].setText("File2");
        jButtons[3].addActionListener(e -> {
            getFile_Dialog.setVisible(true);
            index = 3;
        });
        jButtons[3].setText("File node");
        jButtons[4].addActionListener(e -> {
            getFile_Dialog.setVisible(true);
            index = 4;
        });
        jButtons[4].setText("File table");
        jButtons[5].addActionListener(e -> {
            if (Select[0]) {
                    Runnable r = new Runnable() {
                        public void run() {
                            try {
                                ConvertToCoded(files[0]);
                            } catch (IOException ioException) {
                                ioException.printStackTrace();
                            }
                        }
                    };
                   Thread t = new Thread(r);
                   t.start();
            } else if (Select[1]) {
                File[] files1 = new File[3];
                files1[0] = files[1];
                files1[1] = files[2];
                files1[2] = files[3];
                Runnable r = () -> {
                    try {
                        ConvertBack_ByNode(files1);
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    } catch (ClassNotFoundException classNotFoundException) {
                        classNotFoundException.printStackTrace();
                    }
                };
                Thread t = new Thread(r);
                t.start();
            } else {
                File[] files1 = new File[3];
                files1[0] = files[1];
                files1[1] = files[2];
                files1[2] = files[4];
                Runnable r = () -> {
                    try {
                        ConvertBack_ByTable(files1);
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    } catch (ClassNotFoundException classNotFoundException) {
                        classNotFoundException.printStackTrace();
                    }
                };
                Thread t = new Thread(r);
                t.start();
            }
            SecondMenu.setVisible(false);
            getFile_Dialog.setVisible(false);
            getFile_Dialog.dispatchEvent(new WindowEvent(getFile_Dialog, WindowEvent.WINDOW_CLOSING));
            SecondMenu.dispatchEvent(new WindowEvent(SecondMenu, WindowEvent.WINDOW_CLOSING));
        });
        ok.addActionListener(e -> {
            if (jRadioButtons[0].isSelected()) {
                {
                    Select[0] = true;
                    panels1[0] = new JPanel();
                    panels1[0].add(jButtons[0]);
                    panels1[0].add(jButtons[5]);
                    SecondMenu.add(panels1[0]);
                    SecondMenu.setSize(400,100);
                }
            } else {
                if (jRadioButtons[2].isSelected()) {
                    Select[1] = true;
                    panels1[1] = new JPanel();
                    panels1[1].add(jButtons[1]);
                    panels1[1].add(jButtons[2]);
                    panels1[1].add(jButtons[3]);
                    panels1[1].add(jButtons[5]);
                    SecondMenu.add(panels1[1]);
                    SecondMenu.setSize(400,150);
                } else {
                    panels1[2] = new JPanel();
                    panels1[2].add(jButtons[1]);
                    panels1[2].add(jButtons[2]);
                    panels1[2].add(jButtons[4]);
                    panels1[2].add(jButtons[5]);
                    SecondMenu.add(panels1[2]);
                    SecondMenu.setSize(400,150);
                }
            }
            Menu.setVisible(false);
            Menu.dispatchEvent(new WindowEvent(Menu, WindowEvent.WINDOW_CLOSING));
            SecondMenu.setVisible(true);
        });
        Menu.add(panel);
        getFile_Dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        Menu.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        SecondMenu.setDefaultCloseOperation(2);
        Menu.setVisible(true);
    }

    public static void ConvertToCoded(File file) throws IOException {
        String a = "";
        OutputStream os1 , os2, os3, os4;
        os1 = new FileOutputStream(new File("resultCoded1.txt"));
        os2 = new FileOutputStream(new File("resultRootNode.txt"));
        os3 = new FileOutputStream(new File("resultCoded2.txt"));
        os4 = new FileOutputStream(new File("resultTable.txt"));
        Scanner s = new Scanner(file);
        while (s.hasNextLine()){
            a = s.nextLine();
            UpdateFrequency(a);
        }
        s = new Scanner(file);
        rootN = HuffmanTreeGenerator();
        HashMapBuilder(rootN, "");
        JFrame x = new JFrame();
        x.add(new JTree(TreeDisplayMaker(rootN)));
        x.setVisible(true);
        x.setSize(300, 500);
        x.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        String temp = "";
        while (s.hasNextLine()){
            a = s.nextLine(); 
            temp = writerCoded(temp + Encoding(a), os1);
        }
        byte[] bytes = new byte[2];
        bytes[0] = (byte) temp.length();
        if (temp.length() != 0) {
            bytes[1] = (byte) Integer.parseInt(temp, 2);
        } else bytes[1] = 0;
        os3.write(bytes);
        ObjectOutputStream root_obj = new ObjectOutputStream(os2);
        root_obj.writeObject(rootN);
        ObjectOutputStream table_obj = new ObjectOutputStream(os4);
        table_obj.writeObject(frequency);
        os1.close();
        os2.close();
        os3.close();
        os4.close();
    }

    public static void ConvertBack_ByTable(File[] files) throws IOException, ClassNotFoundException {
        FileInputStream fileinputstream_table = new FileInputStream(files[2]);
        ObjectInputStream table_obj = new ObjectInputStream(fileinputstream_table);
        frequency = (int[]) table_obj.readObject();
        rootN = HuffmanTreeGenerator();
        ConvertBack(files[0], files[1]);
    }

    public static void ConvertBack_ByNode(File[] files) throws IOException, ClassNotFoundException {
        FileInputStream fileinputstream_node = new FileInputStream(files[2]);
        ObjectInputStream obj = new ObjectInputStream(fileinputstream_node);
        rootN = (Node) obj.readObject();
        coded = "";
        ConvertBack(files[0], files[1]);
    }

    public static void ConvertBack(File file1, File file2) throws IOException {
        int i;
        byte[] bytes;
        String tmp = "", t;
        String[] tmp_arr;
        int data;
        FileInputStream f1 = new FileInputStream(file1);
        FileInputStream f2 = new FileInputStream(file2);
        FileOutputStream RemadeFile = new FileOutputStream(new File("RemadeFile.txt"));
        while ((data = f1.read()) != -1) { 
            coded = tmp + String.format("%8s", Integer.toBinaryString(data)).replace(' ', '0');
            tmp_arr = Decode_returnable(rootN, coded);
            tmp = tmp_arr[0];
            for (i = 0; i < tmp_arr[1].length(); i++)
                RemadeFile.write((char) tmp_arr[1].charAt(i));
        }
        int k = 0, data2;
        if((data = f2.read()) != -1)
            k++;
        if((data2 = f2.read()) != -1)
            k++;
        if (k == 2){ 
            t = String.format("%8s", Integer.toBinaryString(data2)).replace(' ', '0');
            coded = tmp + t.substring((t.length() - (data))); 
            decoded = Decode(rootN, coded);
            for (int j = 0; j < decoded.length(); j++)
                RemadeFile.write(decoded.charAt(j));
        }
        f2.close();
        f1.close();
        RemadeFile.close();
    }

    public static DefaultMutableTreeNode TreeDisplayMaker(Node r2) {
        DefaultMutableTreeNode r;
        if (r2.isLeaf()) {
            r = new DefaultMutableTreeNode(r2.data + " : " + Integer.toString(r2.getFrequnecy()));
            return r;
        } else {
            r = new DefaultMutableTreeNode("node : " + Integer.toString(r2.getFrequnecy()));
            DefaultMutableTreeNode x = TreeDisplayMaker(r2.Right);
            DefaultMutableTreeNode y = TreeDisplayMaker(r2.Left);
            r.add(x);
            r.add(y);
        }
        return r;
    }

static class Node implements Serializable {
    char data;
    Boolean ISLeaf = false;
    Node Right = null;
    Node Left = null;
    int frequency;

    public Node(Node r, Node l) {
        this.Right = r;
        this.Left = l;
    }

    public Node(char data, int frequency) {
        this.data = data;
        this.frequency = frequency;
        ISLeaf = true;
    }

    public boolean isLeaf() {
        return ISLeaf;
    }

    public int getFrequnecy() {
        if (ISLeaf)
            return frequency;
        else return (Right == null ? 0 : Right.getFrequnecy()) + (Left == null ? 0 : Left.getFrequnecy());
    }
}
}
