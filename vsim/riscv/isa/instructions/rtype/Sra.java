package vsim.riscv.isa.instructions.rtype;


public final class Sra extends RType {

    @Override
    public int compute(int x, int y) {
        return x >> (y & 0x1f);
    }

}