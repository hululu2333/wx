package com.hu;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class SubmitJou
 */
@WebServlet("/SubmitJou")
public class SubmitJou extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public SubmitJou() {
        super();
    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//1.�������ݿ�����û�н�����ռ����ж��Ǹ����ռǻ��Ǳ����ռ�
//		String sql="select * from Journal where ";
		
		//2.���ݿ�û�н�����ռǣ��¼�
		//�����û���ţ��ռǱ��⣬�ռ����ݣ��ռ�ʱ�䣬���飬������ͼƬ�ݲ����ǣ� mood���������ݿ��и������������
				//love happy daze working sad cry angry died
		SqlUtil.addJour(Integer.parseInt(request.getParameter("uno")), request.getParameter("title"), request.getParameter("content"),
				request.getParameter("date"), request.getParameter("mood"), request.getParameter("weather"));
		
		response.getWriter().append("�ռ����ύ");
	}


	
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
