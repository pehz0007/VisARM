/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javafx.application.Platform
 *  javafx.scene.Parent
 *  javafx.scene.Scene
 *  javafx.stage.Stage
 */
package visual;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import visual.AddressType;
import visual.ArgType;
import visual.Argument;
import visual.Bases;
import visual.BranchInfo;
import visual.DcdWord;
import visual.ExecStatus;
import visual.Instruction;
import visual.LineLabel;
import visual.LoopThresholdPane;
import visual.MemWord;
import visual.MemoryBank;
import visual.MemoryInfo;
import visual.PointerInfo;
import visual.PointerType;
import visual.RegAccordion;
import visual.RegType;
import visual.Register;
import visual.RuntimeError;
import visual.ShiftInfo;
import visual.ShiftType;
import visual.StackEntry;
import visual.StackInfo;
import visual.StackType;
import visual.StatusBit;
import visual.StatusBitStyle;
import visual.StatusRegBar;
import visual.Symbol;
import visual.SyntaxScanner;
import visual.UIController;
import visual.VisUAL;

public class Emulator {
    private static UIController uiController = VisUAL.getUIController();
    private static RegAccordion regAccordion = VisUAL.getRegAccordion();
    private static StatusRegBar statusRegBar = regAccordion.getStatusRegBar();
    private Register[] registers = new Register[16];
    private boolean n = false;
    private boolean z = false;
    private boolean c = false;
    private boolean v = false;
    private boolean set = false;
    private boolean takeBranch = false;
    protected List<LineLabel> labels = new ArrayList<LineLabel>();
    protected List<Symbol> symbols = new ArrayList<Symbol>();
    protected List<DcdWord> dcdWords = new ArrayList<DcdWord>();
    protected MemoryBank memory = new MemoryBank(uiController.getMemoryWindow(), uiController.getSymbolWindow());
    private int instrCycleCount = 0;
    private boolean deferSceneUpdates = false;
    private int currentInstLine = 0;

    public Emulator() {
        this.labels = new ArrayList<LineLabel>();
        this.symbols = new ArrayList<Symbol>();
        this.dcdWords = new ArrayList<DcdWord>();
        this.registers[0] = new Register(0, RegType.R0);
        this.registers[1] = new Register(0, RegType.R1);
        this.registers[2] = new Register(0, RegType.R2);
        this.registers[3] = new Register(0, RegType.R3);
        this.registers[4] = new Register(0, RegType.R4);
        this.registers[5] = new Register(0, RegType.R5);
        this.registers[6] = new Register(0, RegType.R6);
        this.registers[7] = new Register(0, RegType.R7);
        this.registers[8] = new Register(0, RegType.R8);
        this.registers[9] = new Register(0, RegType.R9);
        this.registers[10] = new Register(0, RegType.R10);
        this.registers[11] = new Register(0, RegType.R11);
        this.registers[12] = new Register(0, RegType.R12);
        this.registers[13] = new Register(VisUAL.getSettingsManager().getStackPointerValue(), RegType.R13);
        this.registers[14] = new Register(0, RegType.R14);
        this.registers[15] = new Register(0, RegType.R15);
    }

    public void addLabel(LineLabel l) {
        this.labels.add(l);
    }

    public ExecStatus process(Instruction i, boolean deferSceneUpdates) throws Exception {
        this.deferSceneUpdates = deferSceneUpdates;
        this.memory.resetChanged();
        statusRegBar.resetBitStyle(StatusBitStyle.NORM, this.deferSceneUpdates);
        if (i.getOpcode().isEmpty()) {
            return ExecStatus.SKIP;
        }
        if (i.getOpcode().equals("END")) {
            uiController.setFinished(true);
            return ExecStatus.NORM;
        }
        this.set = SyntaxScanner.isSet(i.getOpcode());
        String conditionCode = SyntaxScanner.getConditionCode(i.getOpcode());
        String baseOpcode = SyntaxScanner.getBaseOpcodeForString(i.getOpcode());
        this.instrCycleCount = VisUAL.getCycleCounter().getCycleCount(baseOpcode);
        boolean useShiftCarry = baseOpcode.matches("(?i:(MOV|MVN|BIC|EOR|ORR|ORN|AND|ASR|LSR|LSL|ROR|RRX))");
        if (!conditionCode.isEmpty()) {
            if (this.checkConditionCode(conditionCode)) {
                if (baseOpcode.equals("B") || baseOpcode.equals("BL")) {
                    this.takeBranch = true;
                }
                this.executeIns(baseOpcode, i, false, useShiftCarry);
                return ExecStatus.TEXEC;
            }
            if (baseOpcode.equals("B") || baseOpcode.equals("BL")) {
                this.takeBranch = false;
            }
            this.instrCycleCount = VisUAL.getCycleCounter().getCycleCount("NOP");
            return ExecStatus.FEXEC;
        }
        if (baseOpcode.equals("B") || baseOpcode.equals("BL")) {
            this.takeBranch = true;
        }
        this.executeIns(baseOpcode, i, false, useShiftCarry);
        return ExecStatus.NORM;
    }

