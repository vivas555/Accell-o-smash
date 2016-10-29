package ca.csf.tp3;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.h2.tools.DeleteDbFiles;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class AcceleroSmashRepository {

    private static final String H2_CONNECTION_STRING = "jdbc:h2:%s/%s";

    private final String databasePath;
    private final String databaseName;
    private final String usernane;
    private final String password;

    public AcceleroSmashRepository(String databasePath, String databaseName, String usernane, String password) throws CriticalRepositoryException {
        this.databasePath = databasePath;
        this.databaseName = databaseName;
        this.usernane = usernane;
        this.password = password;

        try {
            //Nécessaire à charger le pilote de base de données en mémoire
            Class.forName("org.h2.Driver");

            //Pour l'exemple, on re-crée la base de données à chaque fois
            DeleteDbFiles.execute(databasePath, databaseName, true);

            Connection connection = getH2Connection();
            Statement statement = connection.createStatement();
            statement.execute(IOUtils.toString(getClass().getClassLoader().getResourceAsStream("createTables.sql")));
            statement.close();
            connection.close();
        } catch (ClassNotFoundException | SQLException | IOException e) {
            throw new CriticalRepositoryException("Unable to initialiseto the database", e);
        }
    }

    public String createAccount(Account account) throws CriticalRepositoryException {
        //TODO : Faire ici la requête SQL d'ajout (INSERT INTO).

        int resultCode = -1;
        String resultMsg = "";

        try {
            //INSERT INTO USER (USER_Pseudo, USER_Password, USER_Name, USER_FirstName, USER_EmergencyNumber, USER_Weight, USER_SeatBeltAlwaysOn) VALUES ('qq', 'qq', 'qq', 'qq', '911', 11.0, 1);
            //INSERT INTO USER (" + Account.getTableColumns() + ") VALUES (?, ?, ?, ?, ?, ?, ?);

            Connection connection = getH2Connection();
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO USER (" + Account.getTableColumns() + ") VALUES (?, ?, ?, ?, ?, ?, ?);");

            for (int i = 1; i < 8; i++) {
                if (i == 6)
                    preparedStatement.setFloat(i, Float.parseFloat(account.getValues(i)));
                else if (i == 7)
                    preparedStatement.setInt(i, Integer.parseInt(account.getValues(i)));
                else
                    preparedStatement.setString(i, account.getValues(i));
            }

            resultCode = preparedStatement.executeUpdate();
            if (resultCode == -1) {
                resultMsg = "The statement was somehow never executed";
            } else if (resultCode == 0) {
                resultMsg = "Failed to add the collision to the database";
            } else {
                resultMsg = "Success";
            }
            //Si des clés on été générés automatiquement par la base de données, il est possible de les récupérer ainsi.
//            ResultSet resultSet = preparedStatement.getGeneratedKeys();
//            if (resultSet.next()) {
//                sample.setId(resultSet.getInt(1));
//            } else {
//                throw new CriticalRepositoryException("Unable to get generated keys from the database. Please make sure that the implementation of the JDBC driver supports it.");
//            }

            //Toujours fermer la connection à la fin
            preparedStatement.close();
            connection.close();

        } catch (SQLException e) {
            throw new CriticalRepositoryException("Unable to add Sample to the database", e);
        }
        return resultMsg;
    }

    public String retrieveAccount(String id) throws CriticalRepositoryException, NotFoundRepositoryException {
        try {
            Connection connection = getH2Connection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT " + Account.getTableColumns() + " FROM USER WHERE USER_Pseudo = ?;");
            preparedStatement.setString(1, id);
            //La méthode "executeQuery" sert aux select. Pour le reste, c'est juste execute.
            ResultSet resultSet = preparedStatement.executeQuery();

            Account acc = null;
            //Ici, il ne devrait y avoir qu'un seul résultat
            if (resultSet.next()) {
                acc = new Account();
                acc.setAccountID(resultSet.getString(1));
                acc.setPassword(resultSet.getString(2));
                acc.setName(resultSet.getString(3));
                acc.setFirstName(resultSet.getString(4));
                acc.setEmergencyNumber(resultSet.getString(5));
                acc.setWeight(resultSet.getFloat(6));
                acc.setSeatBeltAlwaysOn(resultSet.getBoolean(7));
            }

            preparedStatement.close();
            connection.close();

            if (acc != null) {
                ObjectMapper mapper = new ObjectMapper();
                return mapper.writeValueAsString(acc);

            } else {
                throw new NotFoundRepositoryException("Asked Account not in repository. Unable to retrieve.");
            }

        } catch (SQLException e) {
            throw new CriticalRepositoryException("Unable to read Sample from the database", e);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String createCollision(Collision collision) throws CriticalRepositoryException {
        //TODO : Faire ici la requête SQL d'ajout (INSERT INTO).

        int resultCode = -1;
        String resultMsg = "";

        try {
            Connection connection = getH2Connection();
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO COLLISION (" + Collision.getTableColumns() + ") VALUES (?, ?, ?, ?, ?);");


            preparedStatement.setNull(1, Types.VARCHAR);
            preparedStatement.setDate(2, collision.getCollisionDate());
            preparedStatement.setFloat(3, collision.getCollisionStrength());
            preparedStatement.setInt(4, collision.isCallDoneAsInteger());
            preparedStatement.setString(5, collision.getCollisionOwner());

            resultCode = preparedStatement.executeUpdate();
            if (resultCode == -1) {
                resultMsg = "The statement was somehow never executed";
            } else if (resultCode == 0) {
                resultMsg = "Failed to add the collision to the database";
            } else {
                resultMsg = "Success";
            }
            //Si des clés on été générés automatiquement par la base de données, il est possible de les récupérer ainsi.
//            ResultSet resultSet = preparedStatement.getGeneratedKeys();
//            if (resultSet.next()) {
//                sample.setId(resultSet.getInt(1));
//            } else {
//                throw new CriticalRepositoryException("Unable to get generated keys from the database. Please make sure that the implementation of the JDBC driver supports it.");
//            }

            //Toujours fermer la connection à la fin
            preparedStatement.close();
            connection.close();

        } catch (SQLException e) {
            throw new CriticalRepositoryException("Unable to add Sample to the database", e);
        }
        return resultMsg;
    }

    public String retrieveCollisionFromUser(String user) throws CriticalRepositoryException, NotFoundRepositoryException {
        try {
            Connection connection = getH2Connection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT " + Collision.getTableColumns() + " FROM COLLISION WHERE COL_User = ?;");
            preparedStatement.setString(1, user);

            ResultSet resultSet = preparedStatement.executeQuery();

            List<Collision> cols = new ArrayList<>();
            Collision col = null;
            //Ici, il ne devrait y avoir qu'un seul résultat
            while (resultSet.next()) {
                col = new Collision();
                col.setCollisionDate(resultSet.getDate(2));
                col.setCollisionStrength(resultSet.getFloat(3));
                col.setCollisionCallDone(resultSet.getBoolean(4));
                col.setCollisionOwner(resultSet.getString(5));
                cols.add(col);
            }

            preparedStatement.close();
            connection.close();

            if (col != null) {
                ObjectMapper mapper = new ObjectMapper();
                return mapper.writeValueAsString(cols);

            } else {
                throw new NotFoundRepositoryException("Asked Account not in repository. Unable to retrieve.");
            }

        } catch (SQLException e) {
            throw new CriticalRepositoryException("Unable to read Sample from the database", e);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "FAILED";
    }

    public List<Sample> retrieveAll() throws CriticalRepositoryException {
        //TODO : Faire ici la requête SQL de lecture (SELECT ... FROM).
        try {
            Connection connection = getH2Connection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT ID, INFO FROM SAMPLES;");
            ResultSet resultSet = preparedStatement.executeQuery();

            List<Sample> samples = new LinkedList<>();
            //Ici, il devrait y avoir plus d'un seul résultat
            while (resultSet.next()) {
                Sample sample = new Sample();
                sample.setId(resultSet.getInt("ID"));
                sample.setInfo(resultSet.getString("INFO"));

                samples.add(sample);
            }

            preparedStatement.close();
            connection.close();

            return samples;
        } catch (SQLException e) {
            throw new CriticalRepositoryException("Unable to read Sample from the database", e);
        }
    }

//    public void update(Sample sample) throws CriticalRepositoryException, NotFoundRepositoryException {
//        //TODO : Faire ici la requête SQL de mise à jour (UPDATE ... SET).
//        try {
//            Connection connection = getH2Connection();
//            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE SAMPLES SET INFO = ? WHERE ID = ?;");
//            preparedStatement.setString(1, sample.getInfo());
//            //C'est important de mettre un where lorsque l'on fait un UPDATE, sinon, tous les samples auront le même "INFO"
//            preparedStatement.setInt(2, sample.getId());
//
//            //La méthode "ExecuteUpdate" nous permet de savoir combien d'enregistrement on étés affectés.
//            int nbChanged = preparedStatement.executeUpdate();
//
//            preparedStatement.close();
//            connection.close();
//
//            if (nbChanged == 0) {
//                throw new NotFoundRepositoryException("Provided Sample not in repository. Unable to update.");
//            }
//
//        } catch (SQLException e) {
//            throw new CriticalRepositoryException("Unable to add Sample to the database", e);
//        }
//    }
//
//    public void delete(int id) throws CriticalRepositoryException, NotFoundRepositoryException {
//        //TODO : Faire ici la requête SQL de supression (DELETE FROM ... WHERE).
//        try {
//            Connection connection = getH2Connection();
//            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM SAMPLES WHERE ID = ?;");
//            //C'est important de mettre un where lorsque l'on fait un DELETE, sinon, tout sera supprimé
//            preparedStatement.setInt(1, id);
//
//            //La méthode "ExecuteUpdate" nous permet de savoir combien d'enregistrement on étés supprimés.
//            int nbChanged = preparedStatement.executeUpdate();
//
//            preparedStatement.close();
//            connection.close();
//
//            if (nbChanged == 0) {
//                throw new NotFoundRepositoryException("Provided Sample not in repository. Unable to delete.");
//            }
//
//        } catch (SQLException e) {
//            throw new CriticalRepositoryException("Unable to add Sample to the database", e);
//        }
//    }

    private Connection getH2Connection() throws SQLException {
        return DriverManager.getConnection(getH2ConnectionString(),
                                           usernane,
                                           password);
    }

    private String getH2ConnectionString() {
        return String.format(H2_CONNECTION_STRING, databasePath, databaseName);
    }


}
