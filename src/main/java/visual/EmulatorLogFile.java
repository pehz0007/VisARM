/*
 * Decompiled with CFR 0.152.
 */
package visual;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import visual.Emulator;
import visual.MemWord;
import visual.Register;
import visual.SyntaxError;
import visual.VisUAL;

public class EmulatorLogFile {
    private Document doc;
    private String fileName;
    private Element mainRootElement;
    private static String directory = "";
    private static boolean enabled;
    private static boolean regs;
    private static boolean statusBits;
    private static boolean syntax;
    private static boolean runtime;
    private static boolean changed;
    private static boolean custom;
    private static boolean cycleCount;
    private static List<String> customLocations;

    public EmulatorLogFile(String fileName) {
        this.fileName = fileName;
        DocumentBuilderFactory icFactory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder icBuilder = icFactory.newDocumentBuilder();
            this.doc = icBuilder.newDocument();
            this.mainRootElement = this.doc.createElementNS("http://bitbucket.org/salmanarif/visual", "VisUAL");
            this.doc.appendChild(this.mainRootElement);
            System.out.println("Log file created successfully");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addLine(String id2, String code, Element[] registers, Element statusBitElement, Element[] memChangedElements, Element[] memCustomElements, Element instrCycleCount) {
        int i;
        Element line = this.doc.createElement("line");
        line.setAttribute("id", id2);
        Element codeElement = this.doc.createElement("code");
        codeElement.appendChild(this.doc.createTextNode(code));
        line.appendChild(codeElement);
        if (regs) {
            for (i = 0; i < 16; ++i) {
                line.appendChild(registers[i]);
            }
        }
        if (statusBits) {
            line.appendChild(statusBitElement);
        }
        if (changed) {
            for (i = 0; i < memChangedElements.length; ++i) {
                line.appendChild(memChangedElements[i]);
            }
        }
        if (custom) {
            for (i = 0; i < memCustomElements.length; ++i) {
                line.appendChild(memCustomElements[i]);
            }
        }
        if (cycleCount) {
            line.appendChild(instrCycleCount);
        }
        this.mainRootElement.appendChild(line);
    }

    public void logState(int currentLine, String code, Emulator emulator) throws Exception {
        if (!enabled) {
            return;
        }
        String lineNumberString = "" + (currentLine + 1);
        Register[] registers = emulator.getRegisters();
        Element[] regElements = new Element[16];
        for (int i = 0; i < 16; ++i) {
            regElements[i] = this.doc.createElement("register");
            regElements[i].setAttribute("name", "R" + i);
            regElements[i].appendChild(this.doc.createTextNode("0x" + Integer.toHexString(registers[i].getValue()).toUpperCase()));
        }
        String statusBitString = "0b";
        boolean[] statusBits = emulator.getStatusBits();
        for (int i = 0; i < 4; ++i) {
            statusBitString = statusBitString + (statusBits[i] ? "1" : "0");
        }
        Element statusBitElement = this.doc.createElement("NZCV");
        statusBitElement.appendChild(this.doc.createTextNode(statusBitString));
        List<MemWord> changed = emulator.memory.getChanged();
        Element[] memChangedElements = new Element[changed.size()];
        for (int i = 0; i < changed.size(); ++i) {
            memChangedElements[i] = this.doc.createElement("word");
            memChangedElements[i].setAttribute("address", "0x" + Integer.toHexString(changed.get(i).getAddress()).toUpperCase());
            memChangedElements[i].appendChild(this.doc.createTextNode("0x" + Integer.toHexString(changed.get(i).getValue()).toUpperCase()));
        }
        Element[] memCustomElements = new Element[customLocations.size()];
        for (int i = 0; i < customLocations.size(); ++i) {
            MemWord word = emulator.memory.getMemWord((int)Long.parseLong(customLocations.get(i).substring(2), 16), false, true);
            memCustomElements[i] = this.doc.createElement("word");
            memCustomElements[i].setAttribute("address", "0x" + Integer.toHexString(word.getAddress()).toUpperCase());
            memCustomElements[i].appendChild(this.doc.createTextNode("0x" + Integer.toHexString(word.getValue()).toUpperCase()));
        }
        Element instrCycleCount = this.doc.createElement("cycles");
        instrCycleCount.appendChild(this.doc.createTextNode("" + emulator.getInstrCycleCount()));
        this.addLine(lineNumberString, code, regElements, statusBitElement, memChangedElements, memCustomElements, instrCycleCount);
    }

    public void logSyntaxError(SyntaxError error) {
        if (!syntax || !enabled) {
            return;
        }
        Element errorElement = this.doc.createElement("syntax-error");
        errorElement.setAttribute("line", "" + (error.getLineNumber() + 1));
        errorElement.appendChild(this.doc.createTextNode(error.getMessage()));
        this.mainRootElement.appendChild(errorElement);
    }

    public void logRuntimeError(int currentLine, String message) {
        if (!runtime || !enabled) {
            return;
        }
        Element errorElement = this.doc.createElement("runtime-error");
        errorElement.setAttribute("line", "" + (currentLine + 1));
        errorElement.appendChild(this.doc.createTextNode(message));
        this.mainRootElement.appendChild(errorElement);
    }

    public void saveXmlToFile() {
        if (!enabled) {
            return;
        }
        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty("indent", "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            DOMSource source = new DOMSource(this.doc);
            String filePath = "";
            filePath = !directory.isEmpty() ? directory + System.getProperty("file.separator") + this.fileName : this.fileName;
            System.out.println("Saving XML log file to " + filePath);
            StreamResult outfile = new StreamResult(new File(filePath));
            transformer.transform(source, outfile);
            System.out.println("XML log file saved successfully");
        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("Save to XML file failed");
        }
    }

    public static void configureLogging(String directory, boolean enabled, boolean regs, boolean statusBits, boolean syntax, boolean runtime, boolean changed, boolean custom, boolean cycleCount, String[] customLocations) {
        EmulatorLogFile.directory = directory;
        EmulatorLogFile.enabled = enabled;
        EmulatorLogFile.regs = regs;
        EmulatorLogFile.statusBits = statusBits;
        EmulatorLogFile.syntax = syntax;
        EmulatorLogFile.runtime = runtime;
        EmulatorLogFile.changed = changed;
        EmulatorLogFile.custom = custom;
        EmulatorLogFile.cycleCount = cycleCount;
        EmulatorLogFile.customLocations.clear();
        for (int i = 0; i < customLocations.length; ++i) {
            String toAdd = customLocations[i];
            if (toAdd.isEmpty() || EmulatorLogFile.customLocations.contains(toAdd)) continue;
            EmulatorLogFile.customLocations.add(toAdd);
        }
    }

    public static void reconfigureLogging() {
        System.out.println("Reconfiguring logging...");
        directory = VisUAL.getSettingsManager().getLogFileLocation();
        enabled = VisUAL.getSettingsManager().getLoggingEnabled();
        regs = VisUAL.getSettingsManager().getLogRegisters();
        statusBits = VisUAL.getSettingsManager().getLogStatusBits();
        syntax = VisUAL.getSettingsManager().getLogSyntaxErrors();
        runtime = VisUAL.getSettingsManager().getLogRuntimeErrors();
        changed = VisUAL.getSettingsManager().getLogMemChanged();
        custom = VisUAL.getSettingsManager().getLogMemCustom();
        cycleCount = VisUAL.getSettingsManager().getLogCycleCount();
        customLocations.clear();
        for (int i = 0; i < VisUAL.getSettingsManager().getLogMemCustomLocations().length; ++i) {
            String toAdd = VisUAL.getSettingsManager().getLogMemCustomLocations()[i];
            if (toAdd.isEmpty() || customLocations.contains(toAdd)) continue;
            customLocations.add(toAdd);
        }
    }

    static {
        customLocations = new ArrayList<String>();
    }

    public static enum LogMode {
        ALL,
        COMPLETION,
        BREAKPOINT;

    }
}