    private int executeIns(String opcode, Instruction i, boolean op2, boolean useShiftCarry) throws Exception {
        this.currentInstLine = i.getLineNumber();
        regAccordion.resetRegHighlight(this.deferSceneUpdates);
        boolean updateRegBank = true;
        int result = 0;
        int secondOperand = 0;
        boolean useSecondOperand = false;
        boolean overrideStatusFlags = false;
        if (!i.getSecondOpcode().isEmpty()) {
            ArrayList<Argument> subArgs = new ArrayList<Argument>();
            subArgs.add(i.getArgs().get(i.getArgs().size() - 2));
            subArgs.add(i.getArgs().get(i.getArgs().size() - 1));
            Instruction secondInstruction = new Instruction(i.getLineNumber(), i.getAddress(), i.getSecondOpcode(), subArgs);
            secondOperand = this.executeIns(i.getSecondOpcode(), secondInstruction, true, useShiftCarry);
            i.setSecondOperandCarry(secondInstruction.getSecondOperandCarry());
            useSecondOperand = true;
        }
        int arg1 = 0;
        int arg2 = 0;
        if (opcode.equals("B") || opcode.equals("BL")) {
            arg1 = 0;
            arg2 = 0;
        } else if (op2) {
            arg1 = this.resolveArg(i.getArgs().get(0));
            arg2 = this.resolveArg(i.getArgs().get(1));
        } else {
            if (!opcode.toLowerCase().startsWith("adr")) {
                arg1 = this.resolveArg(i.getArgs().get(1));
            }
            try {
                if (i.getArgs().size() > 2) {
                    arg2 = this.resolveArg(i.getArgs().get(2));
                }
            }
            catch (NullPointerException e) {
                uiController.getLogFile().logRuntimeError(i.getLineNumber() + 1, "Required second instruction argument not found");
                uiController.setAbort(true);
                return -1;
            }
        }
        if (useSecondOperand) {
            arg2 = secondOperand;
        }
        try {
            switch (opcode) {
                case "MOV": 
                case "mov": {
                    result = arg1;
                    if (useSecondOperand) {
                        result = secondOperand;
                        if (this.set) {
                            this.c = i.getSecondOperandCarry();
                            statusRegBar.setBitValue(StatusBit.C, this.c, this.deferSceneUpdates);
                        }
                    }
                    if (op2) break;
                    this.registers[i.getArgs().get(0).getValue()].setValue(result);
                    if (!this.set || !i.getSecondOpcode().isEmpty() || i.getArgs().get(i.getArgs().size() - 1).getType() == ArgType.REG || Integer.compareUnsigned(arg2, 255) != 1 || !SyntaxScanner.isShiftable(arg2, SyntaxScanner.SubstitutionType.INVERT)) break;
                    this.c = this.isNeg(arg1) || this.isNeg(~arg2);
                    break;
                }
                case "MVN": 
                case "mvn": {
                    result = ~arg1;
                    if (useSecondOperand) {
                        result = ~secondOperand;
                        if (this.set) {
                            this.c = i.getSecondOperandCarry();
                        }
                        statusRegBar.setBitValue(StatusBit.C, this.c, this.deferSceneUpdates);
                    }
                    if (op2) break;
                    this.registers[i.getArgs().get(0).getValue()].setValue(result);
                    if (!this.set || !i.getSecondOpcode().isEmpty() || i.getArgs().get(i.getArgs().size() - 1).getType() == ArgType.REG || Integer.compareUnsigned(arg2, 255) != 1 || !SyntaxScanner.isShiftable(arg2, SyntaxScanner.SubstitutionType.INVERT)) break;
                    this.c = this.isNeg(arg1) || this.isNeg(~arg2);
                    break;
                }
                case "ADR": 
                case "adr": {
                    result = SyntaxScanner.solveExpression(this.currentInstLine, i.getArgs().get(i.getArgs().size() - 1).getStrValue(), this.symbols, this.dcdWords, this.labels);
                    if (op2) break;
                    int offset = Math.abs(result - (this.registers[15].getValue() + 8));
                    if (!SyntaxScanner.isShiftable(offset, SyntaxScanner.SubstitutionType.NONE)) {
                        throw new RuntimeError(i.getLineNumber(), "Offset out of range for address load.", "Offset from current PC value should be creatable by rotating an 8-bit byte right within a 32-bit word.");
                    }
                    this.registers[i.getArgs().get(0).getValue()].setValue(result);
                    regAccordion.setRegValue(i.getArgs().get(0).getValue(), result, true, this.deferSceneUpdates, false);
                    break;
                }
                case "LDR": 
                case "ldr": {
                    result = this.ldr(arg1, arg2, i);
                    if (op2) break;
                    if (i.getArgs().get(0).getValue() == 15) {
                        this.instrCycleCount += VisUAL.getCycleCounter().getCycleCount("B") - 1;
                    }
                    this.registers[i.getArgs().get(0).getValue()].setValue(result);
                    break;
                }
                case "LDRB": 
                case "ldrb": {
                    result = this.ldrb(arg1, arg2, i);
                    this.registers[i.getArgs().get(0).getValue()].setValue(result);
                    break;
                }
                case "STR": 
                case "str": {
                    updateRegBank = false;
                    this.str(this.resolveArg(i.getArgs().get(0)), arg1, arg2, i);
                    break;
                }
                case "STRB": 
                case "strb": {
                    updateRegBank = false;
                    this.strb(this.resolveArg(i.getArgs().get(0)), arg1, arg2, i);
                    break;
                }
                case "STM": 
                case "stm": {
                    updateRegBank = false;
                    this.stm(this.resolveArg(i.getArgs().get(0)), i);
                    break;
                }
                case "LDM": 
                case "ldm": {
                    updateRegBank = false;
                    this.ldm(this.resolveArg(i.getArgs().get(0)), i);
                    break;
                }
                case "ADD": 
                case "add": {
                    result = this.add(arg1, arg2);
                    if (op2) break;
                    this.registers[i.getArgs().get(0).getValue()].setValue(result);
                    break;
                }
                case "ADC": 
                case "adc": {
                    result = this.adc(arg1, arg2);
                    if (op2) break;
                    this.registers[i.getArgs().get(0).getValue()].setValue(result);
                    break;
                }
                case "SUB": 
                case "sub": {
                    result = this.sub(arg1, arg2);
                    if (op2) break;
                    this.registers[i.getArgs().get(0).getValue()].setValue(result);
                    break;
                }
                case "SBC": 
                case "sbc": {
                    result = this.sbc(arg1, arg2);
                    if (op2) break;
                    this.registers[i.getArgs().get(0).getValue()].setValue(result);
                    break;
                }
                case "RSB": 
                case "rsb": {
                    result = this.rsb(arg1, arg2);
                    if (op2) break;
                    this.registers[i.getArgs().get(0).getValue()].setValue(result);
                    break;
                }
                case "RSC": 
                case "rsc": {
                    result = this.rsc(arg1, arg2);
                    if (op2) break;
                    this.registers[i.getArgs().get(0).getValue()].setValue(result);
                    break;
                }
                case "ASR": 
                case "asr": {
                    if (i.getArgs().get(i.getArgs().size() - 1).getType().equals((Object)ArgType.REG)) {
                        arg2 &= 0xFF;
                    }
                    result = this.asr(arg1, arg2, regAccordion.getDispBase(i.getArgs().get(0).getValue()), useShiftCarry, i);
                    if (op2) break;
                    this.registers[i.getArgs().get(0).getValue()].setValue(result);
                    break;
                }
                case "LSR": 
                case "lsr": {
                    if (i.getArgs().get(i.getArgs().size() - 1).getType().equals((Object)ArgType.REG)) {
                        arg2 &= 0xFF;
                    }
                    result = this.lsr(arg1, arg2, regAccordion.getDispBase(i.getArgs().get(0).getValue()), useShiftCarry, i);
                    if (op2) break;
                    this.registers[i.getArgs().get(0).getValue()].setValue(result);
                    break;
                }
                case "LSL": 
                case "lsl": {
                    if (i.getArgs().get(i.getArgs().size() - 1).getType().equals((Object)ArgType.REG)) {
                        arg2 &= 0xFF;
                    }
                    result = this.lsl(arg1, arg2, regAccordion.getDispBase(i.getArgs().get(0).getValue()), useShiftCarry, i);
                    if (op2) break;
                    this.registers[i.getArgs().get(0).getValue()].setValue(result);
                    break;
                }
                case "ROR": 
                case "ror": {
                    if (i.getArgs().get(i.getArgs().size() - 1).getType().equals((Object)ArgType.REG)) {
                        arg2 &= 0xFF;
                    }
                    result = this.ror(arg1, arg2, regAccordion.getDispBase(i.getArgs().get(0).getValue()), useShiftCarry, i);
                    if (op2) break;
                    this.registers[i.getArgs().get(0).getValue()].setValue(result);
                    break;
                }
                case "RRX": 
                case "rrx": {
                    if (!op2) {
                        result = this.rrx(arg1, regAccordion.getDispBase(i.getArgs().get(0).getValue()), useShiftCarry, i);
                        this.registers[i.getArgs().get(0).getValue()].setValue(result);
                        break;
                    }
                    result = this.rrx(arg2, regAccordion.getDispBase(i.getArgs().get(1).getValue()), useShiftCarry, i);
                    break;
                }
                case "AND": 
                case "and": {
                    result = this.and(arg1, arg2);
                    if (!op2) {
                        this.registers[i.getArgs().get(0).getValue()].setValue(result);
                        if (this.set && i.getSecondOpcode().isEmpty() && i.getArgs().get(i.getArgs().size() - 1).getType() != ArgType.REG && Integer.compareUnsigned(arg2, 255) == 1 && SyntaxScanner.isShiftable(arg2, SyntaxScanner.SubstitutionType.INVERT)) {
                            boolean bl = this.c = this.isNeg(arg2) || this.isNeg(~arg2);
                        }
                    }
                    if (!this.set || !useSecondOperand) break;
                    this.c = i.getSecondOperandCarry();
                    break;
                }
                case "ORR": 
                case "orr": {
                    result = this.orr(arg1, arg2);
                    if (!op2) {
                        this.registers[i.getArgs().get(0).getValue()].setValue(result);
                        if (this.set && i.getSecondOpcode().isEmpty() && i.getArgs().get(i.getArgs().size() - 1).getType() != ArgType.REG && Integer.compareUnsigned(arg2, 255) == 1 && SyntaxScanner.isShiftable(arg2, SyntaxScanner.SubstitutionType.NONE)) {
                            this.c = this.isNeg(arg2);
                        }
                    }
                    if (!this.set || !useSecondOperand) break;
                    this.c = i.getSecondOperandCarry();
                    break;
                }
                case "EOR": 
                case "eor": {
                    result = this.eor(arg1, arg2);
                    if (!op2) {
                        this.registers[i.getArgs().get(0).getValue()].setValue(result);
                        if (this.set && i.getSecondOpcode().isEmpty() && i.getArgs().get(i.getArgs().size() - 1).getType() != ArgType.REG && Integer.compareUnsigned(arg2, 255) == 1 && SyntaxScanner.isShiftable(arg2, SyntaxScanner.SubstitutionType.NONE)) {
                            boolean bl = this.c = this.isNeg(arg2) || this.isNeg(~arg2);
                        }
                    }
                    if (!this.set || !useSecondOperand) break;
                    this.c = i.getSecondOperandCarry();
                    break;
                }
                case "ORN": 
                case "orn": {
                    result = this.orn(arg1, arg2);
                    if (op2) break;
                    this.registers[i.getArgs().get(0).getValue()].setValue(result);
                    break;
                }
                case "BIC": 
                case "bic": {
                    result = this.bic(arg1, arg2);
                    if (!op2) {
                        this.registers[i.getArgs().get(0).getValue()].setValue(result);
                        if (this.set && i.getSecondOpcode().isEmpty() && i.getArgs().get(i.getArgs().size() - 1).getType() != ArgType.REG && Integer.compareUnsigned(arg2, 255) == 1 && SyntaxScanner.isShiftable(arg2, SyntaxScanner.SubstitutionType.INVERT)) {
                            boolean bl = this.c = this.isNeg(arg2) || this.isNeg(~arg2);
                        }
                    }
                    if (!this.set || !useSecondOperand) break;
                    this.c = i.getSecondOperandCarry();
                    break;
                }
                case "TST": 
                case "tst": {
                    overrideStatusFlags = true;
                    if (useSecondOperand) {
                        this.tst(this.resolveArg(i.getArgs().get(0)), arg2);
                        this.c = i.getSecondOperandCarry();
                    } else {
                        this.tst(this.resolveArg(i.getArgs().get(0)), this.resolveArg(i.getArgs().get(1)));
                    }
                    if (!i.getSecondOpcode().isEmpty() || i.getArgs().get(i.getArgs().size() - 1).getType() == ArgType.REG) break;
                    int value = this.resolveArg(i.getArgs().get(1));
                    if (value > 255 && SyntaxScanner.isShiftable(value, SyntaxScanner.SubstitutionType.NONE)) {
                        this.c = this.isNeg(value);
                    }
                    break;
                }
                case "TEQ": 
                case "teq": {
                    overrideStatusFlags = true;
                    if (useSecondOperand) {
                        this.teq(this.resolveArg(i.getArgs().get(0)), arg2);
                        this.c = i.getSecondOperandCarry();
                    } else {
                        this.teq(this.resolveArg(i.getArgs().get(0)), this.resolveArg(i.getArgs().get(1)));
                    }
                    if (!i.getSecondOpcode().isEmpty() || i.getArgs().get(i.getArgs().size() - 1).getType() == ArgType.REG) break;
                    int value = this.resolveArg(i.getArgs().get(1));
                    if (value > 255 && SyntaxScanner.isShiftable(value, SyntaxScanner.SubstitutionType.NONE)) {
                        this.c = this.isNeg(value);
                    }
                    break;
                }
                case "CMP": 
                case "cmp": {
                    updateRegBank = false;
                    overrideStatusFlags = true;
                    if (useSecondOperand) {
                        this.cmp(this.resolveArg(i.getArgs().get(0)), arg2);
                        break;
                    }
                    this.cmp(this.resolveArg(i.getArgs().get(0)), this.resolveArg(i.getArgs().get(1)));
                    break;
                }
                case "CMN": 
                case "cmn": {
                    updateRegBank = false;
                    overrideStatusFlags = true;
                    if (useSecondOperand) {
                        this.cmn(this.resolveArg(i.getArgs().get(0)), arg2);
                        break;
                    }
                    this.cmn(this.resolveArg(i.getArgs().get(0)), this.resolveArg(i.getArgs().get(1)));
                    break;
                }
                case "B": 
                case "b": {
                    updateRegBank = false;
                    if (i.getArgs().get(0).getType() == ArgType.EXP) {
                        this.takeBranch = true;
                        int destAddress = SyntaxScanner.solveExpression(this.currentInstLine, i.getArgs().get(0).getStrValue(), this.symbols, this.dcdWords, this.labels);
                        this.branchToInstruction(i, uiController.getInstructionForAddress(i.getLineNumber(), destAddress), false);
                        break;
                    }
                    if (i.getArgs().get(0).getType() != ArgType.LIT) break;
                    this.takeBranch = true;
                    int lrLineAddress = this.resolveArg(i.getArgs().get(0)) & 0xFFFFFFFE;
                    this.branchToInstruction(i, uiController.getInstructionForAddress(i.getLineNumber(), lrLineAddress), false);
                    break;
                }
                case "BL": 
                case "bl": {
                    updateRegBank = false;
                    if (i.getArgs().get(0).getType() != ArgType.EXP) break;
                    this.takeBranch = true;
                    int destAddress = SyntaxScanner.solveExpression(this.currentInstLine, i.getArgs().get(0).getStrValue(), this.symbols, this.dcdWords, this.labels);
                    this.branchToInstruction(i, uiController.getInstructionForAddress(i.getLineNumber(), destAddress), true);
                    break;
                }
            }
        }
        catch (Register.WriteToProgramCounterException e) {
            ArrayList<Argument> argList = new ArrayList<Argument>();
            argList.add(new Argument(ArgType.LIT, e.getBranchToAddress()));
            Instruction substituteInstruction = new Instruction(i.getLineNumber(), i.getAddress(), "B", argList);
            substituteInstruction.setSubstituteCycleCount(this.instrCycleCount);
            this.executeIns("B", substituteInstruction, false, useShiftCarry);
            this.instrCycleCount = SyntaxScanner.getBaseOpcodeForString(i.getOpcode()).toUpperCase().matches("LDR|LDM") ? substituteInstruction.getSubstituteCycleCount().intValue() : VisUAL.getCycleCounter().getCycleCount("B");
        }
        if (this.set && !overrideStatusFlags) {
            this.z = result == 0;
            this.n = result < 0;
            statusRegBar.setBitValue(StatusBit.Z, this.z, this.deferSceneUpdates);
            statusRegBar.setBitValue(StatusBit.N, this.n, this.deferSceneUpdates);
        }
        if (!op2 && updateRegBank) {
            regAccordion.setRegValue(i.getArgs().get(0).getValue(), result, true, this.deferSceneUpdates, false);
        }
        return result;
    }

