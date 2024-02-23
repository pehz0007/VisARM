/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javafx.fxml.FXML
 *  javafx.fxml.FXMLLoader
 *  javafx.scene.control.Label
 *  javafx.scene.layout.VBox
 *  javafx.scene.text.Text
 */
package visual;

import java.io.IOException;
import java.io.InputStream;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.apache.commons.io.IOUtils;

public class AboutPane
extends VBox {
    @FXML
    private Label lblTitle;
    @FXML
    private Label lblAbout;
    @FXML
    private Text txtRstaLicense;
    @FXML
    private Text txtControlsFxLicense;
    @FXML
    private Text txtCommonsIoLicense;
    @FXML
    private Text txtJreLicense;
    @FXML
    private Text txtOpenSansLicense;
    @FXML
    private Text txtNsMenuFxLicense;

    AboutPane() {
        FXMLLoader fxmlLoader = new FXMLLoader(((Object)((Object)this)).getClass().getResource("/AboutPane.fxml"));
        fxmlLoader.setRoot((Object)this);
        fxmlLoader.setController((Object)this);
        try {
            fxmlLoader.load();
            this.lblTitle.setText("VisUAL - Version 1.27\nCreated by Salman Arif, Imperial College London, 2015");
            this.lblAbout.setText("VisUAL has been made possible by use of the following third-party libraries:\nRSyntaxTextArea Copyright \u00a9 2012, Robert Futrell. All rights reserved.\nControlsFX Copyright \u00a9 2013, ControlsFX. All rights reserved.\nApache Commons IO Copyright \u00a9 2002-2014, The Apache Software Foundation. All rights reserved.\nOracle Java JRE " + System.getProperty("java.version") + " Copyright \u00a9 Oracle Corporation. All rights reserved.\nNSMenuFX Copyrihgt \u00a9 2015 codecentric AG. All rights reserved.\nGoogle Fonts Open Sans Font \u00a9 Steve Matteson.");
            InputStream rstaStream = ((Object)((Object)this)).getClass().getClassLoader().getResourceAsStream("rsyntaxtextarea_LICENSE.txt");
            InputStream controlsFxStream = ((Object)((Object)this)).getClass().getClassLoader().getResourceAsStream("controlsfx_LICENSE.txt");
            InputStream commonsIoStream = ((Object)((Object)this)).getClass().getClassLoader().getResourceAsStream("commons-io_LICENSE.txt");
            InputStream jreStream = ((Object)((Object)this)).getClass().getClassLoader().getResourceAsStream("oraclebinarycode_LICENSE.txt");
            InputStream openSansStream = ((Object)((Object)this)).getClass().getClassLoader().getResourceAsStream("opensans_LICENSE.txt");
            InputStream nsMenuFxStream = ((Object)((Object)this)).getClass().getClassLoader().getResourceAsStream("nsmenufx_LICENSE.txt");
            this.txtRstaLicense.setText(IOUtils.toString(rstaStream));
            this.txtControlsFxLicense.setText(IOUtils.toString(controlsFxStream));
            this.txtCommonsIoLicense.setText(IOUtils.toString(commonsIoStream));
            this.txtJreLicense.setText(IOUtils.toString(jreStream));
            this.txtOpenSansLicense.setText(IOUtils.toString(openSansStream));
            this.txtNsMenuFxLicense.setText(IOUtils.toString(nsMenuFxStream));
        }
        catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
}

