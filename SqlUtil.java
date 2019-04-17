package com.hu;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * һ��������
 * ���ڷ�װsql���Ĳ���
 * @author hu
 *
 */
public class SqlUtil {
	
	private final static String DB_NAME="wx";//���������ݿ�����
	
	private SqlUtil() {//�����캯������Ϊ˽��
		
	}
	
	
	
	/**
	 *��װ��ѯ����
	 *���ղ�ѯ��sql��䣬���ؽ���� 
	 */
	public static ResultSet doSelect(String sql) {
		//1.��������
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//2.��������
		Connection conn=null;
		try {
			conn=DriverManager.getConnection("jdbc:mysql://localhost:3306/"+DB_NAME, "root", "1008");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//3.��ʼ��ѯ
		ResultSet rs=null;
		try {
			PreparedStatement ps=conn.prepareStatement(sql);//������ѯ����PreparedStatement���sql������Ԥ����
			rs=ps.executeQuery();//ִ�в�ѯ�����ؽ����
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		//��ʹ�����ӳص�����£����������rs,ps,conn�ᱻ�Զ�����
		
		return rs;
	}
	
	
	
	/**
	 * ��װ���²�����insert/update/delete
	 * ����sql��䣬���ظ��µ�����
	 */
	public static int doUpdate(String sql) {
		//1.��������
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//2.��������
		Connection conn=null;
		try {
			conn=DriverManager.getConnection("jdbc:mysql://localhost:3306/"+DB_NAME+"?useUnicode=true&characterEncoding=utf-8", "root", "1008");
																					//��jdbc��utf-8����ʽ����sql���				
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//3.��ʼ����
		int rowCount=0;
		try {
			PreparedStatement ps=conn.prepareStatement(sql);
			rowCount=ps.executeUpdate(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//û��ʹ�����ӳأ����ֶ�����Դ
		
		return rowCount;
	}
	
	
	
	/**
	 * ��װ����û������ݿ����
	 * �û���Ϣ�������ͳ�Ʊ��¼�һ�����ݣ�ϵͳ���ݱ���û�������һ
	 * �����û����û����
	 * @return
	 */
	public static int addUser(String openid) {
		//1.�û���Ϣ���һ������
		
			//first.����ϵͳ���ݱ�������е��û��������ڸ����û������û����
		String sql="select Uamount from sysinfo";//��ѯ����
		ResultSet rs=doSelect(sql);//��ý����
		int uno=0;//���û��ı��
		try {
			while(rs.next()) {
				uno=rs.getInt(1)+1;//��������û��ı��
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
			//second.�û���Ϣ���һ������
		sql="insert into user values("+uno+","+"'"+openid+"'"+",0,0)";
		doUpdate(sql);//�û���Ϣ���һ������
		
		
		//2.����ͳ�Ʊ��һ������
		sql="insert into moodcount values("+uno+",0,0,0,0,0,0,0,0)";
		doUpdate(sql);
		
		
		//3.ϵͳ���ݱ��û�������һ
		sql="update sysinfo set Uamount=Uamount+1 where main=1";
		doUpdate(sql);
		
		return uno;
	}
	
	
	
	
	/**
	 * �ύ�ռ�ʱ�����������
	 * �����û���ţ��ռǱ��⣬�ռ����ݣ��ռ�ʱ�䣬���飬������ͼƬ�ݲ����ǣ� mood���������ݿ��и������������
	 * 1.�޸��û���Ϣ����д�ռ�ƪ����һ���ռ�������������
	 * 2.��ѯϵͳ���ݱ��õ���ƪ�ռǵı�ţ������ռ�������һ
	 * 3.���ռǱ��в���һ����¼
	 * 4.�����û���ź������޸�����ͳ�Ʊ�
	 */
	public static void addJour(int uNo,String Jtitle,String Jtext,String Jdate,String mood,String weather) {
		//1.�޸��û���Ϣ��
		int wordAmount=Jtitle.length()+Jtext.length();//��ƪ�ռǵ�������
		String sql1="update user set Jamount=Jamount+1 where uno="+uNo;//�ռ�������һ
		String sql2="update user set Tamount=Tamount+"+wordAmount+ " where uno="+uNo;//������������
			//��ʼִ��sql
		doUpdate(sql1);
		doUpdate(sql2);
		
		//2.��ѯϵͳ���ݱ���õ�ǰ�ռǱ�Ų���һ
		String sql3="select Jamount from sysinfo";
		ResultSet rs=doSelect(sql3);
		int jno=0;//�����ռǱ��
		try {
			if(rs.next()) {
				jno=rs.getInt(1)+1;//�õ��ռǱ��
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
			//�����Ѿ��õ����ռǱ�ţ�������Ҫ�����ռ�����һ
		String sql4="update sysinfo set Jamount=Jamount+1 where main=1";
		doUpdate(sql4);//���ռ�����һ
		
		//3.���ռǱ��в����¼
		String sql5="insert into journal values("+
				uNo+","+jno+","+"'"+Jtitle+"'"+","+"'"+Jtext+"'"+","+"'"+Jdate+"'"+","+"'"+mood+"'"+","+"'"+weather+"'"+","+null
				+")";
		doUpdate(sql5);//���ռǱ��в�������
		
		//4.�޸�����ͳ�Ʊ� mood���������ݿ��и������������
		String sql6="update moodcount set "+mood+"="+mood+"+1"+" where uno="+uNo;
		doUpdate(sql6);
		
	}
	
	
	/**
	 * �������ݿ�������û�д�����е��ռ�
	 * @return
	 */
	public static ResultSet requestAllJou(int uno) {
		String sql="select* from journal where uno="+uno;
		return doSelect(sql);
	} 
	
}