    private void throwUndefBranchDestError(int lineNumber) throws Exception {
        Platform.runLater(() -> VisUAL.addRuntimeError(lineNumber, "Branch destination undefined.", "The branch destination specified does not exist. Check if the label is correct. Note that you cannot branch to constant or word names."));
        uiController.getLogFile().logRuntimeError(lineNumber + 1, "Branch destination undefined!");
        uiController.setAbort(true);
    }

    private void throwLoopCountThresholdError(Instruction inst, int branchTo, int branchFrom) {
        Platform.runLater(() -> {
            Stage ltpStage = new Stage();
            LoopThresholdPane ltp = new LoopThresholdPane(ltpStage, inst, branchTo, branchFrom);
            ltpStage.setScene(new Scene((Parent)ltp));
            ltpStage.show();
            ltpStage.setResizable(false);
            uiController.setLoopThresholdStage(ltpStage);
        });
        uiController.setPaused(true);
    }

    private void branchToInstruction(Instruction sourceInst, Instruction destInst, boolean updateLinkRegister) throws Exception {
        ExecStatus status = ExecStatus.FEXEC;
        int[] arrowLines = new int[]{sourceInst.getLineNumber(), destInst.getLineNumber()};
        VisUAL.getBranchArrowCanvas().setArrowLines(arrowLines, false);
        if (this.takeBranch) {
            if (Integer.compareUnsigned(Math.abs(arrowLines[0] - arrowLines[1]) * 4, 8000000) > 0) {
                throw new Exception("Branch destination out of range.");
            }
            status = ExecStatus.TEXEC;
            this.detectSelfBranch(arrowLines[0], arrowLines[1]);
            destInst.incrementBranchToCount();
            uiController.setBranchToLine(destInst.getLineNumber());
            if (this.checkLoopCount(destInst)) {
                this.throwLoopCountThresholdError(destInst, arrowLines[1], arrowLines[0]);
            }
        }
        VisUAL.getBranchArrowCanvas().setColour(uiController.calcHighlightColour(status), false);
        double arrowCanvasHeight = VisUAL.getBranchArrowCanvas().widthProperty().doubleValue() * (double)(Math.abs(arrowLines[0] - arrowLines[1]) + 1);
        if (arrowCanvasHeight * VisUAL.getBranchArrowCanvas().widthProperty().doubleValue() < 16384.0) {
            VisUAL.getBranchArrowCanvas().heightProperty().set(VisUAL.getBranchArrowCanvas().widthProperty().doubleValue() * (double)(Math.abs(arrowLines[0] - arrowLines[1]) + 1));
            VisUAL.getBranchArrowCanvas().setHidden(false, !this.deferSceneUpdates);
            if (arrowLines[0] > arrowLines[1]) {
                VisUAL.getBranchArrowCanvas().setLineNumber(arrowLines[1], this.deferSceneUpdates);
            } else {
                VisUAL.getBranchArrowCanvas().setLineNumber(arrowLines[0], this.deferSceneUpdates);
            }
        } else {
            VisUAL.getBranchArrowCanvas().setHidden(true, !this.deferSceneUpdates);
        }
        VisUAL.getBranchDestCanvas().setHidden(false, false);
        VisUAL.getBranchDestCanvas().setColour(VisUAL.getCurrentTheme().getDestHighColour(), !this.deferSceneUpdates);
        VisUAL.getBranchDestCanvas().setLineNumber(destInst.getLineNumber(), this.deferSceneUpdates);
        if (updateLinkRegister) {
            Instruction nextInstruction = uiController.getLinkedInstruction(sourceInst.getLineNumber());
            int lrLineNumber = nextInstruction.getLineNumber();
            int lrValue = nextInstruction.getAddress();
            this.registers[14].setValue(lrValue);
            regAccordion.setRegValue(14, lrValue, true, this.deferSceneUpdates, false);
            VisUAL.getLinkRegisterCanvas().setLineNumber(lrLineNumber, this.deferSceneUpdates);
            VisUAL.getLinkRegisterCanvas().setHidden(false, !this.deferSceneUpdates);
            VisUAL.addLinkRegisterIcon(lrLineNumber);
        }
        BranchInfo branchInfo = new BranchInfo(sourceInst.getLineNumber(), SyntaxScanner.getConditionCode(sourceInst.getOpcode()), sourceInst.getAddress(), destInst.getAddress(), destInst.getLineNumber());
        uiController.setBranchButton(branchInfo);
    }

    private boolean detectSelfBranch(int branchTo, int branchFrom) throws Exception {
        boolean isSelfBranching;
        boolean bl = isSelfBranching = branchTo == branchFrom;
        if (isSelfBranching) {
            int lineNumber = this.currentInstLine;
            Platform.runLater(() -> VisUAL.addRuntimeError(lineNumber, "Self-branching instruction detected.", "This instruction is branching to itself. This will result in an infinite loop."));
            uiController.getLogFile().logRuntimeError(lineNumber + 1, "Self-branching instruction detected!");
            uiController.setAbort(true);
        }
        return isSelfBranching;
    }

    private boolean checkLoopCount(Instruction i) {
        return i.getBranchToCount(false) == VisUAL.getSettingsManager().getLoopThreshold() && !i.isIgnoreThreshold();
    }

    private int resolveArg(Argument a) throws RuntimeError {
        Integer result = null;
        if (a.getType() == ArgType.LIT) {
            result = a.getValue();
        } else if (a.getType() == ArgType.REG || a.getType() == ArgType.REG_POS) {
            result = this.registers[a.getValue()].getValue();
        } else if (a.getType() == ArgType.REG_NEG) {
            result = -this.registers[a.getValue()].getValue();
        } else if (a.getType() == ArgType.LBL || a.getType() == ArgType.MEM) {
            try {
                result = SyntaxScanner.literalToInt(this.currentInstLine, a.getStrValue(), this.symbols, this.dcdWords, this.labels);
            }
            catch (NullPointerException e) {
                throw new RuntimeError(this.currentInstLine, "Undefined pointer or label '" + a.getStrValue() + "' referenced", "Please check that the label has been defined.");
            }
        } else if (a.getType() == ArgType.EXP) {
            boolean shiftable;
            result = SyntaxScanner.solveExpression(this.currentInstLine, a.getStrValue(), this.symbols, this.dcdWords, this.labels);
            if (a.getSubType() != null && a.getSubType() != SyntaxScanner.SubstitutionType.IGNORE && !(shiftable = SyntaxScanner.isShiftable(result, a.getSubType()))) {
                throw new RuntimeError(this.currentInstLine, "Invalid immediate operand value.", "Immediate must be creatable by rotating an 8-bit number right within a 32-bit word.");
            }
            if (Integer.compareUnsigned(result, a.getMaxValue()) > 0) {
                throw new RuntimeError(this.currentInstLine, "Immediate operand value out of range.", "Maximum immediate operand value for this instruction is 0x" + Integer.toHexString(a.getMaxValue()).toUpperCase());
            }
        }
        return result;
    }

