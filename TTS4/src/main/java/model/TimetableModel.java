package model;

import com.example.tts4.ClassInfo;
import com.example.tts4.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class TimetableModel {
    public List<ClassInfo> fetchClassInfoFromDatabase() {
        List<ClassInfo> classInfoList = new ArrayList<>();

        try (Connection connection = Database.getConnection()) {
            Statement statement = connection.createStatement();
            String query = "SELECT course_name, room_no, block, name FROM courses, room, faculty ORDER BY RANDOM() LIMIT 17";
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                String courseName = resultSet.getString("course_name");
                String roomNumber = resultSet.getString("room_no");
                String block = resultSet.getString("block");
                String teacherName = resultSet.getString("name");

                ClassInfo classInfo = new ClassInfo(courseName, roomNumber, block, teacherName);
                classInfoList.add(classInfo);
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return classInfoList;
    }

    public int getIntervalFromDatabase(String slot) {

        int interval = 0;


        try(Connection connection = Database.getConnection()) {

            String sql = "SELECT interval FROM timetable WHERE slot = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, slot);


            ResultSet resultSet = statement.executeQuery();


            if (resultSet.next()) {

                interval = resultSet.getInt("interval");
            }


            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {

            e.printStackTrace();
        }

        return interval;
    }



}


