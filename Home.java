package com.hu;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/Home")
public class Home extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

    public Home() {
        super();
        // TODO Auto-generated constructor stub
    }

    

	/**
	 * ����С������ҳ��ʾ�����ռ�
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int uno=Integer.parseInt(request.getParameter("uno"));//�õ��û����
		ResultSet rs=SqlUtil.requestAllJou(uno);//�õ����е��ռ�
		
		try {
			while(rs.next()) {//��ʼ��������
				response.getWriter().append(rs.getString(3)).append("-").append(rs.getString(4)).append("-")
				.append(rs.getString(5)).append("-").append(rs.getString(6)).append("-").append(rs.getString(7)).append("*");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