    public void addSymbol(Symbol s) {
        this.symbols.add(s);
    }

    public LineLabel.LineLabelConflict noLabelConflict(String name, int lineNumber) {
        for (Symbol s : this.symbols) {
            if (s == null || !s.getName().equals(name)) continue;
            if (s.getLineNumber() != lineNumber) {
                return LineLabel.LineLabelConflict.CONSTANT;
            }
            return LineLabel.LineLabelConflict.SCANNED;
        }
        for (DcdWord d : this.dcdWords) {
            if (d == null || !d.getName().equals(name)) continue;
            if (d.getLineNumber() != lineNumber) {
                return LineLabel.LineLabelConflict.DIRECTIVE;
            }
            return LineLabel.LineLabelConflict.SCANNED;
        }
        for (LineLabel l : this.labels) {
            if (l == null || !l.getId().equals(name)) continue;
            return LineLabel.LineLabelConflict.LABEL;
        }
        return LineLabel.LineLabelConflict.NONE;
    }

    public void addDcdWord(DcdWord w) {
        this.dcdWords.add(w);
    }

    public int getInstrCycleCount() {
        return this.instrCycleCount;
    }

    private boolean checkConditionCode(String cc) {
        switch (cc) {
            case "EQ": {
                if (this.z) {
                    statusRegBar.setBitStyle(StatusBit.Z, StatusBitStyle.TRUE, this.deferSceneUpdates);
                } else {
                    statusRegBar.setBitStyle(StatusBit.Z, StatusBitStyle.FALSE, this.deferSceneUpdates);
                }
                return this.z;
            }
            case "NE": {
                if (!this.z) {
                    statusRegBar.setBitStyle(StatusBit.Z, StatusBitStyle.TRUE, this.deferSceneUpdates);
                } else {
                    statusRegBar.setBitStyle(StatusBit.Z, StatusBitStyle.FALSE, this.deferSceneUpdates);
                }
                return !this.z;
            }
            case "CS": 
            case "HS": {
                if (this.c) {
                    statusRegBar.setBitStyle(StatusBit.C, StatusBitStyle.TRUE, this.deferSceneUpdates);
                } else {
                    statusRegBar.setBitStyle(StatusBit.C, StatusBitStyle.FALSE, this.deferSceneUpdates);
                }
                return this.c;
            }
            case "CC": 
            case "LO": {
                if (!this.c) {
                    statusRegBar.setBitStyle(StatusBit.C, StatusBitStyle.TRUE, this.deferSceneUpdates);
                } else {
                    statusRegBar.setBitStyle(StatusBit.C, StatusBitStyle.FALSE, this.deferSceneUpdates);
                }
                return !this.c;
            }
            case "MI": {
                if (this.n) {
                    statusRegBar.setBitStyle(StatusBit.N, StatusBitStyle.TRUE, this.deferSceneUpdates);
                } else {
                    statusRegBar.setBitStyle(StatusBit.N, StatusBitStyle.FALSE, this.deferSceneUpdates);
                }
                return this.n;
            }
            case "PL": {
                if (!this.n) {
                    statusRegBar.setBitStyle(StatusBit.N, StatusBitStyle.TRUE, this.deferSceneUpdates);
                } else {
                    statusRegBar.setBitStyle(StatusBit.N, StatusBitStyle.FALSE, this.deferSceneUpdates);
                }
                return !this.n;
            }
            case "VS": {
                if (this.v) {
                    statusRegBar.setBitStyle(StatusBit.V, StatusBitStyle.TRUE, this.deferSceneUpdates);
                } else {
                    statusRegBar.setBitStyle(StatusBit.V, StatusBitStyle.FALSE, this.deferSceneUpdates);
                }
                return this.v;
            }
            case "VC": {
                if (!this.v) {
                    statusRegBar.setBitStyle(StatusBit.V, StatusBitStyle.TRUE, this.deferSceneUpdates);
                } else {
                    statusRegBar.setBitStyle(StatusBit.V, StatusBitStyle.FALSE, this.deferSceneUpdates);
                }
                return !this.v;
            }
            case "HI": {
                if (!this.z) {
                    statusRegBar.setBitStyle(StatusBit.Z, StatusBitStyle.TRUE, this.deferSceneUpdates);
                } else {
                    statusRegBar.setBitStyle(StatusBit.Z, StatusBitStyle.FALSE, this.deferSceneUpdates);
                }
                if (this.c) {
                    statusRegBar.setBitStyle(StatusBit.C, StatusBitStyle.TRUE, this.deferSceneUpdates);
                } else {
                    statusRegBar.setBitStyle(StatusBit.C, StatusBitStyle.FALSE, this.deferSceneUpdates);
                }
                return this.c && !this.z;
            }
            case "LS": {
                if (this.z) {
                    statusRegBar.setBitStyle(StatusBit.Z, StatusBitStyle.TRUE, this.deferSceneUpdates);
                } else {
                    statusRegBar.setBitStyle(StatusBit.Z, StatusBitStyle.FALSE, this.deferSceneUpdates);
                }
                if (!this.c) {
                    statusRegBar.setBitStyle(StatusBit.C, StatusBitStyle.TRUE, this.deferSceneUpdates);
                } else {
                    statusRegBar.setBitStyle(StatusBit.C, StatusBitStyle.FALSE, this.deferSceneUpdates);
                }
                return !this.c || this.z;
            }
            case "GE": {
                if (this.n == this.v) {
                    statusRegBar.setBitStyle(StatusBit.N, StatusBitStyle.TRUE, this.deferSceneUpdates);
                    statusRegBar.setBitStyle(StatusBit.V, StatusBitStyle.TRUE, this.deferSceneUpdates);
                } else {
                    statusRegBar.setBitStyle(StatusBit.N, StatusBitStyle.FALSE, this.deferSceneUpdates);
                    statusRegBar.setBitStyle(StatusBit.V, StatusBitStyle.FALSE, this.deferSceneUpdates);
                }
                return this.n == this.v;
            }
            case "LT": {
                if (this.n != this.v) {
                    statusRegBar.setBitStyle(StatusBit.N, StatusBitStyle.TRUE, this.deferSceneUpdates);
                    statusRegBar.setBitStyle(StatusBit.V, StatusBitStyle.TRUE, this.deferSceneUpdates);
                } else {
                    statusRegBar.setBitStyle(StatusBit.N, StatusBitStyle.FALSE, this.deferSceneUpdates);
                    statusRegBar.setBitStyle(StatusBit.V, StatusBitStyle.FALSE, this.deferSceneUpdates);
                }
                return this.n != this.v;
            }
            case "GT": {
                if (!this.z) {
                    statusRegBar.setBitStyle(StatusBit.Z, StatusBitStyle.TRUE, this.deferSceneUpdates);
                } else {
                    statusRegBar.setBitStyle(StatusBit.Z, StatusBitStyle.FALSE, this.deferSceneUpdates);
                }
                if (this.n == this.v) {
                    statusRegBar.setBitStyle(StatusBit.N, StatusBitStyle.TRUE, this.deferSceneUpdates);
                    statusRegBar.setBitStyle(StatusBit.V, StatusBitStyle.TRUE, this.deferSceneUpdates);
                } else {
                    statusRegBar.setBitStyle(StatusBit.N, StatusBitStyle.FALSE, this.deferSceneUpdates);
                    statusRegBar.setBitStyle(StatusBit.V, StatusBitStyle.FALSE, this.deferSceneUpdates);
                }
                return !this.z && this.n == this.v;
            }
            case "LE": {
                if (this.z) {
                    statusRegBar.setBitStyle(StatusBit.Z, StatusBitStyle.TRUE, this.deferSceneUpdates);
                } else {
                    statusRegBar.setBitStyle(StatusBit.Z, StatusBitStyle.FALSE, this.deferSceneUpdates);
                }
                if (this.n != this.v) {
                    statusRegBar.setBitStyle(StatusBit.N, StatusBitStyle.TRUE, this.deferSceneUpdates);
                    statusRegBar.setBitStyle(StatusBit.V, StatusBitStyle.TRUE, this.deferSceneUpdates);
                } else {
                    statusRegBar.setBitStyle(StatusBit.N, StatusBitStyle.FALSE, this.deferSceneUpdates);
                    statusRegBar.setBitStyle(StatusBit.V, StatusBitStyle.FALSE, this.deferSceneUpdates);
                }
                return this.z || this.n != this.v;
            }
            case "AL": {
                return true;
            }
        }
        return false;
    }

    private boolean isNeg(int a) {
        return Integer.toBinaryString(a).charAt(0) == '1' && Integer.toBinaryString(a).length() == 32;
    }

    private boolean isPos(int a) {
        return !this.isNeg(a);
    }

    private boolean hasOverflowAdd(int a, int b, int result) {
        return this.isNeg(a) == this.isNeg(b) && this.isNeg(a) != this.isNeg(result);
    }

    private boolean hasOverflowSub(int a, int b, int result) {
        return this.isNeg(a) && this.isPos(b) && this.isPos(result) || this.isPos(a) && this.isNeg(b) && this.isNeg(result);
    }

    private boolean hasCarryAdd(int a, int b, int result) {
        return this.isNeg(a) && this.isNeg(b) || this.isNeg(a) && this.isPos(result) || this.isNeg(b) && this.isPos(result);
    }

