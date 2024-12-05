package com.activate.ActivateMSV1.presentation;

import com.activate.ActivateMSV1.infra.DTO.InterestDTO;
import com.activate.ActivateMSV1.infra.DTO.LocationDTO;
import com.activate.ActivateMSV1.infra.DTO.RequestRegisterDTO;
import com.activate.ActivateMSV1.infra.DTO.UserDTO;
import com.activate.ActivateMSV1.infra.util.GUIVerifier;
import com.activate.ActivateMSV1.service.TokenManager;
import com.activate.ActivateMSV1.service.UserService;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import static com.activate.ActivateMSV1.infra.util.GUIVerifier.*;

public class LoginView {
    private JTextField txtIdLogin;
    private JButton btnLoginAsParticipant;
    private JTextField txtName;
    private JComboBox cbInterests;
    private JButton agregarButton;
    private JButton btnRegisterUser;
    private JPanel loginPanel;
    private JButton btnLoginAsOrganizator;
    private JLabel lblInterests;
    private JTextField txtAge;
    private JTextField txtEmail;
    private JTextField txtLatitude;
    private JTextField txtLongitude;
    private JLabel lblStatus;
    private JPasswordField pwdFieldPassword;
    private JTextField txtUsername;
    private JTextField txtLastNames;
    private JPasswordField pwdPassword;

    private JFrame frame;
    private TokenManager tokenManager = new TokenManager();

    //Intereses
    private Map<String, InterestDTO> interestMap = new HashMap<>();
    private ArrayList<InterestDTO> interests = new ArrayList<>();

    public LoginView() {
        frame = new JFrame("Login");
        frame.setContentPane(loginPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        interestMap.put("Videojuegos", InterestDTO.VIDEOGAMES);
        interestMap.put("Política", InterestDTO.POLITICS);
        interestMap.put("Gastronomía", InterestDTO.GASTRONOMY);
        interestMap.put("Deportes", InterestDTO.SPORTS);
        interestMap.put("Tecnología", InterestDTO.TECHNOLOGY);
        interestMap.put("Música", InterestDTO.MUSIC);
        interestMap.put("Cine", InterestDTO.CINEMA);
        interestMap.put("Literatura", InterestDTO.LITERATURE);
        interestMap.put("Ciencia", InterestDTO.SCIENCE);
        interestMap.put("Historia", InterestDTO.HISTORY);
        interestMap.put("Arte", InterestDTO.ART);
        btnLoginAsParticipant.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if(isTextFieldEmpty(txtIdLogin, "El nombre de usuario es obligatorio"))return;
                if(isPasswordFieldEmpty(pwdFieldPassword,"La contraseña es obligatoria"))return;
                String username = txtIdLogin.getText();
                String password = new String(pwdFieldPassword.getPassword());
                UserDTO user=null;

                try {
                    tokenManager.setTokens(UserService.login(username, password));
                    user = UserService.getUser(username, tokenManager.getAccessToken());

                } catch (Exception ex) {
                    System.err.println(ex.getMessage());
                    lblStatus.setText("Usuario no encontrado");
                    return;
                }
                ParticipantView participantView = new ParticipantView(frame, user, tokenManager, username);
                participantView.show();
                frame.setVisible(false);
            }
        });

        btnLoginAsOrganizator.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(isTextFieldEmpty(txtIdLogin, "El nombre de usuario es obligatorio"))return;
                if(isPasswordFieldEmpty(pwdFieldPassword,"La contraseña es obligatoria"))return;
                String username = txtIdLogin.getText();
                String password = new String(pwdFieldPassword.getPassword());
                UserDTO user=null;

                try {

                    tokenManager.setTokens(UserService.login(username, password));
                    user = UserService.getUser(username, tokenManager.getAccessToken());

                } catch (Exception ex) {
                    lblStatus.setText("Usuario no encontrado");
                    return;
                }

                OrganizatorView organizatorViewView = new OrganizatorView(frame, user.getId(), username, tokenManager);
                organizatorViewView.show();
                frame.setVisible(false);
            }
        });
        loginPanel.addComponentListener(new ComponentAdapter() {
        });

        initCBInterests();
        agregarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addInterest();
            }
        });
        btnRegisterUser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerUser();
            }
        });
    }

    private void initCBInterests(){
        DefaultComboBoxModel<String> interestModel = new DefaultComboBoxModel<>();
        interestModel.addElement("Seleccione una opción");
        interestModel.addElement("Videojuegos");
        interestModel.addElement("Política");
        interestModel.addElement("Gastronomía");
        interestModel.addElement("Deportes");
        interestModel.addElement("Tecnología");
        interestModel.addElement("Música");
        interestModel.addElement("Cine");
        interestModel.addElement("Literatura");
        interestModel.addElement("Ciencia");
        interestModel.addElement("Historia");
        interestModel.addElement("Arte");
        cbInterests.setModel(interestModel);
    }

    private void addInterest(){
        if(GUIVerifier.isComboBoxNotSelected(cbInterests, "Seleccione un interés")){
            return;
        }
        String selectedInterest = (String) cbInterests.getSelectedItem();
        InterestDTO interest = interestMap.get(selectedInterest);
        if (interest != null && !interests.contains(interest)) {
            interests.add(interest);
            lblInterests.setText(lblInterests.getText() + selectedInterest + ", ");
        }
    }

    public void show() {
        frame.setVisible(true);
    }

    private void registerUser() {
        RequestRegisterDTO user = new RequestRegisterDTO();
        LocationDTO locationUser = new LocationDTO();
        //Validar Campos
        if(isTextFieldEmpty(txtName,"Los nombres son obligatorios"))return;
        if(isTextFieldEmpty(txtLastNames,"Los apellidos son obligatorios"))return;
        if(isTextFieldEmpty(txtUsername,"El usuario es obligatorio"))return;
        if(isPasswordFieldEmpty(pwdPassword,"La contraseña es obligatoria"))return;
        if(isTextFieldEmpty(txtEmail,"El email es obligatorio"))return;
        if(isTextFieldEmpty(txtUsername,"El email es obligatorio"))return;
        if(isTextFieldNotPositiveNumeric(txtAge,"La edad debe ser numerica"))return;
        if(isTextFieldNotNumeric(txtLatitude,"La latitude debe ser numerica"))return;
        if(isTextFieldNotNumeric(txtLongitude,"La longitud debe ser numerica"))return;

        //Llenar DTO
        user.setFirstName(txtName.getText());
        user.setLastName(txtLastNames.getText());
        user.setEmail(txtEmail.getText());
        user.setAge(Integer.parseInt(txtAge.getText()));
        locationUser.setLongitude(Double.parseDouble(txtLongitude.getText()));
        locationUser.setLatitude(Double.parseDouble(txtLatitude.getText()));
        user.setLocation(locationUser);
        user.setInterests(new HashSet<>(interests));
        user.setUsername(txtUsername.getText());
        user.setPassword(new String(pwdPassword.getPassword()));

        boolean status = false;
        try {
            UserService.registerUser(user);
            lblStatus.setText("Usuario registrado correctamente");
            txtName.setText("");
            txtLastNames.setText("");
            txtUsername.setText("");
            pwdPassword.setText("");
            txtEmail.setText("");
            txtAge.setText("");
            txtLatitude.setText("");
            txtLongitude.setText("");
            lblInterests.setText("");
            interests.clear();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            lblStatus.setText(e.getMessage());
        }



    }

}
