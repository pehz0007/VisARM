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
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class TooltipPopOverPane
extends VBox {
    @FXML
    private Label lblTitle;
    @FXML
    private Text txtContent;

    TooltipPopOverPane(String opcode) {
        FXMLLoader fxmlLoader = new FXMLLoader(((Object)((Object)this)).getClass().getResource("/TooltipPopOverPane.fxml"));
        fxmlLoader.setRoot((Object)this);
        fxmlLoader.setController((Object)this);
        try {
            fxmlLoader.load();
            if (System.getProperty("os.name").contains("Mac") || System.getProperty("os.name").contains("Win")) {
                this.lblTitle.setStyle("-fx-font-family: Consolas");
                this.txtContent.setStyle("-fx-font-family: Consolas");
            }
            String trimmed = opcode;
            switch (opcode.length()) {
                default: {
                    break;
                }
                case 2: {
                    break;
                }
                case 3: {
                    if (opcode.equals("BIC") || !opcode.startsWith("B")) break;
                    trimmed = "B";
                    break;
                }
                case 4: {
                    if (opcode.equals("FILL")) break;
                    if (!opcode.startsWith("BL")) {
                        trimmed = opcode.substring(0, 3);
                        break;
                    }
                    if (opcode.endsWith("B")) break;
                    trimmed = "BL";
                    break;
                }
                case 5: 
                case 7: {
                    trimmed = opcode.substring(0, 3);
                    break;
                }
                case 6: {
                    trimmed = !opcode.endsWith("B") ? opcode.substring(0, 3) : opcode.substring(0, 4);
                }
            }
            switch (trimmed.toUpperCase()) {
                case "MOV": {
                    this.lblTitle.setText("MOV instruction");
                    this.txtContent.setText("MOV{S}{cond} dest, source\ndest:\tR0-R15\nsource: Flexible second operand\n\nLoads the value of the source to the destination.\n\nMOV R0, R1\nMOVS R2, #40\nMOVNE R3, R1, LSL #4\n\nNotes:\n* When dest is R15 (PC), bit[0] of the value written to PC\n  is ignored. A branch occurs to the address created as\n  a result of this.\n* When R13 (SP) or R15 (PC) are used, {S} must not be used,\n  and the flexible second operand must be a register without\n  a shift.\n* If a constant or expression, the value of the flexible second\n  operand must evaluate to a number that can be created by rotating\n  an 8-bit value right within a 32-bit word.\n");
                    break;
                }
                case "MVN": {
                    this.lblTitle.setText("MVN instruction");
                    this.txtContent.setText("MVN{S}{cond} dest, source\ndest:\tR0-R15\nsource: Flexible second operand\n\nLoads the bitwise negated value of the source to the destination.\n\nMVN R0, R1\nMVNS R2, #40\nMVNGT R3, R1, LSL #4\n\nNotes:\n* When dest is R15 (PC), bit[0] of the value written to PC\n  is ignored. A branch occurs to the address created as\n  a result of this.\n* When R13 (SP) or R15 (PC) are used, {S} must not be used,\n  and the flexible second operand must be a register without\n  a shift.\n* If a constant or expression, the value of the flexible second\n  operand must evaluate to a number that can be created by rotating\n  an 8-bit value right within a 32-bit word.\n");
                    break;
                }
                case "ADD": {
                    this.lblTitle.setText("ADD instruction");
                    this.txtContent.setText("ADD{S}{cond} dest, op1, op2\ndest:\tR0-R15\nop1:\tR0-R15\nop2:\tFlexible second operand, except R13 (SP) and R15 (PC)\n\nSaves the value op1+op2 to the destination.\n\nADD R0, R1, R2\nADDS R0, R0, #40\nADDEQ R0, PC, #8\n\nNotes:\n* dest can be R13 (SP) only if:\n  - the instruction is of the form: ADD{S}{cond} dest, SP, op2\n    and op2 is constant or a LSL #1, LSL #2 or LSL #3\n* dest can be R15 (PC) only if op1 and op2 are not R13 (SP)\n* op1 can be R15 (PC) if:\n  - dest and op2 are not R13 (SP)\n  - or the instruction is of the form: ADD{cond} dest, PC, #n\n* If a constant or expression, the value of the flexible second\n  operand must evaluate to a number that can be created by rotating\n  an 8-bit value right within a 32-bit word.\n");
                    break;
                }
                case "ADC": {
                    this.lblTitle.setText("ADC instruction");
                    this.txtContent.setText("ADC{S}{cond} dest, op1, op2\ndest:\tR0-R12,R14\nop1:\tR0-R12,R14\nop2:\tFlexible second operand, except R13 (SP) and R15 (PC)\n\nSaves the value op1+op2+carry to the destination.\n\nNotes:\n* If a constant or expression, the value of the flexible second\n  operand must evaluate to a number that can be created by rotating\n  an 8-bit value right within a 32-bit word.\nADC R0, R1, R2\nADCS R0, R0, #40\nADCHI R0, R3, #8\n");
                    break;
                }
                case "SUB": {
                    this.lblTitle.setText("SUB instruction");
                    this.txtContent.setText("SUB{S}{cond} dest, op1, op2\ndest:\tR0-R14\nop1:\tR0-R14\nop2:\tFlexible second operand, except R13 (SP) and R15 (PC)\n\nSaves the value op1-op2 to the destination.\n\nSUB R0, R1, R2\nSUBS R0, R0, #40\nSUBEQ R0, LR, #8\n\nNotes:\n* dest can be R13 (SP) only if:\n  - the instruction is of the form: SUB{S}{cond} dest, SP, op2\n    and op2 is constant or a LSL #1, LSL #2 or LSL #3\n* op1 can be R15 (PC) only if:\n  - the instruction is of the form: SUB{cond} dest, PC, #n\n* If a constant or expression, the value of the flexible second\n  operand must evaluate to a number that can be created by rotating\n  an 8-bit value right within a 32-bit word.\n");
                    break;
                }
                case "SBC": {
                    this.lblTitle.setText("SBC instruction");
                    this.txtContent.setText("SBC{S}{cond} dest, op1, op2\ndest:\tR0-R12,R14\nop1:\tR0-R12,R14\nop2:\tFlexible second operand, except R13 (SP) and R15 (PC)\n\nSaves the value op1-op2-(!carry? 1 : 0) to the destination.\n\nSBC R0, R1, R2\nSBCS R0, R0, #40\nSBCEQ R0, LR, #8\n\nNotes:\n* If a constant or expression, the value of the flexible second\n  operand must evaluate to a number that can be created by rotating\n  an 8-bit value right within a 32-bit word.\n");
                    break;
                }
                case "RSB": {
                    this.lblTitle.setText("RSB instruction");
                    this.txtContent.setText("RSB{S}{cond} dest, op1, op2\ndest:\tR0-R12,R14\nop1:\tR0-R12,R14\nop2:\tFlexible second operand, except R13 (SP) and R15 (PC)\n\nSaves the value op2-op1 to the destination.\n\nRSB R0, R1, R2\nRSBS R0, R0, #40\nRSBEQ R0, LR, #8\n\nNotes:\n* If a constant or expression, the value of the flexible second\n  operand must evaluate to a number that can be created by rotating\n  an 8-bit value right within a 32-bit word.\n");
                    break;
                }
                case "RSC": {
                    this.lblTitle.setText("RSC instruction");
                    this.txtContent.setText("RSC{S}{cond} dest, op1, op2\ndest:\tR0-R12,R14\nop1:\tR0-R12,R14\nop2:\tFlexible second operand, except R13 (SP) and R15 (PC)\n\nSaves the value op2-op1-(!carry? 1 : 0) to the destination.\n\nRSC R0, R1, R2\nRSCS R0, R0, #40\nRSCEQ R0, LR, #8\n\nNotes:\n* If a constant or expression, the value of the flexible second\n  operand must evaluate to a number that can be created by rotating\n  an 8-bit value right within a 32-bit word.\n");
                    break;
                }
                case "ADR": {
                    this.lblTitle.setText("ADR instruction");
                    this.txtContent.setText("ADR{cond} dest, label\ndest:\tR0-R12,R14,R15\nlabel:\tlabel for line or constant or data in memory\n\nSaves the address of the label to the destination.\n\nADR R0, array\nADRLO R1, calculate\n");
                    break;
                }
                case "AND": {
                    this.lblTitle.setText("AND instruction");
                    this.txtContent.setText("AND{S}{cond} dest, op1, op2\ndest:\tR0-R15\nop1:\tR0-R15\nop2:\tFlexible second operand, except R13 (SP) and R15 (PC)\n\nSaves the value of the bitwise operation (op1 & op2) to the destination.\n\nAND R0, R1, R2\nANDS R0, R0, #40\nANDEQ R0, R11, #13\n\nNotes:\n* If a constant or expression, the value of the flexible second\n  operand must evaluate to a number that can be created by rotating\n  an 8-bit value right within a 32-bit word.\n");
                    break;
                }
                case "EOR": {
                    this.lblTitle.setText("EOR instruction");
                    this.txtContent.setText("EOR{S}{cond} dest, op1, op2\ndest:\tR0-R12,R14\nop1:\tR0-R12,R14\nop2:\tFlexible second operand, except R13 (SP) and R15 (PC)\n\nSaves the value of the bitwise operation (op1 ^ op2) to the destination.\n\nEOR R0, R1, R2\nEORS R0, R0, #40\nEOREQ R0, R11, #13\n\nNotes:\n* If a constant or expression, the value of the flexible second\n  operand must evaluate to a number that can be created by rotating\n  an 8-bit value right within a 32-bit word.\n");
                    break;
                }
                case "BIC": {
                    this.lblTitle.setText("BIC instruction");
                    this.txtContent.setText("BIC{S}{cond} dest, op1, op2\ndest:\tR0-R12,R14\nop1:\tR0-R12,R14\nop2:\tFlexible second operand, except R13 (SP) and R15 (PC)\n\nSaves the value of the bitwise operation (op1 & !op2) to the destination.\n\nBIC R0, R1, R2\nBICS R0, R0, #40\nBICEQ R0, R11, #13\n\nNotes:\n* If a constant or expression, the value of the flexible second\n  operand must evaluate to a number that can be created by rotating\n  an 8-bit value right within a 32-bit word.\n");
                    break;
                }
                case "ORR": {
                    this.lblTitle.setText("ORR instruction");
                    this.txtContent.setText("ORR{S}{cond} dest, op1, op2\ndest:\tR0-R12,R14\nop1:\tR0-R12,R14\nop2:\tFlexible second operand, except R13 (SP) and R15 (PC)\n\nSaves the value of the bitwise operation (op1 | op2) to the destination.\n\nORR R0, R1, R2\nORRS R0, R0, #40\nORREQ R0, R11, #13\n\nNotes:\n* If a constant or expression, the value of the flexible second\n  operand must evaluate to a number that can be created by rotating\n  an 8-bit value right within a 32-bit word.\n");
                    break;
                }
                case "ORN": {
                    this.lblTitle.setText("ORN instruction");
                    this.txtContent.setText("ORN{S}{cond} dest, op1, op2\ndest:\tR0-R12,R14\nop1:\tR0-R12,R14\nop2:\tFlexible second operand, except R13 (SP) and R15 (PC)\n\nSaves the value of the bitwise operation !(op1 | op2) to the destination.\n\nORN R0, R1, R2\nORNS R0, R0, #40\nORNEQ R0, R11, #13\n\nNotes:\n* If a constant or expression, the value of the flexible second\n  operand must evaluate to a number that can be created by rotating\n  an 8-bit value right within a 32-bit word.\n");
                    break;
                }
                case "LSL": {
                    this.lblTitle.setText("LSL instruction");
                    this.txtContent.setText("LSL{S}{cond} dest, op1, op2\ndest:\tR0-R12,R14\nop1:\tR0-R12,R14\nop2:\tR0-R12,R14, #n\n\nSaves the value of op1 logically shifted left by n bits to the destination.\nUse shift visualisations for details.\n\nLSL R0, R1, R2\nLSLS R0, R0, #10\nLSLEQ R0, R2, #9\n\nNotes:\n* Only byte 0 of op2 is used if it is a register.\n* If op2 is a constant, it must be between 0 and 255.\n");
                    break;
                }
                case "LSR": {
                    this.lblTitle.setText("LSR instruction");
                    this.txtContent.setText("LSR{S}{cond} dest, op1, op2\ndest:\tR0-R12,R14\nop1:\tR0-R12,R14\nop2:\tR0-R12,R14, #n\n\nSaves the value of op1 logically shifted right by n bits to the destination.\nUse shift visualisations for details.\n\nLSR R0, R1, R2\nLSRS R0, R0, #10\nLSREQ R0, R2, #9\n\nNotes:\n* Only byte 0 of op2 is used if it is a register.\n* If op2 is a constant, it must be between 0 and 255.\n");
                    break;
                }
                case "ASR": {
                    this.lblTitle.setText("ASR instruction");
                    this.txtContent.setText("ASR{S}{cond} dest, op1, op2\ndest:\tR0-R12,R14\nop1:\tR0-R12,R14\nop2:\tR0-R12,R14, #n\n\nSaves the value of op1 arithmetically shifted right by n bits to the destination.\nUse shift visualisations for details.\n\nASR R0, R1, R2\nASRS R0, R0, #10\nASREQ R0, R2, #9\n\nNotes:\n* Only byte 0 of op2 is used if it is a register.\n* If op2 is a constant, it must be between 0 and 255.\n");
                    break;
                }
                case "ROR": {
                    this.lblTitle.setText("ROR instruction");
                    this.txtContent.setText("ROR{S}{cond} dest, op1, op2\ndest:\tR0-R12,R14\nop1:\tR0-R12,R14\nop2:\tR0-R12,R14, #n\n\nSaves the value of op1 rotated right by n bits to the destination.\nUse shift visualisations for details.\n\nROR R0, R1, R2\nRORS R0, R0, #10\nROREQ R0, R2, #9\n\nNotes:\n* Only byte 0 of op2 is used if it is a register.\n* If op2 is a constant, it must be between 0 and 255.\n");
                    break;
                }
                case "RRX": {
                    this.lblTitle.setText("RRX instruction");
                    this.txtContent.setText("RRX{S}{cond} dest, op1\ndest:\tR0-R12,R14,R15\nop1:\tR0-R12,R14,R15\n\nSaves the value of op1 rotated right by 1 bits to the destination.\nBit 31 is set to the carry bit before execution.\nUse shift visualisations for details.\n\nRRX R0, R2\nRRX PC, R0\n");
                    break;
                }
                case "CMP": {
                    this.lblTitle.setText("CMP instruction");
                    this.txtContent.setText("CMP{cond} op1, op2\nop1:\tR0-R14\nop2:\tFlexible second operand, except R13 (SP) and R15 (PC)\n\nSets the status bits based on the result of (op1 - op2).\nCMP R0, R2\nCMP R0, #42\n\nNotes:\n* If a constant or expression, the value of the flexible second\n  operand must evaluate to a number that can be created by rotating\n  an 8-bit value right within a 32-bit word.\n");
                    break;
                }
                case "CMN": {
                    this.lblTitle.setText("CMN instruction");
                    this.txtContent.setText("CMN{cond} op1, op2\nop1:\tR0-R14\nop2:\tFlexible second operand, except R13 (SP) and R15 (PC)\n\nSets the status bits based on the result of (op1 + op2).\n\nCMN R0, R2\nCMN R0, #42\n\nNotes:\n* If a constant or expression, the value of the flexible second\n  operand must evaluate to a number that can be created by rotating\n  an 8-bit value right within a 32-bit word.\n");
                    break;
                }
                case "TST": {
                    this.lblTitle.setText("TST instruction");
                    this.txtContent.setText("TST{cond} op1, op2\nop1:\tR0-R12,R14\nop2:\tFlexible second operand, except R13 (SP) and R15 (PC)\n\nSets the status bits based on the result of (op1 & op2).\n\nTST R0, R2\nTST R0, #42\n\nNotes:\n* If a constant or expression, the value of the flexible second\n  operand must evaluate to a number that can be created by rotating\n  an 8-bit value right within a 32-bit word.\n");
                    break;
                }
                case "TEQ": {
                    this.lblTitle.setText("TEQ instruction");
                    this.txtContent.setText("TEQ{cond} op1, op2\nop1:\tR0-R12,R14\nop2:\tFlexible second operand, except R13 (SP) and R15 (PC)\n\nSets the status bits based on the result of (op1 ^ op2).\n\nTEQ R0, R2\nTEQ R0, #42\n\nNotes:\n* If a constant or expression, the value of the flexible second\n  operand must evaluate to a number that can be created by rotating\n  an 8-bit value right within a 32-bit word.\n");
                    break;
                }
                case "B": {
                    this.lblTitle.setText("B instruction");
                    this.txtContent.setText("B{cond} label\nlabel:  The label for the line of code to branch to.\n\nBranches to the line with the given label.\n\nB   loop\nBNE calculateSubroutine\n");
                    break;
                }
                case "BL": {
                    this.lblTitle.setText("BL instruction");
                    this.txtContent.setText("BL{cond} label\nlabel:  The label for the line of code to branch to.\n\nBranches to the line with the given label, and updates the\nlink register R13 (LR) to the next line of code. This can be\nused to return from subroutines.\n\nBL   saveToMemory\nBLEQ resetCounter\n");
                    break;
                }
                case "LDR": {
                    this.lblTitle.setText("LDR instruction");
                    this.txtContent.setText("LDR{B}{cond} dest, [base {, offset}] ; offset addressing\nLDR{B}{cond} dest, [base, offset]!   ; pre-indexed addressing\nLDR{B}{cond} dest, [base], offset    ; post-indexed addressing\ndest:   R0-R15\nbase:   R0-R12,R14,R15\noffset: Flexible second operand, except R13 (SP) and R15 (PC).\n\nLoads the value stored at the address specified to the destination.\n\nLDR R0, [R1, #4]\nLDR R1, [R2, -R5]!\nLDR R2, [R3], #8\n\nNotes:\n* In offset addressing, the value is loaded from the address (base+offset).\n* In pre-indexed addressing, the value is loaded from the address (base+offset),\n  and base is updated to (base+offset).\n* In post-indexed adressing, the value is loaded from the address (base),\n  and then base is updated to (base+offset).\n* Destination register must be different from the base register for pre/post-indexed.\n* Destination register can be R15 (PC) only with LDR, not with LDRB\n* Destination register can be R13 (SP) only with LDR, not with LDRB.\n* Base register cannot be R13 (SP).\n* If a constant or expression, the value of the flexible second\n  operand must evaluate to a number between 0 and \u00b14095.\n");
                    break;
                }
                case "STR": {
                    this.lblTitle.setText("STR instruction");
                    this.txtContent.setText("STR{B}{cond} source, [base {, offset}] ; offset addressing\nSTR{B}{cond} source, [base, offset]!   ; pre-indexed addressing\nSTR{B}{cond} source, [base], offset    ; post-indexed addressing\nsource: R0-R15\nbase:   R0-R12,R14\noffset: Flexible second operand, except R13 (SP) and R15 (PC).\n\nStores the value in the source to the address specified.\n\nSTR R0, [R1, #4]\nSTR R1, [R2, -R5]!\nSTR R2, [R3], #8\n\nNotes:\n* In offset addressing, the value is stored to the address (base+offset).\n* In pre-indexed addressing, the value is stored to the address (base+offset),\n  and base is updated to (base+offset).\n* In post-indexed adressing, the value is stored to the address (base),\n  and then base is updated to (base+offset).\n* Source register must be different from the base register for pre/post-indexed.\n* Source register can be R15 (PC) only with STR, not with STRB\n* Source register can be R13 (SP) only with STR, not with STRB.\n* Base register cannot be R13 (SP) or R15 (PC).\n* If a constant or expression, the value of the flexible second\n  operand must evaluate to a number between 0 and \u00b14095.\n");
                    break;
                }
                case "STM": {
                    this.lblTitle.setText("STM instruction");
                    this.txtContent.setText("STM{type} pointer{!}, reglist\npointer: R0-R15\nreglist: Comma-seprated list of registers to store. Supports ranges.\n         Allowed registers are R0-R12,R14\ntype:    Stack type, one of: FD, FA, ED, EA, IB, IA, DB, DA\n\nStores the list of registers in the reglist to the address in memory\nspecified by the pointer. Stack type controls pointer increment behaviour.\nUse the stack visualisations for details.\n\nSTMFD R13!, {R0-R5, R8}\nSTMEA SP!, {R3,R8,R9,LR}\n\nNotes:\n* reglist cannot contain the pointer register.\n* FD / DB: Fully descending stack, decerment before store\n* FA / IB: Fully ascending stack, increment before store\n* ED / DA: Empty descending stack, decrement after store\n* EA / IA: Empty ascending stack, increment after store\n");
                    break;
                }
                case "LDM": {
                    this.lblTitle.setText("LDM instruction");
                    this.txtContent.setText("LDM{type} pointer{!}, reglist\npointer: R0-R15\nreglist: Comma-seprated list of registers to load. Supports ranges.\n         Allowed registers are R0-R12,R14,R15\ntype:    Stack type, one of: FD, FA, ED, EA, IB, IA, DB, DA\n\nLoads the list of registers in the reglist from the address in memory\nspecified by the pointer. Stack type controls pointer increment behaviour.\nUse the stack visualisations for details.\n\nLDMFD R13!, {R0-R5, R8}\nLDMEA SP!, {R3,R8,R9,LR}\n\nNotes:\n* reglist cannot contain both R14 (LR) and R15 (PC) at the same time.\n* reglist cannot contain the pointer register.\n* FD / IA: Fully descending stack, increment after load\n* FA / DA: Fully ascending stack, decrement after load\n* ED / IB: Empty descending stack, increment before load\n* EA / DB: Empty ascending stack, decrement before load\n");
                    break;
                }
                case "DCD": {
                    this.lblTitle.setText("DCD directive");
                    this.txtContent.setText("label DCD expressionlist\nlabel: Name for base address of array in memory\nexpressionlist: Comma-separated list of numerical expressions.\n                Allowed operators are +, - and *\n\nDefines a word array in memory containing the solved form of the list\nof given numerical expressions. These expressions can simply be\nimmediate values. The elements in the array are word-aligned.\n\nprimes          DCD   1,2,3,5,7,11\nmultiplesOfTwo  DCD   1,2*1,2*2,2*3,2*4,2*5,2*6\n");
                    break;
                }
                case "DCB": {
                    this.lblTitle.setText("DCB directive");
                    this.txtContent.setText("label DCD expressionlist\nlabel: Name for base address of array in memory\nexpressionlist: Comma-separated list of numerical expressions.\n                Allowed operators are +, - and *.\n                Values are truncated to bytes.\n\nDefines a byte array in memory containing the solved form of the list\nof given numerical expressions. These expressions can simply be\nimmediate values. The elements in the array are byte-aligned.\n\nprimes          DCB   1,2,3,5,7,11\nmultiplesOfTwo  DCB   1,2*1,2*2,2*3,2*4,2*5,2*6\n");
                    break;
                }
                case "EQU": {
                    this.lblTitle.setText("EQU directive");
                    this.txtContent.setText("label EQU expression\nlabel: Name of constant\nexpression: Numerical expression. Allowed operators are +, - and *\n\nDefines a pre-processor constant with the given name and the solved value\nof the numerical expression. The expression can simply be an immediate\nvalue.\n\nbaseAddress     EQU   0x1000FC80\nelementTen      EQU   4*10+baseAddress\n");
                    break;
                }
                case "FILL": {
                    this.lblTitle.setText("FILL directive");
                    this.txtContent.setText("{label} FILL expression\nlabel: Optional symbol name for filled addresses\nexpression: Numerical expression evaluating to a multiple of 4. Allowed operators are +, - and *\n\nZero-fills the given number of bytes.\n\nFILL   12\nFILL   4*3\n");
                }
            }
        }
        catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
}