    private boolean hasCarrySub(int a, int b, int result) {
        return this.isNeg(a) && this.isPos(b) || this.isNeg(a) && this.isPos(result) || this.isPos(b) && this.isPos(result);
    }

    private int add(int a, int b) {
        int res = a + b;
        if (this.set) {
            this.v = this.hasOverflowAdd(a, b, res);
            statusRegBar.setBitValue(StatusBit.V, this.v, this.deferSceneUpdates);
        }
        if (this.set) {
            this.c = this.hasCarryAdd(a, b, res);
            statusRegBar.setBitValue(StatusBit.C, this.c, this.deferSceneUpdates);
        }
        return res;
    }

    private int adc(int a, int b) {
        int res = this.c ? a + b + 1 : a + b;
        if (this.set) {
            this.v = this.hasOverflowAdd(a, b, res);
            statusRegBar.setBitValue(StatusBit.V, this.v, this.deferSceneUpdates);
        }
        if (this.set) {
            this.c = this.hasCarryAdd(a, b, res);
            statusRegBar.setBitValue(StatusBit.C, this.c, this.deferSceneUpdates);
        }
        return res;
    }

    private int sub(int a, int b) {
        int res = a - b;
        if (this.set) {
            this.v = this.hasOverflowSub(a, b, res);
            statusRegBar.setBitValue(StatusBit.V, this.v, this.deferSceneUpdates);
        }
        if (this.set) {
            this.c = this.hasCarrySub(a, b, res);
            statusRegBar.setBitValue(StatusBit.C, this.c, this.deferSceneUpdates);
        }
        return res;
    }

    private int sbc(int a, int b) {
        int res = !this.c ? a - b - 1 : a - b;
        if (this.set) {
            this.v = this.hasOverflowSub(a, b, res);
            statusRegBar.setBitValue(StatusBit.V, this.v, this.deferSceneUpdates);
        }
        if (this.set) {
            this.c = this.hasCarrySub(a, b, res);
            statusRegBar.setBitValue(StatusBit.C, this.c, this.deferSceneUpdates);
        }
        return res;
    }

    private int rsb(int a, int b) {
        int res = b - a;
        if (this.set) {
            this.v = this.hasOverflowSub(b, a, res);
            statusRegBar.setBitValue(StatusBit.V, this.v, this.deferSceneUpdates);
        }
        if (this.set) {
            this.c = this.hasCarrySub(b, a, res);
            statusRegBar.setBitValue(StatusBit.C, this.c, this.deferSceneUpdates);
        }
        return res;
    }

    private int rsc(int a, int b) {
        int res = !this.c ? b - a - 1 : b - a;
        if (this.set) {
            this.v = this.hasOverflowSub(b, a, res);
            statusRegBar.setBitValue(StatusBit.V, this.v, this.deferSceneUpdates);
        }
        if (this.set) {
            this.c = this.hasCarrySub(b, a, res);
            statusRegBar.setBitValue(StatusBit.C, this.c, this.deferSceneUpdates);
        }
        return res;
    }

    private int asr(int a, int n, Bases dispFormat, boolean useShiftCarry, Instruction i) {
        boolean carryValue = false;
        int res;
        if (n <= 31) {
            res = a >> n;
            ArrayList<String> values = new ArrayList<String>();
            ArrayList<String> carrys = new ArrayList<String>();
            for (int j = 0; j <= n; ++j) {
                boolean carryValue2 = false;
                String value = Integer.toBinaryString(a >> j);
                values.add(("00000000000000000000000000000000".substring(value.length()) + value).replace("", " ").trim());
                int shiftLess = a >> j - 1;
                boolean bl = j > 0 ? (j <= 31 ? Integer.toBinaryString(shiftLess).endsWith("1") : this.isNeg(a)) : (carryValue2 = this.c);
                if (this.set && j != 0 && useShiftCarry) {
                    this.c = carryValue2;
                }
                carrys.add(this.c ? "1" : "0");
            }
            ShiftInfo info = new ShiftInfo(values, ShiftType.ASR, n, carrys, useShiftCarry, dispFormat, this.currentInstLine);
            uiController.setShiftButton(info);
        } else {
            res = 0;
        }
        int shiftLess = a >>> n - 1;
        boolean bl = n > 0 ? n < 33 && Integer.toBinaryString(shiftLess).endsWith("1") : (carryValue = this.c);
        if (this.set && n != 0 && useShiftCarry) {
            this.c = carryValue;
        }
        statusRegBar.setBitValue(StatusBit.C, this.c, this.deferSceneUpdates);
        i.setSecondOperandCarry(carryValue);
        return res;
    }

    private int lsr(int a, int n, Bases dispFormat, boolean useShiftCarry, Instruction i) {
        boolean carryValue = false;
        int res;
        if (n <= 31) {
            res = a >>> n;
            ArrayList<String> values = new ArrayList<String>();
            ArrayList<String> carrys = new ArrayList<String>();
            for (int j = 0; j <= n; ++j) {
                boolean carryValue2 = false;
                String value = Integer.toBinaryString(a >>> j);
                values.add(("00000000000000000000000000000000".substring(value.length()) + value).replace("", " ").trim());
                int shiftLess = a >>> j - 1;
                boolean bl = j > 0 ? j < 33 && Integer.toBinaryString(shiftLess).endsWith("1") : (carryValue2 = this.c);
                if (this.set && j != 0 && useShiftCarry) {
                    this.c = carryValue2;
                }
                carrys.add(this.c ? "1" : "0");
            }
            ShiftInfo info = new ShiftInfo(values, ShiftType.LSR, n, carrys, useShiftCarry, dispFormat, this.currentInstLine);
            uiController.setShiftButton(info);
        } else {
            res = 0;
        }
        int shiftLess = a >>> n - 1;
        boolean bl = n > 0 ? n < 33 && Integer.toBinaryString(shiftLess).endsWith("1") : (carryValue = this.c);
        if (this.set && n != 0 && useShiftCarry) {
            this.c = carryValue;
        }
        statusRegBar.setBitValue(StatusBit.C, this.c, this.deferSceneUpdates);
        i.setSecondOperandCarry(carryValue);
        return res;
    }

    private int lsl(int a, int n, Bases dispFormat, boolean useShiftCarry, Instruction i) {
        boolean carryValue = false;
        int res;
        if (n <= 31) {
            res = a << n;
            ArrayList<String> values = new ArrayList<String>();
            ArrayList<String> carrys = new ArrayList<String>();
            for (int j = 0; j <= n; ++j) {
                boolean carryValue2 = false;
                String value = Integer.toBinaryString(a << j);
                values.add(("00000000000000000000000000000000".substring(value.length()) + value).replace("", " ").trim());
                int shiftLess = a << j - 1;
                boolean bl = j > 0 ? j < 33 && this.isNeg(shiftLess) : (carryValue2 = this.c);
                if (this.set && j != 0 && useShiftCarry) {
                    this.c = carryValue2;
                }
                carrys.add(this.c ? "1" : "0");
            }
            ShiftInfo info = new ShiftInfo(values, ShiftType.LSL, n, carrys, useShiftCarry, dispFormat, this.currentInstLine);
            uiController.setShiftButton(info);
        } else {
            res = 0;
        }
        int shiftLess = a << n - 1;
        boolean bl = n > 0 ? n < 33 && this.isNeg(shiftLess) : (carryValue = this.c);
        if (this.set && n != 0 && useShiftCarry) {
            this.c = carryValue;
        }
        statusRegBar.setBitValue(StatusBit.C, this.c, this.deferSceneUpdates);
        i.setSecondOperandCarry(carryValue);
        return res;
    }

    private int ror(int a, int n, Bases dispFormat, boolean useShiftCarry, Instruction i) {
        boolean carryValue = false;
        int res = a >>> (n %= 32) | a << 32 - n;
        ArrayList<String> values = new ArrayList<String>();
        ArrayList<String> carrys = new ArrayList<String>();
        for (int j = 0; j <= n; ++j) {
            boolean carryValue2 = false;
            int intValue = a >>> j | a << 32 - j;
            String value = Integer.toBinaryString(intValue);
            values.add(("00000000000000000000000000000000".substring(value.length()) + value).replace("", " ").trim());
            int carryBit = j - 1;
            if (carryBit < 0) {
                carryBit = 32 + carryBit;
            }
            String binaryString = Integer.toBinaryString(a);
            int padding = 32 - binaryString.length();
            for (int k = 0; k < padding; ++k) {
                binaryString = "0" + binaryString;
            }
            boolean bl = j > 0 ? binaryString.charAt(31 - carryBit) == '1' : (carryValue2 = this.c);
            if (this.set && useShiftCarry) {
                this.c = carryValue2;
            }
            carrys.add(this.c ? "1" : "0");
        }
        ShiftInfo info = new ShiftInfo(values, ShiftType.ROR, n, carrys, useShiftCarry, dispFormat, this.currentInstLine);
        uiController.setShiftButton(info);
        int carryBit = n - 1;
        if (carryBit < 0) {
            carryBit = 32 + carryBit;
        }
        String binaryString = Integer.toBinaryString(a);
        int padding = 32 - binaryString.length();
        for (int j = 0; j < padding; ++j) {
            binaryString = "0" + binaryString;
        }
        boolean bl = n > 0 ? binaryString.charAt(31 - carryBit) == '1' : (carryValue = this.c);
        if (this.set && useShiftCarry) {
            this.c = carryValue;
        }
        statusRegBar.setBitValue(StatusBit.C, this.c, this.deferSceneUpdates);
        i.setSecondOperandCarry(carryValue);
        return res;
    }

