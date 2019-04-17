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
		
		//1.获得用户的code
		String js_code=request.getParameter("js_code");

		//2.得到获得用户的openid的接口
		String adress="https://api.weixin.qq.com/sns/jscode2session?appid=wxa05f3c6e1a1194fe&secret=42c6314125c2a0aa7d7807ababe2923c&"
				+ "js_code="+js_code+"&grant_type=authorization_code";//获得openid和密钥的接口
		
		//3.利用java爬虫获得openid和密匙
		URL url=new URL(adress);//URL对象
		InputStream is=url.openStream();//开始下载openid
		BufferedReader br=new BufferedReader(new InputStreamReader(is));
		String info=br.readLine();//br.readLine里包括了openid和密匙
		
			//对返回结果进行分割，提取
		String [] results=info.split("\"");//切割结果放在results中
		String session_key=results[3];//如果返回结果正确的话，results[3]就是session_key
			//接下来对session_key进行分析，看是不是正确的密匙
		if(session_key.length()<10) {
			response.getWriter().append("code错误");
			
			return;//结束
		}
			//密匙无误，接下来获取openid
		String openid=results[7];
		
		
		
		//4.我们已经获得openid啦，现在在数据库里找这个openid，没找到的话就新建一个.并返回这个用户的编号
		String sql="select * from user where openid="+"'"+openid+"'";//sql语句
		ResultSet rs=SqlUtil.doSelect(sql);
		
		int userNum=0;
		try {
			if(!rs.next()) {
					userNum=SqlUtil.addUser(openid);//没有这个人的情况
				}else {
					userNum=rs.getInt(1);//有这个人的情况就返回这个人的用户编号
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		
		
		
		//5.查找这个用户的日记篇数和总字数。。。
		String sql2="select * from user where uno="+userNum;
		ResultSet rs2=SqlUtil.doSelect(sql2);
		int Jamount=0;//日记篇数
		int Textamount=0;//总字数
		try {
			while(rs2.next()) {
				Jamount=rs2.getInt(3);
				Textamount=rs2.getInt(4);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		//6.返回用户编号，日记篇数和总字数，用户编号前端要保存下来，用于后继的操作
		response.getWriter().append(String.valueOf(userNum)).append("-").append(String.valueOf(Jamount))
		.append("-").append(String.valueOf(Textamount));
		//登陆结束
	}
	
	
	
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
