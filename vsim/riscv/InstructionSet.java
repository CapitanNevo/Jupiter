package vsim.riscv;

import vsim.utils.Message;
import java.util.Hashtable;
import java.util.Enumeration;
import java.lang.reflect.Modifier;
import vsim.riscv.instructions.Instruction;


public final class InstructionSet {

  // packages
  private static final String RTYPE   = "vsim.riscv.instructions.rtype";
  private static final String ITYPE   = "vsim.riscv.instructions.itype";
  private static final String STYPE   = "vsim.riscv.instructions.stype";
  private static final String BTYPE   = "vsim.riscv.instructions.btype";
  private static final String UTYPE   = "vsim.riscv.instructions.utype";
  private static final String JTYPE   = "vsim.riscv.instructions.jtype";
  private static final String FR4TYPE = "vsim.riscv.instructions.fr4type";
  private static final String FRTYPE  = "vsim.riscv.instructions.frtype";

  // current classes in rtype package
  private static final String[] RClasses = {
    "Add", "Sub", "Sll",
    "Slt", "Sltu", "Xor",
    "Srl", "Sra", "Or",
    "And", "Div", "Divu",
    "Mul", "Mulh", "Mulhu",
    "Mulhsu", "Rem", "Remu"
  };

  // current classes in itype package
  private static final String[] IClasses = {
    "Jalr", "Lb", "Lh",
    "Lw", "Lbu", "Lhu",
    "Addi", "Slti", "Sltiu",
    "Xori", "Ori", "Andi",
    "Slli", "Srli", "Srai",
    "ECall", "Fence", "FenceI"
  };

  // current classes in stype package
  private static final String[] SClasses = {
    "Sb", "Sh", "Sw"
  };

  // current classes in btype package
  private static final String[] BClasses = {
    "Beq", "Bge", "Bgeu",
    "Blt", "Bltu", "Bne"
  };

  // current classes in utype package
  private static final String[] UClasses = {
    "Auipc", "Lui"
  };

  // current classes in jtype package
  private static final String[] JClasses = {
    "Jal"
  };

  // current classes in frtype package
  private static final String[] FRClasses = {
    "Fmvwx", "Fmvxw", "Fcvtsw",
    "Fcvtswu", "Fcvtws", "Fcvtwus",
    "Fadds", "Fsubs", "Fmuls",
    "Fdivs", "Fsqrts", "Fsgnjs",
    "Fsgnjns", "Fsgnjxs", "Feqs",
    "Flts", "Fles", "Fclasss",
    "Fmins", "Fmaxs"
  };

  // current classes in fr4type package
  private static final String[] FR4Classes = {
    "Fmadds", "Fmsubs",
    "Fnmadds", "Fnmsubs"
  };

  // only 1 instruction set instance
  public static final InstructionSet insts = new InstructionSet();

  private Hashtable<String, Instruction> instructions;

  private InstructionSet() {
    this.instructions = new Hashtable<String, Instruction>();
    this.populate();
  }

  private void add(String[] classes, String pkg) {
    for(String className: classes) {
      String classPath = pkg + "." + className;
      try {
        Class cls = Class.forName(classPath);
        // only final classes
        if (!Instruction.class.isAssignableFrom(cls) ||
          Modifier.isAbstract(cls.getModifiers()) ||
          Modifier.isInterface(cls.getModifiers()))
          continue;
        // add this new instruction to isa
        Instruction inst = (Instruction) cls.newInstance();
        String mnemonic = inst.getMnemonic();
        if (this.instructions.containsKey(mnemonic))
          Message.warning("duplicated instruction name: '" + mnemonic + "', skip this");
        else
          this.instructions.put(mnemonic, inst);
      } catch (Exception e) {
        Message.panic("class: '" + classPath + "' could not be loaded");
      }
    }
  }

  private void populate() {
    this.add(RClasses, RTYPE);
    this.add(IClasses, ITYPE);
    this.add(SClasses, STYPE);
    this.add(BClasses, BTYPE);
    this.add(UClasses, UTYPE);
    this.add(JClasses, JTYPE);
    this.add(FRClasses, FRTYPE);
    this.add(FR4Classes, FR4TYPE);
  }

  public Instruction get(String mnemonic) {
    return this.instructions.get(mnemonic);
  }

  public int size() {
    return this.instructions.size();
  }

  @Override
  public String toString() {
    String out = "";
    String newline = System.getProperty("line.separator");
    for (Enumeration<String> e = this.instructions.keys(); e.hasMoreElements();) {
      String mnemonic = e.nextElement();
      Instruction i = this.instructions.get(mnemonic);
      out += i.toString() + newline;
    }
    return out.trim();
  }

}