    private int rrx(int a, Bases dispFormat, boolean useShiftCarry, Instruction i) {
        int carryInt = this.c ? 1 : 0;
        int res = a >>> 1 | carryInt << 31;
        ArrayList<String> values = new ArrayList<String>();
        ArrayList<String> carrys = new ArrayList<String>();
        String value = Integer.toBinaryString(a);
        carrys.add(Integer.toBinaryString(carryInt));
        values.add(("00000000000000000000000000000000".substring(value.length()) + value).replace("", " ").trim());
        value = Integer.toBinaryString(res);
        boolean carryValue = Integer.toBinaryString(a).endsWith("1");
        if (this.set && useShiftCarry) {
            this.c = carryValue;
        }
        carrys.add(this.c ? "1" : "0");
        values.add(("00000000000000000000000000000000".substring(value.length()) + value).replace("", " ").trim());
        ShiftInfo info = new ShiftInfo(values, ShiftType.RRX, 1, carrys, useShiftCarry, dispFormat, this.currentInstLine);
        uiController.setShiftButton(info);
        statusRegBar.setBitValue(StatusBit.C, this.c, this.deferSceneUpdates);
        i.setSecondOperandCarry(carryValue);
        return res;
    }

    private int and(int a, int b) {
        int res = a & b;
        return res;
    }

    private int orr(int a, int b) {
        int res = a | b;
        return res;
    }

    private int eor(int a, int b) {
        int res = a ^ b;
        return res;
    }

    private int orn(int a, int b) {
        int res = a | ~b;
        return res;
    }

    private int bic(int a, int b) {
        int res = a & ~b;
        return res;
    }

    private void tst(int a, int b) {
        int res = a & b;
        this.n = this.isNeg(res);
        this.z = res == 0;
        statusRegBar.setBitValue(StatusBit.N, this.n, this.deferSceneUpdates);
        statusRegBar.setBitValue(StatusBit.Z, this.z, this.deferSceneUpdates);
    }

    private void teq(int a, int b) {
        int res = a ^ b;
        this.n = this.isNeg(res);
        this.z = res == 0;
        statusRegBar.setBitValue(StatusBit.N, this.n, this.deferSceneUpdates);
        statusRegBar.setBitValue(StatusBit.Z, this.z, this.deferSceneUpdates);
    }

    private void cmp(int a, int b) {
        int res = a - b;
        this.v = this.hasOverflowSub(a, b, res);
        this.c = this.hasCarrySub(a, b, res);
        this.n = this.isNeg(res);
        this.z = res == 0;
        statusRegBar.setBitValue(StatusBit.N, this.n, this.deferSceneUpdates);
        statusRegBar.setBitValue(StatusBit.Z, this.z, this.deferSceneUpdates);
        statusRegBar.setBitValue(StatusBit.C, this.c, this.deferSceneUpdates);
        statusRegBar.setBitValue(StatusBit.V, this.v, this.deferSceneUpdates);
    }

    private void cmn(int a, int b) {
        int res = a + b;
        this.v = this.hasOverflowAdd(a, b, res);
        this.c = this.hasCarryAdd(a, b, res);
        this.n = this.isNeg(res);
        this.z = res == 0;
        statusRegBar.setBitValue(StatusBit.N, this.n, this.deferSceneUpdates);
        statusRegBar.setBitValue(StatusBit.Z, this.z, this.deferSceneUpdates);
        statusRegBar.setBitValue(StatusBit.C, this.c, this.deferSceneUpdates);
        statusRegBar.setBitValue(StatusBit.V, this.v, this.deferSceneUpdates);
    }

    private int ldr(int arg1, int arg2, Instruction i) throws Exception {
        int result;
        PointerInfo info = new PointerInfo("R" + i.getArgs().get(1).getValue(), arg1, this.currentInstLine);
        if (i.getArgs().size() > 2) {
            int offset;
            if (!i.getPostIndex()) {
                result = this.memory.getWord(arg1 + arg2, false);
            } else {
                result = this.memory.getWord(arg1, false);
                offset = arg1 + arg2;
                this.registers[i.getArgs().get(1).getValue()].setValue(offset);
                regAccordion.setRegValue(i.getArgs().get(1).getValue(), offset, true, this.deferSceneUpdates, false);
            }
            offset = arg1 + arg2;
            if (Integer.compareUnsigned(Math.abs(arg2), 4096) > 0) {
                throw new RuntimeError(i.getLineNumber(), "Address offset out of range for load instruction.", "Offset range is limited to \u00b14096 bytes.");
            }
            if (i.getPreIndex()) {
                this.registers[i.getArgs().get(1).getValue()].setValue(offset);
                regAccordion.setRegValue(i.getArgs().get(1).getValue(), offset, true, this.deferSceneUpdates, false);
            }
        } else {
            result = this.memory.getWord(arg1, false);
            if (i.getPreIndex()) {
                this.registers[i.getArgs().get(1).getValue()].setValue(arg1);
                regAccordion.setRegValue(i.getArgs().get(1).getValue(), arg1, true, this.deferSceneUpdates, false);
            }
        }
        info.setOffset(arg2);
        info.setCurrentValue(result);
        info.setPreIndexed(i.getPreIndex());
        info.setPostIndexed(i.getPostIndex());
        info.setType(PointerType.LDR);
        info.setDestination("R" + i.getArgs().get(0).getValue());
        uiController.setPointerButton(info);
        MemoryInfo memInfo = new MemoryInfo(false, false, this.currentInstLine);
        boolean nameExists = false;
        String name = "";
        for (DcdWord d : this.dcdWords) {
            if (d == null) break;
            if (d.getAddress() != arg1) continue;
            name = d.getName();
            nameExists = true;
            break;
        }
        if (nameExists) {
            memInfo.getNames().add(name);
        } else {
            memInfo.getNames().add("--");
        }
        if (arg2 != 0) {
            memInfo.getWords().add(this.memory.getMemWord(arg1, true, true));
            memInfo.getTypes().add(AddressType.BASE);
            memInfo.getWords().add(this.memory.getMemWord(arg1 + arg2, true, true));
            memInfo.getTypes().add(AddressType.ACTUAL);
            memInfo.getNames().add("--");
        } else {
            memInfo.getWords().add(this.memory.getMemWord(arg1, true, true));
            memInfo.getTypes().add(AddressType.ACTUAL);
        }
        uiController.setMemoryButton(memInfo);
        return result;
    }

    private int ldrb(int arg1, int arg2, Instruction i) throws Exception {
        int actualByteIndex = 0;
        int result;
        PointerInfo info = new PointerInfo("R" + i.getArgs().get(1).getValue(), arg1, this.currentInstLine);
        if (i.getArgs().size() > 2) {
            int offset;
            if (!i.getPostIndex()) {
                result = this.memory.getByte(arg1 + arg2);
            } else {
                result = this.memory.getByte(arg1);
                offset = arg1 + arg2;
                this.registers[i.getArgs().get(1).getValue()].setValue(offset);
                regAccordion.setRegValue(i.getArgs().get(1).getValue(), offset, true, this.deferSceneUpdates, false);
            }
            offset = arg1 + arg2;
            if (Integer.compareUnsigned(Math.abs(arg2), 4096) > 0) {
                throw new RuntimeError(i.getLineNumber(), "Address offset out of range for load instruction.", "Offset range is limited to \u00b14096 bytes.");
            }
            if (i.getPreIndex()) {
                this.registers[i.getArgs().get(1).getValue()].setValue(offset);
                regAccordion.setRegValue(i.getArgs().get(1).getValue(), offset, true, this.deferSceneUpdates, false);
            }
        } else {
            result = this.memory.getByte(arg1);
            if (i.getPreIndex()) {
                this.registers[i.getArgs().get(1).getValue()].setValue(arg1);
                regAccordion.setRegValue(i.getArgs().get(1).getValue(), arg1, true, this.deferSceneUpdates, false);
            }
        }
        info.setOffset(arg2);
        info.setCurrentValue(result);
        info.setPreIndexed(i.getPreIndex());
        info.setPostIndexed(i.getPostIndex());
        info.setType(PointerType.LDRB);
        info.setDestination("R" + i.getArgs().get(0).getValue());
        uiController.setPointerButton(info);
        MemoryInfo memInfo = new MemoryInfo(false, true, this.currentInstLine);
        boolean nameExists = false;
        String name = "";
        for (DcdWord d : this.dcdWords) {
            if (d == null) break;
            if (d.getAddress() != arg1 - arg1 % 4) continue;
            name = d.getName();
            nameExists = true;
            break;
        }
        if (nameExists) {
            memInfo.getNames().add(name);
        } else {
            memInfo.getNames().add("--");
        }
        if (arg2 != 0) {
            MemWord baseWord = this.memory.getMemWordForByte(arg1, true);
            memInfo.getWords().add(baseWord);
            memInfo.getTypes().add(AddressType.BASE);
            MemWord offsetWord = this.memory.getMemWordForByte(arg1 + arg2, true);
            if (offsetWord.getAddress() != baseWord.getAddress()) {
                memInfo.getWords().add(offsetWord);
                memInfo.getTypes().add(AddressType.ACTUAL);
            }
            nameExists = false;
            name = "";
            for (DcdWord d : this.dcdWords) {
                if (d == null) break;
                if (d.getAddress() != arg1 + arg2 - (arg1 + arg2) % 4) continue;
                name = d.getName();
                nameExists = true;
                break;
            }
            if (nameExists) {
                memInfo.getNames().add(name);
            } else {
                memInfo.getNames().add("--");
            }
        } else {
            memInfo.getWords().add(this.memory.getMemWordForByte(arg1, true));
            memInfo.getTypes().add(AddressType.ACTUAL);
        }
        int numBytes = Math.abs(arg2) + 1;
        int baseByteIndex = arg1 % 4;
        if (actualByteIndex > 7) {
            for (actualByteIndex = baseByteIndex + arg2; actualByteIndex > 7; actualByteIndex -= 4) {
            }
        }
        if (memInfo.getWords().size() > 1) {
            numBytes = 5 - Integer.remainderUnsigned(arg1, 4) + Integer.remainderUnsigned(arg2, 4);
        }
        if (arg2 < 0) {
            ++numBytes;
        }
        memInfo.setByteCounts(numBytes, baseByteIndex, actualByteIndex);
        uiController.setMemoryButton(memInfo);
        return result;
    }

