package multi.server;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;


public class ServerMain extends JFrame implements ActionListener, Runnable{
	JPanel p_north; 
	JTextField t_port;
	JButton bt_start;
	JTextArea area;
	JScrollPane scroll;
	int port=7777; 
	Thread thread; //���� ������ ������
	ServerSocket server;
	
	Socket socket;

	//��Ƽ ĳ������ ���ؼ��� ���� ������ ����� ������ ���������� üũ�� ����Ұ� �ʿ��ϸ�,
	//�����ؾ� �ϹǷ� �÷��� �迭�� ��������  �ʴ´�
	
	Vector<ServerThread> list=new Vector<ServerThread>();
	
	
	public ServerMain() {
		p_north = new JPanel();
		t_port = new JTextField(Integer.toString(port) ,10);
		bt_start = new JButton("����");
		area = new JTextArea();
		scroll = new JScrollPane(area);
		p_north.add(t_port);
		p_north.add(bt_start);
		add(p_north, BorderLayout.NORTH);
		add(scroll);
		
		bt_start.addActionListener(this);
		
		setBounds(600,100,300,400);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	//���� ���� �޼���
	public void startServer(){
		try {
			port=Integer.parseInt(t_port.getText());
			server=new ServerSocket(port);	
			area.append("���� ����\n");
			
			while(true){
			socket=server.accept();		
			String ip=socket.getInetAddress().getHostAddress(); //IP ����
			area.append(ip+"������ �߰�\n");
			
			//�����ڰ� �߰ߵ��ڸ��� ������ ���� ->�����带 �ϳ��� �Ҵ��ؼ� ��ȭ�� ������ ���ش�
			ServerThread st=new ServerThread(socket, this);
			st.start(); //������ ����! 
			
			//�����ڰ� �߰ߵǸ�, �� �����ڿ� ��ȭ�� ���� �����带 Vector�� ��´�
			list.add(st);
			area.append("���� �����ڴ� "+list.size()+"��\n");
			
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
	}
	
	public void actionPerformed(ActionEvent e) {
		thread=new Thread(this);
		thread.start();
	}
	
	public void run() {
		
		startServer();
		
	}
	
	public static void main(String[] args) {
		new ServerMain();
	}

}