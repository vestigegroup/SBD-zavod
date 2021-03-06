package sample.components.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import sample.components.models.Type;
import sample.Login;
import sample.dao.DaoUtils;
import sample.dao.database;
import sample.utils.utils;

import java.sql.*;


public class TypeDao {
    private Connection myConn = null;
    private String user_id = String.valueOf(Login.currentUser.getId());

    public TypeDao() {
        try {
            myConn = database.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initializeTable(TableView tableView) throws SQLException {

        Statement statement = null;
        ResultSet resultSet = null;

        //List to add items
        ObservableList<Type> types = FXCollections.observableArrayList();
        try {
            statement = myConn.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM type_v ORDER BY id");
            while (resultSet.next()) {
                Type type = new Type();
                type.setId(resultSet.getString("id"));
                type.setName(resultSet.getString("name"));
                type.setUnit(resultSet.getString("unit"));
                type.setInfo(resultSet.getString("info"));
                type.setDate(resultSet.getString("date_cr"));
                type.setCr_by(resultSet.getString("cr_by"));

                types.add(type);
            }
            tableView.setItems(types);


        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            DaoUtils.close(statement, resultSet);
        }
    }

    public void addType(String name, String type, String info) throws SQLException {

        try (PreparedStatement pr = myConn.prepareStatement("INSERT INTO type(Name,unit,info,date_cr, cr_by) VALUES (?,?,?,?,?)")) {

            pr.setString(1, name);
            pr.setString(2, typeMaker(type));
            pr.setString(3, info);
            pr.setString(4, String.valueOf(utils.getCurrentDate()));
            pr.setString(5, user_id);
            pr.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void updateType(String name, String info, String id) throws SQLException {
        try (PreparedStatement pr = myConn.prepareStatement("UPDATE  sbd_factory.type t set t.name=?, info=? where id=?")) {
            pr.setString(1, name);
            pr.setString(2, info);
            pr.setString(3, id);
            pr.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteType(String id) throws SQLException {
      //  PreparedStatement pr = null;
        try{
            /*pr = myConn.prepareStatement("DELETE FROM sbd_factory.type  WHERE id=?");
            pr.setString(1,id);
            pr.executeUpdate();*/
        }catch (Exception e){
            e.printStackTrace();
        }finally {
        }
    }

    public String typeMaker(String type){
        String unit_id = "1";
        switch (type) {
            case "Kg":
                unit_id = "2";
                break;
            case "Dona":
                unit_id = "1";
                break;
            case "Rulon":
                unit_id = "3";
                break;
            case "Litr":
                unit_id = "4";
                break;
            case "m2":
                unit_id = "5";
                break;
        }
        return unit_id;
    }


}