    private void str(int arg0, int arg1, int arg2, Instruction i) throws Exception {
        PointerInfo info = new PointerInfo("R" + i.getArgs().get(1).getValue(), arg1, this.currentInstLine);
        if (i.getArgs().size() > 2) {
            int offset;
            if (!i.getPostIndex()) {
                this.memory.setWord(arg1 + arg2, arg0, false, this.deferSceneUpdates);
            } else {
                this.memory.setWord(arg1, arg0, false, this.deferSceneUpdates);
                offset = arg1 + arg2;
                this.registers[i.getArgs().get(1).getValue()].setValue(offset);
                regAccordion.setRegValue(i.getArgs().get(1).getValue(), offset, true, this.deferSceneUpdates, false);
            }
            offset = arg1 + arg2;
            if (Integer.compareUnsigned(Math.abs(arg2), 4096) > 0) {
                throw new RuntimeError(i.getLineNumber(), "Address offset out of range for load instruction.", "Offset range is limited to \u00b14096 bytes.");
            }
            if (i.getPreIndex()) {
                this.registers[i.getArgs().get(1).getValue()].setValue(offset);
                regAccordion.setRegValue(i.getArgs().get(1).getValue(), offset, true, this.deferSceneUpdates, false);
            }
        } else {
            this.memory.setWord(arg1, arg0, false, this.deferSceneUpdates);
            if (i.getPreIndex()) {
                this.registers[i.getArgs().get(1).getValue()].setValue(arg1);
                regAccordion.setRegValue(i.getArgs().get(1).getValue(), arg1, true, this.deferSceneUpdates, false);
            }
        }
        info.setOffset(arg2);
        info.setCurrentValue(arg0);
        info.setPreIndexed(i.getPreIndex());
        info.setPostIndexed(i.getPostIndex());
        info.setType(PointerType.STR);
        info.setDestination("R" + i.getArgs().get(0).getValue());
        uiController.setPointerButton(info);
        MemoryInfo memInfo = new MemoryInfo(true, false, this.currentInstLine);
        boolean nameExists = false;
        String name = "";
        for (DcdWord d : this.dcdWords) {
            if (d == null) break;
            if (d.getAddress() != arg1) continue;
            name = d.getName();
            nameExists = true;
            break;
        }
        if (nameExists) {
            memInfo.getNames().add(name);
        } else {
            memInfo.getNames().add("--");
        }
        if (arg2 != 0) {
            memInfo.getWords().add(this.memory.getMemWord(arg1, true, true));
            memInfo.getTypes().add(AddressType.BASE);
            memInfo.getWords().add(this.memory.getMemWord(arg1 + arg2, true, true));
            memInfo.getTypes().add(AddressType.ACTUAL);
            memInfo.getNames().add("--");
        } else {
            memInfo.getWords().add(this.memory.getMemWord(arg1, true, true));
            memInfo.getTypes().add(AddressType.ACTUAL);
        }
        uiController.setMemoryButton(memInfo);
    }

    private void strb(int arg0, int arg1, int arg2, Instruction i) throws Exception {
        int actualByteIndex = 0;
        PointerInfo info = new PointerInfo("R" + i.getArgs().get(1).getValue(), arg1, this.currentInstLine);
        if (i.getArgs().size() > 2) {
            int offset;
            if (!i.getPostIndex()) {
                this.memory.setByte(arg1 + arg2, (byte)arg0, this.deferSceneUpdates);
            } else {
                this.memory.setByte(arg1, (byte)arg0, this.deferSceneUpdates);
                offset = arg1 + arg2;
                this.registers[i.getArgs().get(1).getValue()].setValue(offset);
                regAccordion.setRegValue(i.getArgs().get(1).getValue(), offset, true, this.deferSceneUpdates, false);
            }
            offset = arg1 + arg2;
            if (Integer.compareUnsigned(Math.abs(arg2), 4096) > 0) {
                throw new RuntimeError(i.getLineNumber(), "Address offset out of range for load instruction.", "Offset range is limited to \u00b14096 bytes.");
            }
            if (i.getPreIndex()) {
                this.registers[i.getArgs().get(1).getValue()].setValue(offset);
                regAccordion.setRegValue(i.getArgs().get(1).getValue(), offset, true, this.deferSceneUpdates, false);
            }
        } else {
            this.memory.setByte(arg1, (byte)arg0, this.deferSceneUpdates);
            if (i.getPreIndex()) {
                this.registers[i.getArgs().get(1).getValue()].setValue(arg1);
                regAccordion.setRegValue(i.getArgs().get(1).getValue(), arg1, true, this.deferSceneUpdates, false);
            }
        }
        info.setOffset(arg2);
        info.setCurrentValue(arg0);
        info.setPreIndexed(i.getPreIndex());
        info.setPostIndexed(i.getPostIndex());
        info.setType(PointerType.STRB);
        info.setDestination("R" + i.getArgs().get(0).getValue());
        uiController.setPointerButton(info);
        MemoryInfo memInfo = new MemoryInfo(true, true, this.currentInstLine);
        boolean nameExists = false;
        String name = "";
        for (DcdWord d : this.dcdWords) {
            if (d == null) break;
            if (d.getAddress() != arg1 - arg1 % 4) continue;
            name = d.getName();
            nameExists = true;
            break;
        }
        if (nameExists) {
            memInfo.getNames().add(name);
        } else {
            memInfo.getNames().add("--");
        }
        if (arg2 != 0) {
            MemWord baseWord = this.memory.getMemWordForByte(arg1, true);
            memInfo.getWords().add(baseWord);
            memInfo.getTypes().add(AddressType.BASE);
            MemWord offsetWord = this.memory.getMemWordForByte(arg1 + arg2, true);
            if (offsetWord.getAddress() != baseWord.getAddress()) {
                memInfo.getWords().add(offsetWord);
                memInfo.getTypes().add(AddressType.ACTUAL);
            }
            nameExists = false;
            name = "";
            for (DcdWord d : this.dcdWords) {
                if (d == null) break;
                if (d.getAddress() != arg1 + arg2 - (arg1 + arg2) % 4) continue;
                name = d.getName();
                nameExists = true;
                break;
            }
            if (nameExists) {
                memInfo.getNames().add(name);
            } else {
                memInfo.getNames().add("--");
            }
        } else {
            memInfo.getWords().add(this.memory.getMemWordForByte(arg1, true));
            memInfo.getTypes().add(AddressType.ACTUAL);
        }
        int numBytes = Math.abs(arg2) + 1;
        int baseByteIndex = arg1 % 4;
        if (actualByteIndex > 7) {
            for (actualByteIndex = baseByteIndex + arg2; actualByteIndex > 7; actualByteIndex -= 4) {
            }
        }
        if (memInfo.getWords().size() > 1) {
            numBytes = 5 - Integer.remainderUnsigned(arg1, 4) + Integer.remainderUnsigned(arg2, 4);
        }
        if (arg2 < 0) {
            ++numBytes;
        }
        memInfo.setByteCounts(numBytes, baseByteIndex, actualByteIndex);
        uiController.setMemoryButton(memInfo);
    }

