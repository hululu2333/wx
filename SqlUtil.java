package com.hu;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 一个工具类
 * 用于封装sql语句的操作
 * @author hu
 *
 */
public class SqlUtil {
	
	private final static String DB_NAME="wx";//操作的数据库名称
	
	private SqlUtil() {//将构造函数定义为私有
		
	}
	
	
	
	/**
	 *封装查询操作
	 *接收查询的sql语句，返回结果集 
	 */
	public static ResultSet doSelect(String sql) {
		//1.加载驱动
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//2.建立连接
		Connection conn=null;
		try {
			conn=DriverManager.getConnection("jdbc:mysql://localhost:3306/"+DB_NAME, "root", "1008");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//3.开始查询
		ResultSet rs=null;
		try {
			PreparedStatement ps=conn.prepareStatement(sql);//建立查询对象，PreparedStatement会对sql语句进行预处理
			rs=ps.executeQuery();//执行查询并返回结果集
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		//不使用连接池的情况下，程序结束后rs,ps,conn会被自动回收
		
		return rs;
	}
	
	
	
	/**
	 * 封装更新操作，insert/update/delete
	 * 接收sql语句，返回更新的行数
	 */
	public static int doUpdate(String sql) {
		//1.加载驱动
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//2.建立连接
		Connection conn=null;
		try {
			conn=DriverManager.getConnection("jdbc:mysql://localhost:3306/"+DB_NAME+"?useUnicode=true&characterEncoding=utf-8", "root", "1008");
																					//让jdbc以utf-8的形式编码sql语句				
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//3.开始更新
		int rowCount=0;
		try {
			PreparedStatement ps=conn.prepareStatement(sql);
			rowCount=ps.executeUpdate(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//没有使用连接池，不手动关资源
		
		return rowCount;
	}
	
	
	
	/**
	 * 封装添加用户的数据库操作
	 * 用户信息表和心情统计表新加一列数据，系统数据表的用户总数加一
	 * 返回用户的用户编号
	 * @return
	 */
	public static int addUser(String openid) {
		//1.用户信息表加一行数据
		
			//first.查找系统数据表，获得现有的用户数，用于给新用户分配用户编号
		String sql="select Uamount from sysinfo";//查询代码
		ResultSet rs=doSelect(sql);//获得结果集
		int uno=0;//新用户的编号
		try {
			while(rs.next()) {
				uno=rs.getInt(1)+1;//这就是新用户的编号
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
			//second.用户信息表加一行数据
		sql="insert into user values("+uno+","+"'"+openid+"'"+",0,0)";
		doUpdate(sql);//用户信息表加一行数据
		
		
		//2.心情统计表加一列数据
		sql="insert into moodcount values("+uno+",0,0,0,0,0,0,0,0)";
		doUpdate(sql);
		
		
		//3.系统数据表用户总数加一
		sql="update sysinfo set Uamount=Uamount+1 where main=1";
		doUpdate(sql);
		
		return uno;
	}
	
	
	
	
	/**
	 * 提交日记时调用这个方法
	 * 传入用户编号，日记标题，日记内容，日记时间，心情，天气（图片暂不考虑） mood必须是数据库中各个心情的名称
	 * 1.修改用户信息表，已写日记篇数加一，日记总字数加若干
	 * 2.查询系统数据表，得到这篇日记的编号，并将日记总数加一
	 * 3.在日记表中插入一条记录
	 * 4.根据用户编号和心情修改心情统计表
	 */
	public static void addJour(int uNo,String Jtitle,String Jtext,String Jdate,String mood,String weather) {
		//1.修改用户信息表
		int wordAmount=Jtitle.length()+Jtext.length();//这篇日记的总字数
		String sql1="update user set Jamount=Jamount+1 where uno="+uNo;//日记总数加一
		String sql2="update user set Tamount=Tamount+"+wordAmount+ " where uno="+uNo;//总字数加若干
			//开始执行sql
		doUpdate(sql1);
		doUpdate(sql2);
		
		//2.查询系统数据表，获得当前日记编号并加一
		String sql3="select Jamount from sysinfo";
		ResultSet rs=doSelect(sql3);
		int jno=0;//定义日记编号
		try {
			if(rs.next()) {
				jno=rs.getInt(1)+1;//得到日记编号
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
			//现在已经拿到了日记编号，接下来要把总日记数加一
		String sql4="update sysinfo set Jamount=Jamount+1 where main=1";
		doUpdate(sql4);//总日记数加一
		
		//3.在日记表中插入记录
		String sql5="insert into journal values("+
				uNo+","+jno+","+"'"+Jtitle+"'"+","+"'"+Jtext+"'"+","+"'"+Jdate+"'"+","+"'"+mood+"'"+","+"'"+weather+"'"+","+null
				+")";
		doUpdate(sql5);//在日记表中插入数据
		
		//4.修改心情统计表 mood必须是数据库中各个心情的名称
		String sql6="update moodcount set "+mood+"="+mood+"+1"+" where uno="+uNo;
		doUpdate(sql6);
		
	}
	
	
	/**
	 * 返回数据库中这个用户写的所有的日记
	 * @return
	 */
	public static ResultSet requestAllJou(int uno) {
		String sql="select* from journal where uno="+uno;
		return doSelect(sql);
	} 
	
}
