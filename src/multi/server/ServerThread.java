/*
 * 접속한 클라이언트와 1:1로 대화를 나눌 쓰레드
 * */
package multi.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Vector;

import javax.swing.JTextArea;


public class ServerThread extends Thread{
	Socket socket;
	JTextArea area;
	BufferedReader buffr;
	BufferedWriter buffw;
	ServerMain main; //일일이 갖고 오기 섭섭하니까 아예 서버메인을 갖고옴.
	boolean flag=true;
	
	
	//생성된 서버로부터 소켓을 받아온다
	public ServerThread(Socket socket, ServerMain main) {
		this.socket=socket;
		this.main=main;
		
		try {
			buffr=new BufferedReader(new InputStreamReader(socket.getInputStream())); 
			buffw=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//클라이언트의 메세지 받기
	public void listen(){
		String msg=null;
		
		try {
			//클라이언트의 메세지 청취
			msg=buffr.readLine();
			main.area.append(msg+"\n");
			send(msg+"\n"); //다시 보내기
			
		} catch (IOException e) {
			flag=false; //유저가 나가면 현재 쓰레드를 죽임!!
			
			//벡터에서 이 쓰레드를 제거
			main.list.remove(this);
			
			main.area.append("1명 퇴장 후 현재 접속자"+main.list.size());
			System.out.println("읽기 불가");
			//e.printStackTrace();
		} 
	}
	
	//클라이언트에 메세지 보내기
	public void send(String msg){
		try {
			//현재 접속한 자 전부
			for(int i=0; i<main.list.size(); i++){
				ServerThread st=main.list.elementAt(i);
				st.buffw.write(msg+"\n"); 
				st.buffw.flush();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void run() {
		while(flag){ 
			listen();
		}
	}
	
	
}
