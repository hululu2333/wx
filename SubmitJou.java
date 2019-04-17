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
		//1.根据数据库里有没有今天的日记来判断是更新日记或是保存日记
//		String sql="select * from Journal where ";
		
		//2.数据库没有今天的日记，新加
		//接收用户编号，日记标题，日记内容，日记时间，心情，天气（图片暂不考虑） mood必须是数据库中各个心情的名称
				//love happy daze working sad cry angry died
		SqlUtil.addJour(Integer.parseInt(request.getParameter("uno")), request.getParameter("title"), request.getParameter("content"),
				request.getParameter("date"), request.getParameter("mood"), request.getParameter("weather"));
		
		response.getWriter().append("日记已提交");
	}


	
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
