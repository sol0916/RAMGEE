package ramgee.project.dao;

import java.sql.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ramgee.project.db.DBProperties;
import ramgee.project.vo.*;


public class OrderDAO {
		
	
	//field
	private String url = DBProperties.URL;
	private String uid = DBProperties.UID;
	private String upw = DBProperties.UPW;
	
	private Connection connection;
	
	//생성자
	public OrderDAO() {
		
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (Exception e) {
			System.out.println("CLASS FOR NAME ERR");
		}		
	}
	//주문 생성
	public int insertOrder(ProductVO productVO, OrderVO orderVO) {
		int result = 0; //0이 반환되면 실패, 1이 반환되면 성공

		//insert, update, delete는 ResultSet 객체가 필요없음
		Connection connection = null;
		PreparedStatement statement = null;
		
        String query = "INSERT INTO USER_ORDER VALUES ( order_no_seq.NEXTVAL, SYSDATE, ?, ?, ?, ?, ?)";
        try{
        	connection = DriverManager.getConnection(url, uid, upw);

            // 준비된 문장에 값을 설정합니다.
			statement = connection.prepareStatement(query);
            statement.setInt(1, orderVO.getTotal());
            statement.setString(2, orderVO.getTo_hall());
            statement.setString(3, orderVO.getSize());
            statement.setInt(4, orderVO.getProduct_count());
            statement.setInt(5,  productVO.getProdut_no());

            result = statement.executeUpdate();  // 쿼리를 실행하여 주문을 추가합니다.
        } catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				connection.close();
				statement.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	// 주문 수정
	public void updateOrder(OrderVO orderVO) throws SQLException {
        String query = "UPDATE user_order SET total = ?, to_hall = ?, size = ?, product_count =?  WHERE order_no = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, orderVO.getTotal());
            statement.setString(2, orderVO.getTo_hall());
            statement.setString(3, orderVO.getSize());
            statement.setInt(4, orderVO.getProduct_count());
            statement.setInt(5, orderVO.getOrder_no());

            statement.executeUpdate();  // 쿼리를 실행하여 주문을 업데이트합니다.
        }
    }

    // 3. 주문 삭제
    public void deleteOrder(OrderVO orderVO) throws SQLException {
        String query = "DELETE FROM user_order WHERE order_no = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, orderVO.getOrder_no());

            statement.executeUpdate();  // 쿼리를 실행하여 주문을 삭제합니다.
        }
    }

    // 4. order_no 로 오더 조회
    public OrderVO findOrderById(OrderVO orderVO) throws SQLException {
        OrderVO order = null; // 조회받은 OrderVO

        String query = "SELECT * FROM USER_ORDER WHERE order_no = ?";
        
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, orderVO.getOrder_no());

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    order = mapResultSetToOrder(resultSet);  // 조회된 결과를 OrderVO 객체로 매핑합니다.
                }
            }
        }
        return order;
    }
 // 5. 모든 주문을 조회
    public List<OrderVO> findAllOrder() throws SQLException {
        List<OrderVO> order_list = new ArrayList<>();
        String query = "SELECT * FROM user_order";
        try (
        		PreparedStatement statement = connection.prepareStatement(query);
        		ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
            	
                OrderVO order = mapResultSetToOrder(resultSet);  // 조회된 결과를 OrderVO 객체로 매핑합니다.
                order_list.add(order);
            }
        }catch (Exception e) {
			e.printStackTrace();
			System.out.print("사유 : " + e.getMessage());
		}
        return order_list;
    }

    // ResultSet을 OrderVO 객체로 매핑하는 보조 메서드입니다.
    private OrderVO mapResultSetToOrder(ResultSet resultSet) throws SQLException {
        int order_no = resultSet.getInt("order_no");
        Date order_date = resultSet.getDate("order_date");
        int total = resultSet.getInt("total");
        String to_hall = resultSet.getString("to_hall");
        String size = resultSet.getString("size");
        int product_count = resultSet.getInt("product_count");
        int product_no = resultSet.getInt("product_no");
        	
        return new OrderVO(order_no, order_date, total, to_hall, size, product_count, product_no);
    }
	
	//주문 메서드(포장여부,hot/ice, size 등)
//	public List<OrderVO> addOrdertoCart(ProductVO productVO, CartVO cartVO) {
//		
//		List<MenuVO> cart = new ArrayList<>();
//		
//		//수정 필요
//		String sql = "INSERT INTO ORDER_LIST VALUES (?, ?, SYSDATE, ?, ?, ?, ?)";
//		
//		
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		
//		int result = 0;
//		
//		try {
//			
//			conn = DriverManager.getConnection(url, uid, upw);
//			
//			pstmt = conn.prepareStatement(sql);
//			pstmt.setString(1, menu.getProduct_name());
//			pstmt.setInt(2, menu.getPrice());
//			
//			result = pstmt.executeUpdate();
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			try {
//				conn.close();
//				pstmt.close();
//			} catch (Exception e2) {
//				e2.printStackTrace();
//			}
//		}
//		
//		
//		return cart;
//	}
	
	
	//주문 내역 메서드
	

}
