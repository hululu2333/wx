package com.hu;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Login
 */
@WebServlet("/Login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Login() {
        super();
        
    }

	

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		
		//1.����û���code
		String js_code=request.getParameter("js_code");

		//2.�õ�����û���openid�Ľӿ�
		String adress="https://api.weixin.qq.com/sns/jscode2session?appid=wxa05f3c6e1a1194fe&secret=42c6314125c2a0aa7d7807ababe2923c&"
				+ "js_code="+js_code+"&grant_type=authorization_code";//���openid����Կ�Ľӿ�
		
		//3.����java������openid���ܳ�
		URL url=new URL(adress);//URL����
		InputStream is=url.openStream();//��ʼ����openid
		BufferedReader br=new BufferedReader(new InputStreamReader(is));
		String info=br.readLine();//br.readLine�������openid���ܳ�
		
			//�Է��ؽ�����зָ��ȡ
		String [] results=info.split("\"");//�и�������results��
		String session_key=results[3];//������ؽ����ȷ�Ļ���results[3]����session_key
			//��������session_key���з��������ǲ�����ȷ���ܳ�
		if(session_key.length()<10) {
			response.getWriter().append("code����");
			
			return;//����
		}
			//�ܳ����󣬽�������ȡopenid
		String openid=results[7];
		
		
		
		//4.�����Ѿ����openid�������������ݿ��������openid��û�ҵ��Ļ����½�һ��.����������û��ı��
		String sql="select * from user where openid="+"'"+openid+"'";//sql���
		ResultSet rs=SqlUtil.doSelect(sql);
		
		int userNum=0;
		try {
			if(!rs.next()) {
					userNum=SqlUtil.addUser(openid);//û������˵����
				}else {
					userNum=rs.getInt(1);//������˵�����ͷ�������˵��û����
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		
		
		
		//5.��������û����ռ�ƪ����������������
		String sql2="select * from user where uno="+userNum;
		ResultSet rs2=SqlUtil.doSelect(sql2);
		int Jamount=0;//�ռ�ƪ��
		int Textamount=0;//������
		try {
			while(rs2.next()) {
				Jamount=rs2.getInt(3);
				Textamount=rs2.getInt(4);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		//6.�����û���ţ��ռ�ƪ�������������û����ǰ��Ҫ�������������ں�̵Ĳ���
		response.getWriter().append(String.valueOf(userNum)).append("-").append(String.valueOf(Jamount))
		.append("-").append(String.valueOf(Textamount));
		//��½����
	}
	
	
	
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
