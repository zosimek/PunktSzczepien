import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


public class LogWindowController {

    private DatabaseConnection databaseConnection;

    private Stage stage;
    private Scene scene;
    private Parent root;



    @FXML // ResourceBundle that was given to the FXMLLoader
        private ResourceBundle resources;

        @FXML // URL location of the FXML file that was given to the FXMLLoader
        private URL location;

        @FXML // fx:id="checkboxAdmin"
        private CheckBox checkboxAdmin; // Value injected by FXMLLoader

        @FXML // fx:id="haslo"
        private TextField haslo; // Value injected by FXMLLoader

        @FXML // fx:id="imie"
        private TextField imie; // Value injected by FXMLLoader

        @FXML // fx:id="login"
        private TextField login; // Value injected by FXMLLoader

        @FXML // fx:id="nazwisko"
        private TextField nazwisko; // Value injected by FXMLLoader

        @FXML // fx:id="pesel"
        private TextField pesel; // Value injected by FXMLLoader

        @FXML // fx:id="telefon"
        private TextField telefon; // Value injected by FXMLLoader

        @FXML // fx:id="utworzKonto"
        private Button utworzKonto; // Value injected by FXMLLoader

        @FXML // fx:id="zaloguj"
        private Button zaloguj; // Value injected by FXMLLoader

    @FXML
    private TextField pacjentHaslo;

    @FXML
    private TextField pacjentLogin;

    @FXML
    private Text tvLogInfo;


    @FXML
        void clickedUtworzKonto(ActionEvent event) {

        }

        @FXML
        void clickedZaloguj(ActionEvent event) throws SQLException {

            if (this.login.getText() != (Object) null) {
                tvLogInfo.setText("Probowales sie zalogowac");
                String loginOut = validateLogin().get(0).toString();
                if(loginOut.equals("pacjent")){
                 try{
                    Parent root=  FXMLLoader.load(getClass().getResource("pacjentwindow.fxml"));
                    stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                    scene= new Scene(root,800,600);
                    stage.setScene(scene);
                    stage.show();

                } catch (Exception e) {
                    e.printStackTrace();
                    e.getCause();
                }}
            }else{
                tvLogInfo.setText("Wprowadz login i haslo");
            }

        }

        @FXML
        void isAdmin(ActionEvent event) {

        }

        @FXML // This method is called by the FXMLLoader when initialization is complete
        void initialize() {
            assert checkboxAdmin != null : "fx:id=\"checkboxAdmin\" was not injected: check your FXML file 'logwindow.fxml'.";
            assert haslo != null : "fx:id=\"haslo\" was not injected: check your FXML file 'logwindow.fxml'.";
            assert imie != null : "fx:id=\"imie\" was not injected: check your FXML file 'logwindow.fxml'.";
            assert login != null : "fx:id=\"login\" was not injected: check your FXML file 'logwindow.fxml'.";
            assert nazwisko != null : "fx:id=\"nazwisko\" was not injected: check your FXML file 'logwindow.fxml'.";
            assert pesel != null : "fx:id=\"pesel\" was not injected: check your FXML file 'logwindow.fxml'.";
            assert telefon != null : "fx:id=\"telefon\" was not injected: check your FXML file 'logwindow.fxml'.";
            assert utworzKonto != null : "fx:id=\"utworzKonto\" was not injected: check your FXML file 'logwindow.fxml'.";
            assert zaloguj != null : "fx:id=\"zaloguj\" was not injected: check your FXML file 'logwindow.fxml'.";
            assert pacjentHaslo != null : "fx:id=\"pacjentHaslo\" was not injected: check your FXML file 'logwindow.fxml'.";
            assert pacjentLogin != null : "fx:id=\"pacjentLogin\" was not injected: check your FXML file 'logwindow.fxml'.";
            assert tvLogInfo != null : "fx:id=\"tvLogInfo\" was not injected: check your FXML file 'logwindow.fxml'.";
            databaseConnection = new DatabaseConnection("admin_punktu", "admin1");
            databaseConnection.getConnection();
            tvLogInfo.setText("dziala w miare");
        }

        public List<String> validateLogin() throws SQLException {
            String loginOut;
            String hasloOut;
            String peselOut = null;
            int nrPwzOut = 0;
            List<String> logAndPassword = new ArrayList<>();
            if(login.getText().toString()=="admin_punktu"&& haslo.getText().toString()=="admin1"){
                loginOut = "admin_punktu";
                hasloOut = "admin1";
            }else{
            CallableStatement cStm = databaseConnection.getDatabaseLink().prepareCall("{call login_func(?, ?, ?, ?, ?, ?, ?)}");
            cStm.setString(1,login.getText().toString());
            cStm.setString(2,haslo.getText().toString());
            cStm.registerOutParameter(3, Types.CHAR);
            cStm.registerOutParameter(4, Types.INTEGER);
            cStm.registerOutParameter(5, Types.CHAR);
            cStm.registerOutParameter(6, Types.VARCHAR);
            cStm.registerOutParameter(7, Types.VARCHAR);
            cStm.executeUpdate();
            peselOut = cStm.getString(3);
            nrPwzOut = cStm.getInt(4);
            loginOut = cStm.getString(6);
            hasloOut= cStm.getString(7);
            databaseConnection.dbDisconnect();
            DatabaseConnection databaseConnection1 = new DatabaseConnection(loginOut, hasloOut);
            databaseConnection1.getConnection();
            tvLogInfo.setText( loginOut);
            }
            logAndPassword.add(loginOut);
            logAndPassword.add(hasloOut);
            if(peselOut != null){
                logAndPassword.add(peselOut);
            }else if(nrPwzOut !=0){
                logAndPassword.add(String.valueOf(nrPwzOut));
            }
            return logAndPassword;

        }

        public void getNewView(){


        }

    }
