/*
 * Decompiled with CFR 0.152.
 */
package visual;

import visual.Bases;
import visual.BranchInfo;
import visual.CodeCanvas;
import visual.CodeCanvasState;
import visual.Emulator;
import visual.MemoryInfo;
import visual.PointerInfo;
import visual.Register;
import visual.ShiftInfo;
import visual.StackInfo;
import visual.StatusBit;
import visual.StatusBitState;
import visual.UIController;
import visual.VisUAL;

public class EmulatorState {
    private static int stateCount = 0;
    private static UIController uiController = VisUAL.getUIController();
    private static int statePointer = 0;
    private int stateIdentifier;
    private CodeCanvasState currentLineState;
    private CodeCanvasState branchDestState;
    private CodeCanvasState branchArrowState;
    private CodeCanvasState linkRegisterState;
    private Register[] registers = new Register[16];
    private boolean[] regHighlights = new boolean[16];
    private Bases[] regDispFormats = new Bases[16];
    private int lineNumber = 0;
    private int currentCycles = 0;
    private int totalCycles = 0;
    private StatusBitState[] statusBitStates = new StatusBitState[4];
    private PointerInfo pointerInfo = new PointerInfo(null);
    private MemoryInfo memoryInfo = new MemoryInfo(null);
    private StackInfo stackInfo = new StackInfo();
    private ShiftInfo shiftInfo = new ShiftInfo(null);
    private BranchInfo branchInfo = new BranchInfo(null);

    public EmulatorState(Emulator emulator) {
        if (statePointer + 1 < stateCount) {
            ++statePointer;
            return;
        }
        CodeCanvas currentLineCanvas = VisUAL.getCurrentLineCanvas();
        CodeCanvas branchDestCanvas = VisUAL.getBranchDestCanvas();
        CodeCanvas branchArrowCanvas = VisUAL.getBranchArrowCanvas();
        CodeCanvas linkRegisterCanvas = VisUAL.getLinkRegisterCanvas();
        this.currentLineState = new CodeCanvasState(currentLineCanvas.getLineNumber(), currentLineCanvas.isHide(), currentLineCanvas.getType(), currentLineCanvas.getColour(), currentLineCanvas.getArrowLines());
        this.branchDestState = new CodeCanvasState(branchDestCanvas.getLineNumber(), branchDestCanvas.isHide(), branchDestCanvas.getType(), branchDestCanvas.getColour(), branchDestCanvas.getArrowLines());
        this.branchArrowState = new CodeCanvasState(branchArrowCanvas.getLineNumber(), branchArrowCanvas.isHide(), branchArrowCanvas.getType(), branchArrowCanvas.getColour(), branchArrowCanvas.getArrowLines());
        this.linkRegisterState = new CodeCanvasState(linkRegisterCanvas.getLineNumber(), linkRegisterCanvas.isHide(), linkRegisterCanvas.getType(), linkRegisterCanvas.getColour(), linkRegisterCanvas.getArrowLines());
        for (int i = 0; i < 16; ++i) {
            this.registers[i] = new Register(emulator.getRegisters()[i]);
            this.regHighlights[i] = VisUAL.getRegAccordion().getHighlighted()[i];
            this.regDispFormats[i] = VisUAL.getRegAccordion().getDispBase(i);
        }
        this.statusBitStates[0] = new StatusBitState(StatusBit.N, VisUAL.getRegAccordion().getStatusRegBar().getBitStyle(StatusBit.N), emulator.getStatusBits()[0]);
        this.statusBitStates[1] = new StatusBitState(StatusBit.Z, VisUAL.getRegAccordion().getStatusRegBar().getBitStyle(StatusBit.Z), emulator.getStatusBits()[1]);
        this.statusBitStates[2] = new StatusBitState(StatusBit.C, VisUAL.getRegAccordion().getStatusRegBar().getBitStyle(StatusBit.C), emulator.getStatusBits()[2]);
        this.statusBitStates[3] = new StatusBitState(StatusBit.V, VisUAL.getRegAccordion().getStatusRegBar().getBitStyle(StatusBit.V), emulator.getStatusBits()[3]);
        this.lineNumber = uiController.getCurrentLine();
        this.currentCycles = VisUAL.getClockCycleBar().getCurrentCycles();
        this.totalCycles = VisUAL.getClockCycleBar().getTotalCycles();
        this.pointerInfo = new PointerInfo(uiController.getCachedPointerInfo());
        this.memoryInfo = new MemoryInfo(uiController.getCachedMemoryInfo());
        this.stackInfo = new StackInfo(uiController.getCachedStackInfo());
        this.shiftInfo = new ShiftInfo(uiController.getCachedShiftInfo());
        this.branchInfo = new BranchInfo(uiController.getCachedBranchInfo());
        this.stateIdentifier = stateCount;
        statePointer = stateCount++;
    }

    public static void resetStateCount() {
        stateCount = 0;
        statePointer = 0;
    }

    public static int getStateCount() {
        return stateCount;
    }

    public static int getLastStateSaved() {
        return statePointer + 1;
    }

    public static int getStatePointer() {
        return statePointer;
    }

    public static void setStatePointer(int offset) {
        statePointer += offset;
    }

    public static void overrideStatePointer(int value) {
        statePointer = value;
    }

    public static boolean statePointerAtEnd() {
        return stateCount - 1 == statePointer && stateCount > 0;
    }

    public int getId() {
        return this.stateIdentifier;
    }

    public CodeCanvasState getCurrentLineState() {
        return this.currentLineState;
    }

    public CodeCanvasState getBranchDestState() {
        return this.branchDestState;
    }

    public CodeCanvasState getBranchArrowState() {
        return this.branchArrowState;
    }

    public CodeCanvasState getLinkRegisterState() {
        return this.linkRegisterState;
    }

    public Register[] getRegisters() {
        return this.registers;
    }

    public Bases[] getRegDispFormats() {
        return this.regDispFormats;
    }

    public StatusBitState[] getStatusBitStates() {
        return this.statusBitStates;
    }

    public boolean[] getRegHighlights() {
        return this.regHighlights;
    }

    public int getLineNumber() {
        return this.lineNumber;
    }

    public int getCurrentCycles() {
        return this.currentCycles;
    }

    public int getTotalCycles() {
        return this.totalCycles;
    }

    public PointerInfo getPointerInfo() {
        return this.pointerInfo;
    }

    public MemoryInfo getMemoryInfo() {
        return this.memoryInfo;
    }

    public StackInfo getStackInfo() {
        return this.stackInfo;
    }

    public ShiftInfo getShiftInfo() {
        return this.shiftInfo;
    }

    public BranchInfo getBranchInfo() {
        return this.branchInfo;
    }
}

