package cn.nearf.dw;

import java.sql.*;

public class TestSpringBlob {

	static String url = "jdbc:postgresql://rama.helens.com.cn:5432/test";
	static String usr = "gpadmin";
	static String psd = "gpadmin";

	public static void main(String args[]) {
		Connection conn = null;
		try {
			Class.forName("org.postgresql.Driver");
			conn = DriverManager.getConnection(url, usr, psd);
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("SELECT xzcxlj,zj FROM data.dw_jx_org_dim");
			while (rs.next()) {
				String old = rs.getString(1);
				String zj = rs.getString(2);
				System.out.println(zj+" ::: "+old);
				if (old != null && old.trim().length() > 0) {
					String t[] = old.split(",");
					String sql = "";
					if(t.length ==6) {
						sql = "update data.dw_jx_org_dim set jg0_code='"+t[0]+"'"+",jg1_code='"+t[1]+"'"+",jg2_code='"+t[2]+"'"+",jg3_code='"+t[3]+"'"+",jg4_code='"+t[4]+"'"+",jg5_code='"+t[5]+"'"+" where zj='"+zj+"'"; 
					} else if (t.length == 5) {
						sql = "update data.dw_jx_org_dim set jg0_code='"+t[0]+"'"+",jg1_code='"+t[1]+"'"+",jg2_code='"+t[2]+"'"+",jg3_code='"+t[3]+"'"+",jg4_code='"+t[4]+"'"+",jg5_code='"+t[4]+"'"+" where zj='"+zj+"'"; 
					}else if (t.length == 4) {
						sql = "update data.dw_jx_org_dim set jg0_code='"+t[0]+"'"+",jg1_code='"+t[1]+"'"+",jg2_code='"+t[2]+"'"+",jg3_code='"+t[3]+"'"+",jg4_code='"+t[3]+"'"+",jg5_code='"+t[3]+"'"+" where zj='"+zj+"'"; 
					}else if (t.length == 3) {
						sql = "update data.dw_jx_org_dim set jg0_code='"+t[0]+"'"+",jg1_code='"+t[1]+"'"+",jg2_code='"+t[2]+"'"+",jg3_code='"+t[2]+"'"+",jg4_code='"+t[2]+"'"+",jg5_code='"+t[2]+"'"+" where zj='"+zj+"'"; 
					}else if (t.length == 2) {
						sql = "update data.dw_jx_org_dim set jg0_code='"+t[0]+"'"+",jg1_code='"+t[1]+"'"+",jg2_code='"+t[1]+"'"+",jg3_code='"+t[1]+"'"+",jg4_code='"+t[1]+"'"+",jg5_code='"+t[1]+"'"+" where zj='"+zj+"'"; 
					}else if (t.length == 1) {
						sql = "update data.dw_jx_org_dim set jg0_code='"+t[0]+"'"+",jg1_code='"+t[0]+"'"+",jg2_code='"+t[0]+"'"+",jg3_code='"+t[0]+"'"+",jg4_code='"+t[0]+"'"+",jg5_code='"+t[0]+"'"+" where zj='"+zj+"'"; 
					}
					System.out.println(sql);
					update(conn, sql);
					
					//UPDATE data.dw_jx_org_dim SET jg1_title = (SELECT jgmc FROM data.ip_unit WHERE data.dw_jx_org_dim.jg1_code = data.ip_unit.zj);
				}
//				System.out.println(rs.getString(1));
			}
			rs.close();
			st.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static boolean update(Connection conn, String sql) {
        PreparedStatement pStatement = null;
        int rs = 0;
        try {
            pStatement = conn.prepareStatement(sql);
            rs = pStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (rs > 0) {
            return true;
        }
        return false;
    }

}