    private void stm(int baseReg, Instruction i) throws Exception {
        StackType type = StackType.fromString(i.getOpcode().substring(3, 5), true);
        List<Argument> argsToStore = i.getArgs().subList(1, i.getArgs().size());
        ArrayList<Register> regsToStore = new ArrayList<Register>();
        boolean writeBack = i.getPreIndex();
        for (Argument a : argsToStore) {
            regsToStore.add(new Register(this.resolveArg(a), this.registers[a.getValue()].getType()));
            if (i.getArgs().get(0).getValue() != this.registers[a.getValue()].getType().getRegName() || !writeBack) continue;
            VisUAL.addRuntimeError(i.getLineNumber(), "Destination address register cannot be in register list.", "If write-back is enabled for an STM instruction, the destination address register cannot be in the in the list of registers to store.");
            uiController.setAbort(true);
            return;
        }
        this.instrCycleCount += regsToStore.size();
        Collections.sort(regsToStore);
        ArrayList<StackEntry> entries = new ArrayList<StackEntry>();
        int oldAddress = this.registers[i.getArgs().get(0).getValue()].getValue();
        int direction = 1;
        switch (type) {
            case FD: {
                int j;
                Collections.reverse(regsToStore);
                entries.add(new StackEntry(baseReg, -1, this.memory.getWord(baseReg, false)));
                for (Register r : regsToStore) {
                    this.memory.setWord(baseReg -= 4, r.getValue(), false, this.deferSceneUpdates);
                }
                direction = -1;
                for (j = 0; j < regsToStore.size(); ++j) {
                    entries.add(0, new StackEntry(oldAddress + direction * ((j + 1) * 4), ((Register)regsToStore.get(j)).getType().getRegName(), ((Register)regsToStore.get(j)).getValue()));
                }
                break;
            }
            case FA: {
                int j;
                entries.add(new StackEntry(baseReg, -1, this.memory.getWord(baseReg, false)));
                for (Register r : regsToStore) {
                    this.memory.setWord(baseReg += 4, r.getValue(), false, this.deferSceneUpdates);
                }
                for (j = 0; j < regsToStore.size(); ++j) {
                    entries.add(0, new StackEntry(oldAddress + direction * ((j + 1) * 4), ((Register)regsToStore.get(j)).getType().getRegName(), ((Register)regsToStore.get(j)).getValue()));
                }
                break;
            }
            case ED: {
                int j;
                Collections.reverse(regsToStore);
                for (Register r : regsToStore) {
                    this.memory.setWord(baseReg, r.getValue(), false, this.deferSceneUpdates);
                    baseReg -= 4;
                }
                entries.add(new StackEntry(baseReg, -1, this.memory.getWord(baseReg, false)));
                direction = -1;
                for (j = 0; j < regsToStore.size(); ++j) {
                    entries.add(0, new StackEntry(oldAddress + direction * (j * 4), ((Register)regsToStore.get(j)).getType().getRegName(), ((Register)regsToStore.get(j)).getValue()));
                }
                break;
            }
            case EA: {
                int j;
                for (Register r : regsToStore) {
                    this.memory.setWord(baseReg, r.getValue(), false, this.deferSceneUpdates);
                    baseReg += 4;
                }
                entries.add(new StackEntry(baseReg, -1, this.memory.getWord(baseReg, false)));
                for (j = 0; j < regsToStore.size(); ++j) {
                    entries.add(0, new StackEntry(oldAddress + direction * (j * 4), ((Register)regsToStore.get(j)).getType().getRegName(), ((Register)regsToStore.get(j)).getValue()));
                }
                break;
            }
        }
        Collections.sort(entries);
        if (writeBack) {
            this.registers[i.getArgs().get(0).getValue()].setValue(baseReg);
            regAccordion.setRegValue(i.getArgs().get(0).getValue(), baseReg, true, this.deferSceneUpdates, false);
        }
        StackInfo info = new StackInfo(i.getLineNumber(), i.getArgs().get(0).getValue(), oldAddress, type, true, writeBack, entries);
        uiController.setStackButton(info);
    }

    private void ldm(int baseReg, Instruction i) throws Exception {
        StackType type = StackType.fromString(i.getOpcode().substring(3, 5), false);
        List<Argument> argsToLoad = i.getArgs().subList(1, i.getArgs().size());
        ArrayList<Register> regsToLoad = new ArrayList<Register>();
        boolean writeBack = i.getPreIndex();
        for (Argument a : argsToLoad) {
            regsToLoad.add(this.registers[a.getValue()]);
            if (a.getValue() == 15) {
                this.instrCycleCount += VisUAL.getCycleCounter().getCycleCount("B") - 1;
            }
            if (i.getArgs().get(0).getValue() != this.registers[a.getValue()].getType().getRegName() || !writeBack) continue;
            VisUAL.addRuntimeError(i.getLineNumber(), "Source address register cannot be in register list.", "If write-back is enabled for an LDM instruction, the source address register cannot be in the in the list of registers to load.");
            uiController.setAbort(true);
            return;
        }
        this.instrCycleCount += regsToLoad.size();
        Collections.sort(regsToLoad);
        ArrayList<StackEntry> entries = new ArrayList<StackEntry>();
        int oldAddress = this.registers[i.getArgs().get(0).getValue()].getValue();
        int direction = 1;
        try {
            switch (type) {
                case EA: {
                    if (writeBack) {
                        int writeBackValue = baseReg - 4 * regsToLoad.size();
                        this.registers[i.getArgs().get(0).getValue()].setValue(writeBackValue);
                        regAccordion.setRegValue(i.getArgs().get(0).getValue(), writeBackValue, true, this.deferSceneUpdates, false);
                    }
                    Collections.reverse(regsToLoad);
                    entries.add(new StackEntry(baseReg, -1, this.memory.getWord(baseReg, false)));
                    for (Register r : regsToLoad) {
                        r.setValue(this.memory.getWord(baseReg -= 4, false));
                        regAccordion.setRegValue(r.getType().getRegName(), r.getValue(), true, this.deferSceneUpdates, false);
                        int j = regsToLoad.indexOf(r);
                        entries.add(0, new StackEntry(oldAddress - direction * ((j + 1) * 4), ((Register)regsToLoad.get(j)).getType().getRegName(), ((Register)regsToLoad.get(j)).getValue()));
                    }
                    break;
                }
                case ED: {
                    if (writeBack) {
                        int writeBackValue = baseReg + 4 * regsToLoad.size();
                        this.registers[i.getArgs().get(0).getValue()].setValue(writeBackValue);
                        regAccordion.setRegValue(i.getArgs().get(0).getValue(), writeBackValue, true, this.deferSceneUpdates, false);
                    }
                    entries.add(new StackEntry(baseReg, -1, this.memory.getWord(baseReg, false)));
                    direction = -1;
                    for (Register r : regsToLoad) {
                        r.setValue(this.memory.getWord(baseReg += 4, false));
                        regAccordion.setRegValue(r.getType().getRegName(), r.getValue(), true, this.deferSceneUpdates, false);
                        int j = regsToLoad.indexOf(r);
                        entries.add(0, new StackEntry(oldAddress - direction * ((j + 1) * 4), ((Register)regsToLoad.get(j)).getType().getRegName(), ((Register)regsToLoad.get(j)).getValue()));
                    }
                    break;
                }
                case FA: {
                    if (writeBack) {
                        int writeBackValue = baseReg - 4 * regsToLoad.size();
                        this.registers[i.getArgs().get(0).getValue()].setValue(writeBackValue);
                        regAccordion.setRegValue(i.getArgs().get(0).getValue(), writeBackValue, true, this.deferSceneUpdates, false);
                    }
                    Collections.reverse(regsToLoad);
                    for (Register r : regsToLoad) {
                        r.setValue(this.memory.getWord(baseReg, false));
                        regAccordion.setRegValue(r.getType().getRegName(), r.getValue(), true, this.deferSceneUpdates, false);
                        int j = regsToLoad.indexOf(r);
                        entries.add(0, new StackEntry(oldAddress - direction * (j * 4), ((Register)regsToLoad.get(j)).getType().getRegName(), ((Register)regsToLoad.get(j)).getValue()));
                        baseReg -= 4;
                    }
                    break;
                }
                case FD: {
                    if (writeBack) {
                        int writeBackValue = baseReg + 4 * regsToLoad.size();
                        this.registers[i.getArgs().get(0).getValue()].setValue(writeBackValue);
                        regAccordion.setRegValue(i.getArgs().get(0).getValue(), writeBackValue, true, this.deferSceneUpdates, false);
                    }
                    direction = -1;
                    for (Register r : regsToLoad) {
                        r.setValue(this.memory.getWord(baseReg, false));
                        regAccordion.setRegValue(r.getType().getRegName(), r.getValue(), true, this.deferSceneUpdates, false);
                        int j = regsToLoad.indexOf(r);
                        entries.add(0, new StackEntry(oldAddress - direction * (j * 4), ((Register)regsToLoad.get(j)).getType().getRegName(), ((Register)regsToLoad.get(j)).getValue()));
                        baseReg += 4;
                    }
                    break;
                }
            }
        }
        catch (Register.WriteToProgramCounterException e) {
            entries.add(new StackEntry(baseReg, 15, this.memory.getWord(baseReg, false)));
            if (type == StackType.FA || type == StackType.FD) {
                entries.add(new StackEntry(baseReg - 4 * direction, -1, this.memory.getWord(baseReg - 4 * direction, false)));
            }
            regAccordion.setRegValue(15, this.registers[15].getValue(), true, this.deferSceneUpdates, false);
            Collections.sort(entries);
            StackInfo info = new StackInfo(i.getLineNumber(), i.getArgs().get(0).getValue(), oldAddress, type, false, writeBack, entries);
            uiController.setStackButton(info);
            throw e;
        }
        if (type == StackType.FA || type == StackType.FD) {
            entries.add(new StackEntry(baseReg, -1, this.memory.getWord(baseReg, false)));
        }
        Collections.sort(entries);
        StackInfo info = new StackInfo(i.getLineNumber(), i.getArgs().get(0).getValue(), oldAddress, type, false, writeBack, entries);
        uiController.setStackButton(info);
    }

    public String getCarryAsText() {
        if (this.c) {
            return "1";
        }
        return "0";
    }

    public boolean[] getStatusBits() {
        boolean[] statusBits = new boolean[]{this.n, this.z, this.c, this.v};
        return statusBits;
    }

    public void setStatusBits(boolean[] values) {
        this.n = values[0];
        this.z = values[1];
        this.c = values[2];
        this.v = values[3];
    }

    public Register[] getRegisters() {
        return this.registers;
    }

    public void setRegisters(Register[] registers, boolean[] highlights) {
        for (int i = 0; i < 16; ++i) {
            try {
                this.registers[i].setValue(registers[i].getValue());
            }
            catch (Register.WriteToProgramCounterException writeToProgramCounterException) {
                // empty catch block
            }
            regAccordion.setRegValue(i, registers[i].getValue(), highlights[i], false, true);
        }
    }

    public void setProgramCounter(int value, boolean deferSceneUpdates) {
        try {
            this.registers[15].setValue(value);
        }
        catch (Register.WriteToProgramCounterException writeToProgramCounterException) {
            // empty catch block
        }
        regAccordion.setRegValue(15, value, false, deferSceneUpdates, false);
    }

    public void resetInstrCycleCount() {
        this.instrCycleCount = 0;
    }

    public int getCurrentInstLine() {
        return this.currentInstLine;
    }
}

